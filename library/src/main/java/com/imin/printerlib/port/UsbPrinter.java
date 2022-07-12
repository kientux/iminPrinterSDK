package com.imin.printerlib.port;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.icu.lang.UCharacter;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.imin.printerlib.connect.StarPrinterStatus;
import com.imin.printerlib.port.StarIoPort;
import com.imin.printerlib.print.PrinterStater;
import com.imin.printerlib.print.PrinterStaterFactory;
import com.imin.printerlib.util.Utils;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

public class UsbPrinter extends StarIoPort {

   private int timeout = '\uc350';
   private Context mContext;
   public UsbPrinter.USBType usbType;
   private UsbDevice selectedDevice;
   private UsbInterface interf;
   private UsbEndpoint endPointIn;
   private UsbEndpoint endPointOut;
   private UsbDeviceConnection connection;
   private UsbManager manager;
   private boolean permissionGrantedFinish;
   private volatile boolean permissionGranted;
   private boolean isVendorClass;
   private StarPrinterStatus m_statusCashe;
   private static final int Vendor_ID = 12131;
   private static final int Product_ID = 32940;
   private static final String USB_PERMISSION = "USB_PERMISSION";
   private String permission = "permission";
   private PrinterStater stater;
   private static final String TAG = "UsbPrinter";
   private static final int PID11 = 8211;
   private static final int PID13 = 8213;
   private static final int PID15 = 8215;
   private static final int VENDORID = 1305;
   private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
      public void onReceive(Context context, Intent intent) {
         String action = intent.getAction();
         if("USB_PERMISSION".equals(action)) {
            UsbDevice device = (UsbDevice)intent.getParcelableExtra("device");
            if(intent.getBooleanExtra(UsbPrinter.this.permission, false)) {
               if(device != null) {
                  UsbPrinter.this.permissionGranted = true;
               }
            } else {
               UsbPrinter.this.permissionGranted = false;
            }

            UsbPrinter.this.mContext.unregisterReceiver(UsbPrinter.this.mUsbReceiver);
         }

         UsbPrinter.this.permissionGrantedFinish = true;
      }
   };


   @RequiresApi(
      api = 24
   )
   public static boolean matchPort(String var0) {
      return UCharacter.toUpperCase(var0).startsWith("USB:")?true:(UCharacter.toUpperCase(var0).startsWith("USBVEN:")?true:UCharacter.toUpperCase(var0).startsWith("USBPRN:"));
   }

   public UsbPrinter(Context context) {
      this.mContext = context;
      this.selectedDevice = null;
   }

   @SuppressLint({"NewApi"})
   public void initPrinter(String overrideProductName) {
      this.manager = (UsbManager)this.mContext.getSystemService(Context.USB_SERVICE);
      if(this.selectedDevice == null) {
         HashMap deviceCollect = this.manager.getDeviceList();
         if(deviceCollect.isEmpty()) {
            Log.i("UsbPrinter", "Cannot find any printer  deviceCollect ------- " + deviceCollect.isEmpty());
         } else {
            Set deviceSet = deviceCollect.keySet();
            String[] devInfo = new String[deviceSet.size()];
            deviceSet.toArray(devInfo);
            this.selectedDevice = this.findPrinterDevice(devInfo, deviceCollect, overrideProductName);
            if(this.selectedDevice == null) {
               this.requestPermission(this.selectedDevice);
               Log.i("UsbPrinter", "Cannot find neostra printer");
            } else if(this.selectedDevice.getInterfaceCount() == 0) {
               Log.i("UsbPrinter", "Cannot find printer");
            } else {
               this.interf = this.selectedDevice.getInterface(0);
               int var7 = this.interf.getEndpointCount();

               for(int var8 = 0; var8 < var7; ++var8) {
                  UsbEndpoint var9 = this.interf.getEndpoint(var8);
                  if(var9.getDirection() == 128) {
                     this.endPointIn = var9;
                  } else if(var9.getDirection() == 0) {
                     this.endPointOut = var9;
                  }
               }

               if(this.endPointIn != null && this.endPointOut != null) {
                  this.requestPermission(this.selectedDevice);
                  if(!this.manager.hasPermission(this.selectedDevice)) {
                     this.selectedDevice = null;
                     Log.i("UsbPrinter", "Permission denied");
                  } else {
                     this.connection = this.manager.openDevice(this.selectedDevice);
                     if(this.connection == null) {
                        Log.i("UsbPrinter", "unable to connect to printer");
                     } else if(!this.connection.claimInterface(this.interf, true)) {
                        Log.i("UsbPrinter", "unable to claim interface");
                     } else {
                        Log.i("UsbPrinter", "init success");
                     }
                  }
               } else {
                  Toast.makeText(this.mContext, "Missing usb endpiont", 0).show();
                  Log.i("UsbPrinter", "Cannot find neostra printer");
               }
            }
         }
      }

   }

   public int getPrinterStatus() {
      return this.stater != null?this.stater.getPrinterStatus(this):-1;
   }

   private UsbDevice findPrinterDevice(String[] devInfo, HashMap usbDeviceMap, String overrideProductName) {
      for(int i = 0; i < devInfo.length; ++i) {
         UsbDevice usbDev = (UsbDevice)usbDeviceMap.get(devInfo[i]);
         UsbInterface usbInterface = usbDev.getInterface(0);
         if(usbInterface.getInterfaceClass() == 7 && this.requestPermission(usbDev) && usbDev.getInterfaceCount() > 0) {
            if (overrideProductName != null && !overrideProductName.equals(usbDev.getProductName())) {
               Log.d(TAG, "skip overrideProductName: " + overrideProductName);
               continue;
            }
            UsbDeviceConnection usbDevConnection = this.manager.openDevice(usbDev);
            usbDevConnection.claimInterface(usbInterface, true);
            byte[] lpBuf = new byte[257];
            usbDevConnection.controlTransfer(161, 0, 0, 0, lpBuf, 257, this.timeout);
            usbDevConnection.releaseInterface(usbInterface);
            usbDevConnection.close();
            this.isVendorClass = true;
            this.stater = (new PrinterStaterFactory()).createPrinterStater(usbDev.getProductId(), usbDev.getVendorId());
            return usbDev;
         }
      }

      return null;
   }

   private boolean isNumber(String var1) {
      for(int var2 = 0; var2 < var1.length(); ++var2) {
         if(!Character.isDigit(var1.charAt(var2))) {
            return false;
         }
      }

      return true;
   }

   public boolean requestPermission(UsbDevice var1) {
      if(var1 == null) {
         return false;
      } else if(this.manager.hasPermission(var1)) {
         return true;
      } else {
         this.permissionGrantedFinish = false;
         this.permissionGranted = false;
         PendingIntent var2 = PendingIntent.getBroadcast(this.mContext, 0, new Intent("USB_PERMISSION"), 0);
         IntentFilter var3 = new IntentFilter("USB_PERMISSION");
         this.mContext.registerReceiver(this.mUsbReceiver, var3);
         this.manager.requestPermission(var1, var2);
         Calendar var4 = Calendar.getInstance();
         var4.add(14, this.timeout);

         while(!this.permissionGrantedFinish && !this.manager.hasPermission(var1)) {
            try {
               Thread.sleep(100L);
            } catch (InterruptedException var7) {
               Toast.makeText(this.mContext, "Firmware check firmware", 0).show();
            }

            Calendar var6 = Calendar.getInstance();
            if(var4.before(var6)) {
               break;
            }
         }

         if(this.permissionGranted) {
            while(!this.manager.hasPermission(var1)) {
               try {
                  Thread.sleep(100L);
               } catch (InterruptedException var61) {
                  Toast.makeText(this.mContext, "Firmware check firmware", 0).show();
               }
            }
         }

         return this.manager.hasPermission(var1);
      }
   }

   public void writePort(byte[] datBuf, int size) {
      Log.d("xgh", "usbPrinter ==> " + Arrays.toString(datBuf));
      int cnt = 5;

      try {
         while(cnt > 0 && this.connection.bulkTransfer(this.endPointOut, datBuf, size, this.timeout) <= 0) {
            --cnt;
         }
      } catch (NullPointerException var5) {
         --cnt;
      }

   }

   public int writeBuffer(byte[] datBuf, int size) {
      int cnt = 5;

      try {
         while(cnt > 0) {
            int e = this.connection.bulkTransfer(this.endPointOut, datBuf, size, this.timeout);
            if(e > 0) {
               return e;
            }

            --cnt;
         }
      } catch (NullPointerException var5) {
         --cnt;
      }

      return -1;
   }

   public int readBuffer(byte[] readBuffer, int readSize) {
      if(this.endPointIn == null) {
         return -1;
      } else {
         int bulkTransferSize = this.connection.bulkTransfer(this.endPointIn, readBuffer, readSize, 1000);
         return bulkTransferSize < 0?-1:bulkTransferSize;
      }
   }

   public synchronized int writeIO(byte[] writeBuffer, int writeSize) {
      if(this.connection == null) {
         return -1;
      } else {
         int ret = this.writeBuffer(writeBuffer, writeSize);
         return ret < 0?-1:ret;
      }
   }

   public void clear() {
      if(this.isOpen()) {
         byte[] data = new byte[20];

         for(int readSize = this.connection.bulkTransfer(this.endPointIn, data, data.length, 1); readSize == 20; readSize = this.connection.bulkTransfer(this.endPointIn, data, data.length, 1)) {
            ;
         }
      }

   }

   public boolean isOpen() {
      return this.connection != null;
   }

   public int readPort(byte[] buf, int revLen, int readLen) {
      return this.connection.bulkTransfer(this.endPointOut, new byte[]{(byte)16, (byte)4, (byte)3}, 3, this.timeout) < 0?-1:this.connection.bulkTransfer(this.endPointIn, buf, revLen, 5000);
   }

   public void closeNative() {
      if(this.connection != null) {
         this.connection.releaseInterface(this.interf);
         this.connection.close();
      }

      this.selectedDevice = null;
      this.interf = null;
      this.connection = null;
   }

   public StarPrinterStatus retreiveStatus() {
      this.initPrinter(null);
      StarPrinterStatus var1 = new StarPrinterStatus();
      byte[] var2 = new byte[100];
      this.readPort(var2, 0, var2.length);
      var1.rawLength = this.readPort(var1.raw, 0, var1.raw.length);
      if(var1.rawLength >= 7) {
         Utils.BuildParsedStatus(var1);
         return var1;
      } else {
         byte[] var3 = new byte[]{(byte)27, (byte)30, (byte)97, (byte)2};
         this.writePort(var3, var3.length);

         try {
            Thread.sleep(200L);
         } catch (InterruptedException var5) {
            Toast.makeText(this.mContext, "Firmware check firmware", 0).show();
         }

         this.readPort(var2, 0, var2.length);
         var1.rawLength = this.readPort(var1.raw, 0, var1.raw.length);
         if(var1.rawLength >= 7) {
            Utils.BuildParsedStatus(var1);
         } else {
            Toast.makeText(this.mContext, "unable to read status", 0).show();
         }

         return var1;
      }
   }

   public StarPrinterStatus beginCheckedBlock() {
      StarPrinterStatus var1 = this.retreiveStatus();
      if(!var1.etbAvailable) {
         Toast.makeText(this.mContext, "Checked block is not avaible for this printer", 0).show();
      } else {
         this.m_statusCashe = (StarPrinterStatus)var1.clone();
      }

      return var1;
   }

   public StarPrinterStatus endCheckedBlock() {
      byte[] var1 = new byte[]{(byte)23};
      this.writePort(var1, var1.length);
      short var2 = (short)((this.m_statusCashe.etbCounter + 1) % 32);

      while(true) {
         StarPrinterStatus var3 = new StarPrinterStatus();

         try {
            var3 = this.retreiveStatus();
            if(var3.etbCounter == var2 || var3.offline) {
               return var3;
            }
         } catch (Exception var7) {
            ;
         }

         byte[] var4 = new byte[1];
         if(this.connection.controlTransfer(192, 1, 0, 0, var4, var4.length, this.timeout) < 0) {
            this.closeNative();
            var3.offline = true;
            return var3;
         }

         if((var4[0] & 8) == 0 || (var4[0] & 16) == 0 || (var4[0] & 32) != 0) {
            this.closeNative();
            var3.offline = true;
            return var3;
         }

         try {
            Thread.sleep(200L);
         } catch (InterruptedException var6) {
            Toast.makeText(this.mContext, "Unfinished checked block", 0).show();
         }
      }
   }

   private static enum USBType {

      PRINTER("PRINTER", 0),
      VENDOR("VENDOR", 1),
      EITHER("EITHER", 2);
      // $FF: synthetic field
      private static final UsbPrinter.USBType[] $VALUES = new UsbPrinter.USBType[]{PRINTER, VENDOR, EITHER};


      private USBType(String var1, int var2) {}

   }
}

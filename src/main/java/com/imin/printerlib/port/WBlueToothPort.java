package com.imin.printerlib.port;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import com.imin.printerlib.bean.PortInfo;
import com.imin.printerlib.connect.StarPrinterStatus;
import com.imin.printerlib.exception.StarIoPortException;
import com.imin.printerlib.port.StarIoPort;
import com.imin.printerlib.util.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.TimeoutException;

public class WBlueToothPort extends StarIoPort {

   private boolean flushAfterWrite;
   private String portName;
   private String portSettings;
   private int ioTimeout;
   private BluetoothSocket socket = null;
   private InputStream inStream;
   private OutputStream outStream;
   private final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
   private final String WoosimMacAddress = "00:15:0E";
   private static Vector bluetoothSockets = new Vector();
   private boolean useSecureSocket = true;


   public static boolean MatchPort(String var0) {
      return var0.length() < 3?false:var0.substring(0, 3).equalsIgnoreCase("BT:");
   }

   public WBlueToothPort(String var1, String var2, int var3) throws StarIoPortException {
      this.portName = var1;
      this.portSettings = var2;
      this.ioTimeout = var3;
      this.openPort();

      try {
         long var12 = System.currentTimeMillis() + (long)this.ioTimeout;

         do {
            this.outStream.write(Utils.miniPrinterStatusCommand(), 0, Utils.miniPrinterStatusCommand().length);
            byte[] var6 = new byte[100];
            int var7 = 0;

            try {
               Thread.sleep(100L);
            } catch (InterruptedException var10) {
               throw new StarIoPortException("call-version");
            }

            if(this.inStream.available() != 0) {
               var7 += this.inStream.read(var6, var7, var6.length - var7);
            }

            if(var7 > 0) {
               this.outStream.write(Utils.miniFirmwareCommand(), 0, Utils.miniFirmwareCommand().length);
               var12 = System.currentTimeMillis() + (long)this.ioTimeout;
               var6 = new byte[100];
               var7 = 0;

               while(var12 > System.currentTimeMillis()) {
                  if(this.inStream.available() != 0) {
                     var7 += this.inStream.read(var6, var7, var6.length - var7);
                     String var9 = new String(var6, 0, var7);
                     if(var9.startsWith(Utils.matchFirmware())) {
                        return;
                     }
                  }

                  try {
                     Thread.sleep(100L);
                  } catch (InterruptedException var91) {
                     throw new StarIoPortException("Firmware check firmware");
                  }
               }

               this.closeNative();
               throw new StarIoPortException("Firmware check failed");
            }
         } while(var12 >= System.currentTimeMillis());

         throw new TimeoutException();
      } catch (IOException var11) {
         this.closeNative();
         throw new StarIoPortException("Firmware check firmware");
      } catch (TimeoutException var121) {
         this.closeNative();
         throw new StarIoPortException("getPort: call-version timeout");
      }
   }

   private boolean ValidWoosimMacAddress(String var1) {
      return var1.startsWith("00:15:0E");
   }

   private void GetPairedWoosimPrinter() throws StarIoPortException {
      BluetoothAdapter var1 = BluetoothAdapter.getDefaultAdapter();
      Set var2 = var1.getBondedDevices();
      BluetoothDevice[] var3 = (BluetoothDevice[])var2.toArray(new BluetoothDevice[((Object[])Objects.requireNonNull(var2.toArray())).length]);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         String var5 = var3[var4].getAddress();
         if(this.ValidWoosimMacAddress(var5)) {
            this.portName = "BT:" + var5;
            return;
         }
      }

      throw new StarIoPortException("Cannot find bluetooth printer");
   }

   private void openPort() throws StarIoPortException {
      if(this.socket == null) {
         try {
            if(this.portSettings.contains(";")) {
               int var11 = this.portSettings.indexOf(59) + 1;
               String var13 = this.portSettings.substring(var11);
               if(var13.contains("f")) {
                  this.flushAfterWrite = true;
               }

               if(var13.contains("u")) {
                  this.useSecureSocket = false;
               }
            }

            BluetoothAdapter var131 = BluetoothAdapter.getDefaultAdapter();
            if(var131 == null) {
               throw new StarIoPortException("No bluetooth adapter found");
            }

            if(var131.getState() != 12) {
               throw new StarIoPortException("bluetooth adapter is off");
            }

            if(this.portName.length() == 3) {
               this.GetPairedWoosimPrinter();
            }

            Set var141 = var131.getBondedDevices();
            BluetoothDevice[] var3 = (BluetoothDevice[])var141.toArray(new BluetoothDevice[((Object[])Objects.requireNonNull(var141.toArray())).length]);
            BluetoothDevice var4 = null;

            int var5;
            for(var5 = 0; var5 < var3.length; ++var5) {
               if(var3[var5].getName().equals(this.portName.substring(3)) && this.ValidWoosimMacAddress(var3[var5].getAddress())) {
                  var4 = var3[var5];
                  break;
               }
            }

            if(var4 == null && BluetoothAdapter.checkBluetoothAddress(this.portName.substring(3))) {
               for(var5 = 0; var5 < var3.length; ++var5) {
                  if(var3[var5].getAddress().equals(this.portName.substring(3)) && this.ValidWoosimMacAddress(var3[var5].getAddress())) {
                     var4 = var3[var5];
                     break;
                  }
               }
            }

            if(var4 == null) {
               throw new StarIoPortException("Cannot find printer");
            }

            if(this.useSecureSocket) {
               this.socket = var4.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            } else {
               Method var14 = var4.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
               this.socket = (BluetoothSocket)var14.invoke(var4, new Object[]{UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")});
            }

            if(var131.isDiscovering()) {
               var131.cancelDiscovery();
            }

            this.socket.connect();
            this.outStream = this.socket.getOutputStream();
            this.inStream = this.socket.getInputStream();
         } catch (IOException var7) {
            this.socket = null;
            throw new StarIoPortException(var7.getMessage());
         } catch (SecurityException var8) {
            this.socket = null;
            throw new StarIoPortException(var8.getMessage());
         } catch (NoSuchMethodException var9) {
            this.socket = null;
            throw new StarIoPortException("Need android version 3.1 to use unsecure method");
         } catch (IllegalArgumentException var10) {
            this.socket = null;
            throw new StarIoPortException(var10.getMessage());
         } catch (IllegalAccessException var111) {
            this.socket = null;
            throw new StarIoPortException(var111.getMessage());
         } catch (InvocationTargetException var12) {
            this.socket = null;
            throw new StarIoPortException(var12.getMessage());
         }
      }

   }

   private StarPrinterStatus getParsedStatus() throws StarIoPortException {
      try {
         this.openPort();
         byte[] var8 = new byte[]{(byte)27, (byte)118};
         this.outStream.write(var8, 0, var8.length);
         this.outStream.flush();
         int var2 = 0;

         while(var2 < 5) {
            if(this.inStream.available() == 0) {
               ++var2;

               try {
                  Thread.sleep(1000L);
               } catch (InterruptedException var7) {
                  throw new StarIoPortException("Firmware check firmware");
               }
            } else {
               StarPrinterStatus var3 = new StarPrinterStatus();
               int var4 = this.inStream.read(var3.raw, 0, 1);
               if(var4 == 1) {
                  Utils.BuildParsedWoosimStatus(var3);
                  return var3;
               }

               ++var2;

               try {
                  Thread.sleep(1000L);
               } catch (InterruptedException var6) {
                  throw new StarIoPortException("Firmware check firmware");
               }
            }
         }

         this.closeNative();
         throw new StarIoPortException("Failed to get parsed status");
      } catch (IOException var81) {
         this.closeNative();
         throw new StarIoPortException("failed to getStatus");
      }
   }

   public void writePort(byte[] var1, int var3) throws StarIoPortException {
      try {
         this.openPort();
         this.outStream.write(var1, 0, var3);
         if(this.flushAfterWrite) {
            this.outStream.flush();
         }

      } catch (IOException var4) {
         this.closeNative();
         throw new StarIoPortException("failed to write");
      }
   }

   public int readPort(byte[] var1, int var2, int var3) throws StarIoPortException {
      try {
         this.openPort();
         if(this.inStream.available() == 0) {
            return 0;
         } else {
            int var5 = this.inStream.read(var1, var2, var3);
            if(var5 == -1) {
               var5 = 0;
            }

            return var5;
         }
      } catch (IOException var51) {
         throw new StarIoPortException("Failed to read");
      }
   }

   protected void closeNative() throws StarIoPortException {
      try {
         this.outStream.close();
         this.inStream.close();
         this.socket.close();
         this.socket = null;
      } catch (IOException var2) {
         this.socket = null;
         throw new StarIoPortException(var2.getMessage());
      }
   }

   public StarPrinterStatus retreiveStatus() throws StarIoPortException {
      return this.getParsedStatus();
   }

   public StarPrinterStatus beginCheckedBlock() throws StarIoPortException {
      throw new StarIoPortException("Unsupported Method");
   }

   public StarPrinterStatus endCheckedBlock() throws StarIoPortException {
      throw new StarIoPortException("Unsupported Method");
   }

   public static void InterruptOpen() throws StarIoPortException {
      boolean var0 = false;
      Vector var1 = bluetoothSockets;
      Vector var2 = bluetoothSockets;
      synchronized(bluetoothSockets) {
         while(bluetoothSockets.size() > 0) {
            BluetoothSocket var21 = (BluetoothSocket)bluetoothSockets.elementAt(0);
            bluetoothSockets.remove(var21);

            try {
               var21.close();
            } catch (IOException var6) {
               var0 = true;
            }
         }

         if(var0) {
            throw new StarIoPortException("Interup incomplete");
         }
      }
   }

   public static ArrayList searchPrinter() throws StarIoPortException {
      ArrayList var0 = new ArrayList();
      BluetoothAdapter var1 = BluetoothAdapter.getDefaultAdapter();
      if(var1 != null && var1.isEnabled()) {
         Set var2 = var1.getBondedDevices();
         if(var2.size() > 0) {
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               BluetoothDevice var4 = (BluetoothDevice)var3.next();
               var0.add(new PortInfo("BT:" + var4.getName(), var4.getAddress(), ""));
            }
         }
      }

      return var0;
   }

}

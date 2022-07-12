package com.imin.printerlib.port;

import com.imin.printerlib.connect.StarPrinterStatus;
import com.imin.printerlib.exception.StarIoPortException;
import com.imin.printerlib.port.StarIoPort;
import com.imin.printerlib.util.Utils;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

class WTCPPort extends StarIoPort {

   Socket portConnection;
   DataOutputStream outStream;
   DataInputStream inStream;
   String portName;
   String portSettings;
   int timeOut;
   StarPrinterStatus asbStatus;
   boolean doKeepAlive = false;
   boolean doNagel = true;
   boolean doZeroBuffer = false;
   int remainingReadDataIdx = 0;
   int remainingReadLength = 0;


   public static boolean MatchPort(String var0) {
      return var0.length() < 4?false:var0.substring(0, 4).equalsIgnoreCase("TCP:");
   }

   public WTCPPort(String var1, String var2, int var3) throws StarIoPortException {
      this.portName = var1;
      this.portSettings = var2;
      this.timeOut = var3;
      this.OpenPort();
      String[] var4 = var2.split(";");
      if(var4.length > 1) {
         this.doKeepAlive = var4[1].contains("a");
         this.doNagel = !var4[1].contains("n");
         this.doZeroBuffer = var4[1].contains("z");
      }

      try {
         long var13 = System.currentTimeMillis() + (long)this.timeOut;

         do {
            this.outStream.write(Utils.miniPrinterStatusCommand(), 0, Utils.miniPrinterStatusCommand().length);
            byte[] var7 = new byte[100];
            int var8 = 0;

            try {
               Thread.sleep(100L);
            } catch (InterruptedException var11) {
               throw new StarIoPortException("call-version");
            }

            if(this.inStream.available() != 0) {
               var8 += this.inStream.read(var7, var8, var7.length - var8);
            }

            if(var8 > 0) {
               this.outStream.write(Utils.miniFirmwareCommand(), 0, 2);
               var13 = System.currentTimeMillis() + (long)this.timeOut;
               var7 = new byte[100];
               var8 = 0;

               while(var13 > System.currentTimeMillis()) {
                  if(this.inStream.available() != 0) {
                     var8 += this.inStream.read(var7, var8, var7.length - var8);
                     String var10 = new String(var7, 0, var8);
                     if(var10.startsWith(Utils.matchFirmware())) {
                        return;
                     }
                  }

                  try {
                     Thread.sleep(100L);
                  } catch (InterruptedException var101) {
                     throw new StarIoPortException("Firmware check firmware");
                  }
               }

               this.closeNative();
               throw new StarIoPortException("Firmware check failed");
            }
         } while(var13 >= System.currentTimeMillis());

         throw new TimeoutException();
      } catch (IOException var12) {
         this.closeNative();
         throw new StarIoPortException("Firmware check firmware");
      } catch (TimeoutException var131) {
         this.closeNative();
         throw new StarIoPortException("getPort: call-version timeout");
      }
   }

   private synchronized void OpenPort() throws StarIoPortException {
      String var1 = this.portName.substring(4);

      try {
         InetSocketAddress var4 = new InetSocketAddress(var1, 9100);
         this.portConnection = new Socket();
         this.portConnection.setSoTimeout(this.timeOut);
         this.portConnection.connect(var4, this.timeOut);
         this.portConnection.setSoTimeout(this.timeOut);
         this.portConnection.setKeepAlive(this.doKeepAlive);
         this.portConnection.setTcpNoDelay(this.doNagel);
         this.outStream = new DataOutputStream(this.portConnection.getOutputStream());
         this.inStream = new DataInputStream(this.portConnection.getInputStream());
      } catch (UnknownHostException var3) {
         throw new StarIoPortException("Cannot connect to printer");
      } catch (IOException var41) {
         throw new StarIoPortException(var41.getMessage());
      }
   }

   public synchronized String getPortName() {
      return this.portName;
   }

   public synchronized String getPortSettings() {
      return this.portSettings;
   }

   public void writePort(byte[] var1, int var3) throws StarIoPortException {
      try {
         if(!this.portConnection.isConnected()) {
            this.OpenPort();
         }

         this.outStream.write(var1, 0, var3);
      } catch (IOException var4) {
         throw new StarIoPortException("Failed to write");
      }
   }

   public int readPort(byte[] var1, int var2, int var3) throws StarIoPortException {
      try {
         if(!this.portConnection.isConnected()) {
            this.OpenPort();
         }

         int var5 = this.inStream.read(var1, var2, var3);
         if(var5 == -1) {
            var5 = 0;
         }

         return var5;
      } catch (IOException var51) {
         throw new StarIoPortException("Failed to read");
      }
   }

   protected void closeNative() throws StarIoPortException {
      try {
         this.inStream.close();
         this.outStream.close();
         this.portConnection.close();
      } catch (IOException var2) {
         throw new StarIoPortException(var2.getMessage());
      }
   }

   public StarPrinterStatus retreiveStatus() throws StarIoPortException {
      return this.getParsedStatus();
   }

   private StarPrinterStatus getParsedStatus() throws StarIoPortException {
      try {
         if(!this.portConnection.isConnected()) {
            this.OpenPort();
         }

         int var8 = 0;

         while(var8 < 5) {
            byte[] var2 = new byte[]{(byte)27, (byte)118};
            this.outStream.write(var2, 0, var2.length);
            if(this.inStream.available() == 0) {
               ++var8;

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

               ++var8;

               try {
                  Thread.sleep(1000L);
               } catch (InterruptedException var6) {
                  throw new StarIoPortException("Firmware check firmware");
               }
            }
         }

         throw new StarIoPortException("Failed to get parsed status");
      } catch (IOException var81) {
         throw new StarIoPortException("Failed to get parsed status");
      }
   }

   public StarPrinterStatus beginCheckedBlock() throws StarIoPortException {
      throw new StarIoPortException("Unsupported Method");
   }

   public StarPrinterStatus endCheckedBlock() throws StarIoPortException {
      throw new StarIoPortException("Failed to get unsupported");
   }
}

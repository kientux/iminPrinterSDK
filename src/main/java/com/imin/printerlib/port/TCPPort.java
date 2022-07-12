package com.imin.printerlib.port;

import com.imin.printerlib.bean.PortInfo;
import com.imin.printerlib.connect.StarPrinterStatus;
import com.imin.printerlib.exception.NoReturnException;
import com.imin.printerlib.exception.StarIoPortException;
import com.imin.printerlib.port.StarIoPort;
import com.imin.printerlib.util.Utils;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

class TCPPort extends StarIoPort {

   static final int MIN_STAR_ASB_STATUS_LENGTH = 7;
   Socket portConnection;
   DataOutputStream outStream;
   DataInputStream inStream;
   String portName;
   String portSettings;
   int timeOut;
   StarPrinterStatus asbStatus;
   int wNumber = 0;
   boolean doKeepAlive = false;
   boolean doNagel = true;
   boolean doZeroBuffer = false;
   boolean isAirPortSupported = false;
   int remainingReadDataIdx = 0;
   int remainingReadLength = 0;
   byte[] remainingReadData;


   public static boolean MatchPort(String var0) {
      return var0.length() < 4?false:var0.substring(0, 4).equalsIgnoreCase("TCP:");
   }

   public TCPPort(String var1, String var2, int var3) throws StarIoPortException {
      this.portName = var1;
      this.portSettings = var2;
      this.timeOut = var3;
      this.doKeepAlive = var2.contains("a");
      this.doNagel = !var2.contains("n");
      this.doZeroBuffer = var2.contains("z");
      int var4 = var2.indexOf("910");
      if(var4 != -1 && var2.length() >= var4 + 4) {
         String var5 = var2.substring(var4, 4);

         try {
            this.wNumber = Integer.parseInt(var5);
            this.isAirPortSupported = true;
         } catch (NumberFormatException var7) {
            this.wNumber = 0;
         }
      }

      this.asbStatus = new StarPrinterStatus();
      this.asbStatus.offline = true;
      this.OpenPort();
   }

   private synchronized void FindStatusPortNumber(String var1) throws StarIoPortException {
      InetAddress var2 = null;

      try {
         var2 = InetAddress.getByName(var1);
      } catch (Exception var23) {
         throw new StarIoPortException("Failed to find printer");
      }

      short var3 = 22222;
      short var4 = 512;
      byte[] var5 = new byte[var4];
      byte[] var6 = new byte[]{(byte)83, (byte)84, (byte)82, (byte)95, (byte)66, (byte)67, (byte)65, (byte)83, (byte)84, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)82, (byte)81, (byte)49, (byte)46, (byte)48, (byte)46, (byte)48, (byte)0, (byte)0, (byte)28, (byte)100, (byte)49};
      long var7 = System.currentTimeMillis() + (long)this.timeOut;

      do {
         DatagramPacket var9 = new DatagramPacket(var6, var6.length, var2, var3);
         DatagramSocket var10 = null;

         try {
            var10 = new DatagramSocket(var3);
            var10.setSoTimeout(2000);
            var10.send(var9);
         } catch (SocketException var24) {
            if(var10 != null) {
               var10.close();
            }

            this.wNumber = 9100;
            return;
         } catch (IOException var25) {
            if(var10 != null) {
               var10.close();
            }

            this.wNumber = 9100;
            return;
         }

         DatagramPacket var11 = new DatagramPacket(var5, var4);

         try {
            var10.receive(var11);
            byte[] var27 = var11.getData();

            int var13;
            for(var13 = 0; var13 < 100 && var27[36 + var13] != 0; ++var13) {
               ;
            }

            byte[] var14 = new byte[var13];
            System.arraycopy(var27, 36, var14, 0, var13);
            String var15 = new String(var14);
            if(var15.equals("IFBD-HE07/08")) {
               this.wNumber = 9101;
            } else {
               this.wNumber = 9100;
            }
            break;
         } catch (SocketTimeoutException var26) {
            continue;
         } catch (IOException var271) {
            this.wNumber = 9100;
         } finally {
            var10.close();
         }

         return;
      } while(var7 < System.currentTimeMillis());

   }

   private synchronized void OpenPort() throws StarIoPortException {
      String var1 = this.portName.substring(4);

      try {
         int var5 = this.wNumber == 0?9100:this.wNumber;
         InetSocketAddress var3 = new InetSocketAddress(var1, var5);
         this.portConnection = new Socket();
         this.portConnection.setSoTimeout(this.timeOut);
         this.portConnection.setKeepAlive(this.doKeepAlive);
         this.portConnection.setTcpNoDelay(this.doNagel);
         this.portConnection.connect(var3, this.timeOut);
         this.outStream = new DataOutputStream(this.portConnection.getOutputStream());
         this.inStream = new DataInputStream(this.portConnection.getInputStream());
         if(this.wNumber == 0) {
            this.FindStatusPortNumber(var1);
         } else if(this.wNumber < 9100 || this.wNumber > 9109) {
            throw new StarIoPortException("Not supported port number");
         }

      } catch (UnknownHostException var4) {
         throw new StarIoPortException("Cannot connect to printer");
      } catch (IOException var51) {
         throw new StarIoPortException(var51.getMessage());
      }
   }

   public synchronized String getPortName() {
      return this.portName;
   }

   public synchronized String getPortSettings() {
      return this.portSettings;
   }

   public synchronized void writePort(byte[] var1, int var3) throws StarIoPortException {
      try {
         if(!this.portConnection.isConnected()) {
            this.OpenPort();
         }

         this.outStream.write(var1, 0, var3);
      } catch (IOException var4) {
         throw new StarIoPortException("Failed to write");
      }
   }

   public synchronized int readPort(byte[] var1, int var2, int var3) throws StarIoPortException {
      byte var4 = 0;
      byte var5 = 0;

      try {
         if(!this.portConnection.isConnected()) {
            this.OpenPort();
         }

         while(true) {
            if(var3 > 0) {
               int var10;
               if(this.remainingReadLength > 0) {
                  var10 = this.remainingReadLength < var3?this.remainingReadLength:var3;

                  for(int var12 = 0; var12 < var10; ++var12) {
                     var1[var12 + var5] = this.remainingReadData[this.remainingReadDataIdx + var12];
                  }

                  int var10000 = var5 + var10;
                  var10000 = var3 - var10;
                  int var13 = var4 + var10;
                  this.remainingReadDataIdx += var10;
                  this.remainingReadLength -= var10;
                  if(this.remainingReadLength == 0) {
                     this.remainingReadData = null;
                  }

                  return var13;
               }

               boolean var6 = false;
               byte[] var7 = new byte[200];
               if(this.inStream.available() == 0) {
                  return var4;
               }

               var10 = this.inStream.read(var7, 0, 7);
               if(!this.TCPReadPortIsStarAsbStatus(var7, var10)) {
                  this.TCPReadPortSubParsingEscposFormat(var7, var10);
                  continue;
               }

               try {
                  this.TCPReadPortSubParsingStarFormat(var7);
                  continue;
               } catch (NoReturnException var101) {
                  var10 = var101.AmountRead;
               }
            }

            return var4;
         }
      } catch (IOException var11) {
         throw new StarIoPortException("Failed to read");
      }
   }

   public synchronized StarPrinterStatus retreiveStatus() throws StarIoPortException {
      return this.getParsedStatus(false);
   }

   private synchronized StarPrinterStatus getParsedStatus(boolean var1) throws StarIoPortException {
      StarPrinterStatus var2 = new StarPrinterStatus();
      if(!this.portConnection.isConnected()) {
         this.OpenPort();
      }

      try {
         int var9;
         while(this.inStream.available() != 0) {
            boolean var12 = false;
            if(this.wNumber == 9101 && !this.isAirPortSupported) {
               this.inStream.read(var2.raw, 0, var2.raw.length);
            } else {
               var12 = false;
               int var11 = this.inStream.read(var2.raw, 0, 7);
               if(var11 != 7) {
                  this.closeNative();
                  throw new StarIoPortException("Failed to get status");
               }

               var2.rawLength = 7;
               int var16 = Utils.calculatedStatusLength(var2.raw[0]);
               var11 = this.inStream.read(var2.raw, var2.rawLength, var16 - 7);
               if(var11 != var16 - 7) {
                  this.closeNative();
                  throw new StarIoPortException("Failed to get status");
               }

               var2.rawLength += var11;
               if((var2.raw[1] & 128) == 128) {
                  byte[] var17 = new byte[100];
                  var11 = this.inStream.read(var17, 0, 2);
                  if(var11 != 2) {
                     this.closeNative();
                     throw new StarIoPortException("Failed to get status");
                  }

                  int var18 = var17[1] & 255;
                  int var19 = var17[0] & 255;
                  var19 <<= 8;
                  var9 = var18 + var19;
                  if(var9 > 0) {
                     var11 = this.inStream.read(var17, 0, var9);
                     if(var11 != var9) {
                        this.closeNative();
                        throw new StarIoPortException("Failed to get status");
                     }
                  }
               }

               Utils.BuildParsedStatus(var2);
               this.asbStatus = (StarPrinterStatus)var2.clone();
            }
         }

         if(this.wNumber == 9101 && !this.isAirPortSupported) {
            String var122 = this.portName.substring(4);

            try {
               InetSocketAddress var112 = new InetSocketAddress(var122, 9101);
               Socket var161 = new Socket();
               var161.setSoTimeout(this.timeOut);
               var161.connect(var112, this.timeOut);
               DataOutputStream var171 = new DataOutputStream(var161.getOutputStream());
               DataInputStream var181 = new DataInputStream(var161.getInputStream());
               var171.write(new byte[]{(byte)51}, 0, 1);
               byte[] var191 = new byte[264];
               var9 = var181.read(var191, 0, var191.length);
               if(var9 >= 7) {
                  if((var191[0] & 17) == 1 && (var191[1] & 145) == 128 && (var191[2] & 145) == 0 && (var191[3] & 145) == 0 && (var191[4] & 145) == 0 && (var191[5] & 145) == 0 && (var191[6] & 145) == 0) {
                     if(var9 > var2.raw.length) {
                        var2.rawLength = var2.raw.length;
                     } else {
                        var2.rawLength = var9;
                     }

                     System.arraycopy(var191, 0, var2.raw, 0, var2.rawLength);
                     Utils.BuildParsedStatus(var2);
                     this.asbStatus = (StarPrinterStatus)var2.clone();
                  }
               } else {
                  if(var9 != 4) {
                     throw new StarIoPortException("Failed to read status");
                  }

                  if((var191[0] & 144) == 16 && (var191[1] & 144) == 0 && (var191[2] & 144) == 0 && (var191[3] & 144) == 0) {
                     this.asbStatus.offline = false;
                  }

                  if(!var1) {
                     throw new StarIoPortException("Failed to read status");
                  }
               }

               var171.close();
               var181.close();
               var161.close();
            } catch (UnknownHostException var10) {
               throw new StarIoPortException("Cannot connect to printer");
            } catch (IOException var111) {
               throw new StarIoPortException(var111.getMessage());
            }
         }
      } catch (IOException var121) {
         throw new StarIoPortException("Failed to get status");
      }

      return this.asbStatus;
   }

   private boolean TCPReadPortIsStarAsbStatus(byte[] var1, int var2) {
      return var2 != 7?false:(var1[0] & 17) == 1 && (var1[1] & 145) == 128 && (var1[2] & 145) == 0 && (var1[3] & 145) == 0 && (var1[4] & 145) == 0 && (var1[5] & 145) == 0 && (var1[6] & 145) == 0;
   }

   private void TCPReadPortSubParsingEscposFormat(byte[] var1, int var2) throws IOException {
      int var3 = var2;
      byte[] var4 = new byte[200];
      int var5 = 0;
      if(var2 == 7) {
         var5 = this.inStream.read(var4, 0, 200 - var2);
      }

      int var6 = var2 + var5;
      this.remainingReadData = new byte[var6];

      int var7;
      for(var7 = 0; var7 < var3; ++var7) {
         this.remainingReadData[var7] = var1[var7];
      }

      for(var7 = 0; var7 < var5; ++var7) {
         this.remainingReadData[var7 + var3] = var4[var7];
      }

      this.remainingReadLength = var6;
      this.remainingReadDataIdx = 0;
   }

   private int TCPReadPortSubParsingStarFormat(byte[] var1) throws IOException, NoReturnException {
      int var2 = Utils.calculatedStatusLength(var1[0]);
      int var3 = this.inStream.read(var1, 7, var2 - 7);
      NoReturnException var12;
      if(var3 != var2 - 7) {
         var12 = new NoReturnException("ABS not found");
         var12.AmountRead = var3;
         throw var12;
      } else if((var1[1] & 128) != 128) {
         return var3;
      } else {
         var3 = this.inStream.read(var1, 0, 2);
         if(var3 != 2) {
            var12 = new NoReturnException("ABS not found");
            var12.AmountRead = var3;
            throw var12;
         } else {
            int var4 = var1[1] & 255;
            int var5 = var1[0] & 255;
            var5 <<= 8;
            int var6 = var4 + var5;
            if(var6 == 0) {
               return var3;
            } else {
               var3 = this.inStream.read(var1, 0, var6);
               if(var3 != var6) {
                  NoReturnException var13 = new NoReturnException("ABS not found");
                  var13.AmountRead = var3;
                  throw var13;
               } else {
                  byte var7;
                  if(var1[0] == 48 && var1[1] == 49) {
                     var7 = 4;
                  } else if(var1[0] == 48 && var1[1] == 50) {
                     var7 = 4;
                  } else if(var1[0] == 49 && var1[1] == 48) {
                     var7 = 6;
                  } else if(var1[0] == 49 && var1[1] == 49) {
                     var7 = 4;
                  } else if(var1[0] == 49 && var1[1] == 50) {
                     var7 = 6;
                  } else if(var1[0] == 49 && var1[1] == 51) {
                     var7 = 6;
                  } else if(var1[0] == 49 && var1[1] == 52) {
                     var7 = 5;
                  } else if(var1[0] == 49 && var1[1] == 53) {
                     var7 = 6;
                  } else if(var1[0] == 49 && var1[1] == 54) {
                     var7 = 4;
                  } else if(var1[0] == 49 && var1[1] == 55) {
                     var7 = 6;
                  } else if(var1[0] == 49 && var1[1] == 56) {
                     var7 = 6;
                  } else if(var1[0] == 49 && var1[1] == 57) {
                     var7 = 4;
                  } else if(var1[0] == 50 && var1[1] == 49) {
                     var7 = 6;
                  } else if(var1[0] == 50 && var1[1] == 50) {
                     var7 = 6;
                  } else if(var1[0] == 53 && var1[1] == 48) {
                     var7 = 4;
                  } else if(var1[0] == 53 && var1[1] == 49) {
                     var7 = 6;
                  } else if(var1[0] == 54 && var1[1] == 49) {
                     var7 = 6;
                  } else if(var1[0] == 54 && var1[1] == 50) {
                     var7 = 6;
                  } else {
                     if(var1[0] != 65 || var1[1] != 48) {
                        return var3;
                     }

                     var7 = 6;
                  }

                  int var8 = var1[var7 + 1] & 255;
                  int var9 = var1[var7 + 0] & 255;
                  var9 <<= 8;
                  int var10 = var8 + var9;
                  this.remainingReadData = new byte[var10];

                  for(int var11 = 0; var11 < var10; ++var11) {
                     this.remainingReadData[var11] = var1[var11 + var7 + 2];
                  }

                  this.remainingReadLength = var10;
                  this.remainingReadDataIdx = 0;
                  return var3;
               }
            }
         }
      }
   }

   protected synchronized void closeNative() throws StarIoPortException {
      try {
         this.inStream.close();
         this.outStream.close();
         this.portConnection.close();
      } catch (IOException var2) {
         throw new StarIoPortException(var2.getMessage());
      }
   }

   public synchronized StarPrinterStatus beginCheckedBlock() throws StarIoPortException {
      StarPrinterStatus var1 = this.retreiveStatus();
      if(var1.offline) {
         throw new StarIoPortException("Printer is off line");
      } else {
         this.writePort(Utils.buildCmdClear(), 2503);
         return var1;
      }
   }

   public StarPrinterStatus endCheckedBlock() throws StarIoPortException {
      try {
         byte[] var5 = new byte[]{(byte)23, (byte)23};
         this.outStream.write(var5);

         while(true) {
            StarPrinterStatus var2 = this.getParsedStatus(false);
            if(var2.offline) {
               return var2;
            }

            if(var2.etbCounter == 2) {
               return var2;
            }

            try {
               Thread.sleep(200L);
            } catch (InterruptedException var4) {
               throw new StarIoPortException("Firmware check firmware");
            }
         }
      } catch (IOException var51) {
         throw new StarIoPortException("can not write to printer");
      }
   }

   public static ArrayList searchPrinter() throws StarIoPortException {
      byte[] var0 = new byte[]{(byte)83, (byte)84, (byte)82, (byte)95, (byte)66, (byte)67, (byte)65, (byte)83, (byte)84, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)0, (byte)82, (byte)81, (byte)49, (byte)46, (byte)48, (byte)46, (byte)48, (byte)0, (byte)0, (byte)28, (byte)100, (byte)49};
      ArrayList var1 = new ArrayList();
      DatagramSocket var2 = null;

      try {
         InetAddress var19 = null;
         short var4 = 22222;
         var2 = new DatagramSocket(var4);
         var19 = InetAddress.getByName("255.255.255.255");
         DatagramPacket var5 = new DatagramPacket(var0, var0.length, var19, var4);
         var2.send(var5);

         while(true) {
            byte[] var7;
            String var9;
            String var10;
            do {
               short var11 = 1024;
               var7 = new byte[var11];
               DatagramPacket var12 = new DatagramPacket(var7, var11);
               var2.setSoTimeout(1000);
               var2.receive(var12);
               var9 = "";
               var9 = Integer.toString(var7[88] & 255) + ".";
               var9 = var9 + Integer.toString(var7[89] & 255) + ".";
               var9 = var9 + Integer.toString(var7[90] & 255) + ".";
               var9 = var9 + Integer.toString(var7[91] & 255);
               var10 = "TCP:" + var9;
            } while(var9.equals("0.0.0.0"));

            String var191 = "";
            var191 = String.format("%1$02x", new Object[]{Integer.valueOf(var7[78] & 255)}) + ":";
            var191 = var191 + String.format("%1$02x", new Object[]{Integer.valueOf(var7[79] & 255)}) + ":";
            var191 = var191 + String.format("%1$02x", new Object[]{Integer.valueOf(var7[80] & 255)}) + ":";
            var191 = var191 + String.format("%1$02x", new Object[]{Integer.valueOf(var7[81] & 255)}) + ":";
            var191 = var191 + String.format("%1$02x", new Object[]{Integer.valueOf(var7[82] & 255)}) + ":";
            var191 = var191 + String.format("%1$02x", new Object[]{Integer.valueOf(var7[83] & 255)});

            int var20;
            for(var20 = 0; var20 < 64 && var7[204 + var20] != 0; ++var20) {
               ;
            }

            String var13 = new String(var7, 204, var20);
            var1.add(new PortInfo(var10, var191, var13));
         }
      } catch (SocketTimeoutException var16) {
         ;
      } catch (Exception var17) {
         throw new StarIoPortException("Search printer failure.");
      } finally {
         if(var2 != null) {
            var2.close();
         }

      }

      return var1;
   }
}

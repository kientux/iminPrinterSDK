package com.imin.printerlib.util;

import android.graphics.Bitmap;
import android.icu.lang.UCharacter;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.imin.printerlib.connect.StarPrinterStatus;
import com.imin.printerlib.port.UsbPrinter;
import java.util.ArrayList;
import java.util.Arrays;

public class Utils {

   public static final int CMD_CLEAR_NULL_SIZE = 2496;
   public static final int CMD_CLEAR_SIZE = 2503;
   public static final byte[] cmdClear = new byte[]{(byte)27, (byte)66, (byte)27, (byte)42, (byte)114, (byte)66, (byte)0};


   public static byte[] buildCmdClear() {
      byte[] var0 = new byte[2503];

      int var1;
      for(var1 = 0; var1 < var0.length; ++var1) {
         var0[var1] = 0;
      }

      for(var1 = 0; var1 < cmdClear.length; ++var1) {
         var0[var1] = cmdClear[var1];
      }

      return var0;
   }

   public static int calculatedStatusLength(byte var0) {
      switch(var0) {
      case 15:
         return 7;
      case 16:
      case 17:
      case 18:
      case 19:
      case 20:
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 26:
      case 27:
      case 28:
      case 29:
      case 30:
      case 31:
      case 32:
      case 34:
      case 36:
      case 38:
      case 40:
      case 42:
      case 44:
      case 46:
      default:
         return 0;
      case 33:
         return 8;
      case 35:
         return 9;
      case 37:
         return 10;
      case 39:
         return 11;
      case 41:
         return 12;
      case 43:
         return 13;
      case 45:
         return 14;
      case 47:
         return 15;
      }
   }

   public static void BuildParsedWoosimStatus(StarPrinterStatus var0) {
      var0.rawLength = 1;
      var0.blackMarkError = false;
      var0.compulsionSwitch = false;
      var0.coverOpen = false;
      var0.cutterError = false;
      var0.cutterError = false;
      var0.etbAvailable = false;
      var0.etbCounter = 0;
      var0.headThermistorError = false;
      var0.headUpError = false;
      var0.mechError = false;
      var0.offline = false;
      var0.overTemp = false;
      var0.pageModeCmdError = false;
      var0.peelerPaperPresent = false;
      var0.presenterPaperJamError = false;
      var0.presenterPaperPresent = false;
      var0.presenterState = 0;
      var0.receiptBlackMarkDetection = false;
      var0.receiptPaperEmpty = false;
      var0.receiptPaperNearEmptyInner = false;
      var0.receiptPaperNearEmptyOuter = false;
      var0.receiveBufferOverflow = false;
      var0.slipBOF = false;
      var0.slipCOF = false;
      var0.slipPaperPresent = false;
      var0.stackerFull = false;
      var0.unrecoverableError = false;
      var0.validationPaperPresent = false;
      var0.voltageError = false;
      if((var0.raw[0] & 1) == 1) {
         var0.offline = true;
         var0.receiptPaperEmpty = true;
      }

      if((var0.raw[0] & 4) == 4) {
         var0.offline = true;
         var0.receiptBlackMarkDetection = true;
      }

      if((var0.raw[0] & 2) == 2) {
         var0.offline = true;
         var0.coverOpen = true;
      }

   }

   public static void BuildParsedStatus(StarPrinterStatus var0) {
      var0.coverOpen = (var0.raw[2] & 32) != 0;
      var0.offline = (var0.raw[2] & 8) != 0;
      var0.compulsionSwitch = (var0.raw[2] & 4) != 0;
      var0.overTemp = (var0.raw[3] & 64) != 0;
      var0.unrecoverableError = (var0.raw[3] & 32) != 0;
      var0.cutterError = (var0.raw[3] & 8) != 0;
      var0.mechError = (var0.raw[3] & 4) != 0;
      var0.headThermistorError = (var0.raw[3] & 4) != 0;
      var0.receiveBufferOverflow = (var0.raw[4] & 64) != 0;
      var0.pageModeCmdError = (var0.raw[4] & 32) != 0;
      var0.blackMarkError = (var0.raw[4] & 8) != 0;
      var0.presenterPaperJamError = (var0.raw[4] & 4) != 0;
      var0.headUpError = (var0.raw[4] & 2) != 0;
      var0.voltageError = (var0.raw[4] & 2) != 0;
      var0.receiptBlackMarkDetection = (var0.raw[5] & 32) != 0;
      var0.receiptPaperEmpty = (var0.raw[5] & 8) != 0;
      var0.receiptPaperNearEmptyInner = (var0.raw[5] & 4) != 0;
      var0.receiptPaperNearEmptyOuter = (var0.raw[5] & 2) != 0;
      var0.presenterPaperPresent = (var0.raw[6] & 2) != 0;
      var0.peelerPaperPresent = (var0.raw[6] & 2) != 0;
      var0.stackerFull = (var0.raw[6] & 2) != 0;
      var0.slipTOF = (var0.raw[6] & 2) == 0;
      var0.slipCOF = (var0.raw[6] & 4) == 0;
      var0.slipBOF = (var0.raw[6] & 8) == 0;
      if((var0.raw[6] & 64) == 0 && (var0.raw[6] & 32) == 0) {
         var0.slipPaperPresent = true;
         var0.validationPaperPresent = false;
      } else if((var0.raw[6] & 64) == 0 && (var0.raw[6] & 32) != 0) {
         var0.slipPaperPresent = true;
         var0.validationPaperPresent = false;
      } else if((var0.raw[6] & 64) != 0 && (var0.raw[6] & 32) == 0) {
         var0.slipPaperPresent = false;
         var0.validationPaperPresent = true;
      } else if((var0.raw[6] & 64) != 0 && (var0.raw[6] & 32) != 0) {
         var0.slipPaperPresent = false;
         var0.validationPaperPresent = false;
      }

      var0.etbAvailable = var0.rawLength >= 9;
      var0.etbCounter = (var0.raw[7] & 64) >> 2 | (var0.raw[7] & 32) >> 2 | (var0.raw[7] & 8) >> 1 | (var0.raw[7] & 4) >> 1 | (var0.raw[7] & 2) >> 1;
      var0.presenterState = (var0.raw[8] & 8) >> 1 | (var0.raw[8] & 4) >> 1 | (var0.raw[8] & 2) >> 1;
   }

   public static byte[] miniFirmwareCommand() {
      return new byte[]{(byte)27, (byte)29};
   }

   public static String matchFirmware() {
      return "SM-";
   }

   public static byte[] miniPrinterStatusCommand() {
      return new byte[]{(byte)16, (byte)4, (byte)4};
   }

   @RequiresApi(
      api = 24
   )
   public static String Byte2Hex(Byte var0) {
      return UCharacter.toUpperCase(String.format("%02x", new Object[]{var0}));
   }

   public static void sendCommonCmd(UsbPrinter usbPrinter, int ... cmd) {
      byte[] bytes = new byte[cmd.length];

      for(int i = 0; i < cmd.length; ++i) {
         bytes[i] = (byte)cmd[i];
      }

      Log.d("xgh", "usbPrinter ==> " + Arrays.toString(bytes));
      usbPrinter.writePort(bytes, cmd.length);
   }

   public static int readCommonCmd(UsbPrinter usbPrinter, byte[] readBuf, int readLen) {
      return usbPrinter.readPort(readBuf, readLen, readBuf.length);
   }

   public static int[] getPixelsByBitmap(Bitmap bm) {
      int width = bm.getWidth();
      int height = bm.getHeight();
      int iDataLen = width * height;
      int[] pixels = new int[iDataLen];
      bm.getPixels(pixels, 0, width, 0, 0, width, height);
      return pixels;
   }

   public static byte[] draw2PxPoint(Bitmap bit) {
      int k = bit.getHeight() / 3 >> 3;
      byte[] data = new byte[k * 3 * bit.getWidth()];
      int index = 0;

      for(int j = 0; j < k; ++j) {
         for(int i = 0; i < bit.getWidth(); ++i) {
            for(int m = 0; m < 3; ++m) {
               for(int n = 0; n < 8; ++n) {
                  byte b = px2Byte(i, j * 24 + m * 8 + n, bit);
                  data[index] = (byte)(data[index] + data[index] + b);
               }

               ++index;
            }
         }
      }

      return data;
   }

   public static byte[] getBMPImageFileByte(int[] bmpBytes, int width, int height) {
      int bitWidth = width / 8;
      if((width &= 7) > 0) {
         ++bitWidth;
      }

      byte[] mBitBytes = new byte[bitWidth * height];
      int mBitIndex = 0;
      int index = 0;

      for(int h = 0; h < height; ++h) {
         int colorValue;
         int w;
         for(w = 0; w < bitWidth - 1; ++w) {
            colorValue = 0;
            if(bmpBytes[index++] < -1) {
               colorValue += 128;
            }

            if(bmpBytes[index++] < -1) {
               colorValue += 64;
            }

            if(bmpBytes[index++] < -1) {
               colorValue += 32;
            }

            if(bmpBytes[index++] < -1) {
               colorValue += 16;
            }

            if(bmpBytes[index++] < -1) {
               colorValue += 8;
            }

            if(bmpBytes[index++] < -1) {
               colorValue += 4;
            }

            if(bmpBytes[index++] < -1) {
               colorValue += 2;
            }

            if(bmpBytes[index++] < -1) {
               ++colorValue;
            }

            mBitBytes[mBitIndex++] = (byte)colorValue;
         }

         colorValue = 0;
         if(width == 0) {
            for(w = 8; w > width; --w) {
               if(bmpBytes[index++] < -1) {
                  colorValue += 1 << w;
               }
            }
         } else {
            for(w = 0; w < width; ++w) {
               if(bmpBytes[index++] < -1) {
                  colorValue += 1 << 8 - w;
               }
            }
         }

         mBitBytes[mBitIndex++] = (byte)colorValue;
      }

      return mBitBytes;
   }

   public static byte px2Byte(int x, int y, Bitmap bit) {
      int pixel = bit.getPixel(x, y);
      int red = (pixel & 16711680) >> 16;
      int green = (pixel & '\uff00') >> 8;
      int blue = pixel & 255;
      int gray = RGB2Gray(red, green, blue);
      byte b;
      if(gray < 128) {
         b = 1;
      } else {
         b = 0;
      }

      return b;
   }

   private static int RGB2Gray(int r, int g, int b) {
      int gray = (int)(0.299D * (double)r + 0.587D * (double)g + 0.114D * (double)b);
      return gray;
   }

   public static ArrayList devicePortraitList() {
      ArrayList devicesList = new ArrayList();
      devicesList.add("M2-202");
      devicesList.add("M2-203");
      devicesList.add("M2-Pro");
      devicesList.add("M2 Max");
      return devicesList;
   }

   public static ArrayList spiDevicesList() {
      ArrayList devicesList = new ArrayList();
      devicesList.add("M2-202");
      devicesList.add("M2-203");
      devicesList.add("M2-Pro");
      return devicesList;
   }

   public static ArrayList usbDevicesList() {
      ArrayList devicesList = new ArrayList();
      devicesList.add("S1-701");
      devicesList.add("S1-702");
      devicesList.add("D1p-601");
      devicesList.add("D1p-602");
      devicesList.add("D1p-603");
      devicesList.add("D1p-604");
      devicesList.add("D1w-701");
      devicesList.add("D1w-702");
      devicesList.add("D1w-703");
      devicesList.add("D1w-704");
      devicesList.add("D4-501");
      devicesList.add("D4-502");
      devicesList.add("D4-503");
      devicesList.add("D4-504");
      devicesList.add("D4-505");
      devicesList.add("M2-Max");
      return devicesList;
   }

}

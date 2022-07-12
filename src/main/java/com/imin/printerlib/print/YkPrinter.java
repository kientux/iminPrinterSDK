package com.imin.printerlib.print;

import android.util.Log;
import com.imin.printerlib.port.UsbPrinter;
import com.imin.printerlib.print.PrinterStater;

public class YkPrinter implements PrinterStater {

   private static final String TAG = "YkPrinter";


   public int getPrinterStatus(UsbPrinter usbPrinter) {
      usbPrinter.clear();
      byte[] statusD = new byte[4];
      byte[] mCmd = new byte[]{(byte)16, (byte)4, (byte)1, (byte)16, (byte)4, (byte)2, (byte)16, (byte)4, (byte)3, (byte)16, (byte)4, (byte)4};
      byte writeSize = 12;
      if(usbPrinter.writeBuffer(mCmd, writeSize) < 0) {
         return -1;
      } else if(usbPrinter.readBuffer(statusD, 4) < 0) {
         return -1;
      } else {
         int getStatus = 0;

         try {
            getStatus = this.parseStatus(statusD);
         } catch (Exception var8) {
            var8.printStackTrace();
         }

         int yk_status = getStatusDescribe(getStatus);
         int common_status = this.changeCommonStatus(yk_status);
         Log.i("YkPrinter", "yk_status:" + yk_status + " common_status:" + common_status);
         return common_status;
      }
   }

   public int parseStatus(byte[] mCmd) {
      int nStatus = 0;
      if(mCmd[0] != 22 && mCmd[0] != 8) {
         if((mCmd[0] & 8) > 0) {
            nStatus |= 1;
         }

         if((mCmd[0] & 64) > 0) {
            nStatus |= 2;
         }
      }

      if(mCmd[1] != 18 && mCmd[1] != 0) {
         if((mCmd[1] & 64) > 0) {
            nStatus |= 4;
         }

         if((mCmd[1] & 32) > 0) {
            nStatus |= 8;
         }

         if((mCmd[1] & 8) > 0) {
            nStatus |= 16;
         }

         if((mCmd[1] & 4) > 0) {
            nStatus |= 32;
         }
      }

      if(mCmd[2] != 18 && mCmd[2] != 0) {
         if((mCmd[2] & 64) > 0) {
            nStatus |= 64;
         }

         if((mCmd[2] & 32) > 0) {
            nStatus |= 128;
         }

         if((mCmd[2] & 4) > 0) {
            nStatus |= 256;
         }
      }

      if(mCmd[3] != 0 && mCmd[3] != 18) {
         if((mCmd[3] & 96) > 0) {
            nStatus |= 1024;
         } else if((mCmd[3] & 12) > 0) {
            nStatus |= 512;
         }
      }

      return nStatus;
   }

   public static int getStatusDescribe(int getStatus) {
      Log.i("YkPrinter", "getStatusDescribe status:" + getStatus);

      try {
         return getStatus == -1?1:((getStatus & 512) > 0?2:((getStatus & 1024) <= 0 && (getStatus & 8) <= 0?((getStatus & 4) > 0?4:((getStatus & 32) > 0?5:((getStatus & 1) > 0?6:((getStatus & 2) <= 0 && (getStatus & 16) <= 0?((getStatus & 256) > 0?8:((getStatus & 64) > 0?9:((getStatus & 128) > 0?10:0))):7)))):3));
      } catch (Exception var2) {
         return 6;
      }
   }

   private int changeCommonStatus(int iStatus) {
      byte commonSatus;
      switch(iStatus) {
      case 0:
         commonSatus = 0;
         break;
      case 1:
         commonSatus = 1;
         break;
      case 2:
         commonSatus = 8;
         break;
      case 3:
         commonSatus = 7;
         break;
      case 4:
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
         commonSatus = 99;
         break;
      case 5:
         commonSatus = 3;
         break;
      default:
         commonSatus = 99;
      }

      return commonSatus;
   }
}

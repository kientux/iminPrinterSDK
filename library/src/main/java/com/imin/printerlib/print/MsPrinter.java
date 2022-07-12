package com.imin.printerlib.print;

import android.util.Log;
import com.imin.printerlib.port.UsbPrinter;
import com.imin.printerlib.print.PrinterStater;

public class MsPrinter implements PrinterStater {

   private static final String TAG = "MsPrinter";


   public int getPrinterStatus(UsbPrinter usbPrinter) {
      int status = -1;
      byte[] status1 = this.getStatus1();
      byte[] read1 = new byte[1];
      if(usbPrinter.writeBuffer(status1, status1.length) < 0) {
         return -1;
      } else {
         if(usbPrinter.readBuffer(read1, read1.length) > 0) {
            status = this.checkStatus1(read1[0]);
         }

         if(status != 0) {
            return status;
         } else {
            byte[] read2 = new byte[1];
            byte[] status2 = this.getStatus2();
            if(usbPrinter.writeBuffer(status2, status2.length) < 0) {
               return -1;
            } else {
               if(usbPrinter.readBuffer(read2, read2.length) > 0) {
                  status = this.checkStatus2(read2[0]);
               }

               if(status != 0) {
                  return status;
               } else {
                  byte[] read3 = new byte[1];
                  byte[] status3 = this.getStatus3();
                  if(usbPrinter.writeBuffer(status3, status3.length) < 0) {
                     return -1;
                  } else {
                     if(usbPrinter.readBuffer(read3, read3.length) > 0) {
                        status = this.checkStatus3(read3[0]);
                     }

                     if(status != 0) {
                        return status;
                     } else {
                        byte[] read4 = new byte[1];
                        byte[] status4 = this.getStatus4();
                        if(usbPrinter.writeBuffer(status4, status4.length) < 0) {
                           return -1;
                        } else {
                           if(usbPrinter.readBuffer(read4, read4.length) > 0) {
                              status = this.checkStatus4(read4[0]);
                           }

                           int common_status = this.changeCommonStatus(status);
                           Log.i("MsPrinter", "ms_status:" + status + " common_status:" + common_status);
                           return common_status;
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private byte[] getStatus1() {
      byte[] status;
      (status = new byte[3])[0] = 16;
      status[1] = 4;
      status[2] = 1;
      return status;
   }

   private byte[] getStatus2() {
      byte[] status;
      (status = new byte[3])[0] = 16;
      status[1] = 4;
      status[2] = 2;
      return status;
   }

   private byte[] getStatus3() {
      byte[] var0;
      (var0 = new byte[3])[0] = 16;
      var0[1] = 4;
      var0[2] = 3;
      return var0;
   }

   private byte[] getStatus4() {
      byte[] var0;
      (var0 = new byte[3])[0] = 16;
      var0[1] = 4;
      var0[2] = 4;
      return var0;
   }

   private int checkStatus1(byte code) {
      return (code & 22) != 22?2:0;
   }

   private int checkStatus2(byte code) {
      return (code & 4) == 4?3:0;
   }

   private int checkStatus3(byte code) {
      return (code & 8) == 8?4:((code & 64) == 64?5:((code & 32) == 32?6:0));
   }

   private int checkStatus4(byte var0) {
      return (var0 & 96) == 96?7:((var0 & 12) == 12?8:0);
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
      case 4:
      case 5:
      case 6:
         commonSatus = 99;
         break;
      case 3:
         commonSatus = 3;
         break;
      case 7:
         commonSatus = 7;
         break;
      case 8:
         commonSatus = 8;
         break;
      default:
         commonSatus = 99;
      }

      return commonSatus;
   }
}

package com.imin.printerlib.print;

import com.imin.printerlib.print.MsPrinter;
import com.imin.printerlib.print.PrinterFactory;
import com.imin.printerlib.print.PrinterStater;
import com.imin.printerlib.print.YkPrinter;

public class PrinterStaterFactory implements PrinterFactory {

   public static int MS_PID_1 = 8211;
   public static int MS_PID_2 = 8213;
   public static int MS_PID_3 = 8215;
   public static int MS_VID = 1305;
   public static int YK_PID = 30016;
   public static int YK_VID = 1155;


   public PrinterStater createPrinterStater(int pid, int vid) {
      if(pid != MS_PID_1 && pid != MS_PID_2 && pid != MS_PID_3) {
         if(pid == YK_PID && vid == YK_VID) {
            return new YkPrinter();
         }
      } else if(vid == MS_VID) {
         return new MsPrinter();
      }

      return new MsPrinter();
   }

}

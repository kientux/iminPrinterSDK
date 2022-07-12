package com.imin.printerlib.serial;

import com.imin.printerlib.serial.ComBean;
import com.imin.printerlib.serial.SerialHelper;

public class SerialControl extends SerialHelper {

   private static SerialControl SerialControl;


   public static SerialControl getInstance() {
      Class var0 = SerialControl.class;
      synchronized(SerialControl.class) {
         if(SerialControl == null) {
            SerialControl = new SerialControl();
         }
      }

      return SerialControl;
   }

   protected void onDataReceived(ComBean ComRecData) {}
}

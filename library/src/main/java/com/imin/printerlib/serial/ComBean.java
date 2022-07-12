package com.imin.printerlib.serial;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ComBean {

   public byte[] bRec = null;
   public String sRecTime = "";
   public String sComPort = "";


   public ComBean(String sPort, byte[] buffer, int size) {
      this.sComPort = sPort;
      this.bRec = new byte[size];

      for(int sDateFormat = 0; sDateFormat < size; ++sDateFormat) {
         this.bRec[sDateFormat] = buffer[sDateFormat];
         System.out.println("--xxx---" + this.bRec[sDateFormat]);
      }

      SimpleDateFormat var5 = new SimpleDateFormat("hh:mm:ss");
      this.sRecTime = var5.format(new Date());
   }
}

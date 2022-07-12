package com.imin.printerlib.serial;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.imin.printerlib.serial.SerialHelper;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;

public class SerialPrintUtils {

   public static final String TAG = "xgh";
   public static final String Z2_File = "/sys/devices/platform/ns_power/ns_power";
   public static final String Z2W_File = "/sys/devices/platform/soc/soc:ns_power/ns_power";
   private static String sModel;


   public static void OpenComPort(SerialHelper ComPort, Context context) {
      try {
         ComPort.open();
      } catch (SecurityException var3) {
         Log.d("yegf", "OpenComPort  -- SecurityException : " + var3.getMessage());
         var3.printStackTrace();
      } catch (IOException var4) {
         Log.d("yegf", "OpenComPort  -- IOException : " + var4.getMessage());
         var4.printStackTrace();
      } catch (InvalidParameterException var5) {
         var5.printStackTrace();
         Log.d("yegf", "OpenComPort  -- InvalidParameterException : " + var5.getMessage());
      }

   }

   public static void CloseComPort(SerialHelper ComPort) {
      if(ComPort != null) {
         ComPort.stopSend();
         ComPort.close();
      }

   }

   public static int getPrinterStatus(SerialHelper mComPort) {
      try {
         Thread.sleep(1000L);
      } catch (InterruptedException var3) {
         var3.printStackTrace();
      }

      if(mComPort != null && mComPort.isOpen()) {
         if(mComPort.mInputStream == null) {
            Log.d("XGH", " mComPort.mInputStream == null");
            return -1;
         }

         try {
            byte[] e = new byte[1];
            Log.d("xgh", "getPrinterStatus mInputStream.available = " + mComPort.mInputStream.available());
            if(mComPort.mInputStream.available() > 0) {
               mComPort.mInputStream.read(e);
               Log.d("xgh", "getPrinterStatus   values = " + e[0]);
               return changeCommonStatus(e[0]);
            }
         } catch (IOException var2) {
            Log.d("xgh", "getPrinterStatus   IOException " + var2.getMessage());
            var2.printStackTrace();
         }
      }

      Log.d("XGH", " mComPort isOpen():" + mComPort.isOpen());
      return -1;
   }

   private static int changeCommonStatus(int iStatus) {
      int commonSatus;
      switch(iStatus) {
      case 18:
         commonSatus = 0;
         break;
      case 22:
         commonSatus = 99;
         break;
      default:
         commonSatus = iStatus;
      }

      return commonSatus;
   }

   public static void OpenPower() {
      sModel = Build.MODEL;

      try {
         BufferedWriter e;
         if(!TextUtils.equals("M2-203", sModel) && !TextUtils.equals("M2", sModel) && !TextUtils.equals("M2-Pro", sModel)) {
            if(TextUtils.equals("M2-202", sModel)) {
               e = new BufferedWriter(new FileWriter("/sys/devices/platform/soc/soc:ns_power/ns_power"));
               e.write("100");
               e.close();
            }
         } else {
            e = new BufferedWriter(new FileWriter("/sys/devices/platform/ns_power/ns_power"));
            e.write("0x100");
            e.close();
         }
      } catch (IOException var1) {
         Log.d("xgh", "Unable to write result file : " + var1.getMessage());
      }

   }

   public static void ClosePower() {
      sModel = Build.MODEL;
      Log.d("xgh", "printer power off");

      try {
         BufferedWriter e;
         if(!TextUtils.equals("M2-203", sModel) && !TextUtils.equals("M2", sModel) && !TextUtils.equals("M2-Pro", sModel)) {
            if(TextUtils.equals("M2-202", sModel)) {
               e = new BufferedWriter(new FileWriter("/sys/devices/platform/soc/soc:ns_power/ns_power"));
               e.write("101");
               e.close();
            }
         } else {
            e = new BufferedWriter(new FileWriter("/sys/devices/platform/ns_power/ns_power"));
            e.write("0x101");
            e.close();
         }
      } catch (IOException var1) {
         Log.d("xgh", "Unable to write result file: " + var1.getMessage());
      }

   }

   public static void ShowMessage(String sMsg, Context context) {
      Toast.makeText(context, sMsg, 0).show();
   }

   private static String readFile(String path) {
      String content = "";
      File file = new File(path);
      if(file.isDirectory()) {
         Log.d("TestFile", "The File doesn\'t not exist.");
      } else {
         try {
            FileInputStream e = new FileInputStream(file);
            if(e != null) {
               InputStreamReader inputreader = new InputStreamReader(e);

               String line;
               for(BufferedReader buffreader = new BufferedReader(inputreader); (line = buffreader.readLine()) != null; content = content + line + "\n") {
                  ;
               }

               e.close();
            }
         } catch (FileNotFoundException var7) {
            Log.d("TestFile", "The File doesn\'t not exist.");
         } catch (IOException var8) {
            Log.d("TestFile", var8.getMessage());
         }
      }

      return content;
   }
}

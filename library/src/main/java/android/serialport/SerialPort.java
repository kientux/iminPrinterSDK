package android.serialport;

import android.util.Log;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

   private static final String TAG = "SerialPort";
   private static final String DEFAULT_SU_PATH = "/system/bin/su";
   private static String sSuPath = "/system/bin/su";
   private FileDescriptor mFd;
   private FileInputStream mFileInputStream;
   private FileOutputStream mFileOutputStream;


   public static void setSuPath(String suPath) {
      if(suPath != null) {
         sSuPath = suPath;
      }
   }

   public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {
      if(!device.canRead() || !device.canWrite()) {
         try {
            Process e = Runtime.getRuntime().exec(sSuPath);
            String cmd = "chmod 666 " + device.getAbsolutePath() + "\nexit\n";
            e.getOutputStream().write(cmd.getBytes());
            if(e.waitFor() != 0 || !device.canRead() || !device.canWrite()) {
               throw new SecurityException();
            }
         } catch (Exception var6) {
            var6.printStackTrace();
            throw new SecurityException();
         }
      }

      this.mFd = open(device.getAbsolutePath(), baudrate, flags);
      if(this.mFd == null) {
         Log.e("SerialPort", "native open returns null");
         throw new IOException();
      } else {
         this.mFileInputStream = new FileInputStream(this.mFd);
         this.mFileOutputStream = new FileOutputStream(this.mFd);
      }
   }

   public SerialPort(String devicePath, int baudrate, int flags) throws SecurityException, IOException {
      this(new File(devicePath), baudrate, flags);
   }

   public SerialPort(File device, int baudrate) throws SecurityException, IOException {
      this(device, baudrate, 0);
   }

   public SerialPort(String devicePath, int baudrate) throws SecurityException, IOException {
      this(new File(devicePath), baudrate, 0);
   }

   public InputStream getInputStream() {
      return this.mFileInputStream;
   }

   public OutputStream getOutputStream() {
      return this.mFileOutputStream;
   }

   private static native FileDescriptor open(String var0, int var1, int var2);

   public native void close();

   static {
      System.loadLibrary("serial_port_imin");
   }
}

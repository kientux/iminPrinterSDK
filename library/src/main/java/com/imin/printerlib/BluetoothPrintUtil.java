package com.imin.printerlib;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class BluetoothPrintUtil {

   private OutputStreamWriter mWriter = null;
   private OutputStream mOutputStream = null;


   public BluetoothPrintUtil(OutputStream outputStream, String encoding) throws IOException {
      this.mWriter = new OutputStreamWriter(outputStream, encoding);
      this.mOutputStream = outputStream;
   }

   public void initPrinter() throws IOException {
      this.mWriter.write(27);
      this.mWriter.write(64);
      this.mWriter.flush();
   }

   public void printRawBytes(byte[] bytes) {
      try {
         this.mOutputStream.write(bytes);
         this.mOutputStream.flush();
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public void print(byte[] bs) throws IOException {
      this.mOutputStream.write(bs);
   }

   public void printText(String text) throws IOException {
      this.mWriter.write(text);
      this.mWriter.flush();
   }
}

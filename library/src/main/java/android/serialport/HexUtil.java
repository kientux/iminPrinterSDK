package android.serialport;

import java.util.Iterator;
import java.util.List;

public class HexUtil {

   public static byte[] hexStringToBytes(String hexString) {
      if(hexString != null && !hexString.equals("")) {
         hexString = hexString.toUpperCase();
         int length = hexString.length() / 2;
         char[] hexChars = hexString.toCharArray();
         byte[] d = new byte[length];

         for(int i = 0; i < length; ++i) {
            int pos = i * 2;
            d[i] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
         }

         return d;
      } else {
         return null;
      }
   }

   private static byte charToByte(char c) {
      return (byte)"0123456789ABCDEF".indexOf(c);
   }

   public static short bytes2Short2(byte[] b) {
      short i = (short)((b[1] & 255) << 8 | b[0] & 255);
      return i;
   }

   public static String BinaryToHexString(byte[] bytes) {
      String hexStr = "0123456789ABCDEF";
      String result = "";
      String hex = "";
      byte[] var4 = bytes;
      int var5 = bytes.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         byte b = var4[var6];
         hex = String.valueOf(hexStr.charAt((b & 240) >> 4));
         hex = hex + String.valueOf(hexStr.charAt(b & 15));
         result = result + hex + "";
      }

      return result;
   }

   public static String BinaryToHexString(List bytes) {
      String hexStr = "0123456789ABCDEF";
      String result = "";
      String hex = "";

      for(Iterator var4 = bytes.iterator(); var4.hasNext(); result = result + hex + "") {
         byte b = ((Byte)var4.next()).byteValue();
         hex = String.valueOf(hexStr.charAt((b & 240) >> 4));
         hex = hex + String.valueOf(hexStr.charAt(b & 15));
      }

      return result;
   }

   public static int byteToInt(byte b) {
      return b & 255;
   }

   public static int byteArrayToInt(byte[] bytes) {
      return bytes[0] << 24 | (bytes[1] & 255) << 16 | (bytes[2] & 255) << 8 | bytes[3] & 255;
   }

   public static short byteArrayToShort(byte[] bytes) {
      return (short)((bytes[0] & 255) << 8 | bytes[1] & 255);
   }

   public static byte[] short2ByteArray(short shortValue) {
      byte[] bytes = new byte[]{(byte)(shortValue >> 8 & 255), (byte)(shortValue & 255)};
      return bytes;
   }

   public static int convertTenToHex(int n) {
      return Integer.valueOf(String.valueOf(n), 16).intValue();
   }

   public static final byte[] intToByteArray(int value) {
      return new byte[]{(byte)(value >>> 24), (byte)(value >>> 16), (byte)(value >>> 8), (byte)value};
   }
}

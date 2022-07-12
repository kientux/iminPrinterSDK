package com.imin.printerlib.serial;


public class MyFunc {

   public static int isOdd(int num) {
      return num & 1;
   }

   public static int HexToInt(String inHex) {
      return Integer.parseInt(inHex, 16);
   }

   public static byte HexToByte(String inHex) {
      return (byte)Integer.parseInt(inHex, 16);
   }

   public static String Byte2Hex(Byte inByte) {
      return String.format("%02x", new Object[]{inByte}).toUpperCase();
   }

   public static String ByteArrToHex(byte[] inBytArr) {
      StringBuilder strBuilder = new StringBuilder();
      int j = inBytArr.length;

      for(int i = 0; i < j; ++i) {
         strBuilder.append(Byte2Hex(Byte.valueOf(inBytArr[i])));
         strBuilder.append(" ");
      }

      return strBuilder.toString();
   }

   public static String NeoByteArrToHex(byte[] inBytArr) {
      StringBuilder strBuilder = new StringBuilder();
      int j = inBytArr.length;

      for(int i = 0; i < j; ++i) {
         strBuilder.append(Byte2Hex(Byte.valueOf(inBytArr[i])));
      }

      return strBuilder.toString();
   }

   public static byte[] intToBytesLittle(int value) {
      byte[] src = new byte[]{(byte)(value & 255), (byte)(value >> 8 & 255), (byte)(value >> 16 & 255), (byte)(value >> 24 & 255)};
      return src;
   }

   public static byte[] intToBytesBig(int value) {
      byte[] src = new byte[]{(byte)(value >> 24 & 255), (byte)(value >> 16 & 255), (byte)(value >> 8 & 255), (byte)(value & 255)};
      return src;
   }

   public static int makeChecksum(String hexdata) {
      if(hexdata != null && !hexdata.equals("")) {
         hexdata = hexdata.replaceAll(" ", "");
         int total = 0;
         int len = hexdata.length();
         if(len % 2 != 0) {
            return 0;
         } else {
            for(int num = 0; num < len; num += 2) {
               String s = hexdata.substring(num, num + 2);
               total += Integer.parseInt(s, 16);
            }

            return total;
         }
      } else {
         return 0;
      }
   }

   public static String ByteArrToHex(byte[] inBytArr, int offset, int byteCount) {
      StringBuilder strBuilder = new StringBuilder();
      int j = byteCount;

      for(int i = offset; i < j; ++i) {
         strBuilder.append(Byte2Hex(Byte.valueOf(inBytArr[i])));
      }

      return strBuilder.toString();
   }

   public static byte[] HexToByteArr(String inHex) {
      int hexlen = inHex.length();
      byte[] result;
      if(isOdd(hexlen) == 1) {
         ++hexlen;
         result = new byte[hexlen / 2];
         inHex = "0" + inHex;
      } else {
         result = new byte[hexlen / 2];
      }

      int j = 0;

      for(int i = 0; i < hexlen; i += 2) {
         result[j] = HexToByte(inHex.substring(i, i + 2));
         ++j;
      }

      return result;
   }

   public static String getInverse(String hex) {
      byte[] bytes = hexStringToByte(hex);

      for(int i = 0; i < bytes.length; ++i) {
         byte temp = bytes[i];
         bytes[i] = (byte)(~temp);
      }

      return bytesToHexString(bytes);
   }

   public static byte[] hexStringToByte(String hex) {
      int len = hex.length() / 2;
      byte[] result = new byte[len];
      char[] achar = hex.toCharArray();

      for(int i = 0; i < len; ++i) {
         int pos = i * 2;
         result[i] = (byte)(toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
      }

      return result;
   }

   private static byte toByte(char c) {
      byte b = (byte)"0123456789ABCDEF".indexOf(c);
      return b;
   }

   public static String bytesToHexString(byte[] src) {
      StringBuilder stringBuilder = new StringBuilder();
      if(src != null && src.length > 0) {
         for(int i = 0; i < src.length; ++i) {
            int v = src[i] & 255;
            String hv = Integer.toHexString(v);
            if(hv.length() < 2) {
               stringBuilder.append(0);
            }

            stringBuilder.append(hv);
         }

         return stringBuilder.toString();
      } else {
         return null;
      }
   }

   public static String stringToHexString(String s) {
      String str = "";

      for(int i = 0; i < s.length(); ++i) {
         char ch = s.charAt(i);
         String s4 = Integer.toHexString(ch);
         str = str + s4;
      }

      return str;
   }
}

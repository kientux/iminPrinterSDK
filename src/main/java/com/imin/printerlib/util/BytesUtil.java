package com.imin.printerlib.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

public class BytesUtil {

   public static int isOdd(int num) {
      return num & 1;
   }

   public static String ByteToString(byte inByte) {
      return new String(new byte[]{inByte});
   }

   public static String ByteArrToString(byte[] inByte) {
      return new String(inByte);
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
      if(inBytArr == null) {
         return "null";
      } else {
         StringBuilder strBuilder = new StringBuilder();
         int j = inBytArr.length;

         for(int i = 0; i < j; ++i) {
            strBuilder.append(Byte2Hex(Byte.valueOf(inBytArr[i])));
            strBuilder.append(" ");
         }

         return strBuilder.toString();
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

   public static byte[] toByteArrayOfCharSet(String content, String charSet) {
      try {
         return content.getBytes(charSet);
      } catch (Exception var3) {
         Log.e("xgh", "to Byte Array Of CharSet:" + var3.getLocalizedMessage());
         var3.printStackTrace();
         return new byte[0];
      }
   }

   public static boolean isValue(byte[] data, int offsetSize, int size) {
      return data != null && data.length > 0 && data.length - offsetSize >= size;
   }

   public static byte[] getBytes(byte[] data, int offsetSize, int size) {
      if(isValue(data, offsetSize, size) && (offsetSize != 0 || size != data.length)) {
         byte[] recv = new byte[size];
         System.arraycopy(data, offsetSize, recv, 0, size);
         return recv;
      } else {
         return data;
      }
   }

   public static String getHexStringFromBytes(byte[] data) {
      if(data != null && data.length > 0) {
         String hexString = "0123456789ABCDEF";
         int size = data.length * 2;
         StringBuilder sb = new StringBuilder(size);

         for(int i = 0; i < data.length; ++i) {
            sb.append(hexString.charAt((data[i] & 240) >> 4));
            sb.append(hexString.charAt((data[i] & 15) >> 0));
         }

         return sb.toString();
      } else {
         return null;
      }
   }

   private static byte charToByte(char c) {
      return (byte)"0123456789ABCDEF".indexOf(c);
   }

   @SuppressLint({"DefaultLocale"})
   public static byte[] getBytesFromHexString(String hexstring) {
      if(hexstring != null && !hexstring.equals("")) {
         hexstring = hexstring.replace(" ", "");
         hexstring = hexstring.toUpperCase();
         int size = hexstring.length() / 2;
         char[] hexarray = hexstring.toCharArray();
         byte[] rv = new byte[size];

         for(int i = 0; i < size; ++i) {
            int pos = i * 2;
            rv[i] = (byte)(charToByte(hexarray[pos]) << 4 | charToByte(hexarray[pos + 1]));
         }

         return rv;
      } else {
         return null;
      }
   }

   @SuppressLint({"DefaultLocale"})
   public static byte[] getBytesFromDecString(String decstring) {
      if(decstring != null && !decstring.equals("")) {
         decstring = decstring.replace(" ", "");
         int size = decstring.length() / 2;
         char[] decarray = decstring.toCharArray();
         byte[] rv = new byte[size];

         for(int i = 0; i < size; ++i) {
            int pos = i * 2;
            rv[i] = (byte)(charToByte(decarray[pos]) * 10 + charToByte(decarray[pos + 1]));
         }

         return rv;
      } else {
         return null;
      }
   }

   public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
      byte[] byte_3 = new byte[byte_1.length + byte_2.length];
      System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
      System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
      return byte_3;
   }

   public static byte[] byteMerger(byte[][] byteList) {
      int length = 0;

      for(int result = 0; result < byteList.length; ++result) {
         length += byteList[result].length;
      }

      byte[] var7 = new byte[length];
      int index = 0;

      int i;
      for(i = 0; i < byteList.length; ++i) {
         byte[] nowByte = byteList[i];

         for(int k = 0; k < byteList[i].length; ++k) {
            var7[index] = nowByte[k];
            ++index;
         }
      }

      for(i = 0; i < index; ++i) {
         ;
      }

      return var7;
   }

   public static byte[] initTable(int h, int w) {
      int hh = h * 32;
      int ww = w * 4;
      byte[] data = new byte[hh * ww + 5];
      data[0] = (byte)ww;
      data[1] = (byte)(ww >> 8);
      data[2] = (byte)hh;
      data[3] = (byte)(hh >> 8);
      int k = 4;
      byte m = 31;

      int i;
      for(i = 0; i < h; ++i) {
         int t;
         for(t = 0; t < w; ++t) {
            data[k++] = -1;
            data[k++] = -1;
            data[k++] = -1;
            data[k++] = -1;
         }

         if(i == h - 1) {
            m = 30;
         }

         for(t = 0; t < m; ++t) {
            for(int j = 0; j < w - 1; ++j) {
               data[k++] = -128;
               data[k++] = 0;
               data[k++] = 0;
               data[k++] = 0;
            }

            data[k++] = -128;
            data[k++] = 0;
            data[k++] = 0;
            data[k++] = 1;
         }
      }

      for(i = 0; i < w; ++i) {
         data[k++] = -1;
         data[k++] = -1;
         data[k++] = -1;
         data[k++] = -1;
      }

      data[k++] = 10;
      return data;
   }

   public static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
      int height = origin.getHeight();
      int width = origin.getWidth();
      float scaleWidth = (float)newWidth / (float)width;
      float scaleHeight = (float)newHeight / (float)height;
      Matrix matrix = new Matrix();
      matrix.postScale(scaleWidth, scaleHeight);
      return Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
   }

   public static Bitmap rotateBitmap(Bitmap origin, int d) {
      Matrix matrix = new Matrix();
      matrix.setRotate((float)d);
      return Bitmap.createBitmap(origin, 0, 0, origin.getWidth(), origin.getHeight(), matrix, false);
   }

   public static byte[] getBytesFromBitMap(Bitmap bitmap) {
      int width = bitmap.getWidth();
      int height = bitmap.getHeight();
      int bw = (width - 1) / 8 + 1;
      byte[] rv = new byte[height * bw + 4];
      rv[0] = (byte)bw;
      rv[1] = (byte)(bw >> 8);
      rv[2] = (byte)height;
      rv[3] = (byte)(height >> 8);
      int[] pixels = new int[width * height];
      bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
      bitmap.recycle();

      for(int i = 0; i < height; ++i) {
         for(int j = 0; j < width; ++j) {
            int clr = pixels[width * i + j];
            int red = (clr & 16711680) >> 16;
            int green = (clr & '\uff00') >> 8;
            int blue = clr & 255;
            byte gray = RGB2Gray(red, green, blue);
            rv[bw * i + j / 8 + 4] = (byte)(rv[bw * i + j / 8 + 4] | gray << 7 - j % 8);
         }
      }

      return rv;
   }

   public static byte[] getBytesFromBitMap(Bitmap bitmap, int mode) {
      int width = bitmap.getWidth();
      int height = bitmap.getHeight();
      int[] pixels = new int[width * height];
      byte[] res;
      int i;
      int j;
      int m;
      int clr;
      int red;
      int green;
      if(mode != 0 && mode != 1) {
         if(mode != 32 && mode != 33) {
            return new byte[]{(byte)10};
         } else {
            res = new byte[width * height / 8 + 6 * height / 24];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            for(i = 0; i < height / 24; ++i) {
               res[i * (width * 3 + 6)] = 27;
               res[1 + i * (width * 3 + 6)] = 42;
               res[2 + i * (width * 3 + 6)] = (byte)mode;
               res[3 + i * (width * 3 + 6)] = (byte)(width % 256);
               res[4 + i * (width * 3 + 6)] = (byte)(width / 256);

               for(j = 0; j < width; ++j) {
                  for(int var15 = 0; var15 < 3; ++var15) {
                     byte gray1 = 0;

                     for(m = 0; m < 8; ++m) {
                        clr = pixels[j + width * (i * 24 + m + var15 * 8)];
                        red = (clr & 16711680) >> 16;
                        green = (clr & '\uff00') >> 8;
                        int blue = clr & 255;
                        gray1 = (byte)(RGB2Gray(red, green, blue) << 7 - m | gray1);
                     }

                     res[5 + j * 3 + i * (width * 3 + 6) + var15] = gray1;
                  }
               }

               res[5 + width * 3 + i * (width * 3 + 6)] = 10;
            }

            return res;
         }
      } else {
         res = new byte[width * height / 8 + 6 * height / 8];
         bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

         for(i = 0; i < height / 8; ++i) {
            res[i * (width + 6)] = 27;
            res[1 + i * (width + 6)] = 42;
            res[2 + i * (width + 6)] = (byte)mode;
            res[3 + i * (width + 6)] = (byte)(width % 256);
            res[4 + i * (width + 6)] = (byte)(width / 256);

            for(j = 0; j < width; ++j) {
               byte gray = 0;

               for(m = 0; m < 8; ++m) {
                  m = pixels[j + width * (i * 8 + m)];
                  clr = (m & 16711680) >> 16;
                  red = (m & '\uff00') >> 8;
                  green = m & 255;
                  gray = (byte)(RGB2Gray(clr, red, green) << 7 - m | gray);
               }

               res[5 + j + i * (width + 6)] = gray;
            }

            res[5 + width + i * (width + 6)] = 10;
         }

         return res;
      }
   }

   private static byte RGB2Gray(int r, int g, int b) {
      return (byte)((int)(0.299D * (double)r + 0.587D * (double)g + 0.114D * (double)b) < 200?1:0);
   }

   public static byte[] initBlackBlock(int w) {
      int ww = (w + 7) / 8;
      int hh = ww * 8;
      byte[] data = new byte[hh * ww + 4];
      data[0] = (byte)ww;
      data[1] = (byte)(ww >> 8);
      data[2] = (byte)hh;
      data[3] = (byte)(hh >> 8);
      int k = 4;

      for(int i = 0; i < 8; ++i) {
         for(int j = 0; j < ww; ++j) {
            for(int m = 0; m < ww; ++m) {
               if(m / (ww / 8) == i) {
                  data[k++] = -1;
               } else {
                  data[k++] = 0;
               }
            }
         }
      }

      return data;
   }

   public static byte[] initBlackBlock(int h, int w) {
      int hh = h;
      int ww = (w - 1) / 8 + 1;
      byte[] data = new byte[h * ww + 4];
      data[0] = (byte)ww;
      data[1] = (byte)(ww >> 8);
      data[2] = (byte)h;
      data[3] = (byte)(h >> 8);
      int k = 4;

      for(int i = 0; i < hh; ++i) {
         for(int j = 0; j < ww; ++j) {
            data[k++] = -1;
         }
      }

      return data;
   }

   public static byte[] initLine1(int w, int type) {
      byte[][] kk = new byte[][]{{(byte)0, (byte)0, (byte)124, (byte)124, (byte)124, (byte)0, (byte)0}, {(byte)0, (byte)0, (byte)-1, (byte)-1, (byte)-1, (byte)0, (byte)0}, {(byte)0, (byte)68, (byte)68, (byte)-1, (byte)68, (byte)68, (byte)0}, {(byte)0, (byte)34, (byte)85, (byte)-120, (byte)85, (byte)34, (byte)0}, {(byte)8, (byte)8, (byte)28, (byte)127, (byte)28, (byte)8, (byte)8}, {(byte)8, (byte)20, (byte)34, (byte)65, (byte)34, (byte)20, (byte)8}, {(byte)8, (byte)20, (byte)42, (byte)85, (byte)42, (byte)20, (byte)8}, {(byte)8, (byte)28, (byte)62, (byte)127, (byte)62, (byte)28, (byte)8}, {(byte)73, (byte)34, (byte)20, (byte)73, (byte)20, (byte)34, (byte)73}, {(byte)99, (byte)119, (byte)62, (byte)28, (byte)62, (byte)119, (byte)99}, {(byte)112, (byte)32, (byte)-81, (byte)-86, (byte)-6, (byte)2, (byte)7}, {(byte)-17, (byte)40, (byte)-18, (byte)-86, (byte)-18, (byte)-126, (byte)-2}};
      int ww = (w + 7) / 8;
      byte[] data = new byte[13 * ww + 8];
      data[0] = 29;
      data[1] = 118;
      data[2] = 48;
      data[3] = 0;
      data[4] = (byte)ww;
      data[5] = (byte)(ww >> 8);
      data[6] = 13;
      data[7] = 0;
      int k = 8;

      int i;
      for(i = 0; i < 3 * ww; ++i) {
         data[k++] = 0;
      }

      for(i = 0; i < ww; ++i) {
         data[k++] = kk[type][0];
      }

      for(i = 0; i < ww; ++i) {
         data[k++] = kk[type][1];
      }

      for(i = 0; i < ww; ++i) {
         data[k++] = kk[type][2];
      }

      for(i = 0; i < ww; ++i) {
         data[k++] = kk[type][3];
      }

      for(i = 0; i < ww; ++i) {
         data[k++] = kk[type][4];
      }

      for(i = 0; i < ww; ++i) {
         data[k++] = kk[type][5];
      }

      for(i = 0; i < ww; ++i) {
         data[k++] = kk[type][6];
      }

      for(i = 0; i < 3 * ww; ++i) {
         data[k++] = 0;
      }

      return data;
   }

   public static byte[] initLine2(int w) {
      int ww = (w + 7) / 8;
      byte[] data = new byte[12 * ww + 4];
      data[0] = (byte)ww;
      data[1] = (byte)(ww >> 8);
      data[2] = 12;
      data[3] = 0;
      int k = 4;

      int i;
      for(i = 0; i < 5 * ww; ++i) {
         data[k++] = 0;
      }

      for(i = 0; i < ww; ++i) {
         data[k++] = 127;
      }

      for(i = 0; i < ww; ++i) {
         data[k++] = 127;
      }

      for(i = 0; i < 5 * ww; ++i) {
         data[k++] = 0;
      }

      return data;
   }
}

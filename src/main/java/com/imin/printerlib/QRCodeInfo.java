package com.imin.printerlib;

import com.imin.printerlib.a;
import com.imin.printerlib.b;
import java.io.InputStream;

public class QRCodeInfo {

   int X = 0;
   int a = 0;
   int k = 0;
   int N1 = 0;
   int N2 = 0;
   private int lMargin = 0;
   private int mSide = 2;
   private int version;
   private String charset = "utf-8";
   private char strErrorCorrect = 77;
   private static final String STR_TRUE_FLAG = "1";
   private static final String STR_FALSE_FLAG = "0";
   private static final String STR_FIRST_PARAM = "13";
   private static final String STR_SECOND_PARAM = "52";
   private static final String STR_SECOND_PARAM51 = "51";
   private static final String STR_LAST_PARAM = "00";


   public int getlMargin() {
      return this.lMargin;
   }

   public void setlMargin(int var1) {
      this.lMargin = var1;
   }

   public int getmSide() {
      return this.mSide;
   }

   public void setmSide(int var1) {
      this.mSide = var1;
   }

   public String getCharset() {
      return this.charset;
   }

   public void setCharset(String var1) {
      this.charset = var1;
   }

   public void setErrorCorrect(char var1) {
      this.strErrorCorrect = var1;
   }

   public int getVersion() {
      return this.version;
   }

   public void setVersion(int var1) {
      this.version = var1;
   }

   boolean validContentLegal(byte[] var1) {
      boolean var2 = false;

      try {
         int var3;
         if((var3 = var1.length) <= 14) {
            this.setVersion(1);
            var2 = true;
         } else if(var3 > 14 && var3 <= 26) {
            this.setVersion(2);
            var2 = true;
         } else if(var3 > 26 && var3 <= 42) {
            this.setVersion(3);
            var2 = true;
         } else if(var3 > 42 && var3 <= 62) {
            this.setVersion(4);
            var2 = true;
         } else if(var3 > 62 && var3 <= 84) {
            this.setVersion(5);
            var2 = true;
         } else if(var3 > 84 && var3 <= 106) {
            this.setVersion(6);
            var2 = true;
         } else if(var3 > 106 && var3 <= 122) {
            this.setVersion(7);
            var2 = true;
         } else if(var3 > 122 && var3 <= 152) {
            this.setVersion(8);
            var2 = true;
         } else if(var3 > 152 && var3 <= 180) {
            this.setVersion(9);
            var2 = true;
         } else if(var3 > 180 && var3 <= 213) {
            this.setVersion(10);
            var2 = true;
         } else if(var3 > 213 && var3 <= 251) {
            this.setVersion(11);
            var2 = true;
         } else {
            System.out.println("Content is beyond the scope specified length...");
            var2 = false;
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return var2;
   }

   public byte[] GetQRBCode(String var1, int var2) {
      var1 = this.GetQRCode(var1);
      Object var3 = null;
      int var4 = 0;
      boolean var5 = false;
      boolean var8 = false;
      byte[] var6 = new byte[1024];

      try {
         if(var1.substring(var1.length() - 1) != " ") {
            var1 = var1 + " ";
         }

         if(var2 == 1) {
            var1 = var1.substring(0, var1.length() - 3) + "01 ";
         }

         int var7;
         while(var1.length() > 0) {
            if((var7 = var1.indexOf(" ")) > 0) {
               String var9 = var1.substring(0, var7);
               var3 = null;
               int var10 = Integer.valueOf(var9, 16).intValue();
               var6[var4++] = (byte)var10;
               var1 = var1.substring(var7 + 1);
            }
         }

         byte[] var12 = new byte[var4];

         for(var7 = 0; var7 < var4; ++var7) {
            var12[var7] = var6[var7];
         }

         return var12;
      } catch (Exception var11) {
         var11.printStackTrace();
         return null;
      }
   }

   public String GetQRCode(String var1) {
      int var2 = 0;
      StringBuffer var4 = new StringBuffer();
      StringBuffer var5;
      (var5 = new StringBuffer()).append("13 52 ");

      try {
         byte[] var10 = var1.trim().getBytes(this.charset);
         a var3;
         (var3 = new a()).a(this.strErrorCorrect);
         var3.a();
         if(!this.validContentLegal(var10)) {
            return "";
         }

         var3.a(this.version);
         int var6;
         boolean[][] var12;
         int var7 = (var6 = (var12 = var3.a(var10)).length) % 8 == 0?var6:(var6 / 8 + 1) * 8;
         this.k = var6 * var7 / 8;
         this.N1 = var7 ^ 85;
         this.N2 = var6 ^ 85;
         int var13 = this.mSide ^ 85;
         int var8 = this.lMargin ^ 85;
         this.X = (int)(Math.random() * 10.0D);
         this.a = this.X % 10;
         var5.append(Integer.toHexString(this.X)).append(" ").append(Integer.toHexString(var8)).append(" ").append(Integer.toHexString(this.N1)).append(" ").append(Integer.toHexString(this.N2)).append(" ").append(Integer.toHexString(var13)).append(" ");

         for(var8 = 0; var8 < var6; ++var8) {
            for(int var9 = 0; var9 < var7; ++var9) {
               ++var2;
               if(var9 < var6) {
                  if(var12[var9][var8]) {
                     var4.append("1");
                  } else {
                     var4.append("0");
                  }
               } else {
                  var4.append("0");
               }

               if(var2 % 8 == 0) {
                  if((var2 / 8 - 1) % 10 == this.a) {
                     var13 = Integer.valueOf(var4.toString(), 2).intValue() ^ 153;
                  } else {
                     var13 = Integer.valueOf(var4.toString(), 2).intValue();
                  }

                  String var14 = Integer.toHexString(var13);
                  var5.append(var14).append(" ");
                  var4.delete(0, var4.length());
               }
            }
         }
      } catch (Exception var141) {
         var141.printStackTrace();
      }

      var5.append("00");
      return var5.toString();
   }

   public byte[] Get51QRBCode(String var1, int var2) {
      var1 = this.Get51QRCode(var1);
      Object var3 = null;
      int var4 = 0;
      boolean var5 = false;
      boolean var8 = false;
      byte[] var6 = new byte[1024];

      try {
         if(var1.substring(var1.length() - 1) != " ") {
            var1 = var1 + " ";
         }

         if(var2 == 1) {
            var1 = var1.substring(0, var1.length() - 3) + "01 ";
         }

         int var7;
         while(var1.length() > 0) {
            if((var7 = var1.indexOf(" ")) > 0) {
               String var9 = var1.substring(0, var7);
               var3 = null;
               int var10 = Integer.valueOf(var9, 16).intValue();
               var6[var4++] = (byte)var10;
               var1 = var1.substring(var7 + 1);
            }
         }

         byte[] var12 = new byte[var4];

         for(var7 = 0; var7 < var4; ++var7) {
            var12[var7] = var6[var7];
         }

         return var12;
      } catch (Exception var11) {
         var11.printStackTrace();
         return null;
      }
   }

   public String Get51QRCode(String var1) {
      int var2 = 0;
      StringBuffer var4 = new StringBuffer();
      StringBuffer var5;
      (var5 = new StringBuffer()).append("13 51 ");

      try {
         byte[] var10 = var1.trim().getBytes(this.charset);
         a var3;
         (var3 = new a()).a(this.strErrorCorrect);
         var3.a();
         if(!this.validContentLegal(var10)) {
            return "";
         }

         var3.a(this.version);
         int var6;
         boolean[][] var12;
         int var7 = (var6 = (var12 = var3.a(var10)).length) % 8 == 0?var6:(var6 / 8 + 1) * 8;
         this.k = var6 * var7 / 8;
         this.N1 = var7;
         this.N2 = var6;
         int var13 = this.mSide;
         int var8 = this.lMargin;
         this.X = (int)(Math.random() * 10.0D);
         this.a = this.X % 10;
         var5.append(Integer.toHexString(var8)).append(" ").append(Integer.toHexString(this.N1)).append(" ").append(Integer.toHexString(this.N2)).append(" ").append(Integer.toHexString(var13)).append(" ");

         for(var8 = 0; var8 < var6; ++var8) {
            for(int var9 = 0; var9 < var7; ++var9) {
               ++var2;
               if(var9 < var6) {
                  if(var12[var9][var8]) {
                     var4.append("1");
                  } else {
                     var4.append("0");
                  }
               } else {
                  var4.append("0");
               }

               if(var2 % 8 == 0) {
                  String var14 = Integer.toHexString(Integer.valueOf(var4.toString(), 2).intValue());
                  var5.append(var14).append(" ");
                  var4.delete(0, var4.length());
               }
            }
         }
      } catch (Exception var141) {
         var141.printStackTrace();
      }

      var5.append("00");
      return var5.toString();
   }

   public String getBMPImage(String var1) {
      return b.a(var1);
   }

   public byte[] getBMPImageByte(String var1) {
      return b.b(var1);
   }

   public byte[] getBMPImageByteZD(String var1) {
      return b.c(var1);
   }

   public byte[] getBMPImageFileByte(int[] var1, int var2, int var3) {
      int var4 = var2 / 8;
      if((var2 %= 8) > 0) {
         ++var4;
      }

      byte[] var5 = new byte[var4 * var3 + 8];
      byte var6 = 0;
      int var11 = var6 + 1;
      var5[0] = 29;
      ++var11;
      var5[1] = 118;
      ++var11;
      var5[2] = 48;
      ++var11;
      var5[3] = 0;
      ++var11;
      var5[4] = (byte)var4;
      ++var11;
      var5[5] = (byte)(var4 >> 8);
      ++var11;
      var5[6] = (byte)var3;
      ++var11;
      var5[7] = (byte)(var3 >> 8);
      boolean var7 = false;
      boolean var9 = false;
      int var10 = 0;

      for(int var8 = 0; var8 < var3; ++var8) {
         int var12;
         int var13;
         for(var13 = 0; var13 < var4 - 1; ++var13) {
            var12 = 0;
            if(var1[var10++] < -1) {
               var12 += 128;
            }

            if(var1[var10++] < -1) {
               var12 += 64;
            }

            if(var1[var10++] < -1) {
               var12 += 32;
            }

            if(var1[var10++] < -1) {
               var12 += 16;
            }

            if(var1[var10++] < -1) {
               var12 += 8;
            }

            if(var1[var10++] < -1) {
               var12 += 4;
            }

            if(var1[var10++] < -1) {
               var12 += 2;
            }

            if(var1[var10++] < -1) {
               ++var12;
            }

            var5[var11++] = (byte)var12;
         }

         var12 = 0;
         if(var2 == 0) {
            for(var13 = 8; var13 > var2; --var13) {
               if(var1[var10++] < -1) {
                  var12 += 1 << var13;
               }
            }
         } else {
            for(var13 = 0; var13 < var2; ++var13) {
               if(var1[var10++] < -1) {
                  var12 += 1 << 8 - var13;
               }
            }
         }

         var5[var11++] = (byte)var12;
      }

      return var5;
   }

   public byte[] SetNvbmp(int var1, String var2) {
      return b.a(var1, var2);
   }

   public byte[] SetNvbmpByStream(InputStream[] var1) {
      return b.a(var1);
   }

   public boolean convert24bitto1(String var1, String var2) {
      return b.a(var1, var2);
   }

   public byte[] Get1DBar(String var1, int var2, int var3, int var4) {
      boolean var7 = false;
      char[] var6 = new char[100];
      char var11;
      if(var2 >= 2 && var2 <= 6) {
         var11 = (char)var2;
      } else {
         var11 = 2;
      }

      char var12 = (char)var3;
      byte var13;
      if(var4 > 2) {
         var13 = 1;
      } else {
         var13 = 3;
      }

      char[] var8 = new char[64];

      int var5;
      for(var5 = 0; var5 < 64; ++var5) {
         var8[var5] = 0;
      }

      int var16;
      var8[0] = 29;
      var8[1] = 119;
      var8[2] = var11;
      var8[3] = 29;
      var8[4] = 104;
      var8[5] = var12;
      var8[6] = 29;
      var8[7] = 102;
      var8[8] = 0;
      var8[9] = 29;
      var8[10] = 72;
      var8[11] = 2;
      var12 = 0;
      char var14 = (char)var1.length();
      label93:
      switch(var13) {
      case 0:
         var11 = 0;

         while(true) {
            if(var11 >= var14) {
               break label93;
            }

            if(var1.charAt(var11) > 95) {
               return null;
            }

            var6[var12++] = var1.charAt(var11);
            ++var11;
         }
      case 1:
         var11 = 0;

         while(true) {
            if(var11 >= var14) {
               break label93;
            }

            if(var1.charAt(var11) < 32 || var1.charAt(var11) > 127) {
               return null;
            }

            var6[var12++] = var1.charAt(var11);
            ++var11;
         }
      case 2:
         for(var11 = 0; var11 < var14; ++var11) {
            if(var1.charAt(var11) < 48 || var1.charAt(var11) > 57) {
               return null;
            }
         }

         for(var11 = 0; var11 < var14; var11 = (char)((char)(var11 + 1) + 1)) {
            if(var11 + 1 == var14) {
               var6[var12++] = 123;
               var6[var12++] = 65;
               var6[var12++] = var1.charAt(var11);
               break;
            }

            var16 = (var1.charAt(var11) - 48) * 10 + var1.charAt(var11 + 1) - 48;
            var6[var12++] = (char)var16;
         }
      }

      int var9 = var12 + 2;
      byte var15 = 0;
      var5 = var15 + 1;
      var8[0] = 29;
      ++var5;
      var8[1] = 107;
      ++var5;
      var8[2] = 73;
      ++var5;
      var8[3] = (char)var9;
      ++var5;
      var8[4] = 123;
      var16 = 65 + var13;
      ++var5;
      var8[5] = (char)var16;

      for(var11 = 0; var11 < var9 - 2; ++var11) {
         var8[var5++] = var6[var11];
      }

      var16 = var5;
      byte[] var10 = new byte[var5];

      for(var5 = 0; var5 < var16; ++var5) {
         var10[var5] = (byte)var8[var5];
      }

      return var10;
   }
}

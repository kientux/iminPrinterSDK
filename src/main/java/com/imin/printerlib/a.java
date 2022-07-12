package com.imin.printerlib;

import java.io.BufferedInputStream;
import java.io.InputStream;

public class a {

   char a = 77;
   char b = 66;
   int c = 0;
   int d = 0;
   int e = 0;
   int f = 0;
   String g = "";


   public final void a(char var1) {
      this.a = var1;
   }

   public final void a(int var1) {
      if(var1 >= 0 && var1 <= 40) {
         this.c = var1;
      }

   }

   public final void a() {
      this.b = 66;
   }

   public final boolean[][] a(byte[] var1) {
      byte var3 = 0;
      int var2;
      int[] var4 = new int[(var2 = var1.length) + 32];
      byte[] var5 = new byte[var2 + 32];
      if(var2 <= 0) {
         return new boolean[][]{new boolean[1]};
      } else {
         if(this.d > 1) {
            var4[0] = 3;
            var5[0] = 4;
            var4[1] = this.e - 1;
            var5[1] = 4;
            var4[2] = this.d - 1;
            var5[2] = 4;
            var4[3] = this.f;
            var5[3] = 8;
            var3 = 4;
         }

         var5[var3] = 4;
         int[] var6;
         int var7;
         int var8;
         int var27;
         switch(this.b) {
         case 65:
            var6 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
            var4[var3] = 2;
            var27 = var3 + 1;
            var4[var27] = var2;
            var5[var27] = 9;
            var7 = var27++;

            for(var8 = 0; var8 < var2; ++var8) {
               char var37 = (char)var1[var8];
               byte var38 = 0;
               if(var37 >= 48 && var37 < 58) {
                  var38 = (byte)(var37 - 48);
               } else if(var37 >= 65 && var37 < 91) {
                  var38 = (byte)(var37 - 55);
               } else {
                  if(var37 == 32) {
                     var38 = 36;
                  }

                  if(var37 == 36) {
                     var38 = 37;
                  }

                  if(var37 == 37) {
                     var38 = 38;
                  }

                  if(var37 == 42) {
                     var38 = 39;
                  }

                  if(var37 == 43) {
                     var38 = 40;
                  }

                  if(var37 == 45) {
                     var38 = 41;
                  }

                  if(var37 == 46) {
                     var38 = 42;
                  }

                  if(var37 == 47) {
                     var38 = 43;
                  }

                  if(var37 == 58) {
                     var38 = 44;
                  }
               }

               if(var8 % 2 == 0) {
                  var4[var27] = var38;
                  var5[var27] = 6;
               } else {
                  var4[var27] = var4[var27] * 45 + var38;
                  var5[var27] = 11;
                  if(var8 < var2 - 1) {
                     ++var27;
                  }
               }
            }

            ++var27;
            break;
         case 78:
            var6 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4};
            var4[var3] = 1;
            var27 = var3 + 1;
            var4[var27] = var2;
            var5[var27] = 10;
            var7 = var27++;

            for(var8 = 0; var8 < var2; ++var8) {
               if(var8 % 3 == 0) {
                  var4[var27] = var1[var8] - 48;
                  var5[var27] = 4;
               } else {
                  var4[var27] = var4[var27] * 10 + (var1[var8] - 48);
                  if(var8 % 3 == 1) {
                     var5[var27] = 7;
                  } else {
                     var5[var27] = 10;
                     if(var8 < var2 - 1) {
                        ++var27;
                     }
                  }
               }
            }

            ++var27;
            break;
         default:
            var6 = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8};
            var4[var3] = 4;
            var27 = var3 + 1;
            var4[var27] = var2;
            var5[var27] = 8;
            var7 = var27++;

            for(var8 = 0; var8 < var2; ++var8) {
               var4[var8 + var27] = var1[var8] & 255;
               var5[var8 + var27] = 8;
            }

            var27 += var2;
         }

         var8 = 0;

         for(int var40 = 0; var40 < var27; ++var40) {
            var8 += var5[var40];
         }

         byte var411;
         switch(this.a) {
         case 72:
            var411 = 2;
            break;
         case 76:
            var411 = 1;
            break;
         case 81:
            var411 = 3;
            break;
         default:
            var411 = 0;
         }

         int[][] var42 = new int[][]{{0, 128, 224, 352, 512, 688, 864, 992, 1232, 1456, 1728, 2032, 2320, 2672, 2920, 3320, 3624, 4056, 4504, 5016, 5352, 5712, 6256, 6880, 7312, 8000, 8496, 9024, 9544, 10136, 10984, 11640, 12328, 13048, 13800, 14496, 15312, 15936, 16816, 17728, 18672}, {0, 152, 272, 440, 640, 864, 1088, 1248, 1552, 1856, 2192, 2592, 2960, 3424, 3688, 4184, 4712, 5176, 5768, 6360, 6888, 7456, 8048, 8752, 9392, 10208, 10960, 11744, 12248, 13048, 13880, 14744, 15640, 16568, 17528, 18448, 19472, 20528, 21616, 22496, 23648}, {0, 72, 128, 208, 288, 368, 480, 528, 688, 800, 976, 1120, 1264, 1440, 1576, 1784, 2024, 2264, 2504, 2728, 3080, 3248, 3536, 3712, 4112, 4304, 4768, 5024, 5288, 5608, 5960, 6344, 6760, 7208, 7688, 7888, 8432, 8768, 9136, 9776, 10208}, {0, 104, 176, 272, 384, 496, 608, 704, 880, 1056, 1232, 1440, 1648, 1952, 2088, 2360, 2600, 2936, 3176, 3560, 3880, 4096, 4544, 4912, 5312, 5744, 6032, 6464, 6968, 7288, 7880, 8264, 8920, 9368, 9848, 10288, 10832, 11408, 12016, 12656, 13328}};
         int var25 = 0;
         if(this.c == 0) {
            this.c = 1;

            for(var2 = 1; var2 <= 40; ++var2) {
               if(var42[var411][var2] >= var8 + var6[this.c]) {
                  var25 = var42[var411][var2];
                  break;
               }

               ++this.c;
            }
         } else {
            var25 = var42[var411][this.c];
         }

         var8 += var6[this.c];
         var5[var7] = (byte)(var5[var7] + var6[this.c]);
         var2 = (new int[]{0, 26, 44, 70, 100, 134, 172, 196, 242, 292, 346, 404, 466, 532, 581, 655, 733, 815, 901, 991, 1085, 1156, 1258, 1364, 1474, 1588, 1706, 1828, 1921, 2051, 2185, 2323, 2465, 2611, 2761, 2876, 3034, 3196, 3362, 3532, 3706})[this.c];
         int var10000 = this.c;
         byte[] var39 = new byte[var7 = (var6 = new int[]{0, 0, 7, 7, 7, 7, 7, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0})[this.c] + (var2 << 3)];
         byte[] var11 = new byte[var7];
         byte[] var34 = new byte[var7];
         byte[] var12 = new byte[15];
         byte[] var13 = new byte[15];
         byte[] var14 = new byte[1];
         byte[] var15 = new byte[128];

         String var16;
         try {
            var16 = "qrcode_data/qrv" + Integer.toString(this.c) + "_" + Integer.toString(var411) + ".dat";
            InputStream var41 = a.class.getResourceAsStream(var16);
            BufferedInputStream var43;
            (var43 = new BufferedInputStream(var41)).read(var39);
            var43.read(var11);
            var43.read(var34);
            var43.read(var12);
            var43.read(var13);
            var43.read(var14);
            var43.read(var15);
            var43.close();
            var41.close();
         } catch (Exception var391) {
            var16 = null;
            var391.printStackTrace();
         }

         byte var431 = 1;

         for(byte var441 = 1; var441 < 128; ++var441) {
            if(var15[var441] == 0) {
               var431 = var441;
               break;
            }
         }

         byte[] var45 = new byte[var431];
         System.arraycopy(var15, 0, var45, 0, var431);
         byte[] var44 = new byte[]{(byte)0, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)7, (byte)8, (byte)8, (byte)8, (byte)8, (byte)8, (byte)8, (byte)8, (byte)8};
         var15 = new byte[]{(byte)8, (byte)8, (byte)8, (byte)8, (byte)8, (byte)8, (byte)8, (byte)8, (byte)7, (byte)5, (byte)4, (byte)3, (byte)2, (byte)1, (byte)0};
         int var18 = var25 >> 3;
         int var19;
         var10000 = var19 = 4 * this.c + 17;
         byte[] var20 = new byte[var10000 * var10000 + var19];

         String var21;
         try {
            var21 = "qrcode_data/qrvfr" + Integer.toString(this.c) + ".dat";
            InputStream var46 = a.class.getResourceAsStream(var21);
            BufferedInputStream var47;
            (var47 = new BufferedInputStream(var46)).read(var20);
            var47.close();
            var46.close();
         } catch (Exception var381) {
            var21 = null;
            var381.printStackTrace();
         }

         if(var8 <= var25 - 4) {
            var4[var27] = 0;
            var5[var27] = 4;
         } else if(var8 < var25) {
            var4[var27] = 0;
            var5[var27] = (byte)(var25 - var8);
         } else if(var8 > var25) {
            System.out.println("overflow");
         }

         byte[] var461 = a(a(var4, var5, var18), var14[0], var45, var18, var2);
         byte[][] var471 = new byte[var19][var19];

         for(var25 = 0; var25 < var19; ++var25) {
            for(var27 = 0; var27 < var19; ++var27) {
               var471[var27][var25] = 0;
            }
         }

         int var28;
         int var31;
         byte var35;
         for(var25 = 0; var25 < var2; ++var25) {
            var35 = var461[var25];

            for(var28 = 7; var28 >= 0; --var28) {
               var31 = var25 * 8 + var28;
               var471[var39[var31] & 255][var11[var31] & 255] = (byte)(255 * (var35 & 1) ^ var34[var31]);
               var35 = (byte)((var35 & 255) >>> 1);
            }
         }

         for(var25 = var6[this.c]; var25 > 0; --var25) {
            var27 = var25 + var2 * 8 - 1;
            var471[var39[var27] & 255][var11[var27] & 255] = (byte)(255 ^ var34[var27]);
         }

         byte var29 = a(var471, var6[this.c] + var2 * 8);
         var35 = (byte)(1 << var29);
         byte var30 = (byte)(var411 << 3 | var29);
         String[] var32 = new String[]{"101010000010010", "101000100100101", "101111001111100", "101101101001011", "100010111111001", "100000011001110", "100111110010111", "100101010100000", "111011111000100", "111001011110011", "111110110101010", "111100010011101", "110011000101111", "110001100011000", "110110001000001", "110100101110110", "001011010001001", "001001110111110", "001110011100111", "001100111010000", "000011101100010", "000001001010101", "000110100001100", "000100000111011", "011010101011111", "011000001101000", "011111100110001", "011101000000110", "010010010110100", "010000110000011", "010111011011010", "010101111101101"};

         for(var25 = 0; var25 < 15; ++var25) {
            byte var33 = Byte.parseByte(var32[var30].substring(var25, var25 + 1));
            var471[var44[var25] & 255][var15[var25] & 255] = (byte)(var33 * 255);
            var471[var12[var25] & 255][var13[var25] & 255] = (byte)(var33 * 255);
         }

         boolean[][] var48 = new boolean[var19][var19];
         var2 = 0;

         for(var28 = 0; var28 < var19; ++var28) {
            for(var31 = 0; var31 < var19; ++var31) {
               if((var471[var31][var28] & var35) == 0 && var20[var2] != 49) {
                  var48[var31][var28] = false;
               } else {
                  var48[var31][var28] = true;
               }

               ++var2;
            }

            ++var2;
         }

         return var48;
      }
   }

   private static byte[] a(int[] var0, byte[] var1, int var2) {
      int var3 = var1.length;
      int var5 = 0;
      int var6 = 8;
      int var4 = 0;

      for(int var11 = 0; var11 < var3; ++var11) {
         var4 += var1[var11];
      }

      var4 = (var4 - 1) / 8 + 1;
      byte[] var111 = new byte[var2];

      int var10;
      for(var10 = 0; var10 < var4; ++var10) {
         var111[var10] = 0;
      }

      for(var10 = 0; var10 < var3; ++var10) {
         var4 = var0[var10];
         int var10000 = var1[var10];
         boolean var8 = true;
         if(var10000 == 0) {
            break;
         }

         while(var8) {
            if(var6 > var10000) {
               var111[var5] = (byte)(var111[var5] << var10000 | var4);
               var6 -= var10000;
               var8 = false;
            } else {
               var10000 -= var6;
               var111[var5] = (byte)(var111[var5] << var6 | var4 >> var10000);
               if(var10000 == 0) {
                  var8 = false;
               } else {
                  var4 &= (1 << var10000) - 1;
                  var8 = true;
               }

               ++var5;
               var6 = 8;
            }
         }
      }

      if(var6 != 8) {
         var111[var5] = (byte)(var111[var5] << var6);
      } else {
         --var5;
      }

      if(var5 >= var2 - 1) {
         return var111;
      } else {
         label52:
         while(true) {
            for(boolean var12 = true; var5 < var2 - 1; var12 = false) {
               ++var5;
               if(var12) {
                  var111[var5] = -20;
               } else {
                  var111[var5] = 17;
               }

               if(!var12) {
                  continue label52;
               }
            }

            return var111;
         }
      }
   }

   private static byte[] a(byte[] var0, byte var1, byte[] var2, int var3, int var4) {
      byte[][] var5 = new byte[256][var1];

      String var6;
      try {
         var6 = "qrcode_data/rsc" + Byte.toString(var1) + ".dat";
         InputStream var15 = a.class.getResourceAsStream(var6);
         BufferedInputStream var17 = new BufferedInputStream(var15);

         for(int var18 = 0; var18 < 256; ++var18) {
            var17.read(var5[var18]);
         }

         var17.close();
         var15.close();
      } catch (Exception var151) {
         var6 = null;
         var151.printStackTrace();
      }

      int var161 = 0;
      int var171 = 0;
      byte[][] var181 = new byte[var2.length][];
      byte[] var13 = new byte[var4];
      System.arraycopy(var0, 0, var13, 0, var0.length);

      int var14;
      for(var14 = 0; var14 < var2.length; ++var14) {
         var181[var14] = new byte[(var2[var14] & 255) - var1];
      }

      for(var14 = 0; var14 < var3; ++var14) {
         var181[var171][var161] = var0[var14];
         ++var161;
         if(var161 >= (var2[var171] & 255) - var1) {
            var161 = 0;
            ++var171;
         }
      }

      for(var171 = 0; var171 < var2.length; ++var171) {
         byte[] var12 = (byte[])((byte[])var181[var171].clone());

         for(var161 = (var2[var171] & 255) - var1; var161 > 0; --var161) {
            byte[] var10;
            byte var16;
            if((var16 = var12[0]) != 0) {
               var10 = new byte[var12.length - 1];
               System.arraycopy(var12, 1, var10, 0, var12.length - 1);
               var12 = var5[var16 & 255];
               var12 = a(var10, var12, "xor");
            } else if(var1 < var12.length) {
               var10 = new byte[var12.length - 1];
               System.arraycopy(var12, 1, var10, 0, var12.length - 1);
               var12 = (byte[])((byte[])var10.clone());
            } else {
               var10 = new byte[var1];
               System.arraycopy(var12, 1, var10, 0, var12.length - 1);
               var10[var1 - 1] = 0;
               var12 = (byte[])((byte[])var10.clone());
            }
         }

         System.arraycopy(var12, 0, var13, var0.length + var171 * var1, var1);
      }

      return var13;
   }

   private static byte[] a(byte[] var0, byte[] var1, String var2) {
      byte[] var4;
      byte[] var5;
      if(var0.length > var1.length) {
         var4 = (byte[])((byte[])var0.clone());
         var5 = (byte[])((byte[])var1.clone());
      } else {
         var4 = (byte[])((byte[])var1.clone());
         var5 = (byte[])((byte[])var0.clone());
      }

      int var7 = var4.length;
      int var8 = var5.length;
      byte[] var3 = new byte[var7];

      for(int var6 = 0; var6 < var7; ++var6) {
         if(var6 < var8) {
            if(var2 == "xor") {
               var3[var6] = (byte)(var4[var6] ^ var5[var6]);
            } else {
               var3[var6] = (byte)(var4[var6] | var5[var6]);
            }
         } else {
            var3[var6] = var4[var6];
         }
      }

      return var3;
   }

   private static byte a(byte[][] var0, int var1) {
      int var2 = var0.length;
      int[] var3 = new int[8];
      int[] var4 = new int[8];
      int[] var5 = new int[8];
      int[] var6 = new int[8];
      int var7 = 0;
      int var8 = 0;
      int[] var9 = new int[8];

      int var10;
      int[] var12;
      for(var10 = 0; var10 < var2; ++var10) {
         int[] var17 = new int[8];
         var12 = new int[8];
         boolean[] var18 = new boolean[8];
         boolean[] var19 = new boolean[8];

         for(int var15 = 0; var15 < var2; ++var15) {
            if(var15 > 0 && var10 > 0) {
               var7 = var0[var15][var10] & var0[var15 - 1][var10] & var0[var15][var10 - 1] & var0[var15 - 1][var10 - 1] & 255;
               var8 = var0[var15][var10] & 255 | var0[var15 - 1][var10] & 255 | var0[var15][var10 - 1] & 255 | var0[var15 - 1][var10 - 1] & 255;
            }

            for(int var16 = 0; var16 < 8; ++var16) {
               var17[var16] = (var17[var16] & 63) << 1 | (var0[var15][var10] & 255) >>> var16 & 1;
               var12[var16] = (var12[var16] & 63) << 1 | (var0[var10][var15] & 255) >>> var16 & 1;
               if((var0[var15][var10] & 1 << var16) != 0) {
                  ++var9[var16];
               }

               if(var17[var16] == 93) {
                  var5[var16] += 40;
               }

               if(var12[var16] == 93) {
                  var5[var16] += 40;
               }

               if(var15 > 0 && var10 > 0) {
                  if((var7 & 1) != 0 || (var8 & 1) == 0) {
                     var4[var16] += 3;
                  }

                  var7 >>= 1;
                  var8 >>= 1;
               }

               if((var17[var16] & 31) != 0 && (var17[var16] & 31) != 31) {
                  var18[var16] = false;
               } else if(var15 > 3) {
                  if(var18[var16]) {
                     ++var3[var16];
                  } else {
                     var3[var16] += 3;
                     var18[var16] = true;
                  }
               }

               if((var12[var16] & 31) != 0 && (var12[var16] & 31) != 31) {
                  var19[var16] = false;
               } else if(var15 > 3) {
                  if(var19[var16]) {
                     ++var3[var16];
                  } else {
                     var3[var16] += 3;
                     var19[var16] = true;
                  }
               }
            }
         }
      }

      var10 = 0;
      byte var181 = 0;
      var12 = new int[]{90, 80, 70, 60, 50, 40, 30, 20, 10, 0, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 90};

      for(int var191 = 0; var191 < 8; ++var191) {
         var6[var191] = var12[20 * var9[var191] / var1];
         int var20;
         if((var20 = var3[var191] + var4[var191] + var5[var191] + var6[var191]) < var10 || var191 == 0) {
            var181 = (byte)var191;
            var10 = var20;
         }
      }

      return var181;
   }
}

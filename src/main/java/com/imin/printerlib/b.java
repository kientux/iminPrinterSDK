package com.imin.printerlib;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class b {

   public static String a(String var0) {
      String var1 = "";
      FileInputStream var2 = null;
      boolean var23 = false;

      label304: {
         label305: {
            label306: {
               try {
                  var23 = true;
                  var2 = new FileInputStream(var0);
                  byte[] var27 = new byte[14];
                  var2.read(var27, 0, 14);
                  var27 = new byte[]{var27[0], var27[1]};
                  if(!"BM".equals(new String(var27))) {
                     System.out.println("Image format is not correct...");
                     var23 = false;
                     break label305;
                  }

                  var27 = new byte[40];
                  var2.read(var27, 0, 40);
                  int var3 = (var27[7] & 255) << 24 | (var27[6] & 255) << 16 | (var27[5] & 255) << 8 | var27[4] & 255;
                  int var4 = (var27[11] & 255) << 24 | (var27[10] & 255) << 16 | (var27[9] & 255) << 8 | var27[8] & 255;
                  int var32 = (var27[15] & 255) << 8 | var27[14] & 255;
                  int var5;
                  byte[] var6 = new byte[var5 = (int)StrictMath.pow(2.0D, (double)var32 * 1.0D) * 4];
                  var2.read(var6, 0, var5);
                  boolean var34 = false;
                  if(var6[0] != 0) {
                     var34 = true;
                  }

                  byte var35 = 0;
                  if(var32 != 1) {
                     System.out.println("Resource is not monochromatic image...");
                     var23 = false;
                     break label306;
                  }

                  int var7 = (var3 * var32 + 31 & -32) >> 3;
                  var32 = var3 * var32 / 8 * 8 - 1;
                  int var8 = var7 << 3;
                  int var9 = var7 * var4 + 8;
                  int var10 = var4 - 1;
                  String[] var11 = new String[var9];
                  String[] var12 = new String[var9];
                  boolean var13 = false;
                  var3 = 8 - var3 % 8;
                  int var36 = var35 + 1;
                  var11[0] = "1D";
                  ++var36;
                  var11[1] = "76";
                  ++var36;
                  var11[2] = "30";
                  ++var36;
                  var11[3] = "0";
                  ++var36;
                  var11[4] = Integer.toHexString(var7);
                  ++var36;
                  var11[5] = Integer.toHexString(var7 >> 8);
                  ++var36;
                  var11[6] = Integer.toHexString(var4 % 256);
                  ++var36;
                  var11[7] = Integer.toHexString(var4 >> 8);
                  byte[] var33 = new byte[var9];
                  var2.read(var33, 0, var9 - 8);
                  boolean var14 = false;

                  int var15;
                  for(var15 = 0; var36 < var9; ++var36) {
                     byte var24 = var33[var15];
                     if(!var34) {
                        int var37;
                        if((var37 = (var36 - 8) * 8 % var8) >= var32) {
                           if(var37 - var32 < 8) {
                              for(var37 = 0; var37 < var3; ++var37) {
                                 var24 = (byte)(var24 + (1 << var37) & 255);
                              }
                           } else {
                              var24 = -1;
                           }
                        }

                        var24 = (byte)(255 - var24);
                     }

                     var11[var15 + 8] = Integer.toHexString(var24 & 255);
                     ++var15;
                  }

                  for(var15 = 0; var15 < 8; ++var15) {
                     var12[var15] = var11[var15];
                  }

                  var32 = var10;

                  while(true) {
                     if(var32 < 0) {
                        var1 = a(var12);
                        var23 = false;
                        break;
                     }

                     for(var3 = 0; var3 < var7; ++var3) {
                        var12[var15++] = var11[var32 * var7 + var3 + 8];
                     }

                     --var32;
                  }
               } catch (Exception var43) {
                  var43.printStackTrace();
                  var23 = false;
                  break label304;
               } finally {
                  if(var23 && var2 != null) {
                     try {
                        var2.close();
                     } catch (IOException var38) {
                        var38.printStackTrace();
                     }
                  }

               }

               try {
                  var2.close();
               } catch (IOException var42) {
                  var42.printStackTrace();
               }

               return var1;
            }

            try {
               var2.close();
            } catch (IOException var41) {
               var41.printStackTrace();
            }

            return "";
         }

         try {
            var2.close();
         } catch (IOException var40) {
            var40.printStackTrace();
         }

         return "";
      }

      if(var2 != null) {
         try {
            var2.close();
         } catch (IOException var39) {
            var39.printStackTrace();
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static byte[] b(String var0) {
      FileInputStream var1 = null;
      boolean var22 = false;

      byte[] var23;
      label300: {
         label301: {
            label302: {
               try {
                  var22 = true;
                  var1 = new FileInputStream(var0);
                  var23 = new byte[14];
                  var1.read(var23, 0, 14);
                  var23 = new byte[]{var23[0], var23[1]};
                  if("BM".equals(new String(var23))) {
                     var23 = new byte[40];
                     var1.read(var23, 0, 40);
                     int var25 = (var23[7] & 255) << 24 | (var23[6] & 255) << 16 | (var23[5] & 255) << 8 | var23[4] & 255;
                     int var24 = (var23[11] & 255) << 24 | (var23[10] & 255) << 16 | (var23[9] & 255) << 8 | var23[8] & 255;
                     int var31 = (var23[15] & 255) << 8 | var23[14] & 255;
                     int var4;
                     byte[] var5 = new byte[var4 = (int)StrictMath.pow(2.0D, (double)var31 * 1.0D) * 4];
                     var1.read(var5, 0, var4);
                     boolean var33 = false;
                     if(var5[0] != 0) {
                        var33 = true;
                     }

                     byte var34 = 0;
                     if(var31 == 1) {
                        int var241 = (var25 * var31 + 31 & -32) >> 3;
                        var31 = var25 * var31 / 8 * 8 - 1;
                        int var7 = var241 << 3;
                        int var8 = var241 * var24 + 8;
                        int var9 = var24 - 1;
                        boolean var10 = false;
                        String[] var11 = new String[var8];
                        byte[] var12 = new byte[var8];
                        var25 = 8 - var25 % 8;
                        int var35 = var34 + 1;
                        var11[0] = "1D";
                        ++var35;
                        var11[1] = "76";
                        ++var35;
                        var11[2] = "30";
                        ++var35;
                        var11[3] = "0";
                        ++var35;
                        var11[4] = Integer.toHexString(var241);
                        ++var35;
                        var11[5] = Integer.toHexString(var241 >> 8);
                        ++var35;
                        var11[6] = Integer.toHexString(var24 % 256);
                        ++var35;
                        var11[7] = Integer.toHexString(var24 >> 8);
                        byte[] var32 = new byte[var8];
                        var1.read(var32, 0, var8 - 8);
                        boolean var13 = false;

                        int var14;
                        int var36;
                        for(var14 = 0; var35 < var8; ++var35) {
                           byte var242 = var32[var14];
                           if(!var33) {
                              if((var36 = (var35 - 8) * 8 % var7) >= var31) {
                                 if(var36 - var31 < 8) {
                                    for(var36 = 0; var36 < var25; ++var36) {
                                       var242 = (byte)(var242 + (1 << var36) & 255);
                                    }
                                 } else {
                                    var242 = -1;
                                 }
                              }

                              var242 = (byte)(255 - var242);
                           }

                           var11[var14 + 8] = Integer.toHexString(var242 & 255);
                           ++var14;
                        }

                        for(var14 = 0; var14 < 8; ++var14) {
                           var36 = Integer.valueOf(var11[var14], 16).intValue();
                           var12[var14] = (byte)var36;
                        }

                        for(var31 = var9; var31 >= 0; --var31) {
                           for(var25 = 0; var25 < var241; ++var25) {
                              var36 = Integer.valueOf(var11[var31 * var241 + var25 + 8], 16).intValue();
                              var12[var14++] = (byte)var36;
                           }
                        }

                        var23 = var12;
                        var22 = false;
                        break label300;
                     }

                     System.out.println("Resource is not monochromatic image...");
                     var22 = false;
                     break label301;
                  }

                  System.out.println("Image format is not correct...");
                  var22 = false;
                  break label302;
               } catch (Exception var42) {
                  var42.printStackTrace();
                  var22 = false;
               } finally {
                  if(var22 && var1 != null) {
                     try {
                        var1.close();
                     } catch (IOException var37) {
                        var37.printStackTrace();
                     }
                  }

               }

               if(var1 != null) {
                  try {
                     var1.close();
                  } catch (IOException var38) {
                     var38.printStackTrace();
                  }
               }

               return null;
            }

            try {
               var1.close();
            } catch (IOException var41) {
               var41.printStackTrace();
            }

            return null;
         }

         try {
            var1.close();
         } catch (IOException var40) {
            var40.printStackTrace();
         }

         return null;
      }

      try {
         var1.close();
      } catch (IOException var39) {
         var39.printStackTrace();
      }

      return var23;
   }

   private static byte[] d(String var0) {
      FileInputStream var1 = null;
      boolean var23 = false;

      label289: {
         label290: {
            label291: {
               byte[] var27;
               try {
                  var23 = true;
                  var1 = new FileInputStream(var0);
                  var27 = new byte[14];
                  var1.read(var27, 0, 14);
                  var27 = new byte[]{var27[0], var27[1]};
                  if(!"BM".equals(new String(var27))) {
                     System.out.println("Image format is not correct...");
                     var23 = false;
                     break label290;
                  }

                  var27 = new byte[40];
                  var1.read(var27, 0, 40);
                  int var26 = (var27[7] & 255) << 24 | (var27[6] & 255) << 16 | (var27[5] & 255) << 8 | var27[4] & 255;
                  int var3;
                  int var4 = (var3 = (var27[11] & 255) << 24 | (var27[10] & 255) << 16 | (var27[9] & 255) << 8 | var27[8] & 255) % 2;
                  int var32 = (var27[15] & 255) << 8 | var27[14] & 255;
                  int var5;
                  byte[] var6 = new byte[var5 = (int)StrictMath.pow(2.0D, (double)var32 * 1.0D) * 4];
                  var1.read(var6, 0, var5);
                  boolean var34 = false;
                  if(var6[0] != 0) {
                     var34 = true;
                  }

                  byte var35 = 0;
                  if(var32 != 1) {
                     System.out.println("Resource is not monochromatic image...");
                     var23 = false;
                     break label291;
                  }

                  int var7 = (var26 * var32 + 31 & -32) >> 3;
                  int var8 = var26 * var32;
                  int var9 = var7 << 3;
                  int var10;
                  String[] var11 = new String[var10 = var7 * (var3 + var4) + 8];
                  byte[] var12 = new byte[var10];
                  var26 = 8 - var26 % 8;
                  int var36 = var35 + 1;
                  var11[0] = "1D";
                  ++var36;
                  var11[1] = "76";
                  ++var36;
                  var11[2] = "30";
                  ++var36;
                  var11[3] = "0";
                  ++var36;
                  var11[4] = Integer.toHexString(var7);
                  ++var36;
                  var11[5] = Integer.toHexString(var7 >> 8);
                  ++var36;
                  var11[6] = Integer.toHexString((var3 + var4) % 256);
                  ++var36;
                  var11[7] = Integer.toHexString(var3 + var4 >> 8);
                  byte[] var33 = new byte[var10];
                  var1.read(var33, 0, var10 - var7 * var4 - 8);
                  int var13 = var7;
                  boolean var14 = false;
                  int var25 = 0;

                  while(true) {
                     if(var36 >= var10 - var7 * var4) {
                        for(var36 = 0; var36 < 8; ++var36) {
                           var32 = Integer.valueOf(var11[var36], 16).intValue();
                           var12[var36] = (byte)var32;
                        }

                        for(var36 = 8; var36 < var10 - var7 * var4; ++var36) {
                           var32 = Integer.valueOf(var11[var10 - 1 - var36 + 8 - var7 * var4], 16).intValue();
                           var12[var36] = (byte)var32;
                        }

                        var27 = var12;
                        var23 = false;
                        break;
                     }

                     byte var37 = var33[var25++];
                     if(!var34) {
                        if((var32 = (var36 - 8) * 8 % var9 + 8) > var8) {
                           if(var32 - var8 < 8) {
                              for(var32 = 0; var32 < var26; ++var32) {
                                 var37 = (byte)(var37 + (1 << var32) & 255);
                              }
                           } else {
                              var37 = -1;
                           }
                        }

                        var37 = (byte)(255 - var37);
                     }

                     var0 = Integer.toHexString(var37 & 255);
                     var11[var36 + var13 - 1] = var0;
                     var13 -= 2;
                     if(var13 == -var7) {
                        var13 = var7;
                     }

                     ++var36;
                  }
               } catch (Exception var42) {
                  var42.printStackTrace();
                  var23 = false;
                  break label289;
               } finally {
                  if(var23 && var1 != null) {
                     try {
                        var1.close();
                     } catch (IOException var371) {
                        var371.printStackTrace();
                     }
                  }

               }

               try {
                  var1.close();
               } catch (IOException var41) {
                  var41.printStackTrace();
               }

               return var27;
            }

            try {
               var1.close();
            } catch (IOException var40) {
               var40.printStackTrace();
            }

            return null;
         }

         try {
            var1.close();
         } catch (IOException var39) {
            var39.printStackTrace();
         }

         return null;
      }

      if(var1 != null) {
         try {
            var1.close();
         } catch (IOException var38) {
            var38.printStackTrace();
         }
      }

      return null;
   }

   public static byte[] c(String var0) {
      byte[] var16;
      if((var16 = d(var0)) == null) {
         return null;
      } else {
         boolean var2 = false;

         try {
            int var15 = ((var16[5] + 256) % 256 * 256 + (var16[4] + 256) % 256) * 8;
            int var1 = (var16[7] + 256) % 256 * 256 + (var16[6] + 256) % 256;
            int[] var10 = new int[]{128, 64, 32, 16, 8, 4, 2, 1};
            int var8 = var15 >> 3;
            int var9 = var1 >> 4;
            char var11;
            if((var11 = (char)(var1 % 16)) > 0) {
               var1 = var9 + 1;
            } else {
               var1 = var9;
            }

            byte[] var14 = new byte[var1 * var8 * 2 * 8 * 8];
            int var7 = 0;

            int var3;
            int var4;
            int var5;
            int var12;
            int var13;
            int var17;
            for(var5 = 0; var5 < var9; ++var5) {
               for(var17 = 0; var17 < var8; ++var17) {
                  for(var4 = 0; var4 < 8; ++var4) {
                     var12 = var10[var4];
                     var13 = 0;

                     for(var3 = 0; var3 < 8; ++var3) {
                        if(((256 + var16[var5 * var8 * 16 + var3 * 2 * var8 + var17 + 8]) % 256 & var12) != 0) {
                           var13 += var10[var3];
                        }
                     }

                     var14[var7++] = (byte)var13;
                  }
               }

               for(var17 = 0; var17 < var8; ++var17) {
                  for(var4 = 0; var4 < 8; ++var4) {
                     var12 = var10[var4];
                     var13 = 0;

                     for(var3 = 0; var3 < 8; ++var3) {
                        if(((256 + var16[var5 * var8 * 16 + (var3 * 2 + 1) * var8 + var17 + 8]) % 256 & var12) != 0) {
                           var13 += var10[var3];
                        }
                     }

                     var14[var7++] = (byte)var13;
                  }
               }
            }

            int var18;
            if(var11 > 0) {
               var9 = var11 / 2;
               if(var11 % 2 > 0) {
                  var18 = var9 + 1;
               } else {
                  var18 = var9;
               }

               for(var17 = 0; var17 < var8; ++var17) {
                  for(var4 = 0; var4 < 8; ++var4) {
                     var12 = var10[var4];
                     var13 = 0;

                     for(var3 = 0; var3 < var9; ++var3) {
                        if(((256 + var16[var5 * var8 * 16 + var3 * 2 * var8 + var17 + 8]) % 256 & var12) != 0) {
                           var13 += var10[var3];
                        }
                     }

                     var14[var7++] = (byte)var13;
                  }
               }

               for(var17 = 0; var17 < var8; ++var17) {
                  for(var4 = 0; var4 < 8; ++var4) {
                     var12 = var10[var4];
                     var13 = 0;

                     for(var3 = 0; var3 < var18; ++var3) {
                        if(((256 + var16[var5 * var8 * 16 + (var3 * 2 + 1) * var8 + var17 + 8]) % 256 & var12) != 0) {
                           var13 += var10[var3];
                        }
                     }

                     var14[var7++] = (byte)var13;
                  }
               }
            }

            var18 = 0;
            var16 = new byte[var1 * (var15 * 2 + 16)];

            for(var17 = 0; var17 < var1; ++var17) {
               var16[var18++] = 27;
               var16[var18++] = 42;
               var16[var18++] = 1;
               var16[var18++] = (byte)(var15 % 256);
               var16[var18++] = (byte)(var15 / 256);

               for(var3 = 0; var3 < var15; ++var3) {
                  var16[var18++] = var14[var17 * 2 * var15 + var3];
               }

               var16[var18++] = 27;
               var16[var18++] = 74;
               var16[var18++] = 1;
               var16[var18++] = 27;
               var16[var18++] = 42;
               var16[var18++] = 1;
               var16[var18++] = (byte)(var15 % 256);
               var16[var18++] = (byte)(var15 / 256);

               for(var3 = 0; var3 < var15; ++var3) {
                  var16[var18++] = var14[(var17 * 2 + 1) * var15 + var3];
               }

               var16[var18++] = 27;
               var16[var18++] = 74;
               var16[var18++] = 15;
            }

            return var16;
         } catch (Exception var181) {
            var181.printStackTrace();
            return null;
         }
      }
   }

   private static byte[] e(String var0) {
      FileInputStream var1 = null;
      boolean var24 = false;

      label465: {
         label466: {
            byte[] var29;
            label467: {
               label468: {
                  try {
                     var24 = true;
                     var1 = new FileInputStream(var0);
                     var29 = new byte[14];
                     var1.read(var29, 0, 14);
                     var29 = new byte[]{var29[0], var29[1]};
                     if(!"BM".equals(new String(var29))) {
                        System.out.println("Image format is not correct...");
                        var24 = false;
                        break label466;
                     }

                     var29 = new byte[40];
                     var1.read(var29, 0, 40);
                     int var28 = (var29[7] & 255) << 24 | (var29[6] & 255) << 16 | (var29[5] & 255) << 8 | var29[4] & 255;
                     int var25 = (var29[11] & 255) << 24 | (var29[10] & 255) << 16 | (var29[9] & 255) << 8 | var29[8] & 255;
                     int var34 = (var29[15] & 255) << 8 | var29[14] & 255;
                     int var4;
                     byte[] var5 = new byte[var4 = (int)StrictMath.pow(2.0D, (double)var34 * 1.0D) * 4];
                     var1.read(var5, 0, var4);
                     boolean var35 = false;
                     if(var5[0] != 0) {
                        var35 = true;
                     }

                     byte var36 = 0;
                     if(var34 != 1) {
                        System.out.println("Resource is not monochromatic image...");
                        var24 = false;
                        break label468;
                     }

                     int var6 = (var28 * var34 + 31 & -32) >> 3;
                     var34 = var28 * var34 / 8 * 8 - 1;
                     int var7 = var6 << 3;
                     int var8;
                     if((var8 = var6 * var25 + 8) <= 65544) {
                        int var9 = var6;
                        boolean var10 = false;
                        String[] var11 = new String[var8];
                        byte[] var12 = new byte[var8 - 8];
                        var28 = 8 - var28 % 8;
                        int var38 = var36 + 1;
                        var11[0] = "1D";
                        ++var38;
                        var11[1] = "76";
                        ++var38;
                        var11[2] = "30";
                        ++var38;
                        var11[3] = "0";
                        ++var38;
                        var11[4] = Integer.toHexString(var6);
                        ++var38;
                        var11[5] = Integer.toHexString(var6 >> 8);
                        ++var38;
                        var11[6] = Integer.toHexString(var25 % 256);
                        ++var38;
                        var11[7] = Integer.toHexString(var25 >> 8);
                        byte[] var13 = new byte[var8 - 8];
                        var1.read(var13, 0, var8 - 8);

                        int var42;
                        for(boolean var37 = false; var38 < var8; ++var38) {
                           byte var40 = var13[var38 - 8];
                           if(!var35) {
                              if((var42 = (var38 - 8) * 8 % var7) >= var34) {
                                 if(var42 - var34 < 8) {
                                    for(var42 = 0; var42 < var28; ++var42) {
                                       var40 = (byte)(var40 + (1 << var42) & 255);
                                    }
                                 } else {
                                    var40 = -1;
                                 }
                              }

                              var40 = (byte)(255 - var40);
                           }

                           var11[var38 + var9 - 1] = Integer.toHexString(var40 & 255);
                           var9 -= 2;
                           if(var9 == -var6) {
                              var9 = var6;
                           }
                        }

                        for(var38 = 8; var38 < var8; ++var38) {
                           var42 = Integer.valueOf(var11[var8 - 1 + 8 - var38], 16).intValue();
                           var12[var38 - 8] = (byte)var42;
                        }

                        char[] var52 = new char[]{'\u0080', '@', ' ', '\u0010', '\b', '\u0004', '\u0002', '\u0001'};
                        var34 = var6 * 8 >> 3;
                        var28 = var25 >> 3;
                        char var53;
                        if((var53 = (char)(var25 % 8)) != 0) {
                           var9 = var28 + 1;
                        } else {
                           var9 = var28;
                        }

                        byte[] var15 = new byte[var25 = var34 * var9 * 8 + 4];

                        int var46;
                        for(var46 = 0; var46 < var25; ++var46) {
                           var15[var46] = -1;
                        }

                        for(var46 = 0; var46 < var34; ++var46) {
                           char var39;
                           char var41;
                           int var43;
                           int var44;
                           for(var42 = 0; var42 < var28; ++var42) {
                              for(var44 = 0; var44 < 8; ++var44) {
                                 var39 = var52[var44];
                                 var41 = 0;

                                 for(var43 = 0; var43 < 8; ++var43) {
                                    if(((char)var12[(var42 * 8 + var43) * var34 + var46] & var39) != 0) {
                                       var41 += var52[var43];
                                    }
                                 }

                                 var15[(var46 * 8 + var44) * var9 + var42] = (byte)var41;
                              }
                           }

                           if(var53 != 0) {
                              for(var44 = 0; var44 < 8; ++var44) {
                                 var39 = var52[var44];
                                 var41 = 0;

                                 for(var43 = 0; var43 < var53; ++var43) {
                                    if(((char)var12[(var42 * 8 + var43) * var34 + var46] & var39) != 0) {
                                       var41 += var52[var43];
                                    }
                                 }

                                 var15[(var46 * 8 + var44) * var9 + var42] = (byte)var41;
                              }
                           }
                        }

                        var38 = var15.length - 4;
                        var15[var38++] = (byte)var6;
                        var15[var38++] = (byte)(var6 >> 8);
                        var15[var38++] = (byte)var9;
                        var15[var38] = (byte)(var9 >> 8);
                        var29 = var15;
                        var24 = false;
                        break label467;
                     }

                     var24 = false;
                  } catch (Exception var50) {
                     var50.printStackTrace();
                     var24 = false;
                     break label465;
                  } finally {
                     if(var24 && var1 != null) {
                        try {
                           var1.close();
                        } catch (IOException var441) {
                           var441.printStackTrace();
                        }
                     }

                  }

                  try {
                     var1.close();
                  } catch (IOException var48) {
                     var48.printStackTrace();
                  }

                  return null;
               }

               try {
                  var1.close();
               } catch (IOException var47) {
                  var47.printStackTrace();
               }

               return null;
            }

            try {
               var1.close();
            } catch (IOException var49) {
               var49.printStackTrace();
            }

            return var29;
         }

         try {
            var1.close();
         } catch (IOException var461) {
            var461.printStackTrace();
         }

         return null;
      }

      if(var1 != null) {
         try {
            var1.close();
         } catch (IOException var45) {
            var45.printStackTrace();
         }
      }

      return null;
   }

   public static byte[] a(int var0, String var1) {
      if(var0 > 0 && var1.length() > 0) {
         try {
            try {
               byte[] var12 = new byte[525312];
               byte var151 = 0;
               boolean var4 = false;
               int var5 = 0;
               String var6 = null;
               String var7 = var1;
               if(var1.substring(var1.length() - 1).equals(";")) {
                  var7 = var1 + ";";
               }

               int var15 = var151 + 1;
               var12[0] = 28;
               ++var15;
               var12[1] = 113;
               ++var15;
               var12[2] = (byte)var0;

               int var16;
               byte[] var17;
               for(int var13 = 0; var13 < var0; ++var13) {
                  if((var16 = var7.indexOf(";", 0)) > 0) {
                     var6 = var7.substring(0, var16);
                     var7 = var7.substring(var16 + 1);
                     if((var17 = e(var6)) == null) {
                        return null;
                     }

                     var16 = var17.length;

                     int var8;
                     for(var8 = 4; var8 > 0; --var8) {
                        var12[var15++] = var17[var16 - var8];
                     }

                     for(var8 = 0; var8 < var16 - 4; ++var8) {
                        var12[var15++] = var17[var8];
                     }

                     ++var5;
                  }
               }

               if(var15 > 524288) {
                  return null;
               }

               if(var5 == var0) {
                  var16 = var15;
                  var17 = new byte[var15];

                  for(var15 = 0; var15 < var16; ++var15) {
                     var17[var15] = var12[var15];
                  }

                  return var17;
               }
            } catch (Exception var131) {
               Object var2 = null;
               var131.printStackTrace();
            }

            return null;
         } catch (Throwable var14) {
            throw var14;
         }
      } else {
         return null;
      }
   }

   private static byte[] a(byte[] var0) {
      try {
         byte[] var15 = new byte[14];
         System.arraycopy(var0, 0, var15, 0, 14);
         var15 = new byte[]{var15[0], var15[1]};
         if(!"BM".equals(new String(var15))) {
            System.out.println("Image format is not correct...");
            return null;
         } else {
            var15 = new byte[40];
            System.arraycopy(var0, 14, var15, 0, 40);
            int var261 = (var15[7] & 255) << 24 | (var15[6] & 255) << 16 | (var15[5] & 255) << 8 | var15[4] & 255;
            int var3 = (var15[11] & 255) << 24 | (var15[10] & 255) << 16 | (var15[9] & 255) << 8 | var15[8] & 255;
            int var4 = (var15[15] & 255) << 8 | var15[14] & 255;
            int var19;
            byte[] var5 = new byte[var19 = (int)StrictMath.pow(2.0D, (double)var4 * 1.0D) * 4];
            System.arraycopy(var0, 54, var5, 0, var19);
            var19 += 54;
            boolean var6 = false;
            if(var5[0] != 0) {
               var6 = true;
            }

            byte var23 = 0;
            if(var4 != 1) {
               System.out.println("Resource is not monochromatic image...");
               return null;
            } else {
               int var7 = (var261 * var4 + 31 & -32) >> 3;
               var4 = var261 * var4 / 8 * 8 - 1;
               int var8 = var7 << 3;
               int var9;
               if((var9 = var7 * var3 + 8) > 65544) {
                  return null;
               } else {
                  int var10 = var7;
                  boolean var11 = false;
                  String[] var12 = new String[var9];
                  byte[] var13 = new byte[var9 - 8];
                  var261 = 8 - var261 % 8;
                  int var24 = var23 + 1;
                  var12[0] = "1D";
                  ++var24;
                  var12[1] = "76";
                  ++var24;
                  var12[2] = "30";
                  ++var24;
                  var12[3] = "0";
                  ++var24;
                  var12[4] = Integer.toHexString(var7);
                  ++var24;
                  var12[5] = Integer.toHexString(var7 >> 8);
                  ++var24;
                  var12[6] = Integer.toHexString(var3 % 256);
                  ++var24;
                  var12[7] = Integer.toHexString(var3 >> 8);
                  byte[] var14 = new byte[var9 - 8];
                  System.arraycopy(var0, var19, var14, 0, var9 - 8);

                  int var27;
                  for(boolean var20 = false; var24 < var9; ++var24) {
                     byte var21 = var14[var24 - 8];
                     if(!var6) {
                        if((var27 = (var24 - 8) * 8 % var8) >= var4) {
                           if(var27 - var4 < 8) {
                              for(var27 = 0; var27 < var261; ++var27) {
                                 var21 = (byte)(var21 + (1 << var27) & 255);
                              }
                           } else {
                              var21 = -1;
                           }
                        }

                        var21 = (byte)(255 - var21);
                     }

                     var12[var24 + var10 - 1] = Integer.toHexString(var21 & 255);
                     var10 -= 2;
                     if(var10 == -var7) {
                        var10 = var7;
                     }
                  }

                  for(var24 = 8; var24 < var9; ++var24) {
                     var27 = Integer.valueOf(var12[var9 - 1 + 8 - var24], 16).intValue();
                     var13[var24 - 8] = (byte)var27;
                  }

                  char[] var271 = new char[]{'\u0080', '@', ' ', '\u0010', '\b', '\u0004', '\u0002', '\u0001'};
                  int var281 = var7 * 8 >> 3;
                  var19 = var3 >> 3;
                  char var25;
                  if((var25 = (char)(var3 % 8)) != 0) {
                     var8 = var19 + 1;
                  } else {
                     var8 = var19;
                  }

                  var14 = new byte[var3 = var281 * var8 * 8 + 4];

                  int var28;
                  for(var28 = 0; var28 < var3; ++var28) {
                     var14[var28] = -1;
                  }

                  for(var28 = 0; var28 < var281; ++var28) {
                     char var22;
                     char var26;
                     for(var9 = 0; var9 < var19; ++var9) {
                        for(var27 = 0; var27 < 8; ++var27) {
                           var26 = var271[var27];
                           var22 = 0;

                           for(var10 = 0; var10 < 8; ++var10) {
                              if(((char)var13[(var9 * 8 + var10) * var281 + var28] & var26) != 0) {
                                 var22 += var271[var10];
                              }
                           }

                           var14[(var28 * 8 + var27) * var8 + var9] = (byte)var22;
                        }
                     }

                     if(var25 != 0) {
                        for(var27 = 0; var27 < 8; ++var27) {
                           var26 = var271[var27];
                           var22 = 0;

                           for(var10 = 0; var10 < var25; ++var10) {
                              if(((char)var13[(var9 * 8 + var10) * var281 + var28] & var26) != 0) {
                                 var22 += var271[var10];
                              }
                           }

                           var14[(var28 * 8 + var27) * var8 + var9] = (byte)var22;
                        }
                     }
                  }

                  var24 = var14.length - 4;
                  var14[var24++] = (byte)var7;
                  var14[var24++] = (byte)(var7 >> 8);
                  var14[var24++] = (byte)var8;
                  var14[var24] = (byte)(var8 >> 8);
                  return var14;
               }
            }
         }
      } catch (Exception var251) {
         Object var16 = null;
         var251.printStackTrace();
         return null;
      }
   }

   public static byte[] a(InputStream[] var0) {
      int var1 = var0.length;

      try {
         byte[] var8 = new byte[197632];
         byte var3 = 0;
         boolean var5 = false;
         int var9 = var3 + 1;
         var8[0] = 28;
         ++var9;
         var8[1] = 113;
         ++var9;
         var8[2] = (byte)var1;

         byte[] var6;
         int var10;
         for(int var4 = 0; var4 < var1; ++var4) {
            var6 = new byte[var0[var4].available()];
            var0[var4].read(var6);
            if((var6 = a(var6)) == null) {
               return null;
            }

            var10 = var6.length;

            int var7;
            for(var7 = 4; var7 > 0; --var7) {
               var8[var9++] = var6[var10 - var7];
            }

            for(var7 = 0; var7 < var10 - 4; ++var7) {
               var8[var9++] = var6[var7];
            }
         }

         if(var9 > 196608) {
            return null;
         } else {
            var10 = var9;
            var6 = new byte[var9];

            for(var9 = 0; var9 < var10; ++var9) {
               var6[var9] = var8[var9];
            }

            return var6;
         }
      } catch (Exception var101) {
         var101.printStackTrace();
         return null;
      }
   }

   private static String a(String[] var0) {
      StringBuilder var1 = new StringBuilder();
      int var2 = 0;
      String[] var6 = var0;
      int var5 = var0.length;

      for(int var7 = 0; var7 < var5; ++var7) {
         String var3 = var6[var7];
         ++var2;
         if(var3.length() == 1) {
            var3 = "0" + var3;
         }

         var1.append(var3);
         if(var2 < var0.length) {
            var1.append(" ");
         }
      }

      String var71 = var1.toString();
      var1.delete(0, var71.length());
      return var71;
   }

   public static boolean a(String var0, String var1) {
      boolean var2 = false;

      FileInputStream var3;
      try {
         var3 = null;
         var3 = null;
         FileOutputStream var28 = null;
         byte[] var6 = new byte[8];
         byte[] var7 = new byte[3];
         boolean var21 = false;

         label243: {
            try {
               var21 = true;
               var3 = new FileInputStream(var0);
               var28 = new FileOutputStream(var1);
               byte[] var23 = new byte[14];
               var3.read(var23, 0, 14);
               byte[] var31 = new byte[]{var23[0], var23[1]};
               boolean var8 = false;
               if("BM".equals(new String(var31))) {
                  var31 = new byte[40];
                  var3.read(var31, 0, 40);
                  int var22 = (var31[7] & 255) << 24 | (var31[6] & 255) << 16 | (var31[5] & 255) << 8 | var31[4] & 255;
                  int var11 = (var31[11] & 255) << 24 | (var31[10] & 255) << 16 | (var31[9] & 255) << 8 | var31[8] & 255;
                  int var5 = (var31[15] & 255) << 8 | var31[14] & 255;
                  StrictMath.pow(2.0D, (double)var5 * 1.0D);
                  var23[10] = 62;
                  int var30 = (var22 + 31 & -32) / 8 * var11 + 62;
                  var23[2] = (byte)var30;
                  var23[3] = (byte)(var30 >> 8);
                  var23[4] = (byte)(var30 >> 16);
                  var23[5] = (byte)(var30 >> 24);
                  var28.write(var23);
                  var31[14] = 1;
                  var28.write(var31);
                  var6[0] = 0;
                  var6[1] = 0;
                  var6[2] = 0;
                  var6[3] = 0;
                  var6[4] = -1;
                  var6[5] = -1;
                  var6[6] = -1;
                  var6[7] = 0;
                  var28.write(var6);

                  for(int var221 = 0; var221 < var11; ++var221) {
                     byte var36 = 0;
                     var30 = 0;
                     int var9 = 0;

                     int var13;
                     for(var13 = 0; var13 < var22; ++var13) {
                        try {
                           var3.read(var7, 0, 3);
                        } catch (Exception var37) {
                           break;
                        }

                        byte var35 = (byte)(var7[2] >> 16);
                        byte var34 = (byte)(var7[1] >> 8);
                        byte var32 = var7[0];
                        if((byte)(77 * var32 + 151 * var34 + 28 * var35 >> 8) <= 0) {
                           var36 = (byte)(var36 | 1);
                        }

                        ++var9;
                        if(var9 == 8) {
                           ++var30;
                           var28.write(var36);
                           var36 = 0;
                           var9 = 0;
                        }

                        var36 = (byte)(var36 << 1);
                     }

                     if(var9 != 0) {
                        var36 = (byte)(var36 << 8 - var9);
                        var28.write(var36);
                        ++var30;
                     }

                     if(var30 % 4 != 0) {
                        for(var13 = 0; var13 < 4 - var30 % 4; ++var13) {
                           var28.write(0);
                        }
                     }
                  }

                  var28.close();
                  var3.close();
                  var2 = true;
                  var21 = false;
               } else {
                  System.out.println("Image format is not correct...");
                  var21 = false;
               }
               break label243;
            } catch (Exception var38) {
               var38.printStackTrace();
               var21 = false;
            } finally {
               if(var21 && var3 != null) {
                  try {
                     var3.close();
                  } catch (IOException var351) {
                     var351.printStackTrace();
                  }
               }

            }

            if(var3 != null) {
               try {
                  var3.close();
               } catch (IOException var341) {
                  var341.printStackTrace();
               }

               return var2;
            }

            return var2;
         }

         try {
            var3.close();
         } catch (IOException var361) {
            var361.printStackTrace();
         }
      } catch (Exception var40) {
         var3 = null;
         var40.printStackTrace();
      }

      return var2;
   }
}

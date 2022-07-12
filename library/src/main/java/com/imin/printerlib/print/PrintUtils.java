//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.imin.printerlib.print;

import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.imin.printerlib.BluetoothPrintUtil;
import com.imin.printerlib.enums.DirectionEnum;
import com.imin.printerlib.enums.PrinterFontEnum;
import com.imin.printerlib.port.UsbPrinter;
import com.imin.printerlib.serial.SerialControl;
import com.imin.printerlib.util.Utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrintUtils {
    public static final int MAX_RANGE = 255;
    public static final int MIN_RANGE = 0;
    public static final int MAX_TAB_RANGE = 32;
    public static final int LIMIT_VALUE_1 = 1;
    public static final int LIMIT_VALUE_2 = 2;
    public static final int LIMIT_VALUE_3 = 3;
    public static final int LIMIT_VALUE_4 = 4;
    public static final int LIMIT_VALUE_6 = 6;
    public static final int LIMIT_VALUE_7 = 7;
    public static final int LIMIT_VALUE_8 = 8;
    public static final int LIMIT_VALUE_9 = 9;
    public static final int LIMIT_VALUE_11 = 11;
    public static final int LIMIT_VALUE_12 = 12;
    public static final int LIMIT_VALUE_13 = 13;
    public static final int LIMIT_VALUE_14 = 14;
    public static final int LIMIT_VALUE_15 = 15;
    public static final int LIMIT_VALUE_16 = 16;
    public static final int LIMIT_VALUE_17 = 17;
    public static final int LIMIT_VALUE_32 = 32;
    public static final int LIMIT_VALUE_48 = 48;
    public static final int LIMIT_VALUE_49 = 49;
    public static final int LIMIT_VALUE_50 = 50;
    public static final int LIMIT_VALUE_51 = 51;
    public static final int LIMIT_VALUE_57 = 57;
    public static final int LIMIT_VALUE_65 = 65;
    public static final int LIMIT_VALUE_68 = 68;
    public static final int LIMIT_VALUE_80 = 80;
    public static final int LIMIT_VALUE_90 = 90;
    public static final int LIMIT_VALUE_97 = 97;
    public static final int LIMIT_VALUE_100 = 100;
    public static final int LIMIT_VALUE_122 = 122;
    public static final int LIMIT_VALUE_126 = 126;
    public static final int UPC_A = 0;
    public static final int UPC_E = 1;
    public static final int JAN13_OR_EAN13 = 2;
    public static final int JAN8_OR_EAN8 = 3;
    public static final int CODE39 = 4;
    public static final int ITF = 5;
    public static final int CODABAR = 6;
    public static final int CODE_128 = 73;
    public static final int QR_CODE_CONVERSION_1 = 49;
    public static final int QR_CODE_CONVERSION_2 = 50;
    private static List<String> CODE39_RangeList = new ArrayList();
    private static int printStatuss = -1;

    public PrintUtils() {
    }

    public static void transmitText(String text, UsbPrinter printer) {
        try {
            byte[] textBytes = text.getBytes("GBK");
            printer.writePort(textBytes, textBytes.length);
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }

    }

    public static void printerText(String text, UsbPrinter printer) {
        transmitText(text, printer);
        printAndLineFeed(printer);
    }

    public static void printAndLineFeed(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{10});
    }

    public static void standardPrint(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{12});
    }

    public static void lineFeed(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{13});
    }

    public static void printSelfeTest(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{29, 40, 65, 2, 0, 0, 2});
    }

    public static void partialCut(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{29, 86, 0});
    }

    public static void fullCut(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{29, 86, 1});
    }

    public static void switchUnderLine(UsbPrinter printer, int underLineCmd) {
        Utils.sendCommonCmd(printer, new int[]{27, 45, underLineCmd, 28, 45, underLineCmd});
    }

    public static void intoChinesePrintModel(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{28, 38});
    }

    public static void exitChinesePrintModel(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{28, 46});
    }

    public static void printerPowerOff(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{16, 20, 2, 1, 8});
    }

    public static void initializePrinter(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{27, 64});
    }

    public static void setRightMargin(UsbPrinter printer, int charRightMargin) {
        if (charRightMargin >= 0 && charRightMargin <= 255) {
            Utils.sendCommonCmd(printer, new int[]{27, 32, charRightMargin});
        } else {
            throw new IllegalArgumentException("rightMargin values are not supported, 0 ≤ rightMargin ≤ 255");
        }
    }

    public static void setFontStyle(UsbPrinter printer, PrinterFontEnum... fontEnum) {
        if (fontEnum == null) {
            throw new NullPointerException("fontEnum cannot be null");
        } else {
            byte cmd = (byte)fontEnum[0].ordinal();
            if (fontEnum.length > 1) {
                for(int i = 1; i < fontEnum.length; ++i) {
                    cmd = (byte)(cmd | fontEnum[i].getValue());
                }
            }

            Utils.sendCommonCmd(printer, new int[]{27, 33, cmd});
        }
    }

    public static void setDefaultLineSpace(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{27, 50});
    }

    public static void setLineHeightSpace(UsbPrinter printer, int lineHeight) {
        if (lineHeight >= 0 && lineHeight <= 255) {
            Utils.sendCommonCmd(printer, new int[]{27, 51, lineHeight});
        } else {
            throw new IllegalArgumentException("lineHeight values are not supported, 0 ≤ lineHeight ≤ 255");
        }
    }

    public static void setHorizontalTabPos(UsbPrinter printer, int... tabPosition) {
        if (tabPosition == null) {
            throw new NullPointerException("tabPosition cannot be null");
        } else if (tabPosition.length > 32) {
            throw new UnsupportedOperationException("tab over 32");
        } else {
            byte[] bytes = null;

            for(int i = 0; i < tabPosition.length; ++i) {
                if (tabPosition[i] < 1 || tabPosition[i] > 255) {
                    throw new IllegalArgumentException("Unsupported parameter settings");
                }

                if (i < tabPosition.length - 1 && tabPosition[i] > tabPosition[i + 1]) {
                    throw new IllegalArgumentException("Unsupported parameter settings");
                }

                if (bytes == null) {
                    bytes = new byte[tabPosition.length + 3];
                }

                bytes[i + 2] = (byte)tabPosition[i];
            }

            if (bytes != null) {
                bytes[0] = 27;
                bytes[1] = 68;
                bytes[bytes.length - 1] = 0;
            }

            printer.writePort(bytes, bytes.length);
        }
    }

    public static void emphasizedMode(UsbPrinter printer, int value) {
        if (value >= 0 && value <= 255) {
            Utils.sendCommonCmd(printer, new int[]{27, 69, value});
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static void doubleStrikeMode(UsbPrinter printer, int value) {
        checkLegality(value);
        Utils.sendCommonCmd(printer, new int[]{27, 69, value});
    }

    public static void printAndFeedPaper(UsbPrinter printer, int value) {
        checkLegality(value);
        Utils.sendCommonCmd(printer, new int[]{27, 74, value});
    }

    public static void selectCharacterFont(UsbPrinter printer, int fontStyle) {
        if (fontStyle != 0 && fontStyle != 1 && fontStyle != 48 && fontStyle == 49) {
            throw new IllegalArgumentException("Parameter violation " + fontStyle);
        } else {
            Utils.sendCommonCmd(printer, new int[]{27, 77, fontStyle});
        }
    }

    public static void selectPrintDirection(UsbPrinter printer, DirectionEnum dirEnum) {
        int direction = dirEnum.getValue();
        boolean isLowRange = direction >= 0 && direction <= 3;
        boolean isHeightRange = direction >= 48 && direction <= 51;
        if (!isLowRange && !isHeightRange) {
            throw new IllegalArgumentException("Parameter invalid " + direction);
        } else {
            Utils.sendCommonCmd(printer, new int[]{27, 84, direction});
        }
    }

    public static void switchRotation(UsbPrinter printer, int ratationMode) {
        if (!isRangeZeroToTwo(ratationMode)) {
            throw new IllegalArgumentException("Parameter invalid " + ratationMode);
        } else {
            Utils.sendCommonCmd(printer, new int[]{27, 86, ratationMode});
        }
    }

    public static void setAlignment(UsbPrinter printer, int alignmentMode) {
        if (!isRangeZeroToTwo(alignmentMode)) {
            throw new IllegalArgumentException("Parameter invalid " + alignmentMode);
        } else {
            Utils.sendCommonCmd(printer, new int[]{27, 97, alignmentMode});
        }
    }

    public static void isDisablePanel(UsbPrinter printer, boolean isDisablePanel) {
        Utils.sendCommonCmd(printer, new int[]{27, 99, 53, isDisablePanel ? 1 : 0});
    }

    public static void switchUpsideDownPrint(UsbPrinter printer, boolean isUpsideDown) {
        Utils.sendCommonCmd(printer, new int[]{27, 123, isUpsideDown ? 1 : 0});
    }

    public static void setCharacterSize(UsbPrinter printer, int heightScale, int widthScale) {
        if (heightScale >= 0 && heightScale <= 7) {
            if (widthScale >= 0 && widthScale <= 7) {
                Utils.sendCommonCmd(printer, new int[]{29, 33, heightScale | widthScale << 4});
            } else {
                throw new IllegalArgumentException(" invalid widthScale parameter " + heightScale);
            }
        } else {
            throw new IllegalArgumentException(" invalid heightScale parameter " + heightScale);
        }
    }

    public static void setPrintSpeed(UsbPrinter printer, int speedEnum) {
        Utils.sendCommonCmd(printer, new int[]{29, 40, 75, 2, 0, 50, speedEnum});
    }

    public static void twoColorPrintMode(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{29, 40, 69, 0, 1, 5, 116});
    }

    public static void setCharacterColor(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{29, 40, 78, 2, 0, 48, 50});
    }

    public static void storeQrCodeDataInStorage(UsbPrinter printer, String qrStr) {
        try {
            if (TextUtils.isEmpty(qrStr)) {
                throw new NullPointerException("Qr code data cannot be empty");
            }

            byte[] qrBytes = qrStr.getBytes("GBK");
            if (qrBytes.length > 255) {
                throw new IllegalArgumentException("The length of the qr code data exceeds 255");
            }

            int mBit = qrBytes.length + 3;
            int heightBit = mBit >> 8;
            int lowBit = mBit & 255;
            byte[] mCommand = new byte[mBit + 5];
            mCommand[0] = 29;
            mCommand[1] = 40;
            mCommand[2] = 107;
            mCommand[3] = (byte)lowBit;
            mCommand[4] = (byte)heightBit;
            mCommand[5] = 49;
            mCommand[6] = 80;
            mCommand[7] = 48;
            System.arraycopy(qrBytes, 0, mCommand, 8, qrBytes.length);
            printer.writePort(mCommand, mCommand.length);
        } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
        }

    }

    public static void setQrCodeSize(UsbPrinter printer, int level) {
        if (level >= 1 && level <= 16) {
            Utils.sendCommonCmd(printer, new int[]{29, 40, 107, 3, 0, 49, 67, level});
        } else {
            throw new IllegalArgumentException("Parameter violation : " + level);
        }
    }

    public static void setQrCodeConversionMod(UsbPrinter printer, int model) {
        if (model != 49 && model != 50) {
            throw new IllegalArgumentException("Parameter violation : " + model);
        } else {
            Utils.sendCommonCmd(printer, new int[]{29, 40, 107, 4, 0, 49, 65, model, 0});
        }
    }

    public static void defineNVBmp(UsbPrinter printer, Bitmap bitmap, byte kc1, byte kc2) {
        byte[] bytes = getBytesByBitmap(bitmap);
        byte[] printBytes = new byte[bytes.length + 16];
        int width = bitmap.getWidth();
        if ((bitmap.getWidth() & 7) > 0) {
            ++width;
        }

        int pB = bytes.length + 11;
        byte pL = (byte)(pB & 255);
        byte pH = (byte)(pB >> 8);
        int _index = 0;
        int height = bitmap.getHeight();
        int index = _index + 1;
        printBytes[index] = 29;
        printBytes[index++] = 40;
        printBytes[index++] = 76;
        printBytes[index++] = pL;
        printBytes[index++] = pH;
        printBytes[index++] = 48;
        printBytes[index++] = 67;
        printBytes[index++] = 48;
        printBytes[index++] = kc1;
        printBytes[index++] = kc2;
        printBytes[index++] = 1;
        printBytes[index++] = (byte)(width & 255);
        printBytes[index++] = (byte)(width >> 8);
        printBytes[index++] = (byte)(height & 255);
        printBytes[index++] = (byte)(height >> 8);
        printBytes[index++] = 49;
        System.arraycopy(bytes, 0, printBytes, index, bytes.length);
        printer.writePort(printBytes, printBytes.length);
    }

    public static void deleteNVBmpByKeyCode(UsbPrinter printer, byte kc1, byte kc2) {
        checkValueRange(kc1, 32, 126);
        checkValueRange(kc2, 32, 126);
        Utils.sendCommonCmd(printer, new int[]{29, 40, 76, 4, 0, 48, 66, kc1, kc2});
    }

    public static void deleteAllNVBmp(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{29, 40, 76, 5, 0, 48, 65, 67, 76, 82});
    }

    public static void printBitImage(UsbPrinter printer, Bitmap bitmap) {
        byte[] bytes = getBytesByBitmap(bitmap);
        byte[] printBytes = new byte[bytes.length + 8];
        int bitWidth = bitmap.getWidth() >> 3;
        if ((bitmap.getWidth() & 7) > 0) {
            ++bitWidth;
        }

        int height = bitmap.getHeight();
        printBytes[0] = 29;
        printBytes[1] = 118;
        printBytes[2] = 48;
        printBytes[3] = 0;
        printBytes[4] = (byte)(bitWidth & 255);
        printBytes[5] = (byte)(bitWidth >> 8);
        printBytes[6] = (byte)(height & 255);
        printBytes[7] = (byte)(height >> 8);
        System.arraycopy(bytes, 0, printBytes, 8, bytes.length);
        printer.writePort(printBytes, printBytes.length);
    }

    public static byte[] setNVBmp(int var0, String var1) {
        if (var0 > 0 && var1.length() > 0) {
            try {
                try {
                    byte[] var14 = new byte[525312];
                    byte var3 = 0;
                    boolean var4 = false;
                    int var5 = 0;
                    String var6 = null;
                    String var7 = var1;
                    if (var1.substring(var1.length() - 1).equals(";")) {
                        var7 = var1 + ";";
                    }

                    int var15 = var3 + 1;
                    var14[0] = 28;
                    ++var15;
                    var14[1] = 113;
                    ++var15;
                    var14[2] = (byte)var0;

                    int var16;
                    byte[] var17;
                    for(int var13 = 0; var13 < var0; ++var13) {
                        if ((var16 = var7.indexOf(";", 0)) > 0) {
                            var6 = var7.substring(0, var16);
                            var7 = var7.substring(var16 + 1);
                            if ((var17 = e(var6)) == null) {
                                return null;
                            }

                            var16 = var17.length;

                            int var8;
                            for(var8 = 4; var8 > 0; --var8) {
                                var14[var15++] = var17[var16 - var8];
                            }

                            for(var8 = 0; var8 < var16 - 4; ++var8) {
                                var14[var15++] = var17[var8];
                            }

                            ++var5;
                        }
                    }

                    if (var15 > 524288) {
                        return null;
                    }

                    if (var5 == var0) {
                        var16 = var15;
                        var17 = new byte[var15];

                        for(var15 = 0; var15 < var16; ++var15) {
                            var17[var15] = var14[var15];
                        }

                        return var17;
                    }
                } catch (Exception var13) {
                    Object var2 = null;
                    var13.printStackTrace();
                }

                return null;
            } catch (Throwable var14) {
                throw var14;
            }
        } else {
            return null;
        }
    }

    private static byte[] e(String var0) {
        FileInputStream var1 = null;
        boolean var24 = false;

        label474: {
            label475: {
                label476: {
                    label477: {
                        byte[] var33;
                        try {
                            var24 = true;
                            var1 = new FileInputStream(var0);
                            var33 = new byte[14];
                            var1.read(var33, 0, 14);
                            var33 = new byte[]{var33[0], var33[1]};
                            if (!"BM".equals(new String(var33))) {
                                System.out.println("Image format is not correct...");
                                var24 = false;
                                break label475;
                            }

                            var33 = new byte[40];
                            var1.read(var33, 0, 40);
                            int var2 = (var33[7] & 255) << 24 | (var33[6] & 255) << 16 | (var33[5] & 255) << 8 | var33[4] & 255;
                            int var3 = (var33[11] & 255) << 24 | (var33[10] & 255) << 16 | (var33[9] & 255) << 8 | var33[8] & 255;
                            int var34 = (var33[15] & 255) << 8 | var33[14] & 255;
                            int var4;
                            byte[] var5 = new byte[var4 = (int)StrictMath.pow(2.0D, (double)var34 * 1.0D) * 4];
                            var1.read(var5, 0, var4);
                            boolean var35 = false;
                            if (var5[0] != 0) {
                                var35 = true;
                            }

                            byte var36 = 0;
                            if (var34 != 1) {
                                System.out.println("Resource is not monochromatic image...");
                                var24 = false;
                                break label476;
                            }

                            int var6 = (var2 * var34 + 31 & -32) >> 3;
                            var34 = var2 * var34 / 8 * 8 - 1;
                            int var7 = var6 << 3;
                            int var8;
                            if ((var8 = var6 * var3 + 8) > 65544) {
                                var24 = false;
                                break label477;
                            }

                            int var9 = var6;
                            boolean var10 = false;
                            String[] var11 = new String[var8];
                            byte[] var12 = new byte[var8 - 8];
                            var2 = 8 - var2 % 8;
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
                            var11[6] = Integer.toHexString(var3 % 256);
                            ++var38;
                            var11[7] = Integer.toHexString(var3 >> 8);
                            byte[] var13 = new byte[var8 - 8];
                            var1.read(var13, 0, var8 - 8);

                            int var42;
                            for(boolean var21 = false; var38 < var8; ++var38) {
                                byte var45 = var13[var38 - 8];
                                if (!var35) {
                                    if ((var42 = (var38 - 8) * 8 % var7) >= var34) {
                                        if (var42 - var34 < 8) {
                                            for(var42 = 0; var42 < var2; ++var42) {
                                                var45 = (byte)(var45 + (1 << var42) & 255);
                                            }
                                        } else {
                                            var45 = -1;
                                        }
                                    }

                                    var45 = (byte)(255 - var45);
                                }

                                var11[var38 + var9 - 1] = Integer.toHexString(var45 & 255);
                                var9 -= 2;
                                if (var9 == -var6) {
                                    var9 = var6;
                                }
                            }

                            for(var38 = 8; var38 < var8; ++var38) {
                                var42 = Integer.valueOf(var11[var8 - 1 + 8 - var38], 16);
                                var12[var38 - 8] = (byte)var42;
                            }

                            char[] var37 = new char[]{'\u0080', '@', ' ', '\u0010', '\b', '\u0004', '\u0002', '\u0001'};
                            var34 = var6 * 8 >> 3;
                            var2 = var3 >> 3;
                            char var40;
                            if ((var40 = (char)(var3 % 8)) != 0) {
                                var9 = var2 + 1;
                            } else {
                                var9 = var2;
                            }

                            byte[] var15 = new byte[var3 = var34 * var9 * 8 + 4];

                            int var46;
                            for(var46 = 0; var46 < var3; ++var46) {
                                var15[var46] = -1;
                            }

                            var46 = 0;

                            while(true) {
                                if (var46 >= var34) {
                                    var38 = var15.length - 4;
                                    var15[var38++] = (byte)var6;
                                    var15[var38++] = (byte)(var6 >> 8);
                                    var15[var38++] = (byte)var9;
                                    var15[var38] = (byte)(var9 >> 8);
                                    var33 = var15;
                                    var24 = false;
                                    break;
                                }

                                char var39;
                                char var41;
                                int var43;
                                int var44;
                                for(var42 = 0; var42 < var2; ++var42) {
                                    for(var44 = 0; var44 < 8; ++var44) {
                                        var39 = var37[var44];
                                        var41 = 0;

                                        for(var43 = 0; var43 < 8; ++var43) {
                                            if (((char)var12[(var42 * 8 + var43) * var34 + var46] & var39) != 0) {
                                                var41 += var37[var43];
                                            }
                                        }

                                        var15[(var46 * 8 + var44) * var9 + var42] = (byte)var41;
                                    }
                                }

                                if (var40 != 0) {
                                    for(var44 = 0; var44 < 8; ++var44) {
                                        var39 = var37[var44];
                                        var41 = 0;

                                        for(var43 = 0; var43 < var40; ++var43) {
                                            if (((char)var12[(var42 * 8 + var43) * var34 + var46] & var39) != 0) {
                                                var41 += var37[var43];
                                            }
                                        }

                                        var15[(var46 * 8 + var44) * var9 + var42] = (byte)var41;
                                    }
                                }

                                ++var46;
                            }
                        } catch (Exception var50) {
                            var50.printStackTrace();
                            var24 = false;
                            break label474;
                        } finally {
                            if (var24 && var1 != null) {
                                try {
                                    var1.close();
                                } catch (IOException var45) {
                                    var45.printStackTrace();
                                }
                            }

                        }

                        try {
                            var1.close();
                        } catch (IOException var49) {
                            var49.printStackTrace();
                        }

                        return var33;
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
            } catch (IOException var46) {
                var46.printStackTrace();
            }

            return null;
        }

        if (var1 != null) {
            try {
                var1.close();
            } catch (IOException var44) {
                var44.printStackTrace();
            }
        }

        return null;
    }

    public static byte[] getBytesByBitmap(Bitmap bitmap) {
        int[] pixelsBitmap = Utils.getPixelsByBitmap(bitmap);
        byte[] bytes = Utils.getBMPImageFileByte(pixelsBitmap, bitmap.getWidth(), bitmap.getHeight());
        return bytes;
    }

    public static void printNVBmp(UsbPrinter printer, int kc1, int kc2) {
        Utils.sendCommonCmd(printer, new int[]{29, 40, 76, 6, 0, 48, 69, kc1, kc2, 1, 1});
    }

    public static void setQrCodeErrorCorrectionLev(UsbPrinter printer, int level) {
        if (level >= 48 && level <= 51) {
            Utils.sendCommonCmd(printer, new int[]{29, 40, 107, 3, 0, 49, 69, level});
        } else {
            throw new IllegalArgumentException("Parameter violation : " + level);
        }
    }

    public static void printQrCode(UsbPrinter printer) {
        Utils.sendCommonCmd(printer, new int[]{29, 40, 107, 3, 0, 49, 81, 48});
    }

    public static void setColorReversePrintMode(UsbPrinter printer, boolean isOpen) {
        int cmd = 0;
        if (isOpen) {
            cmd = 1;
        }

        Utils.sendCommonCmd(printer, new int[]{29, 66, cmd});
    }

    public static void setLeftMargin(UsbPrinter printer, int marginValue) {
        int mH = marginValue >> 8;
        int mL = marginValue & 255;
        checkLegality(mH);
        checkLegality(mL);
        Utils.sendCommonCmd(printer, new int[]{29, 76, mL, mH});
    }

    public static void setMotionUnits(UsbPrinter printer, int x, int y) {
        checkLegality(x);
        checkLegality(y);
        Utils.sendCommonCmd(printer, new int[]{29, 80, x, y});
    }

    public static void setPrintAreaWidth(UsbPrinter printer, int width) {
        if (width >= 0 && width <= 80) {
            int mWidth = (int)((double)width * 6.4D);
            int wH = mWidth >> 8;
            int wL = width & 255;
            Utils.sendCommonCmd(printer, new int[]{29, 87, wL, wH});
        } else {
            throw new IllegalArgumentException("Parameter invalid " + width);
        }
    }

    public static void switchSmoothingMode(UsbPrinter printer, int smoDistance) {
        checkLegality(smoDistance);
        Utils.sendCommonCmd(printer, new int[]{29, 98, smoDistance});
    }

    public static void setBarCodeHeight(UsbPrinter printer, int height) {
        if (height >= 1 && height <= 255) {
            Utils.sendCommonCmd(printer, new int[]{29, 104, height});
        } else {
            throw new IllegalArgumentException("Parameter invalid " + height);
        }
    }

    public static void setBarCodeHeight(BluetoothPrintUtil bluetoothPrintUtil, int height) {
        if (height >= 1 && height <= 255) {
            byte[] bytes = intToByte(29, 104, height);
            bluetoothPrintUtil.printRawBytes(bytes);
        } else {
            throw new IllegalArgumentException("Parameter invalid " + height);
        }
    }

    public static void setBarCodeHeight(int height) {
        if (height >= 1 && height <= 255) {
            byte[] bytes = intToByte(29, 104, height);
            printSPISelfByte(bytes);
        } else {
            throw new IllegalArgumentException("Parameter invalid " + height);
        }
    }

    public static void setBarCodeHeight(SerialControl mComPort, int height) {
        if (height >= 1 && height <= 255) {
            byte[] bytes = intToByte(29, 104, height);
            mComPort.sendByte(bytes);
        } else {
            throw new IllegalArgumentException("Parameter invalid " + height);
        }
    }

    private static byte[] getCode128(int barCodeType, String barCodeContent) throws UnsupportedEncodingException {
        String barContent = barCodeContent.substring(2, barCodeContent.length());
        Log.d("xgh", "usbPrinter  print128   barContent==> " + barContent);
        Log.d("xgh", "usbPrinter  print128   barCodeContent==> " + barCodeContent);
        byte[] barCodeBytes = barContent.getBytes("GBK");
        int k = barCodeBytes.length;
//        int maxAllowLen = true;
        boolean rangeOne;
        byte[] command;
        int var9;
        int var10;
        byte barCodeByte;
        if (barCodeContent.startsWith("{A")) {
            command = barCodeBytes;
            var9 = barCodeBytes.length;

            for(var10 = 0; var10 < var9; ++var10) {
                barCodeByte = command[var10];
                Log.d("xgh", "222222    barCodeByte ==> " + barCodeByte);
                rangeOne = barCodeByte >= 97 && barCodeByte <= 122;
                if (rangeOne) {
                    throw new UnsupportedEncodingException("Unsupported encoding range");
                }
            }
        } else if (barCodeContent.startsWith("{C")) {
            command = barCodeBytes;
            var9 = barCodeBytes.length;

            for(var10 = 0; var10 < var9; ++var10) {
                barCodeByte = command[var10];
                Log.d("xgh", "222222    barCodeByte ==> " + barCodeByte);
                rangeOne = barCodeByte >= 48 && barCodeByte <= 57;
                if (!rangeOne) {
                    throw new UnsupportedEncodingException("Unsupported encoding range");
                }
            }
        }

        command = new byte[k + 6];
        command[0] = 29;
        command[1] = 107;
        command[2] = (byte)barCodeType;
        command[3] = (byte)((byte)k + 2);
        command[4] = 123;
        if (barCodeContent.startsWith("{A")) {
            command[5] = 65;
        } else if (barCodeContent.startsWith("{C")) {
            command[5] = 67;
        } else {
            command[5] = 66;
        }

        System.arraycopy(barCodeBytes, 0, command, 6, barCodeBytes.length);
        return command;
    }

    private static byte[] getPrintCode(int barCodeType, String barCodeContent) throws UnsupportedEncodingException {
        byte[] barCodeBytes = barCodeContent.getBytes("GBK");
        boolean isInRangeK = false;
        boolean isInRangeD = false;
        int k = barCodeBytes.length;
        int maxAllowLen = 0;
        byte[] command;
        if (barCodeType == 73) {
            if (barCodeContent.length() == 0) {
                throw new UnsupportedEncodingException("Unsupported encoding range");
            } else if (barCodeContent.equals("null")) {
                throw new UnsupportedEncodingException("Unsupported encoding range");
            } else if (barCodeContent.isEmpty()) {
                throw new UnsupportedEncodingException("Unsupported encoding range");
            } else if (barCodeContent.length() < 4) {
                throw new UnsupportedEncodingException("Unsupported encoding range");
            } else {
                command = getCode128(barCodeType, barCodeContent);
                Log.d("xgh", "getPrintCode print128   ==> " + Arrays.toString(command));
                return command;
            }
        } else {
            boolean rangeOne;
            boolean rangeTwo;
            boolean rangeThree;
            int var12;
            label118:
            switch(barCodeType) {
                case 0:
                case 1:
                    isInRangeK = isInRangeK(k, 11, 12);
                    maxAllowLen = 12;
                    isInRangeD(barCodeBytes, 48, 57);
                    break;
                case 2:
                    isInRangeK = isInRangeK(k, 12, 13);
                    maxAllowLen = 13;
                    isInRangeD(barCodeBytes, 48, 57);
                    break;
                case 3:
                    isInRangeK = isInRangeK(k, 7, 8);
                    maxAllowLen = 8;
                    isInRangeD(barCodeBytes, 48, 57);
                    break;
                case 4:
                    isInRangeK = isInRangeK(k, 1, 0);
                    CODE39_RangeList.clear();
                    CODE39_RangeList.add("32");
                    CODE39_RangeList.add("36");
                    CODE39_RangeList.add("37");
                    CODE39_RangeList.add("42");
                    CODE39_RangeList.add("43");
                    CODE39_RangeList.add("45");
                    CODE39_RangeList.add("46");
                    CODE39_RangeList.add("47");
                    command = barCodeBytes;
                    int var16 = barCodeBytes.length;
                    var12 = 0;

                    while(true) {
                        if (var12 >= var16) {
                            break label118;
                        }

                        byte barCodeByte = command[var12];
                        Log.d("xgh", "222222  UsbPrinter  barCodeByte ==> " + barCodeByte);
                        rangeOne = barCodeByte >= 48 && barCodeByte <= 57;
                        rangeTwo = barCodeByte >= 65 && barCodeByte <= 90;
                        if (!rangeOne && !rangeTwo) {
                            rangeThree = CODE39_RangeList.contains(String.valueOf(barCodeByte));
                            if (!rangeThree) {
                                throw new UnsupportedEncodingException("Unsupported encoding range");
                            }
                        }

                        ++var12;
                    }
                case 5:
                    isInRangeK = isInRangeK(k, 2, 0);
                    isInRangeD(barCodeBytes, 48, 57);
                    break;
                case 6:
                    isInRangeK = isInRangeK(k, 2, 0);
                    CODE39_RangeList.clear();
                    CODE39_RangeList.add("36");
                    CODE39_RangeList.add("43");
                    CODE39_RangeList.add("45");
                    CODE39_RangeList.add("46");
                    CODE39_RangeList.add("47");
                    CODE39_RangeList.add("58");
                    byte[] var11 = barCodeBytes;
                    var12 = barCodeBytes.length;

                    for(int var13 = 0; var13 < var12; ++var13) {
                        byte barCodeByte = var11[var13];
                        rangeOne = barCodeByte >= 48 && barCodeByte <= 57;
                        rangeTwo = barCodeByte >= 65 && barCodeByte <= 68;
                        rangeThree = barCodeByte >= 97 && barCodeByte <= 100;
                        if (!rangeOne && !rangeTwo && !rangeThree) {
                            boolean rangeFour = CODE39_RangeList.contains(String.valueOf(barCodeByte));
                            if (!rangeFour) {
                                throw new UnsupportedEncodingException("Unsupported encoding range");
                            }
                        }
                    }
            }

            if (!isInRangeK) {
                throw new UnsupportedOperationException("barCode length is greather than " + maxAllowLen + "current length = " + k);
            } else {
                command = new byte[k + 4];
                command[0] = 29;
                command[1] = 107;
                command[2] = (byte)barCodeType;
                System.arraycopy(barCodeBytes, 0, command, 3, barCodeBytes.length);
                command[command.length - 1] = 0;
                Log.d("xgh", "usbPrinter ==> " + Arrays.toString(command));
                return command;
            }
        }
    }

    public static void printBarCode(UsbPrinter printer, int barCodeType, String barCodeContent) throws UnsupportedEncodingException {
        byte[] command = getPrintCode(barCodeType, barCodeContent);
        printer.writePort(command, command.length);
    }

    public static void printBarCode(BluetoothPrintUtil bluetoothPrintUtil, int barCodeType, String barCodeContent) throws UnsupportedEncodingException {
        byte[] command = getPrintCode(barCodeType, barCodeContent);
        bluetoothPrintUtil.printRawBytes(command);
    }

    public static void printBarCode(int barCodeType, String barCodeContent) throws UnsupportedEncodingException {
        byte[] command = getPrintCode(barCodeType, barCodeContent);
        printSPISelfByte(command);
    }

    public static void printBarCode(SerialControl mComPort, int barCodeType, String barCodeContent) throws UnsupportedEncodingException {
        byte[] command = getPrintCode(barCodeType, barCodeContent);
        mComPort.sendByte(command);
    }

    public static void setBarCodeWidth(UsbPrinter printer, int width) {
        if (width >= 2 && width <= 6) {
            Utils.sendCommonCmd(printer, new int[]{29, 119, width});
        } else {
            throw new IllegalArgumentException("Parameter invalid " + width);
        }
    }

    public static void setBarCodeWidth(BluetoothPrintUtil bluetoothPrintUtil, int width) {
        if (width >= 2 && width <= 6) {
            byte[] bytes = intToByte(29, 119, width);
            bluetoothPrintUtil.printRawBytes(bytes);
        } else {
            throw new IllegalArgumentException("Parameter invalid " + width);
        }
    }

    public static void setBarCodeWidth(int width) {
        if (width >= 2 && width <= 6) {
            byte[] bytes = intToByte(29, 119, width);
            printSPISelfByte(bytes);
        } else {
            throw new IllegalArgumentException("Parameter invalid " + width);
        }
    }

    public static void setBarCodeWidth(SerialControl mComPort, int width) {
        if (width >= 2 && width <= 6) {
            byte[] bytes = intToByte(29, 119, width);
            mComPort.sendByte(bytes);
        } else {
            throw new IllegalArgumentException("Parameter invalid " + width);
        }
    }

    public static void setBarCodeContentPrintPos(UsbPrinter printer, int position) {
        if (checkValueRange(position, 0, 3)) {
            Utils.sendCommonCmd(printer, new int[]{29, 72, position});
        }

    }

    public static void setBarCodeContentPrintPos(BluetoothPrintUtil bluetoothPrintUtil, int position) {
        if (checkValueRange(position, 0, 3)) {
            byte[] bytes = intToByte(29, 72, position);
            bluetoothPrintUtil.printRawBytes(bytes);
        }

    }

    public static void setBarCodeContentPrintPos(SerialControl mComPort, int position) {
        if (checkValueRange(position, 0, 3)) {
            byte[] bytes = intToByte(29, 72, position);
            mComPort.sendByte(bytes);
        }

    }

    public static void setBarCodeContentPrintPos(int position) {
        if (checkValueRange(position, 0, 3)) {
            byte[] bytes = intToByte(29, 72, position);
            printSPISelfByte(bytes);
        }

    }

    public static synchronized void printSPISelfByte(byte[] bOutArray) {
        File file = null;
        if (!TextUtils.equals("M2-203", Build.MODEL) && !TextUtils.equals("M2", Build.MODEL)) {
            if (TextUtils.equals("M2-202", Build.MODEL)) {
                file = new File("/dev/spidev32766.0");
            } else {
                if (!TextUtils.equals("M2-Pro", Build.MODEL)) {
                    return;
                }

                file = new File("/dev/spidev0.0");
            }
        } else {
            file = new File("/dev/spidev32765.0");
        }

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            fos.write(bOutArray);
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var11) {
                    var11.printStackTrace();
                }
            }

        }

    }

    public static void setInternationalCharset(UsbPrinter printer, int charsetType) {
        if (checkValueRange(charsetType, 0, 15)) {
            Utils.sendCommonCmd(printer, new int[]{27, 82, charsetType});
        }

    }

    public static int getRealTimeStatus(UsbPrinter printer, int statusType) {
        if (checkValueRange(statusType, 1, 4)) {
            byte[] readBuf = new byte[100];
            Utils.readCommonCmd(printer, readBuf, 2);
            Log.i("njm", "read   :  " + readBuf[0] + "    read1   : " + readBuf[1]);
            return readBuf[0];
        } else {
            return 0;
        }
    }

    public static int getNVMemoryCapacity(final UsbPrinter printer) {
        final byte[] bytes = new byte[2];
        Utils.sendCommonCmd(printer, new int[]{29, 40, 76, 2, 0, 48, 0});
        Runnable runnable = new Runnable() {
            public void run() {
                boolean var1 = false;

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException var3) {
                    var3.printStackTrace();
                }

                int value = Utils.readCommonCmd(printer, bytes, 1);
                Log.i("njm", "byte:   " + bytes[0] + "   value   : " + value);
            }
        };
        runnable.run();
        return bytes[0];
    }

    private static boolean isInRangeK(int k, int minRange, int maxRange) {
        if (maxRange == 0) {
            return k >= minRange;
        } else {
            return k <= minRange || k <= maxRange;
        }
    }

    private static boolean isInRangeD(byte[] bytes, int minRange, int maxRange) throws UnsupportedEncodingException {
        byte[] var3 = bytes;
        int var4 = bytes.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            if (b < minRange || b > maxRange) {
                throw new UnsupportedEncodingException("Unsupported encoding range");
            }
        }

        return true;
    }

    private static boolean checkValueRange(int value, int minRange, int maxRange) {
        if (value >= minRange && value <= maxRange) {
            return true;
        } else {
            throw new IllegalArgumentException("Parameter invalid " + value);
        }
    }

    public static boolean isRangeZeroToTwo(int rangeValue) {
        boolean isLowRange = rangeValue >= 0 && rangeValue <= 2;
        boolean isHeightRange = rangeValue >= 48 && rangeValue <= 50;
        return isLowRange || isHeightRange;
    }

    private static void checkLegality(int value) throws IllegalArgumentException {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException("Parameter invalid " + value);
        }
    }

    private static byte[] intToByte(int... cmd) {
        byte[] bytes = new byte[cmd.length];

        for(int i = 0; i < cmd.length; ++i) {
            bytes[i] = (byte)cmd[i];
        }

        return bytes;
    }

    public static int getPrintStatus() {
        return printStatuss;
    }

    public static void setPrintStatus(int printStatus) {
        printStatuss = printStatus;
    }
}

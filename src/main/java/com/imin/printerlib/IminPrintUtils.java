//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.imin.printerlib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.os.Build;
//import android.os.SystemProperties;
import android.provider.Settings.Global;
import android.serialport.HexUtil;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.Layout.Alignment;
import android.util.Log;
import androidx.annotation.RequiresApi;
import com.imin.printerlib.bean.TextSizeBean;
import com.imin.printerlib.port.UsbPrinter;
import com.imin.printerlib.print.PrintUtils;
import com.imin.printerlib.serial.SerialControl;
import com.imin.printerlib.serial.SerialHelper;
import com.imin.printerlib.serial.SerialPrintUtils;
import com.imin.printerlib.util.BluetoothUtil;
import com.imin.printerlib.util.Utils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class IminPrintUtils {
    private static final String TAG = "IminPrintUtils";
    private TextPaint textPaint;
    private int textSize = 28;
    private int defaultTextSize = 28;
    private boolean haveUnderline;
    private boolean haveBold;
    private float lineHeightRatio;
    private int fontSizeStyle = 0;
    private int underlineStyle;
    private int boldStyle;
    private int lineHeighSize;
    private float textLineSpacing = 1.0F;
    private static int textWidth = 385;
    private static int bitmapWidth = 576;
    private Typeface textTypeface;
    private int textStyle;
    private Alignment textAlignment;
    private List<Alignment> mAlignments;
    private UsbPrinter usbPrinter;
    private static IminPrintUtils iminPrintUtils;
    private StringBuilder stringBuilder;
    private List<TextSizeBean> textBean;
    private BluetoothPrintUtil bluetoothPrintUtil;
    public static int connectType;
    private BluetoothSocket mSocket;
    private Context mContext;
    private SerialControl mComPort;
    private boolean serialInit;
    private volatile boolean haveCache;
    private Disposable statusDisposable;
    private volatile int printerStatus;
    private IminPrintUtils.StatusThread statusThread;

    public static IminPrintUtils getInstance(Context context) {
        Class var1 = IminPrintUtils.class;
        synchronized(IminPrintUtils.class) {
            if (iminPrintUtils == null) {
                iminPrintUtils = new IminPrintUtils(context);
            }
        }

        return iminPrintUtils;
    }

    private IminPrintUtils(Context context) {
        this.textTypeface = Typeface.DEFAULT;
        this.textStyle = 0;
        this.textAlignment = Alignment.ALIGN_NORMAL;
        this.mAlignments = new ArrayList();
        this.textBean = new ArrayList();
        this.printerStatus = -1;
        if (this.usbPrinter == null) {
            this.usbPrinter = new UsbPrinter(context);
        }

        this.mContext = context;
        this.serialInit = false;
        this.initParams();
    }

    public void initPrinter(IminPrintUtils.PrintConnectType type, String overrideProductName) {
        Log.i("xgh", " SDK initPrinter:" + type + "; override productName: "
                + (overrideProductName == null ? "null" : overrideProductName));
        switch(type) {
            case USB:
                connectType = 0;
                if (this.stringBuilder != null) {
                    this.stringBuilder.delete(0, this.stringBuilder.length());
                }

                if (this.usbPrinter == null) {
                    this.usbPrinter = new UsbPrinter(this.mContext);
                }

                this.usbPrinter.initPrinter(overrideProductName);
                this.initializePrinter();
            case BLUETOOTH:
            default:
                break;
            case SPI:
                connectType = 3;
                this.serialInit = false;
                this.initPort();
                if (this.statusThread == null) {
                    this.statusThread = new IminPrintUtils.StatusThread();
                    this.statusThread.start();
                }
        }

        this.initModel();
    }

    public void initPrinter(IminPrintUtils.PrintConnectType type, BluetoothDevice device) throws IOException {
        if (type == IminPrintUtils.PrintConnectType.BLUETOOTH) {
            connectType = 2;
            if (device != null) {
                if (this.mSocket != null) {
                    this.mSocket.close();
                }

                this.mSocket = BluetoothUtil.connectDevice(device);
                if (this.mSocket != null) {
                    this.bluetoothPrintUtil = new BluetoothPrintUtil(this.mSocket.getOutputStream(), "GBK");
                    this.initializePrinter();
                }
            }
        }

        this.initModel();
    }

    private void initPort() {
        Log.d("XGH", "  serialInit:" + this.serialInit);
        if (!this.serialInit) {
            String sModel = Build.MODEL;
            this.serialInit = true;
            Log.i("IminPrintUtils", "  initPort");
            SerialPrintUtils.OpenPower();
            this.mComPort = SerialControl.getInstance();
            if (TextUtils.equals("M2-Pro", sModel)) {
                this.mComPort.setPort("/dev/ttyS0");
            } else {
                this.mComPort.setPort("/dev/ttyMT1");
            }

            this.mComPort.setBaudRate("115200");
            SerialPrintUtils.OpenComPort(this.mComPort, this.mContext);
            Log.d("XGH", "  initPort:" + this.mComPort);
        }
    }

    public void release() {
        SerialPrintUtils.ClosePower();
        SerialPrintUtils.CloseComPort(this.mComPort);
    }

    public void resetDevice() {
        this.release();
        this.usbPrinter = null;
        iminPrintUtils = null;
        if (this.statusThread != null) {
            this.statusThread = null;
        }

    }

    public UsbPrinter getUsbPrinter() {
        if (this.usbPrinter == null) {
            this.usbPrinter = new UsbPrinter(this.mContext);
        }

        return this.usbPrinter;
    }

    public void sendCommonCmd(int[] ints) {
        if (0 == connectType) {
            Utils.sendCommonCmd(this.usbPrinter, ints);
        } else {
            byte[] bytes;
            if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                bytes = this.intToByte(ints);
                this.mComPort.send(bytes);
            } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                bytes = this.intToByte(ints);
                this.bluetoothPrintUtil.printRawBytes(bytes);
            } else if (3 == connectType) {
                bytes = this.intToByte(ints);
                PrintUtils.printSPISelfByte(bytes);
            }
        }

    }

    private void initModel() {
        String model = System.getProperty("sys.neostra_oem_id");
        Log.i("IminPrintUtils", "model:" + model);
        if (!TextUtils.isEmpty(model) && model.length() > 4) {
            this.setPageFormat(0);
        } else {
            String modelM = System.getProperty("ro.neostra.imin_model", "");
            Log.i("IminPrintUtils", "modelM:" + modelM);
            if (!TextUtils.isEmpty(modelM) && modelM.length() > 5) {
                this.setPageFormat(1);
            }

            String platform = System.getProperty("ro.board.platform");
            if (!TextUtils.isEmpty(platform) && platform.startsWith("ums512")) {
                this.setPageFormat(1);
            }
        }

    }

    public void openPower() {
        SerialPrintUtils.OpenPower();
    }

    public void closePower() {
        SerialPrintUtils.ClosePower();
    }

    private void initializePrinter() {
        Log.i("IminPrintUtils", "initializePrinter");
        if (0 == connectType) {
            if (this.usbPrinter == null) {
                this.usbPrinter = new UsbPrinter(this.mContext);
            }

            Utils.sendCommonCmd(this.usbPrinter, new int[]{27, 64});
        } else {
            byte[] bytes;
            if (2 == connectType) {
                bytes = this.intToByte(27, 64);
                this.bluetoothPrintUtil.printRawBytes(bytes);
            } else if (3 == connectType) {
                bytes = this.intToByte(27, 64);
                PrintUtils.printSPISelfByte(bytes);
            }
        }

    }

    public void printerPowerOff() {
        if (0 == connectType) {
            Utils.sendCommonCmd(this.usbPrinter, new int[]{16, 20, 2, 1, 8});
        } else {
            byte[] bytes;
            if (2 == connectType) {
                bytes = this.intToByte(16, 20, 2, 1, 8);
                this.bluetoothPrintUtil.printRawBytes(bytes);
            } else if (3 == connectType) {
                bytes = this.intToByte(16, 20, 2, 1, 8);
                PrintUtils.printSPISelfByte(bytes);
            }
        }

    }

    public boolean isSPIPrint() {
        if (TextUtils.equals("M2-202", Build.MODEL)) {
            return Objects.equals(System.getProperty("persist.sys.isSPI"), "true");
        } else if (TextUtils.equals("M2-Pro", Build.MODEL)) {
            return true;
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("/proc/neostra_hw_info"));
                String line;
                if ((line = reader.readLine()) != null) {
                    Log.d("IminPrintUtils", "hasSPI" + line.contains("SPI=ON"));
                    return line.contains("SPI=ON");
                }

                reader.close();
            } catch (Exception var3) {
                var3.printStackTrace();
            }

            return false;
        }
    }

    public int getPrinterStatus(IminPrintUtils.PrintConnectType type) {
        switch(type) {
            case USB:
                if (this.usbPrinter == null) {
                    return -1;
                }

                int status = this.usbPrinter.getPrinterStatus();
                Log.i("XGH", "sdk getPrinterStatus:" + status);
                return status;
            case BLUETOOTH:
                return -1;
            case SPI:
                return this.printerStatus;
            default:
                return -1;
        }
    }

    public void getPrinterStatus(IminPrintUtils.PrintConnectType type, final Callback callback) {
        if (type == IminPrintUtils.PrintConnectType.SPI) {
            if (this.statusDisposable == null) {
                Log.d("xgh", "getPrinterStatus spi 3333-->" + this.printerStatus);
                this.printerStatus = -1;
                byte[] bytes = new byte[]{16, 4, 1};
                PrintUtils.printSPISelfByte(bytes);
            } else {
                this.statusDisposable.dispose();
                this.statusDisposable = null;
            }

            this.statusDisposable = Observable.timer(500L, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                public void accept(Long aLong) throws Exception {
                    if (callback != null) {
                        Log.d("xgh", "getPrinterStatus spi 22-->" + IminPrintUtils.this.printerStatus);
                        callback.callback(IminPrintUtils.this.printerStatus);
                    }

                    IminPrintUtils.this.statusDisposable = null;
                }
            });
        }
    }

    public void printAndLineFeed() {
        if (0 == connectType) {
            Utils.sendCommonCmd(this.getUsbPrinter(), new int[]{10});
        } else {
            byte[] bytes;
            if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                bytes = this.intToByte(10);
                this.mComPort.send(bytes);
            } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                bytes = this.intToByte(10);
                this.bluetoothPrintUtil.printRawBytes(bytes);
            } else if (3 == connectType) {
                bytes = this.intToByte(10);
                PrintUtils.printSPISelfByte(bytes);
            }
        }

    }

    public void printAndFeedPaper(int value) {
        checkLegality(value);
        if (0 == connectType) {
            Utils.sendCommonCmd(this.usbPrinter, new int[]{27, 74, value});
        } else {
            byte[] bytes;
            if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                bytes = this.intToByte(27, 74, value);
                this.mComPort.send(bytes);
            } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                bytes = this.intToByte(27, 74, value);
                this.bluetoothPrintUtil.printRawBytes(bytes);
            } else if (3 == connectType) {
                bytes = this.intToByte(27, 74, value);
                PrintUtils.printSPISelfByte(bytes);
            }
        }

    }

    private static void checkLegality(int value) throws IllegalArgumentException {
        if (value < 0 || value > 255) {
            throw new IllegalArgumentException("Parameter invalid " + value);
        }
    }

    public void fullCut() {
        if (0 == connectType) {
            Utils.sendCommonCmd(this.usbPrinter, new int[]{29, 86, 0});
        } else {
            byte[] bytes;
            if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                bytes = this.intToByte(29, 86, 0);
                this.mComPort.send(bytes);
            } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                bytes = this.intToByte(29, 86, 0);
                this.bluetoothPrintUtil.printRawBytes(bytes);
            } else if (3 == connectType) {
                bytes = this.intToByte(29, 86, 0);
                PrintUtils.printSPISelfByte(bytes);
            }
        }

    }

    public void partialCut() {
        if (0 == connectType) {
            Utils.sendCommonCmd(this.usbPrinter, new int[]{29, 86, 1});
        } else {
            byte[] bytes;
            if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                bytes = this.intToByte(29, 86, 1);
                this.mComPort.send(bytes);
            } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                bytes = this.intToByte(29, 86, 1);
                this.bluetoothPrintUtil.printRawBytes(bytes);
            } else if (3 == connectType) {
                bytes = this.intToByte(29, 86, 1);
                PrintUtils.printSPISelfByte(bytes);
            }
        }

    }

    public void sendRAWData(int[] ints) {
        byte[] bytes = this.intToByte(ints);
        if (0 == connectType) {
            this.usbPrinter.writePort(bytes, bytes.length);
        } else if (1 == connectType) {
            if (this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                this.mComPort.send(bytes);
            }
        } else if (2 == connectType) {
            if (this.bluetoothPrintUtil != null) {
                this.bluetoothPrintUtil.printRawBytes(bytes);
            }
        } else if (3 == connectType) {
            PrintUtils.printSPISelfByte(bytes);
        }

    }

    public void sendRAWData(String hex) {
        byte[] bytes = HexUtil.hexStringToBytes(hex);
        if (0 == connectType) {
            this.usbPrinter.writePort(bytes, bytes.length);
        } else if (1 == connectType) {
            if (this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                this.mComPort.send(bytes);
            }
        } else if (2 == connectType) {
            if (this.bluetoothPrintUtil != null) {
                this.bluetoothPrintUtil.printRawBytes(bytes);
            }
        } else if (3 == connectType) {
            PrintUtils.printSPISelfByte(bytes);
        }

    }

    public void sendRAWData(byte[] bytes) {
        if (0 == connectType) {
            this.usbPrinter.writePort(bytes, bytes.length);
        } else if (1 == connectType) {
            if (this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                this.mComPort.send(bytes);
            }
        } else if (2 == connectType) {
            if (this.bluetoothPrintUtil != null) {
                this.bluetoothPrintUtil.printRawBytes(bytes);
            }
        } else if (3 == connectType) {
            PrintUtils.printSPISelfByte(bytes);
        }

    }

    private void printBitmapText(String text) {
        Bitmap bitmap = this.getTextBitmap(text);
        this.printBitmaps(Collections.singletonList(bitmap));
    }

    public void printText(String text, int type) {
        if (0 == type) {
            this.printText(text);
        } else if (1 == type) {
            this.printBitmapText(text);
        }

    }

    public void printText(String text) {
        Log.e("xgh", "printText1111  " + text);
        if (text.contains("\n") && text.endsWith("\n")) {
            if (this.haveCache) {
                this.printCache();
            }

            text = text.substring(0, text.length() - 2);
            Log.e("xgh", "printText2222  " + text);
            this.printBitmapText(text);
        } else {
            if (this.stringBuilder == null) {
                this.stringBuilder = new StringBuilder();
            }

            this.haveCache = true;
            this.stringBuilder.append(text);
        }

    }

    private void printCache() {
        if (this.stringBuilder != null && this.stringBuilder.length() > 0 && this.haveCache) {
            this.printBitmapText(this.stringBuilder.toString());
            this.stringBuilder.delete(0, this.stringBuilder.length());
            this.haveCache = false;
        }

    }

    public void setCodeAlignment(int alignmentMode) {
        if (0 == connectType) {
            PrintUtils.setAlignment(this.usbPrinter, alignmentMode);
        } else {
            byte[] bytes;
            if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                bytes = this.intToByte(27, 97, alignmentMode);
                this.mComPort.sendByte(bytes);
            } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                bytes = this.intToByte(27, 97, alignmentMode);
                this.bluetoothPrintUtil.printRawBytes(bytes);
            } else if (3 == connectType) {
                bytes = this.intToByte(27, 97, alignmentMode);
                PrintUtils.printSPISelfByte(bytes);
            }
        }

    }

    public void setAlignment(int alignment) {
        switch(alignment) {
            case 0:
                this.textAlignment = Alignment.ALIGN_NORMAL;
                break;
            case 1:
                this.textAlignment = Alignment.ALIGN_CENTER;
                break;
            case 2:
                this.textAlignment = Alignment.ALIGN_OPPOSITE;
        }

    }

    public void setUnderline(boolean haveUnderline) {
        this.haveUnderline = haveUnderline;
        if (this.textPaint == null) {
            this.textPaint = new TextPaint();
        }

        if (this.underlineStyle == 1) {
            this.textPaint.setUnderlineText(true);
        } else {
            this.textPaint.setUnderlineText(haveUnderline);
        }

    }

    public void sethaveBold(boolean haveBold) {
        this.haveBold = haveBold;
        if (this.textPaint == null) {
            this.textPaint = new TextPaint();
        }

        if (this.boldStyle == 1) {
            this.textPaint.setFakeBoldText(true);
        } else {
            this.textPaint.setFakeBoldText(haveBold);
        }

    }

    public void setHaveLineHeight(float lineHeightRatio) {
        this.lineHeightRatio = lineHeightRatio;
        this.textLineSpacing = lineHeightRatio;
        Log.d("xgh", "textLineSpacing:" + this.textLineSpacing);
    }

    public void setTextSize(int size) {
        this.defaultTextSize = size;
        if (this.textPaint == null) {
            this.textPaint = new TextPaint();
        }

        if (this.fontSizeStyle == 0) {
            this.textSize = this.defaultTextSize;
        } else if (this.fontSizeStyle == 1) {
            this.textSize = this.defaultTextSize + 10;
        } else if (this.fontSizeStyle == 2) {
            this.textSize = this.defaultTextSize + 20;
        }

        this.textPaint.setTextSize((float)this.textSize);
    }

    public void setTextTypeface(Typeface typeface) {
        this.textTypeface = typeface;
        if (this.textPaint == null) {
            this.textPaint = new TextPaint();
        }

        this.textTypeface = Typeface.create(typeface, this.textStyle);
        this.textPaint.setTypeface(this.textTypeface);
    }

    public void setTextStyle(int style) {
        this.textStyle = style;
        if (this.textPaint == null) {
            this.textPaint = new TextPaint();
        }

        this.textTypeface = Typeface.create(this.textTypeface, this.textStyle);
        this.textPaint.setTypeface(this.textTypeface);
    }

    public void setTextLineSpacing(float space) {
        this.textLineSpacing = space;
    }

    public void setTextWidth(int width) {
        textWidth = width;
    }

    public void setBitmapWidth(int width) {
        bitmapWidth = width;
    }

    public void setPageFormat(int style) {
        if (0 == style) {
            textWidth = 576;
            bitmapWidth = 576;
        } else if (1 == style) {
            textWidth = 384;
            bitmapWidth = 384;
        }

    }

    @RequiresApi(
            api = 23
    )
    public void printColumnsText(String[] colTextArr, int[] colWidthArr, int[] colAlign, int[] size) {
        if (colTextArr.length == colWidthArr.length && colWidthArr.length == colAlign.length && colAlign.length == size.length) {
            Bitmap bitmap = this.getTableBitMap(colTextArr, colWidthArr, colAlign, textWidth, size, colTextArr.length);
            this.printCache();
            this.printBitmaps(Collections.singletonList(bitmap));
        } else {
            Log.i("IminPrintUtils", "incorrect parameter length");
        }
    }

    public void printNvBitmap(int position) {
        byte[] etBytes = PrintNvbmp(position, 48);
        if (0 == connectType) {
            this.usbPrinter.writePort(etBytes, etBytes.length);
        } else if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
            this.mComPort.sendByte(etBytes);
        } else if (2 == connectType && this.bluetoothPrintUtil != null) {
            this.bluetoothPrintUtil.printRawBytes(etBytes);
        } else if (3 == connectType) {
            PrintUtils.printSPISelfByte(etBytes);
        }

    }

    public boolean setDownloadNvBmp(String loadPath) {
        if (!"".equalsIgnoreCase(loadPath)) {
            int inums = this.Count(loadPath, ";");
            byte[] bValue = SetNvbmp(inums, loadPath);
            if (bValue != null) {
                if (0 == connectType) {
                    this.usbPrinter.writePort(bValue, bValue.length);
                } else if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                    this.mComPort.sendByte(bValue);
                } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                    this.bluetoothPrintUtil.printRawBytes(bValue);
                } else if (3 == connectType) {
                    PrintUtils.printSPISelfByte(bValue);
                }

                return true;
            }
        }

        return false;
    }

    public static byte[] PrintNvbmp(int var0, int var1) {
        byte[] var2 = new byte[4];
        int var3 = var1;
        if (var1 < 48) {
            var3 = 48;
        }

        if (var1 > 51) {
            var3 = 51;
        }

        var2[0] = 28;
        var2[1] = 112;
        var2[2] = (byte)var0;
        var2[3] = (byte)var3;
        return var2;
    }

    public static byte[] SetNvbmp(int var0, String var1) {
        byte[] var2;
        return (var2 = (new QRCodeInfo()).SetNvbmp(var0, var1)).length != 0 ? var2 : null;
    }

    public int Count(String strData, String str) {
        int iBmpNum = 0;

        for(int i = 0; i < strData.length(); ++i) {
            String getS = strData.substring(i, i + 1);
            if (getS.equals(str)) {
                ++iBmpNum;
            }
        }

        return iBmpNum;
    }

    public void printSingleBitmap(Bitmap bitmap, int alignmentMode) {
        this.printCache();
        this.printBitmaps(Collections.singletonList(bitmap), alignmentMode);
        this.setLeftMargin(0);
    }

    public void printMultiBitmap(List<Bitmap> bitmaps, int alignmentMode) {
        this.printCache();
        this.printBitmaps(bitmaps, alignmentMode);
        this.setLeftMargin(0);
    }

    public void printSingleBitmap(Bitmap bitmap) {
        this.printCache();
        this.printBitmaps(Collections.singletonList(bitmap));
    }

    public void printMultiBitmap(List<Bitmap> bitmaps) {
        this.printCache();
        this.printBitmaps(bitmaps);
    }

    public void printBarCode(int barCodeType, String barCodeContent) throws UnsupportedEncodingException {
        this.printCache();
        if (0 == connectType) {
            PrintUtils.printBarCode(this.usbPrinter, barCodeType, barCodeContent);
        } else if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
            PrintUtils.printBarCode(this.mComPort, barCodeType, barCodeContent);
        } else if (2 == connectType && this.bluetoothPrintUtil != null) {
            PrintUtils.printBarCode(this.bluetoothPrintUtil, barCodeType, barCodeContent);
        } else if (3 == connectType) {
            PrintUtils.printBarCode(barCodeType, barCodeContent);
        }

    }

    public void printBarCode(int barCodeType, String barCodeContent, int alignmentMode) throws UnsupportedEncodingException {
        this.printCache();
        this.setCodeAlignment(alignmentMode);
        Log.d("xgh", "usbPrinter spi print128  connectType ==> " + connectType);
        if (0 == connectType) {
            PrintUtils.printBarCode(this.usbPrinter, barCodeType, barCodeContent);
        } else if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
            PrintUtils.printBarCode(this.mComPort, barCodeType, barCodeContent);
        } else if (2 == connectType && this.bluetoothPrintUtil != null) {
            PrintUtils.printBarCode(this.bluetoothPrintUtil, barCodeType, barCodeContent);
        } else if (3 == connectType) {
            PrintUtils.printBarCode(barCodeType, barCodeContent);
        }

        this.setCodeAlignment(0);
    }

    public boolean getPrintPower() {
        return System.getProperty("persist.sys.imin.printPower", "0").equals("1");
    }

    public void setBarCodeWidth(int width) {
        if (0 == connectType) {
            PrintUtils.setBarCodeWidth(this.usbPrinter, width);
        } else if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
            PrintUtils.setBarCodeContentPrintPos(this.mComPort, width);
        } else if (2 == connectType && this.bluetoothPrintUtil != null) {
            PrintUtils.setBarCodeWidth(this.bluetoothPrintUtil, width);
        } else if (3 == connectType) {
            PrintUtils.setBarCodeWidth(width);
        }

    }

    public void setBarCodeHeight(int height) {
        if (0 == connectType) {
            PrintUtils.setBarCodeHeight(this.usbPrinter, height);
        } else if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
            PrintUtils.setBarCodeHeight(this.mComPort, height);
        } else if (2 == connectType && this.bluetoothPrintUtil != null) {
            PrintUtils.setBarCodeHeight(this.bluetoothPrintUtil, height);
        } else if (3 == connectType) {
            PrintUtils.setBarCodeHeight(height);
        }

    }

    public void setBarCodeContentPrintPos(int position) {
        if (0 == connectType) {
            PrintUtils.setBarCodeContentPrintPos(this.usbPrinter, position);
        } else if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
            PrintUtils.setBarCodeContentPrintPos(this.mComPort, position);
        } else if (2 == connectType && this.bluetoothPrintUtil != null) {
            PrintUtils.setBarCodeContentPrintPos(this.bluetoothPrintUtil, position);
        } else if (3 == connectType) {
            PrintUtils.setBarCodeContentPrintPos(position);
        }

    }

    public void printQrCode(String qrStr) {
        this.printCache();
        this.storeQrCodeDataInStorage(qrStr);
        if (0 == connectType) {
            Utils.sendCommonCmd(this.usbPrinter, new int[]{29, 40, 107, 3, 0, 49, 81, 48});
        } else {
            byte[] bytes;
            if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                bytes = this.intToByte(29, 40, 107, 3, 0, 49, 81, 48);
                this.mComPort.send(bytes);
            } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                bytes = this.intToByte(29, 40, 107, 3, 0, 49, 81, 48);
                this.bluetoothPrintUtil.printRawBytes(bytes);
            } else if (3 == connectType) {
                bytes = this.intToByte(29, 40, 107, 3, 0, 49, 81, 48);
                PrintUtils.printSPISelfByte(bytes);
            }
        }

    }

    public void printQrCode(String qrStr, int alignmentMode) {
        this.printCache();
        this.setCodeAlignment(alignmentMode);
        this.storeQrCodeDataInStorage(qrStr);
        if (0 == connectType) {
            Utils.sendCommonCmd(this.usbPrinter, new int[]{29, 40, 107, 3, 0, 49, 81, 48});
        } else {
            byte[] bytes;
            if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                bytes = this.intToByte(29, 40, 107, 3, 0, 49, 81, 48);
                Log.d("xgh", "usbPrinter ==> " + Arrays.toString(bytes));
                this.mComPort.send(bytes);
            } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                bytes = this.intToByte(29, 40, 107, 3, 0, 49, 81, 48);
                Log.d("xgh", "usbPrinter ==> " + bytes);
                this.bluetoothPrintUtil.printRawBytes(bytes);
            } else if (3 == connectType) {
                bytes = this.intToByte(29, 40, 107, 3, 0, 49, 81, 48);
                Log.d("xgh", "usbPrinter ==> " + Arrays.toString(bytes));
                PrintUtils.printSPISelfByte(bytes);
            }
        }

        this.setCodeAlignment(0);
    }

    private void storeQrCodeDataInStorage(String qrStr) {
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
            Log.d("xgh", "usbPrinter  QrCodeData ==> " + Arrays.toString(mCommand));
            if (0 == connectType) {
                this.usbPrinter.writePort(mCommand, mCommand.length);
            } else if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                this.mComPort.send(mCommand);
            } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                this.bluetoothPrintUtil.printRawBytes(mCommand);
            } else if (3 == connectType) {
                PrintUtils.printSPISelfByte(mCommand);
            }
        } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
        }

    }

    public void setQrCodeSize(int level) {
        if (level >= 1 && level <= 13) {
            if (0 == connectType) {
                Utils.sendCommonCmd(this.usbPrinter, new int[]{29, 40, 107, 3, 0, 49, 67, level});
            } else {
                byte[] bytes;
                if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                    bytes = this.intToByte(29, 40, 107, 3, 0, 49, 67, level);
                    this.mComPort.send(bytes);
                } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                    bytes = this.intToByte(29, 40, 107, 3, 0, 49, 67, level);
                    this.bluetoothPrintUtil.printRawBytes(bytes);
                } else if (3 == connectType) {
                    bytes = this.intToByte(29, 40, 107, 3, 0, 49, 67, level);
                    PrintUtils.printSPISelfByte(bytes);
                }
            }

        } else {
            throw new IllegalArgumentException("Parameter violation : " + level);
        }
    }

    public void setLeftMargin(int marginValue) {
        int mH = marginValue >> 8;
        int mL = marginValue & 255;
        checkLegality(mH);
        checkLegality(mL);
        if (0 == connectType) {
            Utils.sendCommonCmd(this.usbPrinter, new int[]{29, 76, mL, mH});
        } else {
            byte[] bytes;
            if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                bytes = this.intToByte(29, 76, mL, mH);
                this.mComPort.send(bytes);
            } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                bytes = this.intToByte(29, 76, mL, mH);
                this.bluetoothPrintUtil.printRawBytes(bytes);
            } else if (3 == connectType) {
                bytes = this.intToByte(29, 76, mL, mH);
                PrintUtils.printSPISelfByte(bytes);
            }
        }

    }

    public void setQrCodeErrorCorrectionLev(int level) {
        if (level >= 48 && level <= 51) {
            if (0 == connectType) {
                Utils.sendCommonCmd(this.usbPrinter, new int[]{29, 40, 107, 3, 0, 49, 69, level});
            } else {
                byte[] bytes;
                if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                    bytes = this.intToByte(29, 40, 107, 3, 0, 49, 69, level);
                    this.mComPort.send(bytes);
                } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                    bytes = this.intToByte(29, 40, 107, 3, 0, 49, 69, level);
                    this.bluetoothPrintUtil.printRawBytes(bytes);
                } else if (3 == connectType) {
                    bytes = this.intToByte(29, 40, 107, 3, 0, 49, 69, level);
                    PrintUtils.printSPISelfByte(bytes);
                }
            }

        } else {
            throw new IllegalArgumentException("Parameter violation : " + level);
        }
    }

    private Bitmap getTableBitMap(String[] colTextArr, int[] colWidthArr, int[] colAlign, int width, int[] size, int colNum) {
        this.mAlignments.clear();

        for(int i = 0; i < colAlign.length; ++i) {
            switch(colAlign[i]) {
                case 0:
                    this.mAlignments.add(Alignment.ALIGN_NORMAL);
                    break;
                case 1:
                    this.mAlignments.add(Alignment.ALIGN_CENTER);
                    break;
                case 2:
                    this.mAlignments.add(Alignment.ALIGN_OPPOSITE);
            }
        }

        if (this.textPaint == null) {
            this.textPaint = new TextPaint();
        }

        if (Global.getInt(this.mContext.getContentResolver(), "imin_printer_size_style", 0) != this.fontSizeStyle) {
            this.fontSizeStyle = Global.getInt(this.mContext.getContentResolver(), "imin_printer_size_style", 0);
            this.setTextSize(this.defaultTextSize);
        }

        if (Global.getInt(this.mContext.getContentResolver(), "imin_printer_bold_style", 0) != this.boldStyle) {
            this.boldStyle = Global.getInt(this.mContext.getContentResolver(), "imin_printer_bold_style", 0);
            this.sethaveBold(this.boldStyle == 1);
        }

        if (Global.getInt(this.mContext.getContentResolver(), "imin_printer_underline_style", 0) != this.underlineStyle) {
            this.underlineStyle = Global.getInt(this.mContext.getContentResolver(), "imin_printer_underline_style", 0);
            this.setUnderline(this.underlineStyle == 1);
        }

        if (Global.getInt(this.mContext.getContentResolver(), "imin_printer_lineHeight_style", 0) != this.lineHeighSize) {
            this.lineHeighSize = Global.getInt(this.mContext.getContentResolver(), "imin_printer_lineHeight_style", 0);
            this.setHaveLineHeight((float)(100 + this.lineHeighSize) / 100.0F);
        }

        int[] allTextHeight = new int[colNum];
        int totalWitchRadio = this.getTotal(colWidthArr);
        StaticLayout staticLayout = null;

        for(int i = 0; i < colWidthArr.length; ++i) {
            this.setTextSize(size[i]);
            staticLayout = new StaticLayout(colTextArr[i], this.textPaint, width * colWidthArr[i] / totalWitchRadio, (Alignment)this.mAlignments.get(i), this.textLineSpacing, 0.0F, false);
            allTextHeight[i] = staticLayout.getHeight();
        }

        Bitmap newBitmap = Bitmap.createBitmap(width, this.getMax(allTextHeight), Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);

        for(int j = 0; j < colNum; ++j) {
            this.setTextSize(size[j]);
            staticLayout = new StaticLayout(colTextArr[j], this.textPaint, width * colWidthArr[j] / totalWitchRadio, (Alignment)this.mAlignments.get(j), this.textLineSpacing, 0.0F, false);
            staticLayout.draw(canvas);
            canvas.translate((float)(width * colWidthArr[j] / totalWitchRadio), 0.0F);
        }

        return newBitmap;
    }

    private Bitmap getTextBitmap(String text) {
        if (this.textPaint == null) {
            this.textPaint = new TextPaint();
        }

        this.textPaint.setTextSize((float)this.textSize);
        this.textPaint.setTypeface(this.textTypeface);
        this.textPaint.setUnderlineText(this.haveUnderline);
        this.textPaint.setFakeBoldText(this.haveBold);
        Log.d("XGH", "textSize:" + this.textSize + " fontSizeStyle:" + this.fontSizeStyle + "  imin_printer_size_style:" + Global.getInt(this.mContext.getContentResolver(), "imin_printer_size_style", 0));
        if (Global.getInt(this.mContext.getContentResolver(), "imin_printer_size_style", 0) != this.fontSizeStyle) {
            this.fontSizeStyle = Global.getInt(this.mContext.getContentResolver(), "imin_printer_size_style", 0);
            this.setTextSize(this.defaultTextSize);
        }

        if (Global.getInt(this.mContext.getContentResolver(), "imin_printer_bold_style", 0) != this.boldStyle) {
            this.boldStyle = Global.getInt(this.mContext.getContentResolver(), "imin_printer_bold_style", 0);
            this.sethaveBold(this.boldStyle == 1);
        }

        if (Global.getInt(this.mContext.getContentResolver(), "imin_printer_underline_style", 0) != this.underlineStyle) {
            this.underlineStyle = Global.getInt(this.mContext.getContentResolver(), "imin_printer_underline_style", 0);
            this.setUnderline(this.underlineStyle == 1);
        }

        if (Global.getInt(this.mContext.getContentResolver(), "imin_printer_lineHeight_style", 0) != this.lineHeighSize) {
            this.lineHeighSize = Global.getInt(this.mContext.getContentResolver(), "imin_printer_lineHeight_style", 0);
            this.setHaveLineHeight((float)(100 + this.lineHeighSize) / 100.0F);
        }

        StaticLayout staticLayout = new StaticLayout(text, this.textPaint, textWidth, this.textAlignment, this.textLineSpacing, 1.0F, false);
        Bitmap newBitmap = Bitmap.createBitmap(textWidth, staticLayout.getHeight(), Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        staticLayout.draw(canvas);
        return newBitmap;
    }

    private String byte2HexStr(byte[] b) {
        String stmp = "";
        StringBuilder sb = new StringBuilder("");

        for(int n = 0; n < b.length; ++n) {
            stmp = Integer.toHexString(b[n] & 255);
            sb.append(stmp.length() == 1 ? "0" + stmp : stmp);
        }

        return sb.toString().toUpperCase().trim();
    }

    public void printerByte(byte[] bytes) {
        Log.e("xgh", "printerByte  " + connectType);
        if (bytes != null && bytes.length != 0) {
            if (0 == connectType) {
                this.usbPrinter.writePort(bytes, bytes.length);
            } else if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                this.mComPort.send(bytes);
            } else if (2 == connectType) {
                if (this.bluetoothPrintUtil == null) {
                    return;
                }

                this.bluetoothPrintUtil.printRawBytes(bytes);
            } else if (3 == connectType) {
                PrintUtils.printSPISelfByte(bytes);
            }

        }
    }

    public void printerByte() {
        Log.e("xgh", "printerByte  ");
        byte[] bytes = new byte[]{27, 64};
        this.bluetoothPrintUtil.printRawBytes(bytes);
    }

    private void printBitmaps(List<Bitmap> bitmaps, int alignmentMode) {
        Iterator var3 = bitmaps.iterator();

        label68:
        while(var3.hasNext()) {
            Bitmap bitmap = (Bitmap)var3.next();
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            int newWidth = bitmapWidth;
            if (width > newWidth) {
                bitmap = Bitmap.createScaledBitmap(bitmap, width > newWidth ? newWidth : width, width > newWidth ? height * newWidth / width : height, true);
            }

            if (0 == alignmentMode) {
                this.setLeftMargin(0);
            } else if (1 == alignmentMode) {
                this.setLeftMargin((bitmapWidth - bitmap.getWidth()) / 2);
            } else if (2 == alignmentMode) {
                this.setLeftMargin(bitmapWidth - bitmap.getWidth());
            }

            ArrayList<Bitmap> cutBitmap = this.cutBitmap(120, bitmap);
            Iterator var9 = cutBitmap.iterator();

            while(true) {
                while(true) {
                    if (!var9.hasNext()) {
                        continue label68;
                    }

                    Bitmap bit = (Bitmap)var9.next();
                    byte[] imgBytes = this.PrintDiskImagefile(this.getPixelsByBitmap(bit), bit.getWidth(), bit.getHeight());
                    if (0 == connectType) {
                        this.usbPrinter.writePort(imgBytes, imgBytes.length);
                    } else if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                        this.mComPort.send(imgBytes);
                    } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                        this.bluetoothPrintUtil.printRawBytes(imgBytes);
                    } else if (3 == connectType) {
                        PrintUtils.printSPISelfByte(imgBytes);
                    }
                }
            }
        }

    }

    private void printBitmaps(List<Bitmap> bitmaps) {
        Iterator var2 = bitmaps.iterator();

        label58:
        while(var2.hasNext()) {
            Bitmap bitmap = (Bitmap)var2.next();
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            int newWidth = bitmapWidth;
            if (width > newWidth) {
                bitmap = Bitmap.createScaledBitmap(bitmap, width > newWidth ? newWidth : width, width > newWidth ? height * newWidth / width : height, true);
            }

            ArrayList<Bitmap> cutBitmap = this.cutBitmap(120, bitmap);
            Iterator var8 = cutBitmap.iterator();

            while(true) {
                while(true) {
                    if (!var8.hasNext()) {
                        continue label58;
                    }

                    Bitmap bit = (Bitmap)var8.next();
                    byte[] imgBytes = this.PrintDiskImagefile(this.getPixelsByBitmap(bit), bit.getWidth(), bit.getHeight());
                    if (0 == connectType) {
                        this.getUsbPrinter().writePort(imgBytes, imgBytes.length);
                    } else if (1 == connectType && this.mComPort != null && this.mComPort.isOpen() && SerialHelper.paper) {
                        this.mComPort.send(imgBytes);
                    } else if (2 == connectType && this.bluetoothPrintUtil != null) {
                        this.bluetoothPrintUtil.printRawBytes(imgBytes);
                    } else if (3 == connectType) {
                        PrintUtils.printSPISelfByte(imgBytes);
                    }
                }
            }
        }

    }

    private byte[] PrintDiskImagefile(int[] var0, int var1, int var2) {
        byte[] var4;
        if ((var1 = (var4 = this.getBMPImageFileByte(var0, var1, var2)).length) == 0) {
            return null;
        } else {
            byte[] var5 = new byte[var1];

            for(int var3 = 0; var3 < var1; ++var3) {
                var5[var3] = var4[var3];
            }

            return var5;
        }
    }

    private byte[] getBMPImageFileByte(int[] var1, int var2, int var3) {
        int var4 = var2 / 8;
        if ((var2 %= 8) > 0) {
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
                if (var1[var10++] < -1) {
                    var12 += 128;
                }

                if (var1[var10++] < -1) {
                    var12 += 64;
                }

                if (var1[var10++] < -1) {
                    var12 += 32;
                }

                if (var1[var10++] < -1) {
                    var12 += 16;
                }

                if (var1[var10++] < -1) {
                    var12 += 8;
                }

                if (var1[var10++] < -1) {
                    var12 += 4;
                }

                if (var1[var10++] < -1) {
                    var12 += 2;
                }

                if (var1[var10++] < -1) {
                    ++var12;
                }

                var5[var11++] = (byte)var12;
            }

            var12 = 0;
            if (var2 == 0) {
                for(var13 = 8; var13 > var2; --var13) {
                    if (var1[var10++] < -1) {
                        var12 += 1 << var13;
                    }
                }
            } else {
                for(var13 = 0; var13 < var2; ++var13) {
                    if (var1[var10++] < -1) {
                        var12 += 1 << 8 - var13;
                    }
                }
            }

            var5[var11++] = (byte)var12;
        }

        return var5;
    }

    private ArrayList<Bitmap> cutBitmap(int h, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        boolean full = height % h == 0;
        int n = height % h == 0 ? height / h : height / h + 1;
        ArrayList<Bitmap> bitmaps = new ArrayList();

        for(int i = 0; i < n; ++i) {
            Bitmap b;
            if (full) {
                b = Bitmap.createBitmap(bitmap, 0, i * h, width, h);
            } else if (i == n - 1) {
                b = Bitmap.createBitmap(bitmap, 0, i * h, width, height - i * h);
            } else {
                b = Bitmap.createBitmap(bitmap, 0, i * h, width, h);
            }

            bitmaps.add(b);
        }

        return bitmaps;
    }

    private int[] getPixelsByBitmap(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        int iDataLen = width * height;
        int[] pixels = new int[iDataLen];
        bm.getPixels(pixels, 0, width, 0, 0, width, height);
        return pixels;
    }

    private int getMax(int[] arr) {
        int max = 0;

        for(int i = 0; i < arr.length; ++i) {
            max = max > arr[i] ? max : arr[i];
        }

        return max;
    }

    private int getTotal(int[] arr) {
        int total = 0;

        for(int i = 0; i < arr.length; ++i) {
            total += arr[i];
        }

        return total;
    }

    private byte[] intToByte(int... cmd) {
        byte[] bytes = new byte[cmd.length];

        for(int i = 0; i < cmd.length; ++i) {
            bytes[i] = (byte)cmd[i];
        }

        return bytes;
    }

    private int changeCommonStatus(int iStatus) {
        int commonSatus;
        switch(iStatus) {
            case 0:
            case 18:
                commonSatus = 0;
                break;
            case 1:
                commonSatus = 1;
                break;
            case 2:
            case 4:
            case 5:
            case 6:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 19:
            case 20:
            case 21:
            default:
                commonSatus = iStatus;
                break;
            case 3:
                commonSatus = 3;
                break;
            case 7:
                commonSatus = 7;
                break;
            case 8:
                commonSatus = 8;
                break;
            case 22:
                commonSatus = 99;
        }

        return commonSatus;
    }

    public void printSelfPageENTest() {
        if (this.mComPort != null && this.mComPort.isOpen()) {
            this.mComPort.sendHex("1B401D7630000100170000000000000000000000000000000000000000000000001D7630002A00580000000000000000FE0000000007FFFFFFFFC00007FFFFFFFFE000000000FE000000000FFFFFFFFFC0000000000000000003FF800000007FFFFFFFFFF8003FFFFFFFFFFC00000003FF800000007FFFFFFFFFF8000000000000000007FFC0000000FFFFFFFFFFFE00FFFFFFFFFFFF00000007FFC0000001FFFFFFFFFFFE00000000000000000FFFE0000003FFFFFFFFFFFF83FFFFFFFFFFFF8000000FFFE0000003FFFFFFFFFFFF80000000000000001FFFF000000FFFFFFFFFFFFFC7FFFFFFFFFFFFE000001FFFF000000FFFFFFFFFFFFFC0000000000000001FFFF000001FFFFFFFFFFFFFFFFFFFFFFFFFFFF000001FFFF000001FFFFFFFFFFFFFF0000000000000001FFFF800003FFFFFFFFFFFFFFFFFFFFFFFFFFFF800001FFFF000003FFFFFFFFFFFFFF8000000000000003FFFF800007FFFFFFFFFFFFFFFFFFFFFFFFFFFFC00003FFFF800007FFFFFFFFFFFFFFC000000000000003FFFF80000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFE00003FFFF80000FFFFFFFFFFFFFFFE000000000000003FFFF80001FFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00003FFFF80001FFFFFFFFFFFFFFFE000000000000003FFFF80003FFFFFFFFFFFFFFFFFFFFFFFFFFFFFF80003FFFF80003FFFFFFFFFFFFFFFF000000000000001FFFF80007FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC0003FFFF80007FFFFFFFFFFFFFFFF800000000000001FFFF00007FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC0001FFFF00007FFFFFFFFFFFFFFFFC00000000000001FFFF0000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFE0001FFFF0000FFFFFFFFFFFFFFFFFC00000000000000FFFE0000FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFE0000FFFE0000FFFFFFFFFFFFFFFFFE000000000000007FFE0001FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF0000FFFC0001FFFFFFFFFFFFFFFFFE000000000000003FFC0001FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00003FF80001FFFFFFFFFFFFFFFFFE000000000000001FF00001FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00001FF00001FFFFFFFFFFFFFFFFFF0000000000000007C00001FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF800007C00003FFFFFFFFFFFFFFFFFF0000000000000000000003FFFFF00000001FFFFFF00000001FFFFF800000000003FFFFF00000001FFFFF0000000000000000000003FFFFC00000000FFFFFE000000007FFFF800000000003FFFFC00000000FFFFF0000000000000000000003FFFF8000000007FFFFC000000003FFFF800000000003FFFF8000000007FFFF0000000000000000000003FFFF8000000003FFFF8000000003FFFF800000000003FFFF8000000003FFFF8000000000000000000003FFFF0000000003FFFF8000000001FFFF800000000003FFFF0000000003FFFF8000000000000000000003FFFF0000000003FFFF8000000001FFFF800000000003FFFF0000000003FFFF800000000000000FE00003FFFF0000000003FFFF8000000001FFFF80000FE00003FFFF0000000003FFFF800000000000003FF80003FFFF0000000003FFFF8000000001FFFF80003FF80003FFFF0000000003FFFF800000000000007FFC0003FFFF0000000003FFFF8000000001FFFF80007FFC0003FFFF0000000003FFFF80000000000000FFFE0003FFFF0000000003FFFF8000000001FFFF8000FFFE0003FFFF0000000003FFFF80000000000000FFFF0003FFFF0000000003FFFF8000000001FFFF8001FFFF0003FFFF0000000003FFFF80000000000001FFFF0003FFFF0000000003FFFF8000000001FFFF8001FFFF0003FFFF0000000003FFFF80000000000001FFFF0003FFFF0000000003FFFF8000000001FFFF8001FFFF0003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000003FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF80000000000001FFFF8003FFFF0000000003FFFF8000000001FFFF8003FFFF8003FFFF0000000003FFFF00000000000001FFFF0003FFFF0000000001FFFF0000000001FFFF8001FFFF0003FFFF0000000001FFFF00000000000001FFFF0001FFFE0000000001FFFF0000000000FFFF0001FFFF0001FFFE0000000001FFFF00000000000000FFFE0001FFFE0000000000FFFE0000000000FFFF0000FFFE0001FFFE0000000000FFFE000000000000007FFC0000FFFC00000000007FFC00000000007FFE00007FFC0000FFFC00000000007FFC000000000000003FFC00007FF800000000003FF800000000003FFC00003FF800007FF800000000003FF8000000000000001FF800003FF000000000001FF000000000001FF800001FF000003FE000000000003FF00000000000000003C000000780000000000007C0000000000003C0000007C0000007800000000000078000A0A");
            this.mComPort.sendHex("1B6101");
            this.mComPort.sendHex("1D2101");
            this.mComPort.sendTxt("Welcome to printer test\n\n\n");
            this.mComPort.sendHex("1B3301");
            this.mComPort.sendTxt("This is aline of normal fontsThis is aline of normal fonts\n");
            this.mComPort.sendHex("1B331E");
            this.mComPort.sendTxt("This is aline of normal fontsThis is aline of normal fonts\n");
            this.mComPort.sendHex("1B3364");
            this.mComPort.sendTxt("This is aline of normal fontsThis is aline of normal fonts\n");
            this.mComPort.sendHex("1B6101");
            this.mComPort.sendHex("1D2100");
            this.mComPort.sendTxt("******************************\n\n");
            this.mComPort.sendHex("1B6100");
            this.mComPort.sendHex("1D2100");
            this.mComPort.sendTxt("This is aline of normal fonts\n\n");
            this.mComPort.sendHex("1B6100");
            this.mComPort.sendHex("1D2101");
            this.mComPort.sendTxt("This is aline of 30-point fonts\n\n");
            this.mComPort.sendHex("1B6100");
            this.mComPort.sendHex("1D2102");
            this.mComPort.sendTxt("This is aline of 36-point fonts\n\n");
            this.mComPort.sendHex("1B6100");
            this.mComPort.sendHex("1D2103");
            this.mComPort.sendTxt("This is aline of 42-point fonts\n\n");
            this.mComPort.sendHex("1B6101");
            this.mComPort.sendHex("1D2100");
            this.mComPort.sendTxt("******************************\n\n");
            this.mComPort.sendHex("1B6101");
            this.mComPort.sendHex("1D2100");
            this.mComPort.sendTxt("***Completed***\n\n\n\n\n");
        }

    }

    public void printSelfPageTest() {
        if (this.mComPort != null && this.mComPort.isOpen()) {
            this.mComPort.sendHex("1B6101");
            this.mComPort.sendHex("1D2101");
            this.mComPort.sendTxt("SELF-TEST\n\n");
            this.mComPort.sendHex("1B6101");
            this.mComPort.sendHex("1D2104");
            this.mComPort.sendHex("1D2140");
            this.mComPort.sendHex("1B4501");
            this.mComPort.sendTxt("iMin\n\n");
            this.mComPort.sendHex("1B6100");
            this.mComPort.sendHex("1D2100");
            this.mComPort.sendTxt("这是一行24号字体\n\n");
            this.mComPort.sendHex("1B6100");
            this.mComPort.sendHex("1D2101");
            this.mComPort.sendTxt("这是一行30号字体\n\n");
            this.mComPort.sendHex("1B6100");
            this.mComPort.sendHex("1D2102");
            this.mComPort.sendTxt("这是一行36号字体\n\n");
            this.mComPort.sendHex("1B6100");
            this.mComPort.sendHex("1D2103");
            this.mComPort.sendTxt("这是一行42号字体\n\n");
            this.mComPort.sendHex("1B6101");
            this.mComPort.sendHex("1D286B0300314308");
            this.mComPort.sendHex("1B6402");
            this.mComPort.sendHex("1D286B14003150304E454F5354524130313233343536373839");
            this.mComPort.sendHex("1D286B0D00315130");
            this.mComPort.sendHex("1B6402");
            this.mComPort.sendHex("1B6402");
        }

    }

    public void initParams() {
        Log.d("xgh", "initParams 初始化参数:");
        this.textSize = 28;
        this.defaultTextSize = 28;
        this.haveUnderline = false;
        this.haveBold = false;
        this.lineHeightRatio = 0.0F;
        this.fontSizeStyle = 0;
        this.underlineStyle = 0;
        this.boldStyle = 0;
        this.lineHeighSize = 0;
        this.textLineSpacing = 1.0F;
        String sModel = Build.MODEL;
        Log.e("Build.MODEL", " Build.MODEL==   " + Build.MODEL);
        if (sModel.contains("M2")) {
            textWidth = 385;
        } else {
            textWidth = 576;
        }

        bitmapWidth = 576;
        this.textTypeface = Typeface.DEFAULT;
        this.textStyle = 0;
        this.textAlignment = Alignment.ALIGN_NORMAL;
        this.mAlignments.clear();
        if (this.stringBuilder != null) {
            this.stringBuilder.delete(0, this.stringBuilder.length());
        }

        this.fontSizeStyle = Global.getInt(this.mContext.getContentResolver(), "imin_printer_size_style", 0);
        this.boldStyle = Global.getInt(this.mContext.getContentResolver(), "imin_printer_bold_style", 0);
        this.underlineStyle = Global.getInt(this.mContext.getContentResolver(), "imin_printer_underline_style", 0);
        this.lineHeighSize = Global.getInt(this.mContext.getContentResolver(), "imin_printer_lineHeight_style", 0);
        this.lineHeightRatio = (float)(100 + this.lineHeighSize) / 100.0F;
        Log.d("XGH", "fontSizeStyle:" + this.fontSizeStyle + " textSize:" + this.textSize + "boldStyle:" + this.boldStyle + "underlineStyle:" + this.underlineStyle + "lineHeighSize:" + this.lineHeighSize);
        this.setTextSize(this.defaultTextSize);
        this.sethaveBold(this.haveBold);
        this.setUnderline(this.haveUnderline);
        this.setHaveLineHeight(this.lineHeightRatio);
        this.setBarCodeWidth(3);
        this.setBarCodeHeight(100);
        this.setBarCodeContentPrintPos(0);
        this.setQrCodeSize(9);
        this.setQrCodeErrorCorrectionLev(51);
        this.setLeftMargin(0);
    }

    public class StatusThread extends Thread {
        boolean isInterrupt = false;

        public StatusThread() {
        }

        public void run() {
            while(!this.isInterrupt) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var3) {
                    var3.printStackTrace();
                }

                if (IminPrintUtils.this.mComPort != null && IminPrintUtils.this.mComPort.isOpen()) {
                    if (IminPrintUtils.this.mComPort.mInputStream == null) {
                        Log.d("xgh", "getPrinterStatus spi 4444   -->" + IminPrintUtils.this.printerStatus);
                        IminPrintUtils.this.printerStatus = -1;
                    }

                    try {
                        byte[] buff = new byte[1];
                        if (IminPrintUtils.this.mComPort.mInputStream.available() > 0) {
                            IminPrintUtils.this.mComPort.mInputStream.read(buff);
                            Log.d("xgh", "getPrinterStatus spi 5555-->" + IminPrintUtils.this.printerStatus);
                            IminPrintUtils.this.printerStatus = IminPrintUtils.this.changeCommonStatus(buff[0]);
                            PrintUtils.setPrintStatus(IminPrintUtils.this.printerStatus);
                            Log.d("xgh", "  getPrinterStatus values = " + buff[0] + " printerStatus:" + IminPrintUtils.this.printerStatus);
                            Log.d("xgh", "getPrinterStatus spi 6666-->" + IminPrintUtils.this.printerStatus);
                        }
                    } catch (IOException var2) {
                        Log.d("IminPrintUtils", "getPrinterStatus   IOException " + var2.getMessage());
                        var2.printStackTrace();
                    }
                } else {
                    Log.d("xgh", "getPrinterStatus spi 7777-->" + IminPrintUtils.this.printerStatus);
                    IminPrintUtils.this.printerStatus = -1;
                }
            }

        }

        public void interrupt() {
            this.isInterrupt = true;
            super.interrupt();
        }
    }

    public static enum PrintConnectType {
        USB,
        BLUETOOTH,
        SPI;

        private PrintConnectType() {
        }
    }
}

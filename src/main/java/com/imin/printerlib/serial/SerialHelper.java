//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.imin.printerlib.serial;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.serialport.SerialPort;
import android.util.Log;
import com.imin.printerlib.IminPrintUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;
import java.util.Arrays;

public abstract class SerialHelper {
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    public InputStream mInputStream;
    private SerialHelper.ReadThread mReadThread;
    private SerialHelper.SendThread mSendThread;
    private String sPort;
    private int iBaudRate;
    private boolean _isOpen;
    private byte[] _bLoopData;
    private int iDelay;
    public static boolean paper = false;
    private final int READ_PRINTER_CODE;
    private final int READ_PRINTER_CODE_AVAILABLE;
    private final int READ_PRINTER_STOP;
    private Handler mHandler;

    public SerialHelper(String sPort, int iBaudRate) {
        this.sPort = "/dev/ttyMT1";
        this.iBaudRate = 115200;
        this._isOpen = false;
        this._bLoopData = new byte[]{48};
        this.iDelay = 500;
        this.READ_PRINTER_CODE = 100;
        this.READ_PRINTER_CODE_AVAILABLE = 200;
        this.READ_PRINTER_STOP = 300;
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case 100:
                        SerialHelper.this.readPrinterStatus();
                        break;
                    case 200:
//                        int count = false;
                        Object var3 = null;

                        try {
                            if (SerialHelper.this.mInputStream == null) {
                                return;
                            }

                            int var6 = SerialHelper.this.mInputStream.available();
                        } catch (Exception var5) {
                            var5.printStackTrace();
                        }

                        SerialHelper.this.mHandler.removeMessages(200);
                        SerialHelper.this.mHandler.sendEmptyMessageDelayed(200, 30L);
                    case 300:
                }

                super.handleMessage(msg);
            }
        };
        this.sPort = sPort;
        this.iBaudRate = iBaudRate;
    }

    public SerialHelper() {
        this("/dev/ttyMT1", 115200);
    }

    public SerialHelper(String sPort) {
        this(sPort, 115200);
    }

    public SerialHelper(String sPort, String sBaudRate) {
        this(sPort, Integer.parseInt(sBaudRate));
    }

    public void open() throws SecurityException, IOException, InvalidParameterException {
        this.mSerialPort = new SerialPort(new File(this.sPort), this.iBaudRate, 0);
        this.mOutputStream = this.mSerialPort.getOutputStream();
        this.mInputStream = this.mSerialPort.getInputStream();
        this._isOpen = true;
        if (IminPrintUtils.connectType != 3) {
            this.mReadThread = new SerialHelper.ReadThread();
            this.mReadThread.start();
            this.mSendThread = new SerialHelper.SendThread();
            this.mSendThread.setSuspendFlag();
            this.mSendThread.start();
        }
    }

    public void close() {
        if (this.mSendThread != null) {
            this.mSendThread.interrupt();
            this.mSendThread = null;
        }

        if (this.mReadThread != null) {
            this.mReadThread.interrupt();
            this.mReadThread = null;
        }

        if (this.mSerialPort != null) {
            this.mSerialPort.close();
            this.mSerialPort = null;
        }

        this._isOpen = false;
    }

    private void sendStatus() {
        String cmdStatus = "100402";
        byte[] bOutArray = MyFunc.HexToByteArr(cmdStatus);

        try {
            bOutArray.toString().getBytes("GB18030");
            this.mOutputStream.write(bOutArray);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public void send(byte[] bOutArray) {
        try {
            if (paper) {
                bOutArray.toString().getBytes("GB18030");
                this.mOutputStream.write(bOutArray);
            }
        } catch (IOException var3) {
            var3.printStackTrace();
        }

    }

    public void sendHex(String sHex) {
        byte[] bOutArray = MyFunc.HexToByteArr(sHex);
        this.send(bOutArray);
    }

    public void sendByte(byte[] bOutArray) {
        Log.d("xgh", "usbPrinter ==> " + Arrays.toString(bOutArray));
        this.send(bOutArray);
    }

    public void sendTxt(String sTxt) {
        try {
            byte[] bOutArray = sTxt.getBytes("GB18030");
            this.send(bOutArray);
        } catch (UnsupportedEncodingException var4) {
            var4.printStackTrace();
        }

    }

    public int getBaudRate() {
        return this.iBaudRate;
    }

    public boolean setBaudRate(int iBaud) {
        if (this._isOpen) {
            return false;
        } else {
            this.iBaudRate = iBaud;
            return true;
        }
    }

    public boolean setBaudRate(String sBaud) {
        int iBaud = Integer.parseInt(sBaud);
        return this.setBaudRate(iBaud);
    }

    public String getPort() {
        return this.sPort;
    }

    public boolean setPort(String sPort) {
        if (this._isOpen) {
            return false;
        } else {
            this.sPort = sPort;
            return true;
        }
    }

    public boolean isOpen() {
        return this._isOpen;
    }

    public byte[] getbLoopData() {
        return this._bLoopData;
    }

    public void setbLoopData(byte[] bLoopData) {
        this._bLoopData = bLoopData;
    }

    public void setTxtLoopData(String sTxt) {
        this._bLoopData = sTxt.getBytes();
    }

    public void setHexLoopData(String sHex) {
        this._bLoopData = MyFunc.HexToByteArr(sHex);
    }

    public int getiDelay() {
        return this.iDelay;
    }

    public void setiDelay(int iDelay) {
        this.iDelay = iDelay;
    }

    public void startSend() {
        if (this.mSendThread != null) {
            this.mSendThread.setResume();
        }

    }

    public void stopSend() {
        if (this.mSendThread != null) {
            this.mSendThread.setSuspendFlag();
        }

    }

    protected abstract void onDataReceived(ComBean var1);

    private void readPrinterStatus() {
        try {
            byte[] buffer = new byte[512];
//            int readCount = false;
            int readCount = this.mInputStream.read(buffer);
            if (readCount > 0) {
                String value = new String(buffer);
                if (value.length() > 0) {
                    this.mHandler.removeMessages(100);
                    this.mHandler.removeMessages(200);
                    this.mHandler.sendEmptyMessageDelayed(200, 20L);
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public String getStrFromInsByCode(InputStream is, String code) {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(is, code));

            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return builder.toString();
    }

    private class SendThread extends Thread {
        public boolean suspendFlag;

        private SendThread() {
            this.suspendFlag = true;
        }

        public void run() {
            super.run();

            while(!this.isInterrupted()) {
                synchronized(this) {
                    while(this.suspendFlag) {
                        try {
                            this.wait();
                        } catch (InterruptedException var5) {
                            var5.printStackTrace();
                        }
                    }
                }

                SerialHelper.this.send(SerialHelper.this.getbLoopData());

                try {
                    Thread.sleep((long)SerialHelper.this.iDelay);
                } catch (InterruptedException var4) {
                    var4.printStackTrace();
                }
            }

        }

        public void setSuspendFlag() {
            this.suspendFlag = true;
        }

        public synchronized void setResume() {
            this.suspendFlag = false;
            this.notify();
        }
    }

    private class ReadThread extends Thread {
        private ReadThread() {
        }

        public void run() {
            super.run();

            while(true) {
                try {
                    SerialHelper.this.sendStatus();

                    try {
                        Thread.sleep(30L);
                    } catch (InterruptedException var4) {
                        var4.printStackTrace();
                    }

                    if (SerialHelper.this.mInputStream == null) {
                        return;
                    }

                    byte[] buffer = new byte[512];
                    if (SerialHelper.this.mInputStream.available() != 0) {
                        int size = SerialHelper.this.mInputStream.read(buffer);
                        if (size > 0) {
                            ComBean ComRecData = new ComBean(SerialHelper.this.sPort, buffer, size);
                            SerialHelper.paper = MyFunc.ByteArrToHex(ComRecData.bRec).contains("12");
                        }
                    }
                } catch (Throwable var5) {
                    var5.printStackTrace();
                    return;
                }
            }
        }
    }
}

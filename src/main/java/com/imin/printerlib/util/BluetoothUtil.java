package com.imin.printerlib.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothUtil {

   public static boolean isBluetoothOn() {
      BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
      return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
   }

   public static List getPairedDevices() {
      ArrayList deviceList = new ArrayList();
      Set pairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
      if(pairedDevices.size() > 0) {
         Iterator var2 = pairedDevices.iterator();

         while(var2.hasNext()) {
            BluetoothDevice device = (BluetoothDevice)var2.next();
            deviceList.add(device);
         }
      }

      return deviceList;
   }

   public static List getPairedPrinterDevices() {
      return getSpecificDevice(1536);
   }

   public static List getSpecificDevice(int deviceClass) {
      List devices = getPairedDevices();
      ArrayList printerDevices = new ArrayList();
      Iterator var3 = devices.iterator();

      while(var3.hasNext()) {
         BluetoothDevice device = (BluetoothDevice)var3.next();
         BluetoothClass klass = device.getBluetoothClass();
         if(klass.getMajorDeviceClass() == deviceClass) {
            printerDevices.add(device);
         }
      }

      return printerDevices;
   }

   public static void openBluetooth(Activity activity) {
      Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
      activity.startActivityForResult(enableBtIntent, 666);
   }

   public static BluetoothSocket connectDevice(BluetoothDevice device) {
      BluetoothSocket socket = null;

      try {
         socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
         socket.connect();
         return socket;
      } catch (IOException var5) {
         try {
            socket.close();
         } catch (IOException var4) {
            return null;
         }

         var5.printStackTrace();
         Log.d("yegf", " connect device error : " + var5.getMessage());
         return null;
      }
   }
}

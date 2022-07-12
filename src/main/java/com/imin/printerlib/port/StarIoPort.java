package com.imin.printerlib.port;

import android.content.Context;
import android.icu.lang.UCharacter;
import androidx.annotation.RequiresApi;
import com.imin.printerlib.bean.PortInfo;
import com.imin.printerlib.connect.StarPrinterStatus;
import com.imin.printerlib.exception.StarIoPortException;
import com.imin.printerlib.port.TCPPort;
import com.imin.printerlib.port.UsbPrinter;
import com.imin.printerlib.port.WBlueToothPort;
import com.imin.printerlib.port.WTCPPort;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

public abstract class StarIoPort {

   public String m_portName = "";
   public String m_portSettings = "";
   public int m_usageCount = 0;
   public static final Vector s_ports = new Vector();


   @RequiresApi(
      api = 24
   )
   public static synchronized ArrayList searchPrinter(String var0) throws StarIoPortException {
      String var1 = UCharacter.toUpperCase(var0);
      new ArrayList();
      ArrayList var2;
      if(var1.startsWith("TCP:")) {
         var2 = TCPPort.searchPrinter();
      } else {
         if(!var1.startsWith("BT:")) {
            throw new StarIoPortException("Invalid argument.");
         }

         var2 = WBlueToothPort.searchPrinter();
         if(!var1.equals("BT:")) {
            ArrayList var3 = new ArrayList();
            String var4 = var0.substring(3);
            Iterator var5 = var2.iterator();

            while(var5.hasNext()) {
               PortInfo var6 = (PortInfo)var5.next();
               String var7 = var6.getPortName().substring(3);
               if(var7.startsWith(var4)) {
                  var3.add(var6);
               }
            }

            return var3;
         }
      }

      return var2;
   }

   @RequiresApi(
      api = 24
   )
   public static synchronized StarIoPort getPort(String var0, String var1, int var2) throws StarIoPortException {
      if(var0 == null) {
         throw new StarIoPortException("Invalid port name.");
      } else {
         if(var1 == null) {
            var1 = "";
         }

         for(int var8 = 0; var8 < s_ports.size(); ++var8) {
            StarIoPort var4 = (StarIoPort)s_ports.get(var8);
            if(var4.getPortName().equals(var0)) {
               if(var4.m_portSettings.equals(var1)) {
                  synchronized(var4) {
                     ++var4.m_usageCount;
                     return var4;
                  }
               }

               throw new StarIoPortException("This port is already opened and is configured with different settings.");
            }
         }

         if(WTCPPort.MatchPort(var0) && UCharacter.toUpperCase(var1).contains("MINI")) {
            WTCPPort var10 = new WTCPPort(var0, var1, var2);
            var10.m_usageCount = 1;
            s_ports.add(var10);
            var10.m_portName = var0;
            var10.m_portSettings = var1;
            return var10;
         } else if(TCPPort.MatchPort(var0)) {
            TCPPort var9 = new TCPPort(var0, var1, var2);
            var9.m_usageCount = 1;
            s_ports.add(var9);
            var9.m_portName = var0;
            var9.m_portSettings = var1;
            return var9;
         } else if(WBlueToothPort.MatchPort(var0) && UCharacter.toUpperCase(var1).contains("MINI")) {
            WBlueToothPort var81 = new WBlueToothPort(var0, var1, var2);
            var81.m_usageCount = 1;
            s_ports.add(var81);
            var81.m_portName = var0;
            var81.m_portSettings = var1;
            return var81;
         } else {
            throw new StarIoPortException("Failed to open port");
         }
      }
   }

   @RequiresApi(
      api = 24
   )
   public static synchronized StarIoPort getPort(String var0, String var1, int var2, Context var3) throws StarIoPortException {
      if(var0 == null) {
         throw new StarIoPortException("Invalid port name.");
      } else {
         if(var1 == null) {
            var1 = "";
         }

         for(int var9 = 0; var9 < s_ports.size(); ++var9) {
            StarIoPort var5 = (StarIoPort)s_ports.get(var9);
            if(var5.getPortName().equals(var0)) {
               if(var5.m_portSettings.equals(var1)) {
                  synchronized(var5) {
                     ++var5.m_usageCount;
                     return var5;
                  }
               }

               throw new StarIoPortException("This port is already opened and is configured with different settings.");
            }
         }

         if(WTCPPort.MatchPort(var0) && UCharacter.toUpperCase(var1).contains("MINI")) {
            WTCPPort var12 = new WTCPPort(var0, var1, var2);
            var12.m_usageCount = 1;
            s_ports.add(var12);
            var12.m_portName = var0;
            var12.m_portSettings = var1;
            return var12;
         } else if(TCPPort.MatchPort(var0)) {
            TCPPort var11 = new TCPPort(var0, var1, var2);
            var11.m_usageCount = 1;
            s_ports.add(var11);
            var11.m_portName = var0;
            var11.m_portSettings = var1;
            return var11;
         } else if(WBlueToothPort.MatchPort(var0) && UCharacter.toUpperCase(var1).contains("MINI")) {
            WBlueToothPort var10 = new WBlueToothPort(var0, var1, var2);
            var10.m_usageCount = 1;
            s_ports.add(var10);
            var10.m_portName = var0;
            var10.m_portSettings = var1;
            return var10;
         } else if(UsbPrinter.matchPort(var0)) {
            UsbPrinter var91 = new UsbPrinter(var3);
            var91.m_usageCount = 1;
            s_ports.add(var91);
            var91.m_portName = var0;
            var91.m_portSettings = var1;
            return var91;
         } else {
            throw new StarIoPortException("Failed to open port");
         }
      }
   }

   public static void InterruptOpen() throws StarIoPortException {
      WBlueToothPort.InterruptOpen();
   }

   public static synchronized void releasePort(StarIoPort var0) throws StarIoPortException {
      for(int var1 = 0; var1 < s_ports.size(); ++var1) {
         StarIoPort var2 = (StarIoPort)s_ports.get(var1);
         if(var2 == var0) {
            synchronized(var2) {
               if(--var2.m_usageCount == 0) {
                  var2.closeNative();
                  s_ports.remove(var2);
               }
            }
         }
      }

   }

   public synchronized String getPortName() {
      return this.m_portName;
   }

   public synchronized String getPortSettings() {
      return this.m_portSettings;
   }

   public abstract void writePort(byte[] var1, int var2) throws StarIoPortException;

   public abstract int readPort(byte[] var1, int var2, int var3) throws StarIoPortException;

   protected abstract void closeNative() throws StarIoPortException;

   public abstract StarPrinterStatus retreiveStatus() throws StarIoPortException;

   public abstract StarPrinterStatus beginCheckedBlock() throws StarIoPortException;

   public abstract StarPrinterStatus endCheckedBlock() throws StarIoPortException;

}

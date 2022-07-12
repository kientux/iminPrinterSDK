package com.imin.printerlib.bean;


public class PortInfo {

   private String portName;
   private String macAddress;
   private String modelName;


   public PortInfo(String portName, String macAddress, String modelName) {
      this.portName = portName;
      this.macAddress = macAddress;
      this.modelName = modelName;
   }

   public String getPortName() {
      return this.portName;
   }

   public String getMacAddress() {
      return this.macAddress;
   }

   public String getModelName() {
      return this.modelName;
   }
}

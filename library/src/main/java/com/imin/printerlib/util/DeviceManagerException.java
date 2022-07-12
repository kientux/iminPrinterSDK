package com.imin.printerlib.util;


public class DeviceManagerException extends RuntimeException {

   int error;


   public DeviceManagerException(int err, String message) {
      super(message);
      this.error = err;
   }

   public String toString() {
      StringBuilder sb = new StringBuilder("{");
      sb.append("\"error\":").append(this.error);
      sb.append("\"msg\":").append(this.getMessage());
      sb.append('}');
      return sb.toString();
   }
}

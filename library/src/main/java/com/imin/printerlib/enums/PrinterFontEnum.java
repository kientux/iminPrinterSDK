package com.imin.printerlib.enums;


public enum PrinterFontEnum {

   LARGE_FONT("LARGE_FONT", 0, 0),
   NOMAR_FONT("NOMAR_FONT", 1, 1),
   DOUBLE_HEIGHT_CANCEL("DOUBLE_HEIGHT_CANCEL", 2, 0),
   DOUBLE_HEIGHT("DOUBLE_HEIGHT", 3, 16),
   DOUBLE_WIDTH("DOUBLE_WIDTH", 4, 32),
   DOUBLE_WIDTH_CANCEL("DOUBLE_WIDTH_CANCEL", 5, 0),
   EMPHASIZED_OFF("EMPHASIZED_OFF", 6, 0),
   EMPHASIZED_ON("EMPHASIZED_ON", 7, 8);
   private int value;
   // $FF: synthetic field
   private static final PrinterFontEnum[] $VALUES = new PrinterFontEnum[]{LARGE_FONT, NOMAR_FONT, DOUBLE_HEIGHT_CANCEL, DOUBLE_HEIGHT, DOUBLE_WIDTH, DOUBLE_WIDTH_CANCEL, EMPHASIZED_OFF, EMPHASIZED_ON};


   private PrinterFontEnum(String var1, int var2, int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }

}

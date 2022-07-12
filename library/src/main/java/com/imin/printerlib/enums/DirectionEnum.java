package com.imin.printerlib.enums;


public enum DirectionEnum {

   LEFT_TO_RIGHT("LEFT_TO_RIGHT", 0, 0),
   BOTTOM_TO_TOP("BOTTOM_TO_TOP", 1, 1),
   RIGHT_TO_LEFT("RIGHT_TO_LEFT", 2, 2),
   TOP_TO_BOTTOM("TOP_TO_BOTTOM", 3, 3);
   private int value;
   // $FF: synthetic field
   private static final DirectionEnum[] $VALUES = new DirectionEnum[]{LEFT_TO_RIGHT, BOTTOM_TO_TOP, RIGHT_TO_LEFT, TOP_TO_BOTTOM};


   private DirectionEnum(String var1, int var2, int value) {
      this.value = value;
   }

   public int getValue() {
      return this.value;
   }

}

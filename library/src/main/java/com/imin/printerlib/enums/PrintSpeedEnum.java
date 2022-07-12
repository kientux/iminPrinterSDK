package com.imin.printerlib.enums;


public enum PrintSpeedEnum {

   DEFAULT_SPEED("DEFAULT_SPEED", 0, 0),
   LEVEL_ONE_SPEED("LEVEL_ONE_SPEED", 1, 1),
   LEVEL_TWO_SPEED("LEVEL_TWO_SPEED", 2, 2),
   LEVEL_THREE_SPEED("LEVEL_THREE_SPEED", 3, 3),
   LEVEL_FOUR_SPEED("LEVEL_FOUR_SPEED", 4, 4),
   LEVEL_FIVE_SPEED("LEVEL_FIVE_SPEED", 5, 5),
   LEVEL_SIX_SPEED("LEVEL_SIX_SPEED", 6, 6),
   LEVEL_SEVEN_SPEED("LEVEL_SEVEN_SPEED", 7, 7),
   LEVEL_EIGHT_SPEED("LEVEL_EIGHT_SPEED", 8, 8),
   LEVEL_NINE_SPEED("LEVEL_NINE_SPEED", 9, 9);
   private int speedValue;
   // $FF: synthetic field
   private static final PrintSpeedEnum[] $VALUES = new PrintSpeedEnum[]{DEFAULT_SPEED, LEVEL_ONE_SPEED, LEVEL_TWO_SPEED, LEVEL_THREE_SPEED, LEVEL_FOUR_SPEED, LEVEL_FIVE_SPEED, LEVEL_SIX_SPEED, LEVEL_SEVEN_SPEED, LEVEL_EIGHT_SPEED, LEVEL_NINE_SPEED};


   private PrintSpeedEnum(String var1, int var2, int speedValue) {
      this.speedValue = speedValue;
   }

   public int getSpeedValue() {
      return this.speedValue;
   }

}

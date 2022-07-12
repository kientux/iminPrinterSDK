package com.imin.printerlib.connect;


public class StarPrinterStatus implements Cloneable {

   public boolean coverOpen;
   public boolean offline;
   public boolean compulsionSwitch;
   public boolean overTemp;
   public boolean unrecoverableError;
   public boolean cutterError;
   public boolean mechError;
   public boolean headThermistorError;
   public boolean receiveBufferOverflow;
   public boolean pageModeCmdError;
   public boolean blackMarkError;
   public boolean presenterPaperJamError;
   public boolean headUpError;
   public boolean voltageError;
   public boolean receiptBlackMarkDetection;
   public boolean receiptPaperEmpty;
   public boolean receiptPaperNearEmptyInner;
   public boolean receiptPaperNearEmptyOuter;
   public boolean presenterPaperPresent;
   public boolean peelerPaperPresent;
   public boolean stackerFull;
   public boolean slipTOF;
   public boolean slipCOF;
   public boolean slipBOF;
   public boolean validationPaperPresent;
   public boolean slipPaperPresent;
   public boolean etbAvailable;
   public int etbCounter;
   public int presenterState;
   public int rawLength;
   public byte[] raw = new byte[63];


   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }
}

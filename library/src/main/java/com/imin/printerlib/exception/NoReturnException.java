package com.imin.printerlib.exception;


public class NoReturnException extends Exception {

   private static final long serialVersionUID = 2L;
   public int AmountRead;


   public NoReturnException(String var1) {
      super(var1);
   }
}

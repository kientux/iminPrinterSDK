package com.imin.printerlib.print;

import com.imin.printerlib.print.PrinterStater;

public interface PrinterFactory {

   PrinterStater createPrinterStater(int var1, int var2);
}

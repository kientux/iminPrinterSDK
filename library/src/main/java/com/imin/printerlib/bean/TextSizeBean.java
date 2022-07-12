package com.imin.printerlib.bean;


public class TextSizeBean {

   private String text;
   private int size;


   public TextSizeBean(String text, int size) {
      this.text = text;
      this.size = size;
   }

   public String getText() {
      return this.text == null?"":this.text;
   }

   public void setText(String text) {
      this.text = text;
   }

   public int getSize() {
      return this.size;
   }

   public void setSize(int size) {
      this.size = size;
   }
}

package com.lightreader.bzz.image;


import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.widget.ImageView;

public class ImageUtils {
	ImageUtils(){};
	public static Bitmap getBitmapFromImageRegion(BitmapRegionDecoder bitmapRegionDecoder, int imageRow, int imageCol, int row, int col) {
	  final int imgWidth = bitmapRegionDecoder.getWidth();
      final int imgHeight = bitmapRegionDecoder.getHeight();
      /*
      int right = beginx ;
      int bottom = beginy ;
      if(right > imgWidth) right = imgWidth;
      if(bottom > imgHeight) bottom = imgHeight;
      if(beginx < 0) beginx = 0;
      if(beginy < 0) beginy = 0;
      */
      int left = imgWidth / imageRow * (row - 1);
      int top = imgHeight / imageCol * (col-1);
      int right = imgWidth / imageRow * row;
      int bottom = imgHeight / imageCol * col;
      Rect rect = new Rect(left,top,right,bottom);
      Bitmap bitmap = bitmapRegionDecoder.decodeRegion(rect, null);
      return bitmap;
  }
}

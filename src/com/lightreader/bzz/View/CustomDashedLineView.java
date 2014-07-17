package com.lightreader.bzz.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 画虚线
 * @author baozhizhi
 *
 */
@SuppressLint("DrawAllocation")
public class CustomDashedLineView extends View {

	public CustomDashedLineView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	
	protected void onDraw(Canvas canvas) {
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.GRAY);
		
		Path path = new Path();
		path.moveTo(0, 8);
		path.lineTo(displayMetrics.heightPixels, 8);//显示屏幕的宽
		
	     
		PathEffect effects = new DashPathEffect(new float[] { 40, 15, 40, 15 }, 1);
		paint.setPathEffect(effects);
		canvas.drawPath(path, paint);
	}

}

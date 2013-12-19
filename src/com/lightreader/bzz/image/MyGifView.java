package com.lightreader.bzz.image;



import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

public class MyGifView extends View {
	private long movieStart;
	private Movie movie;

	// �˴�������д�ù��췽��
	public MyGifView(Context context, AttributeSet attributeSet,int resourceId) {
		super(context, attributeSet);
		// ���ļ�����InputStream����ȡ��gifͼƬ��Դ
		movie = Movie.decodeStream(getResources().openRawResource(resourceId));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		long curTime = android.os.SystemClock.uptimeMillis();
		// ��һ�β���
		if (movieStart == 0) {
			movieStart = curTime;
		}
		if (movie != null) {
			int duraction = movie.duration();//jifͼƬ���ܳ���
			int relTime = (int) ((curTime - movieStart) % duraction);
			movie.setTime(relTime);
			movie.draw(canvas, 0, 0);
			// ǿ���ػ�
			invalidate();
		}
		super.onDraw(canvas);
	}
}
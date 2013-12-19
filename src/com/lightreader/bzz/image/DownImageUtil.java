package com.lightreader.bzz.image;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;

public class DownImageUtil {
	private String image_path;

	public DownImageUtil(String image_path) {
		// ����ͼƬ�����ص�ַ
		this.image_path = image_path;
	}

	public void loadImage(final ImageCallback callback) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// ���ܵ���Ϣ�󣬵��ýӿڻص��ķ���
				callback.getDrawable((Drawable) msg.obj);
			}
		};
		
		// ����һ�����߳����ڷ���ͼƬ����
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// ����ͼƬΪDrawable����
					Drawable drawable = Drawable.createFromStream(new URL(image_path).openStream(), "");
					// ��ͼƬ�����װ��һ����Ϣ���͸�Handler
					Message message = Message.obtain();
					message.what = 1;
					message.obj = drawable;
					handler.sendMessage(message);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		
	}

	// ����һ�������Ľӿڣ�����ִ�лص�����
	public interface ImageCallback {
		public void getDrawable(Drawable draw);
	}
}

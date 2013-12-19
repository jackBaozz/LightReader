package com.lightreader.bzz.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;


public class StartPage extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//ȥ����Ϣ��
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������,����ʾӦ�ó�������
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        Handler handler = new Handler();
        handler.postDelayed(new loadhandler(), 3000);//�ӳ�3���,����ת����һ��Activity
    }
	
	
	//�ӳټ����߳�
    class loadhandler implements Runnable{
        public void run() {
            //startActivity(new Intent(getApplication(),MainActivity.class));
        	Intent intent = new Intent(StartPage.this,MainActivity.class);  
            StartPage.this.startActivity(intent); 
        	StartPage.this.finish();
        }
        
    }
}

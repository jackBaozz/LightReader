package com.lightreader.bzz.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;


public class StartPage extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏,不显示应用程序名字
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        Handler handler = new Handler();
        handler.postDelayed(new loadhandler(), 3000);//延迟3秒后,再跳转到另一个Activity
    }
	
	
	//延迟加载线程
    class loadhandler implements Runnable{
        public void run() {
            //startActivity(new Intent(getApplication(),MainActivity.class));
        	Intent intent = new Intent(StartPage.this,MainActivity.class);  
            StartPage.this.startActivity(intent); 
        	StartPage.this.finish();
        }
        
    }
}

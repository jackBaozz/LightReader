package com.lightreader.bzz.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;



public class SplashActivity extends Activity {
	private ImageView iv;
	
	//延迟跳转的activity
    class loadhandler implements Runnable{
        public void run() {
            //startActivity(new Intent(getApplication(),MainActivity.class));
        	Intent intent = new Intent(SplashActivity.this,BookMainActivity.class);
            SplashActivity.this.startActivity(intent); 
        	SplashActivity.this.finish();
        }
        
    }
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉程序名的title
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        
        Handler handler = new Handler();
        handler.postDelayed(new loadhandler(), 500);//延迟3秒后跳转到其他Activity
        
        
    }
	
	
	
}

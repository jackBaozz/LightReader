package com.lightreader.bzz.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lightreader.bzz.image.MyGifView;

public class MainActivity extends Activity {

	private TextView textView;
	private Button button;
	private ImageView ad_view;
	private MyGifView myGifView;
	private int count = 0;
	private boolean isExit; //˫���˳���ȫ�ֱ���
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//ȥ����Ϣ��
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������,����ʾӦ�ó�������
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_main);
		textView = (TextView) findViewById(R.id.textView);
		textView.setBackgroundColor(Color.BLUE);
		button = (Button) findViewById(R.id.btn1);
		//ad_view = (ImageView)findViewById(R.id.gif_mainLoad);
		//myGifView = new MyGifView(MainActivity.this,null,R.drawable.ad_main_load);

		ButtonListener buttonListener = new ButtonListener();
		button.setOnClickListener(buttonListener);
	}

	
	/**
	 * ������η��ؼ�,�˳�
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {  
            exit();  
            return false;  
        } else {  
            return super.onKeyDown(keyCode, event);  
        }
	}
	/**
	 * �˳�ǰ���ж�
	 */
	public void exit(){
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
            exitHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);//�˳�����
        }
    }
	
	Handler exitHandler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            super.handleMessage(msg);  
            isExit = false;  
        }  
    }; 
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onUserLeaveHint() {
		// TODO Auto-generated method stub
		super.onUserLeaveHint();
		System.out.println("����~~~");
	}

	/**
	 * ��ť������
	 * 
	 * @author baozhizhi
	 * 
	 */
	class ButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			count++;
			textView.setText("google".concat(count + ""));
		}

	}
}

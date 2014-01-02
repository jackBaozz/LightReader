package com.lightreader.bzz.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.lightreader.bzz.image.ImageUtils;
import com.lightreader.bzz.image.MyGifView;


@SuppressLint("NewApi")
public class MainActivity extends Activity {
	private LayoutInflater inflater;
	private TextView textView;
	private Button button;
	private ImageView ad_view;
	private MyGifView myGifView;
	private int count = 0;
	private boolean isExit; //点击是否退出
	private TabHost tabHost = null;
	
	private GridView mainGridLocalBooks = null; //本地书库的布局
	private String[] item = { "唐僧", "孙悟空 ", "猪八戒", "沙和尚" };//数据个数,本地书本个数
	private SimpleAdapter adapter;//书本适配器
	private BitmapRegionDecoder bitmapRegionDecoder;
    private Bitmap bookPlusBitmap;
    private int listItemsLength = 0;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉程序名的title
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_main);
		inflater = LayoutInflater.from(MainActivity.this);
		//textView = (TextView) findViewById(R.id.textView1);
		//textView.setBackgroundColor(Color.BLUE);
		
		//button = (Button) findViewById(R.id.btn1);
		//ad_view = (ImageView)findViewById(R.id.gif_mainLoad);
		//myGifView = new MyGifView(MainActivity.this,null,R.drawable.ad_main_load);

		ButtonListener buttonListener = new ButtonListener();
		//button.setOnClickListener(buttonListener);
		
		
		
		
		//初始化TabHost
		//以下三句代码，注意顺序
		tabHost = (TabHost)findViewById(android.R.id.tabhost);
		tabHost.setup();
		final TabWidget tabWidget = tabHost.getTabWidget();
		//自己添加TabSpec
		//TabHost.TabSpec tabSpec01 = tabHost.newTabSpec("one");
		//tabSpec01.setIndicator("个人信息", null);
		//Intent intent01 = new Intent(MyXiTuanTestActivity.this,MyInfoActivity.class); 意图
		//tabSpec01.setContent(intent01);
		//tabHost.addTab(tabSpec01);
		
		tabHost.addTab(tabHost.newTabSpec("1").setIndicator("本地书库").setContent(R.id.unhanlderLayout1));
		tabHost.addTab(tabHost.newTabSpec("2").setIndicator("在线书库").setContent(R.id.unhanlderLayout2));
		//tabHost.addTab(tabHost.newTabSpec("google2").setIndicator(null,getResources().getDrawable(android.R.drawable.ic_menu_mylocation)).setContent(R.id.unhanlderLayout2));
		tabHost.addTab(tabHost.newTabSpec("3").setIndicator("其他").setContent(R.id.unhanlderLayout3));
		tabHost.setCurrentTab(0);
		updateTab(tabHost);//初始化Tab的颜色，和字体的颜色 
		//TabHost注册点击标签事件
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				tabHost.setCurrentTabByTag(tabId);
				updateTab(tabHost);
			}
		});

		
		//初始化标----签1里面的布局
		mainGridLocalBooks = (GridView)findViewById(R.id.main_grid_localBooks);
		ArrayList<Bitmap> types = new ArrayList<Bitmap>();
		types.add(BitmapFactory.decodeResource(getResources(),R.drawable.bt_soundeffect_equallizerunit_bass_hl));
		types.add(BitmapFactory.decodeResource(getResources(),R.drawable.bt_soundeffect_equallizerunit_classical_hl));
		types.add(BitmapFactory.decodeResource(getResources(),R.drawable.bt_soundeffect_equallizerunit_dance_hl));
		types.add(BitmapFactory.decodeResource(getResources(),R.drawable.bt_soundeffect_equallizerunit_folk_hl));
		types.add(BitmapFactory.decodeResource(getResources(),R.drawable.bt_soundeffect_equallizerunit_highpitch_hl));
		types.add(BitmapFactory.decodeResource(getResources(),R.drawable.bt_soundeffect_equallizerunit_intelligence_hl));
		types.add(BitmapFactory.decodeResource(getResources(),R.drawable.bt_soundeffect_equallizerunit_jazz_hl));
		types.add(BitmapFactory.decodeResource(getResources(),R.drawable.bt_soundeffect_equallizerunit_pop_hl));
		types.add(BitmapFactory.decodeResource(getResources(),R.drawable.bt_soundeffect_equallizerunit_rock_hl));
		types.add(BitmapFactory.decodeResource(getResources(),R.drawable.bt_soundeffect_equallizerunit_voice_hl));
		//mainGridLocalBooks.setAdapter(new MyMain2GridAdapter(inflater,types,screenWidth,screenHeight));
		
		
		//显示本地书库那个 "+"号的书籍(添加新书)
		try {
		    InputStream is = getResources().openRawResource(R.drawable.big_image_1);//获取原始数据的大图
		    bitmapRegionDecoder = BitmapRegionDecoder.newInstance(is, true);
		    bookPlusBitmap = ImageUtils.getBitmapFromImageRegion(bitmapRegionDecoder, 4, 5, 4, 4);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		// 本地所有书籍---图片
	    ArrayList<HashMap<String, Object>> listItems = new ArrayList<HashMap<String, Object>>();
		// 将数组信息分别存入ArrayList中
		int length = item.length;
		for (int i = 0; i < length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			//map.put("image", item[i]);
			map.put("image", R.drawable.book_style);
			listItems.add(map);
		}
		HashMap<String, Object> mapPlus = new HashMap<String, Object>();
		mapPlus.put("image", bookPlusBitmap);
		listItems.add(mapPlus);//添加图书的那个图片,添加到图片队列末尾
		listItemsLength = listItems.size();//本地所有书籍---图片的个数
		
		// 设定一个适配器
		adapter = new SimpleAdapter(this, listItems, R.layout.books_item, new String[] { "image" }, new int[] { R.id.item_imageView});
		//adapter可以绑定Bitmap数据
		adapter.setViewBinder(new ViewBinder(){  
	        @Override 
			public boolean setViewValue(View view, Object data, String textRepresentation) {
				if ((view instanceof ImageView) & (data instanceof Bitmap)) {
					ImageView iv = (ImageView) view;
					Bitmap bm = (Bitmap) data;
					iv.setImageBitmap(bm);
					return true;
				}
				return false;
			}
	    });  
	    //this.setListAdapter(adapter);
		
		
		// 对GridView进行适配
		mainGridLocalBooks.setAdapter(adapter);
		// 设置GridView的监听器
		mainGridLocalBooks.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Log.v("System.out", "listItemsLength - 1 :"+ String.valueOf(listItemsLength - 1));
				Log.v("System.out", "position :" + String.valueOf(position));
				if(listItemsLength - 1 == position ){
					//点击了最后一张图片 "+"添加本地目录,跳转到另一个intent来选择本地文件
					Intent intent = new Intent(MainActivity.this,FileBrowserActivity.class);//  
					MainActivity.this.startActivity(intent);//启动另一个 Activity
				}else{
					String str = "这次妖精把" + item[position] + "抓住了!";
				}
			}
		});
		
		
		
		
	}

	
	/**
	 * 更新Tab标签的颜色，和字体的颜色
	 * 
	 * @param tabHost
	 */
	private void updateTab(final TabHost tabHost) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			View view = tabHost.getTabWidget().getChildAt(i);//获取到选中的TabWidget
			//设置高度
			//int height = 30;
			//view.getLayoutParams().height = height;
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
			tv.setTextSize(14);
			tv.setTypeface(Typeface.SERIF, 2); // 设置字体和风格
			if (tabHost.getCurrentTab() == i) {// 选中
				//设置选中的背景图片
				//view.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_shape));
				//设置选中的背景颜色
				//view.setBackgroundColor(Color.parseColor("#4281f4"));//Google蓝
				tv.setTextColor(this.getResources().getColorStateList(android.R.color.holo_blue_bright));
			} else {// 不选中
				//设置选中的背景颜色
				//view.setBackgroundColor(Color.parseColor("#E9E9E9"));//默认灰
				tv.setTextColor(this.getResources().getColorStateList(android.R.color.white));
			}
			
		}
	}
	
	
	/**
	 * 点击返回键监听事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {  
            exit();  
            return false; 
            
		} else if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {  
            return true; // 返回true就不会弹出默认的setting菜单  
        } else {  
            return super.onKeyDown(keyCode, event);  
        }
	}
	/**
	 * 退出程序判断方法
	 */
	public void exit(){
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再次点击退出程序", Toast.LENGTH_SHORT).show();
            exitHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            System.exit(0);//退出程序
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
		System.out.println("点击到了哟");
	}

	/**
	 * 按钮监听事件
	 * 
	 * @author baozhizhi
	 * 
	 */
	class ButtonListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			count++;
			//textView.setText("google".concat(count + ""));
		}

	}
}

package com.lightreader.bzz.Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.lightreader.bzz.Application.AllApplication;
import com.lightreader.bzz.file.FileUtil;
import com.lightreader.bzz.image.MyGifView;
import com.lightreader.bzz.pojo.Book;
import com.lightreader.bzz.sqlite.DatabaseServer;
import com.lightreader.bzz.utils.BeanTools;
import com.lightreader.bzz.utils.Constant;


@SuppressLint("NewApi")
public class MainActivity extends Activity {
	private static String  TAG = "MainActivity";
	private LayoutInflater inflater;
	private TextView textView;
	private Button button;
	private ImageView ad_view;
	private ImageView item_imageView;
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
    private ArrayList<HashMap<String, Object>> listItems = null;//总数据集
    
    
    //删除书本需要
    private CheckBox checkbox; //是否删除本地文件
    private DatabaseServer databaseServer = new DatabaseServer(MainActivity.this);//数据库操作类
    private PopupWindow $popupWindow ;//全局的popipWindow变量 
    private Timer mTimer = null;  
    private TimerTask mTimerTask = null;  
    private Handler mHandler = null;  
    
    
    
    
    //TabHost滑动需要
    private static final int SWIPE_MIN_DISTANCE = 120;  
    private static final int SWIPE_MAX_OFF_PATH = 250;  
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;  
    private GestureDetector gestureDetector;  
    View.OnTouchListener gestureListener;
    private int currentTabHostIndex = 0;  
    private int maxTabHostIndex = 3;
    
    
    
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉程序名的title
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
        
		inflater = LayoutInflater.from(MainActivity.this);
		//textView = (TextView) findViewById(R.id.textView1);
		//textView.setBackgroundColor(Color.BLUE);
		//item_imageView = (ImageView)findViewById(R.id.item_imageView);
		//item_imageView.setImageResource(R.drawable.book_add); 
		button = (Button) findViewById(R.id.btn1);
		//ad_view = (ImageView)findViewById(R.id.gif_mainLoad);
		//myGifView = new MyGifView(MainActivity.this,null,R.drawable.ad_main_load);
		
		
		ButtonListener buttonListener = new ButtonListener();
		button.setOnClickListener(buttonListener);
		
		
		initTabHost();//初始化TabHost

		
		gestureDetector = new GestureDetector(new MyGestureDetector());  
        gestureListener = new View.OnTouchListener() {  
            public boolean onTouch(View v, MotionEvent event) {  
                if (gestureDetector.onTouchEvent(event)) {  
                    return true;  
                }  
                return false;  
            }  
        }; 
        
		
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
		
		
		
		
		
		
		
		//获取得到Message传送来的值,来更新主UI线程的控件
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Constant.INT_BOOK_TIMER:
					setBackgrounTransparent(1.0f);// 背景设置为不透明
					
					stopTimer();// 关闭定时器
					break;
				default:
					break;
				}
			}
		};
		
		
		/*
		AlertDialog.Builder builder;
		AlertDialog alertDialog;
		Context mContext = MainActivity.this;
		//三种方法都可以.用自定义布局
		LayoutInflater inflater = getLayoutInflater();	//Activity.getLayoutInflater() or Window.getLayoutInflater().
//		LayoutInflater inflater = LayoutInflater.from(this);	//Obtains the LayoutInflater from the given context.
//		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);		
		View layout = inflater.inflate(R.layout.custom_dialog,null);
		
		TextView text = (TextView) layout.findViewById(R.id.text);//获取这个布局上的一个文本控件
		text.setText("Hello, This is a LayoutInflater Demo");
		ImageView image = (ImageView) layout.findViewById(R.id.image);//获取这个布局上的一个图像控件
		image.setImageResource(R.drawable.icon);
		
		builder = new AlertDialog.Builder(mContext);//传入Context创建一个AlertDialog.Builder
		builder.setView(layout);
		alertDialog = builder.create();//用AlertDialog.Builder创建一个alertDialog对象出来
		alertDialog.show();
		*/
		
		
		
		init();//初始化数据
	}

	
	
	// 左右滑动刚好页面也有滑动效果  
    class MyGestureDetector extends SimpleOnGestureListener {
        @Override  
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {   
            try {  
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)  
                    return false;  
                // right to left swipe  
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE  
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {  
                    Log.i("test", "right");
                    
                    if (currentTabHostIndex == maxTabHostIndex) {  
                    	currentTabHostIndex = 0;  
                    } else {  
                    	currentTabHostIndex++;  
                    }  
                    tabHost.setCurrentTab(currentTabHostIndex);  
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {  
                    Log.i("test", "left");  
                    if (currentTabHostIndex == 0) {  
                    	currentTabHostIndex = maxTabHostIndex;  
                    } else {  
                    	currentTabHostIndex--;
                    }  
                    tabHost.setCurrentTab(currentTabHostIndex);  
                }  
            } catch (Exception e) {
            	
            }  
            return false;  
        }  
    }  
	
	
	
	
	
	/**
	 * 初始化数据
	 */
	public void init(){
		// 本地所有书籍---图片
		// 注意: 图片说明,如果是PNG那种只有主图案,周围是透明的图片,那么图片周围就会有一层阴影,如果是有底色,那么就无阴影!
		listItems = new ArrayList<HashMap<String, Object>>();// 总数据集合

		// 查询数据库,看现在已经有几条数据了
		List<Book> booksList = AllApplication.getInstance().getBooks();
		// 将数组信息分别存入ArrayList中
		// int length = item.length;
		int length = booksList.size();
		for (int i = 0; i < length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			// map.put("image", item[i]);
			// map.put("image", R.drawable.book_add);
			int index = booksList.get(i).getName().lastIndexOf(".");
			String name = booksList.get(i).getName().substring(0, index);
			String type = booksList.get(i).getName().substring(index + 1);
			String imgPath = booksList.get(i).getIcon();
			Integer bookId = booksList.get(i).getId();
			String path = booksList.get(i).getPath();
			if (TextUtils.isEmpty(imgPath)) {
				map.put("image", R.drawable.book_1);
			} else {
				map.put("image", R.drawable.book_1);
			}
			map.put("book_name", name);// 书本名字
			map.put("book_type", type);// 书本的文件类型
			map.put("book_id", bookId);// 书本在数据库的ID
			map.put("book_path", path);// 书本在数据库的路径
			listItems.add(map);// [数据拼装第一部分]
		}

		HashMap<String, Object> mapPlus = new HashMap<String, Object>();
		// mapPlus.put("image", bookPlusBitmap);//之前的图片,大图切小图
		mapPlus.put("image", R.drawable.plus);// 封面图
		mapPlus.put("book_name", "");// 书本名字
		mapPlus.put("book_type", "");// 书本的文件类型
		mapPlus.put("book_id", 0);// 书本的id
		mapPlus.put("book_path", "");// 书本在数据库的路径
		listItems.add(mapPlus);// "添加图书"的那个图片,添加到图片队列末尾 [数据拼装第三部分]
		listItemsLength = listItems.size();// 本地所有书籍---图片的个数
		// 设定一个适配器
		adapter = new SimpleAdapter(this, listItems, R.layout.books_item_new, new String[] { "image", "book_name", "book_type", "book_id", "book_path" }, new int[] { R.id.item_imageView_new, R.id.book_name, R.id.book_type, R.id.book_id, R.id.book_path });
		// adapter可以绑定Bitmap数据
		adapter.setViewBinder(new ViewBinder() {
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

		// 对GridView进行适配
		mainGridLocalBooks.setAdapter(adapter);
		// 设置GridView的监听器 --- 单击事件
		mainGridLocalBooks.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				//Log.v("System.out", "MainActivity.listItemsLength - 1 :" + String.valueOf(listItemsLength - 1));
				//Log.v("System.out", "MainActivity.position :" + String.valueOf(position));
				if (listItemsLength - 1 == position) {
					// 点击了最后一张图片 "+"添加本地目录,跳转到另一个intent来选择本地文件
					Intent intent = new Intent(MainActivity.this, FileBrowserActivity.class);//
					MainActivity.this.startActivity(intent);// 启动另一个 Activity
				} else {
					//直接打开图书
					Toast.makeText(getApplicationContext(), "这次妖精把" + item[position] + "抓住了!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		// 设置GridView的监听器 --- 长按事件
		mainGridLocalBooks.setOnItemLongClickListener(new OnItemLongClickListener(){
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterview, View view, int position, long l) {
				if (listItemsLength - 1 == position) {
					// 点击了最后一张图片,不做任何操作
				} else {
					initPopWindow(view);//弹出对话框
				}
				return true;
			}
			
		});
	}

	/**
	 * 初始化TabHost
	 */
	private void initTabHost(){
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
		currentTabHostIndex = 0;//赋值给全局变量保存,给后续调用
		updateTab(tabHost);//初始化Tab的颜色，和字体的颜色 
		//TabHost注册点击标签事件
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				tabHost.setCurrentTabByTag(tabId);
				updateTab(tabHost);
			}
		});
		tabHost.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent motionevent) {
				if (gestureDetector.onTouchEvent(motionevent)) {  
					motionevent.setAction(MotionEvent.ACTION_CANCEL);  
		        } 
				return true;
			}
			
		});
		
		/*
		@Override  
	    public boolean dispatchTouchEvent(MotionEvent event) {  
	        if (gestureDetector.onTouchEvent(event)) {  
	            event.setAction(MotionEvent.ACTION_CANCEL);  
	        }  
	        return super.dispatchTouchEvent(event);  
	    }*/
		
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
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	/**
	 * activity恢复的时候继续调用
	 */
	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onUserLeaveHint() {
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
			//initPopWindow(parent);
			initPopWindow(v);
			count++;
			Toast.makeText(getApplicationContext(), "点击了["+count+"]次", Toast.LENGTH_SHORT).show();
		}

	}
	
	
	/**
	 * 设置主页面的背景是否为透明  灰色
	 * @param float f
	 */
	private void setBackgrounTransparent(float f){
		//背景设置为半透明
      	WindowManager.LayoutParams lp = getWindow().getAttributes();
      	//lp.alpha = 0.5f; //0.0-1.0
      	lp.alpha = f; //0.0-1.0
      	getWindow().setAttributes(lp);
	}
	
	
	
	
	/**
	 * 清楚背景为灰的定时器 ----- 开
	 */
	private void startTimer() {
		if (mTimer == null) {
			mTimer = new Timer();
		}

		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					//Log.i(TAG, "count: " + String.valueOf(count));
					float f = getWindow().getAttributes().alpha;//获取屏幕的属性alpha的值,灰度
					if (!$popupWindow.isShowing() && f == 0.5f && mHandler != null){
				            Message message = Message.obtain(mHandler, Constant.INT_BOOK_TIMER);     
				            mHandler.sendMessage(message);
					}
				}
			};
		}

		if (mTimer != null && mTimerTask != null){
			mTimer.schedule(mTimerTask, 500, 333);//循环执行TimerTask里面的run方法
		}
	}
  
	/**
	 * 清楚背景为灰的定时器 ----- 关
	 */
	private void stopTimer() {
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}
	}
	
	
	
	/**
	 * 点击书本,弹出删除的确认对话框
	 */
	private void initPopWindow(final View parent) {
		stopTimer();//首先清理遗留的定时器
		startTimer();//开始运行定时器
		setBackgrounTransparent(0.5f);//背景设置为半透明
		
		LayoutInflater inflater = getLayoutInflater();//布局充气筒
		ViewGroup popViewGroup = (ViewGroup)inflater.inflate(R.layout.activity_main,null,true);//根据充气筒获取指定layout的布局group
		View view = inflater.inflate(R.layout.activity_main, null);
		
        // 加载popupWindow的布局文件   
        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.book_popup, null);  
        // 设置popupWindow的背景颜色   
        //contentView.setBackgroundColor(color.white);
        checkbox = (CheckBox)contentView.findViewById(R.id.checkBox_local_id);//获取那个checkbox
		checkbox.setOnClickListener(new OnClickListener() {//添加点击监听器
			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					Toast.makeText(MainActivity.this, "Bro, try Android :)", Toast.LENGTH_LONG).show();
				}
			}
		});
        
        
        // 声明一个弹出框   
        //final PopupWindow popupWindow = new PopupWindow(findViewById(R.id.main_base_id), 1080, 350);
        int[] intArray = BeanTools.getDeviceWidthAndHeight(MainActivity.this);//获取设备的宽和高
        $popupWindow = new PopupWindow(contentView,intArray[0],intArray[1] / 2 ,true);
        // 为弹出框设定自定义的布局   
        $popupWindow.setContentView(contentView); 
        $popupWindow.setBackgroundDrawable(new BitmapDrawable());//设置一个空背景[必须]
        $popupWindow.showAtLocation(findViewById(R.id.main_base_id), Gravity.BOTTOM| Gravity.CENTER, 0, 0);
        //$popupWindow.showAsDropDown(parent,0,0);
        //$popupWindow.showAsDropDown(button);
        
        $popupWindow.setFocusable(true);
        $popupWindow.setTouchable(true);
        $popupWindow.setOutsideTouchable(true);
        
        contentView.setFocusable(true);//设置view能够接听事件，标注1
        contentView.setFocusableInTouchMode(true); //设置view能够接听事件 标注2
        //监听键盘的返回键
        contentView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if ($popupWindow != null) {
						$popupWindow.dismiss();
						
						setBackgrounTransparent(1.0f);// 背景设置为不透明
					}
				}
				return false;
			}
		});
        //监听点击其他区域
        contentView.setOnTouchListener(new OnTouchListener(){
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if ($popupWindow != null && $popupWindow.isShowing()) {
					$popupWindow.dismiss();
					
					setBackgrounTransparent(1.0f);//背景设置为不透明
				}
				return true;
			}
        });
        
        
        
        final EditText editText = (EditText) contentView.findViewById(R.id.editText1);  
        // 设定当你点击editText时，弹出的输入框是啥样子的。这里设置默认为数字输入哦，这时候你会发现你输入非数字的东西是不行的哦   
        editText.setInputType(InputType.TYPE_CLASS_NUMBER); 
        
        Button buttonSure = (Button) contentView.findViewById(R.id.button1_sure);
        buttonSure.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {
            	$popupWindow.dismiss();
            	
                setBackgrounTransparent(1.0f);//背景设置为不透明
                
                boolean flag = checkbox.isChecked();//是否是checked状态
                if(flag){
                	//删除本地文件,刷新本地数据库的那个字段
                	TextView clickItemBookPath = (TextView)parent.findViewById(R.id.book_path);//根据Layout获取这个隐藏的路径控件
                	TextView clickItemBookName = (TextView)parent.findViewById(R.id.book_name);//根据Layout获取这个隐藏的路径控件
    				String path = clickItemBookPath.getText().toString().trim();
                	String name = clickItemBookName.getText().toString().trim();
                	
                	//File file = new File(path, name);//当前选中的文件
                	File file = new File(path);//当前选中的文件
                	FileUtil.deleteFile(file);//先删除它
                	
                	boolean bookShowOperation = databaseServer.updateBook(path, 0);//更新该条数据为"下架"
    				if(!bookShowOperation){//更新失败
    					Log.e("程序逻辑错误", "更新上架书本"+path+"失败");
    				}
    				init();//初始化最新数据
    				
                }else{
                	//只刷新本地数据库的那个字段
                	TextView clickItemBookPath = (TextView)parent.findViewById(R.id.book_path);//根据Layout获取这个隐藏的路径控件
    				String path = clickItemBookPath.getText().toString().trim();
                	
                	boolean bookShowOperation = databaseServer.updateBook(path, 0);//更新该条数据为"下架"
    				if(!bookShowOperation){//更新失败
    					Log.e("程序逻辑错误", "更新上架书本"+path+"失败");
    				}
    				init();//初始化最新数据
                }
            }  
        });  
          
        Button buttonCancel = (Button) contentView.findViewById(R.id.button2_cancel);  
        buttonCancel.setOnClickListener(new OnClickListener() {  
            @Override  
            public void onClick(View v) {  
            	$popupWindow.dismiss();
            	
                setBackgrounTransparent(1.0f);//背景设置为不透明
            }  
        });  
          
    }  
	
}

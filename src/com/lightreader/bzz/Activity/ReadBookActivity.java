package com.lightreader.bzz.Activity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lightreader.bzz.Application.AllApplication;
import com.lightreader.bzz.Dialog.MarkDialog;
import com.lightreader.bzz.Entity.BookPageFactory;
import com.lightreader.bzz.Pojo.Mark;
import com.lightreader.bzz.Sqlite.MarkHelper;
import com.lightreader.bzz.Utils.BeanTools;
import com.lightreader.bzz.Utils.Constant;
import com.lightreader.bzz.View.PageWidget;

@SuppressLint("WrongCall")
public class ReadBookActivity extends Activity implements OnClickListener, OnSeekBarChangeListener{
	private static final String TAG = "ReadBookActivity";
	private BookPageFactory pagefactory;// 书工厂
	private int screenWidth;// 宽
	private int screenHeight;//高
	private int readHeight; // 电子书显示高度

	private static int begin = 0;// 记录的书籍开始位置
	private static String word = "";// 记录当前页面的文字
	private int a = 0, b = 0;// 记录toolpop的位置
	private TextView bookBtn1, bookBtn2, bookBtn3, bookBtn4;// 按钮
	private TextView markEdit4;// 文字
	private ImageButton imageBtn2, imageBtn3_1, imageBtn3_2;// 图像按钮
	private ImageButton imageBtn4_1, imageBtn4_2;// 图像按钮
	private String bookPath;// 记录读入书的路径
	private String ccc = null;// 记录是否为快捷方式调用
	private SharedPreferences.Editor editor; // 读取SharedPreferences信息的编辑器
	private SharedPreferences sharedPreferences;// SharedPreferences
	private Boolean isNight; // 亮度模式,白天和晚上
	protected int jumpPage;// 记录跳转进度条
	private int light; // 亮度值
	private WindowManager.LayoutParams layoutParams;// 布局管理
	private MarkHelper markhelper;// 操作书签的数据库
	private Bitmap mCurPageBitmap, mNextPageBitmap;// 位图
	private MarkDialog mDialog = null;
	private Context mContext = ReadBookActivity.this;// 上下文
	private PageWidget mPageWidget;//控制画出贝塞尔曲线
	private PopupWindow mPopupWindow, mToolpop, mToolpop1, mToolpop2, mToolpop3, mToolpop4;// 弹出窗口
	private Boolean popwindowIsShow = false;// popwindow是否显示
	private View popupWindwowView, toolpopView, toolpopView1, toolpopView2, toolpopView3, toolpopView4;
	private SeekBar seekBar1, seekBar2, seekBar4;// 进度条
	private int size = 30; // 字体大小
	private int defaultSize = 0;// 默认字体大小
	protected long count = 1;
	protected int PAGE = 1;// 第几页
	private String txtName, txtName1;// 文字
	public static Canvas mCurPageCanvas, mNextPageCanvas;// 画布

	
	//activity底部的显示信息的控件
	public static TextView textViewBattery;
	public static TextView textViewLeft ;
	public static TextView textViewCenter ;
	public static TextView textViewRight ;
	
	//定时器
	private Timer mTimer = null;  
    private TimerTask mTimerTask = null;
	
	
	
	// 实例化Handler
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		// 接收子线程发来的消息，同时更新UI
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				begin = msg.arg1;
				pagefactory.setM_mbBufBegin(begin);
				pagefactory.setM_mbBufEnd(begin);
				postInvalidateUI();
				break;
			case 1:
				pagefactory.setM_mbBufBegin(begin);
				pagefactory.setM_mbBufEnd(begin);
				postInvalidateUI();
				break;
			case Constant.INT_BOOK_UPDATE_TOTALPAGE:
				float fPercent = (float) (pagefactory.getM_mbBufBegin() * 1.0 / pagefactory.getM_mbBufLength());
				int a = (int)Math.floor(fPercent * Float.valueOf((String)msg.obj) + 0.5f);
				if(a<=0)a=1;
				String b = (String)msg.obj;
				textViewCenter.setText(a+"/"+b);
				break;
			default:
				break;
			}
		}
	};
	
	//专门用来更新时间的handler
	Handler timeHandler = new Handler() {
		// 接收子线程发来的消息，同时更新UI
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.INT_BOOK_TIMER:  //定时器的线程发过来的消息
				String timeText = BeanTools.getSystemCurrentTime(AllApplication.getInstance());//获取当前系统时间
				textViewBattery.setText(textViewBattery.getText());
				textViewLeft.setText(timeText);
				textViewCenter.setText(textViewCenter.getText());
				textViewRight.setText(textViewRight.getText());
				break;
			default:
				break;
			}
		}
	};
	
	
	
	//创建一个广播接受对象    接受广播,更新手机电池的电量信息以及时间信息
    BroadcastReceiver batteryChangedReceiver = new BroadcastReceiver() {
    	@Override
		public void onReceive(Context context, Intent intent) {
			if(Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
				int level = intent.getIntExtra("level", 0);
				int scale = intent.getIntExtra("scale", 100);
				//tvBatteryChanged.setText("电池电量：" + (level * 100 / scale) + "%");
				textViewBattery.setText("P:"+(level * 100 / scale) + "%");
				//pagefactory.onDraw(mCurPageCanvas);
			}
		}
    };
	
	
    
    class TotlePageRunnable implements Runnable {
		@Override
		public void run() {
			String totlePage = pagefactory.getTotalPagesCount()+"";
			Message msg = Message.obtain();
			msg.what = Constant.INT_BOOK_UPDATE_TOTALPAGE;
			msg.obj = totlePage;//需要传送的数据
			mHandler.sendMessage(msg);
		}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.book_read);//[必须]
		//mContext = getBaseContext();
		
		startTimer();//更新时间数据的线程
		textViewBattery = (TextView)findViewById(R.id.book_read_bottom_textview1_id);
		textViewLeft = (TextView)findViewById(R.id.book_read_bottom_textview2_id);
		textViewCenter = (TextView)findViewById(R.id.book_read_bottom_textview3_id);
		textViewRight = (TextView)findViewById(R.id.book_read_bottom_textview4_id);
		
		
		

		
		screenWidth = BeanTools.getDeviceWidthAndHeight(mContext)[0];//设备的显示宽度
		screenHeight = BeanTools.getDeviceWidthAndHeight(mContext)[1];//设备的显示高度
		defaultSize = (screenWidth * 20) / 320;//默认屏幕的
		//readHeight = screenHeight - (50 * screenWidth) / 320;//阅读的高度,去掉广告
		readHeight = screenHeight;
		

		mCurPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);
		mPageWidget = new PageWidget(this, screenWidth, readHeight);// 画出贝塞尔曲线的页面
		
		
		RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.book_read_top_id);//贝塞尔曲线效果附加到 book_read.xml布局上
		rlayout.addView(mPageWidget);
		

		Intent intent = getIntent();
		bookPath = intent.getStringExtra("txtName1");
		ccc = intent.getStringExtra("ccc");

		
		// 获取传递过来的书的绝对路径
		Intent _intent = getIntent();
		String book_path = _intent.getExtras().get("book_path").toString();// 获取书籍的路径
		bookPath = book_path;
		
		
		
		
		
		
		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
		//翻页触发方法的监听器
		mPageWidget.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent e) {
				boolean ret = false;
				if (view == mPageWidget) {
					if (!popwindowIsShow) {
						if (e.getAction() == MotionEvent.ACTION_DOWN) {
							if (e.getY() > readHeight) {// 超出范围了，表示单击到广告条，则不做翻页
								return false;
							}
							mPageWidget.abortAnimation();//取消动画
							mPageWidget.calcCornerXY(e.getX(), e.getY());
							pagefactory.onDraw(mCurPageCanvas);
							if (mPageWidget.DragToRight()) {// 左翻
								try {
									pagefactory.prePage();
									begin = pagefactory.getM_mbBufBegin();// 获取当前阅读位置
									word = pagefactory.getFirstLineText();// 获取当前阅读位置的首行文字
								} catch (IOException e1) {
									Log.e(TAG, "onTouch->prePage error", e1);
								}
								if (pagefactory.isfirstPage()) {
									Toast.makeText(mContext, "当前是第一页", Toast.LENGTH_SHORT).show();
									return false;
								}
								pagefactory.onDraw(mNextPageCanvas);
							} else {// 右翻
								try {
									pagefactory.nextPage();
									begin = pagefactory.getM_mbBufBegin();// 获取当前阅读位置
									word = pagefactory.getFirstLineText();// 获取当前阅读位置的首行文字
								} catch (IOException e1) {
									Log.e(TAG, "onTouch->nextPage error", e1);
								}
								if (pagefactory.islastPage()) {
									Toast.makeText(mContext, "已经是最后一页了", Toast.LENGTH_SHORT).show();
									return false;
								}
								pagefactory.onDraw(mNextPageCanvas);
							}
							mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
						}
						editor.putInt(bookPath + "begin", begin).commit();
						ret = mPageWidget.doTouchEvent(e);
						return ret;
					}
				}
				return false;
			}
		});

		initPop();//初始化弹出的菜单栏
		
		
		// 提取记录在sharedpreferences的各种状态
		sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);//创建文件为 config.xml的SharedPreferences
		editor = sharedPreferences.edit();
		getSize();// 获取配置文件中的size大小
		getLight();// 获取配置文件中的light值
		count = sharedPreferences.getLong(bookPath + "count", 1);

		layoutParams = getWindow().getAttributes();
		layoutParams.screenBrightness = light / 10.0f < 0.01f ? 0.01f : light / 10.0f;
		getWindow().setAttributes(layoutParams);//设置显示屏的亮度
		pagefactory = new BookPageFactory(screenWidth, readHeight);// 书工厂
		if (isNight) {
			Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.main_bg);//获取源位图
			bmp = Bitmap.createScaledBitmap(bmp, screenWidth, screenHeight, true);//图片缩放到全屏大小
			pagefactory.setBgBitmap(bmp);
			pagefactory.setM_textColor(Color.rgb(128, 128, 128));
		} else {
			Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);//获取源位图
			bmp = Bitmap.createScaledBitmap(bmp, screenWidth, screenHeight, true);//图片缩放到全屏大小
			pagefactory.setBgBitmap(bmp);
			pagefactory.setM_textColor(Color.rgb(28, 28, 28));
		}
		begin = sharedPreferences.getInt(bookPath + "begin", 0);
		try {
			Intent intent2 = getIntent();
			txtName = intent2.getStringExtra("txtName1");
			// String strFilePath=Finaltxt.TXTPA+txtName;
			// pagefactory.openbook(strFilePath, begin);
			
			
			pagefactory.openbook(bookPath, begin);
			
			//pagefactory.openbook("mnt/sdcard/" + txtName, begin);
			// Intent intent3=getIntent();
			// txtName1=intent3.getStringExtra("txtName2");
			// String strFilePath=Finaltxt.TXTPA+txtName;
			// pagefactory.opennerbook(strFilePath);

			// pagefactory.openbook(bookPath, begin);// 从指定位置打开书籍，默认从开始打开
			//TODO
			pagefactory.setM_fontSize(size);
			pagefactory.onDraw(mCurPageCanvas);
			new Thread(new TotlePageRunnable()).start();//创建Thread线程
		} catch (Exception e1) {
			Log.e(TAG, "打开电子书失败", e1);
			Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
		}
		markhelper = new MarkHelper(this);

		
		//注册一个接受广播类型,获取手机电池的电量信息
		//可以用于刷新页面的 电池信息和时间信息
        registerReceiver(batteryChangedReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	}
	
	
	
	

	
	
	
	
	/**
	 * 记录数据 并清空popupwindow
	 */
	private void clear() {
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		popwindowIsShow = false;
		mPopupWindow.dismiss();
		popDismiss();
	}

	/**
	 * 读取配置文件中亮度值
	 */
	private void getLight() {
		light = sharedPreferences.getInt("light", 5);
		isNight = sharedPreferences.getBoolean("night", false);
	}

	/**
	 * 读取配置文件中字体大小
	 */
	private void getSize() {
		size = sharedPreferences.getInt("size", defaultSize);
	}
	

	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		pagefactory = null;
		mPageWidget = null;
		finish();
	}

	
    ////////////////////////////////////////////////////////////监听方法/////////////////////////////////////////////////////////////////////
	/**
	 * 添加对menu按钮的监听
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (popwindowIsShow) {
				getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				popwindowIsShow = false;
				mPopupWindow.dismiss();
				popDismiss();

			} else {
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
				popwindowIsShow = true;
				pop();
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * OnClickListener监听器需要的方法
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 字体按钮
		case R.id.bookBtn1:
			a = 1;
			setToolPop(a);
			break;
		// 亮度按钮
		case R.id.bookBtn2:
			a = 2;
			setToolPop(a);
			break;
		// 书签按钮
		case R.id.bookBtn3:
			a = 3;
			setToolPop(a);
			break;
		// 跳转按钮
		case R.id.bookBtn4:
			a = 4;
			setToolPop(a);
			break;

		// 夜间模式按钮
		case R.id.imageBtn2:
			if (isNight) {
				pagefactory.setM_textColor(Color.rgb(28, 28, 28));
				imageBtn2.setImageResource(R.drawable.reader_switch_off);
				isNight = false;
				Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);//获取源位图
				bmp = Bitmap.createScaledBitmap(bmp, screenWidth, screenHeight, true);//图片缩放到全屏大小
				pagefactory.setBgBitmap(bmp);
			} else {
				pagefactory.setM_textColor(Color.rgb(128, 128, 128));
				imageBtn2.setImageResource(R.drawable.reader_switch_on);
				isNight = true;
				Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.main_bg);//获取源位图
				bmp = Bitmap.createScaledBitmap(bmp, screenWidth, screenHeight, true);//图片缩放到全屏大小
				pagefactory.setBgBitmap(bmp);
			}
			setLight();
			pagefactory.setM_mbBufBegin(begin);
			pagefactory.setM_mbBufEnd(begin);
			postInvalidateUI();
			break;
		// 添加书签按钮
		case R.id.imageBtn3_1:
			SQLiteDatabase db = markhelper.getWritableDatabase();
			try {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm ss");
				String time = sf.format(new Date());
				db.execSQL("insert into markhelper (path ,begin,word,time) values (?,?,?,?)", new String[] { bookPath, begin + "", word, time });
				db.close();
				Toast.makeText(this, "书签添加成功", Toast.LENGTH_SHORT).show();
			} catch (SQLException e) {
				Toast.makeText(this, "该书签已存在", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(this, "添加书签失败", Toast.LENGTH_SHORT).show();
			}
			mToolpop.dismiss();
			mToolpop3.dismiss();
			break;
		// 我的书签按钮
		case R.id.imageBtn3_2:
			SQLiteDatabase dbSelect = markhelper.getReadableDatabase();
			String col[] = { "begin", "word", "time" };
			Cursor cur = dbSelect.query("markhelper", col, "path = '" + bookPath + "'", null, null, null, null);
			Integer num = cur.getCount();
			if (num == 0) {
				Toast.makeText(this, "您还没有书签", Toast.LENGTH_SHORT).show();
			} else {
				ArrayList<Mark> markList = new ArrayList<Mark>();
				while (cur.moveToNext()) {
					String s1 = cur.getString(cur.getColumnIndex("word"));
					String s2 = cur.getString(cur.getColumnIndex("time"));
					int b1 = cur.getInt(cur.getColumnIndex("begin"));
					int p = 0;
					int count = 10;
					Mark mv = new Mark(s1, p, count, b1, s2, bookPath);
					markList.add(mv);
				}
				mDialog = new MarkDialog(this, markList, mHandler, R.style.FullHeightDialog);
				mDialog.setCancelable(false);
				mDialog.setTitle("我的书签");
				mDialog.show();
			}
			dbSelect.close();
			cur.close();
			mToolpop.dismiss();
			mToolpop3.dismiss();
			break;
		case R.id.imageBtn4_1:
			clear();
			pagefactory.setM_mbBufBegin(begin);
			pagefactory.setM_mbBufEnd(begin);
			postInvalidateUI();
			break;
		case R.id.imageBtn4_2:
			clear();
			break;
		}
	}
	
	
	
	/**
	 * OnSeekBarChangeListener监听器需要的方法
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
		// 字体进度条
		case R.id.seekBar1:
			size = seekBar1.getProgress() + 15;
			//TODO
			pagefactory.setM_fontSize(size);
			new Thread(new TotlePageRunnable()).start();//创建Thread线程
			pagefactory.setM_mbBufBegin(begin);
			pagefactory.setM_mbBufEnd(begin);
			postInvalidateUI();
			break;
		// 亮度进度条
		case R.id.seekBar2:
			light = seekBar2.getProgress();
			setLight();
			layoutParams.screenBrightness = light / 10.0f < 0.01f ? 0.01f : light / 10.0f;
			getWindow().setAttributes(layoutParams);
			break;
		// 跳转进度条
		case R.id.seekBar4:
			int s = seekBar4.getProgress();
			markEdit4.setText(s + "%");
			begin = (pagefactory.getM_mbBufLength() * s) / 100;
			editor.putInt(bookPath + "begin", begin).commit();
			pagefactory.setM_mbBufBegin(begin);
			pagefactory.setM_mbBufEnd(begin);
			try {
				if (s == 100) {
					pagefactory.prePage();
					pagefactory.getM_mbBufBegin();
					begin = pagefactory.getM_mbBufEnd();
					pagefactory.setM_mbBufBegin(begin);
					pagefactory.setM_mbBufBegin(begin);
				}
			} catch (IOException e) {
				Log.e(TAG, "onProgressChanged seekBar4-> IOException error", e);
			}
			postInvalidateUI();
			break;
		}
	}

	/**
	 * OnSeekBarChangeListener监听器需要的方法
	 */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	/**
	 * OnSeekBarChangeListener监听器需要的方法
	 */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	
	////////////////////////////////////////////////////////////监听方法/////////////////////////////////////////////////////////////////////
	
	
	
	
	
	/**
	 * 判断是从哪个界面进入的READ
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (ccc == null) {
				if (popwindowIsShow) {// 如果popwindow正在显示
					popDismiss();
					getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
					popwindowIsShow = false;
					mPopupWindow.dismiss();
				} else {
					this.finish();
				}
			} else {
				if (!ccc.equals("ccc")) {
					if (popwindowIsShow) {// 如果popwindow正在显示
						getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
						popwindowIsShow = false;
						mPopupWindow.dismiss();
						popDismiss();
					} else {
						this.finish();
					}
				} else {
					this.finish();
				}
			}
		}
		return true;
	}
	
	/**
	 * popupwindow的弹出,工具栏
	 */
	public void pop() {
		mPopupWindow.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, 0);
		bookBtn1 = (TextView) popupWindwowView.findViewById(R.id.bookBtn1);
		bookBtn2 = (TextView) popupWindwowView.findViewById(R.id.bookBtn2);
		bookBtn3 = (TextView) popupWindwowView.findViewById(R.id.bookBtn3);
		bookBtn4 = (TextView) popupWindwowView.findViewById(R.id.bookBtn4);
		bookBtn1.setOnClickListener(this);
		bookBtn2.setOnClickListener(this);
		bookBtn3.setOnClickListener(this);
		bookBtn4.setOnClickListener(this);
	}

	/**
	 * 关闭55个弹出pop
	 */
	public void popDismiss() {
		mToolpop.dismiss();
		mToolpop1.dismiss();
		mToolpop2.dismiss();
		mToolpop3.dismiss();
		mToolpop4.dismiss();
	}

	/**
	 * 记录配置文件中亮度值和横竖屏
	 */
	private void setLight() {
		try {
			light = seekBar2.getProgress();
			editor.putInt("light", light);
			if (isNight) {
				editor.putBoolean("night", true);
			} else {
				editor.putBoolean("night", false);
			}
			editor.commit();
		} catch (Exception e) {
			Log.e(TAG, "setLight-> Exception error", e);
		}
	}

	/**
	 * 初始化所有POPUPWINDOW
	 */
	@SuppressLint("InflateParams")
	private void initPop() {
		//TODO 初始化弹出的popupwindow
		popupWindwowView = this.getLayoutInflater().inflate(R.layout.book_pop, null);//显示的弹出窗口
		//mPopupWindow = new PopupWindow(popupWindwowView, LayoutParams.MATCH_PARENT, BeanTools.getDeviceWidthAndHeight(mContext)[1] / 10 * 1);
		mPopupWindow = new PopupWindow(popupWindwowView, LayoutParams.MATCH_PARENT, BeanTools.dip2px(mContext, Float.parseFloat("90")));
		toolpopView = this.getLayoutInflater().inflate(R.layout.book_toolpop, null);//显示的图和文字
		mToolpop = new PopupWindow(toolpopView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		toolpopView1 = this.getLayoutInflater().inflate(R.layout.book_tool11, null);
		mToolpop1 = new PopupWindow(toolpopView1, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		toolpopView2 = this.getLayoutInflater().inflate(R.layout.book_tool22, null);
		mToolpop2 = new PopupWindow(toolpopView2, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		toolpopView3 = this.getLayoutInflater().inflate(R.layout.book_tool33, null);
		mToolpop3 = new PopupWindow(toolpopView3, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		toolpopView4 = this.getLayoutInflater().inflate(R.layout.book_tool44, null);
		mToolpop4 = new PopupWindow(toolpopView4, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 记录配置文件中字体大小
	 */
	private void setSize() {
		try {
			size = seekBar1.getProgress() + 15;
			editor.putInt("size", size);
			editor.commit();
		} catch (Exception e) {
			Log.e(TAG, "setSize-> Exception error", e);
		}
	}

	/**
	 * 设置popupwindow的显示与隐藏
	 * @param a
	 */
	public void setToolPop(int a) {
		if (a == b && a != 0) {
			if (mToolpop.isShowing()) {
				popDismiss();
			} else {
				mToolpop.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
				// 当点击字体按钮
				if (a == 1) {
					mToolpop1.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
					seekBar1 = (SeekBar) toolpopView1.findViewById(R.id.seekBar1);
					size = sharedPreferences.getInt("size", 20);
					seekBar1.setProgress((size - 15));
					seekBar1.setOnSeekBarChangeListener(this);
				}
				// 当点击亮度按钮
				if (a == 2) {
					mToolpop2.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
					seekBar2 = (SeekBar) toolpopView2.findViewById(R.id.seekBar2);
					imageBtn2 = (ImageButton) toolpopView2.findViewById(R.id.imageBtn2);
					getLight();

					seekBar2.setProgress(light);
					if (isNight) {
						imageBtn2.setImageResource(R.drawable.reader_switch_on);
					} else {
						imageBtn2.setImageResource(R.drawable.reader_switch_off);
					}
					imageBtn2.setOnClickListener(this);
					seekBar2.setOnSeekBarChangeListener(this);
				}
				// 当点击书签按钮
				if (a == 3) {
					mToolpop3.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, toolpopView.getHeight());
					imageBtn3_1 = (ImageButton) toolpopView3.findViewById(R.id.imageBtn3_1);
					imageBtn3_2 = (ImageButton) toolpopView3.findViewById(R.id.imageBtn3_2);
					imageBtn3_1.setOnClickListener(this);
					imageBtn3_2.setOnClickListener(this);
				}
				// 当点击跳转按钮
				if (a == 4) {
					mToolpop4.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
					imageBtn4_1 = (ImageButton) toolpopView4.findViewById(R.id.imageBtn4_1);
					imageBtn4_2 = (ImageButton) toolpopView4.findViewById(R.id.imageBtn4_2);
					seekBar4 = (SeekBar) toolpopView4.findViewById(R.id.seekBar4);
					markEdit4 = (TextView) toolpopView4.findViewById(R.id.markEdit4);
					// begin = sp.getInt(bookPath + "begin", 1);
					float fPercent = (float) (begin * 1.0 / pagefactory.getM_mbBufLength());
					DecimalFormat df = new DecimalFormat("#0");
					String strPercent = df.format(fPercent * 100) + "%";
					markEdit4.setText(strPercent);
					seekBar4.setProgress(Integer.parseInt(df.format(fPercent * 100)));
					seekBar4.setOnSeekBarChangeListener(this);
					imageBtn4_1.setOnClickListener(this);
					imageBtn4_2.setOnClickListener(this);
				}
			}
		} else {
			if (mToolpop.isShowing()) {
				// 对数据的记录
				popDismiss();
			}
			mToolpop.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
			// 点击字体按钮
			if (a == 1) {
				mToolpop1.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
				seekBar1 = (SeekBar) toolpopView1.findViewById(R.id.seekBar1);
				size = sharedPreferences.getInt("size", 20);
				seekBar1.setProgress(size - 15);
				seekBar1.setOnSeekBarChangeListener(this);
			}
			// 点击亮度按钮
			if (a == 2) {
				mToolpop2.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
				seekBar2 = (SeekBar) toolpopView2.findViewById(R.id.seekBar2);
				imageBtn2 = (ImageButton) toolpopView2.findViewById(R.id.imageBtn2);
				getLight();
				seekBar2.setProgress(light);

				if (isNight) {
					Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.main_bg);//获取源位图
					bmp = Bitmap.createScaledBitmap(bmp, screenWidth, screenHeight, true);//图片缩放到全屏大小
					pagefactory.setBgBitmap(bmp);
				} else {
					Bitmap bmp = BitmapFactory.decodeResource(this.getResources(), R.drawable.bg);//获取源位图
					bmp = Bitmap.createScaledBitmap(bmp, screenWidth, screenHeight, true);//图片缩放到全屏大小
					pagefactory.setBgBitmap(bmp);
				}

				if (isNight) {
					imageBtn2.setImageResource(R.drawable.reader_switch_on);
				} else {
					imageBtn2.setImageResource(R.drawable.reader_switch_off);
				}
				imageBtn2.setOnClickListener(this);
				seekBar2.setOnSeekBarChangeListener(this);
			}
			// 点击书签按钮
			if (a == 3) {
				mToolpop3.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
				imageBtn3_1 = (ImageButton) toolpopView3.findViewById(R.id.imageBtn3_1);
				imageBtn3_2 = (ImageButton) toolpopView3.findViewById(R.id.imageBtn3_2);
				imageBtn3_1.setOnClickListener(this);
				imageBtn3_2.setOnClickListener(this);
			}
			// 点击跳转按钮
			if (a == 4) {
				mToolpop4.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
				imageBtn4_1 = (ImageButton) toolpopView4.findViewById(R.id.imageBtn4_1);
				imageBtn4_2 = (ImageButton) toolpopView4.findViewById(R.id.imageBtn4_2);
				seekBar4 = (SeekBar) toolpopView4.findViewById(R.id.seekBar4);
				markEdit4 = (TextView) toolpopView4.findViewById(R.id.markEdit4);
				// jumpPage = sp.getInt(bookPath + "jumpPage", 1);
				float fPercent = (float) (begin * 1.0 / pagefactory.getM_mbBufLength());
				DecimalFormat df = new DecimalFormat("#0");
				String strPercent = df.format(fPercent * 100) + "%";
				markEdit4.setText(strPercent);
				seekBar4.setProgress(Integer.parseInt(df.format(fPercent * 100)));
				seekBar4.setOnSeekBarChangeListener(this);
				imageBtn4_1.setOnClickListener(this);
				imageBtn4_2.setOnClickListener(this);
			}
		}
		// 记录上次点击的是哪一个
		b = a;
	}


	/**
	 * 刷新界面
	 */
	public void postInvalidateUI() {
		mPageWidget.abortAnimation();
		pagefactory.onDraw(mCurPageCanvas);
		try {
			pagefactory.currentPage();
			begin = pagefactory.getM_mbBufBegin();// 获取当前阅读位置
			word = pagefactory.getFirstLineText();// 获取当前阅读位置的首行文字
		} catch (IOException e1) {
			Log.e(TAG, "postInvalidateUI->IOException error", e1);
		}

		pagefactory.onDraw(mNextPageCanvas);
		mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
		mPageWidget.postInvalidate();
	}

	
	
	
	
	
	
	
	
	/**
	 * 定时器获取时间 ----- 开
	 */
	private void startTimer() {
		if (mTimer == null) {
			mTimer = new Timer();
		}

		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
				    	Message message = Message.obtain(timeHandler, Constant.INT_BOOK_TIMER);
				    	timeHandler.sendMessage(message);
				}
			};
		}

		if (mTimer != null && mTimerTask != null){
			mTimer.schedule(mTimerTask, 500, 30000);//循环执行TimerTask里面的run方法   [60秒钟执行一次]
		}
	}
  
	/**
	 * 定时器获取时间 ----- 关
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
	
	
}

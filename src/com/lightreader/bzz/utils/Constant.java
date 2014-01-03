package com.lightreader.bzz.utils;

import com.lightreader.bzz.Activity.R;

public class Constant {
	//临时,以后删除
	public static int[] slideImages = { R.drawable.eq_custom, R.drawable.eq_dance, R.drawable.eq_pop, R.drawable.eq_rock, R.drawable.eq_treble };
	public static int[] slideTitles = { R.string.a, R.string.b, R.string.c, R.string.d, R.string.e };
	
	public static final String MAIM_ALL_TEXT = "所有";
	public static final String MAIM_LIKE_TEXT = "喜欢";
	public static final String MAIM_DONWLOAD_TEXT = "下载";
	public static final String MAIM_SETTING_TEXT = "设置";
	public static final String MAIM_SEARCH_TEXT = "查找";
	public static final String MAIM_EXIT_TEXT = "退出";
	public static final int MENU_CRETEDIR = 1;//新建目录
	public static final int MENU_EXIT = 2;//退出程序
	public static final int MENU_COPY = 3;//复制
	public static final int MENU_DELETE = 4;//删除
	public static final int MENU_PASTE = 5;//粘贴
	public static final int MENU_READ = 6;//读取
	public static final int MENU_FRESH = 7;//刷新
	public static final int MENU_BACK = 8;//回到上一层
	public static final int MENU_BACKHOME = 9;//回到主目录
	
	
	//阅读器支持解码的后缀名文件类型
	public static final String[] BOOK_SUFFIX = {".txt",".epub",".pdf"};
	//默认访问SD卡的路径
	public static final String  DEFAULT_SDCARD_PATH = "/mnt/sdcard";
	
}

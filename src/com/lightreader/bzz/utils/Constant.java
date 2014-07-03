package com.lightreader.bzz.utils;

import com.lightreader.bzz.Activity.R;

public interface Constant {
	//临时,以后删除
	public static int[] slideImages = { R.drawable.eq_custom, R.drawable.eq_dance, R.drawable.eq_pop, R.drawable.eq_rock, R.drawable.eq_treble };
	public static int[] slideTitles = { R.string.a, R.string.b, R.string.c, R.string.d, R.string.e };
	
	public static final String STRING_MAIM_ALL_TEXT = "所有";
	public static final String STRING_MAIM_LIKE_TEXT = "喜欢";
	public static final String STRING_MAIM_DONWLOAD_TEXT = "下载";
	public static final String STRING_MAIM_SETTING_TEXT = "设置";
	public static final String STRING_MAIM_SEARCH_TEXT = "查找";
	public static final String STRING_MAIM_EXIT_TEXT = "退出";
	public static final String STRING_FILE_RENAME = "重命名";
	public static final String STRING_FILE_COPY = "复制";
	public static final String STRING_FILE_COPY_FAIL = "复制失败";
	public static final String STRING_FILE_PASTE = "粘贴";
	public static final String STRING_FILE_MOVE = "移动";
	public static final String STRING_FILE_MOVE_FAIL = "移动失败";
	public static final String STRING_FILE_DELETE = "删除";
	public static final String STRING_FILE_ISORNOT_DELETE = "是否删除";
	public static final String STRING_FILE_OK = "确定";
	public static final String STRING_FILE_CANCEL = "取消";
	public static final String STRING_FILE_INTO = "信息";
	public static final String STRING_FILE_DETAILS = "详细";
	public static final String STRING_FILE_READ = "翻书效果阅读";
	public static final String STRING_MKDIR = "创建目录";
	public static final String STRING_FILE_EXISTS = "文件已经存在";
	public static final String STRING_FILE_NOT_EXISTS = "文件不存在";
	public static final String STRING_FILE_CREATE_FAIL = "文件创建失败";
	public static final String STRING_FILE_NAME_CANNOT_EMPTY = "名字不能为空";
	public static final String STRING_FILE_RENAME_FIAL= "重命名失败";
	public static final String STRING_FILE_MKDIR= "新建文件夹";
	public static final String STRING_FILE_BACK_UPLEVEL= "返回上一级";
	public static final String STRING_FILE_SDCARD_LIST= "SDCARD目录";
	public static final String STRING_FILE_REFLASH= "刷新";
	public static final String STRING_FILE_PLEASE_INPUT_NAME = "请输入名字";
	public static final String STRING_FILE_CHMOD_FAIL = "获取文件权限失败";
	public static final String STRING_FILE_ADDTOBOOKSHELF = "添加到书架";
	public static final String STRING_FILE_DELETEFROMBOOKSHELF = "从书架下架";
	public static final String STRING_FILE_REMOVEBOOKFROMBOOKSHELF = "下架该书本";
	
	
	
	public static final int INT_MENU_CRETEDIR = 1;//新建目录
	public static final int INT_MENU_EXIT = 2;//退出程序
	public static final int INT_MENU_COPY = 3;//复制
	public static final int INT_MENU_DELETE = 4;//删除
	public static final int INT_MENU_PASTE = 5;//粘贴
	public static final int INT_MENU_READ = 6;//读取
	public static final int INT_MENU_FRESH = 7;//刷新
	public static final int INT_MENU_BACK = 8;//回到上一层
	public static final int INT_MENU_BACKHOME = 9;//回到主目录
	public static final int INT_MENU_RENAME = 10;//重命名
	public static final int INT_MENU_MOVE = 11;//移动
	public static final int INT_MENU_INFO = 12;//信息
	public static final int INT_MENU_DETAILS = 13;//详细
	public static final int INT_MENU_CANCEL = 14;//取消
	public static final int INT_MENU_ADDTOBOOKSHELF = 15;//添加到书架
	public static final int INT_MENU_REMOVEBOOKFROMBOOKSHELF = 16;//从书架下架该书本
	public static final int INT_MENU_ON = 17;//开关 开
	public static final int INT_MENU_OFF = 18;//开关 关
	public static final int INT_BOOK_TIMER = 19;//定时器
	
	//阅读器支持解码的后缀名文件类型
	public static final String[] BOOK_SUFFIX = {".txt",".epub",".pdf"};
	//默认访问SD卡的路径
	public static final String  DEFAULT_SDCARD_PATH = "/mnt/sdcard/";
	
}

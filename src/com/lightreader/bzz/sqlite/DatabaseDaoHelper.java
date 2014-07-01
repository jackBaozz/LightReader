package com.lightreader.bzz.sqlite;

import java.util.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseDaoHelper extends SQLiteOpenHelper {

	// 数据库名称
	private static final String DB_NAME = "SQLiteDemo.db";
	// 数据库版本
	private static final int DB_VERSION = 1;
	// 表名
	public static final String T_DEMO = "demo";
	public static final String T_BOOKS = "t_books";
	// 创建表
	public static final String DB_CREATE = "CREATE TABLE IF NOT EXISTS " + T_DEMO + " (_id integer primary key autoincrement, name varchar(20), number varchar(10))";
	
	
	public static final String DB_CREATE_BOOK = "CREATE TABLE IF NOT EXISTS "+ T_BOOKS + "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,md5 TEXT,name TEXT,path TEXT,icon TEXT,size TEXT,isDirectory BOOLEAN,fileCount INTEGER,folderCount INTEGER,lastModifyDate DATETIME,fileCompetence text,totalCharacterNum  INTEGER,prePageCharacterNum  INTEGER,totalPage  INTEGER,currentPage  INTEGER,isShow INTEGER  ) ";
	public static final String DB_CREATE_BOOK_INDEX_1 = "CREATE UNIQUE INDEX \"index_1\" ON \"t_books\" (\"id\" ASC)";
	public static final String DB_CREATE_BOOK_INDEX_2 = "CREATE INDEX \"index_3\" ON \"t_books\" (\"md5\" ASC)";
	public static final String DB_CREATE_BOOK_INDEX_3 = "CREATE INDEX \"indiex_2\" ON \"t_books\" (\"name\" ASC)";
	public static final String DB_CREATE_BOOK_INDEX_4 = "CREATE INDEX \"indiex_4\" ON \"t_books\" (\"path\" ASC)";
	
	
	
	public DatabaseDaoHelper(Context context) {
		//Tips：当创建好SQLite数据库的之后，可以在/data/data/<package name>/databases目录下找到SQLite数据库文件。
		//     /data/data/com.lightreader.bzz.Activity
		super(context, DB_NAME, null, DB_VERSION);
	}

	/**
	 * 创建表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_CREATE);
		db.execSQL(DB_CREATE_BOOK);//创建表t_books
		db.execSQL(DB_CREATE_BOOK_INDEX_1);//创建表t_books的索引id
		db.execSQL(DB_CREATE_BOOK_INDEX_2);//创建表t_books的索引md5
		db.execSQL(DB_CREATE_BOOK_INDEX_3);//创建表t_books的索引name
		db.execSQL(DB_CREATE_BOOK_INDEX_4);//创建表t_books的索引path
	}

	/**
	 * 更新表
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/**可以拿到当前数据库的版本信息 与之前数据库的版本信息 用来更新数据库**/
		// db.execSQL("drop table if exists " + TABLE_NAME);
		// onCreate(db);

	}
	
	/** 
	* 删除数据库 
	* @param context 
	* @return 
	*/
	public boolean deleteDatabase(Context context) { 
		return context.deleteDatabase(DB_NAME); 
	} 
	
	

}
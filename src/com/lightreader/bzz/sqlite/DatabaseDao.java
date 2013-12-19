package com.lightreader.bzz.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseDao extends SQLiteOpenHelper {

	// ���ݿ�����
	private static final String DB_NAME = "SQLiteDemo.db";
	// ���ݿ�汾
	private static final int DB_VERSION = 1;

	// ����
	public static final String TABLE_NAME = "demo";

	private static final String DB_CREATE = "create table " + TABLE_NAME + " (_id integer primary key autoincrement, name varchar(20), number varchar(10))";

	public DatabaseDao(Context context) {
		super(context, DB_NAME, null, DB_VERSION);

	}

	/**
	 * ������
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DB_CREATE);

	}

	/**
	 * ���±�
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("drop table if exists " + TABLE_NAME);
		// onCreate(db);

	}
}
package com.lightreader.bzz.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseServer {
	
	private DatabaseDaoHelper databaseDaoHelper;

	public DatabaseServer(Context context) {
		this.databaseDaoHelper = new DatabaseDaoHelper(context);
	}

	/**
	 * 插入数据
	 * 
	 * @param name
	 *            名字
	 * @param number
	 *            数据
	 * @return 如果成功则返回true,否则返回false
	 */
	public boolean insert(String name, String number) {
		// 创建或打开数据库
		SQLiteDatabase db = databaseDaoHelper.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("number", number);
		// 插入数据,返回插入数据ID
		long id = db.insert(DatabaseDaoHelper.T_DEMO, null, cv);
		if (id != 0) {
			return true;
		}

		return false;
	}
	
	/**
	 * 插入books的dao
	 * @param name
	 * @param path
	 * @param icon
	 * @param size
	 * @param isDirectory
	 * @param fileCount
	 * @param folderCount
	 * @param dateStr 
	 * @param fileCompetence
	 * @return
	 */
	public boolean insertBooks(String md5, String name, String path, String icon, String size, Boolean isDirectory, Integer fileCount, Integer folderCount, String dateStr, String fileCompetence, Integer totalCharacterNum, Integer prePageCharacterNum, Integer totalPage, Integer currentPage,
			Integer isShow) {
		// 创建或打开数据库
		SQLiteDatabase db = databaseDaoHelper.getWritableDatabase();
		// Android自带的ContetValues,类似于Map,提供了put(String key, XXX value)的方法存入数据
		ContentValues cv = new ContentValues();
		// ID自动增长,所以默认可以不写
		cv.put("md5", md5);
		cv.put("name", name);
		cv.put("path", path);
		cv.put("icon", icon);
		cv.put("size", size);
		cv.put("isDirectory", isDirectory);
		cv.put("fileCount", fileCount);
		cv.put("folderCount", folderCount);
		cv.put("lastModifyDate", dateStr);
		cv.put("fileCompetence", fileCompetence);
		cv.put("totalCharacterNum", totalCharacterNum);
		cv.put("prePageCharacterNum", prePageCharacterNum);
		cv.put("totalPage", totalPage);
		cv.put("currentPage", currentPage);
		cv.put("isShow", isShow);
		// 插入数据,返回插入数据ID
		long id = db.insert(DatabaseDaoHelper.T_BOOKS, null, cv);
		if (id != 0) {
			return true;
		}
		return false;
	}
	
	
	
	/** 
	 *查询books的dao
	 *@return 返回数据列表
     */  
	public Cursor selectBook(String name, String path) {
		// 创建或打开数据库
		SQLiteDatabase db = databaseDaoHelper.getReadableDatabase();
		// 查询数据表中所有字段
		Cursor cursor = db.query(databaseDaoHelper.T_BOOKS, new String[]{"id","name","path"}, "name=?  AND  path=?", new String[] {name,path}, null, null, "id asc");
		return cursor;
	}
	
	public Cursor selectBook(String path) {
		// 创建或打开数据库
		SQLiteDatabase db = databaseDaoHelper.getReadableDatabase();
		// 查询数据表中所有字段
		Cursor cursor = db.query(databaseDaoHelper.T_BOOKS, new String[]{"id","name","path","isShow"}, "path=?", new String[] {path}, null, null, "id asc");
		return cursor;
	}
	
	//查询t_books表里面的所有数据
	public Cursor selectAllData() {
		/* 与数据库获得连接，获得只读属性 */
		SQLiteDatabase db = databaseDaoHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from " + databaseDaoHelper.T_BOOKS + " where isShow = 1 " , null);
		return cursor;
	}
	
	
	/* 查询数据库 *//* 返回一个游标对象 */
	public Cursor selectData() {
		/* 与数据库获得连接，获得只读属性 */
		SQLiteDatabase db = databaseDaoHelper.getReadableDatabase();
		//SQLiteDatabase sqliteDatabase = dbhelper.getReadableDatabase();
		//使用查询语句：方式一
		/* 使用游标保存得到的结果集 *//* 参1:查询语句 ； 参2：查询条件 */
		// Cursor cursor = sqliteDatabase.rawQuery("select * from student",null);
		/*
		 * 使用查询语句：方式二
		 * 
		 * @ distinct --是否去除重复行 例：值为：true/false;
		 * 
		 * @ table --表名
		 * 
		 * @ columns --要查询的列 例： new String[]{"id","name","age"}
		 * 
		 * @ selection --查询条件 例："id>?"
		 * 
		 * @ selectionArgs --查询条件的参数 例：new String[]{"3"}
		 * 
		 * @ groupBy --对查询的结果进行分组
		 * 
		 * @ having --对分组的结果进行限制
		 * 
		 * @ orderby --对查询的结果进行排序； 例："age asc"
		 * 
		 * @ limit --分页查询限制 ； 例："2,5" 从第2行开始，到第5行结束；注：行数从0 开始；
		 */
		
		//两种查询
		//Cursor cursor = db.query(databaseDao.T_BOOKS, new String[]{"name","path"}, null, null, null, null, "_id desc");
		Cursor cursor = db.query(true, "student", new String[] { "_id", "name", "age" }, "_id>?", new String[] { "1" }, null, null, "age desc", "1,5");
		//Cursor cursor = sqliteDatabase.query(true, "student", new String[] { "_id", "name", "age" }, "_id>?", new String[] { "1" }, null, null, "age desc", "1,5");

		/* 使用游标---获取游标中的数据 */
		while (cursor.moveToNext()) {
			String id = cursor.getString(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String age = cursor.getString(cursor.getColumnIndex("age"));
			// Toast.makeText(MainActivity.this,
			// "_id="+id+" name="+name+"  age="+age, 1000).show();
		}
		return cursor;

	}
	
	
	

	/**
	 * 更新数据
	 * 
	 * @param id
	 *            数据列_id
	 * @param number
	 *            数量
	 * @return 如果成功则返回true,否则返回false
	 */
	public boolean update(int id, String number) {
		// 创建或打开数据库
		SQLiteDatabase db = databaseDaoHelper.getWritableDatabase();

		// Android自带的ContetValues,类似于Map,提供了put(String key, XXX value)的方法存入数据
		ContentValues cv = new ContentValues();
		cv.put("number", number);

		// 通过ContentValues更新数据表,返回更新的ID值
		int result = db.update(databaseDaoHelper.T_DEMO, cv, "_id=?", new String[] { String.valueOf(id) });

		if (result != 0) {
			return true;
		}

		return false;
	}

	/**
	 * 更新书本数据
	 * @return 如果成功则返回true,否则返回false
	 */
	public boolean updateBook(String path,Integer isShow) {
		// 创建或打开数据库
		SQLiteDatabase db = databaseDaoHelper.getWritableDatabase();
		// Android自带的ContetValues,类似于Map,提供了put(String key, XXX value)的方法存入数据
		ContentValues cv = new ContentValues();
		cv.put("isShow", isShow);
		// 通过ContentValues更新数据表,返回更新的ID值
		int result = db.update(databaseDaoHelper.T_BOOKS, cv, "path=?", new String[] { path });
		if (result != 0) {
			return true;
		}
		return false;
	}
	
	
	
	
	
	/**
	 * 删除数据
	 * 
	 * @param id
	 *            数据列_id
	 * @return
	 */
	public boolean delete(int id) {
		// 创建或打开数据库
		SQLiteDatabase db = databaseDaoHelper.getWritableDatabase();
		// 删除指定ID值
		int result = db.delete(databaseDaoHelper.T_DEMO, "_id=?", new String[] { String.valueOf(id) });
		if (result != 0) {
			return true;
		}
		return false;
	}

	/**
	 * 查询数据
	 * 
	 * @return 返回数据列表
	 */
	public Cursor fetchAll() {
		// 创建或打开数据库
		SQLiteDatabase db = databaseDaoHelper.getReadableDatabase();
		// 查询数据表中所有字段
		Cursor cursor = db.query(databaseDaoHelper.T_DEMO, null, null, null, null, null, "_id desc");
		// db.query("user", new String[]{"id","name"}),"id=?",new
		// String[]{"1"},String groupBy, String having, String orderBy);
		if (cursor != null) {
			return cursor;
		}
		return null;

	}
}
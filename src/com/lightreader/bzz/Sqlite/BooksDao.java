package com.lightreader.bzz.Sqlite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.lightreader.bzz.Pojo.Book;

public class BooksDao extends SimpleDao<Book, Integer> {

	 /**
     * 定义该实体对应数据库的表名
     */
    public static final String TABLE_NAME = "t_books";
    /**
     * 数据库管理器
     */
    private DatabaseDaoHelper dbHelper;
    
    
    
    /**
     * 简化的构造器
     */
    public BooksDao(Context context) {
        this(TABLE_NAME, context);
    }
    
    /**
     * 重写父类的构造器
     */
	public BooksDao(String tableName, Context context) {
		super(tableName, context);
	}
	
	
}

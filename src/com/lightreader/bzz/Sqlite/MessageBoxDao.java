package com.lightreader.bzz.Sqlite;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;

import com.lightreader.bzz.Pojo.MessageBox;

public class MessageBoxDao extends SimpleDao<MessageBox, Integer> {
    /**
     * 定义该实体对应数据库的表名
     */
    public static final String TABLE_NAME = "t_message_box";

    /**
     * 简化的构造器
     */
    public MessageBoxDao(Context context) {
        this(TABLE_NAME, context);
    }

    /**
     * 重写父类的构造器
     */
    public MessageBoxDao(String tableName, Context context) {
        super(tableName, context);
    }

    public int selectMessageBox(MessageBox messageBox) {
        String msgSeq = messageBox.getMsgSeq().trim();//oracle表的主键,不能重复
        Cursor cursor = getDbHelper().getReadableDatabase().query(getTableName(), new String[]{"id"}, "msgSeq=?", new String[]{msgSeq}, null, null, "id asc");
        return cursor.getCount();//查询得到的结果集总数
    }


    /**
     * 获取未读的消息的条数
     */
    public int getUnreadMessageNumber() {
        Cursor cursor = getDbHelper().getReadableDatabase().query(getTableName(), new String[]{"id"}, "isRead=?", new String[]{"N"}, null, null, "id asc");
        return cursor.getCount();//查询得到的结果集总数
    }


    public boolean update(String msgSeq, String isRead) {
        // 创建或打开数据库
        // Android自带的ContetValues,类似于Map,提供了put(String key, XXX value)的方法存入数据
        ContentValues cv = new ContentValues();
        cv.put("isRead", isRead);
        // 通过ContentValues更新数据表,返回更新的ID值
        int result = getDbHelper().getWritableDatabase().update(TABLE_NAME, cv, "msgSeq=?", new String[]{msgSeq});
        return result != 0;
    }
    
    
    public int getMaxMsgSeq(String userId){
    	Cursor cursor = null;
    	if(userId.equals("null") || userId.equals("")){
    		cursor = getDbHelper().getReadableDatabase().rawQuery("SELECT max(msgSeq) as msgSeq FROM t_message_box where userId = 'null' ", null);
    	}else{
    		cursor = getDbHelper().getReadableDatabase().rawQuery("SELECT max(msgSeq) as msgSeq FROM t_message_box where userId = '"+userId+"' ", null);
    	}
    	
    	int returnMaxValue = 0;
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				returnMaxValue = cursor.getInt(0);
			} while (cursor.moveToNext()); // 将光标移动到下一行，从而判断该结果集是否有下一条数据
		}
		cursor.close();

    	return returnMaxValue;
    }
    
    
    
    
}

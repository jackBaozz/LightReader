package com.lightreader.bzz.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseServer {
        private DatabaseDao databaseDao;

        public DatabaseServer(Context context) {
                this.databaseDao = new DatabaseDao(context);
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
                SQLiteDatabase db = databaseDao.getWritableDatabase();

                ContentValues cv = new ContentValues();
                cv.put("name", name);
                cv.put("number", number);
                // 插入数据,返回插入数据ID
                long id = db.insert(databaseDao.TABLE_NAME, null, cv);
                if (id != 0) {
                        return true;
                }

                return false;
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

                SQLiteDatabase db = databaseDao.getWritableDatabase();

                // Android自带的ContetValues,类似于Map,提供了put(String key, XXX value)的方法存入数据
                ContentValues cv = new ContentValues();
                cv.put("number", number);

                // 通过ContentValues更新数据表,返回更新的ID值
                int result = db.update(databaseDao.TABLE_NAME, cv, "_id=?", new String[] { String.valueOf(id) });

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

                SQLiteDatabase db = databaseDao.getWritableDatabase();

                // 删除指定ID值
                int result = db.delete(databaseDao.TABLE_NAME, "_id=?", new String[] { String.valueOf(id) });
                
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

                SQLiteDatabase db = databaseDao.getReadableDatabase();
                // 查询数据表中所有字段
                Cursor cursor = db.query(databaseDao.TABLE_NAME, null, null, null, null, null, "_id desc");
                //db.query("user", new String[]{"id","name"}),"id=?",new String[]{"1"},String groupBy, String having, String orderBy);
                if (cursor != null) {
                        return cursor;
                }
                return null;

        }
}
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
	 * ��������
	 * 
	 * @param name
	 *            ����
	 * @param number
	 *            ����
	 * @return ����ɹ��򷵻�true,���򷵻�false
	 */
	public boolean insert(String name, String number) {
		// ����������ݿ�
		SQLiteDatabase db = databaseDao.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("number", number);
		// ��������,���ز�������ID
		long id = db.insert(databaseDao.TABLE_NAME, null, cv);
		if (id != 0) {
			return true;
		}

		return false;
	}

	/**
	 * ��������
	 * 
	 * @param id
	 *            ������_id
	 * @param number
	 *            ����
	 * @return ����ɹ��򷵻�true,���򷵻�false
	 */
	public boolean update(int id, String number) {

		SQLiteDatabase db = databaseDao.getWritableDatabase();

		// Android�Դ���ContetValues,������Map,�ṩ��put(String key, XXX value)�ķ�����������
		ContentValues cv = new ContentValues();
		cv.put("number", number);

		// ͨ��ContentValues�������ݱ�,���ظ��µ�IDֵ
		int result = db.update(databaseDao.TABLE_NAME, cv, "_id=?", new String[] { String.valueOf(id) });

		if (result != 0) {
			return true;
		}

		return false;
	}

	/**
	 * ɾ������
	 * 
	 * @param id
	 *            ������_id
	 * @return
	 */
	public boolean delete(int id) {

		SQLiteDatabase db = databaseDao.getWritableDatabase();

		// ɾ��ָ��IDֵ
		int result = db.delete(databaseDao.TABLE_NAME, "_id=?", new String[] { String.valueOf(id) });
		
		if (result != 0) {
			return true;
		}

		return false;
	}

	/**
	 * ��ѯ����
	 * 
	 * @return ���������б�
	 */
	public Cursor fetchAll() {

		SQLiteDatabase db = databaseDao.getReadableDatabase();
		// ��ѯ���ݱ��������ֶ�
		Cursor cursor = db.query(databaseDao.TABLE_NAME, null, null, null, null, null, "_id desc");
		//db.query("user", new String[]{"id","name"}),"id=?",new String[]{"1"},String groupBy, String having, String orderBy);
		if (cursor != null) {
			return cursor;
		}
		return null;

	}
}

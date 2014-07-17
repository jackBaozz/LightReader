package com.lightreader.bzz.Sqlite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.lightreader.bzz.Utils.BeanTools;

/**
 * 实现了EntityDao接口,其他实体DAO只要继承它即可拥有所有强大功能。
 *
 * @param <T>
 * @param <PK>
 * @author EwinLive
 */
public abstract class SimpleDao<T, PK extends Serializable> implements EntityDao<T, PK> {
    /**
     * 字段在数据表中所对应的列的索引 只在创建对象时初始化。
     */
    protected int[] fieldPosition;
    /**
     * 实体的类型
     */
    private Class<T> entityClass;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 数据库管理器
     */
    private DatabaseDaoHelper dbHelper;
    /**
     * 保存实体所要执行的SQL语句 只在创建对象时初始化。
     */
    private String saveSql;
    /**
     * 更新实体所要执行的SQL语句 只在创建对象时初始化。
     */
    private String updateSql;

    /**
     * 专属构造器 可通过子类的范型定义取得对象类型Class.
     *
     * @param tableName 实体对应的表名
     * @param context   设备上下文，通常是一个Activity对象
     */
    @SuppressWarnings("unchecked")
	public SimpleDao(String tableName, Context context) {
        //noinspection unchecked
        this.entityClass = (Class<T>) BeanTools.getGenericClass(getClass());
        this.tableName = tableName;
        this.dbHelper = new DatabaseDaoHelper(context);
        this.saveSql = initSaveSql();
        this.updateSql = initUpdateSql();
        this.fieldPosition = initFieldPosition();
    }

    public String getTableName() {
        return tableName;
    }

    public DatabaseDaoHelper getDbHelper() {
        return dbHelper;
    }

    @Override
    public void save(T entity) throws Exception {
        dbHelper.getReadableDatabase().execSQL(saveSql, getSaveValue(entity));
    }

    @Override
    public void remove(PK... ids) {
        if (ids.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ids.length; i++) {
                if (i > 0) {
                    sb.append(',');
                }
                sb.append('?');
            }
            sb.deleteCharAt(sb.length() - 1);
            dbHelper.getReadableDatabase().execSQL("delete from " + tableName + " where id in(" + sb + ")", ids);
        }
    }

    @Override
    public void update(T entity) throws Exception {
        dbHelper.getReadableDatabase().execSQL(updateSql, getUpdateValue(entity));
    }

    @Override
    public T find(PK id) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from " + tableName + " where id=?", new String[]{String.valueOf(id)});
        cursor.moveToNext();
        return getEntityFromCursor(cursor);
    }

    public List<T> getScrollData(Integer startResult, Integer maxResult, String field, String sortType, String selectType) {
        String thisSortType = "";
        if (sortType.equals("ASC")) {
            thisSortType = "ASC";
        } else if (sortType.equals("DESC")) {
            thisSortType = "DESC";
        } else {
            thisSortType = "ASC";
        }
        
        List<T> list = new ArrayList<T>(0);
        // Cursor cursor =
        // dbHelper.getReadableDatabase().rawQuery("select * from " + tableName
        // + " limit ?,? " + field + " " + thisSortType + " ", new String[] {
        // String.valueOf(startResult), String.valueOf(maxResult) });
        
        Cursor cursor = null;
        if (selectType.equals("PUBLIC")) {
        	//只查询公共的消息
        	cursor = dbHelper.getReadableDatabase().rawQuery("select * from " + tableName +" where userId = 'null' " + " order by " + field + " " + thisSortType + " limit ?,? ", new String[]{String.valueOf(startResult), String.valueOf(maxResult)});
        }else{
        	//公共的消息和用户个人消息都查询
        	cursor = dbHelper.getReadableDatabase().rawQuery("select * from " + tableName + " order by " + field + " " + thisSortType + " limit ?,? ", new String[]{String.valueOf(startResult), String.valueOf(maxResult)});
        }
        while (cursor.moveToNext()) {
            list.add(getEntityFromCursor(cursor));
        }
        return list;
    }

    
    public List<T> getScrollData(Integer startResult, Integer maxResult, String field) {
        List<T> list = new ArrayList<T>(0);
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from " + tableName + " limit ?,? " + field, new String[] {String.valueOf(startResult), String.valueOf(maxResult) });
        while (cursor.moveToNext()) {
            list.add(getEntityFromCursor(cursor));
        }
        return list;
    }
    
    
    @Override
    public Long count() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select count(*) from " + tableName, null);
        if (cursor.moveToNext())
            return cursor.getLong(0);
        return 0l;
    }

    
    
    /**
     * 初始化保存实体所需的SQL语句
     */
    @SuppressWarnings("rawtypes")
    protected String initSaveSql() {
        HashMap data = BeanTools.getAllFiled(entityClass);
        String[] fieldName = (String[]) data.get("fieldName");
        StringBuilder bufferName = new StringBuilder();
        StringBuilder bufferExpr = new StringBuilder();

        for (String tmp : fieldName) {
            bufferName.append(tmp).append(',');
            bufferExpr.append("?,");
        }

        // 去除id字段及其属性值
        bufferName.delete(bufferName.indexOf("id"), bufferName.indexOf("id") + 3);
        bufferExpr.delete(0, 2);

        // 去除多余的分隔符
        bufferName.deleteCharAt(bufferName.length() - 1);
        bufferExpr.deleteCharAt(bufferExpr.length() - 1);

        return "insert into " + tableName + "(" + bufferName.toString() + ") values(" + bufferExpr.toString() + ")";
    }

    /**
     * 初始化更新实体所需的SQL语句
     */
    @SuppressWarnings("rawtypes")
    protected String initUpdateSql() {
        HashMap data = BeanTools.getAllFiled(entityClass);
        String[] fieldName = (String[]) data.get("fieldName");

        StringBuilder sqlBuffer = new StringBuilder();
        sqlBuffer.append("update ").append(tableName).append(" set ");
        for (String tmp : fieldName) {
            sqlBuffer.append(tmp).append("=?, ");
        }

        // 去除id字段及其属性值
        sqlBuffer.delete(sqlBuffer.indexOf(" id=?"), sqlBuffer.indexOf("id") + 5);
        sqlBuffer.deleteCharAt(sqlBuffer.length() - 2);
        sqlBuffer.append("where id =?");

        return sqlBuffer.toString();
    }

    /**
     * 获取保存实体所需的值
     */
    @SuppressWarnings("rawtypes")
    protected Object[] getSaveValue(T entity) throws IllegalAccessException, NoSuchFieldException {
        HashMap data = BeanTools.getAllFiled(entityClass);
        String[] fieldName = (String[]) data.get("fieldName");
        Object[] values;

        int length = fieldName.length;
        values = new Object[length - 1];
        int j = 0;
        for (String aFieldName : fieldName) {
            if ("id".equals(aFieldName)) {
                continue;// 跳过ID字段
            }
            values[j++] = BeanTools.getPrivateProperty(entity, aFieldName);
        }
        return values;
    }

    /**
     * 获取更新实体所需的值
     */
    @SuppressWarnings("rawtypes")
    protected Object[] getUpdateValue(T entity) throws Exception {
        HashMap data = BeanTools.getAllFiled(entityClass);
        String[] fieldName = (String[]) data.get("fieldName");
        Object[] values;

        int length = fieldName.length;
        values = new Object[length - 1];
        int j = 0;
        int id = 0;

        for (String aFieldName : fieldName) {
            if ("id".equals(aFieldName)) {
                id = (Integer) BeanTools.getPrivateProperty(entity, aFieldName);
                continue;// 跳过ID字段
            }
            values[j++] = BeanTools.getPrivateProperty(entity, aFieldName);
        }

        Object[] values2 = new Object[length];
        System.arraycopy(values, 0, values2, 0, values.length);
        values2[length - 1] = id;

        return values2;
    }

    /**
     * 初始化字段在数据表中 对应的索引
     */
    @SuppressWarnings("rawtypes")
    protected int[] initFieldPosition() {
        HashMap data = BeanTools.getAllFiled(entityClass);
        String[] fieldName = (String[]) data.get("fieldName");
        int length = fieldName.length;
        int[] position = new int[length];
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("select * from " + tableName + " limit ?, ?", new String[]{"0", "2"});
        for (int i = 0; i < length; i++) {
            position[i] = cursor.getColumnIndex(fieldName[i]);
        }

        return position;
    }

    /**
     * 从游标中获取实体
     *
     * @param cursor 游标
     * @return T 实体对象
     */
    @SuppressWarnings("rawtypes")
    public T getEntityFromCursor(Cursor cursor) {
        HashMap data = BeanTools.getAllFiled(entityClass);
        String[] fieldName = (String[]) data.get("fieldName");
        Class<?>[] fieldType = (Class<?>[]) data.get("fieldType");
        int length = fieldName.length;

        T entity = null;
        String db_data;
        String fieldTypeName;
        try {
            entity = entityClass.newInstance();
            for (int i = 0; i < length; i++) {
                fieldTypeName = fieldType[i].getSimpleName();
                db_data = cursor.getString(fieldPosition[i]);
                if (null != db_data) {
                    if ("String".equals(fieldTypeName)) {
                        BeanTools.setFieldValue(entity, fieldName[i], db_data);
                    } else if ("int".equals(fieldTypeName)) {
                        BeanTools.setFieldValue(entity, fieldName[i], Integer.parseInt(db_data));
                    } else if ("long".equals(fieldTypeName)) {
                        BeanTools.setFieldValue(entity, fieldName[i], Long.getLong(db_data));
                    } else if ("float".equals(fieldTypeName)) {
                        BeanTools.setFieldValue(entity, fieldName[i], Float.parseFloat(db_data));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }
}

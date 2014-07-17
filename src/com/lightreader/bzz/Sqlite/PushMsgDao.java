package com.lightreader.bzz.Sqlite;

import android.content.Context;

import com.lightreader.bzz.Pojo.PushMsg;

public class PushMsgDao extends SimpleDao<PushMsg, Integer> {
    /**
     * 定义该实体对应数据库的表名
     */
    public static final String TABLE_NAME = "t_push";

    /**
     * 简化的构造器
     */
    public PushMsgDao(Context context) {
        this(TABLE_NAME, context);
    }

    /**
     * 重写父类的构造器
     */
    public PushMsgDao(String tableName, Context context) {
        super(tableName, context);
    }

}

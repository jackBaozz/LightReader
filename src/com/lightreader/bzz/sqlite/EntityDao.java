package com.lightreader.bzz.sqlite;

import java.io.Serializable;

/**
 * 基本DAO接口
 *
 * @param <T>
 * @param <PK>
 * @author EwinLive
 */
public interface EntityDao<T, PK extends Serializable> {

    /**
     * 添加
     */
    void save(final T entity) throws Exception;

    /**
     * 移除记录（指定ID集）
     *
     * @param ids 可以有多个
     */
    void remove(final PK... ids);

    /**
     * 更新
     *
     * @throws Exception
     */
    void update(final T entity) throws Exception;

    /**
     * 按ID查询对象
     */
    T find(final PK id);

    /**
     * 返回记录总数
     */
    public Long count();

}

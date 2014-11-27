package com.lightreader.bzz.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Application;
import android.database.Cursor;
import android.util.DisplayMetrics;

import com.lightreader.bzz.Pojo.Book;
import com.lightreader.bzz.Sqlite.BooksDao;
import com.lightreader.bzz.Sqlite.DatabaseServer;
import com.lightreader.bzz.Sqlite.MessageBoxDao;

public class AllApplication extends Application {
    public static ArrayList<HashMap<String, Object>> tempObjectsList = new ArrayList<HashMap<String, Object>>();//全局的临时变量List
    private static AllApplication instance = null;
    private static MessageBoxDao messageBoxDao = null;//SQLite数据库操作
    private static BooksDao booksDao = null;//SQLite数据库操作
    private static DatabaseServer databaseServer = null;
    
    public AllApplication() {
        instance = this;
    }

    public static AllApplication getInstance() {
        return instance;
    }


    public static MessageBoxDao getMessageBoxDao() {
        // 初始化的时候创建一个数据库的表对象
        if (messageBoxDao == null) {
            messageBoxDao = new MessageBoxDao(getInstance().getApplicationContext());
        }
        return messageBoxDao;
    }

    public static BooksDao getBooksDao() {
        // 初始化的时候创建一个数据库的表对象
        if (booksDao == null) {
        	booksDao = new BooksDao(getInstance().getApplicationContext());
        }
        return booksDao;
    }

    public static DatabaseServer getDatabaseServer() {
        // 初始化的时候创建一个数据库的表对象
        if (databaseServer == null) {
        	databaseServer = new DatabaseServer(getInstance().getApplicationContext());
        }
        return databaseServer;
    }
    
    
    
    /**
     * 初始化图片缓存组件
     */
    public void initImageLoader() {
        /*File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
//                .memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiscCache(cacheDir))
//                .diskCacheSize(50 * 1024 * 1024)
//                .diskCacheFileCount(200)
//                .writeDebugLogs()
                .build();


        ImageLoader.getInstance().init(config);*/
    }


    @Override
    public void onCreate() {
        super.onCreate();
        
        //测试
        getBooks();
    }

    /**
     * 友盟升级检查
     */
    public void umengUpdate() {
        //UmengUpdateAgent.setUpdateOnlyWifi(false); // 仅Wifi下更新
        //UmengUpdateAgent.update(this); // 自动更新
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        //refreshUser();
        //refreshIntegral();
    }
    
    
    /**
     * 获取当前数据库book的信息
     */
    public List<Book> getBooks(){
    	List<Book> list = new ArrayList<Book>(0);
    	Cursor cursor = getDatabaseServer().selectAllData();//得到游标的源数据
    	while (cursor.moveToNext()) {
    		Book book = getBooksDao().getEntityFromCursor(cursor);
            list.add(book);//源数据转换为实体对象
        }
    	cursor.close();
        return list;
    }
    
    
}

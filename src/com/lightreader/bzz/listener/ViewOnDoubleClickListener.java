package com.lightreader.bzz.listener;


import android.os.Handler;
import android.os.Message;
import android.view.View;

public class ViewOnDoubleClickListener {
        
        public static interface OnDoubleClickListener {
                public abstract void OnSingleClick(View view);
                public abstract void OnDoubleClick(View view);
        }
        
        private View _view = null; 
        private OnDoubleClickListener _listener = null;
        
        
        public ViewOnDoubleClickListener(View view, final OnDoubleClickListener listener){
                this._view = view;
                this._listener = listener;
                registerDoubleClickListener(view,listener);
        }
        
        /**
         * 注册一个双击事件
         * 改自网友的，增加  Handler  处理，如果不加这个，会引起线程安全之类错误。<br>     
         */
        public void registerDoubleClickListener(View view, final OnDoubleClickListener listener){
            if(listener==null) return;
            
            view.setOnClickListener(new View.OnClickListener() {
                private static final int DOUBLE_CLICK_TIME = 350;        //双击间隔时间350毫秒
                private boolean waitDouble = true;  
                 
                private Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        listener.OnSingleClick((View)msg.obj);
                    }
                };
                 
                //等待双击
                public void onClick(final View v) {
                    if(waitDouble){
                        waitDouble = false;        //与执行双击事件
                        new Thread(){
                            public void run() {
                                try {
                                    Thread.sleep(DOUBLE_CLICK_TIME);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }    
                                //等待双击时间，否则执行单击事件
                                if(!waitDouble){
                                    //如果过了等待时间,还是预执行双击状态，则视为单击
                                    waitDouble = true;
                                    //Message msg = handler.obtainMessage();  
                                    Message msg = Message.obtain();
                                    msg.obj = v;
                                    handler.sendMessage(msg);
                                }
                            }
                        }.start();
                    }else{
                        waitDouble = true;
                        listener.OnDoubleClick(v);    //执行双击
                    }
                }
            });
        }

        
        
        
}
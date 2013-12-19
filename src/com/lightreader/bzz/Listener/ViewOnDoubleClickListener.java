package com.lightreader.bzz.Listener;


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
	 * ע��һ��˫���¼�
	 * �������ѵģ�����  Handler  �����������������������̰߳�ȫ֮�����<br>     
	 */
	public void registerDoubleClickListener(View view, final OnDoubleClickListener listener){
	    if(listener==null) return;
	    
	    view.setOnClickListener(new View.OnClickListener() {
	        private static final int DOUBLE_CLICK_TIME = 350;        //˫�����ʱ��350����
	        private boolean waitDouble = true;  
	         
	        private Handler handler = new Handler(){
	            @Override
	            public void handleMessage(Message msg) {
	                listener.OnSingleClick((View)msg.obj);
	            }
	        };
	         
	        //�ȴ�˫��
	        public void onClick(final View v) {
	            if(waitDouble){
	                waitDouble = false;        //��ִ��˫���¼�
	                new Thread(){
	                    public void run() {
	                        try {
	                            Thread.sleep(DOUBLE_CLICK_TIME);
	                        } catch (InterruptedException e) {
	                            e.printStackTrace();
	                        }    
	                        //�ȴ�˫��ʱ�䣬����ִ�е����¼�
	                        if(!waitDouble){
	                            //������˵ȴ�ʱ��,����Ԥִ��˫��״̬������Ϊ����
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
	                listener.OnDoubleClick(v);    //ִ��˫��
	            }
	        }
	    });
	}

	
	
	
}

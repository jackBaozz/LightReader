package com.lightreader.bzz.activity;

import java.util.ArrayList;





import com.lightreader.bzz.Layout.BookLayout;
import com.lightreader.bzz.adapter.BookAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class ReadBookActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		BookLayout bookLayout = new BookLayout(this);//view的子类
		Intent intent = getIntent();
		ArrayList<String> bookContents = intent.getExtras().getStringArrayList("texts");
		BookAdapter bookAdapter = new BookAdapter(this);
		bookAdapter.addItem(bookContents);//Adapter里装入数据
		bookLayout.setPageAdapter(bookAdapter);//layout里装入刚才的数据
		ReadBookActivity.this.setContentView(bookLayout);//往Activity里面装入这个布局
	}
}

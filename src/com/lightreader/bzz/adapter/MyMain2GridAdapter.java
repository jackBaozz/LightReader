package com.lightreader.bzz.adapter;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lightreader.bzz.activity.R;
import com.lightreader.bzz.utils.Constant;

public class MyMain2GridAdapter extends BaseAdapter {
private LayoutInflater inflater;
private ArrayList<Bitmap> types;
private int screenWidth;
private int screenHeight;
private ArrayList<String> textList;
	public MyMain2GridAdapter(LayoutInflater flater,ArrayList<Bitmap> types,int screenWidth,int screenHeight) {
		this.inflater=flater;
		this.types=types;
		this.screenWidth=screenWidth;
		this.screenHeight=screenHeight;
		// TODO Auto-generated constructor stub
		
		textList=new ArrayList<String>();
		textList.add(Constant.STRING_MAIM_ALL_TEXT);
		textList.add(Constant.STRING_MAIM_LIKE_TEXT);
//		textList.add(Constant.STRING_MAIM_DONWLOAD_TEXT);
		textList.add(Constant.STRING_MAIM_SETTING_TEXT);
//		textList.add(Constant.STRING_MAIM_SEARCH_TEXT);
		textList.add(Constant.STRING_MAIM_EXIT_TEXT);
	}
	public int getCount() {
		// TODO Auto-generated method stub
		return types.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.activity_main, null);
		//ImageView pic = (ImageView)view.findViewById(R.id.maingrid2_pics);
		Bitmap srcpic = types.get(position);
		//Bitmap newpic = BitmapUtil.GetNewBitmap(srcpic, screenWidth, screenHeight,(screenWidth/4)-2, screenHeight/6);
		//pic.setImageBitmap(newpic);
		return view;
	}


}

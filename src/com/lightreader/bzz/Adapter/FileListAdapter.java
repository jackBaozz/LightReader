package com.lightreader.bzz.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lightreader.bzz.Activity.R;
import com.lightreader.bzz.pojo.FileItem;

public class FileListAdapter extends BaseAdapter {

	ArrayList<FileItem> items;
	LayoutInflater inflater;

	class ViewHolder {
		ImageView ivFileIcon;
		TextView tvFileName;
	}
	
	public FileListAdapter(Context context,ArrayList<FileItem> items){
		super();
		this.inflater = LayoutInflater.from(context);
		this.items = items;
	}
	
	public FileListAdapter(LayoutInflater inflater, ArrayList<FileItem> items) {
		super();
		this.items = items;
		this.inflater = inflater;
	}

	public void dataChanged(ArrayList<FileItem> items) {
		this.items = items;
		this.notifyDataSetChanged();// 通知数据改变
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewgroup) {
		ViewHolder holder = null;
		if (view == null) {
			view = inflater.inflate(R.layout.item, null);
			holder = new ViewHolder();
			holder.ivFileIcon = (ImageView) view.findViewById(R.id.ivFileIcon);
			holder.tvFileName = (TextView) view.findViewById(R.id.tvFileName);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		FileItem item = items.get(position);

		holder.ivFileIcon.setImageDrawable(item.getFileIcon());
		holder.tvFileName.setText(item.getFileName());
		return view;
	}

}

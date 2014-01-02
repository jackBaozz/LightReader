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

	ArrayList<FileItem> itemsList;
	LayoutInflater inflater;

	class ViewHolder {
		ImageView imageViewFileIcon;
		TextView textViewFileName;
		public ImageView getImageViewFileIcon() {
			return imageViewFileIcon;
		}
		public void setImageViewFileIcon(ImageView imageViewFileIcon) {
			this.imageViewFileIcon = imageViewFileIcon;
		}
		public TextView getTextViewFileName() {
			return textViewFileName;
		}
		public void setTextViewFileName(TextView textViewFileName) {
			this.textViewFileName = textViewFileName;
		}
	}
	
	public FileListAdapter(Context context,ArrayList<FileItem> itemsList){
		super();
		this.inflater = LayoutInflater.from(context);
		this.itemsList = itemsList;
	}
	
	public FileListAdapter(LayoutInflater inflater, ArrayList<FileItem> items) {
		super();
		this.itemsList = items;
		this.inflater = inflater;
	}

	public void dataChanged(ArrayList<FileItem> items) {
		this.itemsList = items;
		this.notifyDataSetChanged();// 通知数据改变
	}

	@Override
	public int getCount() {
		return itemsList.size();
	}

	@Override
	public Object getItem(int position) {
		return itemsList.get(position);
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
			holder.setImageViewFileIcon((ImageView) view.findViewById(R.id.ivFileIcon));
			holder.setTextViewFileName((TextView) view.findViewById(R.id.tvFileName));
			view.setTag(holder);//给每一个view设置一个标签
		} else {
			holder = (ViewHolder) view.getTag();
		}
		FileItem fileItem = itemsList.get(position);
	
		holder.getImageViewFileIcon().setImageDrawable(fileItem.getFileIcon());
		holder.getTextViewFileName().setText(fileItem.getFileName());
		return view;
	}

}

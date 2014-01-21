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
import com.lightreader.bzz.pojo.FileInfo;
import com.lightreader.bzz.utils.Constant;

public class FileListAdapter extends BaseAdapter {

	ArrayList<FileInfo> itemsList;
	LayoutInflater inflater;
	ViewHolder currentHolder;
	
	public ViewHolder getCurrentHolder() {
		return currentHolder;
	}

	public void setCurrentHolder(ViewHolder currentHolder) {
		this.currentHolder = currentHolder;
	}

	
	
	
	
	public class ViewHolder {
		ImageView imageViewFileIcon;
		ImageView imageViewFileCheckIcon;
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
		public ImageView getImageViewFileCheckIcon() {
			return imageViewFileCheckIcon;
		}
		public void setImageViewFileCheckIcon(ImageView imageViewFileCheckIcon) {
			this.imageViewFileCheckIcon = imageViewFileCheckIcon;
		}
	}
	
	public FileListAdapter(Context context,ArrayList<FileInfo> itemsList){
		super();
		this.inflater = LayoutInflater.from(context);
		this.itemsList = itemsList;
	}
	
	public FileListAdapter(LayoutInflater inflater, ArrayList<FileInfo> itemsList) {
		super();
		this.inflater = inflater;
		this.itemsList = itemsList;
	}

	public void dataChanged(ArrayList<FileInfo> items) {
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
			holder.setImageViewFileCheckIcon((ImageView) view.findViewById(R.id.ivFileCheckIcon));
			view.setTag(holder);//给每一个view设置一个标签
		} else {
			holder = (ViewHolder) view.getTag();
		}
		FileInfo fileItem = itemsList.get(position);//一行,一个FileInfo对象
	
		holder.getImageViewFileIcon().setImageDrawable(fileItem.getIcon());
		holder.getTextViewFileName().setText(fileItem.getName());
		if (fileItem.isIsDirectory() == false) {// 不是文件夹
			for (String suffixString : Constant.BOOK_SUFFIX) {// 后缀名必须符合规范
				if (fileItem.getName().endsWith(suffixString)) {
					holder.getImageViewFileCheckIcon().setImageResource(R.drawable.btn_check_buttonless_off);// 未勾选状态
					break;
				}
			}
		}
		return view;
	}

}

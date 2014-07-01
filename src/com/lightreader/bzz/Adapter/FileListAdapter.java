package com.lightreader.bzz.Adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
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

	ArrayList<FileInfo> fileItemsList;// 数据List
	LayoutInflater inflater;
	ViewHolder currentHolder;
	private int position;
	private int flag;//开关
	
	
	
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

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

	// 构造方法1
	public FileListAdapter(Context context, ArrayList<FileInfo> fileItemsList) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.fileItemsList = fileItemsList;
	}

	// 构造方法2
	public FileListAdapter(Context context, ArrayList<FileInfo> fileItemsList, int position, int flag) {
		super();
		this.inflater = LayoutInflater.from(context);
		this.fileItemsList = fileItemsList;
		this.position = position;
		this.flag = flag;
	}

	// 构造方法3
	public FileListAdapter(LayoutInflater inflater, ArrayList<FileInfo> fileItemsList) {
		super();
		this.inflater = inflater;
		this.fileItemsList = fileItemsList;
	}

	public void dataChanged(ArrayList<FileInfo> fileItemsList) {
		this.fileItemsList = fileItemsList;
		this.notifyDataSetChanged();// 通知数据改变
	}

	/**
	 * 系统在绘制ListView之前，将会先调用getCount方法来获取Item的个数
	 */
	@Override
	public int getCount() {
		return this.fileItemsList.size();
	}

	@Override
	public Object getItem(int position) {
		return this.fileItemsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 每绘制一个 Item就会调用一次getView方法，
	 * 在此方法内就可以引用事先定义好的xml来确定显示的效果并返回一个View对象作为一个Item显示出来。 
	 * 也正是在这个过程中完成了适配器的主要转换功能，把数据和资源以开发者想要的效果显示出来。 
	 * 也正是getView的重复调用，使得ListView的使用更为简单和灵活。
	 * 
	 * 这两个方法是自定ListView显示效果中最为重要的，同时只要重写好了这两个方法，ListView就能完全按开发者的要求显示。
	 * 而getItem和getItemId方法将会在调用ListView的响应方法的时候被调用到。
	 * 所以要保证ListView的各个方法有效的话，这两个方法也得重写。
	 */
	@Override
	public View getView(int position, View view, ViewGroup viewgroup) {
		ViewHolder holder = null;

		if (view == null) {
			view = inflater.inflate(R.layout.item, null);
			holder = new ViewHolder();
			holder.setImageViewFileIcon((ImageView) view.findViewById(R.id.ivFileIcon));
			holder.setTextViewFileName((TextView) view.findViewById(R.id.tvFileName));
			holder.setImageViewFileCheckIcon((ImageView) view.findViewById(R.id.ivFileCheckIcon));
			view.setTag(holder);// 给每一个view设置一个标签
		} else {
			holder = (ViewHolder) view.getTag();
		}
		FileInfo fileItem = fileItemsList.get(position);// 一行,一个FileInfo对象
		holder.getImageViewFileIcon().setImageDrawable(fileItem.getIcon());
		holder.getTextViewFileName().setText(fileItem.getName());
		
		int isShow = fileItem.getIsShow();//是否已经上架下架
		
		if (fileItem.isIsDirectory() == false) {// 不是文件夹
			for (String suffixString : Constant.BOOK_SUFFIX) {// 后缀名必须符合规范
				if (fileItem.getName().endsWith(suffixString)) {
					Log.i(" position[1]: ", String.valueOf(this.getPosition()));
					Log.i(" position[2]: ", String.valueOf(position));
					
					if(isShow == 1){
						holder.getImageViewFileCheckIcon().setImageResource(R.drawable.btn_check_buttonless_on);// 勾选状态
					}else{
						holder.getImageViewFileCheckIcon().setImageResource(R.drawable.btn_check_buttonless_off);// 未勾选状态
					}
					/*
					if (this.getFlag() == Constant.INT_MENU_ON && this.getPosition() == position) {
						holder.getImageViewFileCheckIcon().setImageResource(R.drawable.btn_check_buttonless_on);// 勾选状态
					} else {
						holder.getImageViewFileCheckIcon().setImageResource(R.drawable.btn_check_buttonless_off);// 未勾选状态
					}*/
				}
			}
		}
		return view;
	}

}

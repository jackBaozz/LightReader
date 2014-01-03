package com.lightreader.bzz.pojo;

import java.util.Date;

import android.graphics.drawable.Drawable;

import com.lightreader.bzz.Activity.R;

public class FileInfo {
	private String Name;
	private String Path;
	private Drawable Icon;
	private long Size;
	private boolean IsDirectory = false;
	private int FileCount = 0;
	private int FolderCount = 0;
	private Date lastModifyDate;
	
	
	public Date getLastModifyDate() {
		return lastModifyDate;
	}


	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}


	public int getIconResourceId() {
		if (IsDirectory) {
			return R.drawable.folder;
		}
		return R.drawable.doc;
	}
	
	
	public String getName() {
		return Name;
	}


	public void setName(String name) {
		Name = name;
	}


	public String getPath() {
		return Path;
	}


	public void setPath(String path) {
		Path = path;
	}


	public Drawable getIcon() {
		return Icon;
	}


	public void setIcon(Drawable icon) {
		Icon = icon;
	}


	public long getSize() {
		return Size;
	}


	public void setSize(long size) {
		Size = size;
	}


	public boolean isIsDirectory() {
		return IsDirectory;
	}


	public void setIsDirectory(boolean isDirectory) {
		IsDirectory = isDirectory;
	}


	public int getFileCount() {
		return FileCount;
	}


	public void setFileCount(int fileCount) {
		FileCount = fileCount;
	}


	public int getFolderCount() {
		return FolderCount;
	}


	public void setFolderCount(int folderCount) {
		FolderCount = folderCount;
	}


	public FileInfo() {
		super();
	}

	public FileInfo(String name, Drawable icon) {
		super();
		Name = name;
		Icon = icon;
	}
	
}
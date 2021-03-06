package com.lightreader.bzz.Pojo;

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
	private String fileCompetence;//权限
	private int isShow;//是否已经添加,是否已经可以显示 1:显示 0:隐藏
	
	
	public int getIsShow() {
		return isShow;
	}


	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}


	public Date getLastModifyDate() {
		return lastModifyDate;
	}


	public String getFileCompetence() {
		return fileCompetence;
	}


	public void setFileCompetence(String fileCompetence) {
		this.fileCompetence = fileCompetence;
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


	public FileInfo(String name, String path, Drawable icon, long size, boolean isDirectory) {
		super();
		this.Name = name;
		this.Path = path;
		this.Icon = icon;
		this.Size = size;
		this.IsDirectory = isDirectory;
	}

	public FileInfo(String name, String path, Drawable icon, long size, boolean isDirectory,int isShow) {
		super();
		this.Name = name;
		this.Path = path;
		this.Icon = icon;
		this.Size = size;
		this.IsDirectory = isDirectory;
		this.isShow = isShow;
	}

	public FileInfo(String name, String path, Drawable icon, long size, boolean isDirectory, String fileCompetence) {
		super();
		this.Name = name;
		this.Path = path;
		this.Icon = icon;
		this.Size = size;
		this.IsDirectory = isDirectory;
		this.fileCompetence = fileCompetence;
	}


	
	
	
	
	
}

package com.lightreader.bzz.pojo;

import android.graphics.drawable.Drawable;

public class FileItem {
	String fileName;
	Drawable fileIcon;
	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Drawable getFileIcon() {
		return fileIcon;
	}

	public void setFileIcon(Drawable fileIcon) {
		this.fileIcon = fileIcon;
	}

	@Override
	public String toString() {
		return "FileItem [fileName=" + fileName + ", fileIcon=" + fileIcon + "]";
	}

	public FileItem() {
		super();
	}

	public FileItem(String fileName, Drawable fileIcon) {
		super();
		this.fileName = fileName;
		this.fileIcon = fileIcon;
	}
	
}

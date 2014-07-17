package com.lightreader.bzz.File;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;

import com.lightreader.bzz.Pojo.FileInfo;

/**
 * 文件排序, .XX文件在最上,其次按0-9,a-z(字母不分大小写)排序文件夹,最后才是文件
 */
public class FileComparator implements Comparator<FileInfo> {
	
	public int compare(FileInfo file1, FileInfo file2) {
		// 文件夹排在前面
		if (file1.isIsDirectory() && !file2.isIsDirectory()) {
			return -1000;
		} else if (!file1.isIsDirectory() && file2.isIsDirectory()) {
			return 1000;
		}
		// 相同类型按名称排序
		return file1.getName().compareTo(file2.getName());
	}

	
	/**
	 * 文件按文件名排序（从A到Z）
	 */
	public class CompratorByFileName implements Comparator<FileInfo> {
		@Override
		public int compare(FileInfo lhs, FileInfo rhs) {
			Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);
			return cmp.compare(lhs.getName(), rhs.getName());
		}

		@Override
		public boolean equals(Object o) {
			return true;
		}
	}

	/**
	 * 按文件大小排序(从小到大)
	 */
	public class CompratorByFileSize implements Comparator<FileInfo> {
		@Override
		public int compare(FileInfo lhs, FileInfo rhs) {
			if (lhs.getSize() > rhs.getSize()) {
				return 1;
			} else if (lhs.getSize() == rhs.getSize()) {
				return 0;
			} else {
				return -1;
			}
		}

		@Override
		public boolean equals(Object o) {
			return true;
		}
	}

	/**
	 * 按 文件修改时间排序（从旧到新）
	 * 
	 */
	public class CompratorByFileTime implements Comparator<FileInfo> {
		@Override
		public int compare(FileInfo file1, FileInfo file2) {
			int diff = 0;
			try {
				diff = file1.getLastModifyDate().compareTo(file2.getLastModifyDate());
			} catch (NullPointerException e) {
				diff = 1;
			}
			if (diff > 0) {
				return 1;
			} else if (diff == 0) {
				return 0;
			} else {
				return -1;
			}
		}

		@Override
		public boolean equals(Object o) {
			return true;
		}
	}

}

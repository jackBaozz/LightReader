package com.lightreader.bzz.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.os.Environment;

public class FileUtil {
	private String SDPATH;

	public String getSDPATH() {
		return SDPATH;
	}

	public FileUtil() {
		// 得到当前外部存储设备的目录
		// /SDCARD
		SDPATH = Environment.getExternalStorageDirectory() + "/";
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(SDPATH + dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 判断SD卡上的文件夹是否存在
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		return file.exists();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 */
	public File write2SDFromInput(String path, String fileName, InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			creatSDDir(path);
			file = creatSDFile(path + fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4 * 1024];
			while ((input.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 根据InputStream流,转换为字符串数组
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<String> readBook(InputStream in) throws IOException {
		ArrayList<String> bookContents = new ArrayList<String>();
		byte[] bytes = new byte[600];
		int len = -1;
		while ((len = in.read(bytes)) != -1) {
			String text = new String(bytes, "GB2312");
			bookContents.add(text);
		}
		in.close();
		return bookContents;
	}
	
	
	/** 
	 * 检查是否为合法的文件名，或者是否为路径 
	 */
	public static boolean isValidFileOrDir(File file) {
		if (file.isDirectory()) {
			return false;
		} else {
			//String fileName = file.getName().toLowerCase();
			Locale defloc = Locale.getDefault();
			String fileName = file.getName().toLowerCase(defloc);
			if (fileName.endsWith(".txt")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 把文件A的内容，写入文件B中
	 * @param fromFile
	 * @param toFile
	 * @throws IOException
	 */
	public static void saveAToB(File fromFile, File toFile) throws IOException {
		InputStream fis = new FileInputStream(fromFile);
		BufferedInputStream in = new BufferedInputStream(fis);
		OutputStream fos = new FileOutputStream(toFile);
		BufferedOutputStream out = new BufferedOutputStream(fos);

		int len = -1;
		byte[] bytes = new byte[1024];
		while ((len = in.read(bytes)) != -1) {
			out.write(bytes, 0, len);
		}
		out.close();
		fos.close();
		in.close();
		fis.close();
	}
	
	
}
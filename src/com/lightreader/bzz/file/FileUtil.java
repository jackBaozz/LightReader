package com.lightreader.bzz.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lightreader.bzz.pojo.FileInfo;
import com.lightreader.bzz.utils.Constant;

public class FileUtil {
	private String SDPATH;

	public void setSDPATH(String sDPATH) {
		SDPATH = sDPATH;
	}

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
	
	
	
	/** 获取SD路径 **/
	public static String getSDPath() {
		// 判断sd卡是否存在
		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return sdDir.getPath();
		}
		return "/sdcard";
	}

	/** 获取文件信息 **/
	public static FileInfo getFileInfo(File file) {
		FileInfo info = new FileInfo();
		info.setName(file.getName());
		info.setIsDirectory(file.isDirectory());
		calcFileContent(info, file);
		return info;
	}

	/** 计算文件内容 **/
	private static void calcFileContent(FileInfo fileInfo, File file) {
		if (file.isFile()) {
			//info.Size += f.length();
			fileInfo.setSize(fileInfo.getSize()+file.length());
		}
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; ++i) {
					File tmp = files[i];
					if (tmp.isDirectory()) {
						//info.FolderCount++;
						int flag = fileInfo.getFolderCount();
						fileInfo.setFolderCount(flag++);
					} else if (tmp.isFile()) {
						//info.FileCount++;
						int flag = fileInfo.getFileCount();
						fileInfo.setFileCount(flag++);
					}
					// 超过一万不计算
					/*if (info.FileCount + info.FolderCount >= 10000) { 
						break;
					}*/
					if(fileInfo.getFileCount() + fileInfo.getFolderCount() >= 10000){
						break;
					}
					calcFileContent(fileInfo, tmp);
				}
			}
		}
	}

	/** 转换文件大小 **/
	public static String formetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = fileS + " B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + " K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + " M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + " G";
		}
		return fileSizeString;
	}

	/** 合并路径 **/
	public static String combinPath(String path, String fileName) {
		return path + (path.endsWith(File.separator) ? "" : File.separator) + fileName;
	}

	/** 复制文件 **/
	public static boolean copyFile(File src, File tar) throws Exception {
		if (src.isFile()) {
			InputStream is = new FileInputStream(src);
			OutputStream op = new FileOutputStream(tar);
			BufferedInputStream bis = new BufferedInputStream(is);
			BufferedOutputStream bos = new BufferedOutputStream(op);
			byte[] bt = new byte[1024 * 8];
			int len = bis.read(bt);
			while (len != -1) {
				bos.write(bt, 0, len);
				len = bis.read(bt);
			}
			bis.close();
			bos.close();
		}
		if (src.isDirectory()) {
			File[] f = src.listFiles();
			tar.mkdir();
			for (int i = 0; i < f.length; i++) {
				copyFile(f[i].getAbsoluteFile(), new File(tar.getAbsoluteFile() + File.separator
						+ f[i].getName()));
			}
		}
		return true;
	}

	/** 移动文件 **/
	public static boolean moveFile(File src, File tar) throws Exception {
		if (copyFile(src, tar)) {
			deleteFile(src);
			return true;
		}
		return false;
	}

	/** 删除文件 **/
	public static void deleteFile(File f) {
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null && files.length > 0) {
				for (int i = 0; i < files.length; ++i) {
					deleteFile(files[i]);
				}
			}
		}
		f.delete();
	}

	/** 获取MIME类型 **/
	public static String getMIMEType(String name) {
		String type = "";
		String end = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
		if (end.equals("apk")) {
			return "application/vnd.android.package-archive";
		} else if (end.equals("mp4") || end.equals("avi") || end.equals("3gp")
				|| end.equals("rmvb")) {
			type = "video";
		} else if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf")
				|| end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("txt") || end.equals("log")) {
			type = "text";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}
	
	
	/**
	 * 获取一个文件夹下的所有文件
	 * @param activity
	 * @param path
	 * @return
	 */
	public static ArrayList<FileInfo> getFiles(Activity activity, String path) {
		File f = new File(path);
		File[] files = f.listFiles();
		if (files == null) {
			Toast.makeText(activity.getBaseContext(),String.format("无法打开: %1$s", path),Toast.LENGTH_SHORT).show();
			return null;
		}
		ArrayList<FileInfo> fileList = new ArrayList<FileInfo>();
		// 获取文件列表
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			FileInfo fileInfo = new FileInfo();
			fileInfo.setName(file.getName());
			fileInfo.setIsDirectory(file.isDirectory());
			fileInfo.setPath(file.getPath());
			fileInfo.setSize(file.length());
			fileList.add(fileInfo);
		}
		// 排序
		Collections.sort(fileList, new FileComparator());
		return fileList;
	}

	/**
	 * 创建新文件夹
	 * @param activity
	 * @param path
	 * @param handler
	 */
	public static void createDir(final Activity activity, final String path, final Handler handler,final int layoutResource,String inputDirName) {
		final String newName = inputDirName;
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		View layout = LayoutInflater.from(activity).inflate(layoutResource, null);
		builder.setView(layout);
		builder.setPositiveButton(Constant.STRING_FILE_OK, new OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {
				if (newName.length() == 0) {
					Toast.makeText(activity, Constant.STRING_FILE_NAME_CANNOT_EMPTY, Toast.LENGTH_SHORT).show();
					return;
				}
				String fullFileName = FileUtil.combinPath(path, newName);
				File newFile = new File(fullFileName);
				if (newFile.exists()) {
					Toast.makeText(activity, Constant.STRING_FILE_EXISTS, Toast.LENGTH_SHORT).show();
				} else {
					try {
						if (newFile.mkdir()) {
							handler.sendEmptyMessage(0); // 创建成功 what=0
						} else {
							Toast.makeText(activity, Constant.STRING_FILE_CREATE_FAIL, Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						Log.e("create dir error : ",e.getLocalizedMessage());
						Toast.makeText(activity, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		}).setNegativeButton(Constant.STRING_FILE_CANCEL, null);
		AlertDialog alertDialog = builder.create();
		alertDialog.setTitle(Constant.STRING_MKDIR);
		alertDialog.show();
	}

	
	/**
	 * 重命名文件
	 * @param activity
	 * @param file
	 * @param handler
	 */
	public static void renameFile(final Activity activity, final File file, final Handler handler,
			final int layoutResource,final int editTextResource) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		View layout = LayoutInflater.from(activity).inflate(layoutResource, null);
		final EditText text = (EditText) layout.findViewById(editTextResource);
		/*if(file.isDirectory()){//选中的文件是一个文件夹
			text.setText(file.getName());
		}else{//选中的文件是一个文件
			int flag = file.getName().lastIndexOf('.');
			String fileName = file.getName().substring(0, flag);
			text.setText(fileName);//放入截取后缀名的文件名
		}*/
		text.setText(file.getName());
		builder.setView(layout);
		builder.setPositiveButton(Constant.STRING_FILE_OK, new OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {
				String path = file.getParentFile().getPath();
				String newName = text.getText().toString().trim();
				if (newName.equalsIgnoreCase(file.getName())) {//相等则忽略
					return;
				}
				if (newName.length() == 0) {
					Toast.makeText(activity, Constant.STRING_FILE_NAME_CANNOT_EMPTY, Toast.LENGTH_SHORT).show();
					return;
				}
				String fullFileName = FileUtil.combinPath(path, newName);

				File newFile = new File(fullFileName);
				if (newFile.exists()) {
					Toast.makeText(activity, Constant.STRING_FILE_EXISTS, Toast.LENGTH_SHORT).show();
				} else {
					try {
						if (file.renameTo(newFile)) {
							handler.sendEmptyMessage(0); // 成功 what=0
						} else {
							Toast.makeText(activity, Constant.STRING_FILE_RENAME_FIAL, Toast.LENGTH_SHORT).show();
						}
					} catch (Exception e) {
						Log.e("rename file error : ",e.getLocalizedMessage());
						Toast.makeText(activity, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
					}
				}
			}
		}).setNegativeButton(Constant.STRING_FILE_CANCEL, null);
		AlertDialog alertDialog = builder.create();
		alertDialog.setTitle(Constant.STRING_FILE_RENAME);
		alertDialog.show();
	}

	/**
	 * 查看文件详情(现有layout.xml布局下)
	 * @param activity
	 * @param file
	 */
	public static void viewFileInfo(final Activity activity, File file,FileInfo inputFileInfo,
			final int layoutResource,
			final int fileNameResource,
			final int fileLastModifiedResource,
			final int fileSizeResource,
			final int fileContentsResource,
			final int fileCompetenceResource
			) {
		FileInfo fileInfo = FileUtil.getFileInfo(file);
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		//设置与布局上有几个控件密切相关------注意!!!!!
		View layout = LayoutInflater.from(activity).inflate(layoutResource, null);
		((TextView) layout.findViewById(fileNameResource)).setText(file.getName());
		((TextView) layout.findViewById(fileLastModifiedResource)).setText(new Date(file.lastModified()).toLocaleString());
		((TextView) layout.findViewById(fileSizeResource)).setText(FileUtil.formetFileSize(fileInfo.getSize()));
		if (file.isDirectory()) {
			((TextView) layout.findViewById(fileContentsResource)).setText("文件夹  "+ inputFileInfo.getFolderCount() + ", 文件  " + inputFileInfo.getFileCount());
		}
		((TextView) layout.findViewById(fileCompetenceResource)).setText(inputFileInfo.getFileCompetence());
		builder.setView(layout);
		builder.setPositiveButton(Constant.STRING_FILE_OK, new OnClickListener() {
			public void onClick(DialogInterface dialoginterface, int i) {
				dialoginterface.cancel();
			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.setTitle(Constant.STRING_FILE_DETAILS);
		alertDialog.show();
	}
	
}

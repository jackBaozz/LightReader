package com.lightreader.bzz.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.lightreader.bzz.Activity.R;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.util.Log;



public class SystemTools {
	
	private static final String TAG = "SystemTools";
	
	/**
	 * 一、内存（ram）： android的总内存大小信息存放在系统的/proc/meminfo文件里面，可以通过读取这个文件来获取这些信息： 
	 */
	public void getTotalMemory() {
		String str1 = "/proc/meminfo";
		String str2="";
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			while ((str2 = localBufferedReader.readLine()) != null) {
				Log.i(TAG, "---" + str2);
			}
		} catch (IOException e) {
		}
	}
	
	
	/**
	 * 获取当前剩余内存(ram)大小的方法： 
	 * @return
	 */
	public long getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);
		return mi.availMem;
	}


	
	
	
	/**
	 * 二、Rom大小
	 * @return
	 */
	public long[] getRomMemroy() {
		long[] romInfo = new long[2];
		//Total rom memory
		romInfo[0] = getTotalInternalMemorySize();

		//Available rom memory
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		romInfo[1] = blockSize * availableBlocks;
		getVersion();
		return romInfo;
	}

	public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	
	
	/**
	 * 三、sdCard大小 
	 * @return
	 */
	public long[] getSDCardMemory() {
		long[] sdCardInfo=new long[2];
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long bSize = sf.getBlockSize();
			long bCount = sf.getBlockCount();
			long availBlocks = sf.getAvailableBlocks();

			sdCardInfo[0] = bSize * bCount;//总大小
			sdCardInfo[1] = bSize * availBlocks;//可用大小
		}
		return sdCardInfo;
	}

	
	
	
	/**
	 * 四、电池电量
	 * 然后在activity的onCreate()方法中注册
	 * registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
	 */
	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra("level", 0);
			// level加%就是当前电量了
		}
	};

	
	
	/**
	 * 五、CPU信息
	 * /proc/cpuinfo文件中第一行是CPU的型号，第二行是CPU的频率，可以通过读文件，读取这些数据！ 
	 * @return 
	 * 
	 */
	public String[] getCpuInfo() {
		String str1 = "/proc/cpuinfo";
		String str2="";
		String[] cpuInfo={"",""};
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (IOException e) {
		}
		return cpuInfo;
	}

	
	/**
	 * 六、系统的版本信息：  版本信息里面还包括型号等信息。 
	 */
	public String[] getVersion(){
		String[] version={"null","null","null","null"};
		String str1 = "/proc/version";
		String str2;
		String[] arrayOfString;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			version[0]=arrayOfString[2];//KernelVersion
			localBufferedReader.close();
		} catch (IOException e) {
		}
		version[1] = Build.VERSION.RELEASE;// firmware version
		version[2]=Build.MODEL;//model
		version[3]=Build.DISPLAY;//system version
		return version;
	}

	
	
	/**
	 * 七、MAC地址和开机时间：
	 * @return
	 */
	public String[] getOtherInfo(Context context){
		String[] other={"null","null"};
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if(wifiInfo.getMacAddress()!=null){
        	other[0]=wifiInfo.getMacAddress();
		} else {
			other[0] = "Fail";
		}
		other[1] = getTimes(context);
        return other;
	}
	
	/**
	 * 开机时间
	 * @return
	 */
	private String getTimes(Context context) {
		long ut = SystemClock.elapsedRealtime() / 1000;
		if (ut == 0) {
			ut = 1;
		}
		int m = (int) ((ut / 60) % 60);
		int h = (int) ((ut / 3600));
		return h + " " + context.getString(R.string.info_times_hour) + m + " " + context.getString(R.string.info_times_minute);
	}

	
	
	/**
	 * 初始化数据 保留两位小数。
	 * @param size
	 * @return
	 */
	public String formatSize(long size) {
		String suffix = null;
		float fSize=0;

		if (size >= 1024) {
			suffix = "KB";
			fSize=size / 1024;
			if (fSize >= 1024) {
				suffix = "MB";
				fSize /= 1024;
			}
			if (fSize >= 1024) {
				suffix = "GB";
				fSize /= 1024;
			}
		} else {
			fSize = size;
		}
		java.text.DecimalFormat df = new java.text.DecimalFormat("#0.00");
		StringBuilder resultBuffer = new StringBuilder(df.format(fSize));
		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}

	
	
}

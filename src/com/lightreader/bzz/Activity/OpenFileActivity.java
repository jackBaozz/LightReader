package com.lightreader.bzz.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class OpenFileActivity extends Activity {

	private String filenameString;
	private static final String gb2312 = "GB2312";
	private static final String utf8 = "UTF-8";
	//private static final String defaultCode = gb2312;
	private static final String defaultCode = "UTF-8";
	private String text;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filebrowser);

		try {
			Intent intent = getIntent();
			if (intent.getExtras().getString("fileName") != null) {
				filenameString = getIntent().getExtras().getString("fileName");
				refreshGUI(utf8);
			}else if(intent.getExtras().getSerializable("text")!=null){
				text = (String) intent.getExtras().getSerializable("text");
				readAsset(text);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = this.getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.gb2312:
			refreshGUI(defaultCode);
			break;
		case R.id.utf8:
			refreshGUI(utf8);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	private void refreshGUI(String code) {
		TextView tv = (TextView) findViewById(R.id.view_contents);
		String fileContent = getStringFromFile(code);
		tv.setText(fileContent);
	}
	
	private void readAsset(String text){
		TextView tv = (TextView) findViewById(R.id.view_contents);
		tv.setText(text);
	}

	public String getStringFromFile(String code) {
		try {
			StringBuffer sBuffer = new StringBuffer();
			FileInputStream fInputStream = new FileInputStream(filenameString);
			InputStreamReader inputStreamReader = new InputStreamReader(
					fInputStream, code);
			BufferedReader in = new BufferedReader(inputStreamReader);
			if (!new File(filenameString).exists()) {
				return null;
			}
			while (in.ready()) {
				sBuffer.append(in.readLine() + "\n");
			}
			in.close();
			return sBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

//	public String getInputStream(String code) {
//
//		StringBuffer sBuffer = new StringBuffer();
//		InputStreamReader inputStreamReader;
//		try {
//			inputStreamReader = new InputStreamReader(in, code);
//			BufferedReader br = new BufferedReader(inputStreamReader);
//			while (br.ready()) {
//				sBuffer.append(br.readLine() + "\n");
//			}
//			br.close();
//			return sBuffer.toString();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//
//	}

//	// 读取文件内容
//	public byte[] readFile(String fileName) throws Exception {
//		byte[] result = null;
//		FileInputStream fis = null;
//		try {
//			File file = new File(fileName);
//			fis = new FileInputStream(file);
//			result = new byte[fis.available()];
//			fis.read(result);
//		} catch (Exception e) {
//		} finally {
//			fis.close();
//		}
//		return result;
//	}

}

package com.lightreader.bzz.Activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import com.lightreader.bzz.Adapter.FileListAdapter;
import com.lightreader.bzz.file.FileUtil;
import com.lightreader.bzz.pojo.FileItem;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FileBrowserActivity extends Activity implements android.view.View.OnClickListener {
	private ListView lvFiles;
	private ArrayList<FileItem> items;
	private FileListAdapter adapter;
	private File current_dir;
	private static final int MENUCRETEDIR = 1;
	private static final int MENUEXIT = 2;
	private static final int MENUCOPY = 3;
	private static final int MENUDELETE = 4;
	private static final int MENUPASTE = 5;
	private static final int MENUREAD = 6;
	private Builder builder;
	private File selectedFile;
	private TextView tvTitle;
	private Bundle bundle;
	private File clickedFile; 

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filelist);
		init();
	}

	@Override
	public void onClick(View v) {
		
	}
	
	

	private void init() {
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		lvFiles = (ListView) findViewById(android.R.id.list);
		browseTo(new File("/mnt/sdcard"));
		adapter = new FileListAdapter(this, items);
		lvFiles.setAdapter(adapter);
		lvFiles.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {
				// 获取被单击的item的对象
				FileItem fi = (FileItem) adapter.getItem(position);
				String fileName = fi.getFileName();
				File file = new File(current_dir, fileName);
				if (FileUtil.isValidFileOrDir(file)) {
					Intent intent = new Intent(FileBrowserActivity.this,OpenFileActivity.class);
					bundle = new Bundle();
					bundle.putString("fileName", file.getAbsolutePath());
					intent.putExtras(bundle);
					startActivityForResult(intent, 0);
				} else {
					browseTo(new File(current_dir, fi.getFileName()));
				}
			}

		});

		// lvFiles创建上下文菜单事件
		lvFiles.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// 获取事件源的position
				int position = ((AdapterContextMenuInfo) menuInfo).position;
				// 根据position获取事件源对应的文件名
				String fileName = ((FileItem) adapter.getItem(position))
						.getFileName();
				// 根据当前目录和文件名构建一个文件对象
				File clickedFile = new File(current_dir, fileName);
				// 如果该文件对象是一个文件，向上下文菜单添加两个菜单项
				// 如果该文件对象是一个目录，且之前执行过复制动作（selectedFile不为null），则向菜单中添加一个菜单项
				menu.add(0, MENUDELETE, 3, "删除");
				if (clickedFile.isFile()) {
					menu.add(0,MENUREAD,1,"翻书效果阅读");
					menu.add(0, MENUCOPY, 2, "复制");
				}
				if (clickedFile.isDirectory() && selectedFile != null) {
					menu.add(0, MENUPASTE, 4, "粘贴");
				}

			}
		});
	}

	// 上下文菜单的单击事件
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// 获取事件源对应的position
		int position = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
		// 获取事件源对应的文件名
		String fileName = ((FileItem) adapter.getItem(position)).getFileName();
		clickedFile = new File(current_dir, fileName);
		switch (item.getItemId()) {
		case MENUDELETE:
			// 如果单击的是delete，调用clickedFile对象的delete方法
			AlertDialog.Builder mDialog = new AlertDialog.Builder(
					FileBrowserActivity.this);
			mDialog.setTitle("删除");
			mDialog.setIcon(android.R.drawable.ic_delete);
			mDialog.setMessage("是否删除");
			mDialog.setPositiveButton("确定",new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							clickedFile.delete();
							browseTo(current_dir);
							
						}
					});
			mDialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
						}
					});
			mDialog.show();

			break;
			
		case MENUREAD:
			Intent intent = new Intent(FileBrowserActivity.this, ReadBookActivity.class);
			String name = clickedFile.getAbsolutePath();
			FileInputStream in;
			try {
				in = new FileInputStream(name);
				ArrayList<String> bookContents = FileUtil.readBook(in);//读取出文本所有内容
				intent.putStringArrayListExtra("texts", bookContents);
				FileBrowserActivity.this.startActivity(intent);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		case MENUCOPY:
			// 如果单击的是copy，则将clickedFile对象，设置为选中对象（selectedFile）
			selectedFile = clickedFile;
			break;
		case MENUPASTE:
			// 如果单击的是粘贴，则调用工具类的保存方法，从selectedFile向toFile复制
			File toFile = new File(clickedFile, selectedFile.getName());
			try {
				FileUtil.saveAToB(selectedFile, toFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
		browseTo(current_dir);
		return super.onContextItemSelected(item);
	}

	// 浏览根目录
	private void browseRoot() {
		browseTo(new File("/mnt/sdcard"));
	}

	// 浏览上级目录
	private void browseUpLevel() {
		if (current_dir.getParent() != null) {
			browseTo(current_dir.getParentFile());
		}
	}

	// 浏览指定目录
	private void browseTo(File dir) {
		// 如果dir对象是一个目录
		if (dir.isDirectory()) {
			// 改变标题栏的标题
			tvTitle.setText(dir.getAbsolutePath());
			// 更改当前目录为指定目录
			this.current_dir = dir;
			// 查找dir目录中的所有子目录和文件 填充到items集合
			fill(current_dir.listFiles());
		}
	}

	private void fill(File[] files) {
		// 如果items未初始化则初始化
		if (items == null) {
			items = new ArrayList<FileItem>();
		}
		// 清空items中所存储的原目录信息
		items.clear();
		Resources res = getResources();

		// 如果当前目录有父目录，则添加 返回根目录和返回上级目录
		/*
		if (current_dir.getParent() != null) {
			// 返回根目录的item
			items.add(new FileItem(getString(R.string.root_dir), res.getDrawable(R.drawable.goroot)));
			items.add(new FileItem(getString(R.string.upLevel_dir), res.getDrawable(R.drawable.uponelevel)));
		}
		items.add(new FileItem(getString(R.string.current_dir), res.getDrawable(R.drawable.folder)));
		*/

		if (files != null) {
			// 遍历当前目录中的所有文件和子目录
			for (File file : files) {
				// 获取文件名
				String fileName = file.getName();
				// 获取文件或目录的图标
				Drawable icon = null;
				// 如果是目录，则图标为folder
				if (file.isDirectory()) {
					icon = res.getDrawable(R.drawable.folder);
				} else {
					// 如果测试为音频文件，设置图标
					if (checkEnd(fileName,
							res.getStringArray(R.array.audioFile))) {
						icon = res.getDrawable(R.drawable.audio);
					}
					// 如果测试为图片文件，设置图标
					else if (checkEnd(fileName,
							res.getStringArray(R.array.imageFile))) {
						icon = res.getDrawable(R.drawable.image);
					} else if (checkEnd(fileName,
							res.getStringArray(R.array.packageFile))) {
						icon = res.getDrawable(R.drawable.packed);
					} else if (checkEnd(fileName,
							res.getStringArray(R.array.webFile))) {
						icon = res.getDrawable(R.drawable.webtext);
					} else {
						icon = res.getDrawable(R.drawable.text);
					}
				}
				// 创建fileitem对象，并添加到集合
				FileItem item = new FileItem(fileName, icon);
				items.add(item);
			}
		}
		// 如果adapter不为null 则通知更新界面
		if (adapter != null)
			adapter.dataChanged(items);
	}

	// 检查指定文件名 是否以数组中指定的扩展名结尾
	private boolean checkEnd(String fileName, String[] endType) {
		if (fileName != null && endType != null) {
			for (String end : endType) {
				if (fileName.endsWith(end))
					return true;
			}
		}
		return false;
	}


	// 创建系统菜单
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
	}*/
	
	// 创建系统菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 向menu中添加menuitem
		menu.add(0, MENUCRETEDIR, 1, "创建文件夹").setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, MENUEXIT, 2, "取消").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}
	

	// 系统菜单的单击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final EditText etInput = new EditText(this);
		switch (item.getItemId()) {
		case MENUEXIT:
			break;
		case MENUCRETEDIR:
			// 如果单击创建文件夹，弹出对话框
			builder = new Builder(this);
			builder.setTitle("输入")// 设置对话框标题
					.setMessage("请输入新文件夹的名称")// 设置对话框提示信息
					.setIcon(android.R.drawable.ic_menu_add)// 设置对话框图标
					.setView(etInput)// 设置对话框显示一个文本框视图
					// 设置对话框中的确定按钮
					.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String fileName = null;
							// 如果用户输入的新文件夹名称不为空
							if (etInput.getText() != null
									&& !(fileName = etInput.getText()
											.toString().trim()).equals("")) {
								// 根据当前目录 和 新文件夹名构建一个File对象
								File newDir = new File(current_dir, fileName);
								// 如果该目录不存在，则创建并更新界面
								if (!newDir.exists()) {
									newDir.mkdir();
									browseTo(current_dir);
								}

							}
						}
					}).setNegativeButton("取消", null)// 设置对话框的取消按钮
					.show();// 显示对话框
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}

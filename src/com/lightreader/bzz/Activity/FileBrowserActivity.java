package com.lightreader.bzz.Activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lightreader.bzz.Adapter.FileListAdapter;
import com.lightreader.bzz.file.FileComparator;
import com.lightreader.bzz.file.FileUtil;
import com.lightreader.bzz.pojo.FileInfo;
import com.lightreader.bzz.utils.Constant;

public class FileBrowserActivity extends BaseActivity implements android.view.View.OnClickListener {
	private ListView listViewFiles;
	private ArrayList<FileInfo> fileItemsList;
	private FileListAdapter fileListAdapter;
	private File current_dir; //当前显示的文件目录
	private Builder builder;
	private File selectedFile;
	private File clickedFile;
	private String clickedFileName = "";
	private TextView textViewTitle;
	private Bundle bundle;
	private Button btnBack, btnHome,btnMkdir,btnPaste,btnCancel;
	private String btnFlag = "";//按钮的操作类型的flag
	private ProgressDialog progressDialog;//进度条
	private TableLayout tableLayout ;//隐藏或则显示的按钮条
	private FileInfo clickedFileInfo;
	private ImageView currentImageView;//当前选中的ListView的其中一行的View的ImageView
	private View currentView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉程序名的title
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_list);
		init();
	}

	
	
	
	//初始化方法
	private void init() {
		btnBack = (Button) findViewById(R.id.back);
		btnHome = (Button) findViewById(R.id.home);
		btnMkdir = (Button) findViewById(R.id.file_list_btn_createdir);
		btnPaste = (Button) findViewById(R.id.file_list_btn_paste);
		btnCancel = (Button) findViewById(R.id.file_list_btn_cancel);
		btnBack.setOnClickListener(this);//绑定按下监听器
		btnHome.setOnClickListener(this);//绑定按下监听器
		btnMkdir.setOnClickListener(this);//绑定按下监听器
		btnPaste.setOnClickListener(this);//绑定按下监听器
		btnCancel.setOnClickListener(this);//绑定按下监听器
		tableLayout = (TableLayout)findViewById(R.id.btn_bar_bottom);//获取按钮条
		
		
		textViewTitle = (TextView) findViewById(R.id.tvTitle);
		listViewFiles = (ListView) findViewById(android.R.id.list);
		browseTo(new File(Constant.DEFAULT_SDCARD_PATH));// fileItemsList 设置值  "/mnt/sdcard"
		fileListAdapter = new FileListAdapter(FileBrowserActivity.this, fileItemsList);
		listViewFiles.setAdapter(fileListAdapter);
		//listView被点击事件
		listViewFiles.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,int position, long time) {
				// 获取被单击的item的对象
				FileInfo fileItem = (FileInfo) fileListAdapter.getItem(position);
				String fileName = fileItem.getName();
				File file = new File(current_dir, fileName);
				if (FileUtil.isValidFileOrDir(file)) {
					/*Intent intent = new Intent(FileBrowserActivity.this,OpenFileActivity.class);
					bundle = new Bundle();
					bundle.putString("fileName", file.getAbsolutePath());
					intent.putExtras(bundle);
					FileBrowserActivity.this.startActivityForResult(intent, 0);*/
					
					//如果为书本后缀,则直接添加本到书架
					if (checkEnd(fileName, getResources().getStringArray(R.array.pdfFile))){
						
					}
					
					
				} else {
					browseTo(new File(current_dir, fileItem.getName()));
				}
			}
		});

		// listViewFiles创建上下文菜单监听(文件长按事件)
		listViewFiles.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
			@Override
			public void onCreateContextMenu(ContextMenu menu, View view,ContextMenuInfo menuInfo) {
				//当前选中的ListView的其中一行的View
				currentView = view;
				// 获取事件源的position
				int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
				// 根据position获取事件源对应的文件名
				String fileName = ((FileInfo) fileListAdapter.getItem(position)).getName();
				// 根据当前目录和文件名构建一个文件对象
				File clickedFile = new File(current_dir, fileName);
				FileInfo fileInfo = FileUtil.getFileInfo(clickedFile);
				menu.setHeaderTitle(fileName);//设置标题
				// 如果该文件对象是一个文件，向上下文菜单添加两个菜单项
				// 如果该文件对象是一个目录，且之前执行过复制动作（selectedFile不为null），则向菜单中添加一个菜单项
				if (fileInfo.isIsDirectory() == false) {// 不是文件夹
					int flag = 0;
					for (String suffixString : Constant.BOOK_SUFFIX) {// 后缀名必须符合规范
						if (fileInfo.getName().endsWith(suffixString)) {
							menu.add(0, Constant.INT_MENU_ADDTOBOOKSHELF, 1, Constant.STRING_FILE_ADDTOBOOKSHELF);// 添加到书架
							menu.add(0, Constant.INT_MENU_RENAME, 2, Constant.STRING_FILE_RENAME);// 重命名
							menu.add(0, Constant.INT_MENU_COPY, 3, Constant.STRING_FILE_COPY);// 复制
							menu.add(0, Constant.INT_MENU_MOVE, 4, Constant.STRING_FILE_MOVE);// 移动
							menu.add(0, Constant.INT_MENU_DELETE, 5, Constant.STRING_FILE_DELETE);// 删除
							menu.add(0, Constant.INT_MENU_DETAILS, 6, Constant.STRING_FILE_DETAILS);// 详细
							menu.add(0, Constant.INT_MENU_CANCEL, 7, Constant.STRING_FILE_CANCEL);// 取消
							break;
						}
						flag++;
					}
					if (flag == Constant.BOOK_SUFFIX.length) {
						menu.add(0, Constant.INT_MENU_RENAME, 1, Constant.STRING_FILE_RENAME);// 重命名
						menu.add(0, Constant.INT_MENU_COPY, 2, Constant.STRING_FILE_COPY);// 复制
						menu.add(0, Constant.INT_MENU_MOVE, 3, Constant.STRING_FILE_MOVE);// 移动
						menu.add(0, Constant.INT_MENU_DELETE, 4, Constant.STRING_FILE_DELETE);// 删除
						menu.add(0, Constant.INT_MENU_DETAILS, 5, Constant.STRING_FILE_DETAILS);// 详细
						menu.add(0, Constant.INT_MENU_CANCEL, 6, Constant.STRING_FILE_CANCEL);// 取消
					}
				} else {
					menu.add(0, Constant.INT_MENU_RENAME, 1, Constant.STRING_FILE_RENAME);// 重命名
					menu.add(0, Constant.INT_MENU_COPY, 2, Constant.STRING_FILE_COPY);// 复制
					menu.add(0, Constant.INT_MENU_MOVE, 3, Constant.STRING_FILE_MOVE);// 移动
					menu.add(0, Constant.INT_MENU_DELETE, 4, Constant.STRING_FILE_DELETE);// 删除
					menu.add(0, Constant.INT_MENU_DETAILS, 5, Constant.STRING_FILE_DETAILS);// 详细
					menu.add(0, Constant.INT_MENU_CANCEL, 6, Constant.STRING_FILE_CANCEL);// 取消
				}
				
			}
		});
	}

	// 上下文菜单的单击事件(文件长按事件)
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// 获取事件源对应的position
		int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
		// 获取事件源对应的文件名
		FileInfo fileInfo = (FileInfo)fileListAdapter.getItem(position);
		String fileName = fileInfo.getName();
		clickedFile = new File(current_dir, fileName);//当前选中的文件
		clickedFileName = fileName;//当前选中的文件名字
		switch (item.getItemId()) {
		case Constant.INT_MENU_ADDTOBOOKSHELF:
			//添加书本到书架
			//currentView.setImageResource(R.drawable.btn_check_buttonless_on);//勾选状态
			View view = fileListAdapter.getView(position,currentView,null);
			//fileListAdapter.notifyDataSetChanged();// 通知数据改变
			TextView tv = (TextView) view.findViewById(R.id.tvFileName);
			CharSequence cs = tv.getText();
			Toast.makeText(this, cs.toString(), Toast.LENGTH_SHORT).show();
			break;
		case Constant.INT_MENU_RENAME:
			//重命名
			FileUtil.renameFile(this, clickedFile, fileCallBackHandler, R.layout.file_rename, R.id.file_name);
			break;
		case Constant.INT_MENU_DELETE:
			// 如果单击的是delete，调用clickedFile对象的delete方法
			AlertDialog.Builder mDialog = new AlertDialog.Builder(FileBrowserActivity.this);
			mDialog.setTitle(Constant.STRING_FILE_DELETE);//删除
			mDialog.setIcon(android.R.drawable.ic_delete);
			mDialog.setMessage(Constant.STRING_FILE_ISORNOT_DELETE);//确认是否删除
			mDialog.setPositiveButton(Constant.STRING_FILE_OK, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					clickedFile.delete();
					browseTo(current_dir);
				}
			});
			mDialog.setNegativeButton(Constant.STRING_FILE_CANCEL, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {}
			});
			mDialog.show();
			break;
		case Constant.INT_MENU_READ:
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
		case Constant.INT_MENU_COPY:
			// 如果单击的是copy，则将clickedFile对象，设置为选中对象（selectedFile）
			tableLayout.setVisibility(View.VISIBLE);
			btnFlag = "COPY";
			/*
			Intent copyIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("CURRENTPASTEFILEPATH", clickedFile.getPath());
			bundle.putString("ACTION", "CPOY");
			copyIntent.putExtras(bundle);
			copyIntent.setClass(FileBrowserActivity.this, null);
			FileBrowserActivity.this.startActivityForResult(copyIntent, 0);
			*/
			//selectedFile = clickedFile;
			break;
		case Constant.INT_MENU_MOVE:
			btnPaste.setText(Constant.STRING_FILE_MOVE);//移动
			tableLayout.setVisibility(View.VISIBLE);
			btnFlag = "MOVE";
			break;
		case Constant.INT_MENU_PASTE:
			// 如果单击的是粘贴，则调用工具类的保存方法，从selectedFile向toFile复制
			File toFile = new File(clickedFile, selectedFile.getName());
			try {
				FileUtil.saveAToB(selectedFile, toFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case Constant.INT_MENU_DETAILS:
			String fileCompetence = "";
			int fileCount = 0;
			int folderCount = 0;
			//获取文件的系统权限 fileCompetence
			BufferedReader buffer = null;
			String line = "";
			Process p = null;
			try {
				p = Runtime.getRuntime().exec(new String[] { "ls","-ld",clickedFile.getAbsolutePath() });
				buffer = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = buffer.readLine()) != null) {
					line = line.substring(0, 10);
					fileCompetence = line;
				}
				
				//没wc统计的命令
				//p = Runtime.getRuntime().exec(new String[] { "ls -l /mnt/sdcard | grep \"^-\" | wc -l" });
				p = Runtime.getRuntime().exec(new String[] { "ls","-l",clickedFile.getAbsolutePath() });
				buffer = null;
				buffer = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = buffer.readLine()) != null) {
					line = line.substring(0, 1);
					if(line.equals("-")){
						fileCount++;
					}else if(line.equals("d")){
						folderCount++;
					}else{
						
					}
					line = "";
				}
				
				//修改文件权限使用下面代码
				/*
				int status = p.waitFor();     
				if (status == 0) {//chmod succeed      
				} else { //chmod failed    
				}*/
			} catch (IOException e) {
				Log.e("FileBrowserActivity.fill.IOException", Constant.STRING_FILE_CHMOD_FAIL, e);
			//} catch (InterruptedException e) {
				//Log.e("FileBrowserActivity.fill.InterruptedException", Constant.STRING_FILE_CHMOD_FAIL, e);
			} finally {
			    if (p != null) {
			        try {
						p.getOutputStream().close();
						p.getInputStream().close();
				        p.getErrorStream().close(); 
					} catch (Exception e) {
						e.printStackTrace();
					}
			    }
			}
			fileInfo.setFileCompetence(fileCompetence);//赋值文件权限字符串
			fileInfo.setFileCount(fileCount);
			fileInfo.setFolderCount(folderCount);
			//文件详细信息
			FileUtil.viewFileInfo(FileBrowserActivity.this,clickedFile,fileInfo,R.layout.file_info,
					R.id.file_info_name,R.id.file_info_lastmodified,R.id.file_info_size,R.id.file_info_contents,R.id.file_info_competence);
			break;
		case Constant.INT_MENU_CANCEL:
			break;
		}
		browseTo(current_dir);
		return super.onContextItemSelected(item);
	}

	
	
	// 按钮事件的监听器
	@Override
	public void onClick(View v) {
		final EditText etInput = new EditText(this);
		switch (v.getId()) {
		case R.id.back:
			browseUpLevel();
			break;
		case R.id.home:
			browseRoot();
			break;
		case R.id.file_list_btn_createdir:
			// 如果单击创建文件夹，弹出对话框
			builder = new Builder(this);
			builder.setTitle(Constant.STRING_FILE_MKDIR)// 设置对话框标题
					.setMessage(Constant.STRING_FILE_PLEASE_INPUT_NAME)// 设置对话框提示信息
					.setIcon(android.R.drawable.ic_menu_add)// 设置对话框图标
					.setView(etInput)// 设置对话框显示一个文本框视图
					// 设置对话框中的确定按钮
					.setPositiveButton(Constant.STRING_FILE_OK, new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String fileName = null;
							// 如果用户输入的新文件夹名称不为空
							if (etInput.getText() != null && !(fileName = etInput.getText().toString().trim()).equals("")) {
								// 根据当前目录 和 新文件夹名构建一个File对象
								File newDir = new File(current_dir, fileName);
								// 如果该目录不存在，则创建并更新界面
								if (!newDir.exists()) {
									newDir.mkdir();
									browseTo(current_dir);
								}
							}
						}
					}).setNegativeButton(Constant.STRING_FILE_CANCEL, null)// 设置对话框的取消按钮
					.show();// 显示对话框
			break;
		case R.id.file_list_btn_paste:
			final File src = clickedFile;
			if (!src.exists()) {
				Toast.makeText(getApplicationContext(), Constant.STRING_FILE_NOT_EXISTS, Toast.LENGTH_SHORT).show();
				return;
			}
			String newPath = FileUtil.combinPath(current_dir.getAbsolutePath(), clickedFileName);
			final File tarFile = new File(newPath);
			if (tarFile.exists()) {
				Toast.makeText(getApplicationContext(), Constant.STRING_FILE_EXISTS, Toast.LENGTH_SHORT).show();
				return;
			}

			//进度条
			progressDialog = ProgressDialog.show(FileBrowserActivity.this, "", "Please wait......", true, false);

			new Thread() {
				@Override
				public void run() {
					if (btnFlag.equals("MOVE")) {
						// 移动文件
						try {
							FileUtil.moveFile(src, tarFile);
						} catch (Exception e) {
							Log.e("FileBrowserActivity.onClick", Constant.STRING_FILE_MOVE_FAIL, e);
							Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
						}
					} else {
						// 复制文件
						try {
							FileUtil.copyFile(src, tarFile);
						} catch (Exception e) {
							Log.e("FileBrowserActivity.onClick",  Constant.STRING_FILE_COPY_FAIL, e);
							Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
						}
					}
					progressHandler.sendEmptyMessage(0);
				}
			}.start();
			break;
		case R.id.file_list_btn_cancel:
			tableLayout.setVisibility(View.GONE);
			break;
		}
	}
	
	/**
	 * 用Handler来更新进度条UI
	 */
	private Handler progressHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 关闭ProgressDialog
			progressDialog.dismiss();//关闭进度条
			browseCurrent();//刷新文件目录
			tableLayout.setVisibility(View.GONE);//关闭按钮条
		}
	};
	
	
	// 浏览根目录
	private void browseRoot() {
		browseTo(new File(Constant.DEFAULT_SDCARD_PATH));
	}

	// 浏览上级目录
	private void browseUpLevel() {
		if (current_dir.getParent() != null) {
			browseTo(current_dir.getParentFile());
		}
	}
	
	// 浏览本级目录
	private void browseCurrent() {
		if (current_dir != null) {
			browseTo(current_dir);
		}else{
			//本级指针为空,则指向默认的路径
			browseTo(new File(Constant.DEFAULT_SDCARD_PATH));
		}
	}
	
	// 浏览指定目录
	private void browseTo(File dir) {
		// 如果dir对象是一个目录
		if (dir.isDirectory()) {
			// 改变标题栏的路径文字
			textViewTitle.setText(dir.getAbsolutePath());
			// 更改当前目录为指定目录
			this.current_dir = dir;
			// 查找dir目录中的所有子目录和文件 填充到items集合
			fill(current_dir.listFiles());//获取所有文件列表集合fileItemsList
		}
		Collections.sort(fileItemsList, new FileComparator());// 对文件夹进行排序
		//Collections.reverse(fileItemsList);//倒序排序
	}

	
	/**
	 * 往(fileItems) ArrayList<FileItem>数组里添加文件列表
	 * @param files
	 */
	private void fill(File[] files) {
		// 如果items未初始化则初始化
		if (fileItemsList == null) {
			fileItemsList = new ArrayList<FileInfo>();
		}
		// 清空items中所存储的原目录信息
		fileItemsList.clear();
		Resources res = getResources();

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
					if (checkEnd(fileName, res.getStringArray(R.array.audioFile))) {
						icon = res.getDrawable(R.drawable.audio);// 音频图标
					} else if (checkEnd(fileName, res.getStringArray(R.array.imageFile))) {
						icon = res.getDrawable(R.drawable.image);// 图片图标
					} else if (checkEnd(fileName, res.getStringArray(R.array.apkFile))) {
						icon = res.getDrawable(R.drawable.apk);// 安装包图标
					} else if (checkEnd(fileName, res.getStringArray(R.array.packageFile))) {
						icon = res.getDrawable(R.drawable.rar);// 安装包图标
					} else if (checkEnd(fileName, res.getStringArray(R.array.webFile))) {
						icon = res.getDrawable(R.drawable.html);// 网络文本的图标
					} else if (checkEnd(fileName, res.getStringArray(R.array.txtFile))) {
						icon = res.getDrawable(R.drawable.text);// 一般文本图标
					} else if (checkEnd(fileName, res.getStringArray(R.array.pdfFile))) {
						icon = res.getDrawable(R.drawable.pdf);// pdf图标
					} else if (checkEnd(fileName, res.getStringArray(R.array.epubFile))) {
						icon = res.getDrawable(R.drawable.epub);// epub图标
					} else if (checkEnd(fileName, res.getStringArray(R.array.videoFile))) {
						icon = res.getDrawable(R.drawable.video);// 视频图标
					} else if (checkEnd(fileName, res.getStringArray(R.array.wordFile))) {
						icon = res.getDrawable(R.drawable.word);// word图标
					} else if (checkEnd(fileName, res.getStringArray(R.array.powerpointFile))) {
						icon = res.getDrawable(R.drawable.powerpoint);// ppt图标
					} else if (checkEnd(fileName, res.getStringArray(R.array.excelFile))) {
						icon = res.getDrawable(R.drawable.excel);// excel图标
					} else if (checkEnd(fileName, res.getStringArray(R.array.databaseFile))) {
						icon = res.getDrawable(R.drawable.database);// db图标
					} else {
						icon = res.getDrawable(R.drawable.unknown);//unknow file
					}
				}
				// 创建fileitem对象，并添加到集合
				//FileInfo item = new FileInfo(fileName, icon);
				FileInfo item = new FileInfo(fileName, file.getAbsolutePath(),icon,file.length(),file.isDirectory());
				fileItemsList.add(item);
			}
		}
		// 如果adapter不为null 则通知更新界面
		if (fileListAdapter != null){
			fileListAdapter.dataChanged(fileItemsList);
		}
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
		return true;
	}*/
	
	// 创建MENU菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// 向menu中添加menuitem
		//getMenuInflater().inflate(R.menu.menu, menu);
		menu.add(0, Constant.INT_MENU_BACK, 1, Constant.STRING_FILE_BACK_UPLEVEL).setIcon(android.R.drawable.ic_menu_revert);
		menu.add(0, Constant.INT_MENU_BACKHOME, 2, Constant.STRING_FILE_SDCARD_LIST).setIcon(android.R.drawable.ic_menu_myplaces);
		menu.add(0, Constant.INT_MENU_FRESH, 3, Constant.STRING_FILE_REFLASH).setIcon(android.R.drawable.ic_menu_rotate);
		menu.add(0, Constant.INT_MENU_CRETEDIR, 4, Constant.STRING_FILE_MKDIR).setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, Constant.INT_MENU_EXIT, 5, Constant.STRING_FILE_CANCEL).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		return super.onCreateOptionsMenu(menu);
	}
	

	// MENU菜单的单击事件
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final EditText etInput = new EditText(this);
		switch (item.getItemId()) {
		case Constant.INT_MENU_BACK:
			browseUpLevel();
			break;
		case Constant.INT_MENU_BACKHOME:
			browseRoot();
			break;
		case Constant.INT_MENU_FRESH:
			browseCurrent();
			break;
		case Constant.INT_MENU_EXIT:
			break;
		case Constant.INT_MENU_CRETEDIR:
			// 如果单击创建文件夹，弹出对话框
			builder = new Builder(this);
			builder.setTitle(Constant.STRING_FILE_MKDIR)// 设置对话框标题
					.setMessage(Constant.STRING_FILE_PLEASE_INPUT_NAME)// 设置对话框提示信息
					.setIcon(android.R.drawable.ic_menu_add)// 设置对话框图标
					.setView(etInput)// 设置对话框显示一个文本框视图
					// 设置对话框中的确定按钮
					.setPositiveButton(Constant.STRING_FILE_OK, new OnClickListener() {
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
					}).setNegativeButton(Constant.STRING_FILE_CANCEL, null)// 设置对话框的取消按钮
					.show();// 显示对话框
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	/**
	 * 重命名回调委托  or  创建文件夹回调委托
	 */
	private Handler fileCallBackHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0)
				browseTo(current_dir);
		}
	};

	
	
	
}

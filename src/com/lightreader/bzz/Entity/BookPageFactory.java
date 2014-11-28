package com.lightreader.bzz.Entity;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Vector;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.Log;
import com.lightreader.bzz.Activity.ReadBookActivity;
import com.lightreader.bzz.Application.AllApplication;
import com.lightreader.bzz.Utils.BeanTools;

public class BookPageFactory {

	private static final String TAG = "BookPageFactory";
	private File book_file = null;
	private int m_backColor = 0xffff9e85; // 背景颜色
	private Bitmap m_book_bg = null;
	private int m_fontSize = 20;//全局的字体大小
	private final int m_fontSize_bottom = 30;//底部字体的大小
	private boolean m_isfirstPage, m_islastPage;
	private Vector<String> m_lines = new Vector<String>();//本页计算最后需要显示的文本
	private MappedByteBuffer m_mbBuff = null;// 内存中的图书字符
	private int m_mbBufBegin = 0;// 当前页起始位置
	private int m_mbBufEnd = 0;// 当前页终点位置
	private int m_mbBufLength = 0; // 图书总长度
	private String m_strCharsetName = "GBK";
	private int m_textColor = Color.rgb(28, 28, 28);//偏黑   文字颜色
	private final int m_textColor_bottom = Color.rgb(215, 99, 215);//纯黑
	private int marginHeight = 15; // 上下与边缘的距离
	private int marginWidth = 15; // 左右与边缘的距离
	private int mHeight;//屏幕高度
	private int mWidth;//屏幕宽度
	private int mPreLineCount;// 每页可以显示的行数
	private Paint mPaint;//全局的画笔
	private Paint mPaint_bottom;//页面显示底部的画笔
	private float mVisibleHeight; // 绘制内容的宽
	private float mVisibleWidth; // 绘制内容的宽
	
	private int totalPagesCount = -1;//总页数
	private int currentPageCount;//当前页
	private int mPrePageLength;//每一页的长度
	private ArrayList<Integer> pageHeadlists = null;//每一页开头的index构建的列表
	
	
	public BookPageFactory(int w, int h) {
		mWidth = w;
		mHeight = h;
		//画笔1
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setTextAlign(Align.LEFT);// 左对齐
		mPaint.setTextSize(m_fontSize);// 字体大小
		mPaint.setColor(m_textColor);// 字体颜色
		//画笔2
		mPaint_bottom = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint_bottom.setTextAlign(Align.LEFT);// 左对齐
		mPaint_bottom.setTextSize(m_fontSize_bottom);// 字体大小
		mPaint_bottom.setColor(m_textColor_bottom);// 字体颜色
		
		mVisibleWidth = mWidth - marginWidth * 2;
		mVisibleHeight = mHeight - marginHeight * 2;
		mPreLineCount = (int) (mVisibleHeight / m_fontSize) - 1; // 每页可显示的行数,-1是因为底部显示进度的位置容易被遮住
	}

	//是否是第一页
	public boolean isfirstPage() {
		return m_isfirstPage;
	}

	//是否是最后一页
	public boolean islastPage() {
		return m_islastPage;
	}

	// 设置背景图
	public void setBgBitmap(Bitmap BG) {
		m_book_bg = BG;
	}

	// 设置字体大小
	public void setM_fontSize(int m_fontSize) {
		this.m_fontSize = m_fontSize;
		mPreLineCount = (int) (mVisibleHeight / m_fontSize) - 1;// 每页可显示的行数,-1是因为底部显示进度的位置容易被遮住
	}

	// 设置页面起始点
	public void setM_mbBufBegin(int m_mbBufBegin) {
		this.m_mbBufBegin = m_mbBufBegin;
	}

	// 设置页面结束点
	public void setM_mbBufEnd(int m_mbBufEnd) {
		this.m_mbBufEnd = m_mbBufEnd;
	}

	// 设置字体颜色
	public void setM_textColor(int m_textColor) {
		this.m_textColor = m_textColor;
	}

	//设置总页数
	public void setTotalPagesCount(int totalPagesCount) {
		this.totalPagesCount = totalPagesCount;
	}
	
	//设置当期页数
	public void setCurrentPageCount(int currentPageCount) {
		this.currentPageCount = currentPageCount;
	}
	
	//设置每一页的长度
	public void setmPrePageLength(int mPrePageLength) {
		this.mPrePageLength = mPrePageLength;
	}
	
	public int getmPrePageLength() {
		return mPrePageLength;
	}

	public int getTotalPagesCount() {
		if(totalPagesCount != -1){//-1为初始化时候的值
			return totalPagesCount;
		}else{
			this.totalPagesCount = getTotlePages();
			return getTotlePages();//根据方法求出总页数
		}
	}
	
	public int getCurrentPageCount() {
		return currentPageCount;
	}

	public int getM_mbBufBegin() {
		return m_mbBufBegin;
	}

	public String getFirstLineText() {
		return m_lines.size() > 0 ? m_lines.get(0) : "";
	}

	public int getM_textColor() {
		return m_textColor;
	}

	public int getM_mbBufLength() {
		return m_mbBufLength;
	}

	public int getM_mbBufEnd() {
		return m_mbBufEnd;
	}

	public int getM_fontSize() {
		return m_fontSize;
	}

	public int getmLineCount() {
		return mPreLineCount;
	}

	//强制更新总页数
	public int forcedUpdateTotalPagesCount(){
		this.totalPagesCount = getTotlePages();
		return getTotlePages();//根据方法求出总页数
	}
	

	


	
	/**
	 * 特定的更新
	 * @param canvas
	 */
	@SuppressLint("DrawAllocation")
	public void onDraw(Canvas canvas) {
		//TODO 绘图方法,重要
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		if (m_lines.size() == 0)
			m_lines = pageDown();
		if (m_lines.size() > 0) {
			if (m_book_bg == null){
				canvas.drawColor(m_backColor);
			}else{
				canvas.drawBitmap(m_book_bg, 0, 0, null);
			}
			int y = marginHeight;// 上下与边缘的距离
			for (String strLine : m_lines) {
				y += m_fontSize;
				canvas.drawText(strLine, marginWidth, y, mPaint);//写全局汉字的画布
			}
		}
		float fPercent = (float) (m_mbBufBegin * 1.0 / m_mbBufLength);
		DecimalFormat df = new DecimalFormat("#0.0");
		String strPercent = df.format(fPercent * 100) + "%";//底部要写入的字符串
		//int nPercentWidth = (int) mPaint.measureText("999.9%") + 1;
		int nPercentWidth = (int) mPaint_bottom.measureText("999.9%") + 1; //阅读百分比 [计算位置]
		String totalPagesCountText = String.valueOf(999999);
		String currentPageCountText = String.valueOf(123);
		String pagesText = currentPageCountText.concat("/").concat(totalPagesCountText);//变成了最后的文字 123/9999
		int totalPagesNumberWidth = (int) mPaint_bottom.measureText(pagesText) + 1;//当前页显示位置 [计算位置]
		String timeText = BeanTools.getSystemCurrentTime(AllApplication.getInstance());//获取当前系统时间
		//int timeWidth = (int) mPaint_bottom.measureText(timeText) + 1;//时间的宽度[计算位置]
		//int power = (int) mPaint_bottom.measureText(getBattery()) + 1;//电量文字的显示宽度 [计算位置]
		
		//canvas.drawText(strPercent, mWidth - nPercentWidth, mHeight - 5, mPaint_bottom);//写底部汉字的画布
		//canvas.drawText(pagesText, (mWidth - totalPagesNumberWidth) / 2 , mHeight - 5, mPaint_bottom);//写底部汉字的画布
		//canvas.drawText(getBattery()+" "+timeText, marginWidth , mHeight - 5, mPaint_bottom);//写底部时间的画布
		
		ReadBookActivity.textViewBattery.setText(ReadBookActivity.textViewBattery.getText());
		ReadBookActivity.textViewLeft.setText(timeText);
		if(totalPagesCount == -1){//第一次进来,总页数还没计算出来的时候
			ReadBookActivity.textViewCenter.setText("计算中...");
			//ReadBookActivity.textViewCenter.setText("1/"+getTotlePagesLike()+"");
		}else{
			int a = (int)Math.floor(fPercent * Float.valueOf(getTotalPagesCount()) + 0.5f);
			if(a <= 0 )a=1;
			String b = getTotalPagesCount()+"";
			ReadBookActivity.textViewCenter.setText(a+"/"+b);
		}
		ReadBookActivity.textViewRight.setText(strPercent);//进度百分比
	}
	
	
	
	/**
	 * 
	 * @param strFilePath
	 * @param begin
	 * 表示书签记录的位置，读取书签时，将begin值给m_mbBufEnd，在读取nextpage，及成功读取到了书签
	 * 记录时将m_mbBufBegin开始位置作为书签记录
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public void openbook(String strFilePath, int begin) throws IOException {
		book_file = new File(strFilePath);
		// book_file=new File("mnt/sdcard/s.txt");
		long length = book_file.length();
		m_mbBufLength = (int) length;
		/* 
         * 内存映射文件能让你创建和修改那些因为太大而无法放入内存的文件。有了内存映射文件，你就可以认为文件已经全部读进了内存， 
         * 然后把它当成一个非常大的数组来访问。这种解决办法能大大简化修改文件的代码。  
         *  
         * fileChannel.map(FileChannel.MapMode mode, long position, long size)将此通道的文件区域直接映射到内存中。但是，你必 
         * 须指明，它是从文件的哪个位置开始映射的，映射的范围又有多大 
         */  
		
		//文件通道的可读可写要建立在文件流本身可读写的基础之上    
		m_mbBuff = new RandomAccessFile(book_file, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);//把文件所有字节放入内存
		Log.d(TAG, "total lenth：" + m_mbBufLength);
		// 设置已读进度
		if (begin >= 0) {
			m_mbBufBegin = begin;
			m_mbBufEnd = begin;
		} else {
			
		}
		
	}

	/**
	 * 获取当前书本总页数(精算)
	 * 根据 :
	 * 1.当前字体大小 
	 */
	protected int getTotlePages(){
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		String tempString = "";
		Vector<String> lines = new Vector<String>();
		int _mbBufEnd = 0;
		int pageTotla = 0;
		pageHeadlists = new ArrayList<Integer>();
		//System.out.println("执行开始时间:"+BeanTools.getSystemCurrentTime2(AllApplication.getInstance()));
		while(_mbBufEnd < m_mbBufLength){
			pageHeadlists.add(_mbBufEnd);//存入每一页的结尾的长度
			
			while (lines.size() < mPreLineCount) {// 每页可以显示的行数
				byte[] paraBuff = readParagraphForward(_mbBufEnd);
				_mbBufEnd += paraBuff.length;// 每次读取后，记录结束点位置，该位置是段落结束位置
				try {
					tempString = new String(paraBuff, m_strCharsetName);// 转换成制定GBK编码
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, "pageDown->转换编码失败", e);
				}
				String strReturn = "";
				// 替换掉回车换行符
				if (tempString.indexOf("\r\n") != -1) {
					strReturn = "\r\n";
					tempString = tempString.replaceAll("\r\n", "");
				} else if (tempString.indexOf("\n") != -1) {
					strReturn = "\n";
					tempString = tempString.replaceAll("\n", "");
				}
	
				if (tempString.length() == 0) {
					lines.add(tempString);
				}
				
				while (tempString.length() > 0) {
					// 画一行文字
					int nSize = mPaint.breakText(tempString, true, mVisibleWidth, null);
					lines.add(tempString.substring(0, nSize));
					tempString = tempString.substring(nSize);// 得到剩余的文字
					// 超出最大行数则不再画
					if (lines.size() >= mPreLineCount) {
						break;
					}
				}
				//System.out.println(BeanTools.getSystemCurrentTime2(AllApplication.getInstance()));
				// 如果该页最后一段只显示了一部分，则从新定位结束点位置
				if (tempString.length() != 0) {
					try {
						_mbBufEnd -= (tempString + strReturn).getBytes(m_strCharsetName).length;
					} catch (UnsupportedEncodingException e) {
						Log.e(TAG, "pageDown->记录结束点位置失败", e);
					}
				}
			}
			//System.out.println(BeanTools.getSystemCurrentTime2(AllApplication.getInstance()));
			pageTotla++;
			lines.clear();
		}
		//System.out.println(BeanTools.getSystemCurrentTime2(AllApplication.getInstance()));
		//System.out.println("执行完毕时间:");
		//System.out.println("执行完毕时间:"+BeanTools.getSystemCurrentTime2(AllApplication.getInstance()));
		return pageTotla;
	}
	
	
	/**
	 * 获取当前书本总页数(模糊初步计算)
	 * 根据 :
	 * 1.当前字体大小 
	 */
	protected int getTotlePagesLike(){
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		String tempString = "";
		Vector<String> lines = new Vector<String>();
		int _mbBufEnd = 0;
		int pageTotla = 0;
		pageHeadlists = new ArrayList<Integer>();
		while(_mbBufEnd < m_mbBufLength){
			pageHeadlists.add(_mbBufEnd);//存入每一页的结尾的长度
			//快速计算 模糊计算
			if(pageHeadlists.size() > 10){
				Integer avg = BeanTools.getAvgNumber(pageHeadlists);
				return m_mbBufLength / avg + 1;
			}
			
			while (lines.size() < mPreLineCount) {// 每页可以显示的行数
				byte[] paraBuff = readParagraphForward(_mbBufEnd);
				_mbBufEnd += paraBuff.length;// 每次读取后，记录结束点位置，该位置是段落结束位置
				try {
					tempString = new String(paraBuff, m_strCharsetName);// 转换成制定GBK编码
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, "pageDown->转换编码失败", e);
				}
				String strReturn = "";
				// 替换掉回车换行符
				if (tempString.indexOf("\r\n") != -1) {
					strReturn = "\r\n";
					tempString = tempString.replaceAll("\r\n", "");
				} else if (tempString.indexOf("\n") != -1) {
					strReturn = "\n";
					tempString = tempString.replaceAll("\n", "");
				}
	
				if (tempString.length() == 0) {
					lines.add(tempString);
				}
				while (tempString.length() > 0) {
					// 画一行文字
					int nSize = mPaint.breakText(tempString, true, mVisibleWidth, null);
					lines.add(tempString.substring(0, nSize));
					tempString = tempString.substring(nSize);// 得到剩余的文字
					// 超出最大行数则不再画
					if (lines.size() >= mPreLineCount) {
						break;
					}
				}
				// 如果该页最后一段只显示了一部分，则从新定位结束点位置
				if (tempString.length() != 0) {
					try {
						_mbBufEnd -= (tempString + strReturn).getBytes(m_strCharsetName).length;
					} catch (UnsupportedEncodingException e) {
						Log.e(TAG, "pageDown->记录结束点位置失败", e);
					}
				}
			}
			pageTotla++;
			lines.clear();
		}
		
		return pageTotla;
	}
	
	
	
	
	/**
	 * 画指定页的内容
	 * @return 
	 *          显示的内容 Vector<String>
	 */
	protected Vector<String> pageDown() {
		//getTotlePages();
		mPaint.setTextSize(m_fontSize);
		mPaint.setColor(m_textColor);
		
		String tempString = "";
		Vector<String> lines = new Vector<String>();
		while (lines.size() < mPreLineCount && m_mbBufEnd < m_mbBufLength) {
			byte[] paraBuff = readParagraphForward(m_mbBufEnd);
			m_mbBufEnd += paraBuff.length;// 每次读取后，记录结束点位置，该位置是段落结束位置
			try {
				tempString = new String(paraBuff, m_strCharsetName);// 转换成制定GBK编码
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "pageDown->转换编码失败", e);
			}
			String strReturn = "";
			// 替换掉回车换行符
			if (tempString.indexOf("\r\n") != -1) {
				strReturn = "\r\n";
				tempString = tempString.replaceAll("\r\n", "");
			} else if (tempString.indexOf("\n") != -1) {
				strReturn = "\n";
				tempString = tempString.replaceAll("\n", "");
			}

			if (tempString.length() == 0) {
				lines.add(tempString);
			}
			while (tempString.length() > 0) {
				// 画一行文字
				int nSize = mPaint.breakText(tempString, true, mVisibleWidth, null);
				lines.add(tempString.substring(0, nSize));
				tempString = tempString.substring(nSize);// 得到剩余的文字
				// 超出最大行数则不再画
				if (lines.size() >= mPreLineCount) {
					break;
				}
			}
			// 如果该页最后一段只显示了一部分，则从新定位结束点位置
			if (tempString.length() != 0) {
				try {
					m_mbBufEnd -= (tempString + strReturn).getBytes(m_strCharsetName).length;
				} catch (UnsupportedEncodingException e) {
					Log.e(TAG, "pageDown->记录结束点位置失败", e);
				}
			}
		}
		return lines;
	}

	
	
	/**
	 * 得到上上页的结束位置
	 */
	protected void pageUp() {
		if (m_mbBufBegin < 0){
			m_mbBufBegin = 0;
		}
		Vector<String> lines = new Vector<String>();
		String strParagraph = "";
		while (lines.size() < mPreLineCount && m_mbBufBegin > 0) {
			Vector<String> paraLines = new Vector<String>();
			byte[] paraBuf = readParagraphBack(m_mbBufBegin);
			m_mbBufBegin -= paraBuf.length;// 每次读取一段后,记录开始点位置,是段首开始的位置
			try {
				strParagraph = new String(paraBuf, m_strCharsetName);
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "pageUp->转换编码失败", e);
			}
			strParagraph = strParagraph.replaceAll("\r\n", "");
			strParagraph = strParagraph.replaceAll("\n", "");
			// 如果是空白行，直接添加
			if (strParagraph.length() == 0) {
				paraLines.add(strParagraph);
			}
			while (strParagraph.length() > 0) {
				// 画一行文字
				int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
				paraLines.add(strParagraph.substring(0, nSize));
				strParagraph = strParagraph.substring(nSize);
			}
			lines.addAll(0, paraLines);
		}

		while (lines.size() > mPreLineCount) {
			try {
				m_mbBufBegin += lines.get(0).getBytes(m_strCharsetName).length;
				lines.remove(0);
			} catch (UnsupportedEncodingException e) {
				Log.e(TAG, "pageUp->记录起始点位置失败", e);
			}
		}
		m_mbBufEnd = m_mbBufBegin;// 上上一页的结束点等于上一页的起始点
		return;
	}

	
	
	
	
	
	
	/**
	 * 向前翻页
	 * 
	 * @throws IOException
	 */
	public void prePage() throws IOException {
		if (m_mbBufBegin <= 0) {
			m_mbBufBegin = 0;
			m_isfirstPage = true;
			return;
		} else{
			m_isfirstPage = false;
		}
		m_lines.clear();
		pageUp();
		m_lines = pageDown();
	}

	/**
	 * 下一页
	 * 
	 * @throws IOException
	 */
	public void nextPage() throws IOException {
		if (m_mbBufEnd >= m_mbBufLength) {
			m_islastPage = true;
			return;
		} else{
			m_islastPage = false;
		}
		m_lines.clear();
		m_mbBufBegin = m_mbBufEnd;// 下一页页起始位置=当前页结束位置
		m_lines = pageDown();
	}
	
	/**
	 * 当前页
	 * @throws IOException
	 */
	public void currentPage() throws IOException {
		m_lines.clear();
		m_lines = pageDown();
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 读取指定位置的上一个段落
	 * @param nFromPos
	 * @return byte[]
	 */
	protected byte[] readParagraphBack(int nFromPos) {
		int nEnd = nFromPos;
		int i;
		byte byte0, byte1;
		if (m_strCharsetName.equals("UTF-16LE")) {
			i = nEnd - 2;
			while (i > 0) {
				byte0 = m_mbBuff.get(i);
				byte1 = m_mbBuff.get(i + 1);
				if (byte0 == 0x0a && byte1 == 0x00 && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}
		} else if (m_strCharsetName.equals("UTF-16BE")) {
			i = nEnd - 2;
			while (i > 0) {
				byte0 = m_mbBuff.get(i);
				byte1 = m_mbBuff.get(i + 1);
				if (byte0 == 0x00 && byte1 == 0x0a && i != nEnd - 2) {
					i += 2;
					break;
				}
				i--;
			}
		} else {
			i = nEnd - 1;
			while (i > 0) {
				byte0 = m_mbBuff.get(i);
				if (byte0 == 0x0a && i != nEnd - 1) {// 0x0a表示换行符
					i++;
					break;
				}
				i--;
			}
		}
		if (i < 0)
			i = 0;
		int nParaSize = nEnd - i;
		int j;
		byte[] buf = new byte[nParaSize];
		for (j = 0; j < nParaSize; j++) {
			buf[j] = m_mbBuff.get(i + j);
		}
		return buf;
	}

	
	/**
	 * 读取指定位置的下一个段落
	 * 
	 * @param nFromPos
	 * @return byte[]
	 * 
	 *  0x0A LF表示换行
		0x0D CR表示回车
		0x1A SUB在文本文件中表示文件结果
		0x00 16进制的0
	 */
	protected byte[] readParagraphForward(int nFromPos) {
		int nStart = nFromPos;
		int i = nStart;
		byte byte0, byte1;
		// 根据编码格式判断换行
		if (m_strCharsetName.equals("UTF-16LE")) {
			while (i < m_mbBufLength - 1) {
				byte0 = m_mbBuff.get(i++);
				byte1 = m_mbBuff.get(i++);
				if (byte0 == 0x0a && byte1 == 0x00) {
					break;
				}
			}
		} else if (m_strCharsetName.equals("UTF-16BE")) {
			while (i < m_mbBufLength - 1) {
				byte0 = m_mbBuff.get(i++);
				byte1 = m_mbBuff.get(i++);
				if (byte0 == 0x00 && byte1 == 0x0a) {
					break;
				}
			}
		} else {
			while (i < m_mbBufLength) {// m_mbBufLength图书总长度
				byte0 = m_mbBuff.get(i++);//m_mbBuff 内存中的图书字符
				if (byte0 == 0x0a) {
					break;
				}
			}
		}
		//i循环后达到最大值
		int nParaSize = i - nStart;
		byte[] buff = new byte[nParaSize];//构建一个byte[]组
		for (i = 0; i < nParaSize; i++) {
			buff[i] = m_mbBuff.get(nFromPos + i);//从内存中获取从指定位置到目标位置的byte[]
		}
		return buff;
	}

	
}

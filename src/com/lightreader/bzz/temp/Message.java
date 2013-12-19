package com.lightreader.bzz.temp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Message implements Parcelable {
	private int id;
	private String msgText;
	private String fromName;
	private String date;

	public Message() {
		super();

	}
	public Message(int id, String msgText, String fromName, String date) {
		super();
		this.id = id;
		this.msgText = msgText;
		this.fromName = fromName;
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsgText() {
		return msgText;
	}

	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * 一个位掩码，表示一组特殊对象类型的Parcelable，一般返回0即可
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public String toString() {
		return "信息内容=" + msgText + ", 发件人=" + fromName + ", 时间=" + date;
	}

	
	/**
	 * 实现对象的序列化
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.i("main", "客户端Message被序列化");
		dest.writeInt(id);
		dest.writeString(msgText);
		dest.writeString(fromName);
		dest.writeString(date);
	}

	/**
	 * 实现了对象的反序列化
	 */
	public static final Parcelable.Creator<Message> CREATOR = new Creator<Message>() {
		
		//创建一个新的Parcelable对象的数组
		@Override
		public Message[] newArray(int size) {
			return new Message[size];
		}

		//根据writeToParcel()方法序列化的数据，反序列化一个Parcelable对象
		@Override
		public Message createFromParcel(Parcel source) {
			Log.i("main", "客户端Message被反序列化");
			return new Message(source.readInt(), source.readString(), source.readString(), source.readString());
		}
	};
}

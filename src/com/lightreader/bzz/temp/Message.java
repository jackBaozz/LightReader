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
	 * һ��λ���룬��ʾһ������������͵�Parcelable��һ�㷵��0����
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public String toString() {
		return "��Ϣ����=" + msgText + ", ������=" + fromName + ", ʱ��=" + date;
	}

	
	/**
	 * ʵ�ֶ�������л�
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.i("main", "�ͻ���Message�����л�");
		dest.writeInt(id);
		dest.writeString(msgText);
		dest.writeString(fromName);
		dest.writeString(date);
	}

	/**
	 * ʵ���˶���ķ����л�
	 */
	public static final Parcelable.Creator<Message> CREATOR = new Creator<Message>() {
		
		//����һ���µ�Parcelable���������
		@Override
		public Message[] newArray(int size) {
			return new Message[size];
		}

		//���writeToParcel()�������л�����ݣ������л�һ��Parcelable����
		@Override
		public Message createFromParcel(Parcel source) {
			Log.i("main", "�ͻ���Message�������л�");
			return new Message(source.readInt(), source.readString(), source.readString(), source.readString());
		}
	};
}

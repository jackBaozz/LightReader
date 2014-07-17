package com.lightreader.bzz.temp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class User implements Parcelable {

	private int id;
	private String username;
	private String password;

	public User() {
		super();
	}

	public User(int id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	
	@Override
	public boolean equals(Object o) {
		User us = (User) o;
		if (this.username.equals(us.username) && this.password.equals(us.password)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * һ��λ���룬��ʾһ������������͵�Parcelable��һ�㷵��0����
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	
	/**
	 * ʵ�ֶ�������л�
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.i("main", "�ͻ���User�����л�");
		dest.writeInt(id);
		dest.writeString(username);
		dest.writeString(password);
	}

	
	/**
	 * ʵ���˶���ķ����л�
	 */
	public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
		/**
		 * ����һ���µ�Parcelable���������
		 */
		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
		/**
		 * ���writeToParcel()�������л�����ݣ������л�һ��Parcelable����
		 */
		@Override
		public User createFromParcel(Parcel source) {
			Log.i("main", "�ͻ���User�������л�");
			return new User(source.readInt(), source.readString(), source.readString());
		}
	};

}

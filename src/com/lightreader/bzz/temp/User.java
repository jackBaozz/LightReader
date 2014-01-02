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
	 * 一个位掩码，表示一组特殊对象类型的Parcelable，一般返回0即可
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	
	/**
	 * 实现对象的序列化
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.i("main", "客户端User被序列化");
		dest.writeInt(id);
		dest.writeString(username);
		dest.writeString(password);
	}

	
	/**
	 * 实现了对象的反序列化
	 */
	public static final Parcelable.Creator<User> CREATOR = new Creator<User>() {
		/**
		 * 创建一个新的Parcelable对象的数组
		 */
		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
		/**
		 * 根据writeToParcel()方法序列化的数据，反序列化一个Parcelable对象
		 */
		@Override
		public User createFromParcel(Parcel source) {
			Log.i("main", "客户端User被反序列化");
			return new User(source.readInt(), source.readString(), source.readString());
		}
	};

}

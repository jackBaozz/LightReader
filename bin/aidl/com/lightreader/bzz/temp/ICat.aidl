package com.lightreader.bzz.temp;
import com.lightreader.bzz.temp.Message;
interface ICat {
	String getName();
	int getAge();
	int gender();
	// 在AIDL接口中定义一个getMes方法
    List<Message> getMes();
}

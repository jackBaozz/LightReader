package com.lightreader.bzz.temp;

import com.lightreader.bzz.temp.Message;
import com.lightreader.bzz.temp.User;

interface IDog {
	String getName();
	int getAge();
	int gender();
	// 在AIDL接口中定义一个getMes方法
    List<Message> getMes(in User us);
}

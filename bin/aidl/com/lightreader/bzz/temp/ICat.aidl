package com.lightreader.bzz.temp;
import com.lightreader.bzz.temp.Message;
interface ICat {
	String getName();
	int getAge();
	int gender();
	// ��AIDL�ӿ��ж���һ��getMes����
    List<Message> getMes();
}

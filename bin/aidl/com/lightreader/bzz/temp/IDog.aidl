package com.lightreader.bzz.temp;

import com.lightreader.bzz.temp.Message;
import com.lightreader.bzz.temp.User;

interface IDog {
	String getName();
	int getAge();
	int gender();
	// ��AIDL�ӿ��ж���һ��getMes����
    List<Message> getMes(in User us);
}

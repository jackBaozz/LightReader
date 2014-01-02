package com.lightreader.bzz.temp;

import com.lightreader.bzz.temp.Message;
import com.lightreader.bzz.temp.User;

interface IGetMsg{
	List<Message> getMes(in User us);
}
package com.lightreader.bzz.file;

import java.text.Collator;
import java.util.Comparator;

import com.lightreader.bzz.pojo.FileInfo;

/** 
 * 文件按文件名排序（从A到Z） 
 */ 
public class CompratorByFileName implements Comparator<FileInfo>{

	@Override  
    public int compare(FileInfo lhs,FileInfo rhs) {  
        Comparator cmp = Collator.getInstance(java.util.Locale.CHINA);  
        return cmp.compare(lhs.getName() , rhs.getName());  
    }  
      
    @Override  
    public boolean equals(Object o) {  
        return true;  
    }
	
}

package com.lightreader.bzz.pojo;

public class Book {
	private Integer id; //id 
	private String md5 ;//md5编码
	private String name ;//名字
	private String path ;//路径
	private String icon ;//图标
	private String size ;//大小
	private String isDirectory;//是否是目录
	private Integer fileCount;//多少文件
	private Integer folderCount;//多少目录
	private String lastModifyDate;//最后一次修改的时间
	private String fileCompetence;//权限
	private Integer totalCharacterNum;//总字数
	private Integer prePageCharacterNum;//每页字数
	private Integer totalPage;//总页数
	private Integer currentPage;//当前页数
	private Integer isShow;//是否显示
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getIsDirectory() {
		return isDirectory;
	}
	public void setIsDirectory(String isDirectory) {
		this.isDirectory = isDirectory;
	}
	public Integer getFileCount() {
		return fileCount;
	}
	public void setFileCount(Integer fileCount) {
		this.fileCount = fileCount;
	}
	public Integer getFolderCount() {
		return folderCount;
	}
	public void setFolderCount(Integer folderCount) {
		this.folderCount = folderCount;
	}
	public String getLastModifyDate() {
		return lastModifyDate;
	}
	public void setLastModifyDate(String lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
	}
	public String getFileCompetence() {
		return fileCompetence;
	}
	public void setFileCompetence(String fileCompetence) {
		this.fileCompetence = fileCompetence;
	}
	public Integer getTotalCharacterNum() {
		return totalCharacterNum;
	}
	public void setTotalCharacterNum(Integer totalCharacterNum) {
		this.totalCharacterNum = totalCharacterNum;
	}
	public Integer getPrePageCharacterNum() {
		return prePageCharacterNum;
	}
	public void setPrePageCharacterNum(Integer prePageCharacterNum) {
		this.prePageCharacterNum = prePageCharacterNum;
	}
	public Integer getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
	public Integer getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}
	public Integer getIsShow() {
		return isShow;
	}
	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}
	
	
	
	
	
}

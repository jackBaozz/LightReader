package com.lightreader.bzz.Pojo;

public class PushMsg {
	private Integer id;
	private String userId;
	private String userName ;
	private String userTel ;
	private String pushTiem ;
	private String pushTitle ;
	private String pushMsg ;
	private String pushTag ;
	private String isPush ;
	
	
	public PushMsg(String userId,String userName,String userTel,String pushTiem,String pushTitle,String pushMsg,String pushTag,String isPush){
		this.userId = userId;
		this.userName = userName;
		this.userTel = userTel;
		this.pushTiem = pushTiem;
		this.pushTitle = pushTitle;
		this.pushMsg = pushMsg;
		this.pushTag = pushTag;
		this.isPush = isPush;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserTel() {
		return userTel;
	}
	public void setUserTel(String userTel) {
		this.userTel = userTel;
	}
	public String getPushTiem() {
		return pushTiem;
	}
	public void setPushTiem(String pushTiem) {
		this.pushTiem = pushTiem;
	}
	public String getPushTitle() {
		return pushTitle;
	}
	public void setPushTitle(String pushTitle) {
		this.pushTitle = pushTitle;
	}
	public String getPushMsg() {
		return pushMsg;
	}
	public void setPushMsg(String pushMsg) {
		this.pushMsg = pushMsg;
	}
	public String getPushTag() {
		return pushTag;
	}
	public void setPushTag(String pushTag) {
		this.pushTag = pushTag;
	}
	public String getIsPush() {
		return isPush;
	}
	public void setIsPush(String isPush) {
		this.isPush = isPush;
	}
	
	
}

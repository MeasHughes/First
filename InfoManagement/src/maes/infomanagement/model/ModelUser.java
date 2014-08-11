package maes.infomanagement.model;


import java.util.Date;

import android.R.integer;

public class ModelUser {
	private String mUserName;
	private int mUserID;
	private Date mDate = new Date();
	//0Ω˚”√,1∆Ù”√
	private int state ;
	public String getmUserName() {
		return mUserName;
	}
	public void setmUserName(String mUserName) {
		this.mUserName = mUserName;
	}
	public int getmUserID() {
		return mUserID;
	}
	public void setmUserID(int mUserID) {
		this.mUserID = mUserID;
	}
	public Date getmDate() {
		return mDate;
	}
	public void setmDate(Date mDate) {
		this.mDate = mDate;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	
}

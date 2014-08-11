package maes.infomanagement.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ModelPayout implements Serializable{
	// 支出表主键ID
	private int mPayoutID;
	// 账本ID外键
	private int mAccountBookID;
	// 账本名称
	private String mAccountBookName;
	// 支出类别ID外键
	private int mCategoryID;
	// 类别名称
	private String mCategoryName;
	// 路径
	private String mPath;
	// 付款方式ID外键
	private int mPayWayID;
	// 消费地点ID外键
	private int mPlaceID;
	// 消费金额
	private BigDecimal mAmount;
	// 消费日期
	private Date mPayoutDate;
	// 计算方式
	private String mPayoutType;
	// 消费人ID外键
	private String mPayoutUserID;
	// 备注
	private String mComment;
	// 添加日期
	private Date mCreateDate = new Date();
	// 状态 0失效 1启用
	private int mState = 1;
	public int getPayoutID() {
		return mPayoutID;
	}
	public void setPayoutID(int mPayoutID) {
		this.mPayoutID = mPayoutID;
	}
	public int getAccountBookID() {
		return mAccountBookID;
	}
	public void setAccountBookID(int mAccountBookID) {
		this.mAccountBookID = mAccountBookID;
	}
	public String getAccountBookName() {
		return mAccountBookName;
	}
	public void setAccountBookName(String mAccountBookName) {
		this.mAccountBookName = mAccountBookName;
	}
	public int getCategoryID() {
		return mCategoryID;
	}
	public void setCategoryID(int mCategoryID) {
		this.mCategoryID = mCategoryID;
	}
	public String getCategoryName() {
		return mCategoryName;
	}
	public void setCategoryName(String mCategoryName) {
		this.mCategoryName = mCategoryName;
	}
	public String getPath() {
		return mPath;
	}
	public void setPath(String mPath) {
		this.mPath = mPath;
	}
	public int getPayWayID() {
		return mPayWayID;
	}
	public void setPayWayID(int mPayWayID) {
		this.mPayWayID = mPayWayID;
	}
	public int getPlaceID() {
		return mPlaceID;
	}
	public void setPlaceID(int mPlaceID) {
		this.mPlaceID = mPlaceID;
	}
	public BigDecimal getAmount() {
		return mAmount;
	}
	public void setAmount(BigDecimal mAmount) {
		this.mAmount = mAmount;
	}
	public Date getPayoutDate() {
		return mPayoutDate;
	}
	public void setPayoutDate(Date mPayoutDate) {
		this.mPayoutDate = mPayoutDate;
	}
	public String getPayoutType() {
		return mPayoutType;
	}
	public void setPayoutType(String mPayoutType) {
		this.mPayoutType = mPayoutType;
	}
	public String getPayoutUserID() {
		return mPayoutUserID;
	}
	public void setPayoutUserID(String mPayoutUserID) {
		this.mPayoutUserID = mPayoutUserID;
	}
	public String getComment() {
		return mComment;
	}
	public void setComment(String mComment) {
		this.mComment = mComment;
	}
	public Date getCreateDate() {
		return mCreateDate;
	}
	public void setCreateDate(Date mCreateDate) {
		this.mCreateDate = mCreateDate;
	}
	public int getState() {
		return mState;
	}
	public void setState(int mState) {
		this.mState = mState;
	}
	
	
	
	
}

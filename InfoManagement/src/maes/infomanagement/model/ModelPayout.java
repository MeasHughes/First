package maes.infomanagement.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ModelPayout implements Serializable{
	// ֧��������ID
	private int mPayoutID;
	// �˱�ID���
	private int mAccountBookID;
	// �˱�����
	private String mAccountBookName;
	// ֧�����ID���
	private int mCategoryID;
	// �������
	private String mCategoryName;
	// ·��
	private String mPath;
	// ���ʽID���
	private int mPayWayID;
	// ���ѵص�ID���
	private int mPlaceID;
	// ���ѽ��
	private BigDecimal mAmount;
	// ��������
	private Date mPayoutDate;
	// ���㷽ʽ
	private String mPayoutType;
	// ������ID���
	private String mPayoutUserID;
	// ��ע
	private String mComment;
	// �������
	private Date mCreateDate = new Date();
	// ״̬ 0ʧЧ 1����
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

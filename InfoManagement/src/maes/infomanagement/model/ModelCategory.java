package maes.infomanagement.model;

import java.io.Serializable;
import java.util.Date;

public class ModelCategory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4493312582968339458L;
	//��������ID
	private int mCategoryID;
	//�������
	private String mCategoryName;
	//���ͱ������
	private String mTypeFlag;
	//������ID
	private int mParentID = 0;
	//·��
	private String mPath;	
	//�������
	private Date mCreateDate = new Date();
	//״̬ 0ʧЧ 1����
	private int mState = 1;
	/**
	 * �˱�������ID
	 */
	public int getCategoryID() {
		return mCategoryID;
	}
	/**
	 * �˱�������ID
	 */
	public void setCategoryID(int p_CategoryID) {
		this.mCategoryID = p_CategoryID;
	}
	/**
	 * �˱�����
	 */
	public String getCategoryName() {
		return mCategoryName;
	}
	/**
	 * �˱�����
	 */
	public void setCategoryName(String p_CategoryName) {
		this.mCategoryName = p_CategoryName;
	}
	/**
	 * ���ͱ������
	 */
	public String getTypeFlag() {
		return mTypeFlag;
	}
	/**
	 * ���ͱ������
	 */
	public void setTypeFlag(String p_TypeFlag) {
		this.mTypeFlag = p_TypeFlag;
	}
	/**
	 * ������ID
	 */
	public int getParentID() {
		return mParentID;
	}
	/**
	 * ������ID
	 */
	public void setParentID(int p_ParentID) {
		this.mParentID = p_ParentID;
	}
	/**
	 * ·��
	 */
	public String getPath() {
		return mPath;
	}
	/**
	 * ·��
	 */
	public void setPath(String p_Path) {
		this.mPath = p_Path;
	}
	/**
	 * �������
	 */
	public Date getCreateDate() {
		return mCreateDate;
	}
	/**
	 * �������
	 */
	public void setCreateDate(Date p_CreateDate) {
		this.mCreateDate = p_CreateDate;
	}
	/**
	 * ״̬ 0ʧЧ 1����
	 */
	public int getState() {
		return mState;
	}
	/**
	 * ״̬ 0ʧЧ 1����
	 */
	public void setState(int p_State) {
		this.mState = p_State;
	}
	
	@Override
	public String toString() {
		return mCategoryName;
	}
}

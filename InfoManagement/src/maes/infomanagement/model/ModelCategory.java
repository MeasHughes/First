package maes.infomanagement.model;

import java.io.Serializable;
import java.util.Date;

public class ModelCategory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4493312582968339458L;
	//类别表主键ID
	private int mCategoryID;
	//类别名称
	private String mCategoryName;
	//类型标记名称
	private String mTypeFlag;
	//父类型ID
	private int mParentID = 0;
	//路径
	private String mPath;	
	//添加日期
	private Date mCreateDate = new Date();
	//状态 0失效 1启用
	private int mState = 1;
	/**
	 * 账本表主键ID
	 */
	public int getCategoryID() {
		return mCategoryID;
	}
	/**
	 * 账本表主键ID
	 */
	public void setCategoryID(int p_CategoryID) {
		this.mCategoryID = p_CategoryID;
	}
	/**
	 * 账本名称
	 */
	public String getCategoryName() {
		return mCategoryName;
	}
	/**
	 * 账本名称
	 */
	public void setCategoryName(String p_CategoryName) {
		this.mCategoryName = p_CategoryName;
	}
	/**
	 * 类型标记名称
	 */
	public String getTypeFlag() {
		return mTypeFlag;
	}
	/**
	 * 类型标记名称
	 */
	public void setTypeFlag(String p_TypeFlag) {
		this.mTypeFlag = p_TypeFlag;
	}
	/**
	 * 父类型ID
	 */
	public int getParentID() {
		return mParentID;
	}
	/**
	 * 父类型ID
	 */
	public void setParentID(int p_ParentID) {
		this.mParentID = p_ParentID;
	}
	/**
	 * 路径
	 */
	public String getPath() {
		return mPath;
	}
	/**
	 * 路径
	 */
	public void setPath(String p_Path) {
		this.mPath = p_Path;
	}
	/**
	 * 添加日期
	 */
	public Date getCreateDate() {
		return mCreateDate;
	}
	/**
	 * 添加日期
	 */
	public void setCreateDate(Date p_CreateDate) {
		this.mCreateDate = p_CreateDate;
	}
	/**
	 * 状态 0失效 1启用
	 */
	public int getState() {
		return mState;
	}
	/**
	 * 状态 0失效 1启用
	 */
	public void setState(int p_State) {
		this.mState = p_State;
	}
	
	@Override
	public String toString() {
		return mCategoryName;
	}
}

package maes.infomanagement.bussiness;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import maes.infomanagement.bussiness.base.BussinessBase;
import maes.infomanagement.database.sqlitedal.SQLiteDALUser;
import maes.infomanagement.model.ModelUser;

public class BussinessUser extends BussinessBase{

	private Context mContext;
	private SQLiteDALUser mSqLiteDALUser;
	
	public BussinessUser(Context pContext) {
		super(pContext);
		mSqLiteDALUser = new SQLiteDALUser(pContext);
	}

	public boolean insertUser(ModelUser pModelUser)
	{
		boolean _result = mSqLiteDALUser.insertUser(pModelUser);
		return _result;
	}
	
	public Boolean hideUserByUserID(int p_UserID) {
		String _Condition = " UserID = " + p_UserID;
		ContentValues contentValues = new ContentValues();
		contentValues.put("State", 0);
		Boolean result = mSqLiteDALUser.updateUser(_Condition, contentValues);

		if (result) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean updateUser(ModelUser pModelUser)
	{

		String _condition = " UserID = " + pModelUser.getmUserID();
		boolean _result = mSqLiteDALUser.updateUser(_condition, pModelUser);
		return _result;
	}
	
	public boolean deleteUser(int userID)
	{
		String _condition = "AND UserID = " + userID;
		boolean _result = mSqLiteDALUser.deleteUser(_condition);
		return _result;
	}
	
	public List<ModelUser> getNotHideUser()
	{
		return mSqLiteDALUser.getUser("AND State = 1");
	}
	
	private List<ModelUser> getUser(String pCondition)
	{
		return mSqLiteDALUser.getUser(pCondition);
	}
	
	public ModelUser getUserByID(int userID)
	{
		List<ModelUser> _List = mSqLiteDALUser.getUser(" AND UserID = " + userID);
		if(_List.size() == 1)
		{
			return _List.get(0);
		}
		else{
			return null;
		}
	}
	
	public List<ModelUser> getUserListByUserID(String userID[])
	{
		List<ModelUser> _List = new ArrayList<ModelUser>();
		for(int i = 0;i < userID.length;i ++)
		{
			_List.add(getUserByID(Integer.valueOf(userID[i])));
		}
		return _List;
	}
	
	public boolean isExistUserByUserName(String pUserName, Integer pUserID) {
		String _Condition = " And UserName = '" + pUserName + "'";
		if (pUserID != null) {
			_Condition += " And UserID <> " + pUserID;
		}
		List _List = mSqLiteDALUser.getUser(_Condition);
		if (_List.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getUserNameByUserID(String userID) {
		List<ModelUser> list = getUserListByUserID(userID.split(","));
		String name = "";

		for (int i = 0; i < list.size(); i++) {
			name += list.get(i).getmUserName() + ",";
		}
		return name;
	}
}

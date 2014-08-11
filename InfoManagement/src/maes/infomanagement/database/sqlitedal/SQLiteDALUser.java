package maes.infomanagement.database.sqlitedal;

import java.util.Date;
import java.util.List;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import maes.infomanagement.R;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.AvoidXfermode.Mode;
import maes.infomanagement.database.base.SQLiteDALBase;
import maes.infomanagement.model.ModelUser;

public class SQLiteDALUser extends SQLiteDALBase {
	
	private static final int INIT_STATE = 1;
	
	public SQLiteDALUser(Context pContext)
	{
		super(pContext);
		
	}
	

	@Override
	protected String[] getTableNameAndPK() {
		String[] _Strings = new String[]{"User", "UserID"};
		return _Strings;
	}
	
	
	
	public boolean insertUser(ModelUser pModelUser)
	{
		ContentValues _ContentValues = createParams(pModelUser);
		long _userID = getDataBase().insert(getTableNameAndPK()[0], null, _ContentValues);
		pModelUser.setmUserID((int)_userID);
		return (_userID > 0);
	}
	
	public boolean deleteUser(String pCondition)
	{
		return delete(getTableNameAndPK()[0], pCondition);
	}
	
	/**
	 * 根据contentValuse修改用户信息
	 * @param p_Condition
	 * @param pContentValues
	 * @return
	 */
	public Boolean updateUser(String p_Condition,ContentValues pContentValues){
		return getDataBase().update("User", pContentValues, p_Condition, null) > 0;
	}
	
	public boolean updateUser(String pCondition,ModelUser pModelUser)
	{
		ContentValues _ContentValues = createParams(pModelUser);
		return getDataBase().update(getTableNameAndPK()[0], _ContentValues, pCondition, null) > 0;
	}
	
	public List<ModelUser> getUser(String pCondition)
	{
		String _sqlString = "select * from User where 1=1 " + pCondition;
		return getList(_sqlString);
	}

	
	private void initDefaultDatabase(SQLiteDatabase sqLiteDatabase)
	{
		ModelUser _ModelUser = new ModelUser();
		String _userName[] = getContext().getResources().getStringArray(R.array.array_initarray);
		
		for(int i = 0;i < _userName.length;i ++)
		{
			_ModelUser.setmUserName(_userName[i]);
			_ModelUser.setState(1);
			ContentValues _ContentValues = createParams(_ModelUser);
			sqLiteDatabase.insert(getTableNameAndPK()[0], null, _ContentValues);
		}
		
	}

	@Override
	protected Object findModel(Cursor pCursor) {
		ModelUser _ModelUser = new ModelUser();
		_ModelUser.setmUserID(Integer.valueOf(pCursor.getString(pCursor.getColumnIndex("UserID"))));
		_ModelUser.setmUserName(pCursor.getString(pCursor.getColumnIndex("UserName")));
		Date _pDate = null;
		try {
			_pDate = DateUtils.parseDate(pCursor.getString(pCursor.getColumnIndex("CreateDate")));
		} catch (DateParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_ModelUser.setmDate(_pDate);
		_ModelUser.setState(pCursor.getInt(pCursor.getColumnIndex("state")));
		
		return _ModelUser;
	}



	@Override
	public void onCreate(SQLiteDatabase pDatabase) {
		StringBuffer _createTableSQL = new StringBuffer();
		_createTableSQL.append(" Create  TABLE User( ");
		_createTableSQL.append(" [UserID] Integer PRIMARY KEY AUTOINCREMENT NOT NULL ");
		_createTableSQL.append(" ,[CreateDate] datetime NOT NULL ");
		_createTableSQL.append(" ,[state] Integer NOT NULL ");
		_createTableSQL.append(" ,[UserName] varchar(10) NOT NULL ");
		_createTableSQL.append(" );");
		
		pDatabase.execSQL(_createTableSQL.toString());
		initDefaultDatabase(pDatabase);
	}



	@Override
	public void onUpdate(SQLiteDatabase pDatabase) {
		// TODO Auto-generated method stub
		
	}
	
	public ContentValues createParams(ModelUser pModelUser)
	{
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put("UserName", pModelUser.getmUserName());
		_ContentValues.put("CreateDate", DateUtils.formatDate(pModelUser.getmDate()));
		_ContentValues.put("State", INIT_STATE);
		return _ContentValues;
	}

}

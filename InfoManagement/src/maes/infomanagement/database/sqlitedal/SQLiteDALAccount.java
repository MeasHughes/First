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
import maes.infomanagement.model.ModelAccount;

public class SQLiteDALAccount extends SQLiteDALBase {
	
	public SQLiteDALAccount(Context pContext)
	{
		super(pContext);
		
	}
	

	@Override
	protected String[] getTableNameAndPK() {
		String[] _Strings = new String[]{"Account", "AccountBookID"};
		return _Strings;
	}
	
	
	
	public boolean insertAccount(ModelAccount pModelAccount)
	{
		ContentValues _ContentValues = createParams(pModelAccount);
		long _AccountID = getDataBase().insert(getTableNameAndPK()[0], null, _ContentValues);
		pModelAccount.setAccountBookID((int)_AccountID);
		return (_AccountID > 0);
	}
	
	public boolean deleteAccount(String pCondition)
	{
		return delete(getTableNameAndPK()[0], pCondition);
	}
	
	/**
	 * 根据contentValuse修改用户信息
	 * @param p_Condition
	 * @param pContentValues
	 * @return
	 */
	public Boolean updateAccount(String p_Condition,ContentValues pContentValues){
		return getDataBase().update("Account", pContentValues, p_Condition, null) > 0;
	}
	
	public boolean updateAccount(String pCondition,ModelAccount pModelAccount)
	{
		ContentValues _ContentValues = createParams(pModelAccount);
		return getDataBase().update(getTableNameAndPK()[0], _ContentValues, pCondition, null) > 0;
	}
	
	public List<ModelAccount> getAccount(String pCondition)
	{
		String _sqlString = "select * from Account where 1=1 " + pCondition;
		return getList(_sqlString);
	}

	
	private void initDefaultDatabase(SQLiteDatabase sqLiteDatabase)
	{
		ModelAccount _ModelAccount = new ModelAccount();
		String _accountName[] = getContext().getResources().getStringArray(R.array.InitDefaultDataAccountBookName);
		
		for(int i = 0;i < _accountName.length;i ++)
		{
			_ModelAccount.setAccountBookName(_accountName[i]);
			_ModelAccount.setIsDefault(1);
			ContentValues _ContentValues = createParams(_ModelAccount);
			sqLiteDatabase.insert(getTableNameAndPK()[0], null, _ContentValues);
		}
		
	}

	@Override
	protected Object findModel(Cursor pCursor) {
		ModelAccount _ModelAccount = new ModelAccount();
		_ModelAccount.setAccountBookID(Integer.valueOf(pCursor.getString(pCursor.getColumnIndex("AccountBookID"))));
		_ModelAccount.setAccountBookName(pCursor.getString(pCursor.getColumnIndex("AccountBookName")));
		Date _pDate = null;
		try {
			_pDate = DateUtils.parseDate(pCursor.getString(pCursor.getColumnIndex("CreateDate")));
		} catch (DateParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		_ModelAccount.setCreateDate(_pDate);
		_ModelAccount.setState(pCursor.getInt(pCursor.getColumnIndex("State")));
		_ModelAccount.setIsDefault(pCursor.getInt(pCursor.getColumnIndex("IsDefault")));
		
		return _ModelAccount;
	}



	@Override
	public void onCreate(SQLiteDatabase pDatabase) {
		StringBuffer s_CreateTableScript = new StringBuffer();
		s_CreateTableScript.append("		Create  TABLE Account(");
		s_CreateTableScript.append("				[AccountBookID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		s_CreateTableScript.append("				,[AccountBookName] varchar(20) NOT NULL");
		s_CreateTableScript.append("				,[CreateDate] datetime NOT NULL");
		s_CreateTableScript.append("				,[State] integer NOT NULL");
		s_CreateTableScript.append("				,[IsDefault] integer NOT NULL");
		s_CreateTableScript.append("				)");
		
		pDatabase.execSQL(s_CreateTableScript.toString());
		initDefaultDatabase(pDatabase);
	}



	@Override
	public void onUpdate(SQLiteDatabase pDatabase) {
		// TODO Auto-generated method stub
		
	}
	
	public ContentValues createParams(ModelAccount pModelAccount)
	{
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put("AccountBookName", pModelAccount.getAccountBookName());
		_ContentValues.put("CreateDate", DateUtils.formatDate(pModelAccount.getCreateDate()));
		_ContentValues.put("State", pModelAccount.getState());
		_ContentValues.put("IsDefault",pModelAccount.getIsDefault());
		return _ContentValues;
	}

}

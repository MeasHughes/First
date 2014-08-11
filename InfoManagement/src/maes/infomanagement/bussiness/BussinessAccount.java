package maes.infomanagement.bussiness;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import maes.infomanagement.bussiness.base.BussinessBase;
import maes.infomanagement.database.sqlitedal.SQLiteDALAccount;
import maes.infomanagement.database.sqlitedal.SQLiteDALAccount;
import maes.infomanagement.model.ModelAccount;

public class BussinessAccount extends BussinessBase{

	private Context mContext;
	private SQLiteDALAccount mSQLiteDALAccount;
	
	public BussinessAccount(Context pContext) {
		super(pContext);
		mSQLiteDALAccount = new SQLiteDALAccount(pContext);
	}

	public boolean insertAccount(ModelAccount pModelAccount)
	{
		mSQLiteDALAccount.bigginTransaction();
		try {
			Boolean _Result = mSQLiteDALAccount.insertAccount(pModelAccount);
			Boolean _Result2 = true;
			if (pModelAccount.getIsDefault() == 1 && _Result) {
				_Result2 = setIsDefault(pModelAccount.getAccountBookID());
			}

			if (_Result && _Result2) {
				mSQLiteDALAccount.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALAccount.endTransaction();
		}
	}
	
	public Boolean hideAccountByAccountID(int pAccountBookID) {
		String _Condition = " mAccountBookID = " + pAccountBookID;
		ContentValues contentValues = new ContentValues();
		contentValues.put("State", 0);
		Boolean result = mSQLiteDALAccount.updateAccount(_Condition, contentValues);

		if (result) {
			return true;
		} else {
			return false;
		}
	}
	
	public ModelAccount getDefaultModelAccountBook() {
		List _List = mSQLiteDALAccount.getAccount(" And IsDefault = 1");
		if (_List.size() == 1) {
			return (ModelAccount) _List.get(0);
		} else {
			return null;
		}
	}
	
	public boolean hasDefaultModelAccountBook() {
		List _List = mSQLiteDALAccount.getAccount(" And IsDefault = 1");
		if (_List.size() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean updateAccount(ModelAccount pModelAccount)
	{

		String _condition = " AccountBookID = " + pModelAccount.getAccountBookID();
		boolean _result = mSQLiteDALAccount.updateAccount(_condition, pModelAccount);
		return _result;
	}
	
	public boolean deleteAccount(int accountID)
	{
		String _condition = "AND AccountBookID = " + accountID;
		boolean _result = mSQLiteDALAccount.deleteAccount(_condition);
		return _result;
	}
	
	public Boolean deleteAccountBookByAccountBookID(int accountBookID) {
		mSQLiteDALAccount.bigginTransaction();
		try {
			String condition = " And AccountBookID = " + accountBookID;
			Boolean result = mSQLiteDALAccount.deleteAccount(condition);
			Boolean result2 = true;
			if (result) {
				mSQLiteDALAccount.setTransactionSuccessful();
				return true;
			}else {
				return false;
			}
			 
			/*if (result) {
				BusinessPayout businessPayout = new BusinessPayout(getContext());
				result2 = businessPayout.deletePayoutByAccountBookID(accountBookID);
			}
			if (result && result2) {
				mSQLiteDALAccount.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}*/
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALAccount.endTransaction();
		}
	}
	
	public Boolean updateAccountBookByAccountBookID(ModelAccount pInfo) {
		mSQLiteDALAccount.bigginTransaction();
		try {
			String _Condition = " AccountBookID = " + pInfo.getAccountBookID();
			Boolean _Result = mSQLiteDALAccount.updateAccount(_Condition, pInfo);
			Boolean _Result2 = true;
			if (pInfo.getIsDefault() == 1 ) {
				_Result2 = setIsDefault(pInfo.getAccountBookID());
			}

			if (_Result && _Result2) {
				mSQLiteDALAccount.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALAccount.endTransaction();
		}
	}
	
	public Boolean setIsDefault(int pID) {
		String condition = " IsDefault = 1";
		ContentValues contentValues = new ContentValues();
		contentValues.put("IsDefault", 0);
		Boolean result = mSQLiteDALAccount.updateAccount(condition, contentValues);

		condition = " AccountBookID = " + pID;
		contentValues.clear();
		contentValues.put("IsDefault", 1);
		Boolean result2 = mSQLiteDALAccount.updateAccount(condition, contentValues);

		if (result && result2) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<ModelAccount> getNotHideAccount()
	{
		return mSQLiteDALAccount.getAccount("AND State = 1");
	}
	
	public List<ModelAccount> getAccount(String pCondition)
	{
		return mSQLiteDALAccount.getAccount(pCondition);
	}
	
	public ModelAccount getAccountByID(int AccountID)
	{
		List<ModelAccount> _List = mSQLiteDALAccount.getAccount(" AND AccountBookID = " + AccountID);
		if(_List.size() == 1)
		{
			return _List.get(0);
		}
		else{
			return null;
		}
	}
	
	public List<ModelAccount> getAccountListByAccountID(String AccountID[])
	{
		List<ModelAccount> _List = new ArrayList<ModelAccount>();
		for(int i = 0;i < AccountID.length;i ++)
		{
			_List.add(getAccountByID(Integer.valueOf(AccountID[i])));
		}
		return _List;
	}
	
	public boolean isExistAccountByAccountName(String pAccountName, Integer pAccountID) {
		String _Condition = " And AccountBookName = '" + pAccountName + "'";
		if (pAccountID != null) {
			_Condition += " And AccountBookID <> " + pAccountID;
		}
		List _List = mSQLiteDALAccount.getAccount(_Condition);
		if (_List.size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getAccountBookNameByAccountId(int pBookId) {
		String conditionString = "And AccountBookID = " + String.valueOf(pBookId);
		List<ModelAccount> _AccountBooks = mSQLiteDALAccount.getAccount(conditionString);
		return _AccountBooks.get(0).getAccountBookName();
	}
}

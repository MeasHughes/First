package maes.infomanagement.database.base;

import java.util.ArrayList;
import java.util.List;

import maes.infomanagement.database.base.SQLiteHelper.SQLiteDataTable;
import android.R.string;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class SQLiteDALBase implements SQLiteDataTable{
	private static Context mContext;
	private static SQLiteDatabase mInstance;
	
	protected SQLiteDALBase(Context pContext)
	{
		mContext = pContext;
	}
	
	public static SQLiteDatabase getDataBase()
	{
		if(mInstance == null)
		{
			mInstance = SQLiteHelper.getInstance(mContext).getWritableDatabase();
		}
		return mInstance;
	}
	
	public static Context getContext()
	{
		return mContext;
	}
	
	
	
	
	public void bigginTransaction()
	{
		mInstance.beginTransaction();
	}
	
	public void setTransactionSuccessful()
	{
		mInstance.setTransactionSuccessful();
	}
	
	public void endTransaction()
	{
		mInstance.endTransaction();
	}
	
	protected List getList(String pSqlString)
	{
		Cursor cursor = execSQL(pSqlString);
		return cursorToList(cursor);
	}
	
	public Cursor execSQL(String pSqlString)
	{
		SQLiteDatabase _SqLiteDatabase =  getDataBase();
		return _SqLiteDatabase.rawQuery(pSqlString, null);
	}
	
	protected List cursorToList(Cursor pCursor)
	{
		List _list = new ArrayList();
		while(pCursor.moveToNext())
		{
			Object _Object = findModel(pCursor);
			_list.add(_Object);
		}
		pCursor.close();
		return _list;
	}
	
	protected boolean delete(String pTableName,String pCondition)
	{
		return getDataBase().delete(pTableName, " 1 = 1 " + pCondition, null) > 0;
	}
	
	protected abstract String[] getTableNameAndPK();
	
	protected abstract Object findModel(Cursor pCursor);
	
	/**
	 * 查询一共有多少条数据
	 * 
	 * @param condition
	 * @return
	 */
	public int getCount(String condition) {
		String _String[] = getTableNameAndPK();
		Cursor cursor = execSQL("Select " + _String[1] + " From " + _String[0]
				+ " Where 1=1 " + condition);
		int _Count = cursor.getCount();
		cursor.close();
		return _Count;
	}
}

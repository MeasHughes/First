package maes.infomanagement.database.base;

import java.util.ArrayList;

import maes.infomanagement.R;

import android.content.Context;
import android.text.GetChars;

public class SQLiteDataBaseConfig {
	private static final String NAME_DATABASE = "InfoManagement";
	private static int DATABASE_VERSION = 1;
	private static SQLiteDataBaseConfig mInstance;
	private static Context mContext;
	
	SQLiteDataBaseConfig()
	{
		
	}
	
	public static SQLiteDataBaseConfig getInstance(Context pContext)
	{	
		if(mInstance == null)
		{
			mInstance = new SQLiteDataBaseConfig();
			mContext = pContext;
		}
		return mInstance;
		
	}
	
	public String getDBName()
	{
		return NAME_DATABASE;
	}
	
	public int getVersion()
	{
		return DATABASE_VERSION;
	}
	
	public ArrayList<String> getTables()
	{
		ArrayList<String> _ArrayList = new ArrayList<String>();
		
		String _SQLiteDALClassNameString[] = mContext.getResources().getStringArray(R.array.array_className);
		String _packagePath = mContext.getPackageName() + ".database.sqlitedal." ;
		
		for(int i = 0;i < _SQLiteDALClassNameString.length;i ++)
		{
			
			_ArrayList.add(_packagePath + _SQLiteDALClassNameString[i]);
		}
		return _ArrayList;
	}
}

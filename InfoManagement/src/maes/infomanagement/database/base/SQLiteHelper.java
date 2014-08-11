package maes.infomanagement.database.base;

import java.util.ArrayList;

import maes.infomanagement.util.Reflection;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

	private static SQLiteDataBaseConfig mBaseConfig;
	private static SQLiteHelper mInstance;
	private Context mContext;
	private Reflection mReflaction;

	/**
	 * 定义接口
	 * @author Administrator
	 *
	 */
	public interface SQLiteDataTable
	{
		public void onCreate(SQLiteDatabase pDatabase);
		public void onUpdate(SQLiteDatabase pDatabase);
	}



	private SQLiteHelper(Context pContext) {
		super(pContext, mBaseConfig.getDBName(),null, mBaseConfig.getVersion());
		mContext = pContext;
	}

	public static SQLiteHelper getInstance(Context pContext)
	{
		if(mInstance == null)
		{
			mBaseConfig = SQLiteDataBaseConfig.getInstance(pContext);
			mInstance = new SQLiteHelper(pContext);
		}

		return mInstance;
	}


	/**
	 * 定义个接口 SQLiteDataTable，通过反射拿到诸如User，Category之类的SQL操作类,从而取得实例（就可以调用它们的方法）
	 * 调用的时候先调用该类中的_sqLiteDataTable.onCreate(pSqLiteDatabase);
	 * 在这些类中复写OnCreate方法，在这里调用onCreate方法之后就会分别到SQL操作类中调用复写的onCreate方法
	 * 所以各自实现各自的onCreate方法
	 */
	@Override
	public void onCreate(SQLiteDatabase pSqLiteDatabase) {
		mReflaction = new Reflection();
		ArrayList<String> _arrayList = mBaseConfig.getTables();
		for(int i = 0;i < _arrayList.size();i ++)
		{
			try {
				SQLiteDataTable _sqLiteDataTable = (SQLiteDataTable) mReflaction.newInstance(_arrayList.get(i),
						new Object[]{mContext}, new Class[]{Context.class});
				_sqLiteDataTable.onCreate(pSqLiteDatabase);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

}

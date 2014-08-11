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
	 * ����ӿ�
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
	 * ������ӿ� SQLiteDataTable��ͨ�������õ�����User��Category֮���SQL������,�Ӷ�ȡ��ʵ�����Ϳ��Ե������ǵķ�����
	 * ���õ�ʱ���ȵ��ø����е�_sqLiteDataTable.onCreate(pSqLiteDatabase);
	 * ����Щ���и�дOnCreate���������������onCreate����֮��ͻ�ֱ�SQL�������е��ø�д��onCreate����
	 * ���Ը���ʵ�ָ��Ե�onCreate����
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

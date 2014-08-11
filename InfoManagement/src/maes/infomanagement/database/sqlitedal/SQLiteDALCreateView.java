package maes.infomanagement.database.sqlitedal;


import maes.infomanagement.database.base.SQLiteHelper.SQLiteDataTable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDALCreateView implements SQLiteDataTable {

	private Context mContext;

	public SQLiteDALCreateView(Context context) {
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase pSqLiteDatabase) {
		StringBuilder s_CreateTableScript = new StringBuilder();

		s_CreateTableScript.append("Create View v_Payout As ");
		s_CreateTableScript.append("select a.*,b.ParentID,b.categoryname,b.Path,b.TypeFlag,c.AccountBookName  ");
		s_CreateTableScript.append("from Payout a ");
		s_CreateTableScript.append("LEFT JOIN Category b ON a.categoryID = b.categoryID    ");
		s_CreateTableScript.append("LEFT JOIN Account c ON a.AccountBookID = c.AccountBookID ");
		pSqLiteDatabase.execSQL(s_CreateTableScript.toString());
	}

	@Override
	public void onUpdate(SQLiteDatabase pDatabase) {
		// TODO Auto-generated method stub
		
	}


}

package maes.infomanagement.database.sqlitedal;



import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import maes.infomanagement.database.base.SQLiteDALBase;
import maes.infomanagement.model.ModelPayout;
import maes.infomanagement.util.DateTools;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDALPayout extends SQLiteDALBase {

	public SQLiteDALPayout(Context pContext) {
		super(pContext);
	}

	public Boolean insertPayout(ModelPayout payout) {
		ContentValues contentValues = CreatParms(payout);
		Long pNewID = getDataBase().insert("Payout", null, contentValues);
		payout.setPayoutID(pNewID.intValue());
		return pNewID > 0;
	}

	public Boolean deletePayout(String condition) {
		return delete(getTableNameAndPK()[0], condition);
	}

	public Boolean updatePayout(String condition, ModelPayout info) {
		ContentValues _ContentValues = CreatParms(info);
		return getDataBase().update("Payout", _ContentValues, condition, null) > 0;
	}

	public Boolean updatePayout(String condition, ContentValues contentValues) {
		return getDataBase().update("Payout", contentValues, condition, null) > 0;
	}

	public List<ModelPayout> getPayout(String condition) {
		String sqlText = "Select * From v_Payout Where  1=1 " + condition;
		return getList(sqlText);
	}
	
	
	
	@Override
	public void onCreate(SQLiteDatabase pSqLiteDatabase) {
		StringBuilder createTableScript = new StringBuilder();

		createTableScript.append("		Create  TABLE Payout(");
		createTableScript.append("				[PayoutID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		createTableScript.append("				,[AccountBookID] integer NOT NULL");
		createTableScript.append("				,[CategoryID] integer NOT NULL");
		createTableScript.append("				,[PayWayID] integer");
		createTableScript.append("				,[PlaceID] integer");
		createTableScript.append("				,[Amount] decimal NOT NULL");
		createTableScript.append("				,[PayoutDate] datetime NOT NULL");
		createTableScript.append("				,[PayoutType] varchar(20) NOT NULL");
		createTableScript.append("				,[PayoutUserID] text NOT NULL");
		createTableScript.append("				,[Comment] text");
		createTableScript.append("				,[CreateDate] datetime NOT NULL");
		createTableScript.append("				,[State] integer NOT NULL");
		createTableScript.append("				)");

		pSqLiteDatabase.execSQL(createTableScript.toString());
	}


	@Override
	protected String[] getTableNameAndPK() {
		return new String[] { "Payout", "PayoutID" };
	}

	@Override
	protected Object findModel(Cursor pCursor) {
		ModelPayout payout = new ModelPayout();
		payout.setPayoutID(pCursor.getInt(pCursor.getColumnIndex("PayoutID")));
		payout.setAccountBookID(pCursor.getInt((pCursor.getColumnIndex("AccountBookID"))));
		payout.setAccountBookName((pCursor.getString(pCursor.getColumnIndex("AccountBookName"))));
		payout.setCategoryID(pCursor.getInt((pCursor.getColumnIndex("CategoryID"))));
		payout.setCategoryName((pCursor.getString(pCursor.getColumnIndex("CategoryName"))));
		payout.setPath((pCursor.getString(pCursor.getColumnIndex("Path"))));
		payout.setPayWayID(pCursor.getInt((pCursor.getColumnIndex("PayWayID"))));
		payout.setPlaceID(pCursor.getInt((pCursor.getColumnIndex("PlaceID"))));
		payout.setAmount(new BigDecimal(pCursor.getString(((pCursor.getColumnIndex("Amount"))))));
		Date payoutDate = DateTools.getDate(pCursor.getString(pCursor.getColumnIndex("PayoutDate")), "yyyy-MM-dd");
		payout.setPayoutDate(payoutDate);
		payout.setPayoutType((pCursor.getString(pCursor.getColumnIndex("PayoutType"))));
		payout.setPayoutUserID((pCursor.getString(pCursor.getColumnIndex("PayoutUserID"))));
		payout.setComment((pCursor.getString(pCursor.getColumnIndex("Comment"))));
		Date createDate = DateTools.getDate(pCursor.getString(pCursor.getColumnIndex("CreateDate")),
				"yyyy-MM-dd HH:mm:ss");
		payout.setCreateDate(createDate);
		payout.setState((pCursor.getInt(pCursor.getColumnIndex("State"))));

		return payout;
	}

	public ContentValues CreatParms(ModelPayout pInfo) {

		ContentValues contentValues = new ContentValues();
		contentValues.put("AccountBookID", pInfo.getAccountBookID());
		contentValues.put("CategoryID", pInfo.getCategoryID());
		contentValues.put("PayWayID", pInfo.getPayWayID());
		contentValues.put("PlaceID", pInfo.getPlaceID());
		contentValues.put("Amount", pInfo.getAmount().toString());
		contentValues.put("PayoutDate", DateTools.getFormatDateTime(pInfo.getPayoutDate(), "yyyy-MM-dd"));
		contentValues.put("PayoutType", pInfo.getPayoutType());
		contentValues.put("PayoutUserID", pInfo.getPayoutUserID());
		contentValues.put("Comment", pInfo.getComment());
		contentValues.put("CreateDate", DateTools.getFormatDateTime(pInfo.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		contentValues.put("State", pInfo.getState());

		return contentValues;
	}

	@Override
	public void onUpdate(SQLiteDatabase pDatabase) {
		// TODO Auto-generated method stub
		
	}

	
}

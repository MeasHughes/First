package maes.infomanagement.database.sqlitedal;

import java.util.Date;
import java.util.List;

import maes.infomanagement.R;
import maes.infomanagement.database.base.SQLiteDALBase;
import maes.infomanagement.model.ModelCategory;
import maes.infomanagement.util.DateTools;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDALCategory extends SQLiteDALBase {

	public SQLiteDALCategory(Context context) {
		super(context);
	}
	@Override
	public void onCreate(SQLiteDatabase pSqLiteDatabase) {
		//建立数据库
		StringBuilder s_CreateTableScript = new StringBuilder();

		s_CreateTableScript.append("		Create  TABLE Category(");
		s_CreateTableScript.append("				[CategoryID] integer PRIMARY KEY AUTOINCREMENT NOT NULL");
		s_CreateTableScript.append("				,[CategoryName] varchar(20) NOT NULL");
		s_CreateTableScript.append("				,[TypeFlag] varchar(20) NOT NULL");
		s_CreateTableScript.append("				,[ParentID] integer NOT NULL");
		s_CreateTableScript.append("				,[Path] text NOT NULL");
		s_CreateTableScript.append("				,[CreateDate] datetime NOT NULL");
		s_CreateTableScript.append("				,[State] integer NOT NULL");
		s_CreateTableScript.append("				)");

		pSqLiteDatabase.execSQL(s_CreateTableScript.toString());
		//初始化数据
		initDefaultData(pSqLiteDatabase);

	}

	/**
	 * 初始化数据
	 * @param pSqLiteDatabase
	 */
	private void initDefaultData(SQLiteDatabase pSqLiteDatabase) {
		
		ModelCategory _ModelCategory=  new ModelCategory();
		_ModelCategory.setTypeFlag(getContext().getString(R.string.PayoutTypeFlag));
		_ModelCategory.setPath("");
		_ModelCategory.setParentID(0);
		String _InitDefaultCategoryNameArr[] = getContext().getResources().getStringArray(R.array.InitDefaultCategoryName);
		for (int i = 0; i < _InitDefaultCategoryNameArr.length; i++) {
			_ModelCategory.setCategoryName(_InitDefaultCategoryNameArr[i]);
			ContentValues _ContentValues= creatParms(_ModelCategory);
			Long _NewID= pSqLiteDatabase.insert(getTableNameAndPK()[0], null, _ContentValues);
			
			_ModelCategory.setPath(_NewID.intValue() +"");
			_ContentValues= creatParms(_ModelCategory);
			pSqLiteDatabase.update(getTableNameAndPK()[0], _ContentValues, " CategoryID = " + _NewID.intValue(), null);
		}
	}
	private ContentValues creatParms(ModelCategory pModelCategory) {
		ContentValues _ContentValues = new ContentValues();
		_ContentValues.put("CategoryName", pModelCategory.getCategoryName());
		_ContentValues.put("TypeFlag", pModelCategory.getTypeFlag());
		_ContentValues.put("ParentID", pModelCategory.getParentID());
		_ContentValues.put("Path", pModelCategory.getPath());
		_ContentValues.put("CreateDate",DateTools.getFormatDateTime(pModelCategory.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
		_ContentValues.put("State",pModelCategory.getState());
		return _ContentValues;
	}

	@Override
	protected String[] getTableNameAndPK() {
		return new String[]{"Category","CategoryID"};
	}

	@Override
	protected ModelCategory findModel(Cursor pCursor) {
		
		ModelCategory _ModelCategory = new ModelCategory();
		_ModelCategory.setCategoryID(pCursor.getInt(pCursor.getColumnIndex("CategoryID")));
		_ModelCategory.setCategoryName(pCursor.getString(pCursor.getColumnIndex("CategoryName")));
		_ModelCategory.setTypeFlag(pCursor.getString(pCursor.getColumnIndex("TypeFlag")));
		_ModelCategory.setParentID(pCursor.getInt(pCursor.getColumnIndex("ParentID")));
		_ModelCategory.setPath(pCursor.getString(pCursor.getColumnIndex("Path")));
		Date _CreateDate = DateTools.getDate(pCursor.getString(pCursor.getColumnIndex("CreateDate")), "yyyy-MM-dd HH:mm:ss");	
		_ModelCategory.setCreateDate(_CreateDate);
		_ModelCategory.setState((pCursor.getInt(pCursor.getColumnIndex("State"))));
		
		return _ModelCategory;
	}

	/**
	 * 插入数据
	 * @param modelCategory
	 * @return
	 */
	public Boolean insertCategory(ModelCategory modelCategory){
		ContentValues contentValues = creatParms(modelCategory);
		Long newID = getDataBase().insert(getTableNameAndPK()[0], null,contentValues);
		modelCategory.setCategoryID(newID.intValue());
		return newID > 0;
	}
	
	public Boolean deleteCategory(String condition) {
		
		return delete(getTableNameAndPK()[0], condition);
		
	}
	
	public Boolean updateCategory(String condition,ModelCategory modelCategory) {
		
		ContentValues contentValues = creatParms(modelCategory);
		return getDataBase().update(getTableNameAndPK()[0], contentValues, condition, null) >0;
		
	}
	
	public Boolean updateCategory(String condition,ContentValues contentValues){
		
		return getDataBase().update(getTableNameAndPK()[0], contentValues, condition, null) > 0;
		
	}
	
	public List<ModelCategory> getCategory(String condition) {
		
		String sqlText = "SELECT * FROM Category WHERE  1=1 " + condition;
		return getList(sqlText);
		
	}
	@Override
	public void onUpdate(SQLiteDatabase pDatabase) {
		// TODO Auto-generated method stub
		
	}
}

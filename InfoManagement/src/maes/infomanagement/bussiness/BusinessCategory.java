package maes.infomanagement.bussiness;

import java.util.ArrayList;
import java.util.List;


import maes.infomanagement.R;
import maes.infomanagement.bussiness.base.BussinessBase;
import maes.infomanagement.database.sqlitedal.SQLiteDALCategory;
import maes.infomanagement.model.ModelCategory;
import maes.infomanagement.model.ModelCategoryTotal;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;

public class BusinessCategory extends BussinessBase {

	private SQLiteDALCategory mSQLiteDALCategory;
	private final String TYPE_FLAG = " AND TypeFlag = '" + getsString(R.string.PayoutTypeFlag) + "'";

	public BusinessCategory(Context context) {
		super(context);
		mSQLiteDALCategory = new SQLiteDALCategory(context);
	}

	public List<ModelCategoryTotal> getCategoryTotalByRootCategory() {

		String condition = TYPE_FLAG + " And ParentID = 0 And State = 1";
		List<ModelCategoryTotal> modelCategoryTotalList = getCategoryTotal(condition);

		return modelCategoryTotalList;
	}
	
	private List<ModelCategoryTotal> getCategoryTotal(String condition) {

		Cursor cursor = mSQLiteDALCategory
				.execSQL("Select Count(PayoutID) As Count, Sum(Amount) As SumAmount, CategoryName From v_Payout Where 1=1 "
						+ condition + " Group By CategoryName");
		List<ModelCategoryTotal> modelCategoryTotalList = new ArrayList<ModelCategoryTotal>();
		while (cursor.moveToNext()) {
			ModelCategoryTotal modelCategoryTotal = new ModelCategoryTotal();
			modelCategoryTotal.Count = cursor.getString(cursor.getColumnIndex("Count"));
			modelCategoryTotal.SumAmount = cursor.getString(cursor.getColumnIndex("SumAmount"));
			modelCategoryTotal.CategoryName = cursor.getString(cursor.getColumnIndex("CategoryName"));
			modelCategoryTotalList.add(modelCategoryTotal);
		}

		return modelCategoryTotalList;
	}
	
	public Boolean hideCategoryByByPath(String p_Path) {
		String condition = " Path Like '" + p_Path + "%'";
		ContentValues contentValues = new ContentValues();
		contentValues.put("State", 0);
		Boolean _Result = mSQLiteDALCategory.updateCategory(condition, contentValues);

		if (_Result) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean insertCategory(ModelCategory modelCategory) {
		// 开启事务
		mSQLiteDALCategory.bigginTransaction();
		try {
			Boolean result = mSQLiteDALCategory.insertCategory(modelCategory);
			Boolean result2 = true;

			ModelCategory parentCategory = getCategoryByID(modelCategory.getCategoryID());
			// 生成路径
			String path;
			if (parentCategory != null) {
				path = parentCategory.getPath() + modelCategory.getCategoryID() + ".";
			} else {
				path = modelCategory.getCategoryID() + ".";
			}
			modelCategory.setPath(path);
			result2 = updateCategoryInsertTypeByID(modelCategory);
			if (result && result2) {
				// 标记事物成功
				mSQLiteDALCategory.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALCategory.endTransaction();
		}
	}

	public Boolean deleteCategoryByID(int categoryID) {
		String condition = "CategoryID = " + categoryID;
		return mSQLiteDALCategory.deleteCategory(condition);
	}

	/**
	 * 根据ID更新标记
	 * 
	 * @param modelCategory
	 * @return
	 */
	private Boolean updateCategoryInsertTypeByID(ModelCategory modelCategory) {

		String condition = " CategoryID  = " + modelCategory.getCategoryID();

		return mSQLiteDALCategory.updateCategory(condition, modelCategory);
	}

	/**
	 * 根据ID获取类别
	 * 
	 * @param categoryID
	 * @return
	 */
	public ModelCategory getCategoryByID(int categoryID) {
		List list = mSQLiteDALCategory.getCategory(TYPE_FLAG + " AND CategoryID = " + categoryID);
		if (list.size() == 1) {
			return (ModelCategory) list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据ID更新类别
	 * 
	 * @param modelCategory
	 * @return
	 */
	public Boolean updateCategoryByID(ModelCategory modelCategory) {
		mSQLiteDALCategory.bigginTransaction();

		try {
			String condition = " CategoryID = " + modelCategory.getCategoryID();
			Boolean result = mSQLiteDALCategory.updateCategory(condition, modelCategory);
			Boolean result2 = true;
			ModelCategory parentModelCategory = getCategoryByID(modelCategory.getCategoryID());
			String path;
			if (parentModelCategory != null) {
				path = parentModelCategory.getPath() + modelCategory.getCategoryID() + ".";
			} else {
				path = parentModelCategory.getCategoryID() + ".";
			}
			modelCategory.setPath(path);
			result2 = updateCategoryInsertTypeByID(modelCategory);
			if (result && result2) {
				mSQLiteDALCategory.setTransactionSuccessful();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		} finally {
			mSQLiteDALCategory.endTransaction();
		}

	}

	public ArrayAdapter getAllCategoryArrayAdapter() {
		/*
		 * List _List = GetNotHideCategory(); String _Name[] = new
		 * String[_List.size()]; for (int i = 0; i < _List.size(); i++) {
		 * _Name[i] = ((ModelCategory)_List.get(i)).GetCategoryName(); }
		 * ArrayAdapter _ArrayAdapter = new ArrayAdapter(GetContext(),
		 * R.layout.common_auto_complete, _List); return _ArrayAdapter;
		 */

		return null;
	}

	public Boolean hideCategoryByPath(String path) {

		String condition = " Path Like '" + path + "%'";
		ContentValues contentValues = new ContentValues();
		contentValues.put("State", 0);
		return mSQLiteDALCategory.updateCategory(condition, contentValues);

	}

	public List<ModelCategory> getCategory(String condition) {
		return mSQLiteDALCategory.getCategory(condition);
	}

	public List<ModelCategory> getotHideCategory() {
		return mSQLiteDALCategory.getCategory(TYPE_FLAG + " And State = 1");
	}

	public int getNotHideCount() {
		return mSQLiteDALCategory.getCount(TYPE_FLAG + " And State = 1");
	}

	public int getNotHideCountByParentID(int categoryID) {

		return mSQLiteDALCategory.getCount(TYPE_FLAG + " And ParentID = " + categoryID + " And State = 1");
	}

	public List<ModelCategory> getNotHideRootCategory() {

		return mSQLiteDALCategory.getCategory(TYPE_FLAG + " And ParentID = 0 And State = 1");
	}

	public List<ModelCategory> getNotHideCategoryListByParentID(int p_ParentID) {

		return mSQLiteDALCategory.getCategory(TYPE_FLAG + " And ParentID = " + p_ParentID + " And State = 1");
	}

	public ModelCategory GetModelCategoryByParentID(int p_ParentID) {

		List list = mSQLiteDALCategory.getCategory(TYPE_FLAG + " And ParentID = " + p_ParentID);
		if (list.size() == 1) {
			return (ModelCategory) list.get(0);
		} else {
			return null;
		}
	}

	public ArrayAdapter getRootCategoryArrayAdapter() {
		List list = getNotHideRootCategory();
		list.add(0, "--请选择--");
		ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, list);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return arrayAdapter;
	}

	/*public List<ModelCategoryTotal> getCategoryTotalByRootCategory() {

		String condition = TYPE_FLAG + " And ParentID = 0 And State = 1";
		List<ModelCategoryTotal> modelCategoryTotalList = getCategoryTotal(condition);

		return modelCategoryTotalList;
	}

	public List<ModelCategoryTotal> getCategoryTotalByParentID(int parentID) {

		String _Condition = TYPE_FLAG + " And ParentID = " + parentID;
		List<ModelCategoryTotal> _ModelCategoryTotalList = getCategoryTotal(_Condition);

		return _ModelCategoryTotalList;
	}

	private List<ModelCategoryTotal> getCategoryTotal(String condition) {

		Cursor cursor = mSQLiteDALCategory
				.execSql("Select Count(PayoutID) As Count, Sum(Amount) As SumAmount, CategoryName From v_Payout Where 1=1 "
						+ condition + " Group By CategoryName");
		List<ModelCategoryTotal> modelCategoryTotalList = new ArrayList<ModelCategoryTotal>();
		while (cursor.moveToNext()) {
			ModelCategoryTotal modelCategoryTotal = new ModelCategoryTotal();
			modelCategoryTotal.Count = cursor.getString(cursor.getColumnIndex("Count"));
			modelCategoryTotal.SumAmount = cursor.getString(cursor.getColumnIndex("SumAmount"));
			modelCategoryTotal.CategoryName = cursor.getString(cursor.getColumnIndex("CategoryName"));
			modelCategoryTotalList.add(modelCategoryTotal);
		}

		return modelCategoryTotalList;
	}*/

}

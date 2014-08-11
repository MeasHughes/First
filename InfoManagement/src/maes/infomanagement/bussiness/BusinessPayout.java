package maes.infomanagement.bussiness;

import java.util.List;

import maes.infomanagement.R;
import maes.infomanagement.bussiness.base.BussinessBase;
import maes.infomanagement.database.sqlitedal.SQLiteDALPayout;
import maes.infomanagement.model.ModelPayout;

import android.content.Context;
import android.database.Cursor;

public class BusinessPayout extends BussinessBase {

	private SQLiteDALPayout mSqLiteDALPayout;

	public BusinessPayout(Context context) {
		super(context);
		mSqLiteDALPayout = new SQLiteDALPayout(context);
	}


	public Boolean insertPayout(ModelPayout payout) {
		return mSqLiteDALPayout.insertPayout(payout);
	}


	public Boolean deletePayoutByPayoutID(int payoutID) {
		String condition = "AND PayoutID = " + payoutID;
		return mSqLiteDALPayout.deletePayout(condition);
	}


	public Boolean deletePayoutByAccountBookID(int accountBookID) {
		String condition = "AND AccountBookID = " + accountBookID;
		return mSqLiteDALPayout.deletePayout(condition);
	}


	public Boolean updatePayoutByPayoutID(ModelPayout payout) {
		String condition = " PayoutID = " + payout.getPayoutID();
		return mSqLiteDALPayout.updatePayout(condition, payout);
	}

	public List<ModelPayout> getPayout(String condition) {
		return mSqLiteDALPayout.getPayout(condition);
	}

	public int getCount() {
		return mSqLiteDALPayout.getCount("");
	}

	public List<ModelPayout> getPayoutByAccountBookID(int accountBookID) {
		String condition = " AND AccountBookID = " + accountBookID + " Order By PayoutDate DESC,PayoutID DESC";
		return mSqLiteDALPayout.getPayout(condition);
	}

	public String getPayoutTotalMessage(String payoutDate, int accountBookID) {
		String sotal[] = getPayoutTotalByPayoutDate(payoutDate, accountBookID);
		return getContext().getString(R.string.TextViewTextPayoutTotal, new Object[] { sotal[0], sotal[1] });
	}

	private String[] getPayoutTotalByPayoutDate(String payoutDate, int accountBookID) {
		String condition = " AND PayoutDate = '" + payoutDate + "' And AccountBookID = " + accountBookID;
		return getPayoutTotal(condition);
	}

	public String[] GetPayoutTotalByAccountBookID(int accountBookID) {
		String condition = " AND AccountBookID = " + accountBookID;
		return getPayoutTotal(condition);
	}

	private String[] getPayoutTotal(String condition) {
		String sqlText = "Select ifnull(Sum(Amount),0) As SumAmount,Count(Amount) As Count From Payout Where 1=1 "
				+ condition;
		String total[] = new String[2];
		Cursor cursor = mSqLiteDALPayout.execSQL(sqlText);
		if (cursor.getCount() == 1) {
			while (cursor.moveToNext()) {
				total[0] = cursor.getString(cursor.getColumnIndex("Count"));
				total[1] = cursor.getString(cursor.getColumnIndex("SumAmount"));
			}
		}
		return total;
	}

	public List<ModelPayout> getPayoutOrderByPayoutUserID(String condition) {
		condition += " Order By PayoutUserID";
		List<ModelPayout> list = getPayout(condition);
		if (list.size() > 0) {
			return list;
		}

		return null;
	}

	public String[] getPayoutDateAndAmountTotal(String condition) {
		String sqlText = "Select Min(PayoutDate) As MinPayoutDate,Max(PayoutDate) As MaxPayoutDate,Sum(Amount) As Amount From Payout Where 1=1 "
				+ condition;
		String payoutTotal[] = new String[3];
		Cursor cursor = mSqLiteDALPayout.execSQL(sqlText);
		if (cursor.getCount() == 1) {
			while (cursor.moveToNext()) {
				payoutTotal[0] = cursor.getString(cursor.getColumnIndex("MinPayoutDate"));
				payoutTotal[1] = cursor.getString(cursor.getColumnIndex("MaxPayoutDate"));
				payoutTotal[2] = cursor.getString(cursor.getColumnIndex("Amount"));
			}
		}
		return payoutTotal;
	}
}

package maes.infomanagement.adapter;

import maes.infomanagement.R;
import maes.infomanagement.adapter.base.AdapterBase;
import maes.infomanagement.bussiness.BusinessPayout;
import maes.infomanagement.bussiness.BussinessUser;
import maes.infomanagement.model.ModelPayout;
import maes.infomanagement.util.DateTools;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterPayout extends AdapterBase {

	private class Holder {
		ImageView ivIcon;
		TextView tvName;
		TextView tvTotal;
		TextView tvPayoutUserAndPayoutType;
		View RelativeLayoutDate;
	}

	private BusinessPayout mBusinessPayout;
	private int mAccountBookID;

	public AdapterPayout(Context context, int accountBookID) {
		super(context, null);
		mBusinessPayout = new BusinessPayout(context);
		mAccountBookID = accountBookID;
		setList(mBusinessPayout.getPayoutByAccountBookID(accountBookID));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;

		if (convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.payout_list_item, null);
			holder = new Holder();
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.PayoutIcon);
			holder.tvName = (TextView) convertView.findViewById(R.id.PayoutName);
			holder.tvTotal = (TextView) convertView.findViewById(R.id.Total);
			holder.tvPayoutUserAndPayoutType = (TextView) convertView.findViewById(R.id.PayoutUserAndPayoutType);
			holder.RelativeLayoutDate = (View) convertView.findViewById(R.id.RelativeLayoutDate);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.RelativeLayoutDate.setVisibility(View.GONE);
		ModelPayout modelPayout = (ModelPayout) getItem(position);
		String payoutDate = DateTools.getFormatShortTime(modelPayout.getPayoutDate());
		Boolean isShow = false;
		if (position > 0) {
			ModelPayout modelPayoutLast = (ModelPayout) getItem(position - 1);
			String payoutDateLast = DateTools.getFormatShortTime(modelPayoutLast.getPayoutDate());
			isShow = !payoutDate.equals(payoutDateLast);
		}
		if (isShow || position == 0) {
			holder.RelativeLayoutDate.setVisibility(View.VISIBLE);
			String message = mBusinessPayout.getPayoutTotalMessage(payoutDate, mAccountBookID);
			((TextView) holder.RelativeLayoutDate.findViewById(R.id.tvPayoutDate)).setText(payoutDate);
			((TextView) holder.RelativeLayoutDate.findViewById(R.id.tvTotal)).setText(message);
		}
		holder.ivIcon.setImageResource(R.drawable.small4);
		holder.tvTotal.setText(modelPayout.getAmount().toString());
		holder.tvName.setText(modelPayout.getCategoryName());

		BussinessUser businessUser = new BussinessUser(getContext());
		String userName = businessUser.getUserNameByUserID(modelPayout.getPayoutUserID());
		holder.tvPayoutUserAndPayoutType.setText(userName + " " + modelPayout.getPayoutType());

		return convertView;

	}

}

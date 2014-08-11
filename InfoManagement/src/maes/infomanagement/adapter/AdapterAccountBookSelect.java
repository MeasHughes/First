package maes.infomanagement.adapter;


import java.util.List;

import maes.infomanagement.R;
import maes.infomanagement.adapter.base.AdapterBase;
import maes.infomanagement.bussiness.BussinessAccount;
import maes.infomanagement.model.ModelAccount;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterAccountBookSelect extends AdapterBase {

	private class Holder {
		ImageView ivIcon;
		TextView tvName;
	}

	public AdapterAccountBookSelect(Context context) {
		super(context, null);
		BussinessAccount musinessAccountBook = new BussinessAccount(context);
		List list = musinessAccountBook.getAccount("");
		setList(list);
	}

	public AdapterAccountBookSelect(Context context, List list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if (convertView == null) {
			convertView = getLayoutInflater().inflate(R.layout.account_book_select_list_item, null);
			holder = new Holder();
			holder.ivIcon = (ImageView) convertView.findViewById(R.id.ivAccountBookIcon);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvAccountBookName);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		ModelAccount modelAccountBook = (ModelAccount) getItem(position);
		holder.ivIcon.setImageResource(R.drawable.small1);
		holder.tvName.setText(modelAccountBook.getAccountBookName());

		return convertView;
	}

}

package maes.infomanagement.adapter;

import java.util.List;

import maes.infomanagement.adapter.base.AdapterBase;

import maes.infomanagement.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterGrid extends BaseAdapter {

	private String[] strings = new String[6];
	private Context mContext;

	private int[] imageResID = new int[]{
			R.drawable.billmanage,
			R.drawable.categorymana,
			R.drawable.payrec,
			R.drawable.peoplemanage,
			R.drawable.querymanage,
			R.drawable.totalmanage
	};
	
	/*public AdapterGrid(Context pContext, List pList) {
		super(pContext, pList);
		mContext = pContext;
		strings[0] = pContext.getString(R.string.string_main_billmanage);
		strings[1] = pContext.getString(R.string.string_main_categorymana);
		strings[2] = pContext.getString(R.string.string_main_payrec);
		strings[3] = pContext.getString(R.string.string_main_peoplemanage);
		strings[4] = pContext.getString(R.string.string_main_querymanage);
		strings[5] = pContext.getString(R.string.string_main_totalmanage);
	}*/

	

	public AdapterGrid(Context context){
		mContext = context;
		strings[0] = context.getString(R.string.string_main_billmanage);
		strings[1] = context.getString(R.string.string_main_categorymana);
		strings[2] = context.getString(R.string.string_main_payrec);
		strings[3] = context.getString(R.string.string_main_peoplemanage);
		strings[4] = context.getString(R.string.string_main_querymanage);
		strings[5] = context.getString(R.string.string_main_totalmanage);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		InitRes _res ;

		if(convertView == null)
		{
			LayoutInflater _layout = LayoutInflater.from(mContext);
			convertView = _layout.inflate(R.layout.layout_main_body_item, null);

			_res= new InitRes();
			_res.id_main_item_iv = (ImageView)convertView.findViewById(R.id.id_main_item_iv);
			_res.id_main_item_tv = (TextView)convertView.findViewById(R.id.id_main_item_tv);
			convertView.setTag(_res);

		}
		else{
			_res = (InitRes) convertView.getTag();
		}
		_res.id_main_item_iv.setImageResource(imageResID[position]);
		
		LinearLayout.LayoutParams _ivParams = new LinearLayout.LayoutParams(60,60);
		_res.id_main_item_iv.setLayoutParams(_ivParams);
		_res.id_main_item_iv.setScaleType(ImageView.ScaleType.FIT_XY);	 

		_res.id_main_item_tv.setText(strings[position]);
		return convertView;
	}

	private class InitRes{
		ImageView id_main_item_iv;
		TextView id_main_item_tv;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strings.length;
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return strings[position];
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


}

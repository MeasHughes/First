package maes.infomanagement.adapter;

import java.util.List;

import maes.infomanagement.adapter.base.AdapterBase;
import maes.infomanagement.control.SlideMenuItem;


import maes.infomanagement.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterSlideMenu extends AdapterBase {
	private String[] strings = new String[6];
	private Context mContext;
	
	private class InitRes{
		TextView tv_buttom_listview;
	}
	
	public AdapterSlideMenu(Context pContext, List pList) {
		super(pContext, pList);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		InitRes _res ;

		if(convertView == null)
		{
			convertView = getLayoutInflater().inflate(R.layout.layout_slider_listview_item, null);

			_res= new InitRes();
			_res.tv_buttom_listview = (TextView)convertView.findViewById(R.id.tv_buttom_listview);
			convertView.setTag(_res);

		}
		else{
			_res = (InitRes) convertView.getTag();
		}
		
		SlideMenuItem _sItem = (SlideMenuItem) getList().get(position);
		
		_res.tv_buttom_listview.setText(_sItem.getmTitle());
		return convertView;
	}
	
}

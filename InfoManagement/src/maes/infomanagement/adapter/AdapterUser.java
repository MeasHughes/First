package maes.infomanagement.adapter;

import java.util.List;

import maes.infomanagement.adapter.base.AdapterBase;
import maes.infomanagement.bussiness.BussinessUser;
import maes.infomanagement.control.SlideMenuItem;
import maes.infomanagement.model.ModelUser;


import maes.infomanagement.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdapterUser extends AdapterBase {
	private String[] strings = new String[6];
	private Context mContext;
	
	private class InitRes{
		ImageView ivUserIcon;
		TextView tvUserText;
	}
	
	public AdapterUser(Context pContext) {
		super(pContext, null);
		BussinessUser _BussinessUser = new BussinessUser(pContext);
		List<ModelUser> _list = _BussinessUser.getNotHideUser();
		setList(_list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		InitRes _res ;

		if(convertView == null)
		{
			convertView = getLayoutInflater().inflate(R.layout.layout_user_listitem, null);

			_res= new InitRes();
			_res.ivUserIcon = (ImageView)convertView.findViewById(R.id.ivUserIcon);
			_res.tvUserText = (TextView)convertView.findViewById(R.id.tvUserText);
			convertView.setTag(_res);

		}
		else{
			_res = (InitRes) convertView.getTag();
		}
		
		ModelUser _sItem = (ModelUser) getList().get(position);
		
		_res.ivUserIcon.setImageResource(R.drawable.pic_head);
		_res.tvUserText.setText(_sItem.getmUserName());
		return convertView;
	}
	
}

package maes.infomanagement.activity;


import java.util.List;


import maes.infomanagement.activity.base.ActivityFrame;
import maes.infomanagement.adapter.AdapterGrid;
import maes.infomanagement.control.SlideMenuItem;
import maes.infomanagement.control.SlideMenuView.OnSlideMenuListener;

import maes.infomanagement.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ActivityMain extends ActivityFrame implements OnSlideMenuListener{

	private GridView gridView;
	private AdapterGrid adapterGrid;
	 int i;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addMainBody(R.layout.layout_main_body);
		initVaribale();
		initView();
		bindData();
		initListener();
		hiddenBackBtn();
		createSlideMenu(R.array.array_main_buttom_list);
		
		showMessage(String.valueOf(i));
	}

	private void initVaribale(){
		adapterGrid = new AdapterGrid(ActivityMain.this);
	}
	private void initListener(){
		gridView.setOnItemClickListener(new onGridViewItemClick());
	}
	
	private class onGridViewItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> adapterView, View _pView, int postition,
				long arg3) {
			String _menuName = (String)adapterView.getAdapter().getItem(postition);
			if(_menuName.equals(getString(R.string.string_main_peoplemanage)))
			{
				openActivity(ActivityUser.class);
				return;
			}else if(_menuName.equals(getString(R.string.string_main_billmanage)))
			{
				openActivity(ActivityAccount.class);
				return;
			}else if(_menuName.equals(getString(R.string.string_main_categorymana)))
			{
				openActivity(ActivityCategory.class);
				return;
			}else if (_menuName.endsWith(getString(R.string.string_main_payrec))) {
				openActivity(ActivityPayoutAddOrEdit.class);
				return;
			} else if (_menuName.endsWith(getString(R.string.string_main_querymanage))) {
				openActivity(ActivityPayout.class);
				return;
			} else if (_menuName.endsWith(getString(R.string.string_main_totalmanage))) {
				openActivity(ActivityStatistics.class);
				return;
			} 
		}
		
	}
	
	private void initView(){
		gridView = (GridView)findViewById(R.id.id_main_body_grid);
	}
	private void bindData(){
		gridView.setAdapter(adapterGrid);
	}


	@Override
	public void onSlideMenuItemClick(View _pView, SlideMenuItem _pSlideMenuItem) {
		showMessage(_pSlideMenuItem.getmTitle());
	}


}

package maes.infomanagement.control;

import java.util.ArrayList;
import java.util.List;



import maes.infomanagement.adapter.AdapterSlideMenu;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import maes.infomanagement.R;

public class SlideMenuView {

	private Activity mActivity;
	private List mMenuList;
	private boolean mIsClosed;
	private RelativeLayout layoutButtom;
	private OnSlideMenuListener onSlideMenuListener;
	
	public interface OnSlideMenuListener{
		public abstract void onSlideMenuItemClick(View _pView,SlideMenuItem _pSlideMenuItem);
	}

	public SlideMenuView(Activity pActivity){
		/*mActivity = pActivity; 
		onSlideMenuListener = (OnSlideMenuListener) pActivity;
		mIsClosed = true;
		
		initVaribale();
		initView();
		initListener();*/
		
		
		mActivity = pActivity;
		initView();
		if (pActivity instanceof OnSlideMenuListener) {
			onSlideMenuListener = (OnSlideMenuListener) pActivity;
			initVaribale();
			initListener();
		}
		close();
	}

	private void initVaribale(){
		mMenuList = new ArrayList();
	}
	private void initListener(){
		layoutButtom.setOnClickListener(new onSlideMenuClick());
		layoutButtom.setFocusableInTouchMode(true);
		layoutButtom.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_MENU && event.getAction()== KeyEvent.ACTION_UP)
				{
					toggle();
				}
				return false;
			}
		});
	}

	private void initView(){
		layoutButtom = (RelativeLayout)mActivity.findViewById(R.id.id_main_layout_buttom);
	}

	private void open(){
		RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams
				(RelativeLayout.LayoutParams.FILL_PARENT,RelativeLayout.LayoutParams.FILL_PARENT);
		_LayoutParams.addRule(RelativeLayout.BELOW, R.id.id_main_layout_top);
		layoutButtom.setLayoutParams(_LayoutParams); 

		mIsClosed = false;
	}
	private void close(){
		RelativeLayout.LayoutParams _LayoutParams = new RelativeLayout.LayoutParams
				(RelativeLayout.LayoutParams.FILL_PARENT,68);
		_LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layoutButtom.setLayoutParams(_LayoutParams); 

		mIsClosed = true;

	}
	
	public void removeBottomBox() {
		RelativeLayout mainLayout = (RelativeLayout) mActivity.findViewById(R.id.main_layout);
		mainLayout.removeView(layoutButtom);
		layoutButtom = null;
	}
	
	public void toggle(){
		if(mIsClosed)
		{
			open();
		}else{
			close();
		}
	}
	
	public void add(SlideMenuItem pSliderMenuItem){
		mMenuList.add(pSliderMenuItem);
	}
	public void bindList(){
//		AdapterSlideMenu _AdapterSlideMenu = new AdapterSlideMenu(mActivity, mMenuList);
		
//		ListView _lisListView = (ListView)mActivity.findViewById(R.id.lv_main_buttom);
//		ListView _lisListView = pListView;
//		_lisListView.setAdapter(_AdapterSlideMenu);
//		_lisListView.setOnItemClickListener(new onSlideItemClick());
		
		AdapterSlideMenu _AdapterSlideMenu = new AdapterSlideMenu(mActivity, mMenuList);
		ListView _ListView = (ListView) mActivity.findViewById(R.id.lv_main_buttom);
		_ListView.setAdapter(_AdapterSlideMenu);
		_ListView.setOnItemClickListener(new onSlideItemClick());
	}

	private class onSlideMenuClick implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			toggle();
		}

	}
	
	private class onSlideItemClick implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> adapterView, View _pView, int postition,
				long arg3) {
			SlideMenuItem _pSlideMenuItem = (SlideMenuItem) adapterView.getItemAtPosition(postition);
			onSlideMenuListener.onSlideMenuItemClick(_pView, _pSlideMenuItem);
		}
		
	}
}

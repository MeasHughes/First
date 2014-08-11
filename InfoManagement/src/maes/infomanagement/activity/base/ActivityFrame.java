package maes.infomanagement.activity.base;

import maes.infomanagement.control.SlideMenuItem;
import maes.infomanagement.control.SlideMenuView;

import maes.infomanagement.R;

import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityFrame extends AcitivityBase {
	
	private SlideMenuView slideMenuView;
	private ListView _ListView ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		View _view = findViewById(R.id.ivAppBack);
		OnbackListener _onbackListener = new OnbackListener();
		_view.setOnClickListener(_onbackListener);
	}
	
	private class OnbackListener implements OnClickListener
	{

		@Override
		public void onClick(View view) {
			finish();
		} 
		
	}
	
	protected void hiddenBackBtn()
	{
		((View)findViewById(R.id.ivAppBack)).setVisibility(View.GONE);
	}
	
	/**
	 * 设置TopBar标题
	 * 
	 * @param pRestID
	 *            要设置的String资源ID
	 */
	protected void setTopBarTitle(String title) {
		// String _Title = FormatResString(pRestID);
		TextView tvTitle = (TextView) findViewById(R.id.tv_main_title);
		tvTitle.setText(title);

		// ImageView _ImageView = (ImageView) findViewById(R.id.ivBottomIcon);
		// _ImageView.setImageResource(R.drawable.account_book_32x32);
	}
	
	protected void removeBottomBox() {
		slideMenuView = new SlideMenuView(this);
		slideMenuView.removeBottomBox();
	}
	
	protected void addMainBody(int resID)
	{
		LinearLayout _mainBody = (LinearLayout)findViewById(R.id.id_main_layout_body);
		View _view = LayoutInflater.from(this).inflate(resID, null);
		RelativeLayout.LayoutParams _params = new RelativeLayout.
				LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
		_mainBody.addView(_view,_params);
	}
	
	private int getMainBodyLayoutID() {
		return R.id.id_main_layout_body;
	}
	
	/**
	 * 添加Layout到程序Body中
	 * 
	 * @param view
	 *            要添加的View
	 */
	protected void appendMainBody(View view) {
		LinearLayout mainBody = (LinearLayout) findViewById(getMainBodyLayoutID());
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
		mainBody.addView(view, layoutParams);
		// _View.setPadding(15,15,15,15);
	}
	
	protected void createMenu(Menu p_Menu) {
		int _GroupID = 0;
		int _Order = 0;
		int[] p_ItemID = { 1, 2 };

		for (int i = 0; i < p_ItemID.length; i++) {
			switch (p_ItemID[i]) {
			case 1:
				p_Menu.add(_GroupID, p_ItemID[i], _Order, R.string.MenuTextEdit);
				break;
			case 2:
				p_Menu.add(_GroupID, p_ItemID[i], _Order, R.string.MenuTextDelete);
				break;
			default:
				break;
			}
		}
	}
	
	protected void slideMenuToggle() {
		slideMenuView.toggle();
	}
	
	protected ListView addListView()
	{
		LinearLayout _mainBody = (LinearLayout)findViewById(R.id.id_main_bottom_linear);
//		View _view = LayoutInflater.from(this).inflate(resID, null);
		 _ListView = new ListView(this);
		RelativeLayout.LayoutParams _params = new RelativeLayout.
				LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		_mainBody.addView(_ListView,_params);
		
		return _ListView;
	}
	
	protected void createSlideMenu(int pArryID){
		slideMenuView = new SlideMenuView(this);
		String[] _menuItemArray = getResources().getStringArray(pArryID);
		
		for(int i = 0;i < _menuItemArray.length;i ++){
			SlideMenuItem _SlideMenuItem = new SlideMenuItem(i, _menuItemArray[i]);
			slideMenuView.add(_SlideMenuItem);
		}
		slideMenuView.bindList();
	}
}

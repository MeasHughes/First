package maes.infomanagement.activity;


import java.util.List;

import maes.infomanagement.activity.base.ActivityFrame;
import maes.infomanagement.adapter.AdapterGrid;
import maes.infomanagement.adapter.AdapterUser;
import maes.infomanagement.bussiness.BussinessUser;
import maes.infomanagement.control.SlideMenuItem;
import maes.infomanagement.control.SlideMenuView.OnSlideMenuListener;
import maes.infomanagement.model.ModelUser;
import maes.infomanagement.util.RegexTools;

import maes.infomanagement.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;

public class ActivityUser extends ActivityFrame implements OnSlideMenuListener{

	private ListView listView;
	private AdapterUser adapterUser;
	private BussinessUser mBusinessUser;
	private ModelUser mSelectModelUser;
	protected static final int SHOW_TIME = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addMainBody(R.layout.layout_user);
		initVaribale();
		initView();
		bindData();
		initListener();
		setTitle();
		createSlideMenu(R.array.array_user_buttom);
	}

	private void initVaribale(){
		mBusinessUser = new BussinessUser(this);
	}
	private void initListener(){
		registerForContextMenu(listView);
	}
	
	private void setTitle() {
		int _Count = adapterUser.getCount();
		String _Titel = getString(R.string.ActivityTitleUser, new Object[]{_Count});
		setTopBarTitle(_Titel);
	}
	
	private void initView(){
		listView = (ListView)findViewById(R.id.lv_user);
	}
	private void bindData(){
		adapterUser = new AdapterUser(ActivityUser.this);
		listView.setAdapter(adapterUser);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// 得到菜单信息
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;
		ListAdapter listAdapter = listView.getAdapter();
		mSelectModelUser = (ModelUser) listAdapter
				.getItem(adapterContextMenuInfo.position);
		// 设置菜单图标
		menu.setHeaderIcon(R.drawable.pic_head);
		menu.setHeaderTitle(mSelectModelUser.getmUserName());
		// 生成菜单
		createMenu(menu);
	}

	@Override
	public void onSlideMenuItemClick(View _pView, SlideMenuItem _pSlideMenuItem) {
		slideMenuToggle();
		if (_pSlideMenuItem.getmItemID() == 0) {
			showDialog(null);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			// 修改
			showDialog(mSelectModelUser);
			break;
			// 删除
		case 2:
			delete();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	/**
	 * 上下文菜单弹出框，删除功能
	 */
	private void delete() {
		String message = getString(R.string.DialogMessageUserDelete,
				new Object[] { mSelectModelUser.getmUserName() });
		showAlertDialog(R.string.DialogTitleDelete, message,
				new OnDeleteClickListener());
	}
	
	private class OnDeleteClickListener implements
	DialogInterface.OnClickListener {

@Override
public void onClick(DialogInterface dialog, int which) {
	// 逻辑删除
	boolean result = mBusinessUser.hideUserByUserID(mSelectModelUser
			.getmUserID());
	if (result) {
		bindData();
	} else {
		// ToDo删除失败
	}

}

}

	/**
	 * 点击listview弹出修改新增用户的窗口
	 * @param pModelUser
	 */
	private void showDialog(ModelUser pModelUser)
	{
		View view = getLayoutInflator().inflate(R.layout.layout_user_addoredit, null);
		EditText _etUserName = (EditText)view.findViewById(R.id.et_user_add);
		
		if(pModelUser != null)
		{
			_etUserName.setText(pModelUser.getmUserName());
		}
		
		String _title;
		if(pModelUser == null)
		{
			_title = getString(R.string.string_user_add, new Object[]{R.string.string_user_add});
		}else{
			_title = getString(R.string.string_user_add, new Object[]{R.string.string_user_edit});
		}
		
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		_Builder.setTitle(_title)
				.setView(view)
				.setIcon(R.drawable.pic_head)
				// 确定按钮
				.setNeutralButton(
						getString(R.string.ButtonTextSave),
						new OnAddOrEditUserListener(pModelUser, _etUserName,
								true))
				// 取消按钮
				.setNegativeButton(getString(R.string.ButtonTextCancel),
						new OnAddOrEditUserListener(null, null, false)).show();
		
	}

	private class OnAddOrEditUserListener implements
	DialogInterface.OnClickListener{
		
		private ModelUser mModelUser;
		private EditText etUserName;
		private boolean mIsSaveButton;
		public OnAddOrEditUserListener(ModelUser pModelUser,
				EditText petUserName, boolean pIsSaveButton) {
			mModelUser = pModelUser;
			etUserName = petUserName;
			mIsSaveButton = pIsSaveButton;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (mIsSaveButton == false) {
				setAlertDialogIsClose(dialog, true);
				return;
			}
			// 判断是修改还是添加
			if (mModelUser == null) {
				mModelUser = new ModelUser();
			}
			String userName = etUserName.getText().toString().trim();
			
			// 检查用户名格式
			Boolean checkResult = RegexTools.IsChineseEnglishNum(userName);
			if (!checkResult) {
				
				showMessage(getString(R.string.CheckDataTextChineseEnglishNum,
								new Object[] { etUserName.getHint() }));
				setAlertDialogIsClose(dialog, false);
				return;
			} else {
				setAlertDialogIsClose(dialog, true);
			}
			
			
			// 判断用户名是否存在
						checkResult = mBusinessUser.isExistUserByUserName(userName,
								mModelUser.getmUserID());
						if (checkResult) {
							showMessage(getString(R.string.CheckDataTextUserExist));
							setAlertDialogIsClose(dialog, false);
							return;
						} else {
							setAlertDialogIsClose(dialog, true);
						}
						
						boolean result = false;
						mModelUser.setmUserName(userName);
						if (mModelUser.getmUserID() == 0) {
							result = mBusinessUser.insertUser(mModelUser);
						} else {
							result = mBusinessUser.updateUser(mModelUser);
						}
						

						if (result) {
							bindData();
							showMessage(getString(R.string.TipsAddSucceed));

						} else {
							showMessage(getString(R.string.TipsAddFail));
						}
						
		}
		
	}
	

}

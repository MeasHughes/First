package maes.infomanagement.activity;


import java.util.List;

import maes.infomanagement.activity.base.ActivityFrame;
import maes.infomanagement.adapter.AdapterGrid;
import maes.infomanagement.adapter.AdapterAccount;
import maes.infomanagement.bussiness.BussinessAccount;
import maes.infomanagement.control.SlideMenuItem;
import maes.infomanagement.control.SlideMenuView.OnSlideMenuListener;
import maes.infomanagement.model.ModelAccount;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemLongClickListener;

public class ActivityAccount extends ActivityFrame implements OnSlideMenuListener{

	private ListView listView;
	private AdapterAccount adapterAccount;
	private BussinessAccount mBusinessAccount;
	private ModelAccount mSelectModelAccount;
	protected static final int SHOW_TIME = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addMainBody(R.layout.layout_account);
		initVaribale();
		initView();
		bindData();
		initListener();
		setTitle();
		createSlideMenu(R.array.SlideMenuAccountBook);
	}

	private void initVaribale(){
		mBusinessAccount = new BussinessAccount(this);
	}
	private void initListener(){
		registerForContextMenu(listView);
	}
	
	
	
	private void initView(){
		listView = (ListView)findViewById(R.id.lv_account);
	}
	private void bindData(){
		adapterAccount = new AdapterAccount(ActivityAccount.this);
		listView.setAdapter(adapterAccount);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// 得到菜单信息
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;
		ListAdapter listAdapter = listView.getAdapter();
		mSelectModelAccount = (ModelAccount) listAdapter
				.getItem(adapterContextMenuInfo.position);
		// 设置菜单图标
		menu.setHeaderIcon(R.drawable.pic_head);
		menu.setHeaderTitle(mSelectModelAccount.getAccountBookName());
		// 生成菜单
		createMenu(menu);
	}

	private void setTitle() {
		int _Count = adapterAccount.getCount();
		String titel = getString(R.string.ActivityTitleAccountBook, new Object[] { _Count });
		setTopBarTitle(titel);
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
			showDialog(mSelectModelAccount);
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
		if(adapterAccount.getCount() <= 1)
		{
			showMessage("至少要有一个账本！");
			return;
		}
		if(mSelectModelAccount.getIsDefault() == 1){
			showMessage("默认账本不能删除！请先选择其他账本为默认再删除当前账本！");
			return;
		}
		String message = getString(R.string.DialogMessageAccountBookDelete,
				new Object[] { mSelectModelAccount.getAccountBookName() });
		showAlertDialog(R.string.DialogTitleDelete, message,
				new OnDeleteClickListener());
	}
	
	private class OnDeleteClickListener implements
	DialogInterface.OnClickListener {

@Override
public void onClick(DialogInterface dialog, int which) {
	// 逻辑删除
	boolean result = mBusinessAccount.deleteAccountBookByAccountBookID(mSelectModelAccount
			.getAccountBookID());
	if (result) {
		bindData();
	} else {
		// ToDo删除失败
	}

}

}

	/**
	 * 点击listview弹出修改新增用户的窗口
	 * @param pModelAccount
	 */
	private void showDialog(ModelAccount pModelAccount)
	{
		View view = getLayoutInflator().inflate(R.layout.layout_account_book_add_or_edit, null);
		EditText _etAccountName = (EditText)view.findViewById(R.id.etAccountBookName);
		CheckBox _chkIsDefault = (CheckBox) view.findViewById(R.id.chkIsDefault);
		if(pModelAccount != null)
		{
			_etAccountName.setText(pModelAccount.getAccountBookName());
		}
		
		String _title;
		if(pModelAccount == null)
		{
			_title = getString(R.string.DialogTitleAccountBook, new Object[]{R.string.TitleAdd});
		}else{
			_title = getString(R.string.DialogTitleAccountBook, new Object[]{R.string.TitleEdit});
			if(pModelAccount.getIsDefault() == 1)
			{
			_chkIsDefault.setChecked(true);
			}else{
				_chkIsDefault.setChecked(false);
			}
		}
		
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		_Builder.setTitle(_title)
				.setView(view)
				.setIcon(R.drawable.pic_head)
				// 确定按钮
				.setNeutralButton(
						getString(R.string.ButtonTextSave),
						new OnAddOrEditAccountListener(pModelAccount, _etAccountName,_chkIsDefault,
								true))
				// 取消按钮
				.setNegativeButton(getString(R.string.ButtonTextCancel),
						new OnAddOrEditAccountListener(null, null,_chkIsDefault, false)).show();
		
	}

	private class OnAddOrEditAccountListener implements
	DialogInterface.OnClickListener{
		
		private ModelAccount mModelAccount;
		private EditText etAccountName;
		private boolean mIsSaveButton;
		private CheckBox chkIsDefault;
		
		public OnAddOrEditAccountListener(ModelAccount pModelAccount,
				EditText petAccountName,CheckBox pchkIsDefault, boolean pIsSaveButton) {
			mModelAccount = pModelAccount;
			etAccountName = petAccountName;
			mIsSaveButton = pIsSaveButton;
			chkIsDefault = pchkIsDefault;
		}
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (mIsSaveButton == false) {
				setAlertDialogIsClose(dialog, true);
				return;
			}
			// 判断是修改还是添加
			if (mModelAccount == null) {
				mModelAccount = new ModelAccount();
			}
			String AccountName = etAccountName.getText().toString().trim();
			
			// 检查用户名格式
			Boolean checkResult = RegexTools.IsChineseEnglishNum(AccountName);
			if (!checkResult) {
				
				showMessage(getString(R.string.CheckDataTextChineseEnglishNum,
								new Object[] { etAccountName.getHint() }));
				setAlertDialogIsClose(dialog, false);
				return;
			} else {
				setAlertDialogIsClose(dialog, true);
			}
			if (chkIsDefault.isChecked()) {
				mModelAccount.setIsDefault(1);
			} else {
				if(mModelAccount.getAccountBookID() == 0)
				{
				if(mBusinessAccount.hasDefaultModelAccountBook())
				{
				mModelAccount.setIsDefault(0);
				}
				else {
					showMessage("至少要有一个默认账本！");
					return;
				}
				}else if(adapterAccount.getCount() == 1)
				{
					showMessage("至少要有一个默认账本！");
					return;
				}
			}
			
			
			// 判断用户名是否存在
						checkResult = mBusinessAccount.isExistAccountByAccountName(AccountName,
								mModelAccount.getAccountBookID());
						if (checkResult) {
							showMessage(getString(R.string.CheckDataTextAccountBookExist));
							setAlertDialogIsClose(dialog, false);
							return;
						} else {
							setAlertDialogIsClose(dialog, true);
						}
						
//						boolean result = false;
						mModelAccount.setAccountBookName(AccountName);
						/*if (mModelAccount.getAccountBookID() == 0) {
							result = mBusinessAccount.insertAccount(mModelAccount);
						} else {
							result = mBusinessAccount.updateAccount(mModelAccount);
						}*/
						

						/*if (mModelAccount.getAccountBookID() > 0) {
							mModelAccount.setIsDefault(1);
						}*/

						boolean result = false;

						if (mModelAccount.getAccountBookID() == 0) {
							result = mBusinessAccount.insertAccount(mModelAccount);
						} else {
							result = mBusinessAccount.updateAccountBookByAccountBookID(mModelAccount);
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

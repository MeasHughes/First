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
		// �õ��˵���Ϣ
		AdapterContextMenuInfo adapterContextMenuInfo = (AdapterContextMenuInfo) menuInfo;
		ListAdapter listAdapter = listView.getAdapter();
		mSelectModelAccount = (ModelAccount) listAdapter
				.getItem(adapterContextMenuInfo.position);
		// ���ò˵�ͼ��
		menu.setHeaderIcon(R.drawable.pic_head);
		menu.setHeaderTitle(mSelectModelAccount.getAccountBookName());
		// ���ɲ˵�
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
			// �޸�
			showDialog(mSelectModelAccount);
			break;
			// ɾ��
		case 2:
			delete();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	/**
	 * �����Ĳ˵�������ɾ������
	 */
	private void delete() {
		if(adapterAccount.getCount() <= 1)
		{
			showMessage("����Ҫ��һ���˱���");
			return;
		}
		if(mSelectModelAccount.getIsDefault() == 1){
			showMessage("Ĭ���˱�����ɾ��������ѡ�������˱�ΪĬ����ɾ����ǰ�˱���");
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
	// �߼�ɾ��
	boolean result = mBusinessAccount.deleteAccountBookByAccountBookID(mSelectModelAccount
			.getAccountBookID());
	if (result) {
		bindData();
	} else {
		// ToDoɾ��ʧ��
	}

}

}

	/**
	 * ���listview�����޸������û��Ĵ���
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
				// ȷ����ť
				.setNeutralButton(
						getString(R.string.ButtonTextSave),
						new OnAddOrEditAccountListener(pModelAccount, _etAccountName,_chkIsDefault,
								true))
				// ȡ����ť
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
			// �ж����޸Ļ������
			if (mModelAccount == null) {
				mModelAccount = new ModelAccount();
			}
			String AccountName = etAccountName.getText().toString().trim();
			
			// ����û�����ʽ
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
					showMessage("����Ҫ��һ��Ĭ���˱���");
					return;
				}
				}else if(adapterAccount.getCount() == 1)
				{
					showMessage("����Ҫ��һ��Ĭ���˱���");
					return;
				}
			}
			
			
			// �ж��û����Ƿ����
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

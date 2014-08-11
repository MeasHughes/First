package maes.infomanagement.activity;

import maes.infomanagement.R;
import maes.infomanagement.activity.base.ActivityFrame;
import maes.infomanagement.adapter.AdapterAccountBookSelect;
import maes.infomanagement.adapter.AdapterPayout;
import maes.infomanagement.bussiness.BusinessPayout;
import maes.infomanagement.bussiness.BussinessAccount;
import maes.infomanagement.control.SlideMenuItem;
import maes.infomanagement.control.SlideMenuView.OnSlideMenuListener;
import maes.infomanagement.model.ModelAccount;
import maes.infomanagement.model.ModelPayout;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ActivityPayout extends ActivityFrame implements OnSlideMenuListener {
	private ListView lvPayoutList;
	private ModelPayout mSelectModelPayout;
	private BusinessPayout mBusinessPayout;
	private AdapterPayout mAdapterPayout;
	private ModelAccount mAccountBook;
	private BussinessAccount mBusinessAccountBook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		addMainBody(R.layout.payout_list);
		initVariable();
		initView();
		initListeners();
		bindData();
		createSlideMenu(R.array.SlideMenuPayout);
	}

	private void setTitle() {
		int count = lvPayoutList.getCount();
		String titel = getString(R.string.ActivityTitlePayout,
				new Object[] { mAccountBook.getAccountBookName(), count });
		setTopBarTitle(titel);
	}

	private void initVariable() {
		mBusinessPayout = new BusinessPayout(ActivityPayout.this);
		mBusinessAccountBook = new BussinessAccount(ActivityPayout.this);
		mAccountBook = mBusinessAccountBook.getDefaultModelAccountBook();
		mAdapterPayout = new AdapterPayout(this,mAccountBook.getAccountBookID());

	}

	private void initView() {
		lvPayoutList = (ListView) findViewById(R.id.lvPayoutList);

	}

	private void initListeners() {
		registerForContextMenu(lvPayoutList);
	}

	private void bindData() {
		AdapterPayout adapterPayout = new AdapterPayout(this, mAccountBook.getAccountBookID());
		lvPayoutList.setAdapter(adapterPayout);
		setTitle();
	}

	@Override
	public void onSlideMenuItemClick(View view, SlideMenuItem slideMenuItem) {
		slideMenuToggle();
		if (slideMenuItem.getmItemID() == 0) {
			ShowAccountBookSelectDialog();
		}
	}

	private void ShowAccountBookSelectDialog() {
		AlertDialog.Builder _Builder = new AlertDialog.Builder(this);
		View _View = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
		ListView _ListView = (ListView) _View.findViewById(R.id.lvSelect);
		AdapterAccountBookSelect _AdapterAccountBookSelect = new AdapterAccountBookSelect(this);
		_ListView.setAdapter(_AdapterAccountBookSelect);

		_Builder.setTitle(R.string.ButtonTextSelectAccountBook);
		_Builder.setNegativeButton(R.string.ButtonTextBack, null);
		_Builder.setView(_View);
		AlertDialog _AlertDialog = _Builder.create();
		_AlertDialog.show();
		_ListView.setOnItemClickListener(new OnAccountBookItemClickListener(_AlertDialog));
	}

	private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener {
		private AlertDialog m_AlertDialog;

		public OnAccountBookItemClickListener(AlertDialog p_AlertDialog) {
			m_AlertDialog = p_AlertDialog;
		}

		@Override
		public void onItemClick(AdapterView p_AdapterView, View arg1, int p_Position, long arg3) {
			mAccountBook = (ModelAccount) ((Adapter) p_AdapterView.getAdapter()).getItem(p_Position);
			bindData();
			m_AlertDialog.dismiss();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu p_ContextMenu, View p_View, ContextMenuInfo p_MenuInfo) {
		AdapterContextMenuInfo _AdapterContextMenuInfo = (AdapterContextMenuInfo) p_MenuInfo;
		ListAdapter _ListAdapter = lvPayoutList.getAdapter();
		mSelectModelPayout = (ModelPayout) _ListAdapter.getItem(_AdapterContextMenuInfo.position);
		p_ContextMenu.setHeaderIcon(R.drawable.small4);
		p_ContextMenu.setHeaderTitle(mSelectModelPayout.getCategoryName());
		int _ItemID[] = { 1, 2 };
		createMenu(p_ContextMenu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem p_Item) {
		switch (p_Item.getItemId()) {
		case 1:
			Intent _Intent = new Intent(this, ActivityPayoutAddOrEdit.class);
			_Intent.putExtra("ModelPayout", mSelectModelPayout);
			this.startActivityForResult(_Intent, 1);
			break;
		case 2:
			delete(mSelectModelPayout);
			break;
		default:
			break;
		}

		return super.onContextItemSelected(p_Item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1)
			bindData();
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void delete(ModelPayout p_ModelPayout) {
		Object _Object[] = { p_ModelPayout.getCategoryName() };
		String _Message = getString(R.string.DialogMessagePayoutDelete, _Object);
		showAlertDialog(R.string.DialogTitleDelete, _Message, new OnDeleteClickListener());
	}

	private class OnDeleteClickListener implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {

			Boolean _Result = mBusinessPayout.deletePayoutByPayoutID(mSelectModelPayout.getPayoutID());
			if (_Result == true) {
				// mAdapterPayout.GetList().remove(mListViewPosition);
				// mAdapterPayout.notifyDataSetChanged();
				bindData();
			}
		}

	}
}

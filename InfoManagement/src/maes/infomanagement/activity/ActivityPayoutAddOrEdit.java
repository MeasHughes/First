package maes.infomanagement.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import maes.infomanagement.R;
import maes.infomanagement.activity.base.ActivityFrame;
import maes.infomanagement.adapter.AdapterAccountBookSelect;
import maes.infomanagement.adapter.AdapterCategory;
import maes.infomanagement.adapter.AdapterUser;
import maes.infomanagement.bussiness.BusinessCategory;
import maes.infomanagement.bussiness.BusinessPayout;
import maes.infomanagement.bussiness.BussinessAccount;
import maes.infomanagement.bussiness.BussinessUser;
import maes.infomanagement.control.NumberDialog;
import maes.infomanagement.control.NumberDialog.OnNumberDialogListener;
import maes.infomanagement.model.ModelAccount;
import maes.infomanagement.model.ModelCategory;
import maes.infomanagement.model.ModelPayout;
import maes.infomanagement.model.ModelUser;
import maes.infomanagement.util.DateTools;
import maes.infomanagement.util.RegexTools;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ActivityPayoutAddOrEdit extends ActivityFrame implements OnClickListener, OnNumberDialogListener {

	private Button btSave;
	private Button btCancel;

	private ModelPayout mModelPayout;
	private BusinessPayout mBusinessPayout;
	private Integer mAccountBookID;
	private Integer mCategoryID;
	private String mPayoutUserID;
	private String mPayoutTypeArr[];

	private EditText etAccountBookName;
	private EditText etAmount;
	private AutoCompleteTextView actvCategoryName;
	private EditText etPayoutDate;
	private EditText etPayoutType;
	private EditText etPayoutUser;
	private EditText etComment;
	private Button btSelectCategory;
	private Button btSelectUser;
	private Button btSelectAccountBook;
	private Button btSelectAmount;
	private Button btSelectPayoutDate;
	private Button btSelectPayoutType;

	private BussinessAccount mBusinessAccountBook;
	private BusinessCategory mBusinessCategory;
	private ModelAccount mModelAccountBook;
	private BussinessUser mBusinessUser;
	private List<RelativeLayout> mItemColor;
	private List<ModelUser> mUserSelectedList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addMainBody(R.layout.payout_add_or_edit);
		removeBottomBox();
		initVariable();
		initView();
		bindData();
		setTitle();
		initListeners();
	}
	

	protected void initVariable() {
		mBusinessPayout = new BusinessPayout(this);
		mModelPayout = (ModelPayout) getIntent().getSerializableExtra("ModelPayout");
		mBusinessAccountBook = new BussinessAccount(this);
		mBusinessCategory = new BusinessCategory(this);
		mModelAccountBook = mBusinessAccountBook.getDefaultModelAccountBook();
		mBusinessUser = new BussinessUser(this);
	}
	
	private void initView() {
		btSave = (Button) findViewById(R.id.btSave);
		btCancel = (Button) findViewById(R.id.btCancel);
		btSelectAccountBook = (Button) findViewById(R.id.btSelectAccountBook);
		btSelectAmount = (Button) findViewById(R.id.btSelectAmount);
		btSelectCategory = (Button) findViewById(R.id.btSelectCategory);
		btSelectPayoutDate = (Button) findViewById(R.id.btSelectPayoutDate);
		btSelectPayoutType = (Button) findViewById(R.id.btSelectPayoutType);
		btSelectUser = (Button) findViewById(R.id.btSelectUser);
		etAccountBookName = (EditText) findViewById(R.id.etAccountBookName);
		etPayoutDate = (EditText) findViewById(R.id.etPayoutDate);
		etPayoutType = (EditText) findViewById(R.id.etPayoutType);
		etAmount = (EditText) findViewById(R.id.etAmount);
		etAccountBookName = (EditText) findViewById(R.id.etAccountBookName);
		actvCategoryName = (AutoCompleteTextView) findViewById(R.id.actvCategoryName);
		etPayoutUser = (EditText) findViewById(R.id.etPayoutUser);
		etComment = (EditText) findViewById(R.id.etComment);
	}

	private void bindData() {
		mAccountBookID = mModelAccountBook.getAccountBookID();
		etAccountBookName.setText(mModelAccountBook.getAccountBookName());
		actvCategoryName.setAdapter(mBusinessCategory.getAllCategoryArrayAdapter());
		etPayoutDate.setText(DateTools.getFormatDateTime(new Date(), "yyyy-MM-dd"));
		mPayoutTypeArr = getResources().getStringArray(R.array.PayoutType);
		etPayoutType.setText(mPayoutTypeArr[0]);
	}
	
	private void setTitle() {
		String titel;
		if (mModelPayout == null) {
			titel = getString(R.string.ActivityTitlePayoutAddOrEdit, new Object[] { getString(R.string.TitleAdd) });
		} else {
			titel = getString(R.string.ActivityTitlePayoutAddOrEdit, new Object[] { getString(R.string.TitleEdit) });
			initData(mModelPayout);
		}

		setTopBarTitle(titel);
	}
	
	/**
	 * 如果是要编辑消费内容,则以此方法初始化数据
	 * @param modelPayout
	 */
	private void initData(ModelPayout modelPayout) {
		etAccountBookName.setText(modelPayout.getAccountBookName());
		mAccountBookID = modelPayout.getAccountBookID();
		etAmount.setText(modelPayout.getAmount().toString());
		actvCategoryName.setText(modelPayout.getCategoryName());
		mCategoryID = modelPayout.getCategoryID();
		etPayoutDate.setText(DateTools.getFormatDateTime(modelPayout.getPayoutDate(), "yyyy-MM-dd"));
		etPayoutType.setText(modelPayout.getPayoutType());

		BussinessUser businessUser = new BussinessUser(this);
		String userNameString = businessUser.getUserNameByUserID(modelPayout.getPayoutUserID());
		etPayoutUser.setText(userNameString);
		mPayoutUserID = modelPayout.getPayoutUserID();
		etComment.setText(modelPayout.getComment());
	}
	
	private void initListeners() {
		btSave.setOnClickListener(this);
		btCancel.setOnClickListener(this);
		btSelectAmount.setOnClickListener(this);
		btSelectCategory.setOnClickListener(this);
		btSelectPayoutDate.setOnClickListener(this);
		btSelectPayoutType.setOnClickListener(this);
		btSelectUser.setOnClickListener(this);
		actvCategoryName.setOnItemClickListener(new OnAutoCompleteTextViewItemClickListener());
		btSelectAccountBook.setOnClickListener(this);
	}

	private class OnAutoCompleteTextViewItemClickListener implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView adapterView, View arg1, int postion, long arg3) {
			ModelCategory _ModelCategory = (ModelCategory) adapterView.getAdapter().getItem(postion);
			mCategoryID = _ModelCategory.getCategoryID();
		}

	}



	


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btSelectAccountBook:
			showAccountBookSelectDialog();
			break;
		case R.id.btSelectAmount:
			(new NumberDialog(this)).show();
			break;
		case R.id.btSelectCategory:
			showCategorySelectDialog();
			break;
		case R.id.btSelectPayoutDate:
			Calendar calendar = Calendar.getInstance();
			showDateSelectDialog(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
			break;
		case R.id.btSelectPayoutType:
			showPayoutTypeSelectDialog();
			break;
		case R.id.btSelectUser:
			showUserSelectDialog(etPayoutType.getText().toString());
			break;
		case R.id.btSave:
			AddOrEditPayout();
			break;
		case R.id.btCancel:
			finish();
			break;
		default:
			break;
		}
	}
	private void AddOrEditPayout() {
		Boolean _CheckResult = CheckData();
		if (_CheckResult == false) {
			return;
		}

		if (mModelPayout == null) {
			mModelPayout = new ModelPayout();
		}
		mModelPayout.setAccountBookID(mAccountBookID);
		mModelPayout.setCategoryID(mCategoryID);
		mModelPayout.setAmount(new BigDecimal(etAmount.getText().toString().trim()));
		mModelPayout.setPayoutDate(DateTools.getDate(etPayoutDate.getText().toString().trim(), "yyyy-MM-dd"));
		mModelPayout.setPayoutType(etPayoutType.getText().toString().trim());
		mModelPayout.setPayoutUserID(mPayoutUserID);
		mModelPayout.setComment(etComment.getText().toString().trim());

		Boolean _Result = false;

		if (mModelPayout.getPayoutID() == 0) {
			_Result = mBusinessPayout.insertPayout(mModelPayout);
		} else {
			_Result = mBusinessPayout.updatePayoutByPayoutID(mModelPayout);
		}

		if (_Result) {
			Toast.makeText(getApplicationContext(), getString(R.string.TipsAddSucceed), 1).show();
			finish();
		} else {
			Toast.makeText(getApplicationContext(), getString(R.string.TipsAddFail), 1).show();
		}
	}
	private Boolean CheckData() {
		Boolean checkResult = RegexTools.IsMoney(etAmount.getText().toString().trim());
		if (checkResult == false) {
			etAmount.requestFocus();
			// etAmount.setFocusable(false);
			Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextMoney), 1).show();
			return checkResult;
		}

		checkResult = RegexTools.IsNull(mCategoryID);
		if (checkResult == false) {
			btSelectCategory.setFocusable(true);
			btSelectCategory.setFocusableInTouchMode(true);
			btSelectCategory.requestFocus();
			Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextCategoryIsNull), 1).show();
			return checkResult;
		}

		if (mPayoutUserID == null) {
			btSelectUser.setFocusable(true);
			btSelectUser.setFocusableInTouchMode(true);
			btSelectUser.requestFocus();
			Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextPayoutUserIsNull), 1).show();
			return false;
		}

		String payoutType = etPayoutType.getText().toString();
		if (payoutType.equals(mPayoutTypeArr[0]) || payoutType.equals(mPayoutTypeArr[1])) {
			if (mPayoutUserID.split(",").length <= 1) {
				btSelectUser.setFocusable(true);
				btSelectUser.setFocusableInTouchMode(true);
				btSelectUser.requestFocus();
				Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextPayoutUser), 1).show();
				return false;
			}
		} else {
			if (mPayoutUserID.equals("")) {
				btSelectUser.setFocusable(true);
				btSelectUser.setFocusableInTouchMode(true);
				btSelectUser.requestFocus();
				Toast.makeText(getApplicationContext(), getString(R.string.CheckDataTextPayoutUser2), 1).show();
				return false;
			}
		}

		return true;
	}
	private void showUserSelectDialog(String payoutType) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.layout_user, null);
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.user_layout);
		linearLayout.setBackgroundResource(R.color.blueviolet);
		ListView listView = (ListView) view.findViewById(R.id.lv_user);
		AdapterUser adapterUser = new AdapterUser(this);
		listView.setAdapter(adapterUser);

		builder.setIcon(R.drawable.pic_head);
		builder.setTitle(R.string.ButtonTextSelectUser);
		builder.setNegativeButton(R.string.ButtonTextBack, new OnSelectUserBack());
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		listView.setOnItemClickListener(new OnUserItemClickListener(alertDialog, payoutType));
	}

	private class OnSelectUserBack implements DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			etPayoutUser.setText("");
			String names = "";
			mPayoutUserID = "";
			if (mUserSelectedList != null) {
				for (int i = 0; i < mUserSelectedList.size(); i++) {
					names += mUserSelectedList.get(i).getmUserName() + ",";
					mPayoutUserID += mUserSelectedList.get(i).getmUserID() + ",";
				}
				names = names.substring(0,names.length() - 1);
				etPayoutUser.setText(names);
			}

			mItemColor = null;
			mUserSelectedList = null;
		}
	}
	private class OnUserItemClickListener implements AdapterView.OnItemClickListener {
		private AlertDialog m_AlertDialog;
		private String payoutType;

		public OnUserItemClickListener(AlertDialog p_AlertDialog, String p_PayoutType) {
			m_AlertDialog = p_AlertDialog;
			payoutType = p_PayoutType;
		}

		@Override
		public void onItemClick(AdapterView p_AdapterView, View arg1, int p_Position, long arg3) {
			String payoutTypeArr[] = getResources().getStringArray(R.array.PayoutType);
			ModelUser _ModelUser = (ModelUser) ((Adapter) p_AdapterView.getAdapter()).getItem(p_Position);
			if (payoutType.equals(payoutTypeArr[0]) || payoutType.equals(payoutTypeArr[1])) {
				RelativeLayout layUserItemMan = (RelativeLayout) arg1.findViewById(R.id.layUserItemMan);

				if (mItemColor == null && mUserSelectedList == null) {
					mItemColor = new ArrayList<RelativeLayout>();
					mUserSelectedList = new ArrayList<ModelUser>();
				}

				if (mItemColor.contains(layUserItemMan)) {
					layUserItemMan.setBackgroundResource(R.color.blue);
					mItemColor.remove(layUserItemMan);
					mUserSelectedList.remove(_ModelUser);
				} else {
					layUserItemMan.setBackgroundResource(R.color.red);
					mItemColor.add(layUserItemMan);
					mUserSelectedList.add(_ModelUser);
				}
				return;
			}

			if (payoutType.equals(payoutTypeArr[2])) {
				mUserSelectedList = new ArrayList<ModelUser>();
				mUserSelectedList.add(_ModelUser);
				etPayoutUser.setText("");
				String _Name = "";
				mPayoutUserID = "";
				for (int i = 0; i < mUserSelectedList.size(); i++) {
					_Name += mUserSelectedList.get(i).getmUserName() + ",";
					mPayoutUserID += mUserSelectedList.get(i).getmUserID() + ",";
				}
				etPayoutUser.setText(_Name);

				mItemColor = null;
				mUserSelectedList = null;
				m_AlertDialog.dismiss();
			}
		}
	}
	
	/**
	 * 选择账本,弹出Dialog选择账本
	 */
	private void showAccountBookSelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
		ListView listView = (ListView) view.findViewById(R.id.lvSelect);
		AdapterAccountBookSelect adapterAccountBookSelect = new AdapterAccountBookSelect(this);
		listView.setAdapter(adapterAccountBookSelect);

		builder.setTitle(R.string.ButtonTextSelectAccountBook);
		builder.setNegativeButton(R.string.ButtonTextBack, null);
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		//将新建的Dialog传入到新建的监听类中
		listView.setOnItemClickListener(new OnAccountBookItemClickListener(alertDialog));
	}

	private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener {
		private AlertDialog mAlertDialog;

		public OnAccountBookItemClickListener(AlertDialog alertDialog) {
			mAlertDialog = alertDialog;
		}

		@Override
		public void onItemClick(AdapterView adapterView, View arg1, int position, long arg3) {
			ModelAccount modelAccountBook = (ModelAccount) ((Adapter) adapterView.getAdapter())
					.getItem(position);
			etAccountBookName.setText(modelAccountBook.getAccountBookName());
			mAccountBookID = modelAccountBook.getAccountBookID();
			mAlertDialog.dismiss();
		}
	}

	private void showCategorySelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.category_select_list, null);
		ExpandableListView expandableListView = (ExpandableListView) view.findViewById(R.id.elvCategory);
		AdapterCategory adapterCategorySelect = new AdapterCategory(this);
		expandableListView.setAdapter(adapterCategorySelect);

		builder.setIcon(R.drawable.small2);
		builder.setTitle(R.string.ButtonTextSelectCategory);
		builder.setNegativeButton(R.string.ButtonTextBack, null);
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		expandableListView.setOnGroupClickListener(new OnCategoryGroupItemClickListener(alertDialog,
				adapterCategorySelect));
		expandableListView.setOnChildClickListener(new OnCategoryChildItemClickListener(alertDialog,
				adapterCategorySelect));
	}

	private class OnCategoryGroupItemClickListener implements OnGroupClickListener {
		private AlertDialog mAlertDialog;
		private AdapterCategory mAdapterCategory;

		public OnCategoryGroupItemClickListener(AlertDialog alertDialog, AdapterCategory adapterCategory) {
			mAlertDialog = alertDialog;
			mAdapterCategory = adapterCategory;
		}

		@Override
		public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
			int count = mAdapterCategory.getChildrenCount(groupPosition);
			if (count == 0) {
				ModelCategory modelCategory = (ModelCategory) mAdapterCategory.getGroup(groupPosition);
				actvCategoryName.setText(modelCategory.getCategoryName());
				mCategoryID = modelCategory.getCategoryID();
				mAlertDialog.dismiss();
			}
			return false;
		}

	}

	private class OnCategoryChildItemClickListener implements OnChildClickListener {
		private AlertDialog mAlertDialog;
		private AdapterCategory mAdapterCategory;

		public OnCategoryChildItemClickListener(AlertDialog alertDialog, AdapterCategory adapterCategory) {
			mAlertDialog = alertDialog;
			mAdapterCategory = adapterCategory;
		}

		@Override
		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
			ModelCategory modelCategory = (ModelCategory) mAdapterCategory.getChild(groupPosition, childPosition);
			actvCategoryName.setText(modelCategory.getCategoryName());
			mCategoryID = modelCategory.getCategoryID();
			mAlertDialog.dismiss();
			return false;
		}

	}

	private void showDateSelectDialog(int p_Year, int p_Month, int p_Day) {
		(new DatePickerDialog(this, new OnDateSelectedListener(), p_Year, p_Month, p_Day)).show();
	}

	private class OnDateSelectedListener implements OnDateSetListener {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			//年要减去1900
			Date _Date = new Date(year - 1900, monthOfYear, dayOfMonth);
			etPayoutDate.setText(DateTools.getFormatDateTime(_Date, "yyyy-MM-dd"));
		}
	}
	private void showPayoutTypeSelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.payout_type_select_list, null);
		ListView listView = (ListView) view.findViewById(R.id.ListViewPayoutType);

		builder.setTitle(R.string.ButtonTextSelectPayoutType);
		builder.setNegativeButton(R.string.ButtonTextBack, null);
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		listView.setOnItemClickListener(new OnPayoutTypeItemClickListener(alertDialog));
	}

	private class OnPayoutTypeItemClickListener implements AdapterView.OnItemClickListener {
		private AlertDialog m_AlertDialog;

		public OnPayoutTypeItemClickListener(AlertDialog p_AlertDialog) {
			m_AlertDialog = p_AlertDialog;
		}

		@Override
		public void onItemClick(AdapterView adapterView, View arg1, int position, long arg3) {
			String payoutType = (String) adapterView.getAdapter().getItem(position);
			//切换计算方式后，将消费人清空
			etPayoutType.setText(payoutType);
			etPayoutUser.setText("");
			mPayoutUserID = "";
			m_AlertDialog.dismiss();
		}
	}

	@Override
	public void setNumberFinish(BigDecimal number) {
		((EditText) findViewById(R.id.etAmount)).setText(number.toString());
	}
}

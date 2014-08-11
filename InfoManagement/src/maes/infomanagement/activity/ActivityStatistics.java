package maes.infomanagement.activity;

import maes.infomanagement.R;
import maes.infomanagement.activity.base.ActivityFrame;
import maes.infomanagement.adapter.AdapterAccountBookSelect;
import maes.infomanagement.bussiness.BusinessStatistics;
import maes.infomanagement.bussiness.BussinessAccount;
import maes.infomanagement.control.SlideMenuItem;
import maes.infomanagement.control.SlideMenuView.OnSlideMenuListener;
import maes.infomanagement.model.ModelAccount;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Title: ActivityStatistics.java
 * @Package org.fight.AccountingAssistant.activity
 * @Description: 消费信息统计
 * 
 * @author lizhipeng lzpeng@live.cn
 * @date 2012-2-22 下午1:06:32
 * @version V1.3
 */

public class ActivityStatistics extends ActivityFrame implements OnSlideMenuListener {
	private TextView tvStatisticsResult;
	private BusinessStatistics mBusinessStatistics;
	private ModelAccount mAccountBook;
	private BussinessAccount mBusinessAccountBook;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addMainBody(R.layout.statistics);
		initVariable();
		initView();
		initListeners();
		bindData();
		SetTitle();
		createSlideMenu(R.array.SlideMenuStatistics);

	}

	private void initVariable() {
		mBusinessStatistics = new BusinessStatistics(ActivityStatistics.this);
		mBusinessAccountBook = new BussinessAccount(ActivityStatistics.this);
		mAccountBook = mBusinessAccountBook.getDefaultModelAccountBook();

	}

	private void initView() {
		tvStatisticsResult = (TextView) findViewById(R.id.tvStatisticsResult);
	}

	private void initListeners() {
	}

	private void bindData() {
		showProgressDialog(R.string.StatisticsProgressDialogTitle, R.string.StatisticsProgressDialogWaiting);
		new BindDataThread().start();
	}

	private void SetTitle() {
		// int _Count = lvStatisticsList.getCount();
		// int _Count = 7;
		String _Titel = getString(R.string.ActivityTitleStatistics, new Object[] { mAccountBook.getAccountBookName() });
		setTopBarTitle(_Titel);
	}

	private class BindDataThread extends Thread {
		@Override
		public void run() {
			String result = mBusinessStatistics.getPayoutUserIDByAccountBookID(mAccountBook.getAccountBookID());
			Message message = new Message();
			message.obj = result;
			message.what = 1;
			mHandler.sendMessage(message);
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String result = (String) msg.obj;
				tvStatisticsResult.setText(result);
				dismissProgressDialog();
				break;
			default:
				break;
			}
		}
	};

	protected void dismissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	public void onSlideMenuItemClick(View p_View, SlideMenuItem p_SlideMenuItem) {
		slideMenuToggle();
		if (p_SlideMenuItem.getmItemID() == 0) {
			ShowAccountBookSelectDialog();
		}
		if (p_SlideMenuItem.getmItemID() == 1) {
			exportData();
		}
	}

	private void ShowAccountBookSelectDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_list, null);
		ListView listView = (ListView) view.findViewById(R.id.lvSelect);
		AdapterAccountBookSelect _AdapterAccountBookSelect = new AdapterAccountBookSelect(this);
		listView.setAdapter(_AdapterAccountBookSelect);

		builder.setTitle(R.string.ButtonTextSelectAccountBook);
		builder.setNegativeButton(R.string.ButtonTextBack, null);
		builder.setView(view);
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		listView.setOnItemClickListener(new OnAccountBookItemClickListener(alertDialog));
	}

	private class OnAccountBookItemClickListener implements AdapterView.OnItemClickListener {
		private AlertDialog m_AlertDialog;

		public OnAccountBookItemClickListener(AlertDialog p_AlertDialog) {
			m_AlertDialog = p_AlertDialog;
		}

		@Override
		public void onItemClick(AdapterView adapterView, View arg1, int position, long arg3) {
			mAccountBook = (ModelAccount) ((Adapter) adapterView.getAdapter()).getItem(position);
			bindData();
			m_AlertDialog.dismiss();
		}
	}

	private void exportData() {
		String result = "";
		try {
			result = mBusinessStatistics.ExportStatistics(mAccountBook.getAccountBookID());
		} catch (Exception e) {
			e.printStackTrace();
			result = "导出失败！";
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}
}

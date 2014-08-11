package maes.infomanagement.activity;

import java.io.Serializable;
import java.util.List;

import maes.infomanagement.R;
import maes.infomanagement.activity.base.ActivityFrame;
import maes.infomanagement.adapter.AdapterCategory;
import maes.infomanagement.bussiness.BusinessCategory;
import maes.infomanagement.control.SlideMenuItem;
import maes.infomanagement.control.SlideMenuView.OnSlideMenuListener;
import maes.infomanagement.model.ModelCategory;
import maes.infomanagement.model.ModelCategoryTotal;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;
import android.widget.Toast;

public class ActivityCategory extends ActivityFrame implements OnSlideMenuListener {

	private ExpandableListView elvCategory;
	private ModelCategory mSelectModelCategory;
	private BusinessCategory mBusinessCategory;
	private AdapterCategory mAdapterCategory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addMainBody(R.layout.category);
		initVariable();
		initView();
		initListeners();
		bindData();
		createSlideMenu(R.array.SlideMenuCategory);
	}

	private void initVariable() {
		mBusinessCategory = new BusinessCategory(ActivityCategory.this);
	}

	private void initView() {
		elvCategory = (ExpandableListView) findViewById(R.id.elvCategory);
		setTitle();
	}

	private void setTitle() {
		int count = mBusinessCategory.getNotHideCount();
		String title = getString(R.string.ActivityTitleCategory, new Object[] { count });
		setTopBarTitle(title);
	}

	private void initListeners() {
		registerForContextMenu(elvCategory);
	}

	private void bindData() {
		mAdapterCategory = new AdapterCategory(this);
		elvCategory.setAdapter(mAdapterCategory);
		setActivityTitle();
	}

	private void setActivityTitle() {
		int count = mBusinessCategory.getNotHideCount();
		String title = getString(R.string.ActivityTitleCategory, new Object[] { count });
		setTopBarTitle(title);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		ExpandableListContextMenuInfo expandableListContextMenuInfo = (ExpandableListContextMenuInfo) menuInfo;
		long position = expandableListContextMenuInfo.packedPosition;
		int type = ExpandableListView.getPackedPositionType(position);
		int groupPosition = ExpandableListView.getPackedPositionGroup(position);
		switch (type) {
		case ExpandableListView.PACKED_POSITION_TYPE_GROUP:
			mSelectModelCategory = (ModelCategory) mAdapterCategory.getGroup(groupPosition);
			break;
		case ExpandableListView.PACKED_POSITION_TYPE_CHILD:
			int childPosition = ExpandableListView.getPackedPositionChild(position);
			mSelectModelCategory = (ModelCategory) mAdapterCategory.getChild(groupPosition, childPosition);
			break;
		default:
			break;
		}

		menu.setHeaderIcon(R.drawable.categorymana);
		if (mSelectModelCategory != null) {
			menu.setHeaderTitle(mSelectModelCategory.getCategoryName());
		}
		// 添加了菜单
		createMenu(menu);
		menu.add(0, 3, 3, R.string.ActivityCategoryTotal);
		if (mAdapterCategory.getChildCountOfGroup(groupPosition) != 0 && mSelectModelCategory.getParentID() == 0) {
			menu.findItem(2).setEnabled(false);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case 1:
			intent = new Intent(this, ActivityCategoryAddOrEdit.class);
			intent.putExtra("ModelCategory", mSelectModelCategory);
			startActivityForResult(intent, 1);
			break;
		case 2:
			delete(mSelectModelCategory);
			break;
		case 3:
			/*List<ModelCategoryTotal> list = mBusinessCategory.getCategoryTotalByParentID(mSelectModelCategory.getCategoryID());
			intent = new Intent();
			intent.putExtra("Total", (Serializable)list);
			intent.setClass(this, ActivityCategoryChart.class);
			startActivity(intent);*/
			break;
		default:
			break;
		}
		return false;
	}

	private void delete(ModelCategory modelCategory) {
		Object object[] = { modelCategory.getCategoryName() };
		String message = getString(R.string.DialogMessageCategoryDelete, object);
		showAlertDialog(R.string.DialogTitleDelete, message, new OnDeleteClickListener(modelCategory));
	}

	private class OnDeleteClickListener implements DialogInterface.OnClickListener {
		private ModelCategory mModelCategory;

		public OnDeleteClickListener(ModelCategory modelCategory) {
			mModelCategory = modelCategory;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Boolean result;
			result = mBusinessCategory.hideCategoryByByPath(mModelCategory.getPath());
			if (result == true) {
				bindData();
			} else {
				Toast.makeText(getApplicationContext(), R.string.TipsDeleteFail, 1).show();
			}
		}

	}

	// 自动回调
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		bindData();
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSlideMenuItemClick(View pView, SlideMenuItem pSlideMenuItem) {
		slideMenuToggle();
		if (pSlideMenuItem.getmItemID() == 0) {
			Intent intent = new Intent(this, ActivityCategoryAddOrEdit.class);
			startActivityForResult(intent, 1);
			return;
		}
		if (pSlideMenuItem.getmItemID() == 1) {
			List<ModelCategoryTotal> _List = mBusinessCategory.getCategoryTotalByRootCategory();
			Intent _Intent = new Intent();
			_Intent.putExtra("Total", (Serializable) _List);
			_Intent.setClass(this, ActivityCategoryChart.class);
			startActivity(_Intent);
		}
	}
}

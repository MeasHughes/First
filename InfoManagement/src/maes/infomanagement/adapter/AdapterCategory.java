package maes.infomanagement.adapter;

import java.util.ArrayList;
import java.util.List;


import maes.infomanagement.R;
import maes.infomanagement.bussiness.BusinessCategory;
import maes.infomanagement.model.ModelCategory;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class AdapterCategory extends BaseExpandableListAdapter{
	private class HolderGroup {
		TextView tvName;
		TextView tvCount;
	} 
	
	private class HolderChild{
		TextView tvName;
	}
	
	private Context mContext;
	private List mList;
	private List mChildCountOfGroup;
	public BusinessCategory mBusinessCategory;
	
	public AdapterCategory(Context context){
		mContext = context;
		mBusinessCategory = new BusinessCategory(context);
		mList = mBusinessCategory.getNotHideRootCategory();
		mChildCountOfGroup = new ArrayList();
	}
	
	@Override
	public int getGroupCount() {
		
		return mList.size();
	}
	@Override
	public int getChildrenCount(int groupPosition) {
		return (Integer) mChildCountOfGroup.get(groupPosition);
	}
	@Override
	public Object getGroup(int groupPosition) {
		return (ModelCategory)mList.get(groupPosition);
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ModelCategory modelCategory = (ModelCategory) getGroup(groupPosition);
		List list =mBusinessCategory.getNotHideCategoryListByParentID(modelCategory.getCategoryID());
		return list.get(childPosition);
		
	}
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	@Override
	public boolean hasStableIds() {
		//行是否具有唯一id
		// 是否指定分组视图及其子视图的ID对应的后台数据改变也会保持该ID
		return false;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		HolderGroup holderGroup;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.category_group_list_item, null);
			holderGroup = new HolderGroup();
			holderGroup.tvName = (TextView) convertView.findViewById(R.id.tvCategoryName);
			holderGroup.tvCount = (TextView) convertView.findViewById(R.id.tvCount);
			convertView.setTag(holderGroup);
		}else {
			holderGroup = (HolderGroup) convertView.getTag();
		}
		ModelCategory modelCategory = (ModelCategory) getGroup(groupPosition);
		holderGroup.tvName.setText(modelCategory.getCategoryName());
		int count = mBusinessCategory.getNotHideCountByParentID(modelCategory.getCategoryID());
		holderGroup.tvCount.setText(mContext.getString(R.string.TextViewTextChildrenCategory,count));
		mChildCountOfGroup.add(count);
		return convertView;
	}
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		HolderChild holderChild;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.category_children_list_item, null);
			holderChild = new HolderChild();
			holderChild.tvName = (TextView) convertView.findViewById(R.id.tvCategoryName);
			convertView.setTag(holderChild);
		}else {
			holderChild = (HolderChild) convertView.getTag(); 
		}
		ModelCategory modelCategory = (ModelCategory) getChild(groupPosition, childPosition);
		holderChild.tvName.setText(modelCategory.getCategoryName());
		return convertView;
	}
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// 行是否可选
		return true;
	}
	
	public Integer getChildCountOfGroup(int groupPosition){
		
		return (Integer) mChildCountOfGroup.get(groupPosition);
	}
	
}

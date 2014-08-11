package maes.infomanagement.adapter.base;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AdapterBase extends BaseAdapter {

	private Context mContext;
	private List mList;
	private LayoutInflater mlInflater;
	
	public AdapterBase(Context pContext,List pList){
		mContext = pContext;
		mList = pList;
		mlInflater = LayoutInflater.from(mContext);
	}
	
	public LayoutInflater getLayoutInflater(){
		return mlInflater;
	}
	
	public Context getContext(){
		return mContext;
	}
	
	public List getList(){
		return mList;
	}
	
	public void setList(List pList)
	{
		mList = pList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}
	
	

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}

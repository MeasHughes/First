package maes.infomanagement.bussiness.base;

import android.content.Context;

public class BussinessBase {
	private Context mContext;
	
	public BussinessBase(Context pContext)
	{
		mContext = pContext;
	}
	
	//�����Ļ�ȡString�õ���ʲô�������ID��ʲô��ID��
	public String getsString(int pResID)
	{
		return mContext.getString(pResID); 
	}
	
	public String getsString(int pResID,Object object[])
	{
		return mContext.getString(pResID,object); 
	}
	
	public Context getContext() {
		return mContext;
	}

	public void setContext(Context pContext) {
		this.mContext = pContext;
	}
}

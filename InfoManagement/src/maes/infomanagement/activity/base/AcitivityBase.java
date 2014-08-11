package maes.infomanagement.activity.base;

import java.lang.reflect.Field;

import maes.infomanagement.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

public class AcitivityBase extends Activity {

	protected ProgressDialog mProgressDialog;
	private static final int MSG_SHOW_TIME = 1000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	protected void openActivity(Class pClass)
	{
		Intent _Intent = new Intent();
		_Intent.setClass(this,pClass);
		startActivity(_Intent);
	}
	protected AlertDialog showAlertDialog(int titleId, String message, DialogInterface.OnClickListener OnClickListener) {
		String title = getString(titleId);
		return showAlertDialog(title, message, OnClickListener);
	}

	protected AlertDialog showAlertDialog(String title, String message, DialogInterface.OnClickListener OnClickListener) {
		return new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton(R.string.ButtonTextYes, OnClickListener)
				.setNegativeButton(R.string.ButtonTextNo, null).show();
	}
	
	protected void showMessage(String message) {
		Toast.makeText(this, message, MSG_SHOW_TIME).show();
	}
	
	public LayoutInflater getLayoutInflator()
	{
		LayoutInflater _Inflater = LayoutInflater.from(this);
		return _Inflater;
	}
	
	// 通过反射控制对话框的开启和关闭
	public void setAlertDialogIsClose(DialogInterface dialog, Boolean isClose) {
		try {
			Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, isClose);
		} catch (Exception e) {
		}
	}
	
	protected void showProgressDialog(int titleResID, int messageResID) {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle(getString(titleResID));
		mProgressDialog.setMessage(getString(messageResID));
		mProgressDialog.show();
	}

	protected void dismissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
}

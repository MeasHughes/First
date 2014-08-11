package maes.infomanagement.control;

import java.math.BigDecimal;

import maes.infomanagement.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class NumberDialog extends Dialog implements android.view.View.OnClickListener {
	private Context mContext;

	public interface OnNumberDialogListener {
		public abstract void setNumberFinish(BigDecimal number);
	}

	public NumberDialog(Context context) {
		super(context);
		mContext = context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.number_dialog);
		initListeners();
	}
	
	public void initListeners()
	{
		findViewById(R.id.btnDot).setOnClickListener(this);
		findViewById(R.id.btnZero).setOnClickListener(this);
		findViewById(R.id.btnOne).setOnClickListener(this);
		findViewById(R.id.btnTwo).setOnClickListener(this);
		findViewById(R.id.btnThree).setOnClickListener(this);
		findViewById(R.id.btnFour).setOnClickListener(this);
		findViewById(R.id.btnFive).setOnClickListener(this);
		findViewById(R.id.btnSix).setOnClickListener(this);
		findViewById(R.id.btnSeven).setOnClickListener(this);
		findViewById(R.id.btnEight).setOnClickListener(this);
		findViewById(R.id.btnNine).setOnClickListener(this);
		findViewById(R.id.btnChange).setOnClickListener(this);
		findViewById(R.id.btnOk).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		EditText editText = (EditText) findViewById(R.id.txtDisplay);
		String number = editText.getText().toString();

		switch (v.getId()) {
		case R.id.btnDot:
			if (number.indexOf(".") == -1) {
				number += ".";
			}
			break;
		case R.id.btnOne:
			number += "1";
			break;
		case R.id.btnTwo:
			number += "2";
			break;
		case R.id.btnThree:
			number += "3";
			break;
		case R.id.btnFour:
			number += "4";
			break;
		case R.id.btnFive:
			number += "5";
			break;
		case R.id.btnSix:
			number += "6";
			break;
		case R.id.btnSeven:
			number += "7";
			break;
		case R.id.btnEight:
			number += "8";
			break;
		case R.id.btnNine:
			number += "9";
			break;
		case R.id.btnZero:
			number += "0";
			break;
		case R.id.btnChange:
			if (number.length() != 0) {
				number = number.substring(0, number.length() - 1);
			}
			break;
		case R.id.btnOk:
			BigDecimal bigDecimal;
			if (!number.equals(".") && number.length() != 0) {
				bigDecimal = new BigDecimal(number);
			} else {
				bigDecimal = new BigDecimal(0);
			}
			((OnNumberDialogListener) mContext).setNumberFinish(bigDecimal);
			dismiss();
			break;
		default:
			break;
		}

		editText.setText(number);
	}
}

package my.localocal.views;

import my.localocal.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

public class NumberPickerDialog extends AlertDialog implements OnClickListener,NumberPicker.OnValueChangeListener{

	private static final String	      NUMBER	= "number";
	private final NumberPicker	      mNumberPicker;
	private final OnNumberSetListener	mCallback;
	private int	                      oldVal	= 0;
	private int	                      newVal	= 0;

	public interface OnNumberSetListener{
		void onNumberSet(NumberPicker view, int num);
	}

	public NumberPickerDialog(Context context, OnNumberSetListener mCallback, int minNum, int maxNum, String title) {
		super(context);

		this.mCallback = mCallback;

		Context themeContext = getContext();
		setTitle(title);
		setButton(BUTTON_NEGATIVE, "Cancel", this);
		setButton(BUTTON_POSITIVE, "Set", this);
		setIcon(0);

		LayoutInflater inflater = (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.number_picker_dialog, null);
		setView(view);
		mNumberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
		mNumberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		mNumberPicker.setMinValue(minNum);
		mNumberPicker.setMaxValue(maxNum);
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		this.oldVal = oldVal;
		this.newVal = newVal;
		tryNotifyNumberSet();
	}

	private void tryNotifyNumberSet() {
		if (mCallback != null) {
			mCallback.onNumberSet(mNumberPicker, newVal);
		}
	}

	@Override
	protected void onStop() {
		tryNotifyNumberSet();
		super.onStop();
	}

	@Override
	public Bundle onSaveInstanceState() {
		Bundle state = super.onSaveInstanceState();
		state.putInt(NUMBER, newVal);
		return state;
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int number = savedInstanceState.getInt(NUMBER);
		mNumberPicker.setValue(number);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == BUTTON_NEGATIVE) {
			mNumberPicker.setValue(0);
		}
		tryNotifyNumberSet();
	}

}

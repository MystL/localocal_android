package my.localocal.views;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment{

	private OnDateSetListener	onDateSetListener;
	private int	              year;
	private int	              month;
	private int	              day;

	public DatePickerFragment() {
		super();
	}

	public void setOnDateSetListener(OnDateSetListener listener) {
		onDateSetListener = listener;
	}

	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);

		if (args == null) {
			// Use the current date as the default date in the picker if nothing was set
			Calendar c = Calendar.getInstance();
			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);
		} else {
			year = args.getInt("year");
			month = args.getInt("month");
			day = args.getInt("date");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Create a new instance of DatePickerDialog and return it
		DatePickerDialog dialog = new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);
		dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
		return dialog;
	}

}

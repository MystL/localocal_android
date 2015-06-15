package my.localocal.views;

import my.localocal.booking.TourGuidesViewingActivity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DurationPickerFragment extends DialogFragment{

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new NumberPickerDialog(getActivity(), (TourGuidesViewingActivity) getActivity(), 0, 8, "Select Tour Duration (Hours)");
	}

}

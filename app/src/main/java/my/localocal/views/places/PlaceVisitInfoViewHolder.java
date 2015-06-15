package my.localocal.views.places;

import android.widget.TextView;

public class PlaceVisitInfoViewHolder{

	private TextView	lbl_title;
	private TextView	lbl_title_opHours;
	private TextView	lbl_opHours;
	private TextView	lbl_title_entFees;
	private TextView	lbl_entFees;

	public PlaceVisitInfoViewHolder() {
		//...
	}

	public void setPlaceVisitInfoTitleTextView(TextView textView) {
		lbl_title = textView;
	}

	public void setPlaceVisitInfoOpHoursTitleTextView(TextView textView) {
		lbl_title_opHours = textView;
	}

	public void setPlaceVisitInfoOpHoursTextView(TextView textView) {
		lbl_opHours = textView;
	}

	public void setPlaceVisitInfoEntFeesTitleTextView(TextView textView) {
		lbl_title_entFees = textView;
	}

	public void setPlaceVisitInfoEntFeesTextView(TextView textView) {
		lbl_entFees = textView;
	}

	public TextView getPlaceVisitInfoTitleTextView() {
		return lbl_title;
	}

	public TextView getPlaceVisitInfoOpHoursTitleTextView() {
		return lbl_title_opHours;
	}

	public TextView getPlaceVisitInfoOpHoursTextView() {
		return lbl_opHours;
	}

	public TextView getPlaceVisitInfoEntFeesTitleTextView() {
		return lbl_title_entFees;
	}

	public TextView getPlaceVisitInfoEntFeesTextView() {
		return lbl_entFees;
	}
}

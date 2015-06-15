package my.localocal.views.places;

import android.widget.TextView;

public class PlacesSectionHeaderItemViewHolder{

	private TextView	lbl_header_text;

	public PlacesSectionHeaderItemViewHolder() {
		//...
	}

	public void setPlacesSectionHeaderTextView(TextView textView) {
		lbl_header_text = textView;
	}

	public TextView getPlacesSectionHeaderTextView() {
		return lbl_header_text;
	}

}

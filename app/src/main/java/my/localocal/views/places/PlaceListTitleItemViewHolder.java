package my.localocal.views.places;

import android.widget.TextView;

public class PlaceListTitleItemViewHolder{

	private TextView	lbl_place_title;

	public PlaceListTitleItemViewHolder() {
		//...
	}

	public void setPlaceListTitleItemTextView(TextView textView) {
		lbl_place_title = textView;
		lbl_place_title.setSelected(true);
	}

	public TextView getPlaceListTitleTextView() {
		return lbl_place_title;
	}

}

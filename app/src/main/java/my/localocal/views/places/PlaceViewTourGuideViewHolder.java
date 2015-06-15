package my.localocal.views.places;

import android.view.View;
import android.widget.TextView;

public class PlaceViewTourGuideViewHolder{

	private TextView	lbl_text;
	private View	 view_bg;

	public PlaceViewTourGuideViewHolder() {
		//...
	}

	public void setPlaceViewTourGuideViewTextView(TextView textView) {
		lbl_text = textView;
	}

	public TextView getPlaceViewTourGuideViewTextView() {
		return lbl_text;
	}

	public void setPlaceViewTourGuideViewBg(View view) {
		view_bg = view;
	}

	public View getPlaceViewTourGuideViewBg() {
		return view_bg;
	}

}

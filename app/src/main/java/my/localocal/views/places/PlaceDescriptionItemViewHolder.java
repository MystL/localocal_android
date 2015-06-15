package my.localocal.views.places;

import android.view.View;
import android.widget.TextView;

public class PlaceDescriptionItemViewHolder{

	private TextView	lbl_description;
	private TextView	lbl_title;

	public PlaceDescriptionItemViewHolder() {
		//...
	}

	public void setDescriptionTitleTextView(TextView textView) {
		if (textView != null) {
			lbl_title = textView;
			lbl_title.setSelected(true);
			lbl_title.setVisibility(View.VISIBLE);
		}
	}

	public TextView getDesctiptionTitleTextView() {
		return lbl_title;
	}

	public void setDescriptionTextView(TextView textView) {
		if (textView != null) {
			lbl_description = textView;
			lbl_description.setSelected(true);
			lbl_description.setVisibility(View.VISIBLE);
		}
	}

	public TextView getDescriptionTextView() {
		return lbl_description;
	}

}

package my.localocal.views.places;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlaceTopInfoViewHolder{

	private TextView	lbl_name;
	private TextView	lbl_distance;
	private TextView	lbl_distance_away;
	private ImageButton	btn_share;
	private ImageButton	btn_fav;
	private View	    lyt_tags;

	public PlaceTopInfoViewHolder() {
		//...
	}

	public void setPlaceTopInfoNameTextView(TextView textView) {
		lbl_name = textView;
		lbl_name.setSelected(true);
	}

	public TextView getPlaceTopInfoNameTextView() {
		return lbl_name;
	}

	public void setPlaceTopInfoDistanceTextView(TextView textView) {
		lbl_distance = textView;
	}

	public TextView getPlaceTopInfoDistanceTextView() {
		return lbl_distance;
	}

	public void setPlaceTopInfoDistanceAwayTextView(TextView textView) {
		lbl_distance_away = textView;
	}

	public TextView getPlaceTopInfoDistanceAwayTextView() {
		return lbl_distance_away;
	}

	public void setPlaceTopInfoShareImageButton(ImageButton imageButton) {
		btn_share = imageButton;
	}

	public ImageButton getPlaceTopInfoShareImageButton() {
		return btn_share;
	}

	public void setPlaceTopInfoFavImageButton(ImageButton imageButton) {
		btn_fav = imageButton;
	}

	public ImageButton getPlaceTopInfoFavImageButton() {
		return btn_fav;
	}

	public void setPlaceTopInfoTagsLayout(View view) {
		lyt_tags = view;
	}

	public View getPlaceTopInfoTagsLayout() {
		return lyt_tags;
	}

}

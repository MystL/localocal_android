package my.localocal.views.profiling;

import my.localocal.circularimageview.RoundedImageView;
import android.view.View;
import android.widget.TextView;

public class PreferredPlaceItemViewHolder{

	private RoundedImageView	img_icon;
	private TextView	     lbl_title;

	public PreferredPlaceItemViewHolder() {
		//...
	}

	public void setPreferredPlaceItemIconImageView(RoundedImageView imageView) {
		img_icon = imageView;
		img_icon.setVisibility(View.VISIBLE);
		img_icon.setSelected(true);
	}

	public RoundedImageView getPreferredPlaceItemIconImageView() {
		return img_icon;
	}

	public void setPreferredPlaceItemTitleTextView(TextView textView) {
		lbl_title = textView;
	}

	public TextView getPreferredPlaceItemTitleTextView() {
		return lbl_title;
	}

}

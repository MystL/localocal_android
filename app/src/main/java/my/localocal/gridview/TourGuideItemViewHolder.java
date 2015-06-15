package my.localocal.gridview;

import my.localocal.circularimageview.RoundedImageView;
import android.view.View;
import android.widget.TextView;

public class TourGuideItemViewHolder{

	private RoundedImageView	img_item;
	private TextView	     lbl_title;

	public TourGuideItemViewHolder() {
		//...
	}

	public void setGridViewItemImageView(RoundedImageView imageView) {
		img_item = imageView;
		img_item.setVisibility(View.VISIBLE);
		img_item.setSelected(true);
	}

	public RoundedImageView getGridViewItemImageView() {
		return img_item;
	}

	public void setGridViewItemTitleTextView(TextView textView) {
		lbl_title = textView;
		lbl_title.setVisibility(View.VISIBLE);
		lbl_title.setSelected(true);
	}

	public TextView getGridViewItemTitleTextView() {
		return lbl_title;
	}

}

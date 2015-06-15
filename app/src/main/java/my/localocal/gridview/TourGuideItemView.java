package my.localocal.gridview;

import my.localocal.R;
import my.localocal.circularimageview.RoundedImageView;
import my.localocal.data.TourGuide;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.androidquery.AQuery;

public class TourGuideItemView implements GridViewItem{

	private final Context	        context;
	private final TourGuide	        tourGuide;
	private TourGuideItemViewHolder	viewHolder;
	private final boolean	        isItemSelected	= false;

	public TourGuideItemView(Context context, TourGuide tourGuide) {
		this.context = context;
		this.tourGuide = tourGuide;
	}

	@Override
	public View createView() {
		View view = LayoutInflater.from(context).inflate(R.layout.gridview_item_layout, null);

		TourGuideItemViewHolder holder = new TourGuideItemViewHolder();

		holder.setGridViewItemImageView((RoundedImageView) view.findViewById(R.id.gridview__img_bg));
		holder.setGridViewItemTitleTextView((TextView) view.findViewById(R.id.gridview__lbl_title));

		setValueToItemViewHolder(holder);
		view.setTag(holder);
		return view;
	}

	private void setValueToItemViewHolder(TourGuideItemViewHolder holder) {

		AQuery aQuery = new AQuery(holder.getGridViewItemImageView());
		aQuery.image(tourGuide.getTourGuideProfileImageUrl());
		holder.getGridViewItemTitleTextView().setText(tourGuide.getTourGuideFirstName());

	}

	@Override
	public void convertView(View view) {
		TourGuideItemViewHolder holder = (TourGuideItemViewHolder) view.getTag();
		setValueToItemViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof TourGuideItemViewHolder;
	}

	public TourGuide getTourGuide() {
		return tourGuide;
	}

}

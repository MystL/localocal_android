package my.localocal.views.places;

import my.localocal.MainActivity;
import my.localocal.R;
import my.localocal.booking.TourGuidesViewingActivity;
import my.localocal.data.Place;
import my.localocal.views.listview.ListViewItem;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PlaceViewTourGuideView implements ListViewItem{

	private final Context	context;
	private final Place	  place;

	public PlaceViewTourGuideView(Context context, Place place) {
		this.context = context;
		this.place = place;
	}

	@Override
	public View createView() {
		View view = LayoutInflater.from(context).inflate(R.layout.place_list_view_tourguide, null);

		PlaceViewTourGuideViewHolder holder = new PlaceViewTourGuideViewHolder();

		holder.setPlaceViewTourGuideViewTextView((TextView) view.findViewById(R.id.view_tourguide__lbl_text));

		setValueToViewHolder(holder);
		view.setTag(holder);

		return view;
	}

	private void setValueToViewHolder(PlaceViewTourGuideViewHolder holder) {
		holder.getPlaceViewTourGuideViewTextView().setText("VIEW TOUR GUIDES");
		holder.getPlaceViewTourGuideViewTextView().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent viewTourGuideActivity = new Intent(context, TourGuidesViewingActivity.class);
				viewTourGuideActivity.putExtra(MainActivity.VIEW_TOUR_GUIDE_BUNDLE_PLACE, place.toString());
				context.startActivity(viewTourGuideActivity);
			}
		});
	}

	@Override
	public void convertView(View view) {
		PlaceViewTourGuideViewHolder holder = (PlaceViewTourGuideViewHolder) view.getTag();
		setValueToViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof PlaceViewTourGuideViewHolder;
	}

}

package my.localocal.views.places;

import my.localocal.R;
import my.localocal.data.Place;
import my.localocal.views.listview.ListViewItem;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class PlaceVisitInfoView implements ListViewItem{

	private final Context	context;
	private final Place	  place;
	private Typeface	  roboto_typeface;

	public PlaceVisitInfoView(Context context, Place place) {
		this.context = context;
		this.place = place;

		try {
			roboto_typeface = Typeface.createFromAsset(context.getAssets(), "RobotoThin.ttf");
		} catch (Throwable e) {
			//...
		}
	}

	@Override
	public View createView() {
		View view = LayoutInflater.from(context).inflate(R.layout.place_list_visitinfo, null);
		PlaceVisitInfoViewHolder holder = new PlaceVisitInfoViewHolder();

		holder.setPlaceVisitInfoTitleTextView((TextView) view.findViewById(R.id.visit_info__lbl_title));
		holder.setPlaceVisitInfoOpHoursTitleTextView((TextView) view.findViewById(R.id.visit_info__lbl_title_operatinghours));
		holder.setPlaceVisitInfoOpHoursTextView((TextView) view.findViewById(R.id.visit_info__lbl_operatinghours));
		holder.setPlaceVisitInfoEntFeesTitleTextView((TextView) view.findViewById(R.id.visit_info__lbl_title_entrancefee));
		holder.setPlaceVisitInfoEntFeesTextView((TextView) view.findViewById(R.id.visit_info__lbl_entrancefee));

		setValueToItemViewHolder(holder);
		view.setTag(holder);
		return view;
	}

	private void setValueToItemViewHolder(PlaceVisitInfoViewHolder holder) {

		holder.getPlaceVisitInfoTitleTextView().setText("Visit Information");

		holder.getPlaceVisitInfoOpHoursTitleTextView().setText("Operating Hours : ");
		holder.getPlaceVisitInfoEntFeesTitleTextView().setText("Entrance Fee : ");

		holder.getPlaceVisitInfoOpHoursTextView().setText(TextUtils.join("\n", place.getVisitInfo().getOperatingHours()));
		holder.getPlaceVisitInfoOpHoursTextView().setTypeface(roboto_typeface);

		holder.getPlaceVisitInfoEntFeesTextView().setText(TextUtils.join("\n", place.getVisitInfo().getAdmissionFee()));
		holder.getPlaceVisitInfoEntFeesTextView().setTypeface(roboto_typeface);
	}

	@Override
	public void convertView(View view) {
		PlaceVisitInfoViewHolder holder = (PlaceVisitInfoViewHolder) view.getTag();
		setValueToItemViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof PlaceVisitInfoViewHolder;
	}

}

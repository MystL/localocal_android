package my.localocal.views.places;

import my.localocal.R;
import my.localocal.data.Place;
import my.localocal.views.listview.ListViewItem;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class PlaceDescriptionItemView implements ListViewItem{

	private final Context	context;
	private final Place	  place;
	private Typeface	  roboto_typeface;

	public PlaceDescriptionItemView(Context context, Place place) {
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

		View view = LayoutInflater.from(context).inflate(R.layout.place_description_item, null);

		PlaceDescriptionItemViewHolder holder = new PlaceDescriptionItemViewHolder();

		holder.setDescriptionTitleTextView((TextView) view.findViewById(R.id.place_description__lbl_title));
		holder.setDescriptionTextView((TextView) view.findViewById(R.id.place_description__lbl_desc));
		setValueToItemViewHolder(holder);
		view.setTag(view);
		return view;
	}

	private void setValueToItemViewHolder(PlaceDescriptionItemViewHolder holder) {

		holder.getDesctiptionTitleTextView().setText("Something about the place...");
		//holder.getDesctiptionTitleTextView().setTypeface(roboto_typeface);

		holder.getDescriptionTextView().setText(place.getPlaceDesc());
		holder.getDescriptionTextView().setTypeface(roboto_typeface);
	}

	@Override
	public void convertView(View view) {
		PlaceDescriptionItemViewHolder holder = (PlaceDescriptionItemViewHolder) view.getTag();
		setValueToItemViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof PlaceDescriptionItemViewHolder;
	}

}

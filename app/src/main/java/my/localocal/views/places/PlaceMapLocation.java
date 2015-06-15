package my.localocal.views.places;

import my.localocal.R;
import my.localocal.data.GeoLocation;
import my.localocal.data.Place;
import my.localocal.views.listview.ListViewItem;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.androidquery.AQuery;

public class PlaceMapLocation implements ListViewItem{

	private final FragmentActivity	activity;
	private final Place	           place;

	public PlaceMapLocation(FragmentActivity activity, Place place) {
		this.activity = activity;
		this.place = place;
	}

	@Override
	public View createView() {

		View rootView = LayoutInflater.from(activity).inflate(R.layout.place_list_map, null);

		PlaceMapLocationViewHolder holder = new PlaceMapLocationViewHolder();
		holder.setMapLocationImageView((ImageView) rootView.findViewById(R.id.place_list_map__img_map));

		setValueToViewHolder(holder);
		rootView.setTag(holder);
		return rootView;
	}

	private void setValueToViewHolder(PlaceMapLocationViewHolder holder) {

		AQuery aQuery = new AQuery(holder.getMapLocationImageView());
		String mapImageURL = place.getLocation().getImageMapUrl();
		aQuery.image(mapImageURL);
		holder.getMapLocationImageView().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				GeoLocation location = place.getLocation();
				String label = place.getPlaceName();
				String uriBegin = "geo:" + location.getLatitude() + "," + location.getLongitude();
				String query = location.getLatitude() + "," + location.getLongitude() + "(" + label + ")";
				String encodedQuery = Uri.encode(query);
				String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
				Uri uri = Uri.parse(uriString);
				Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
				activity.startActivity(intent);
			}
		});
	}

	@Override
	public void convertView(View view) {
		PlaceMapLocationViewHolder holder = (PlaceMapLocationViewHolder) view.getTag();
		setValueToViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof PlaceMapLocationViewHolder;
	}

}

package my.localocal.views.places;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import my.localocal.DataManager;
import my.localocal.MainActivity;
import my.localocal.R;
import my.localocal.circularimageview.RoundedImageView;
import my.localocal.data.Place;
import my.localocal.data.PreferredOption;
import my.localocal.theme.ThemeHandler;
import my.localocal.utils.DataUtils;
import my.localocal.views.listview.ListViewItem;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class PlaceTopInfoView implements ListViewItem,LocationListener{

	private final Context	            context;
	private final Place	                place;
	private Typeface	                roboto_typeface;
	private final Set<String>	        favouritePlaceIds	= new HashSet<String>();
	private final List<PreferredOption>	placeTags	      = new ArrayList<PreferredOption>();
	private final ThemeHandler	        themeHandler;
	private final LocationManager	    locationManager;
	private Location	                placeLocation	  = null;
	private Location	                currentLocation	  = null;
	private TextView	                lbl_distance;
	private boolean	                    toShare	          = false;

	public PlaceTopInfoView(Context context, Place place) {
		this.context = context;
		this.place = place;
		themeHandler = new ThemeHandler(context);
		try {
			roboto_typeface = Typeface.createFromAsset(context.getAssets(), "RobotoThin.ttf");
		} catch (Throwable e) {
			//...
		}

		for (String tagsId : place.getPlaceTags()) {
			PreferredOption tag = DataManager.shared(context).getOptionById(tagsId);
			if (tag != null) {
				placeTags.add(tag);
			}
		}

		if (place.getLocation().getLatitude() != 0 && place.getLocation().getLongitude() != 0) {
			placeLocation = new Location(place.getPlaceName());
			placeLocation.setLatitude(place.getLocation().getLatitude());
			placeLocation.setLongitude(place.getLocation().getLongitude());
			placeLocation.setAltitude(0);
		}

		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
	}

	@Override
	public View createView() {
		View view = LayoutInflater.from(context).inflate(R.layout.place_list_top_info, null);

		PlaceTopInfoViewHolder holder = new PlaceTopInfoViewHolder();

		holder.setPlaceTopInfoNameTextView((TextView) view.findViewById(R.id.top_info__lbl_name));
		holder.setPlaceTopInfoShareImageButton((ImageButton) view.findViewById(R.id.top_info__btn_share));
		holder.setPlaceTopInfoFavImageButton((ImageButton) view.findViewById(R.id.top_info__btn_fav));
		holder.setPlaceTopInfoDistanceTextView((TextView) view.findViewById(R.id.top_info__lbl_distance));
		holder.setPlaceTopInfoDistanceAwayTextView((TextView) view.findViewById(R.id.top_info__lbl_km));
		holder.setPlaceTopInfoTagsLayout(view.findViewById(R.id.top_info__tags));

		setValueToItemViewHolder(holder);
		view.setTag(holder);
		return view;
	}

	private void setValueToItemViewHolder(PlaceTopInfoViewHolder holder) {

		holder.getPlaceTopInfoNameTextView().setText(place.getPlaceName());

		LinearLayout lyt_tags = (LinearLayout) holder.getPlaceTopInfoTagsLayout();
		for (PreferredOption option : placeTags) {
			if (!(lyt_tags.getChildCount() > 1)) { // temp solution to make it dun add again during convert view
				RoundedImageView img_tag = new RoundedImageView(context);
				img_tag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				img_tag.setImageDrawable(themeHandler.getImageDrawable(option.getOptionThumbnail() + "_small"));
				lyt_tags.addView(img_tag);
			}
		}

		lbl_distance = holder.getPlaceTopInfoDistanceTextView();
		if (placeLocation != null) {
			if (currentLocation != null) {
				float distanceInMeter = currentLocation.distanceTo(placeLocation);
				if (distanceInMeter > 0) {
					float distanceInKM = distanceInMeter / 1000;
					DecimalFormat df = new DecimalFormat("0");
					String formate = df.format(distanceInKM);
					int finalValue = Integer.parseInt(formate);
					holder.getPlaceTopInfoDistanceTextView().setText(DataUtils.getStringValue(finalValue));
				} else {
					holder.getPlaceTopInfoDistanceTextView().setText("--");
				}
			} else {
				holder.getPlaceTopInfoDistanceTextView().setText("--");
			}
		} else {
			holder.getPlaceTopInfoDistanceTextView().setText("--");
		}
		holder.getPlaceTopInfoDistanceTextView().setTypeface(roboto_typeface);

		holder.getPlaceTopInfoDistanceAwayTextView().setText("KM away");

		holder.getPlaceTopInfoShareImageButton().setImageResource(R.drawable.ic_action_share);
		holder.getPlaceTopInfoShareImageButton().setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {

				toShare = true;
				if (Session.getActiveSession().isOpened()) {
					if (Session.getActiveSession().getPermissions().contains(MainActivity.FACEBOOK_PUBLISH_STREAM_PERMISSION_KEY)) {
						shareItemViaFacebook();
					} else {
						Session.openActiveSession((FragmentActivity) context, true, new Session.StatusCallback(){
							@Override
							public void call(Session session, SessionState state, Exception exception) {
								// TODO Auto-generated method stub

							}

						});
					}
				} else {
					Session.openActiveSession((FragmentActivity) context, true, new Session.StatusCallback(){
						@Override
						public void call(Session session, SessionState state, Exception exception) {
							// TODO Auto-generated method stub

						}
					});
				}

				//				final String appPackageName = context.getPackageName();
				//				try {
				//					context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				//				} catch (android.content.ActivityNotFoundException anfe) {
				//					context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
				//				}
			}
		});

		updateFavButtonImage(holder.getPlaceTopInfoFavImageButton());

	}

	public boolean isToShare() {
		return toShare;
	}

	public void shareItemViaFacebook() {

		Bundle params = new Bundle();
		params.putString("name", "LocaLocal: Travel Malaysia");
		params.putString("caption", "Book A Tour Now!");
		params.putString("description", "[" + place.getPlaceName() + "] is available for booking! Get the app here http://goo.gl/fYCWLS");
		params.putString("link", "http://goo.gl/fYCWLS");
		params.putString("picture", "http://oi59.tinypic.com/35jz3if.jpg");

		WebDialog sharingDialog = new WebDialog.FeedDialogBuilder(context, Session.getActiveSession(), params).setOnCompleteListener(new OnCompleteListener(){

			@Override
			public void onComplete(Bundle values, FacebookException error) {
				if (error == null) {
					final String postId = values.getString("post_id");
					if (postId != null) {
						Toast.makeText(context.getApplicationContext(), "Thanks for sharing :D", Toast.LENGTH_SHORT).show();
					} else {
						// User clicked the Cancel button
						Toast.makeText(context.getApplicationContext(), "Publish cancelled", Toast.LENGTH_SHORT).show();
					}

				} else if (error instanceof FacebookOperationCanceledException) {
					Toast.makeText(context.getApplicationContext(), "Sharing cancelled", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context.getApplicationContext(), "Error sharing to Facebook, please try again later", Toast.LENGTH_SHORT).show();
				}
			}
		}).build();
		sharingDialog.show();
		toShare = false;
	}

	private void updateFavButtonImage(final ImageView btn_fav) {

		if (readFavFromPrefs() != null) {
			if (!readFavFromPrefs().contains(place.getPlaceId())) {
				btn_fav.setImageResource(R.drawable.ic_action_favorite);
			} else {
				btn_fav.setImageResource(R.drawable.ic_action_favorite_h);
			}
		} else {
			btn_fav.setImageResource(R.drawable.ic_action_favorite);
		}

		btn_fav.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {

				if (readFavFromPrefs() != null) {
					favouritePlaceIds.addAll(readFavFromPrefs());
				}

				if (!favouritePlaceIds.contains(place.getPlaceId())) {
					favouritePlaceIds.add(place.getPlaceId());
				} else {
					favouritePlaceIds.remove(place.getPlaceId());
				}

				storeToPrefs(MainActivity.PREF_FAVOURITES_PLACE_KEY, favouritePlaceIds);
				Toast.makeText(context, "Thanks! This place is now added to your favourites!", Toast.LENGTH_SHORT).show();
				updateFavButtonImage(btn_fav);
			}
		});
	}

	@Override
	public void convertView(View view) {
		PlaceTopInfoViewHolder holder = (PlaceTopInfoViewHolder) view.getTag();
		setValueToItemViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof PlaceTopInfoViewHolder;
	}

	@SuppressWarnings("unchecked")
	private void storeToPrefs(String key, Object value) {

		SharedPreferences sharedPrefs = context.getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = sharedPrefs.edit();

		if (value instanceof String) {
			prefsEditor.putString(key, value.toString());
		} else if (value instanceof Boolean) {
			prefsEditor.putBoolean(key, ((Boolean) value).booleanValue());
		} else if (value instanceof HashSet) {
			HashSet<String> valueList = (HashSet<String>) value;
			prefsEditor.putStringSet(key, valueList);
		}

		boolean isCommited = prefsEditor.commit();
		Log.i("XXX", DataUtils.getStringValue(sharedPrefs.getAll()));
		if (!isCommited) {
			throw new RuntimeException("Unable to save into SharedPreferences.");
		}

	}

	private Set<String> readFavFromPrefs() {
		SharedPreferences sharedPrefs = context.getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		//Log.i("XXX", DataUtils.getStringValue(sharedPrefs.getAll()));
		return sharedPrefs.getStringSet(MainActivity.PREF_FAVOURITES_PLACE_KEY, null);
	}

	@Override
	public void onLocationChanged(Location currentLoc) {
		if (currentLocation == null) {
			currentLocation = currentLoc;
			if (lbl_distance != null) {
				if (placeLocation != null) {
					float distanceInMeter = currentLoc.distanceTo(placeLocation);
					if (distanceInMeter > 0) {
						float distanceInKM = distanceInMeter / 1000;
						DecimalFormat df = new DecimalFormat("0.0");
						String formate = df.format(distanceInKM);
						double finalValue = Double.parseDouble(formate);
						lbl_distance.setText(DataUtils.getStringValue(finalValue));
					} else {
						lbl_distance.setText("--");
					}
				} else {
					lbl_distance.setText("--");
				}
			}
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) { /* ignore */}

	@Override
	public void onProviderEnabled(String provider) { /* ignore */}

	@Override
	public void onProviderDisabled(String provider) { /* ignore */}

}

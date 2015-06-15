package my.localocal.booking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import my.localocal.MainActivity;
import my.localocal.R;
import my.localocal.data.BookingItem;
import my.localocal.data.CoveragePlace;
import my.localocal.data.FacebookUser;
import my.localocal.data.Place;
import my.localocal.data.TourGuide;
import my.localocal.json.JSONUtils;
import my.localocal.utils.DataUtils;
import my.localocal.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.PendingCall;

public class TourReviewBookingActivity extends FragmentActivity{

	private ActionBar	      actionBar;
	private Place	          place	                   = null;
	private TourGuide	      tourGuide	               = null;
	private ImageView	      bg_img;
	private TextView	      lbl_durationTitle, lbl_duration, lbl_placeTitle, lbl_place, lbl_dateTitle, lbl_date, lbl_tourguideTitle, lbl_tourGuide, lbl_chargesTitle, lbl_charges;
	private Button	          btn_bookTour;
	private Calendar	      bookingDate;
	private UiLifecycleHelper	fbUiLifecycleHelper;
	List<String>	          storedBookingsPlaceId	   = new ArrayList<String>();
	List<String>	          storedBookingsStringDate	= new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

		setContentView(R.layout.tour_guide_booking_review);

		actionBar = getActionBar();
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Booking Review");

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		storedBookingsPlaceId.clear();
		storedBookingsStringDate.clear();

		bg_img = (ImageView) findViewById(R.id.booking_review__img_bg);
		lbl_durationTitle = (TextView) findViewById(R.id.booking_review__lbl_duration_title);
		lbl_duration = (TextView) findViewById(R.id.booking_review__lbl_duration);
		lbl_placeTitle = (TextView) findViewById(R.id.booking_review__lbl_place_title);
		lbl_place = (TextView) findViewById(R.id.booking_review__lbl_place);
		lbl_dateTitle = (TextView) findViewById(R.id.booking_review__lbl_date_title);
		lbl_date = (TextView) findViewById(R.id.booking_review__lbl_date);
		lbl_tourguideTitle = (TextView) findViewById(R.id.booking_review__lbl_tourguide_title);
		lbl_tourGuide = (TextView) findViewById(R.id.booking_review__lbl_tourguide);
		lbl_chargesTitle = (TextView) findViewById(R.id.booking_review__lbl_price_title);
		lbl_charges = (TextView) findViewById(R.id.booking_review__lbl_price);
		btn_bookTour = (Button) findViewById(R.id.booking_review__btn_booktour);

		lbl_placeTitle.setText("Place : ");
		lbl_dateTitle.setText("Tour Date : ");
		lbl_durationTitle.setText("Tour Duration : ");
		lbl_tourguideTitle.setText("Tour Guide : ");
		lbl_chargesTitle.setText("Charges : ");

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			try {
				JSONObject json_place = new JSONObject(extras.getString(MainActivity.VIEW_TOUR_GUIDE_BUNDLE_PLACE));
				place = new Place(json_place);

				if (!StringUtils.IsNullOrEmpty(place.getPlaceImages().get(0))) {
					String blurred_bg_img_name = getFilesDir() + place.getPlaceImages().get(0) + ".jpg";
					if (!StringUtils.IsNullOrEmpty(blurred_bg_img_name)) {
						Bitmap blurred_bitmap = BitmapFactory.decodeFile(blurred_bg_img_name);
						bg_img.setImageBitmap(blurred_bitmap);
					}
				}

				lbl_place.setText(place.getPlaceName());

				JSONObject json_tourguide = new JSONObject(extras.getString(MainActivity.TOUR_GUIDE_BOOKING_BUNDLE_TOURGUIDE));
				tourGuide = new TourGuide(json_tourguide);

				lbl_tourGuide.setText(tourGuide.getTourGuideFirstName() + ", " + tourGuide.getTourGuideLastName());

				for (CoveragePlace coverage : tourGuide.getTourGuideCoveragePlaces()) {
					if (coverage.getCoveragePlaceId().equalsIgnoreCase(place.getPlaceId())) {
						lbl_charges.setText("USD" + DataUtils.getStringValue(coverage.getCharges()));
						lbl_duration.setText("up to " + DataUtils.getStringValue(coverage.getDuration()) + " hours");
					}
				}

				bookingDate = Calendar.getInstance();
				int year = extras.getInt(MainActivity.TOUR_GUIDE_BOOKING_TOUR_YEAR);
				int month = extras.getInt(MainActivity.TOUR_GUIDE_BOOKING_TOUR_MONTH);
				int date = extras.getInt(MainActivity.TOUR_GUIDE_BOOKING_TOUR_DATE);
				bookingDate.set(Calendar.YEAR, year);
				bookingDate.set(Calendar.MONTH, month);
				bookingDate.set(Calendar.DATE, date);
				lbl_date.setText(year + "/" + (month + 1) + "/" + date);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		btn_bookTour.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {

				if (Session.getActiveSession() != null) {
					if (Session.getActiveSession().isOpened()) {
						saveBooking();
					} else {
						AlertDialog.Builder ad = new AlertDialog.Builder(TourReviewBookingActivity.this);
						ad.setMessage("In order to proceed, you would need to log in to Facebook. Do you wish to continue?");
						ad.setNegativeButton("No", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});

						ad.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								Session.openActiveSession(TourReviewBookingActivity.this, true, new StatusCallback(){
									@SuppressWarnings("deprecation")
									@Override
									public void call(Session session, SessionState state, Exception exception) {
										if (session.isOpened()) {
											saveBooking();
											Toast.makeText(TourReviewBookingActivity.this, "Logged into Facebook", Toast.LENGTH_SHORT).show();
											Request.executeMeRequestAsync(session, new GraphUserCallback(){
												@Override
												public void onCompleted(GraphUser user, Response response) {
													if (user != null) {
														FacebookUser facebookUser = new FacebookUser(user.getInnerJSONObject());
														if (getFacebookUserProfile() == null) {
															storeToPrefs(MainActivity.PREF_FACEBOOK_USER_KEY, facebookUser.toString());
														}
													}
												}
											});
										}

										if (exception != null) {
											Toast.makeText(TourReviewBookingActivity.this, "Sorry, but we are unable to log you into Facebook for now. Please try again later.", Toast.LENGTH_SHORT)
											    .show();
										}

									}
								});
							}
						});

						ad.show();
					}
				}

			}
		});

		fbUiLifecycleHelper = new UiLifecycleHelper(this, new Session.StatusCallback(){
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				// TODO Auto-generated method stub

			}
		});
		fbUiLifecycleHelper.onCreate(savedInstanceState);

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent upIntent = new Intent(this, TourGuideBookingActivity.class);
				upIntent.putExtra(MainActivity.VIEW_TOUR_GUIDE_BUNDLE_PLACE, place.toString());
				upIntent.putExtra(MainActivity.TOUR_GUIDE_BOOKING_BUNDLE_TOURGUIDE, tourGuide.toString());
				if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
					TaskStackBuilder.from(this).addNextIntent(upIntent).startActivities();
					finish();
				} else {
					NavUtils.navigateUpTo(this, upIntent);
				}
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		super.onResume();
		fbUiLifecycleHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		fbUiLifecycleHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		fbUiLifecycleHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		fbUiLifecycleHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

		fbUiLifecycleHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback(){
			@Override
			public void onComplete(PendingCall pendingCall, Bundle data) {
				Log.i("Activity", "Success!");
			}

			@Override
			public void onError(PendingCall pendingCall, Exception error, Bundle data) {
				Log.e("Activity", String.format("Error: %s", error.toString()));
			}

		});
	}

	private void saveBooking() {
		JSONObject json = new JSONObject();
		Calendar cal = Calendar.getInstance();
		try {
			json.put("placeId", JSONUtils.toJSON(place.getPlaceId()));
			json.put("tourGuideId", JSONUtils.toJSON(tourGuide.getTourGuideId()));
			json.put("tour_year", JSONUtils.toJSON(bookingDate.get(Calendar.YEAR)));
			json.put("tour_month", JSONUtils.toJSON(bookingDate.get(Calendar.MONTH)));
			json.put("tour_date", JSONUtils.toJSON(bookingDate.get(Calendar.DATE)));
			json.put("book_year", JSONUtils.toJSON(cal.get(Calendar.YEAR)));
			json.put("book_month", JSONUtils.toJSON(cal.get(Calendar.MONTH)));
			json.put("book_date", JSONUtils.toJSON(cal.get(Calendar.DATE)));
			json.put("status", JSONUtils.toJSON("pending"));
		} catch (JSONException e) {
			// ...
		}
		BookingItem newBookingItem = new BookingItem(json);
		String newBookingStringDate = newBookingItem.getTourDate().get(Calendar.YEAR) + "/" + newBookingItem.getTourDate().get(Calendar.MONTH) + "/" + newBookingItem.getTourDate().get(Calendar.DATE);
		String storedBooking = readFromPrefs(MainActivity.PREF_TOUR_BOOKINGS_KEY);
		JSONArray storedBookingJSONArray = null;

		if (!StringUtils.IsNullOrEmpty(storedBooking)) { // there are bookings earlier, need to check
			//Log.i("XXX", DataUtils.getStringValue(storedBooking));
			try {
				storedBookingJSONArray = new JSONArray(storedBooking);
				if (storedBookingJSONArray != null) {

					for (int i = 0; i < storedBookingJSONArray.length(); i++) {
						BookingItem item = new BookingItem(storedBookingJSONArray.optJSONObject(i));
						storedBookingsPlaceId.add(item.getPlaceId());
						String storedBookingStringDate = item.getTourDate().get(Calendar.YEAR) + "/" + item.getTourDate().get(Calendar.MONTH) + "/" + item.getTourDate().get(Calendar.DATE);
						storedBookingsStringDate.add(storedBookingStringDate);
					}

					if (!storedBookingsStringDate.contains(newBookingStringDate) && !storedBookingsPlaceId.contains(newBookingItem.getPlaceId())) { // date and place not the same, can book
						storedBookingJSONArray.put(newBookingItem.toJSON());
						storeBookingsAndReturn(storedBookingJSONArray);
					} else if (!storedBookingsStringDate.contains(newBookingStringDate) && storedBookingsPlaceId.contains(newBookingItem.getPlaceId())) { // date different, same place, can book
						storedBookingJSONArray.put(newBookingItem.toJSON());
						storeBookingsAndReturn(storedBookingJSONArray);
					} else if (storedBookingsStringDate.contains(newBookingStringDate) && !storedBookingsPlaceId.contains(newBookingItem.getPlaceId())) { // date same, place different, can book
						storedBookingJSONArray.put(newBookingItem.toJSON());
						storeBookingsAndReturn(storedBookingJSONArray);
					} else if (storedBookingsStringDate.contains(newBookingStringDate) && storedBookingsPlaceId.contains(newBookingItem.getPlaceId())) { // date same, place same, cannot book
						Toast.makeText(getApplicationContext(), "It seems that you had a same booking earlier... o.o", Toast.LENGTH_SHORT).show();
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else { // no previous booking at all, create new JSONArray and store it in
			storedBookingJSONArray = new JSONArray();
			storedBookingJSONArray.put(newBookingItem.toJSON());
			storeBookingsAndReturn(storedBookingJSONArray);
		}
	}

	private void storeBookingsAndReturn(JSONArray json_array) {
		storeToPrefs(MainActivity.PREF_TOUR_BOOKINGS_KEY, json_array.toString());
		Toast.makeText(getApplicationContext(), "Thank you for booking, our tour guide will be contacting you for tour confirmation", Toast.LENGTH_LONG).show();
		backToHomeScreen();
	}

	private void backToHomeScreen() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(intent);
		finish();
	}

	@SuppressWarnings("unchecked")
	private void storeToPrefs(String key, Object value) {

		SharedPreferences sharedPrefs = getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
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
		//Log.i("XXX", DataUtils.getStringValue(sharedPrefs.getAll()));
		if (!isCommited) {
			throw new RuntimeException("Unable to save into SharedPreferences.");
		}

	}

	private String readFromPrefs(String key) {
		SharedPreferences sharedPrefs = getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		return sharedPrefs.getString(key, null);
	}

	private String getFacebookUserProfile() {
		SharedPreferences sharedPrefs = getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		return sharedPrefs.getString(MainActivity.PREF_FACEBOOK_USER_KEY, null);
	}

}

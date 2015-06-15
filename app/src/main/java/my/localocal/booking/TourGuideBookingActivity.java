package my.localocal.booking;

import java.util.Calendar;

import my.localocal.MainActivity;
import my.localocal.R;
import my.localocal.data.CoveragePlace;
import my.localocal.data.Place;
import my.localocal.data.TourGuide;
import my.localocal.utils.DataUtils;
import my.localocal.utils.StringUtils;
import my.localocal.views.DatePickerFragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.mikhaellopez.circularimageview.CircularImageView;

public class TourGuideBookingActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener{

	private Place	          place	    = null;
	private TourGuide	      tourGuide	= null;
	private ImageView	      bg_img;
	private CircularImageView	img_tourGuide;
	private TextView	      lbl_place, lbl_tourGuideName, lbl_chargesTitle, lbl_charges, lbl_durationTitle, lbl_duration, lbl_languageTitle, lbl_language, lbl_itineraryTitle, lbl_itinerary,
	        lbl_includedTitle, lbl_included, lbl_excludedTitle, lbl_excluded, lbl_extraNoteTitle, lbl_extraNote, lbl_tourdateTitle, lbl_tourdate;
	private ActionBar	      actionBar;
	private Button	          btn_book, btn_tourdate;
	private Calendar	      bookingDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.tour_guide_booking);

		bookingDate = Calendar.getInstance();

		actionBar = getActionBar();
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Tour Booking");

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		bg_img = (ImageView) findViewById(R.id.tour_guide_booking_img_bg);
		lbl_place = (TextView) findViewById(R.id.tour_guide_booking__lbl_place);
		lbl_tourGuideName = (TextView) findViewById(R.id.tour_guide_booking__lbl_tourguidename);
		lbl_chargesTitle = (TextView) findViewById(R.id.tour_guide_booking__lbl_charges_title);
		lbl_charges = (TextView) findViewById(R.id.tour_guide_booking__lbl_charges);
		lbl_durationTitle = (TextView) findViewById(R.id.tour_guide_booking__lbl_duration_title);
		lbl_duration = (TextView) findViewById(R.id.tour_guide_booking__lbl_duration);
		lbl_languageTitle = (TextView) findViewById(R.id.tour_guide_booking__lbl_language_title);
		lbl_language = (TextView) findViewById(R.id.tour_guide_booking__lbl_language);

		lbl_itineraryTitle = (TextView) findViewById(R.id.tour_guide_booking__lbl_title_itinerary);
		lbl_itinerary = (TextView) findViewById(R.id.tour_guide_booking__lbl_itinerary);
		lbl_includedTitle = (TextView) findViewById(R.id.tour_guide_booking__lbl_title_included);
		lbl_included = (TextView) findViewById(R.id.tour_guide_booking__lbl_included);
		lbl_excludedTitle = (TextView) findViewById(R.id.tour_guide_booking__lbl_title_excluded);
		lbl_excluded = (TextView) findViewById(R.id.tour_guide_booking__lbl_excluded);
		lbl_extraNoteTitle = (TextView) findViewById(R.id.tour_guide_booking__lbl_note_excluded);
		lbl_extraNote = (TextView) findViewById(R.id.tour_guide_booking__lbl_note);
		lbl_tourdateTitle = (TextView) findViewById(R.id.tour_guide_booking__lbl_tourdate_title);
		lbl_tourdate = (TextView) findViewById(R.id.tour_guide_booking__lbl_tourdate);
		btn_tourdate = (Button) findViewById(R.id.tour_guide_booking__btn_date);
		btn_tourdate.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				DatePickerFragment datePickerFragment = new DatePickerFragment();
				Bundle args = new Bundle();
				args.putInt("year", getIntent().getExtras().getInt(MainActivity.TOUR_GUIDE_BOOKING_TOUR_YEAR));
				args.putInt("month", getIntent().getExtras().getInt(MainActivity.TOUR_GUIDE_BOOKING_TOUR_MONTH));
				args.putInt("date", getIntent().getExtras().getInt(MainActivity.TOUR_GUIDE_BOOKING_TOUR_DATE));
				datePickerFragment.setArguments(args);
				datePickerFragment.setOnDateSetListener(TourGuideBookingActivity.this);
				datePickerFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});
		img_tourGuide = (CircularImageView) findViewById(R.id.tour_guide_booking__img_profile);
		img_tourGuide.setBorderColor(getResources().getColor(R.color.LightGrey));
		img_tourGuide.setBorderWidth(10);
		img_tourGuide.addShadow();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			try {
				JSONObject json_place = new JSONObject(extras.getString(MainActivity.VIEW_TOUR_GUIDE_BUNDLE_PLACE));
				place = new Place(json_place);
				lbl_place.setText(place.getPlaceName());
				if (!StringUtils.IsNullOrEmpty(place.getPlaceImages().get(0))) {
					String blurred_bg_img_name = getFilesDir() + place.getPlaceImages().get(0) + ".jpg";
					if (!StringUtils.IsNullOrEmpty(blurred_bg_img_name)) {
						Bitmap blurred_bitmap = BitmapFactory.decodeFile(blurred_bg_img_name);
						bg_img.setImageBitmap(blurred_bitmap);
					}
				}
				JSONObject json_tourguide = new JSONObject(extras.getString(MainActivity.TOUR_GUIDE_BOOKING_BUNDLE_TOURGUIDE));
				tourGuide = new TourGuide(json_tourguide);

				if (!StringUtils.IsNullOrEmpty(tourGuide.getTourGuideProfileImageUrl())) {
					AQuery aQuery = new AQuery(img_tourGuide);
					aQuery.image(tourGuide.getTourGuideProfileImageUrl());
				}

				lbl_tourGuideName.setText(tourGuide.getTourGuideFirstName() + ", " + tourGuide.getTourGuideLastName());
				lbl_chargesTitle.setText("Charges : ");
				lbl_durationTitle.setText("Tour Duration : ");
				lbl_itineraryTitle.setText("Tour Itinerary");
				lbl_includedTitle.setText("Package Includes");
				lbl_excludedTitle.setText("Package Excludes");
				lbl_extraNoteTitle.setText("Additional Note");

				for (CoveragePlace coverage : tourGuide.getTourGuideCoveragePlaces()) {
					if (coverage.getCoveragePlaceId().equalsIgnoreCase(place.getPlaceId())) {
						lbl_charges.setText("USD " + DataUtils.getStringValue(coverage.getCharges()));
						lbl_duration.setText("up to " + DataUtils.getStringValue(coverage.getDuration()) + " hours");

						if (coverage.getTourPlaceItinerary() != null) {
							if (!coverage.getTourPlaceItinerary().isEmpty()) {
								lbl_itinerary.setText(TextUtils.join("\n", coverage.getTourPlaceItinerary()));
							} else {
								lbl_itinerary.setText("No information available for now");
							}
						}

						if (coverage.getTourPlaceIncludedServices() != null) {
							if (!coverage.getTourPlaceIncludedServices().isEmpty()) {
								lbl_included.setText(TextUtils.join("\n", coverage.getTourPlaceIncludedServices()));
							} else {
								lbl_included.setText("No information available for now");
							}
						}

						if (coverage.getTourPlaceExcludedServices() != null) {
							if (!coverage.getTourPlaceExcludedServices().isEmpty()) {
								lbl_excluded.setText(TextUtils.join("\n", coverage.getTourPlaceExcludedServices()));
							} else {
								lbl_excluded.setText("No information available for now");
							}
						}

						if (coverage.getTourPlaceExtraNotes() != null) {
							if (!coverage.getTourPlaceExtraNotes().isEmpty()) {
								lbl_extraNote.setText(TextUtils.join("\n", coverage.getTourPlaceExtraNotes()));
							} else {
								lbl_extraNote.setText("No information available for now");
							}
						}
					}
				}

				lbl_languageTitle.setText("Spoken Language : ");
				lbl_language.setText(TextUtils.join(", ", tourGuide.getTourGuideSpokenLanguage()));

				bookingDate.set(Calendar.YEAR, extras.getInt(MainActivity.TOUR_GUIDE_BOOKING_TOUR_YEAR));
				bookingDate.set(Calendar.MONTH, extras.getInt(MainActivity.TOUR_GUIDE_BOOKING_TOUR_MONTH));
				bookingDate.set(Calendar.DATE, extras.getInt(MainActivity.TOUR_GUIDE_BOOKING_TOUR_DATE));

				lbl_tourdateTitle.setText("Tour Date");
				lbl_tourdate.setText("Date :");
				btn_tourdate.setText(bookingDate.get(Calendar.YEAR) + "/" + (bookingDate.get(Calendar.MONTH) + 1) + "/" + bookingDate.get(Calendar.DATE));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		btn_book = (Button) findViewById(R.id.tour_guide_booking__btn_confirm);
		btn_book.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent reviewBookingIntent = new Intent(getApplicationContext(), TourReviewBookingActivity.class);
				reviewBookingIntent.putExtra(MainActivity.VIEW_TOUR_GUIDE_BUNDLE_PLACE, place.toString());
				reviewBookingIntent.putExtra(MainActivity.TOUR_GUIDE_BOOKING_BUNDLE_TOURGUIDE, tourGuide.toString());
				reviewBookingIntent.putExtra(MainActivity.TOUR_GUIDE_BOOKING_TOUR_YEAR, bookingDate.get(Calendar.YEAR));
				reviewBookingIntent.putExtra(MainActivity.TOUR_GUIDE_BOOKING_TOUR_MONTH, bookingDate.get(Calendar.MONTH));
				reviewBookingIntent.putExtra(MainActivity.TOUR_GUIDE_BOOKING_TOUR_DATE, bookingDate.get(Calendar.DATE));
				startActivity(reviewBookingIntent);
			}
		});

	}

	@Override
	public void onDateSet(DatePicker view, int year, int month, int date) {
		bookingDate.set(Calendar.YEAR, year);
		bookingDate.set(Calendar.MONTH, month);
		bookingDate.set(Calendar.DATE, date);
		btn_tourdate.setText(bookingDate.get(Calendar.YEAR) + "/" + (bookingDate.get(Calendar.MONTH) + 1) + "/" + bookingDate.get(Calendar.DATE));
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent upIntent = new Intent(this, TourGuidesViewingActivity.class);
				upIntent.putExtra(MainActivity.VIEW_TOUR_GUIDE_BUNDLE_PLACE, place.toString());
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

	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

}

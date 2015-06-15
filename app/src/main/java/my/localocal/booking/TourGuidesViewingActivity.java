package my.localocal.booking;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import my.localocal.DataManager;
import my.localocal.MainActivity;
import my.localocal.R;
import my.localocal.data.CoveragePlace;
import my.localocal.data.Place;
import my.localocal.data.TourGuide;
import my.localocal.gridview.GridViewAdapter;
import my.localocal.gridview.GridViewItem;
import my.localocal.gridview.TourGuideItemView;
import my.localocal.theme.ThemeHandler;
import my.localocal.utils.StringUtils;
import my.localocal.views.DatePickerFragment;
import my.localocal.views.DurationPickerFragment;
import my.localocal.views.NumberPickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TourGuidesViewingActivity extends FragmentActivity implements DatePickerDialog.OnDateSetListener,NumberPickerDialog.OnNumberSetListener{

	private Place	                 place;
	private ImageView	             bg_img;
	private TextView	             lbl_dateTitle, lbl_priceTitle, lbl_durationTitle;
	private View	                 filter_layout, results_layout;
	private Button	                 btn_date, btn_price, btn_duration, btn_clearFilter, btn_filter;
	private GridView	             gridView;
	private String	                 searchDate;
	private int	                     searchBudget, searchDuration;
	private final List<TourGuide>	 listOfTourGuides	 = new ArrayList<TourGuide>();
	private final List<TourGuide>	 newListOfTourGuides	= new ArrayList<TourGuide>();
	private final List<GridViewItem>	itemsList	     = new ArrayList<GridViewItem>();
	private GridViewAdapter	         gridViewAdapter;
	final CharSequence[]	         listOfBudget	     = { "Any", "USD 30", "USD 50", "USD 70", "USD 90" };
	private ActionBar	             actionBar;
	private ScrollView	             scrollView;
	private ThemeHandler	         themeHandler;
	private final boolean	         isExpanded	         = false;
	private Calendar	             tourDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
		setContentView(R.layout.tour_guides_selection);

		actionBar = getActionBar();
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Tour Guides Selection");

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		themeHandler = new ThemeHandler(getApplicationContext());

		bg_img = (ImageView) findViewById(R.id.tour_guide_selection__img_bg);
		scrollView = (ScrollView) findViewById(R.id.scrollview);

		filter_layout = findViewById(R.id.tour_guide__filter_selection_layout);
		results_layout = findViewById(R.id.tour_guide__results_layout);

		btn_filter = (Button) findViewById(R.id.tour_guide__filter_btn_expandCollapse);
		btn_filter.setText("Apply Filter");
		btn_filter.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				performFilter();
			}
		});

		btn_date = (Button) findViewById(R.id.tour_guide__filter_btn_date);
		btn_price = (Button) findViewById(R.id.tour_guide__filter_btn_price);
		btn_duration = (Button) findViewById(R.id.tour_guide__filter_btn_duration);
		btn_clearFilter = (Button) findViewById(R.id.tour_guides__btn_clear_filter);
		updateClearFilterButtonView();

		lbl_dateTitle = (TextView) findViewById(R.id.tour_guide__lbl_date_title);
		lbl_priceTitle = (TextView) findViewById(R.id.tour_guide__lbl_budget_title);
		lbl_durationTitle = (TextView) findViewById(R.id.tour_guide__lbl_duration_title);

		gridView = (GridView) findViewById(R.id.tour_guides__gridview);

		btn_date.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				DatePickerFragment datePickerFragment = new DatePickerFragment();
				datePickerFragment.setArguments(null);
				datePickerFragment.setOnDateSetListener(TourGuidesViewingActivity.this);
				datePickerFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});

		btn_price.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(TourGuidesViewingActivity.this);
				builder.setTitle("Max Tour Budget (USD)");
				builder.setItems(listOfBudget, new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						updateBudget(which);
					}
				});

				AlertDialog dialog = builder.create();
				dialog.show();
			}
		});

		btn_duration.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				DialogFragment numerPickerFragment = new DurationPickerFragment();
				numerPickerFragment.show(getSupportFragmentManager(), "numberPicker");
			}
		});

		btn_clearFilter.setText("Clear All Preferences");
		btn_clearFilter.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				searchDate = "";
				updateDate(null);
				updateDuration(0);
				updateBudget(0);
				performFilter();
				btn_clearFilter.setVisibility(View.GONE);
			}
		});

		lbl_dateTitle.setText("Tour Date : ");
		btn_date.setText("Any");
		lbl_priceTitle.setText("Tour Budget : ");
		btn_price.setText("Any");
		lbl_durationTitle.setText("Tour Duration : ");
		btn_duration.setText("Any");

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
				for (int i = 0; i < place.getTourGuideIds().size(); i++) {
					TourGuide tg = DataManager.shared(getApplicationContext()).getTourGuideById(place.getTourGuideIds().get(i));
					if (tg != null) {
						for (CoveragePlace coveragePlace : tg.getTourGuideCoveragePlaces()) {
							if (coveragePlace.getCoveragePlaceId().equalsIgnoreCase(place.getPlaceId())) {
								listOfTourGuides.add(tg);
								itemsList.add(new TourGuideItemView(getApplicationContext(), tg));
							}
						}
					}
				}

				gridViewAdapter = new GridViewAdapter(getApplicationContext(), itemsList);
				gridView.setAdapter(gridViewAdapter);
				gridView.setOnItemClickListener(new OnItemClickListener(){
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						GridViewAdapter m_adapter = (GridViewAdapter) gridView.getAdapter();

						if (m_adapter.getSelectedItem(position) instanceof TourGuideItemView) {
							TourGuideItemView selectedItem = (TourGuideItemView) m_adapter.getSelectedItem(position);
							if (view != null) {
								view.setSelected(false);
							}

							if (selectedItem != null) {
								TourGuide selectedTG = selectedItem.getTourGuide();
								Intent tourGuideBookingIntent = new Intent(getApplicationContext(), TourGuideBookingActivity.class);
								tourGuideBookingIntent.putExtra(MainActivity.VIEW_TOUR_GUIDE_BUNDLE_PLACE, place.toString());
								tourGuideBookingIntent.putExtra(MainActivity.TOUR_GUIDE_BOOKING_BUNDLE_TOURGUIDE, selectedTG.toString());
								if (tourDate == null) {
									tourDate = Calendar.getInstance(); // nothing selected assume is today
								}
								tourGuideBookingIntent.putExtra(MainActivity.TOUR_GUIDE_BOOKING_TOUR_YEAR, tourDate.get(Calendar.YEAR));
								tourGuideBookingIntent.putExtra(MainActivity.TOUR_GUIDE_BOOKING_TOUR_MONTH, tourDate.get(Calendar.MONTH));
								tourGuideBookingIntent.putExtra(MainActivity.TOUR_GUIDE_BOOKING_TOUR_DATE, tourDate.get(Calendar.DATE));
								startActivity(tourGuideBookingIntent);
							}
						}

					}
				});

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		scrollView.post(new Runnable(){
			@Override
			public void run() {
				scrollView.fullScroll(ScrollView.FOCUS_UP);
				updateFilterButtonVisibility();
			}
		});

	}

	public void performFilter() {

		newListOfTourGuides.clear();
		if (searchBudget > 0 && searchDuration == 0) {
			for (TourGuide tg : listOfTourGuides) {
				for (CoveragePlace coveragePlace : tg.getTourGuideCoveragePlaces()) {
					if (coveragePlace.getCoveragePlaceId().equalsIgnoreCase(place.getPlaceId())) {
						if (coveragePlace.getCharges() <= searchBudget) {
							newListOfTourGuides.add(tg);
						}
					}
				}
			}
		} else if (searchBudget == 0 && searchDuration > 0) {
			for (TourGuide tg : listOfTourGuides) {
				for (CoveragePlace coveragePlace : tg.getTourGuideCoveragePlaces()) {
					if (coveragePlace.getCoveragePlaceId().equalsIgnoreCase(place.getPlaceId())) {
						if (coveragePlace.getDuration() <= searchDuration) {
							newListOfTourGuides.add(tg);
						}
					}
				}
			}
		} else if (searchBudget > 0 && searchDuration > 0) {
			for (TourGuide tg : listOfTourGuides) {
				for (CoveragePlace coveragePlace : tg.getTourGuideCoveragePlaces()) {
					if (coveragePlace.getCoveragePlaceId().equalsIgnoreCase(place.getPlaceId())) {
						if (coveragePlace.getDuration() <= searchDuration && coveragePlace.getCharges() <= searchBudget) {
							newListOfTourGuides.add(tg);
						}
					}
				}
			}
		} else if (searchBudget == 0 && searchDuration == 0) {
			for (TourGuide tg : listOfTourGuides) {
				newListOfTourGuides.add(tg);
			}
		}

		itemsList.clear();
		gridView.setAdapter(null);
		for (TourGuide tour_guide : newListOfTourGuides) {
			itemsList.add(new TourGuideItemView(getApplicationContext(), tour_guide));
		}
		gridViewAdapter = new GridViewAdapter(getApplicationContext(), itemsList);
		gridViewAdapter.notifyDataSetChanged();
		gridView.setAdapter(gridViewAdapter);

		if (newListOfTourGuides.isEmpty()) {
			Toast.makeText(getApplicationContext(), "Sorry, we couldn't find any Tour Guide based on your preferences...for now!", Toast.LENGTH_LONG).show();
		}

		updateClearFilterButtonView();
		updateFilterButtonVisibility();
		scrollView.post(new Runnable(){
			@Override
			public void run() {
				scrollView.fullScroll(ScrollView.FOCUS_UP);
			}
		});
	}

	public void updateDate(Calendar cal) {
		if (cal != null) {
			tourDate = cal;
			btn_date.setText(cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.DATE));
		} else {
			tourDate = null;
			btn_date.setText("Any");
		}
		updateFilterButtonVisibility();

	}

	public void updateDuration(int duration) {
		if (duration == 0) {
			btn_duration.setText("Any");
		} else if (duration > 0 && duration < 2) {
			btn_duration.setText(duration + " hour");
		} else {
			btn_duration.setText(duration + " hours");
		}

		searchDuration = duration;
		updateFilterButtonVisibility();
	}

	public void updateBudget(int which) {
		if (which >= 0 && which <= listOfBudget.length) {
			btn_price.setText(listOfBudget[which]);
			switch (which) {
				case 0:
					searchBudget = 0;
				break;
				case 1:
					searchBudget = 30;
				break;
				case 2:
					searchBudget = 50;
				break;
				case 3:
					searchBudget = 70;
				break;
				case 4:
					searchBudget = 90;
				break;
				default:
					searchBudget = 0;
			}
			updateFilterButtonVisibility();
		}
	}

	public void updateClearFilterButtonView() {
		if (searchBudget > 0 || searchDuration > 0 || tourDate != null) {
			btn_clearFilter.setVisibility(View.VISIBLE);
			return;
		} else {
			btn_clearFilter.setVisibility(View.GONE);
			return;
		}
	}

	private void updateFilterButtonVisibility() {
		if (btn_date.getText().toString().equalsIgnoreCase("Any") && btn_duration.getText().toString().equalsIgnoreCase("Any") && btn_price.getText().toString().equalsIgnoreCase("Any")) {
			btn_filter.setVisibility(View.GONE);
			return;
		} else {
			btn_filter.setVisibility(View.VISIBLE);
			return;
		}
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

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, monthOfYear, dayOfMonth);
		updateDate(cal);
	}

	@Override
	public void onNumberSet(NumberPicker view, int num) {
		updateDuration(view.getValue());
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				Intent upIntent = new Intent(this, MainActivity.class);
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
}

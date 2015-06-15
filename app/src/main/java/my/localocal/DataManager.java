package my.localocal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import my.localocal.data.ListOfOptions;
import my.localocal.data.ListOfPlaces;
import my.localocal.data.ListOfTourGuides;
import my.localocal.data.Place;
import my.localocal.data.PreferredOption;
import my.localocal.data.TourGuide;
import my.localocal.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class DataManager{

	private static DataManager	sharedInstance;
	private ListOfPlaces	   listOfPlaces	     = null;
	private ListOfTourGuides	listOfTourGuides	= null;
	private ListOfOptions	   listOfOptions	 = null;

	private DataManager(Context context) {
		sharedInstance = this;

		AssetManager assetManager = context.getAssets();
		InputStream is = null;

		try {
			is = assetManager.open("data.json");
			if (is != null) {
				JSONObject data_json = null;
				try {
					data_json = new JSONObject(convertStreamToString(is));
					if (data_json != null) {
						loadPlace(data_json.optJSONArray("places"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.i("XXX", "Can't read file > data.json");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			is = assetManager.open("tourguides.json");
			if (is != null) {
				JSONObject data_json = null;
				try {
					data_json = new JSONObject(convertStreamToString(is));
					if (data_json != null) {
						loadTourGuides(data_json.optJSONArray("tour_guides"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.i("XXX", "Can't read file > tourguides.json");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			is = assetManager.open("preferredoptions.json");
			if (is != null) {
				JSONObject data_json = null;
				try {
					data_json = new JSONObject(convertStreamToString(is));
					if (data_json != null) {
						loadOptions(data_json.optJSONArray("preferred_options"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.i("XXX", "Can't read file > preferredoptions.json");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static String convertStreamToString(InputStream is) throws IOException {
		Writer writer = new StringWriter();

		char[] buffer = new char[2048];
		try {
			Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
		} finally {
			is.close();
		}
		String text = writer.toString();
		return text;
	}

	public static synchronized DataManager shared(Context c) {
		if (sharedInstance == null) {
			sharedInstance = new DataManager(c.getApplicationContext());
		}
		return sharedInstance;
	}

	private void loadPlace(JSONArray place_json) {
		if (place_json != null) {
			listOfPlaces = new ListOfPlaces(place_json);
		}
	}

	private void loadTourGuides(JSONArray tourGuides_json) {
		if (tourGuides_json != null) {
			listOfTourGuides = new ListOfTourGuides(tourGuides_json);
		}
	}

	private void loadOptions(JSONArray options_json) {
		if (options_json != null) {
			listOfOptions = new ListOfOptions(options_json);
		}
	}

	public ListOfPlaces getAllPlaces() {
		return listOfPlaces;
	}

	public Place getPlaceById(String placeId) {
		if (!StringUtils.IsNullOrEmpty(placeId)) {
			for (Place place : listOfPlaces.getPlaces()) {
				if (place.getPlaceId().equals(placeId)) {
					return place;
				}
			}
		}
		return null;
	}

	public ListOfTourGuides getAllTourGuides() {
		return listOfTourGuides;
	}

	public TourGuide getTourGuideById(String tourGuideId) {
		if (!StringUtils.IsNullOrEmpty(tourGuideId)) {
			for (TourGuide tourguide : listOfTourGuides.getTourGuides()) {
				if (tourguide.getTourGuideId().equalsIgnoreCase(tourGuideId)) {
					return tourguide;
				}
			}
		}
		return null;
	}

	public ListOfOptions getAllOptions() {
		return listOfOptions;
	}

	public PreferredOption getOptionById(String optionId) {
		if (!StringUtils.IsNullOrEmpty(optionId)) {
			for (PreferredOption option : listOfOptions.getOptions()) {
				if (option.getOptionId().equalsIgnoreCase(optionId)) {
					return option;
				}
			}
		}
		return null;
	}

}

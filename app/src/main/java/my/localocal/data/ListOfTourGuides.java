package my.localocal.data;

import java.util.ArrayList;
import java.util.List;

import my.localocal.json.IJSONifyableObject;
import my.localocal.json.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListOfTourGuides implements IJSONifyableObject{
	private final List<TourGuide>	tour_guides	= new ArrayList<TourGuide>();

	public ListOfTourGuides(JSONArray json_array) {
		tour_guides.clear();
		if (json_array != null) {
			for (int i = 0; i < json_array.length(); i++) {
				TourGuide place = new TourGuide(json_array.optJSONObject(i));
				tour_guides.add(place);
			}
		}
	}

	public List<TourGuide> getTourGuides() {
		return tour_guides;
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("tour_guides", JSONUtils.toJSON(tour_guides));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
}

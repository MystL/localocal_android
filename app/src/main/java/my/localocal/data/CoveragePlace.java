package my.localocal.data;

import java.util.ArrayList;
import java.util.List;

import my.localocal.json.IJSONifyableObject;
import my.localocal.json.JSONUtils;
import my.localocal.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class CoveragePlace implements IJSONifyableObject{

	private String	           place_id	          = null;
	private int	               charges	          = 0;
	private int	               duration	          = 0;
	private final List<String>	included_services	= new ArrayList<String>();
	private final List<String>	excluded_services	= new ArrayList<String>();
	private final List<String>	extra_note	      = new ArrayList<String>();
	private final List<String>	tour_itinerary	  = new ArrayList<String>();

	public CoveragePlace(JSONObject json) {
		place_id = !json.isNull("place_id") ? json.optString("place_id") : null;
		charges = json.optInt("charges");
		duration = json.optInt("duration");

		if (json.optJSONArray("included_services") != null) {
			for (int i = 0; i < json.optJSONArray("included_services").length(); i++) {
				if (!StringUtils.IsNullOrEmpty(json.optJSONArray("included_services").optString(i))) {
					included_services.add(json.optJSONArray("included_services").optString(i));
				}
			}
		}

		if (json.optJSONArray("excluded_services") != null) {
			for (int i = 0; i < json.optJSONArray("excluded_services").length(); i++) {
				if (!StringUtils.IsNullOrEmpty(json.optJSONArray("excluded_services").optString(i))) {
					excluded_services.add(json.optJSONArray("excluded_services").optString(i));
				}
			}
		}

		if (json.optJSONArray("tour_itinerary") != null) {
			for (int i = 0; i < json.optJSONArray("tour_itinerary").length(); i++) {
				if (!StringUtils.IsNullOrEmpty(json.optJSONArray("tour_itinerary").optString(i))) {
					tour_itinerary.add(json.optJSONArray("tour_itinerary").optString(i));
				}
			}
		}

		if (json.optJSONArray("extra_note") != null) {
			for (int i = 0; i < json.optJSONArray("extra_note").length(); i++) {
				if (!StringUtils.IsNullOrEmpty(json.optJSONArray("extra_note").optString(i))) {
					extra_note.add(json.optJSONArray("extra_note").optString(i));
				}
			}
		}

	}

	public String getCoveragePlaceId() {
		return place_id;
	}

	public int getCharges() {
		return charges;
	}

	public int getDuration() {
		return duration;
	}

	public List<String> getTourPlaceIncludedServices() {
		return included_services;
	}

	public List<String> getTourPlaceExcludedServices() {
		return excluded_services;
	}

	public List<String> getTourPlaceExtraNotes() {
		return extra_note;
	}

	public List<String> getTourPlaceItinerary() {
		return tour_itinerary;
	}

	public boolean isValid() {
		if (!StringUtils.IsNullOrEmpty(place_id)) { // at least has the id
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("place_id", JSONUtils.toJSON(place_id));
			json.put("charges", JSONUtils.toJSON(charges));
			json.put("duration", JSONUtils.toJSON(duration));
			json.put("included_services", JSONUtils.toJSON(included_services));
			json.put("excluded_services", JSONUtils.toJSON(excluded_services));
			json.put("tour_itinerary", JSONUtils.toJSON(tour_itinerary));
			json.put("extra_note", JSONUtils.toJSON(extra_note));
		} catch (JSONException e) {
			//...
		}
		return json;
	}

}

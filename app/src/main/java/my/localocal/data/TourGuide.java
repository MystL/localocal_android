package my.localocal.data;

import java.util.ArrayList;
import java.util.List;

import my.localocal.json.IJSONifyableObject;
import my.localocal.json.JSONUtils;
import my.localocal.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class TourGuide implements IJSONifyableObject{

	private String	                  id	          = null;
	private String	                  first_name	  = null;
	private String	                  last_name	      = null;
	private String	                  description	  = null;
	private String	                  profile_img	  = null;
	private final List<CoveragePlace>	tour_places	  = new ArrayList<CoveragePlace>();
	private Contact	                  contact	      = null;
	private final List<String>	      spoken_language	= new ArrayList<String>();

	public TourGuide(JSONObject json) {

		id = !json.isNull("id") ? json.optString("id") : null;
		first_name = !json.isNull("first_name") ? json.optString("first_name") : null;
		last_name = !json.isNull("last_name") ? json.optString("last_name") : null;
		description = !json.isNull("description") ? json.optString("description") : null;
		profile_img = !json.isNull("profile_img") ? json.optString("profile_img") : null;

		if (json.optJSONArray("tour_places") != null) {
			for (int i = 0; i < json.optJSONArray("tour_places").length(); i++) {
				CoveragePlace coveragePlace = new CoveragePlace(json.optJSONArray("tour_places").optJSONObject(i));
				if (coveragePlace.isValid()) {
					tour_places.add(coveragePlace);
				}
			}
		}

		if (!json.isNull("contact")) {
			contact = new Contact(json.optJSONObject("contact"));
		}

		if (json.optJSONArray("spoken_language") != null) {
			for (int i = 0; i < json.optJSONArray("spoken_language").length(); i++) {
				if (!StringUtils.IsNullOrEmpty(json.optJSONArray("spoken_language").optString(i))) {
					spoken_language.add(json.optJSONArray("spoken_language").optString(i));
				}
			}
		}
	}

	public String getTourGuideId() {
		return id;
	}

	public String getTourGuideFirstName() {
		return first_name;
	}

	public String getTourGuideLastName() {
		return last_name;
	}

	public String getTourGuideDescription() {
		return description;
	}

	public String getTourGuideProfileImageUrl() {
		return profile_img;
	}

	public List<CoveragePlace> getTourGuideCoveragePlaces() {
		return tour_places;
	}

	public Contact getTourGuideContact() {
		return contact;
	}

	public List<String> getTourGuideSpokenLanguage() {
		return spoken_language;
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("id", JSONUtils.toJSON(id));
			json.put("first_name", JSONUtils.toJSON(first_name));
			json.put("last_name", JSONUtils.toJSON(last_name));
			json.put("description", JSONUtils.toJSON(description));
			json.put("profile_img", JSONUtils.toJSON(profile_img));
			json.put("tour_places", JSONUtils.toJSON(tour_places));
			json.put("contact", JSONUtils.toJSON(contact));
			json.put("spoken_language", JSONUtils.toJSON(spoken_language));
		} catch (JSONException e) {
			//...
		}
		return json;
	}

}

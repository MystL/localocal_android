package my.localocal.data;

import java.util.ArrayList;
import java.util.List;

import my.localocal.json.IJSONifyableObject;
import my.localocal.json.JSONUtils;
import my.localocal.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Place implements IJSONifyableObject{

	private String	           id	        = null;
	private String	           name	        = null;
	private String	           description	= null;
	private final List<String>	tags	    = new ArrayList<String>();
	private final List<String>	image	    = new ArrayList<String>();
	private final List<String>	address	    = new ArrayList<String>();
	private final List<String>	tour_guides	= new ArrayList<String>();
	private Contact	           contact	    = null;
	private GeoLocation	       geo_location	= null;
	private VisitInfo	       visit_info	= null;

	public Place(JSONObject json) {

		id = !json.isNull("id") ? json.optString("id") : null;
		name = !json.isNull("name") ? json.optString("name") : null;
		description = !json.isNull("description") ? json.optString("description") : null;

		if (json.optJSONArray("image") != null) {
			for (int i = 0; i < json.optJSONArray("image").length(); i++) {
				String imageName = json.optJSONArray("image").optString(i);
				if (!StringUtils.IsNullOrEmpty(imageName)) {
					image.add(imageName);
				}
			}
		}

		if (json.optJSONArray("tags") != null) {
			for (int i = 0; i < json.optJSONArray("tags").length(); i++) {
				tags.add(json.optJSONArray("tags").optString(i));
			}
		}

		if (json.optJSONArray("address") != null) {
			for (int i = 0; i < json.optJSONArray("address").length(); i++) {
				address.add(json.optJSONArray("address").optString(i));
			}
		}

		if (json.has("contact")) {
			contact = new Contact(json.optJSONObject("contact"));
		}

		if (json.has("geo_location")) {
			geo_location = new GeoLocation(json.optJSONObject("geo_location"));
		}

		if (json.has("visit_info")) {
			visit_info = new VisitInfo(json.optJSONObject("visit_info"));
		}

		if (json.optJSONArray("tour_guides") != null) {
			for (int i = 0; i < json.optJSONArray("tour_guides").length(); i++) {
				String tourGuides = json.optJSONArray("tour_guides").optString(i);
				if (!StringUtils.IsNullOrEmpty(tourGuides)) {
					tour_guides.add(tourGuides);
				}
			}
		}
	}

	public String getPlaceId() {
		return id;
	}

	public String getPlaceName() {
		return name;
	}

	public String getPlaceDesc() {
		return description;
	}

	public List<String> getPlaceTags() {
		return tags;
	}

	public List<String> getPlaceImages() {
		return image;
	}

	public List<String> getPlaceAddress() {
		return address;
	}

	public Contact getContact() {
		return contact;
	}

	public GeoLocation getLocation() {
		return geo_location;
	}

	public VisitInfo getVisitInfo() {
		return visit_info;
	}

	public List<String> getTourGuideIds() {
		return tour_guides;
	}

	public boolean isValid() {
		if (!StringUtils.IsNullOrEmpty(name, description) && !image.isEmpty() && !tags.isEmpty()) { // at least these 4 needs to be available, otherwise the views can't be created
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
			json.put("id", JSONUtils.toJSON(id));
			json.put("name", JSONUtils.toJSON(name));
			json.put("description", JSONUtils.toJSON(description));
			json.put("image", JSONUtils.toJSON(image));
			json.put("tags", JSONUtils.toJSON(tags));
			json.put("address", JSONUtils.toJSON(address));
			json.put("contact", JSONUtils.toJSON(contact));
			json.put("geo_location", JSONUtils.toJSON(geo_location));
			json.put("visit_info", JSONUtils.toJSON(visit_info));
			json.put("tour_guides", JSONUtils.toJSON(tour_guides));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

}

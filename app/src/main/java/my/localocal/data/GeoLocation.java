package my.localocal.data;

import my.localocal.json.IJSONifyableObject;
import my.localocal.json.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class GeoLocation implements IJSONifyableObject{

	private double	     latitude	= 0.0000000;
	private double	     longitude	= 0.0000000;
	private final String	mapImageUrl;

	public GeoLocation(JSONObject json) {
		latitude = json.optDouble("latitude");
		longitude = json.optDouble("longitude");
		mapImageUrl = "http://maps.google.com/maps/api/staticmap?center=" + latitude + "," + longitude + "&zoom=15&size=250x250&" + "&markers=" + createMarkerParams() + "&sensor=false";
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public String getImageMapUrl() {
		return mapImageUrl;
	}

	private String createMarkerParams() {
		String markerParam = "color:red|label:Spot|" + latitude + "," + longitude;
		return markerParam;
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();

		try {
			json.put("latitude", JSONUtils.toJSON(latitude));
			json.put("longitude", JSONUtils.toJSON(longitude));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}
}

package my.localocal.data;

import java.util.ArrayList;
import java.util.List;

import my.localocal.json.IJSONifyableObject;
import my.localocal.json.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListOfPlaces implements IJSONifyableObject{

	private final List<Place>	places	= new ArrayList<Place>();

	public ListOfPlaces(JSONArray json_array) {
		places.clear();
		if (json_array != null) {
			for (int i = 0; i < json_array.length(); i++) {
				Place place = new Place(json_array.optJSONObject(i));
				if (place.isValid()) {
					places.add(place);
				}
			}
		}
	}

	public List<Place> getPlaces() {
		return places;
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("places", JSONUtils.toJSON(places));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

}

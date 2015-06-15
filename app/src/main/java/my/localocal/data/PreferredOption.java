package my.localocal.data;

import my.localocal.json.IJSONifyableObject;
import my.localocal.json.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class PreferredOption implements IJSONifyableObject{

	private final String	id;
	private final String	name;
	private final String	thumbnail;

	public PreferredOption(JSONObject json) {

		id = !json.isNull("id") ? json.optString("id") : null;
		name = !json.isNull("name") ? json.optString("name") : null;;
		thumbnail = !json.isNull("thumbnail") ? json.optString("thumbnail") : null;;
	}

	public String getOptionId() {
		return id;
	}

	public String getOptionName() {
		return name;
	}

	public String getOptionThumbnail() {
		return thumbnail;
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
			json.put("thumbnail", JSONUtils.toJSON(thumbnail));
		} catch (JSONException e) {
			//...
		}
		return json;
	}

}

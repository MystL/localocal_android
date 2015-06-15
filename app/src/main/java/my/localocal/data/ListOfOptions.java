package my.localocal.data;

import java.util.ArrayList;
import java.util.List;

import my.localocal.json.IJSONifyableObject;
import my.localocal.json.JSONUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ListOfOptions implements IJSONifyableObject{

	private final List<PreferredOption>	listOfOptions	= new ArrayList<PreferredOption>();

	public ListOfOptions(JSONArray json_array) {
		listOfOptions.clear();
		if (json_array != null) {
			for (int i = 0; i < json_array.length(); i++) {
				PreferredOption option = new PreferredOption(json_array.optJSONObject(i));
				listOfOptions.add(option);
			}
		}

	}

	public List<PreferredOption> getOptions() {
		return listOfOptions;
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("listOfOptions", JSONUtils.toJSON(listOfOptions));
		} catch (JSONException e) {
			//....
		}
		return json;
	}

}

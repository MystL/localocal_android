package my.localocal.data;

import java.util.ArrayList;
import java.util.List;

import my.localocal.json.IJSONifyableObject;
import my.localocal.json.JSONUtils;
import my.localocal.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class VisitInfo implements IJSONifyableObject{

	private final List<String>	operating_hours	= new ArrayList<String>();
	private final List<String>	admission_fee	= new ArrayList<String>();

	public VisitInfo(JSONObject json) {

		if (json.optJSONArray("operating_hours") != null) {
			for (int i = 0; i < json.optJSONArray("operating_hours").length(); i++) {
				String operatingHours = json.optJSONArray("operating_hours").optString(i);
				if (!StringUtils.IsNullOrEmpty(operatingHours)) {
					operating_hours.add(operatingHours);
				}
			}
		}

		if (json.optJSONArray("admission_fee") != null) {
			for (int i = 0; i < json.optJSONArray("admission_fee").length(); i++) {
				String operatingHours = json.optJSONArray("admission_fee").optString(i);
				if (!StringUtils.IsNullOrEmpty(operatingHours)) {
					admission_fee.add(operatingHours);
				}
			}
		}
	}

	public List<String> getOperatingHours() {
		return operating_hours;
	}

	public List<String> getAdmissionFee() {
		return admission_fee;
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();

		try {
			json.put("operating_hours", JSONUtils.toJSON(operating_hours));
			json.put("admission_fee", JSONUtils.toJSON(admission_fee));
		} catch (JSONException e) {
			//...
		}
		return json;
	}

}

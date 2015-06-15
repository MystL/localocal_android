package my.localocal.data;

import my.localocal.json.IJSONifyableObject;
import my.localocal.json.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class Contact implements IJSONifyableObject{

	private String	tel	  = null;
	private String	fax	  = null;
	private String	email	= null;

	public Contact(JSONObject json) {
		tel = !json.isNull("tel") ? json.optString("tel") : null;
		fax = !json.isNull("fax") ? json.optString("fax") : null;
		email = !json.isNull("email") ? json.optString("email") : null;
	}

	public String getTel() {
		return tel;
	}

	public String getFax() {
		return fax;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();

		try {
			json.put("tel", JSONUtils.toJSON(tel));
			json.put("fax", JSONUtils.toJSON(fax));
			json.put("email", JSONUtils.toJSON(email));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json;
	}

}

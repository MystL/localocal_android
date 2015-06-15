package my.localocal.data;

import my.localocal.json.IJSONifyableObject;
import my.localocal.json.JSONUtils;
import my.localocal.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class FacebookUser implements IJSONifyableObject{

	private final String	id;
	private final String	first_name;
	private final String	last_name;
	private final String	name;
	private final String	locale;
	private final String	link;
	private final String	gender;
	private final String	profile_img;

	public FacebookUser(JSONObject json) {

		id = !json.isNull("id") ? json.optString("id") : null;
		first_name = !json.isNull("first_name") ? json.optString("first_name") : null;
		last_name = !json.isNull("last_name") ? json.optString("last_name") : null;
		name = !json.isNull("name") ? json.optString("name") : null;
		locale = !json.isNull("locale") ? json.optString("locale") : null;
		link = !json.isNull("link") ? json.optString("link") : null;
		gender = !json.isNull("gender") ? json.optString("gender") : null;
		profile_img = !StringUtils.IsNullOrEmpty(id) ? "https://graph.facebook.com/" + id + "/picture?type=large" : null;
	}

	public String getId() {
		return id;
	}

	public String getFirstname() {
		return first_name;
	}

	public String getLastname() {
		return last_name;
	}

	public String getFullName() {
		return name;
	}

	public String getLocale() {
		return locale;
	}

	public String getLink() {
		return link;
	}

	public String getGender() {
		return gender;
	}

	public String getProfileImage() {
		return profile_img;
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
			json.put("name", JSONUtils.toJSON(name));
			json.put("locale", JSONUtils.toJSON(locale));
			json.put("link", JSONUtils.toJSON(link));
			json.put("gender", JSONUtils.toJSON(gender));
			json.put("profile_img", JSONUtils.toJSON(profile_img));
		} catch (JSONException e) {
			//...
		}
		return json;
	}

}

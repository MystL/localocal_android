package my.localocal.json;

import java.util.Collection;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class JSONUtils{

	public static Object toJSON(Object o) {
		if (o == null) {
			return "";
		}
		try {
			if (o instanceof IJSONifyableObject)
				return ((IJSONifyableObject) o).toJSON();
			else if (o instanceof Boolean)
				return Integer.valueOf(((Boolean) o).booleanValue() ? 1 : 0);
			else if (o.getClass().isArray()) {
				JSONArray json = new JSONArray();
				for (Object o2 : ((Object[]) o)) {
					json.put(toJSON(o2));
				}
				return json;
			} else if (o instanceof Map<?, ?>) {
				JSONObject json = new JSONObject();
				for (Object key : ((Map<?, ?>) o).keySet()) {
					json.put(key.toString(), toJSON(((Map<?, ?>) o).get(key)));
				}
				return json;
			} else if (o instanceof Collection<?>) {
				JSONArray json = new JSONArray();
				for (Object o2 : ((Collection<?>) o)) {
					json.put(toJSON(o2));
				}
				return json;
			} else if (o.getClass().isEnum()) {
				return o.toString();
			} else
				return o;
		} catch (Exception e) {
			return o;
		}
	}

}

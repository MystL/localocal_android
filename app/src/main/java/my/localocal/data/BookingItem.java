package my.localocal.data;

import java.util.Calendar;

import my.localocal.json.IJSONifyableObject;
import my.localocal.json.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class BookingItem implements IJSONifyableObject{

	private final String	placeId;
	private final String	tourGuideId;
	private final String	status;

	private final int	 tour_date;
	private final int	 tour_month;
	private final int	 tour_year;

	private final int	 book_date;
	private final int	 book_month;
	private final int	 book_year;

	public BookingItem(JSONObject json) {
		placeId = !json.isNull("placeId") ? json.optString("placeId") : null;
		tourGuideId = !json.isNull("tourGuideId") ? json.optString("tourGuideId") : null;
		status = !json.isNull("status") ? json.optString("status") : null;

		tour_year = json.optInt("tour_year");
		tour_month = json.optInt("tour_month");
		tour_date = json.optInt("tour_date");

		book_year = json.optInt("book_year");
		book_month = json.optInt("book_month");
		book_date = json.optInt("book_date");

	}

	public String getPlaceId() {
		return placeId;
	}

	public String getTourGuideId() {
		return tourGuideId;
	}

	public Calendar getTourDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, tour_year);
		c.set(Calendar.MONTH, tour_month);
		c.set(Calendar.DATE, tour_date);
		return c;
	}

	public Calendar getBookingDate() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, book_year);
		c.set(Calendar.MONTH, book_month);
		c.set(Calendar.DATE, book_date);
		return c;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return toJSON().toString();
	}

	@Override
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		try {
			json.put("placeId", JSONUtils.toJSON(placeId));
			json.put("tourGuideId", JSONUtils.toJSON(tourGuideId));

			json.put("tour_year", JSONUtils.toJSON(tour_year));
			json.put("tour_month", JSONUtils.toJSON(tour_month));
			json.put("tour_date", JSONUtils.toJSON(tour_date));

			json.put("book_year", JSONUtils.toJSON(book_year));
			json.put("book_month", JSONUtils.toJSON(book_month));
			json.put("book_date", JSONUtils.toJSON(book_date));

			json.put("status", JSONUtils.toJSON(status));
		} catch (JSONException e) {
			//...
		}
		return json;
	}

}

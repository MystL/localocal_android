package my.localocal.booking;

import java.util.ArrayList;
import java.util.List;

import my.localocal.MainActivity;
import my.localocal.R;
import my.localocal.data.BookingItem;
import my.localocal.theme.ThemeHandler;
import my.localocal.utils.StringUtils;
import my.localocal.views.listview.ListViewAdapter;
import my.localocal.views.listview.ListViewItem;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BookingHistoryViewController extends Fragment{

	private ImageView	             bg_img;
	private TextView	             lbl_empty;
	private ThemeHandler	         themeHandler;
	private ListView	             listView;
	private final List<ListViewItem>	itemsList	= new ArrayList<ListViewItem>();
	private final List<BookingItem>	 bookedItemList	= new ArrayList<BookingItem>();

	public BookingHistoryViewController() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.booking_history_layout, container, false);

		themeHandler = new ThemeHandler(getActivity());

		bg_img = (ImageView) rootView.findViewById(R.id.booking_history__img_bg);
		bg_img.setImageDrawable(themeHandler.getImageDrawable("bg__img_splash"));
		listView = (ListView) rootView.findViewById(R.id.booking_history__list);
		lbl_empty = (TextView) rootView.findViewById(R.id.booking_history__lbl_empty);

		String bookedTours = readFromPrefs(MainActivity.PREF_TOUR_BOOKINGS_KEY);
		if (!StringUtils.IsNullOrEmpty(bookedTours)) {
			bookedItemList.clear();
			JSONArray bookedToursJSONArray = null;
			try {
				bookedToursJSONArray = new JSONArray(bookedTours);
				for (int i = 0; i < bookedToursJSONArray.length(); i++) {
					BookingItem item = new BookingItem(bookedToursJSONArray.getJSONObject(i));
					bookedItemList.add(item);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			for (BookingItem item : bookedItemList) {
				itemsList.add(new BookingHistoryItemView(getActivity(), item));
			}
			listView.setAdapter(new ListViewAdapter(itemsList));
			listView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub

				}
			});
		} else {
			listView.setVisibility(View.GONE);
			lbl_empty.setVisibility(View.VISIBLE);
		}

		return rootView;
	}

	private String readFromPrefs(String key) {
		SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		return sharedPrefs.getString(key, null);
	}

}

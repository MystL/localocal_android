package my.localocal.views.profiling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import my.localocal.DataManager;
import my.localocal.MainActivity;
import my.localocal.R;
import my.localocal.data.PreferredOption;
import my.localocal.gridview.GridViewAdapter;
import my.localocal.gridview.GridViewItem;
import my.localocal.theme.ThemeHandler;
import my.localocal.views.MainViewController;
import my.localocal.views.places.PlacesViewController;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfilingViewController extends Fragment{

	public static final String	     TAG	           = "ProfilingViewController.Tag";

	private ThemeHandler	         themeHandler;
	private GridView	             gridView;
	private GridViewAdapter	         gridViewAdapter;
	private final List<GridViewItem>	itemsList	   = new ArrayList<GridViewItem>();
	private final Set<String>	     selectedOptionIds	= new HashSet<String>();
	private Button	                 btn_savePreference;
	private ImageView	             img_bg;

	public ProfilingViewController() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.app_main_content, container, false);

		selectedOptionIds.clear();

		themeHandler = new ThemeHandler(getActivity());
		initGridViewItem();

		img_bg = (ImageView) rootView.findViewById(R.id.main_view__img_bg);
		img_bg.setImageDrawable(themeHandler.getImageDrawable("bg__img_putrajaya"));

		if (readStoredOptionsIds() != null) {
			for (String storedId : readStoredOptionsIds()) {
				if (!selectedOptionIds.contains(storedId)) {
					selectedOptionIds.add(storedId);
				}
			}
		}

		gridView = (GridView) rootView.findViewById(R.id.profiling_gridview);
		gridViewAdapter = new GridViewAdapter(getActivity(), itemsList);
		gridView.setAdapter(gridViewAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GridViewAdapter m_adapter = (GridViewAdapter) gridView.getAdapter();
				if (m_adapter.getSelectedItem(position) instanceof PreferredPlaceItemView) {
					PreferredPlaceItemView selectedItem = (PreferredPlaceItemView) m_adapter.getSelectedItem(position);
					if (selectedItem.setItemSelected()) {
						if (!selectedOptionIds.contains(selectedItem.getOption().getOptionId())) {
							selectedOptionIds.add(selectedItem.getOption().getOptionId());
						}
					} else {
						if (selectedOptionIds.contains(selectedItem.getOption().getOptionId())) {
							selectedOptionIds.remove(selectedItem.getOption().getOptionId());
						}
					}
				}
			}
		});

		btn_savePreference = (Button) rootView.findViewById(R.id.profiling__btn_save);
		btn_savePreference.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {

				storeToPrefs(MainActivity.PREF_OPTIONS_ID_KEY, selectedOptionIds);

				if (readStoredOptionsIds() != null) {
					if (!readStoredOptionsIds().isEmpty()) {
						Toast.makeText(getActivity(), "Thanks! We will help you discover places based on your preferences!", Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(getActivity(), "No preferred categories was set for now.. :(", Toast.LENGTH_LONG).show();
					}
				}

				MainActivity mainActivity = (MainActivity) getActivity();
				MainViewController mainVC = mainActivity.getMainVC();
				mainVC.selectMenuItem(new PlacesViewController(), false);

			}
		});

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	private void initGridViewItem() {
		if (readStoredOptionsIds() != null) {
			for (PreferredOption option : DataManager.shared(getActivity()).getAllOptions().getOptions()) {
				if (readStoredOptionsIds().contains(option.getOptionId())) {
					itemsList.add(new PreferredPlaceItemView(getActivity(), option, true));
				} else {
					itemsList.add(new PreferredPlaceItemView(getActivity(), option, false));
				}
			}
		} else {
			for (PreferredOption option : DataManager.shared(getActivity()).getAllOptions().getOptions()) {
				itemsList.add(new PreferredPlaceItemView(getActivity(), option, false));
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void storeToPrefs(String key, Object value) {

		SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = sharedPrefs.edit();

		if (value instanceof String) {
			prefsEditor.putString(key, value.toString());
		} else if (value instanceof Boolean) {
			prefsEditor.putBoolean(key, ((Boolean) value).booleanValue());
		} else if (value instanceof HashSet) {
			HashSet<String> valueList = (HashSet<String>) value;
			prefsEditor.putStringSet(key, valueList);
		}

		boolean isCommited = prefsEditor.commit();
		//Log.i("XXX", DataUtils.getStringValue(sharedPrefs.getAll()));
		if (!isCommited) {
			throw new RuntimeException("Unable to save into SharedPreferences.");
		}

	}

	private void removeFromSharedPrefs(String keyToRemove) {
		SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = sharedPrefs.edit();

		prefsEditor.remove(keyToRemove);

		boolean isCommited = prefsEditor.commit();
		//Log.i("XXX", DataUtils.getStringValue(sharedPrefs.getAll()));
		if (!isCommited) {
			throw new RuntimeException("Unable to save into SharedPreferences.");
		}

	}

	private Set<String> readStoredOptionsIds() {
		SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		return sharedPrefs.getStringSet(MainActivity.PREF_OPTIONS_ID_KEY, null);
	}

}

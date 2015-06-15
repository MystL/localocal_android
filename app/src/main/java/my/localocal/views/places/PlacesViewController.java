package my.localocal.views.places;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import my.localocal.DataManager;
import my.localocal.MainActivity;
import my.localocal.R;
import my.localocal.data.Place;
import my.localocal.listeners.PlacesViewControllerListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PlacesViewController extends Fragment{

	public static final String	                     TAG	                         = "PlacesViewController.Tag";

	private static final int	                     VIEW_PAGER_MARGIN	             = 5;
	protected ViewPager	                             viewPager;
	protected final List<Fragment>	                 listOfDestinationViews	         = new ArrayList<Fragment>();
	private final List<PlacesViewControllerListener>	placesViewControllerListener	= new ArrayList<PlacesViewControllerListener>();
	private final List<Place>	                     listOfPlaces	                 = new ArrayList<Place>();

	public PlacesViewController() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			//Ask the user to enable GPS
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("GPS Location");
			builder.setMessage("Would you like to provide us information about your current location for better recommendation?");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//Launch settings, allowing user to make a change
					Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(i);
				}
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					//No location service, no Activity
					dialog.dismiss();
				}
			});
			builder.create().show();
		}

		listOfPlaces.clear();

		//TODO - consider not parsing the data in but rather read from somewhere else. e.g. DB
		if (DataManager.shared(getActivity()).getAllPlaces() != null) {
			listOfPlaces.addAll(DataManager.shared(getActivity()).getAllPlaces().getPlaces());
			for (Place place : sortListOfPlacesByPrefOptions(listOfPlaces)) {
				Fragment destinationFragment = PlaceListViewController.newInstance(place);
				listOfDestinationViews.add(destinationFragment);
			}
		}
	}

	private List<Place> sortListOfPlacesByPrefOptions(List<Place> listOfPlaces) {

		if (readStoredOptionsIds() == null) {
			return listOfPlaces;
		} else if (readStoredOptionsIds().isEmpty()) {
			return listOfPlaces;
		}

		List<String> rearrangedPlaceIds = new ArrayList<String>();
		rearrangedPlaceIds.clear();
		for (Place place : listOfPlaces) {
			for (String prefId : readStoredOptionsIds()) {
				if (place.getPlaceTags().contains(prefId)) {
					if (!rearrangedPlaceIds.contains(place.getPlaceId())) { // add those places with tags that matches user's preference
						rearrangedPlaceIds.add(place.getPlaceId());
					}
				}
			}
		}

		for (Place place : listOfPlaces) {
			if (!rearrangedPlaceIds.contains(place)) {
				rearrangedPlaceIds.add(place.getPlaceId());
			}
		}

		//TODO - temp
		//Log.i("XXX", DataUtils.getStringValue(rearrangedPlaceIds));

		List<Place> rearrangedPlaceList = new ArrayList<Place>();
		rearrangedPlaceList.clear();
		for (String placeId : rearrangedPlaceIds) {
			rearrangedPlaceList.add(DataManager.shared(getActivity()).getPlaceById(placeId));
		}
		return rearrangedPlaceList;
	}

	@Override
	public View onCreateView(LayoutInflater inflate, ViewGroup container, Bundle savedInstance) {

		View rootView = inflate.inflate(R.layout.destinations, container, false);

		if (!listOfDestinationViews.isEmpty()) {
			PlaceListViewController placeListViewController = (PlaceListViewController) listOfDestinationViews.get(0);
			notifyPlacesViewControllerListener(placeListViewController.getPlace());
		}

		viewPager = (ViewPager) rootView.findViewById(R.id.destinations__view_pager);
		viewPager.setPageMargin(VIEW_PAGER_MARGIN);
		viewPager.setOffscreenPageLimit(1);
		viewPager.setOnPageChangeListener(new OnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				PlaceListViewController placeListViewController = (PlaceListViewController) listOfDestinationViews.get(position);
				notifyPlacesViewControllerListener(placeListViewController.getPlace());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { /*ignore*/}

			@Override
			public void onPageScrollStateChanged(int arg0) { /*ignore*/}
		});
		ViewPagerAdapter pageAdapter = new ViewPagerAdapter(getFragmentManager(), listOfDestinationViews);
		viewPager.setAdapter(pageAdapter);
		viewPager.setCurrentItem(0);

		return rootView;
	}

	public void addPlacesViewControllerListener(PlacesViewControllerListener listener) {
		placesViewControllerListener.add(listener);
	}

	public void removePlacesViewControllerListener(PlacesViewControllerListener listener) {
		placesViewControllerListener.remove(listener);
	}

	private void notifyPlacesViewControllerListener(Place place) {
		for (PlacesViewControllerListener m_listener : placesViewControllerListener) {
			m_listener.PlacesViewControllerListener__onCurrentPlaceUpdated(place);
		}
	}

	@Override
	public void onPause() {
		super.onPause();

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (viewPager != null) {
			viewPager.removeAllViews();
			viewPager.removeAllViewsInLayout();
		}

		if (listOfDestinationViews != null) {
			listOfDestinationViews.clear();
		}

		if (listOfPlaces != null) {
			listOfPlaces.clear();
		}

	}

	private Set<String> readStoredOptionsIds() {
		SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		return sharedPrefs.getStringSet(MainActivity.PREF_OPTIONS_ID_KEY, null);
	}

}

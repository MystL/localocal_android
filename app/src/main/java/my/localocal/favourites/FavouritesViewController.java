package my.localocal.favourites;

import java.util.HashSet;
import java.util.Set;

import my.localocal.DataManager;
import my.localocal.MainActivity;
import my.localocal.data.Place;
import my.localocal.views.places.PlaceListViewController;
import my.localocal.views.places.PlacesViewController;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public class FavouritesViewController extends PlacesViewController{

	private final Set<String>	favouritePlaceIds	= new HashSet<String>();

	public FavouritesViewController() {
		super();
	}

	@Override
	public void onCreate(Bundle savedStateInstance) {
		super.onCreate(savedStateInstance);

		favouritePlaceIds.clear();
		listOfDestinationViews.clear();

		if (readFavFromPrefs() != null) {
			for (String id : readFavFromPrefs()) {
				if (!favouritePlaceIds.contains(id)) {
					favouritePlaceIds.add(id);
				}
			}

			for (String favId : favouritePlaceIds) {
				Place place = DataManager.shared(getActivity()).getPlaceById(favId);
				Fragment destinationFragment = PlaceListViewController.newInstance(place);
				listOfDestinationViews.add(destinationFragment);
			}
		}

	}

	private Set<String> readFavFromPrefs() {
		SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		//Log.i("XXX", DataUtils.getStringValue(sharedPrefs.getAll()));
		return sharedPrefs.getStringSet(MainActivity.PREF_FAVOURITES_PLACE_KEY, null);
	}
}

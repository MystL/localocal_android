package my.localocal.views;

import java.util.Set;

import my.localocal.MainActivity;
import my.localocal.R;
import my.localocal.about.AboutViewController;
import my.localocal.account.UserAccountViewController;
import my.localocal.booking.BookingHistoryViewController;
import my.localocal.data.Place;
import my.localocal.favourites.FavouritesViewController;
import my.localocal.listeners.PlacesViewControllerListener;
import my.localocal.views.places.PlacesViewController;
import my.localocal.views.profiling.ProfilingViewController;
import my.localocal.views.sidemenu.SideMenuViewController;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;

public class MainViewController{

	private final FragmentActivity	     mainActivity;
	private final ViewGroup	             mainViewControllerView;
	private final DrawerLayout	         slidingDrawerLayout;
	private final FragmentManager	     fragmentManager;
	private final SideMenuViewController	sideMenuViewController;
	private final NavbarViewController	 navbarViewController;

	public MainViewController(FragmentActivity mainActivity, View controllerView) {

		this.mainActivity = mainActivity;

		fragmentManager = mainActivity.getSupportFragmentManager();

		mainViewControllerView = (ViewGroup) controllerView;

		slidingDrawerLayout = (DrawerLayout) mainViewControllerView.findViewById(R.id.drawer_layout);
		slidingDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		sideMenuViewController = new SideMenuViewController(mainActivity, mainViewControllerView.findViewById(R.id.main__side_menu));
		sideMenuViewController.initMenu();

		navbarViewController = new NavbarViewController(mainActivity, mainViewControllerView.findViewById(R.id.main__navbar));

		if (readStoredOptionsIds() != null) {
			if (!readStoredOptionsIds().isEmpty()) {
				selectMenuItem(new PlacesViewController(), false);
			} else {
				selectMenuItem(new ProfilingViewController(), false);
			}
		} else {
			selectMenuItem(new ProfilingViewController(), false);
		}

	}

	public View getMainViewControllerView() {
		return this.mainViewControllerView;
	}

	public void selectMenuItem(Fragment fragment, boolean bAddToBackStack) {

		if (fragment instanceof PlacesViewController || fragment instanceof FavouritesViewController) {
			navbarViewController.setBookTourButtonVisibility(true);
			navbarViewController.setNavbarDarkerBackground(true);

			PlacesViewController placesViewController = (PlacesViewController) fragment;
			placesViewController.addPlacesViewControllerListener(new PlacesViewControllerListener(){
				@Override
				public void PlacesViewControllerListener__onCurrentPlaceUpdated(Place place) {
					navbarViewController.setCurrentPlace(place);
				}
			});

			if (fragment instanceof FavouritesViewController) {
				navbarViewController.setTitle("Favourites", true);
			} else {
				navbarViewController.setTitle("", false);
			}

		} else if (fragment instanceof AboutViewController) {
			navbarViewController.setBookTourButtonVisibility(false);
			navbarViewController.setNavbarDarkerBackground(true);
			navbarViewController.setTitle("About", true);
		} else if (fragment instanceof ProfilingViewController) {
			navbarViewController.setBookTourButtonVisibility(false);
			navbarViewController.setNavbarDarkerBackground(false);
			navbarViewController.setTitle("Preferences", true);
		} else if (fragment instanceof BookingHistoryViewController) {
			navbarViewController.setBookTourButtonVisibility(false);
			navbarViewController.setNavbarDarkerBackground(false);
			navbarViewController.setTitle("Bookings", true);
		} else if (fragment instanceof UserAccountViewController) {
			navbarViewController.setBookTourButtonVisibility(false);
			navbarViewController.setNavbarDarkerBackground(true);
			navbarViewController.setTitle("Account", true);
		} else {
			navbarViewController.setBookTourButtonVisibility(false);
			navbarViewController.setNavbarDarkerBackground(false);
			navbarViewController.setTitle("", false);
		}

		FragmentTransaction fragmentTrans = fragmentManager.beginTransaction();
		fragmentTrans.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
		//  temp
		fragmentTrans.replace(R.id.main_content_frame, fragment);
		//fragmentTrans.addToBackStack(null);
		if (bAddToBackStack) {
			//fragmentTrans.addToBackStack(null);
		}
		fragmentTrans.commit();
		mainActivity.getSupportFragmentManager().executePendingTransactions();
	}

	public void toggleMenu() {
		if (slidingDrawerLayout.isDrawerOpen(sideMenuViewController.getControllerView())) {
			slidingDrawerLayout.closeDrawer(sideMenuViewController.getControllerView());
			return;
		} else {
			slidingDrawerLayout.openDrawer(sideMenuViewController.getControllerView());
			return;
		}
	}

	public Object readFromPrefs(String key) {
		SharedPreferences sharedPrefs = mainActivity.getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		return sharedPrefs.getStringSet(key, null);
	}

	private Set<String> readStoredOptionsIds() {
		SharedPreferences sharedPrefs = mainActivity.getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		return sharedPrefs.getStringSet(MainActivity.PREF_OPTIONS_ID_KEY, null);
	}

	public Set<String> readFavFromPrefs() {
		SharedPreferences sharedPrefs = mainActivity.getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		return sharedPrefs.getStringSet(MainActivity.PREF_FAVOURITES_PLACE_KEY, null);
	}

}

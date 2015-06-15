package my.localocal.views.sidemenu;

import java.util.ArrayList;
import java.util.List;

import my.localocal.MainActivity;
import my.localocal.R;
import my.localocal.about.AboutViewController;
import my.localocal.account.UserAccountViewController;
import my.localocal.booking.BookingHistoryViewController;
import my.localocal.favourites.FavouritesViewController;
import my.localocal.theme.ThemeHandler;
import my.localocal.views.MainViewController;
import my.localocal.views.listview.ListViewAdapter;
import my.localocal.views.listview.ListViewItem;
import my.localocal.views.places.PlacesViewController;
import my.localocal.views.profiling.ProfilingViewController;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class SideMenuViewController{

	private final ListView	       listView;
	private final FragmentActivity	fragmentActivity;
	private View	               sideMenuView	= null;
	//private final ImageView	       img_bg;
	private final ThemeHandler	   themeHandler;

	public SideMenuViewController(FragmentActivity fragmentActivity, View controllerView) {

		this.fragmentActivity = fragmentActivity;
		sideMenuView = controllerView;

		themeHandler = new ThemeHandler(fragmentActivity);

		//img_bg = (ImageView) controllerView.findViewById(R.id.side_menu__img_bg);
		//img_bg.setImageDrawable(themeHandler.getImageDrawable("img__bg_side_menu"));

		listView = (ListView) controllerView.findViewById(R.id.side_menu__list);

	}

	public synchronized void initMenu() {
		List<ListViewItem> sideMenuItems = new ArrayList<ListViewItem>();

		sideMenuItems.add(new SideMenuHeaderItemView(fragmentActivity, new SideMenuHeaderItem("DISCOVER")));
		sideMenuItems.add(new SideMenuItemView(fragmentActivity, new SideMenuItem("Destinations", R.drawable.ic_action_place)));

		sideMenuItems.add(new SideMenuHeaderItemView(fragmentActivity, new SideMenuHeaderItem("ACCOUNT")));
		sideMenuItems.add(new SideMenuItemView(fragmentActivity, new SideMenuItem("Account", R.drawable.ic_action_person)));
		sideMenuItems.add(new SideMenuItemView(fragmentActivity, new SideMenuItem("Bookings", R.drawable.ic_action_important)));
		sideMenuItems.add(new SideMenuItemView(fragmentActivity, new SideMenuItem("Favourites", R.drawable.ic_action_favorite)));
		sideMenuItems.add(new SideMenuItemView(fragmentActivity, new SideMenuItem("Preferences", R.drawable.ic_action_settings)));

		sideMenuItems.add(new SideMenuHeaderItemView(fragmentActivity, new SideMenuHeaderItem("OTHER")));
		sideMenuItems.add(new SideMenuItemView(fragmentActivity, new SideMenuItem("About", R.drawable.ic_action_about)));
		sideMenuItems.add(new SideMenuItemView(fragmentActivity, new SideMenuItem("Contact Us", R.drawable.ic_action_help)));

		listView.setAdapter(new ListViewAdapter(sideMenuItems));
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				ListViewAdapter m_adapter = (ListViewAdapter) listView.getAdapter();

				MainActivity mainActivity = (MainActivity) fragmentActivity;
				MainViewController mainVC = mainActivity.getMainVC();

				if (m_adapter.getSelectedItem(position) instanceof SideMenuItemView) {
					SideMenuItemView selectedItem = (SideMenuItemView) m_adapter.getSelectedItem(position);
					if (v != null) {
						v.setSelected(false);
					}

					if (selectedItem.getSideMenuItem().getSideMenuItemText().equalsIgnoreCase("Destinations")) {
						mainVC.selectMenuItem(new PlacesViewController(), true);
					} else if (selectedItem.getSideMenuItem().getSideMenuItemText().equalsIgnoreCase("Preferences")) {
						mainVC.selectMenuItem(new ProfilingViewController(), true);
					} else if (selectedItem.getSideMenuItem().getSideMenuItemText().equalsIgnoreCase("Bookings")) {
						mainVC.selectMenuItem(new BookingHistoryViewController(), true);
					} else if (selectedItem.getSideMenuItem().getSideMenuItemText().equalsIgnoreCase("About")) {
						mainVC.selectMenuItem(new AboutViewController(), true);
					} else if (selectedItem.getSideMenuItem().getSideMenuItemText().equalsIgnoreCase("Favourites")) {
						if (mainVC.readFavFromPrefs() != null) {
							if (!mainVC.readFavFromPrefs().isEmpty()) {
								mainVC.selectMenuItem(new FavouritesViewController(), true);
							} else {
								Toast.makeText(fragmentActivity, "Sorry, currently there is no favourite place selected.\nPlease come back again when you had added your favourite place. :D",
								    Toast.LENGTH_LONG).show();
								mainVC.selectMenuItem(new PlacesViewController(), true);
							}
						} else {
							Toast.makeText(fragmentActivity, "Sorry, currently there is no favourite place selected.\nPlease come back again when you had added your favourite place. :D",
							    Toast.LENGTH_LONG).show();
							mainVC.selectMenuItem(new PlacesViewController(), true);
						}
					} else if (selectedItem.getSideMenuItem().getSideMenuItemText().equalsIgnoreCase("Contact Us")) {
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "melvinlee.developer@gmail.com" });
						intent.putExtra(Intent.EXTRA_SUBJECT, "About LocaLocal Services");
						intent.setType("message/rfc822");
						mainActivity.startActivity(intent);
					} else if (selectedItem.getSideMenuItem().getSideMenuItemText().equalsIgnoreCase("Account")) {
						mainVC.selectMenuItem(new UserAccountViewController(), true);
					}
				}
				mainVC.toggleMenu();
			}

		});
		//listView.addFooterView(LayoutInflater.from(fragmentActivity).inflate(R.layout.side_menu_list_emptyfooter, null));
		listView.invalidateViews();
	}

	public View getControllerView() {
		return sideMenuView;
	}

}

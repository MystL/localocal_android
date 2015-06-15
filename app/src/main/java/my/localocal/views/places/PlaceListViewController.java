package my.localocal.views.places;

import java.util.ArrayList;
import java.util.List;

import my.localocal.data.Place;
import my.localocal.views.listview.ListViewFooterItemView;
import my.localocal.views.listview.ListViewItem;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

@SuppressLint("ValidFragment")
public class PlaceListViewController extends BlurEffectListViewFragment{

	private final List<ListViewItem>	viewsList	= new ArrayList<ListViewItem>();
	private Place	                 place	      = null;
	private PlaceTopInfoView	     topInfoView	= null;

	public PlaceListViewController() {
		super();
	}

	public static final PlaceListViewController newInstance(Place place) {
		PlaceListViewController vc = new PlaceListViewController();
		vc.setPlace(place);
		return vc;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		topInfoView = new PlaceTopInfoView(getActivity(), place);

		viewsList.add(topInfoView);
		viewsList.add(new PlaceVisitInfoView(getActivity(), place));
		viewsList.add(new PlaceDescriptionItemView(getActivity(), place));
		viewsList.add(new PlaceMapLocation(getActivity(), place));
		viewsList.add(new PlaceViewTourGuideView(getActivity(), place));
		viewsList.add(new ListViewFooterItemView(getActivity()));
	}

	@Override
	protected List<ListViewItem> getListItem() {
		return viewsList;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected int getTopHeight() {
		WindowManager wm = (WindowManager) getActivity().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return (int) (display.getHeight() * 0.52);
	}

	@Override
	protected String getInitialBackgroundImageName() {
		return place.getPlaceImages().get(0); // get the first one first
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		this.viewsList.clear();

	}

	public PlaceTopInfoView getTopInfoView() {
		return topInfoView;
	}

	public Place getPlace() {
		return this.place;
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

}

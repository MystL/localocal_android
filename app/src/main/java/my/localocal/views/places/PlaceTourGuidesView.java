package my.localocal.views.places;

import java.util.ArrayList;
import java.util.List;

import my.localocal.DataManager;
import my.localocal.R;
import my.localocal.data.Place;
import my.localocal.data.TourGuide;
import my.localocal.gridview.GridViewAdapter;
import my.localocal.gridview.GridViewItem;
import my.localocal.gridview.TourGuideItemView;
import my.localocal.views.listview.ListViewItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class PlaceTourGuidesView implements ListViewItem{

	private final Context	         context;
	private final Place	             place;
	private final List<String>	     addedIds	  = new ArrayList<String>();
	private final List<GridViewItem>	itemsList	= new ArrayList<GridViewItem>();

	public PlaceTourGuidesView(Context context, Place place) {
		this.context = context;
		this.place = place;
		addedIds.clear();
	}

	@Override
	public View createView() {
		View view = LayoutInflater.from(context).inflate(R.layout.place_tour_guides, null);
		PlaceTourGuidesViewHolder holder = new PlaceTourGuidesViewHolder();

		holder.setPlaceTourGuidesTitleTextView((TextView) view.findViewById(R.id.tour_guides__lbl_title));
		holder.setPlaceTourGuidesGridView((GridView) view.findViewById(R.id.tour_guides__gridview));

		setValueToViewHolder(holder);
		view.setTag(holder);
		return view;
	}

	private void setValueToViewHolder(PlaceTourGuidesViewHolder holder) {

		holder.getPlaceTourGuidesTitleTextView().setText("Tour Guides");

		for (String tgid : place.getTourGuideIds()) {
			if (!addedIds.contains(tgid)) {
				addedIds.add(tgid);
				TourGuide tourGuide = DataManager.shared(context).getTourGuideById(tgid);
				if (tourGuide != null) {
					itemsList.add(new TourGuideItemView(context, tourGuide));
				}
			}
		}
		holder.getPlaceTourGuidesGridView().setAdapter(new GridViewAdapter(context, itemsList));
		holder.getPlaceTourGuidesGridView().setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public void convertView(View view) {
		PlaceTourGuidesViewHolder holder = (PlaceTourGuidesViewHolder) view.getTag();
		setValueToViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof PlaceTourGuidesViewHolder;
	}

}

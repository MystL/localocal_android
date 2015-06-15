package my.localocal.views.places;

import android.widget.GridView;
import android.widget.TextView;

public class PlaceTourGuidesViewHolder{

	private TextView	lbl_title;
	private GridView	gridView;

	public PlaceTourGuidesViewHolder() {
		//...
	}

	public TextView getPlaceTourGuidesTitleTextView() {
		return lbl_title;
	}

	public GridView getPlaceTourGuidesGridView() {
		return gridView;
	}

	public void setPlaceTourGuidesTitleTextView(TextView textView) {
		lbl_title = textView;
	}

	public void setPlaceTourGuidesGridView(GridView gridView) {
		this.gridView = gridView;
	}

}

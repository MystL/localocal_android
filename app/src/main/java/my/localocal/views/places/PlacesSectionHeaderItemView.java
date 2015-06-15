package my.localocal.views.places;

import my.localocal.R;
import my.localocal.views.listview.ListViewItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class PlacesSectionHeaderItemView implements ListViewItem{

	private final Context	              context;
	private final PlacesSectionHeaderItem	headerItem;

	public PlacesSectionHeaderItemView(Context context, PlacesSectionHeaderItem headerItem) {
		this.context = context;
		this.headerItem = headerItem;
	}

	@Override
	public View createView() {
		View view = LayoutInflater.from(context).inflate(R.layout.places_section_header_item, null);

		PlacesSectionHeaderItemViewHolder holder = new PlacesSectionHeaderItemViewHolder();

		holder.setPlacesSectionHeaderTextView((TextView) view.findViewById(R.id.places_section_header__lbl_title));
		setValueToItemViewHolder(holder);
		view.setTag(view);
		return view;
	}

	private void setValueToItemViewHolder(PlacesSectionHeaderItemViewHolder holder) {
		holder.getPlacesSectionHeaderTextView().setText(headerItem.getSectionHeaderText());
	}

	@Override
	public void convertView(View view) {
		PlacesSectionHeaderItemViewHolder holder = (PlacesSectionHeaderItemViewHolder) view.getTag();
		setValueToItemViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof PlacesSectionHeaderItemViewHolder;
	}

}

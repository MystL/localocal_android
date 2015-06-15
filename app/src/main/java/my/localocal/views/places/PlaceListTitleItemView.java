package my.localocal.views.places;

import my.localocal.R;
import my.localocal.views.listview.ListViewItem;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class PlaceListTitleItemView implements ListViewItem{

	private final Context	context;
	private final String	title;
	private Typeface	  roboto_typeface;

	public PlaceListTitleItemView(Context context, String title) {
		this.context = context;
		this.title = title;

		try {
			roboto_typeface = Typeface.createFromAsset(context.getAssets(), "RobotoThin.ttf");
		} catch (Throwable e) {
			//...
		}

	}

	@Override
	public View createView() {
		View view = LayoutInflater.from(context).inflate(R.layout.place_list_title_item, null);

		PlaceListTitleItemViewHolder holder = new PlaceListTitleItemViewHolder();

		holder.setPlaceListTitleItemTextView((TextView) view.findViewById(R.id.place_list_title__lbl_title));

		setValueToItemViewHolder(holder);
		view.setTag(holder);
		return view;
	}

	private void setValueToItemViewHolder(PlaceListTitleItemViewHolder holder) {
		holder.getPlaceListTitleTextView().setText(title);
		holder.getPlaceListTitleTextView().setTypeface(roboto_typeface);
	}

	@Override
	public void convertView(View view) {
		PlaceListTitleItemViewHolder holder = (PlaceListTitleItemViewHolder) view.getTag();
		setValueToItemViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof PlaceListTitleItemViewHolder;
	}

}

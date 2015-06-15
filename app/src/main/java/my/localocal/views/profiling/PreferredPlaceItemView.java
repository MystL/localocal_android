package my.localocal.views.profiling;

import my.localocal.R;
import my.localocal.circularimageview.RoundedImageView;
import my.localocal.data.PreferredOption;
import my.localocal.gridview.GridViewItem;
import my.localocal.theme.ThemeHandler;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class PreferredPlaceItemView implements GridViewItem{

	private final Context	             context;
	private final PreferredOption	     prefOption;
	private final ThemeHandler	         themeHandler;
	private PreferredPlaceItemViewHolder	viewHolder;
	private boolean	                     isItemSelected	= false;
	private boolean	                     isInPrefs	    = false;

	public PreferredPlaceItemView(Context context, PreferredOption prefPlace, boolean isInPrefs) {
		this.context = context;
		this.prefOption = prefPlace;
		this.isInPrefs = isInPrefs;
		isItemSelected = isInPrefs;
		themeHandler = new ThemeHandler(context);

	}

	@Override
	public View createView() {

		View view = LayoutInflater.from(context).inflate(R.layout.preferred_place_item_layout, null);
		PreferredPlaceItemViewHolder holder = new PreferredPlaceItemViewHolder();

		holder.setPreferredPlaceItemIconImageView((RoundedImageView) view.findViewById(R.id.preferred_place__img_option));
		holder.setPreferredPlaceItemTitleTextView((TextView) view.findViewById(R.id.preferred_place__lbl_title));

		setValueToItemViewHolder(holder);
		view.setTag(holder);

		return view;
	}

	private void setValueToItemViewHolder(PreferredPlaceItemViewHolder holder) {

		viewHolder = holder;

		if (isInPrefs) {
			holder.getPreferredPlaceItemIconImageView().setImageDrawable(themeHandler.getImageDrawable(prefOption.getOptionThumbnail() + "_h"));
		} else {
			holder.getPreferredPlaceItemIconImageView().setImageDrawable(themeHandler.getImageDrawable(prefOption.getOptionThumbnail()));
		}
		holder.getPreferredPlaceItemTitleTextView().setText(prefOption.getOptionName());
	}

	@Override
	public void convertView(View view) {
		PreferredPlaceItemViewHolder holder = (PreferredPlaceItemViewHolder) view.getTag();
		setValueToItemViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof PreferredPlaceItemViewHolder;
	}

	public PreferredOption getOption() {
		return prefOption;
	}

	public boolean setItemSelected() {
		if (!isItemSelected) {
			viewHolder.getPreferredPlaceItemIconImageView().setImageDrawable(themeHandler.getImageDrawable(prefOption.getOptionThumbnail() + "_h"));
			isItemSelected = true;
		} else {
			viewHolder.getPreferredPlaceItemIconImageView().setImageDrawable(themeHandler.getImageDrawable(prefOption.getOptionThumbnail()));
			isItemSelected = false;
		}
		return isItemSelected;
	}

}

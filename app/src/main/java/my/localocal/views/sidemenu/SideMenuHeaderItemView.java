package my.localocal.views.sidemenu;

import my.localocal.R;
import my.localocal.views.listview.ListViewItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class SideMenuHeaderItemView implements ListViewItem{

	private final Context	         context;
	private final SideMenuHeaderItem	sideMenuHeaderItem;

	public SideMenuHeaderItemView(Context context, SideMenuHeaderItem sideMenuHeaderItem) {
		this.context = context;
		this.sideMenuHeaderItem = sideMenuHeaderItem;
	}

	@Override
	public View createView() {
		View view = LayoutInflater.from(context).inflate(R.layout.side_menu_header_item, null);

		SideMenuHeaderItemViewHolder holder = new SideMenuHeaderItemViewHolder();

		holder.setTextViewMenuHeader((TextView) view.findViewById(R.id.side_menu_header__lbl_title));
		setValueToItemViewHolder(holder);
		view.setTag(holder);
		return view;
	}

	private void setValueToItemViewHolder(SideMenuHeaderItemViewHolder holder) {
		holder.getTextViewMenuHeader().setText(sideMenuHeaderItem.getSideMenuHeaderText());
	}

	@Override
	public void convertView(View view) {
		SideMenuHeaderItemViewHolder holder = (SideMenuHeaderItemViewHolder) view.getTag();
		setValueToItemViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof SideMenuHeaderItemViewHolder;
	}

	public SideMenuHeaderItem getSideMenuHeaderItem() {
		return sideMenuHeaderItem;
	}

}

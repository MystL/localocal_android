package my.localocal.views.sidemenu;

import my.localocal.R;
import my.localocal.views.listview.ListViewItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SideMenuItemView implements ListViewItem{

	private final Context	   context;
	private final SideMenuItem	sideMenuItem;

	public SideMenuItemView(Context context, SideMenuItem sideMenuitem) {
		this.context = context;
		this.sideMenuItem = sideMenuitem;
	}

	@Override
	public View createView() {
		LayoutInflater mInflate = LayoutInflater.from(context);
		View view = mInflate.inflate(R.layout.side_menu_item, null);

		SideMenuItemViewHolder holder = new SideMenuItemViewHolder();

		holder.setTextViewMenuText((TextView) view.findViewById(R.id.side_menu__lbl_title));
		holder.setImageViewMenuIcon((ImageView) view.findViewById(R.id.side_menu__img_icon));

		setValueToItemViewHolder(holder);
		view.setTag(holder);
		return view;
	}

	private void setValueToItemViewHolder(SideMenuItemViewHolder holder) {
		holder.getImageViewMenuIcon().setImageResource(sideMenuItem.getSideMenuIconId());
		holder.getTextViewMenuText().setText(sideMenuItem.getSideMenuItemText());
	}

	@Override
	public void convertView(View view) {
		SideMenuItemViewHolder holder = (SideMenuItemViewHolder) view.getTag();
		setValueToItemViewHolder(holder);
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof SideMenuItemViewHolder;
	}

	public SideMenuItem getSideMenuItem() {
		return sideMenuItem;
	}

}

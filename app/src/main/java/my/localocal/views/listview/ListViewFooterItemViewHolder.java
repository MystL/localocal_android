package my.localocal.views.listview;

import android.view.View;

public class ListViewFooterItemViewHolder{

	private View	footer_view;

	public ListViewFooterItemViewHolder() {
		// ...
	}

	public void setListViewFooterItemView(View view) {
		footer_view = view;
	}

	public View getListViewFooterItemView() {
		return footer_view;
	}
}

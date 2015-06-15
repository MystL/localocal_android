package my.localocal.views.listview;

import my.localocal.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class ListViewFooterItemView implements ListViewItem{

	private final Context	context;

	public ListViewFooterItemView(Context context) {
		this.context = context;
	}

	@Override
	public View createView() {
		View view = LayoutInflater.from(context).inflate(R.layout.common_list_emptyfooter, null);
		ListViewFooterItemViewHolder holder = new ListViewFooterItemViewHolder();
		holder.setListViewFooterItemView(view.findViewById(R.id.empty_footer));
		view.setTag(holder);
		return view;
	}

	@Override
	public void convertView(View view) {
		ListViewFooterItemViewHolder holder = (ListViewFooterItemViewHolder) view.getTag();
	}

	@Override
	public boolean isConvertable(View view) {
		return view.getTag() instanceof ListViewFooterItemViewHolder;
	}

}

package my.localocal.gridview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GridViewAdapter extends BaseAdapter{

	private final Context	   context;
	private List<GridViewItem>	itemsList	= new ArrayList<GridViewItem>();

	public GridViewAdapter(Context context, List<GridViewItem> itemsList) {
		this.context = context;
		this.itemsList = itemsList;
	}

	@Override
	public int getCount() {
		return itemsList.size();
	}

	@Override
	public Object getItem(int position) {
		return position >= 0 && position < itemsList.size() ? itemsList.get(position) : null;
	}

	@Override
	public boolean isEnabled(int position) {
		return position >= 0 && position < itemsList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public synchronized void onDataSetUpdated() {
		this.notifyDataSetChanged();
	}

	public GridViewItem getSelectedItem(int pos) {
		if (itemsList.size() >= 0) {
			GridViewItem item = itemsList.get(pos);
			if (item != null) {
				return item;
			}
		}
		return null;
	}

	@Override
	public View getView(int position, View _convertView, ViewGroup parent) {
		View convertView = _convertView;
		GridViewItem item = itemsList.get(position);
		if (convertView == null || !item.isConvertable(convertView)) {
			convertView = item.createView();
			return convertView;
		}
		item.convertView(convertView);
		return convertView;
	}
}

package my.localocal.views.listview;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListViewAdapter extends BaseAdapter{

	private List<ListViewItem>	listItems;

	public ListViewAdapter(List<ListViewItem> _listItems) {
		if (_listItems == null) {
			throw new NullPointerException("Cannot Initialize Adapter With Null List");
		}
		listItems = _listItems;
	}

	public ListViewAdapter() {
		listItems = new ArrayList<ListViewItem>();
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return position >= 0 && position < listItems.size() ? listItems.get(position) : null;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return position >= 0 && position < listItems.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public synchronized void onDataSetUpdated() {
		this.notifyDataSetChanged();
	}

	public ListViewItem getSelectedItem(int pos) {
		if (listItems.size() >= 0) {
			ListViewItem item = listItems.get(pos);
			if (item != null) {
				return item;
			}
		}
		return null;
	}

	@Override
	public View getView(int position, View _convertView, ViewGroup parent) {
		View convertView = _convertView;
		ListViewItem item = listItems.get(position);
		if (convertView == null || !item.isConvertable(convertView)) {
			convertView = item.createView();
			return convertView;
		}
		item.convertView(convertView);
		return convertView;
	}

	public void setItems(List<ListViewItem> items) {
		if (items == null) {
			throw new NullPointerException("Cannot Set List With Null Value");
		}
		this.listItems = items;
	}

}

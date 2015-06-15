package my.localocal.views.listview;

import android.view.View;

public interface ListViewItem{

	public View createView();

	public void convertView(View view);

	boolean isConvertable(View view);
}

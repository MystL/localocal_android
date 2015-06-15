package my.localocal.gridview;

import android.view.View;

public interface GridViewItem{

	public View createView();

	public void convertView(View view);

	boolean isConvertable(View view);

}

package my.localocal.views.sidemenu;

import android.widget.TextView;

public class SideMenuHeaderItemViewHolder{

	private TextView	lbl_menu_header;

	public SideMenuHeaderItemViewHolder() {
		//...
	}

	public void setTextViewMenuHeader(TextView textView) {
		this.lbl_menu_header = textView;
	}

	public TextView getTextViewMenuHeader() {
		return lbl_menu_header;
	}

}

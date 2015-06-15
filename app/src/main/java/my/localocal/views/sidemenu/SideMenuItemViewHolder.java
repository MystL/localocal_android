package my.localocal.views.sidemenu;

import android.widget.ImageView;
import android.widget.TextView;

public class SideMenuItemViewHolder{

	private ImageView	img_menu_icon;
	private TextView	lbl_menu_text;

	public SideMenuItemViewHolder() {
		//...
	}

	public ImageView getImageViewMenuIcon() {
		return img_menu_icon;
	}

	public void setImageViewMenuIcon(ImageView img_menu_icon) {
		this.img_menu_icon = img_menu_icon;
	}

	public TextView getTextViewMenuText() {
		return lbl_menu_text;
	}

	public void setTextViewMenuText(TextView lbl_menu_text) {
		this.lbl_menu_text = lbl_menu_text;
	}

}

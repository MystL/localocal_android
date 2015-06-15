package my.localocal.views.places;

import android.view.View;
import android.widget.ImageView;

public class PlaceMapLocationViewHolder{

	private ImageView	img_map;

	public PlaceMapLocationViewHolder() {
		// ...
	}

	public ImageView getMapLocationImageView() {
		return img_map;
	}

	public void setMapLocationImageView(ImageView imageView) {

		this.img_map = imageView;
		this.img_map.setEnabled(true);
		this.img_map.setVisibility(View.VISIBLE);

	}

}

package my.localocal.views.places;

import my.localocal.R;
import my.localocal.custom.ParallaxImageView;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class NewPlaceBaseView extends Fragment{

	private ParallaxImageView	img_place	= null;
	private ListView	      listView	  = null;

	public NewPlaceBaseView() {
		//...
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.new_place_view_layout, container, false);

		img_place = (ParallaxImageView) view.findViewById(R.id.new_place__img_place);
		listView = (ListView) view.findViewById(R.id.new_place__listview);

		return view;
	}

}

package my.localocal.views;

import my.localocal.MainActivity;
import my.localocal.R;
import my.localocal.booking.TourGuidesViewingActivity;
import my.localocal.data.Place;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class NavbarViewController{

	private final ImageButton	   btn_menu;
	private final ImageButton	   btn_bookTour;
	private final TextView	       lbl_title;
	private final ImageView	       img_bg;
	private final View	           viewController, navbar_layout;
	private final FragmentActivity	fragmentActivity;
	private final MainActivity	   mainActivity;
	private MainViewController	   mainVC;
	private Place	               place	= null;
	private Typeface	           robotoFont;

	public NavbarViewController(final FragmentActivity fragmentActivity, View controllerView) {

		this.fragmentActivity = fragmentActivity;
		mainActivity = (MainActivity) fragmentActivity;

		try {
			robotoFont = Typeface.createFromAsset(fragmentActivity.getAssets(), "RobotoCondensedItalic.ttf");
		} catch (Throwable e) {
			//...
		}

		viewController = controllerView;
		viewController.bringToFront();

		navbar_layout = controllerView.findViewById(R.id.navbar__layout);

		img_bg = (ImageView) controllerView.findViewById(R.id.navbar__img_bg);
		img_bg.setImageResource(R.drawable.img__bg_gradiant);

		btn_menu = (ImageButton) controllerView.findViewById(R.id.navbar__btn_menu);
		btn_menu.setImageResource(R.drawable.ic_action_drawer);
		btn_menu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				mainVC = mainActivity.getMainVC();
				mainVC.toggleMenu();
			}
		});

		lbl_title = (TextView) controllerView.findViewById(R.id.navbar__lbl_title);
		lbl_title.setTypeface(robotoFont);

		btn_bookTour = (ImageButton) controllerView.findViewById(R.id.navbar__btn_booktour);
		btn_bookTour.setImageResource(R.drawable.ic_action_new);
		btn_bookTour.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if (place != null) {
					Intent viewTourGuideActivity = new Intent(fragmentActivity, TourGuidesViewingActivity.class);
					viewTourGuideActivity.putExtra(MainActivity.VIEW_TOUR_GUIDE_BUNDLE_PLACE, place.toString());
					fragmentActivity.startActivity(viewTourGuideActivity);
				}

			}
		});
	}

	public void setTitle(String text, boolean toShow) {
		if (toShow) {
			lbl_title.setVisibility(View.VISIBLE);
			lbl_title.setText(text);
		} else {
			lbl_title.setVisibility(View.GONE);
			lbl_title.setText("");
		}

	}

	public void setCurrentPlace(Place place) {
		this.place = place;
	}

	public View getViewController() {
		return viewController;
	}

	public void setBookTourButtonVisibility(boolean bShow) {
		if (bShow) {
			btn_bookTour.setVisibility(View.VISIBLE);
		} else {
			btn_bookTour.setVisibility(View.GONE);
		}
	}

	public void setNavbarDarkerBackground(boolean bShow) {
		if (bShow) {
			navbar_layout.setBackgroundColor(0);
		} else {
			navbar_layout.setBackgroundColor(fragmentActivity.getResources().getColor(R.color.BlackTransluscent));
		}
	}

}

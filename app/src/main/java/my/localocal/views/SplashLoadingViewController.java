package my.localocal.views;

import my.localocal.R;
import my.localocal.theme.ThemeHandler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class SplashLoadingViewController{

	private final View	       viewController;
	private final ImageView	   img_bg, img_logo;
	private final ProgressBar	loading_circle;
	private final ThemeHandler	themeHandler;

	public SplashLoadingViewController(final FragmentActivity mainActivity, View viewController) {
		this.viewController = viewController;
		themeHandler = new ThemeHandler(mainActivity);

		img_bg = (ImageView) viewController.findViewById(R.id.loading__bg);
		img_bg.setImageResource(R.drawable.bg__img_splash);

		img_logo = (ImageView) viewController.findViewById(R.id.loading__img_logo);
		img_logo.setImageResource(R.drawable.img__logo);

		loading_circle = (ProgressBar) viewController.findViewById(R.id.loading__circle);
		loading_circle.setVisibility(View.VISIBLE);

	}

	public View getViewController() {
		return viewController;
	}

	public ProgressBar getProgressBar() {
		return loading_circle;
	}

	public void deflate() {
		unbindDrawables(viewController);
	}

	//Release bind to resources
	private void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}

		if (view instanceof ImageView) {
			ImageView imgView = ((ImageView) view);
			if (imgView.getDrawable() != null) {
				imgView.getDrawable().setCallback(null);
				imgView.setImageDrawable(null);
				return;
			}
		}

		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			try {
				((ViewGroup) view).removeAllViews();
			} catch (UnsupportedOperationException e) {

				//Some views like AdapterView does not support this operation
			}
		}
	}

}

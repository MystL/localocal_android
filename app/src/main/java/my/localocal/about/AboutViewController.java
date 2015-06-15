package my.localocal.about;

import java.util.HashSet;

import my.localocal.R;
import my.localocal.theme.ThemeHandler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutViewController extends Fragment{

	private ImageView	          img_bg, img_logo;
	private TextView	          lbl_about_alpha, lbl_about_beta;
	private ViewGroup	          lyt_about_paragraph;
	private ThemeHandler	      themeHandler;
	private final HashSet<String>	about_paragraph	= new HashSet<String>();

	public AboutViewController() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.about, null);

		themeHandler = new ThemeHandler(getActivity());

		img_bg = (ImageView) rootView.findViewById(R.id.about__img_bg);
		img_bg.setImageDrawable(themeHandler.getImageDrawable("img_about"));

		img_logo = (ImageView) rootView.findViewById(R.id.about__img_logo);
		img_logo.setImageDrawable(themeHandler.getImageDrawable("img__logo_about"));

		lbl_about_alpha = (TextView) rootView.findViewById(R.id.about__lbl_about_title);
		lbl_about_alpha.setText(getText(R.string.about_alpha));

		lyt_about_paragraph = (LinearLayout) rootView.findViewById(R.id.about__paragraph_layout);

		about_paragraph.add("- Set up your discovery profile");
		about_paragraph.add("- Discover different attractions");
		about_paragraph.add("- Add destinations to favorite");
		about_paragraph.add("- Filter tour guides account to time, budget and travel date");
		about_paragraph.add("- Book tour guides (mock up)");
		about_paragraph.add("- Check booking status");

		for (String text : about_paragraph) {
			TextView tv = new TextView(getActivity());
			tv.setText(text);
			tv.setTextColor(getResources().getColor(R.color.White));
			tv.setTextSize(16F);
			lyt_about_paragraph.addView(tv);
		}

		lbl_about_beta = (TextView) rootView.findViewById(R.id.about__lbl_about_beta);
		lbl_about_beta.setText(getText(R.string.about_beta));

		return rootView;
	}

}

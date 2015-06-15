package my.localocal.account;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import my.localocal.DataManager;
import my.localocal.MainActivity;
import my.localocal.R;
import my.localocal.data.FacebookUser;
import my.localocal.data.PreferredOption;
import my.localocal.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.mikhaellopez.circularimageview.CircularImageView;

public class UserAccountViewController extends Fragment{

	private ImageView	      img_bg;
	private View	          lyt_accountInfo;
	private CircularImageView	img_profile;
	//private ImageView	      img_accountType;
	private TextView	      lbl_nameTitle;
	private TextView	      lbl_name;
	private TextView	      lbl_preferenceTitle;
	private TextView	      lbl_preference;
	private TextView	      lbl_noAccount;
	private FacebookUser	  user_account;

	public UserAccountViewController() {
		super();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.account, null);

		img_bg = (ImageView) rootView.findViewById(R.id.account__img_bg);
		lyt_accountInfo = rootView.findViewById(R.id.account__info_layout);
		img_profile = (CircularImageView) rootView.findViewById(R.id.account__img_profile);
		//img_accountType = (ImageView) rootView.findViewById(R.id.account__img_account_type);
		lbl_nameTitle = (TextView) rootView.findViewById(R.id.account__lbl_nameTitle);
		lbl_name = (TextView) rootView.findViewById(R.id.account__lbl_name);
		lbl_preferenceTitle = (TextView) rootView.findViewById(R.id.account__lbl_preferenceTitle);
		lbl_preference = (TextView) rootView.findViewById(R.id.account__lbl_preference);
		lbl_noAccount = (TextView) rootView.findViewById(R.id.account__lbl_noaccount);

		img_bg.setImageResource(R.drawable.bg__img_mt_panorama);

		if (!StringUtils.IsNullOrEmpty(readStoredFacebookProfile())) {
			JSONObject json_profile = null;
			try {
				json_profile = new JSONObject(readStoredFacebookProfile());
				user_account = new FacebookUser(json_profile);

				if (user_account != null) {
					AQuery aQuery = new AQuery(img_profile);
					aQuery.image(user_account.getProfileImage());
					//img_accountType.setImageResource(R.drawable.ic_facebook);
					lbl_nameTitle.setText("Account Name");
					lbl_name.setText(user_account.getFullName());
					lbl_noAccount.setVisibility(View.INVISIBLE);
				} else {
					lyt_accountInfo.setVisibility(View.GONE);
					lbl_noAccount.setVisibility(View.VISIBLE);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			if (readStoredOptionsIds() != null) {
				lbl_preferenceTitle.setText("Preferred Attractions :");
				if (!readStoredOptionsIds().isEmpty()) {
					List<String> options = new ArrayList<String>();
					for (String id : readStoredOptionsIds()) {
						PreferredOption option = DataManager.shared(getActivity()).getOptionById(id);
						options.add(option.getOptionName());
					}
					lbl_preference.setText(TextUtils.join(", ", options));
				} else {
					lbl_preference.setText("Not Set");
				}
			} else {
				lbl_preference.setText("Not Set");
			}

		} else {
			lyt_accountInfo.setVisibility(View.GONE);
			lbl_noAccount.setVisibility(View.VISIBLE);
		}

		return rootView;
	}

	private Set<String> readStoredOptionsIds() {
		SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		return sharedPrefs.getStringSet(MainActivity.PREF_OPTIONS_ID_KEY, null);
	}

	private String readStoredFacebookProfile() {
		SharedPreferences sharedPrefs = getActivity().getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		return sharedPrefs.getString(MainActivity.PREF_FACEBOOK_USER_KEY, null);
	}

	@Override
	public void onStop() {
		super.onStop();

		img_bg.setImageBitmap(null);

	}

}

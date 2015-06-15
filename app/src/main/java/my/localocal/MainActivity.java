package my.localocal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashSet;

import my.localocal.data.FacebookUser;
import my.localocal.theme.ThemeHandler;
import my.localocal.utils.DataUtils;
import my.localocal.views.MainViewController;
import my.localocal.views.SplashLoadingViewController;
import my.localocal.views.places.PlaceListViewController;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.NewPermissionsRequest;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.FacebookDialog.PendingCall;
import com.facebook.widget.LoginButton;

public class MainActivity extends FragmentActivity{

	// Bundle key identifier
	public static final String	        VIEW_TOUR_GUIDE_BUNDLE_PLACE	       = "bundle.extra.place";
	public static final String	        TOUR_GUIDE_BOOKING_BUNDLE_TOURGUIDE	   = "bundle.extra.tourguide";

	// Preference key identifier
	public static final String	        PREFS_KEY	                           = "localocal.prefs";
	public static final String	        PREF_OPTIONS_ID_KEY	                   = "preferred.options";
	public static final String	        PREF_FACEBOOK_USER_KEY	               = "facebook.user";
	public static final String	        PREF_FAVOURITES_PLACE_KEY	           = "favourites.place";
	public static final String	        PREF_TOUR_BOOKINGS_KEY	               = "booking.tour";
	public static final String	        TOUR_GUIDE_BOOKING_TOUR_YEAR	       = "booking.year";
	public static final String	        TOUR_GUIDE_BOOKING_TOUR_MONTH	       = "booking.month";
	public static final String	        TOUR_GUIDE_BOOKING_TOUR_DATE	       = "booking.date";

	// Facebook Permission Request Key
	public static final String	        FACEBOOK_PUBLISH_STREAM_PERMISSION_KEY	= "publish_stream";
	public static final String	        FACEBOOK_USER_BIRTHDAY_PERMISSION_KEY	   = "user_birthday";

	private MainViewController	        mainView;
	private SplashLoadingViewController	splashView;
	private final Handler	            mHandler	                           = new Handler();
	private UiLifecycleHelper	        fbUiLifecycleHelper;
	private ThemeHandler	            themeHandler	                       = null;

	// Login Layout Elements
	private LoginButton	                btn_facebook;
	private ImageButton	                btn_cancel;
	private View	                    login_layout;

	// Double press back elements
	private static final int	        PRESS_BACK_TIME	                       = 3000;
	private long	                    lastBackPressTime	                   = 0;
	private Toast	                    confirmExitToast;
	private boolean	                    msgShown	                           = false;

	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			setTheme(android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
		} else {
			setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		}

		setContentView(R.layout.app);
		themeHandler = new ThemeHandler(this);
		createMainView();
		mainView.getMainViewControllerView().setVisibility(View.GONE);

		fbUiLifecycleHelper = new UiLifecycleHelper(this, new Session.StatusCallback(){
			@SuppressWarnings("deprecation")
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					
					Request.newMeRequest(session, new GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if(user != null){
								Log.i("XXX", DataUtils.getStringValue(user));
							}
							
						}
					}).executeAsync();
					
					if (getSupportFragmentManager() != null) {
						FragmentManager fm = getSupportFragmentManager();
						for (Fragment fragment : fm.getFragments()) {
							if (fragment instanceof PlaceListViewController) {
								PlaceListViewController vc = (PlaceListViewController) fragment;
								if (vc.getTopInfoView().isToShare()) {
									if (Session.getActiveSession().getPermissions().contains(FACEBOOK_PUBLISH_STREAM_PERMISSION_KEY)) {
										vc.getTopInfoView().shareItemViaFacebook();
									} else {
										Session.getActiveSession().requestNewPublishPermissions(new NewPermissionsRequest(MainActivity.this, FACEBOOK_PUBLISH_STREAM_PERMISSION_KEY));
									}
								}

							}
						}
					}

					removeSplashLoginView();
					if (!msgShown) {
						Toast.makeText(MainActivity.this, "Logged into Facebook", Toast.LENGTH_SHORT).show();
						msgShown = true;
					}
					
					
					
//					if (session.getPermissions().contains(FACEBOOK_USER_BIRTHDAY_PERMISSION_KEY)) {
//						Request.executeMeRequestAsync(session, new GraphUserCallback(){
//							@Override
//							public void onCompleted(GraphUser user, Response response) {
//								if (user != null) {
//									Log.i("XXX", DataUtils.getStringValue(user));
//									FacebookUser facebookUser = new FacebookUser(user.getInnerJSONObject());
//									if (getFacebookUserProfile() == null) {
//										storeToPrefs(PREF_FACEBOOK_USER_KEY, facebookUser.toString());
//									}
//								}
//							}
//						});
//					}
				}

				if (exception != null) {
					//removeSplashLoginView();
					//Toast.makeText(MainActivity.this, "Something went wrong, can't login to Facebook :/", Toast.LENGTH_SHORT).show();
					exception.printStackTrace();
				}
			}
		});
		fbUiLifecycleHelper.onCreate(savedInstanceState);

		//		PackageInfo info;
		//		try {
		//			info = getPackageManager().getPackageInfo("my.localocal", PackageManager.GET_SIGNATURES);
		//			for (Signature signature : info.signatures) {
		//				MessageDigest md;
		//				md = MessageDigest.getInstance("SHA");
		//				md.update(signature.toByteArray());
		//				String something = new String(Base64.encode(md.digest(), 0));
		//				//String something = new String(Base64.encodeBytes(md.digest()));
		//				Log.e("hash key", something);
		//				Toast.makeText(getApplicationContext(), something, Toast.LENGTH_LONG).show();
		//			}
		//		} catch (NameNotFoundException e1) {
		//			Log.e("name not found", e1.toString());
		//		} catch (NoSuchAlgorithmException e) {
		//			Log.e("no such an algorithm", e.toString());
		//		} catch (Exception e) {
		//			Log.e("exception", e.toString());
		//		}

		login_layout = findViewById(R.id.loading__login_layout);
		btn_facebook = (LoginButton) findViewById(R.id.loading__btn_facebook);
		btn_facebook.setReadPermissions(Arrays.asList(FACEBOOK_USER_BIRTHDAY_PERMISSION_KEY));
		btn_facebook.setBackgroundResource(0);
		btn_facebook.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		btn_facebook.setBackgroundDrawable(themeHandler.getButtonDrawable("login__btn_facebook"));

		btn_cancel = (ImageButton) findViewById(R.id.loading__btn_cancel);
		btn_cancel.setImageDrawable(themeHandler.getButtonDrawable("login__btn_perhapslater"));
		btn_cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				removeSplashLoginView();
			}
		});

		splashView = new SplashLoadingViewController(this, findViewById(R.id.app_loading));
		splashView.getViewController().setVisibility(View.VISIBLE);
		if (Session.getActiveSession() != null) {
			if (Session.getActiveSession().isOpened()) {
				removeSplashLoginView();
			} else {
				showFacebookLogin();
			}
		}

	}

	public void createMainView() {
		mainView = new MainViewController(this, findViewById(R.id.app_main_view));
		mainView.getMainViewControllerView().setVisibility(View.VISIBLE);
	}

	private void removeSplashLoginView() {
		login_layout.setVisibility(View.GONE);
		mainView.getMainViewControllerView().setVisibility(View.VISIBLE);
		if (splashView != null) {
			splashView.getViewController().setVisibility(View.GONE);
			((ViewGroup) mainView.getMainViewControllerView()).removeView(splashView.getViewController());
			splashView.deflate();
			splashView = null;
		}
	}

	private void showFacebookLogin() {

		if (splashView != null) {
			splashView.getProgressBar().setVisibility(View.GONE);
		}

		login_layout.setVisibility(View.VISIBLE);

	}

	public MainViewController getMainVC() {
		return mainView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

		fbUiLifecycleHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback(){
			@Override
			public void onComplete(PendingCall pendingCall, Bundle data) {
				Log.i("Activity", "Success!");
			}

			@Override
			public void onError(PendingCall pendingCall, Exception error, Bundle data) {
				Log.e("Activity", String.format("Error: %s", error.toString()));
			}

		});
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();

	}

	public Handler getHandler() {
		return mHandler;
	}

	@Override
	public void onResume() {
		super.onResume();
		fbUiLifecycleHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		fbUiLifecycleHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		fbUiLifecycleHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		fbUiLifecycleHelper.onSaveInstanceState(outState);
	}

	public static String convertStreamToString(InputStream is) throws IOException {
		Writer writer = new StringWriter();

		char[] buffer = new char[2048];
		try {
			Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			int n;
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
		} finally {
			is.close();
		}
		String text = writer.toString();
		return text;
	}

	@SuppressWarnings("unchecked")
	private void storeToPrefs(String key, Object value) {

		SharedPreferences sharedPrefs = getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = sharedPrefs.edit();

		if (value instanceof String) {
			prefsEditor.putString(key, value.toString());
		} else if (value instanceof Boolean) {
			prefsEditor.putBoolean(key, ((Boolean) value).booleanValue());
		} else if (value instanceof HashSet) {
			HashSet<String> valueList = (HashSet<String>) value;
			prefsEditor.putStringSet(key, valueList);
		}

		boolean isCommited = prefsEditor.commit();
		if (!isCommited) {
			throw new RuntimeException("Unable to save into SharedPreferences.");
		}

	}

	private String getFacebookUserProfile() {
		SharedPreferences sharedPrefs = getSharedPreferences(MainActivity.PREFS_KEY, Context.MODE_PRIVATE);
		return sharedPrefs.getString(MainActivity.PREF_FACEBOOK_USER_KEY, null);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		//		Log.i("XXX", DataUtils.getStringValue(getSupportFragmentManager().getBackStackEntryCount()));
		//
		//		if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
		//			getSupportFragmentManager().popBackStackImmediate();
		//			return true;
		//		} else {
		if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) && event.getRepeatCount() == 0) {
			if (this.lastBackPressTime < System.currentTimeMillis() - PRESS_BACK_TIME) {
				confirmExitToast = Toast.makeText(this, "Press back again to exit", PRESS_BACK_TIME);
				confirmExitToast.show();
				this.lastBackPressTime = System.currentTimeMillis();
			} else {
				if (confirmExitToast != null) {
					confirmExitToast.cancel();
				}
				this.finish();
			}
			return true;
		}
		//		}

		return super.onKeyDown(keyCode, event);
	}

}

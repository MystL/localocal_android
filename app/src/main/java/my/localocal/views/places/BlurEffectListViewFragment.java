package my.localocal.views.places;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import my.localocal.R;
import my.localocal.blureffect.Blur;
import my.localocal.blureffect.ImageUtils;
import my.localocal.views.listview.ListViewAdapter;
import my.localocal.views.listview.ListViewItem;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;

public abstract class BlurEffectListViewFragment extends ListFragment{

	private float	                 alpha;
	private int	                     screenWidth;
	private final List<ListViewItem>	itemsList	= new ArrayList<ListViewItem>();
	protected ListViewAdapter	     adapter	  = null;
	private ImageView	             img_bg_blurred;
	private ImageView	             img_bg_normal;
	private Bitmap	                 bmpNormal;
	private Bitmap	                 bmpBlurred;
	private Animation	             fadeOut, fadeIn;

	public BlurEffectListViewFragment() {
		// ...
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		screenWidth = ImageUtils.getScreenWidth(activity);
		fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
		fadeIn = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		final View headerView = new View(getActivity());
		headerView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, getTopHeight()));
		headerView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		getListView().addHeaderView(headerView);

		if (adapter == null) {
			adapter = new ListViewAdapter(getListItem());
		}
		setListAdapter(adapter);

		getListView().setOnScrollListener(new OnScrollListener(){
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//...
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// Calculate the ratio between the scroll amount and the list
				// header weight to determinate the top picture alpha
				alpha = (float) -headerView.getTop() / (float) getTopHeight();
				//Apply a ceil
				if (alpha > 1) {
					alpha = 1;
				}
				img_bg_blurred.setAlpha(alpha);
				img_bg_blurred.setColorFilter(adjustAlpha(Color.BLACK, 0.6f), Mode.SRC_ATOP);

			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.place_listview_layout, container, false);

		img_bg_blurred = (ImageView) view.findViewById(R.id.img__bg_blurred);
		img_bg_normal = (ImageView) view.findViewById(R.id.img__bg_normal);

		setBackgroundImage(getInitialBackgroundImageName(), false);

		return view;
	}

	private synchronized void updateView(int screenWidthValue, String img_fileName) {
		if (getActivity() != null) {
			//attempt to decode from Drawable folder
			Bitmap newBlurred = BitmapFactory.decodeFile(getActivity().getFilesDir() + img_fileName + ".jpg");
			//if not null => image is successfully decoded
			if (newBlurred != null) {
				if (bmpBlurred != null) {
					bmpBlurred.recycle();
					bmpBlurred = null;
				}
				bmpBlurred = Bitmap.createScaledBitmap(newBlurred, screenWidthValue, (int) (newBlurred.getHeight() * ((float) screenWidthValue) / newBlurred.getWidth()), false);
			} else {
				//TODO - temp
			}
			img_bg_blurred.setImageBitmap(bmpBlurred);
		}

	}

	private int adjustAlpha(int color, float factor) {
		int colorAlpha = Math.round(Color.alpha(color) * factor);
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		return Color.argb(colorAlpha, red, green, blue);
	}

	private synchronized void generateBlurBackgroundImage(final String img_fileName) {

		final int screenWidthValue = screenWidth;
		final int bgImgResId = getActivity().getResources().getIdentifier(img_fileName, "drawable", getActivity().getPackageName());
		if (bgImgResId != 0) {
			final File blurredImage = new File(getActivity().getFilesDir() + img_fileName + ".jpg");
			if (!blurredImage.exists()) {
				new Thread(new Runnable(){
					@Override
					public void run() {
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inSampleSize = 4;
						Bitmap image = BitmapFactory.decodeResource(getActivity().getResources(), bgImgResId, options);
						Bitmap scaledImage = Bitmap.createScaledBitmap(image, screenWidthValue, (int) (image.getHeight() * ((float) screenWidthValue) / image.getWidth()), false);
						Bitmap blurredImg = Blur.fastblur(getActivity(), scaledImage, 12);
						ImageUtils.storeImage(blurredImg, blurredImage);
						if (getActivity() != null) {
							getActivity().runOnUiThread(new Runnable(){
								@Override
								public void run() {
									updateView(screenWidthValue, img_fileName);
								}
							});
						}
						image.recycle();
						image = null;
						scaledImage.recycle();
						scaledImage = null;
						blurredImg.recycle();
						blurredImg = null;
					}
				}).start();
			} else {
				updateView(screenWidthValue, img_fileName);
			}
		}
	}

	public synchronized void setBackgroundImage(final String img_fileName, boolean firstTime) {

		if (img_fileName != null || !img_fileName.equals("")) {
			if (getActivity() != null) {
				final int bgImgResId = getActivity().getResources().getIdentifier(img_fileName, "drawable", getActivity().getPackageName());
				if (bgImgResId != 0) {
					if (bmpNormal != null) {
						bmpNormal.recycle();
						bmpNormal = null;
					}
					bmpNormal = BitmapFactory.decodeResource(getResources(), bgImgResId);
				} else {
					//TODO - gotta think of something if no images, perhaps a default image?
					Log.i("XXX", "Normal Image Not Exists!");
				}
			}
		}

		if (bmpNormal != null) {
			if (!firstTime) {
				fadeOut.setAnimationListener(new AnimationListener(){
					@Override
					public void onAnimationEnd(Animation animation) {
						img_bg_normal.setImageBitmap(bmpNormal);
						img_bg_normal.startAnimation(fadeIn);
					}

					@Override
					public void onAnimationStart(Animation animation) {
						/*ignore*/
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						/*ignore*/
					}

				});
				img_bg_normal.startAnimation(fadeOut);
			} else {
				img_bg_normal.setImageBitmap(bmpNormal);
			}
			generateBlurBackgroundImage(img_fileName);
		}

	}

	protected abstract List<ListViewItem> getListItem();

	protected abstract int getTopHeight();

	protected abstract String getInitialBackgroundImageName();

	protected void setListAdapterItem(List<ListViewItem> items) {
		if (items != null) {
			itemsList.clear();
			itemsList.addAll(items);
		}
	}

	protected ListViewAdapter getAdapter() {
		return this.adapter;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (bmpBlurred != null) {
			bmpBlurred.recycle();
			bmpBlurred = null;
		}

		if (bmpNormal != null) {
			bmpNormal.recycle();
			bmpNormal = null;
		}

		setListAdapter(null);
		itemsList.clear();

		img_bg_blurred.setImageBitmap(null);
		img_bg_normal.setImageBitmap(null);

	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (bmpBlurred != null) {
			bmpBlurred.recycle();
		}

		if (bmpNormal != null) {
			bmpNormal.recycle();
		}

		if (img_bg_blurred != null) {
			img_bg_blurred.setImageBitmap(null);
		}

		if (img_bg_normal != null) {
			img_bg_normal.setImageBitmap(null);
		}

	}

}

package my.localocal.theme;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

public class ThemeHandler{

	private final Context	context;
	private final Resources	resources;
	private final String	packageName;

	public ThemeHandler(Context context) {
		this.context = context;
		resources = context.getResources();
		packageName = context.getPackageName();
	}

	public Drawable getImageDrawable(String imageName) {

		Drawable result;
		result = getDrawableForName(imageName);
		if (result != null) {
			return result;
		}

		return null;
	}

	public Drawable getDrawableForName(String resImageName) {
		Drawable drawable = null;
		int resId = getDrawableIdForName(resImageName);

		if (resId != 0) {
			drawable = context.getResources().getDrawable(resId);
		}

		return drawable;
	}

	public int getDrawableIdForName(String res) {
		int id = resources.getIdentifier(res, "drawable", packageName);
		return id;
	}

	public Drawable getButtonDrawable(String imageName) {

		StateListDrawable drawables = new StateListDrawable();
		int stateChecked = android.R.attr.state_checked;
		int stateFocused = android.R.attr.state_focused;
		int statePressed = android.R.attr.state_pressed;
		int stateWindowFocused = android.R.attr.state_window_focused;
		int stateSelected = android.R.attr.state_selected;
		int stateEnabled = android.R.attr.state_enabled;

		Drawable img_normal = getImageDrawable(imageName);
		Drawable img_selected = getImageDrawable(imageName + "_s");
		Drawable img_highlighted = getImageDrawable(imageName + "_h");
		Drawable img_disabled = getImageDrawable(imageName + "_d");

		if (img_selected == null) {
			img_selected = img_normal;
		}
		if (img_highlighted == null) {
			img_highlighted = img_selected;
		}
		if (img_disabled == null) {
			img_disabled = img_normal;
		}

		drawables.addState(new int[] { stateChecked, -stateWindowFocused }, img_selected);
		drawables.addState(new int[] { -stateChecked, -stateWindowFocused }, img_normal);
		drawables.addState(new int[] { stateChecked, statePressed }, img_highlighted);
		drawables.addState(new int[] { -stateChecked, statePressed }, img_highlighted);
		drawables.addState(new int[] { stateChecked, stateFocused }, img_selected);
		drawables.addState(new int[] { -stateChecked, stateFocused }, img_selected);
		drawables.addState(new int[] { stateSelected }, img_selected);
		drawables.addState(new int[] { stateChecked }, img_selected);
		drawables.addState(new int[] { -stateEnabled }, img_disabled);
		drawables.addState(new int[] { -stateChecked }, img_normal);

		return drawables;

	}

}

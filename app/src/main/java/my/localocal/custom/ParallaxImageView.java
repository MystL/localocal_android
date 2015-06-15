package my.localocal.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ParallaxImageView extends ImageView{

	private int	currentTranslation;

	public ParallaxImageView(Context context) {
		super(context);
	}

	public ParallaxImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ParallaxImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setCurrentTranslation(int currentTranslation) {
		this.currentTranslation = currentTranslation;
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		canvas.translate(0, -currentTranslation / 2);
		super.draw(canvas);
		canvas.restore();
	}
}

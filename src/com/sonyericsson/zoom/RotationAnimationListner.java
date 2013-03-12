package com.sonyericsson.zoom;

import android.content.Context;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class RotationAnimationListner {

	private Context mContext;
	ImageZoomView topImage,bottomImage;
	int firstCurrentPage = 0;
	int secondCurrentPage = 0;
	boolean isLoadNext;
	public void init(Context context, int firstCurrentPage,
			int secondCurrentPage) {
		this.mContext = context;
		this.firstCurrentPage = firstCurrentPage;
		this.secondCurrentPage = secondCurrentPage;
	}

	
	
	
	public int getFirstCurrentPage() {
		return firstCurrentPage;
	}

	public void setFirstCurrentPage(int firstCurrentPage) {
		this.firstCurrentPage = firstCurrentPage;
	}

	public int getSecondCurrentPage() {
		return secondCurrentPage;
	}

	public void setSecondCurrentPage(int secondCurrentPage) {
		this.secondCurrentPage = secondCurrentPage;
	}

	public boolean isLoadNext() {
		return isLoadNext;
	}

	public void setLoadNext(boolean isLoadNext) {
		this.isLoadNext = isLoadNext;
	}




	AnimationListener listener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
			Log.d("TopLayerAnimation",
					"onAnimationStart from RotationAnimationListner");
			// change bottomView and topview both here-Ankit
			topImage.post(new SwapTopViews(mContext,isLoadNext, false, topImage, firstCurrentPage,
					secondCurrentPage));
			topImage.post(new SwapBottomViews(mContext,isLoadNext,bottomImage, firstCurrentPage,
					secondCurrentPage));
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			Log.d("TopLayerAnimation",
					"onAnimationRepeat from RotationAnimationListner");
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			Log.d("TopLayerAnimation",
					"onAnimationEnd from RotationAnimationListner" + mContext);
			// change topView here-Ankit
			topImage.post(new SwapTopViews(mContext,isLoadNext, true, topImage, firstCurrentPage,
					secondCurrentPage));// check values here-Ankit
		}

	};

	public void applyRotation(float start, float inter, float end,
			ImageZoomView topImage, ImageZoomView bottomImage, boolean isFirst) {
		// Find the center of image
		final float centerX;
		final float centerY;
		this.topImage = topImage;
		this.bottomImage = bottomImage;
		
		if (topImage != null) {
			if (isFirst) {
				centerX = 0;
				centerY = 0;

			} else {
				centerX = topImage.getWidth() / 2.f;
				centerY = 0;
			}

			// Create a new 3D rotation with the supplied parameter
			// The animation listener is used to trigger the next animation
			final Flip3dAnimation rotationStart = new Flip3dAnimation(start,
					inter, centerX, centerY);
			final Flip3dAnimation rotationEnd = new Flip3dAnimation(inter, end,
					centerX, centerY);
			rotationStart.setDuration(3000);
			rotationStart.setFillAfter(false);
			rotationStart.setInterpolator(new AccelerateInterpolator());
			rotationStart.setAnimationListener(listener);
			topImage.startAnimation(rotationStart);

		}
	}

}

package com.sonyericsson.zoom;

import com.sonyericsson.tutorial.zoom4.R;
import com.sonyericsson.util.UIUtils;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class RotationAnimationListner {
	Flip3dAnimation rotationStart,rotationEnd;
	private Context mContext;
	ImageZoomView topImage,topImage2,bottomImage;
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
	//		topImage.post(new SwapTopViews(mContext,isLoadNext, false, topImage, topImage2,firstCurrentPage,
		//			secondCurrentPage));
//			topImage.post(new SwapBottomViews(mContext,isLoadNext,bottomImage, firstCurrentPage,
	//				secondCurrentPage));
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
		//	topImage2.setVisibility(View.VISIBLE);
		//	topImage.setImage(UIUtils.combineImages(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.page1)));
		//	topImage.setVisibility(View.GONE);
			topImage.post(new SwapTopViews(mContext,isLoadNext, true, topImage,topImage2, firstCurrentPage,
					secondCurrentPage));// check values here-Ankit
			//rotationStart = new Flip3dAnimation(90,0, topImage2.getWidth() / 2.f, 0);
			//topImage2.startAnimation(rotationStart);
			
		//	topImage.setImage(UIUtils.combineImages(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.page2), false));
			
			//topImage.startAnimation(rotationEnd);
			
			
		}

	};

	public void applyRotation(float start, float inter, float end,
			ImageZoomView topImage, 	ImageZoomView topImage2,ImageZoomView bottomImage, boolean isFirst) {
		// Find the center of image
		final float centerX;
		final float centerY;
		this.topImage = topImage;
		this.topImage2 = topImage2;
		this.bottomImage = bottomImage;
		
		if (topImage != null) {
			if (isFirst) {
				centerX = 0;
				centerY = 0;

			} else {
				centerX = topImage.getWidth() / 2.f;
				  centerY = 0;//topImage.getHeight() / 2.0f;
			}

			// Create a new 3D rotation with the supplied parameter
			// The animation listener is used to trigger the next animation
			rotationStart = new Flip3dAnimation(start,
					inter, centerX, centerY);
			rotationEnd = new Flip3dAnimation(inter, end,
					centerX, centerY);
			rotationStart.setDuration(10000);
			rotationStart.setFillAfter(false);
			//rotationStart.setFillEnabled(false);
			rotationStart.setInterpolator(new AccelerateInterpolator());
			rotationStart.setAnimationListener(listener);
			this.topImage.startAnimation(rotationStart);
			//this.topImage2.startAnimation(rotationStart);

		}
	}

}

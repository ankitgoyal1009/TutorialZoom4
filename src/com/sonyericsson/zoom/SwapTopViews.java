package com.sonyericsson.zoom;

import com.sonyericsson.tutorial.zoom4.R;
import com.sonyericsson.util.UIUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public final class SwapTopViews implements Runnable {
	private boolean isLoadNext;// true if next page to be loaded and false for
								// previous page
	private ImageZoomView imageZoomView,imageZoomView2; // bottom imageZoomView instance
	int firstCurrentPage;// page number of current first half
	int secondCurrentPage;// page number of current second half
	boolean isAnimationStarted; // true if animation has started and this has been called from onAnimationEnd()
	private Context context;
	 Flip3dAnimation rotation;
	public SwapTopViews(Context context,boolean isLoadNext, boolean isAnimationStarted,
			ImageZoomView imageZoomView, int firstCurrentPage,
			int secondCurrentPage) {
		this.context = context;
		this.isLoadNext = isLoadNext;
		this.imageZoomView = imageZoomView;
		this.firstCurrentPage = firstCurrentPage;
		this.secondCurrentPage = secondCurrentPage;
		this.isAnimationStarted = isAnimationStarted;
	}

	public void run() {
		if(!isAnimationStarted){
			//called from onAnimationStart
		if (isLoadNext) {
			// LoadNext() has been called
			// First half==empty
			// Second half== secondCurrentPage
			if(secondCurrentPage==1){
			UIUtils.combineImages(BitmapFactory.decodeResource(context.getResources(), R.drawable.page1),true);
			}else if(secondCurrentPage==3){
				UIUtils.combineImages(BitmapFactory.decodeResource(context.getResources(), R.drawable.page3),true);
			}else{
				//after max page
			}
		} else {
			// LoadPrevious() has been called
			// First half==firstCurrentPage
			// Second half== empty
			if(firstCurrentPage==0){
					imageZoomView.setImage(	UIUtils.combineImages(BitmapFactory.decodeResource(context.getResources(), R.drawable.page1),false));
				}else if(firstCurrentPage==2){
					imageZoomView.setImage(	UIUtils.combineImages(BitmapFactory.decodeResource(context.getResources(), R.drawable.page2),false));
				}else{
					imageZoomView.setImage(	UIUtils.combineImages(BitmapFactory.decodeResource(context.getResources(), R.drawable.page4),true));
				}
		}}else{
			//called from onAnimationEnd
			final float centerX = imageZoomView.getWidth() / 2.f;
			final float centerY = 0;
			imageZoomView2 = imageZoomView;
			imageZoomView.setVisibility(View.INVISIBLE);
			
			if (isLoadNext) {
				// LoadNext() has been called
				// First half==firstCurrentPage+1
				// Second half== empty
				if(firstCurrentPage==0){
					imageZoomView.setImage(	UIUtils.combineImages(BitmapFactory.decodeResource(context.getResources(), R.drawable.page2),false));
				}else if(firstCurrentPage==2){
					imageZoomView.setImage(	UIUtils.combineImages(BitmapFactory.decodeResource(context.getResources(), R.drawable.page4),false));
				}
				rotation = new Flip3dAnimation(90, 0, centerX, centerY);
			} else {
				// LoadPrevious() has been called
				// First half==empty
				// Second half== secondCurrentPage-1
			}	
		
			
			rotation.setDuration(3000);
			 rotation.setFillAfter(true);
			 rotation.setInterpolator(new DecelerateInterpolator());
			 imageZoomView2.startAnimation(rotation);
		}
	}

}

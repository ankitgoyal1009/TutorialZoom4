package com.sonyericsson.zoom;

import com.sonyericsson.tutorial.zoom4.R;

import android.content.Context;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class RotationAnimationListner {
	
	private ImageView mLeftImage,mRightImage;
	private Context mContext;
	  AnimationListener listener = new AnimationListener() {

          @Override
          public void onAnimationStart(Animation animation) {
        	  Log.d("TopLayerAnimation", "onAnimationStart");
          }

          @Override
          public void onAnimationRepeat(Animation animation) {
        	  Log.d("TopLayerAnimation", "onAnimationRepeat");
          }

          @Override
          public void onAnimationEnd(Animation animation) {
        	  Log.d("TopLayerAnimation", "onAnimationEnd"+mContext);
//        	  mLeftImage.setBackgroundDrawable(mContext.getResources().getDrawable(android.R.color.transparent));
//        	  mRightImage.setBackgroundDrawable(mContext.getResources().getDrawable(android.R.color.transparent));
          }

      };


      public ImageView getmLeftImage() {
		return mLeftImage;
	}
	public void setmLeftImage(ImageView mLeftImage) {
		this.mLeftImage = mLeftImage;
	}
	public ImageView getmRightImage() {
		return mRightImage;
	}
	public void setmRightImage(ImageView mRightImage) {
		this.mRightImage = mRightImage;
	}
	public void init(Context context, ImageView leftImage, ImageView rightImage){
    	  this.mContext =context;
    	  this.mLeftImage = leftImage;
    	  this.mRightImage = rightImage;
      }
      public void applyRotation(float start, float inter, float end, ImageView image, boolean isFirst) {
          // Find the center of image
          final float centerX;
          final float centerY;
          
  if(image!=null){
          if (isFirst) {
              centerX = 0;
              centerY = image.getHeight() / 2.0f;

          } else {
              centerX = image.getWidth();
              centerY = image.getHeight() / 2.0f;
          }

          // Create a new 3D rotation with the supplied parameter
          // The animation listener is used to trigger the next animation
          final Flip3dAnimation rotationStart = new Flip3dAnimation(start, inter, centerX, centerY);
          final Flip3dAnimation rotationEnd = new Flip3dAnimation(inter, end, centerX, centerY);
          rotationStart.setDuration(300);
          rotationStart.setFillAfter(false);
          rotationStart.setInterpolator(new AccelerateInterpolator());
          rotationStart.setAnimationListener(listener);
          image.startAnimation(rotationStart);
    

      }}

}

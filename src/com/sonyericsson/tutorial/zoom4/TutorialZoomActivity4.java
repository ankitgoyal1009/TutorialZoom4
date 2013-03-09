/*
 * Copyright 2010 Sony Ericsson Mobile Communications AB
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.sonyericsson.tutorial.zoom4;

import com.sonyericsson.zoom.DynamicZoomControl;
import com.sonyericsson.zoom.ImageZoomView;
import com.sonyericsson.zoom.LongPressZoomListener;
import com.sonyericsson.zoom.PinchZoomListener;

import com.sonyericsson.tutorial.zoom4.R;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Activity for zoom tutorial 1
 */
public class TutorialZoomActivity4 extends Activity {
	private static final String TAG = "TutorialZoomActivity4";
    /** Constant used as menu item id for resetting zoom state */
    private static final int MENU_ID_RESET = 0;

    /** Image zoom view */
    private ImageZoomView mZoomView;

    /** Zoom control */
    private DynamicZoomControl mZoomControl;

    /** Decoded bitmap image */
    private Bitmap mBitmap;
    private Bitmap mBitmap2;
private ImageView leftUpperView;
private ImageView rightUpperView;
    /** On touch listener for zoom view */
    private LongPressZoomListener mZoomListener;
    
    private PinchZoomListener mPinchZoomListener;
    
    private boolean longpressZoom = false;
    Rect mRectSrc;
    Rect mRectDst;
    Paint mPaint;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.main);

        mZoomControl = new DynamicZoomControl();
leftUpperView = (ImageView)findViewById(R.id.leftUpperView);
rightUpperView = (ImageView)findViewById(R.id.rightUpperView);
//        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image800x600);
mBitmap = combineImages(BitmapFactory.decodeResource(getResources(), R.drawable.page1), BitmapFactory.decodeResource(getResources(), R.drawable.page2));
mBitmap2 = combineImages(BitmapFactory.decodeResource(getResources(), R.drawable.page1), BitmapFactory.decodeResource(getResources(), R.drawable.page1));
        mZoomListener = new LongPressZoomListener(getApplicationContext());
        mZoomListener.setZoomControl(mZoomControl);
        mPinchZoomListener = new PinchZoomListener(getApplicationContext());
        mPinchZoomListener.setZoomControl(mZoomControl);
        
        
        mZoomView = (ImageZoomView)findViewById(R.id.zoomview);
        mZoomView.setZoomState(mZoomControl.getZoomState());
        mZoomView.setImage2(mBitmap2);
        mZoomView.setImage(mBitmap);

        mZoomControl.setAspectQuotient(mZoomView.getAspectQuotient());

        resetZoomState();
        
        mZoomView.setOnTouchListener(mPinchZoomListener);
        
       final Button b = (Button)this.findViewById(R.id.zoomtype); 
        b.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	/*leftUpperView.setBackgroundDrawable(getResources().getDrawable(R.drawable.page2));
            	mBitmap = combineImages(BitmapFactory.decodeResource(getResources(), R.drawable.page2), BitmapFactory.decodeResource(getResources(), R.drawable.page1));
            	mZoomView.setImage(mBitmap);
                */if(longpressZoom){
                   leftUpperView.setBackgroundDrawable(getResources().getDrawable(R.drawable.page2));
            	mBitmap = combineImages(BitmapFactory.decodeResource(getResources(), R.drawable.page2), BitmapFactory.decodeResource(getResources(), R.drawable.page1));
            	mZoomView.setImage(mBitmap);
            	    longpressZoom = false;
                    b.setText("LongPressZoom");
                }else{
                leftUpperView.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
                rightUpperView.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
            	mBitmap = combineImages(BitmapFactory.decodeResource(getResources(), R.drawable.page1), BitmapFactory.decodeResource(getResources(), R.drawable.page2));
            	mZoomView.setImage(mBitmap);
            	    longpressZoom = true;
                    b.setText("PinchZoom");
                }
            	
            }
        });
        
        CharSequence text = "Pinch zoom ftw!\n(Press button top left to switch between zoom modes)";
        
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(this, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
   
    @Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
		mRectDst = mZoomView.getmRectDst();
		mRectSrc = mZoomView.getmRectSrc();
		leftUpperView.setLeft(mRectDst.left);
		leftUpperView.setRight(mRectDst.right/2);
		leftUpperView.setBottom(mRectDst.bottom);
		leftUpperView.setTop(mRectDst.top);
//
//		rightUpperView.setLeft(mRectDst.right/2);
//		rightUpperView.setRight(mRectDst.right);
//		rightUpperView.setBottom(mRectDst.bottom);
//		rightUpperView.setTop(mRectDst.top);
	}

    
    
    @Override
    public void onPause(){
        super.onPause();
        
        // Kill when pressing home key.
        this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mBitmap.recycle();
        mZoomView.setOnTouchListener(null);
        mZoomControl.getZoomState().deleteObservers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_ID_RESET, 2, R.string.menu_reset);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ID_RESET:
                resetZoomState();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Reset zoom state and notify observers
     */
    private void resetZoomState() {
        mZoomControl.getZoomState().setPanX(0.5f);
        mZoomControl.getZoomState().setPanY(0.5f);
        mZoomControl.getZoomState().setZoom(1f);
        mZoomControl.getZoomState().notifyObservers();
    }
    public Bitmap combineImages(Bitmap c, Bitmap s) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom 
        Bitmap cs = null; 
     
        int width, height = 0; 
         
        /*if(c.getWidth() > s.getWidth()) { 
          width = c.getWidth(); 
          height = c.getHeight() + s.getHeight(); 
        } else { */
          width = s.getWidth()+c.getWidth(); 
          height = c.getHeight() ; 
        //} 
     
        cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 
     
        Canvas comboImage = new Canvas(cs); 
     
        comboImage.drawBitmap(c, 0f, 0f, null); 
        comboImage.drawBitmap(s, c.getWidth(), 0f, null); 
     
  
        return cs; 
      } 

	class SimpleGestureListner123 implements OnGestureListener {

		

		@Override
		public boolean onDown(MotionEvent e) {
			Log.d(TAG, "onDown from SimpleGestureListner");
			return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.d(TAG, "onFling from SimpleGestureListner");
			//changeMe(null);
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			Log.d(TAG, "onLongPress from SimpleGestureListner");
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			//Log.d(TAG, "onScroll from >>>>SimpleGestureListner");
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
			Log.d(TAG, "onShowPress from SimpleGestureListner");
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			Log.d(TAG, "onSingleTapUp from SimpleGestureListner");
			return false;
		}

	}
}

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

package com.sonyericsson.zoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.Observable;
import java.util.Observer;

import com.sonyericsson.tutorial.zoom4.R;


/**
 * Steps to implement flip rotation
 * Send left and right images from activity and combine them here
 * when rotating based on direction add transparent image to top view container at respective position(if loading next page left image will be transparent and vice versa
 * 
 * */


/**
 * View capable of drawing an image at different zoom state levels
 */
public class ImageZoomView extends View implements Observer {
	 /**
     * Enum defining listener modes. Before the view is touched the listener is
     * in the UNDEFINED mode. Once touch starts it can enter either one of the
     * other two modes: If the user scrolls over the view the listener will
     * enter PAN mode, if the user lets his finger rest and makes a long press
     * the listener will enter ZOOM mode.
     */
    private enum Mode {
        UNDEFINED, PAN, PINCHZOOM
    }
    /** Maximum velocity for fling */
    private final int mScaledMaximumFlingVelocity;
    /** Distance touch can wander before we think it's scrolling */
    private final int mScaledTouchSlop;
    private long panAfterPinchTimeout = 0;
    /** Current listener mode */
    private Mode mMode = Mode.UNDEFINED;

    private PointF mMidPoint = new PointF();
	   /** Distance between fingers */
    private float oldDist = 1f;
    /** X-coordinate of previously handled touch event */
    private float mX;

    /** Y-coordinate of previously handled touch event */
    private float mY;

    /** X-coordinate of latest down event */
    private float mDownX;

    /** Y-coordinate of latest down event */
    private float mDownY;

    /** Zoom control to manipulate */
    private DynamicZoomControl mZoomControl;
   
    /** Velocity tracker for touch events */
    private VelocityTracker mVelocityTracker;
    /** Paint object used when drawing bitmap. */
    private final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);

    /** Rectangle used (and re-used) for cropping source image. */
    private final Rect mRectSrc = new Rect();

    /** Rectangle used (and re-used) for specifying drawing area on canvas. */
    private final Rect mRectDst = new Rect();

    /** Object holding aspect quotient */
    private final AspectQuotient mAspectQuotient = new AspectQuotient();

    /** The bitmap that we're zooming in, and drawing on the screen. */
    private Bitmap mBitmap;
    private Bitmap mBitmap2;
    /** State of the zoom. */
    private ZoomState mState;

    // Public methods

    /**
     * Constructor
     */
    public ImageZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScaledMaximumFlingVelocity = ViewConfiguration.get(context)
                .getScaledMaximumFlingVelocity();
    }

    /**
     * Set image bitmap
     * 
     * @param bitmap The bitmap to view and zoom into
     */
    public void setImage(Bitmap bitmap) {
        mBitmap = bitmap;
//mBitmap2 =bitmap;
        mAspectQuotient.updateAspectQuotient(getWidth(), getHeight(), mBitmap.getWidth(), mBitmap
                .getHeight());
        mAspectQuotient.notifyObservers();

        invalidate();
    }
    
    public void setImage2(Bitmap bitmap) {
       
mBitmap2 =bitmap;
invalidate();
    }

    /**
     * Set object holding the zoom state that should be used
     * 
     * @param state The zoom state
     */
    public void setZoomState(ZoomState state) {
        if (mState != null) {
            mState.deleteObserver(this);
        }

        mState = state;
        mState.addObserver(this);

        invalidate();
    }

    /**
     * Gets reference to object holding aspect quotient
     * 
     * @return Object holding aspect quotient
     */
    public AspectQuotient getAspectQuotient() {
        return mAspectQuotient;
    }

    // Superclass overrides

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null && mState != null) {
        	Log.d("", "onDraw");
            final float aspectQuotient = mAspectQuotient.get();

            final int viewWidth = getWidth();
            final int viewHeight = getHeight();
            final int bitmapWidth = mBitmap.getWidth();
            final int bitmapHeight = mBitmap.getHeight();

            final float panX = mState.getPanX();
            final float panY = mState.getPanY();
            final float zoomX = mState.getZoomX(aspectQuotient) * viewWidth / bitmapWidth;
            final float zoomY = mState.getZoomY(aspectQuotient) * viewHeight / bitmapHeight;

            // Setup source and destination rectangles
            mRectSrc.left = (int)(panX * bitmapWidth - viewWidth / (zoomX * 2));
            mRectSrc.top = (int)(panY * bitmapHeight - viewHeight / (zoomY * 2));
            mRectSrc.right = (int)(mRectSrc.left + viewWidth / zoomX);
            mRectSrc.bottom = (int)(mRectSrc.top + viewHeight / zoomY);
            mRectDst.left = getLeft();
            mRectDst.top = getTop();
            mRectDst.right = getRight();
            mRectDst.bottom = getBottom();

            // Adjust source rectangle so that it fits within the source image.
            if (mRectSrc.left < 0) {
                mRectDst.left += -mRectSrc.left * zoomX;
                mRectSrc.left = 0;
            }
            if (mRectSrc.right > bitmapWidth) {
                mRectDst.right -= (mRectSrc.right - bitmapWidth) * zoomX;
                mRectSrc.right = bitmapWidth;
            }
            if (mRectSrc.top < 0) {
                mRectDst.top += -mRectSrc.top * zoomY;
                mRectSrc.top = 0;
            }
            if (mRectSrc.bottom > bitmapHeight) {
                mRectDst.bottom -= (mRectSrc.bottom - bitmapHeight) * zoomY;
                mRectSrc.bottom = bitmapHeight;
            }
            Rect test = new Rect(0, 0, mRectDst.right, mRectDst.bottom+10);
            
            canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);
            canvas.drawBitmap(mBitmap2, mRectSrc, mRectDst, mPaint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
Log.d("", "onLayout");
        mAspectQuotient.updateAspectQuotient(right - left, bottom - top, mBitmap.getWidth(),
                mBitmap.getHeight());
        mAspectQuotient.notifyObservers();
    }

    // implements Observer
    public void update(Observable observable, Object data) {
        invalidate();
    }

    /**
     * Sets the zoom control to manipulate
     * 
     * @param control Zoom control
     */
    public void setZoomControl(DynamicZoomControl control) {
        mZoomControl = control;
    }
    @Override
    public boolean onTouchEvent( MotionEvent event) {
        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        final float x = event.getX();
        final float y = event.getY();

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mZoomControl.stopFling();
                mDownX = x;
                mDownY = y;
                mX = x;
                mY = y;
                break;
                
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() > 1) {
                    oldDist = spacing(event);
                    if (oldDist > 10f) {
                        midPoint(mMidPoint, event);
                        mMode = Mode.PINCHZOOM;
                    }
                }
                break;
                
            case MotionEvent.ACTION_UP:
                if (mMode == Mode.PAN) {
                    final long now = System.currentTimeMillis();
                    if(panAfterPinchTimeout < now){
                        mVelocityTracker.computeCurrentVelocity(1000, mScaledMaximumFlingVelocity);
                        mZoomControl.startFling(-mVelocityTracker.getXVelocity() / mBitmap.getWidth(),
                                -mVelocityTracker.getYVelocity() / mBitmap.getHeight());
                    }
                } else if(mMode != Mode.PINCHZOOM) {
                    mZoomControl.startFling(0, 0);
                }
                mVelocityTracker.recycle();
                mVelocityTracker = null;
            case MotionEvent.ACTION_POINTER_UP:
                if(event.getPointerCount() > 1 &&  mMode == Mode.PINCHZOOM){
                    panAfterPinchTimeout = System.currentTimeMillis() + 100;
                }
                mMode = Mode.UNDEFINED;                
                break;
                
            case MotionEvent.ACTION_MOVE:
                final float dx = (x - mX) / mBitmap.getWidth();
                final float dy = (y - mY) / mBitmap.getHeight();
               
                if (mMode == Mode.PAN) {
                    mZoomControl.pan(-dx, -dy);
                } else if (mMode == Mode.PINCHZOOM) {
                    float newDist = spacing(event);
                    if (newDist > 10f) {
                        final float scale = newDist / oldDist;
                        final float xx = mMidPoint.x / mBitmap.getWidth();
                        final float yy = mMidPoint.y / mBitmap.getHeight();
                        mZoomControl.zoom(scale, xx, yy);
                        oldDist = newDist;
                    }
                } else {
                    final float scrollX = mDownX - x;
                    final float scrollY = mDownY - y;

                    final float dist = (float)Math.sqrt(scrollX * scrollX + scrollY * scrollY);

                    if (dist >= mScaledTouchSlop ){
                        mMode = Mode.PAN;
                    }
                }
                
                mX = x;
                mY = y;
                break;

            default:
                mVelocityTracker.recycle();
                mVelocityTracker = null;
                mMode = Mode.UNDEFINED;
                break;
        }
        return true;
    }
    /** Determine the space between the first two fingers */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /** Calculate the mid point of the first two fingers */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

	public Rect getmRectSrc() {
		return mRectSrc;
	}

	public Rect getmRectDst() {
		return mRectDst;
	}

	public Paint getmPaint() {
		return mPaint;
	}
    
}

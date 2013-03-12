package com.sonyericsson.zoom;

import com.sonyericsson.tutorial.zoom4.R;
import com.sonyericsson.util.UIUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public final class SwapBottomViews implements Runnable {
	private boolean isLoadNext;//true if next page to be loaded and false for previous page
	private ImageZoomView imageTopZoomView; // bottom imageZoomView instance
	int firstCurrentPage;// page number of current first half
	int secondCurrentPage;//page number of current second half
	private Context context;

	public SwapBottomViews(Context context,boolean isLoadNext, ImageZoomView imageZoomView,
			 int firstCurrentPage,int secondCurrentPage) {
		this.isLoadNext = isLoadNext;
		this.imageTopZoomView = imageZoomView;
		this.firstCurrentPage=firstCurrentPage;
		this.secondCurrentPage = secondCurrentPage;
		this.context= context;
	}

	public void run() {
		if(isLoadNext){
			//LoadNext() has been called
			//First half==firstCurrentPage
			//Second half== secondCurrentPage+2
			if(firstCurrentPage == 0){
				//we are at first page
				imageTopZoomView.setImage(	UIUtils.combineImages(BitmapFactory.decodeResource(context.getResources(), R.drawable.page3), true));
			}else {
				if(firstCurrentPage==2){
					imageTopZoomView.setImage(UIUtils.combineImages(BitmapFactory.decodeResource(context.getResources(), R.drawable.page2), BitmapFactory.decodeResource(context.getResources(), R.drawable.page3)));
				}else if(firstCurrentPage == 4){
					imageTopZoomView.setImage(	UIUtils.combineImages(BitmapFactory.decodeResource(context.getResources(), R.drawable.page4), false));
				}
			}
		}else{
			//LoadPrevious() has been called
			//First half==firstCurrentPage-2
			//Second half== secondCurrentPage
			if(firstCurrentPage == 0){
				//do nothing at first page
			}else{
				if(firstCurrentPage==2){
				//	UIUtils.combineImages(BitmapFactory.decodeResource(context.getResources(), R.drawable.page2), BitmapFactory.decodeResource(context.getResources(), R.drawable.page3));
				}else if(firstCurrentPage == 4){
					imageTopZoomView.setImage(UIUtils.combineImages(BitmapFactory.decodeResource(context.getResources(), R.drawable.page2), BitmapFactory.decodeResource(context.getResources(), R.drawable.page3)));
				}
			}
		}
	}

}

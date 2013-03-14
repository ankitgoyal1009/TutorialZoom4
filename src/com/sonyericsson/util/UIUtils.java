package com.sonyericsson.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class UIUtils {
	public static Bitmap combineImages(Bitmap c, boolean loadNext) {
		// loadNext==true if next page has to be load otherwise previous page-Ankit
		Bitmap cs = null;
		int width, height = 0;
		width = c.getWidth() * 2;
		height = c.getHeight();
		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas comboImage = new Canvas(cs);
		if (loadNext) {
			comboImage.drawBitmap(c, width / 2, 0f, null);
		} else {
			comboImage.drawBitmap(c, 0f, 0f, null);
		}
		return cs;
	}
	public static Bitmap combineImages(Bitmap c) {
		// loadNext==true if next page has to be load otherwise previous page-Ankit
		Bitmap cs = null;
		int width, height = 0;
		width = c.getWidth() * 2;
		height = c.getHeight();
		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas comboImage = new Canvas(cs);
		
		return cs;
	}
	public static Bitmap combineImages(Bitmap c, Bitmap s) {
		Bitmap cs = null;
		int width, height = 0;
		width = s.getWidth() + c.getWidth();
		height = c.getHeight();
		cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas comboImage = new Canvas(cs);
		comboImage.drawBitmap(c, 0f, 0f, null);
		comboImage.drawBitmap(s, c.getWidth(), 0f, null);
		return cs;
	}
}

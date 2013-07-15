package com.emenu.common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.emenu.R;

public class BitmapLoader {

	private BitmapLoader() {
	}

	private static BitmapLoader instance = null;

	public static BitmapLoader getInstance() {
		if (instance == null) {
			instance = new BitmapLoader();
		}
		return instance;
	}

	private Map<String, Bitmap> bitmaps = new HashMap<String, Bitmap>();

	public void boundImage(ImageView iv, String file) {
		Bitmap bm = bitmaps.get(file);
		if (bm != null) {
			iv.setImageBitmap(bm);
			return;
		}
		boolean OOME = true;
		int sample = 2;
		while (OOME) {
			try {
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = sample;
				File imgf = new File(file);
				long size = imgf.length();
				if (size < 200 * 1024 * 1024) {
					sample = 1;
				} else if (size < 500 * 1024 * 1024) {
					sample = 2;
				} else if (size < 800 * 1024 * 1024) {
					sample = 4;
				} else if (size < 1000 * 1024 * 1024) {
					sample = 8;
				} else {
					sample = 16;
				}
				if (imgf.exists()) {
					bm = BitmapFactory.decodeFile(imgf.getAbsolutePath(), opt);
					iv.setImageBitmap(bm);
					bitmaps.put(file, bm);
				} else {
					iv.setImageResource(R.drawable.default_images);
				}
				OOME = false;
			} catch (OutOfMemoryError e) {
				sample *= 2;
				if (sample > 8) {
					OOME = false;
					MLog.d("OOM Exception while loading Bitmap from [" + file + "]");
				}
			}
		}
	}

	public void recycleBitmaps() {
		for (String key : bitmaps.keySet()) {
			Bitmap bm = bitmaps.get(key);
			if (bm != null && !bm.isRecycled()) {
				bm.recycle();
				bm = null;
			}
		}
		bitmaps.clear();
	}
}
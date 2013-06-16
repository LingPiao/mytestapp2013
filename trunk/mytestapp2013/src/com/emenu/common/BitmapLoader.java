package com.emenu.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

	private List<Bitmap> bitmaps = new ArrayList<Bitmap>();

	public void boundImage(ImageView iv, String file) {
		boolean OOME = true;
		int sample = 2;
		while (OOME) {
			try {
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = sample;
				Bitmap bm = null;
				File imgf = new File(file);
				if (imgf.exists()) {
					bm = BitmapFactory.decodeFile(imgf.getAbsolutePath(), opt);
					iv.setImageBitmap(bm);
					bitmaps.add(bm);
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
		for (Bitmap bm : bitmaps) {
			if (bm != null && !bm.isRecycled()) {
				bm.recycle();
				bm = null;
			}
		}
		bitmaps.clear();
	}

}

package com.emenu.common;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import com.emenu.R;

public class BitmapLoader {

	private LruCache<String, Bitmap> mMemoryCache;

	private BitmapLoader() {
		// Get max available VM memory, exceeding this amount will throw an
		// OutOfMemory exception. Stored in kilobytes as LruCache takes an
		// int in its constructor.
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		// Use 1/4th of the available memory for this memory cache.
		final int cacheSize = maxMemory / 4;

		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// The cache size will be measured in kilobytes rather than
				// number of items.
				return bitmap.getByteCount() / 1024;
			}

			@Override
			protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
				if (oldValue != null && !oldValue.isRecycled()) {
					MLog.d("=========== Recycling the cached bitmap after removed ");
					oldValue.recycle();
				}
			}
		};
	}

	private static BitmapLoader instance = null;

	public static BitmapLoader getInstance() {
		if (instance == null) {
			instance = new BitmapLoader();
		}
		return instance;
	}

	public void boundImage(ImageView iv, String file) {

		final String imageKey = file;

		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null) {
			iv.setImageBitmap(bitmap);
		} else {
			iv.setImageResource(R.drawable.default_images);
			BitmapWorkerTask task = new BitmapWorkerTask(iv);
			task.execute(file);
		}

	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	public void shutdown() {
		mMemoryCache.evictAll();
	}

	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
		private ImageView iv;

		private BitmapWorkerTask(ImageView iv) {
			this.iv = iv;
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bm = null;
			String file = params[0];
			int sample = 2;
			File imgf = new File(file);
			if (imgf.exists()) {
				long size = imgf.length();
				if (size < 200 * 1024) {
					sample = 1;
				} else if (size < 500 * 1024) {
					sample = 2;
				} else if (size < 800 * 1024) {
					sample = 4;
				} else if (size < 1000 * 1024) {
					sample = 8;
				} else {
					sample = 16;
				}
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = sample;
				bm = BitmapFactory.decodeFile(imgf.getAbsolutePath(), opt);
				addBitmapToMemoryCache(file, bm);
			}
			return bm;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (result == null) {
				iv.setImageResource(R.drawable.default_images);
			} else {
				iv.setImageBitmap(result);
			}
		}
	}
}

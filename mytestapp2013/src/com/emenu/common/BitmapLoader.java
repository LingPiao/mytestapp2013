package com.emenu.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import com.emenu.R;

public class BitmapLoader {

	private LruCache<String, Bitmap> mMemoryCache;
	private AtomicBoolean isLoading = new AtomicBoolean(true);

	private List<BitmapWorkerTask> tasks = new ArrayList<BitmapWorkerTask>();

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
				super.entryRemoved(evicted, key, oldValue, newValue);
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

	public void lazyBoundingImage(int position, ImageView iv, String file) {

		final String imageKey = file;

		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null) {
			iv.setImageBitmap(bitmap);
		} else {
			iv.setImageResource(R.drawable.default_images);
			tasks.add(new BitmapWorkerTask(position, iv, file));
		}

	}

	public void executAllLoadingTasks() {
		MLog.d("===========Loading all, size=" + tasks.size());
		isLoading.set(true);
		for (BitmapWorkerTask t : tasks) {
			if (!isLoading.get()) {
				MLog.d("===========Loading cancled");
				break;
			}
			t.execute(t.getImg());
		}
		if (isLoading.get())
			tasks.clear();
	}

	public void executLoadingTasks(int start, int end) {
		isLoading.set(true);
		for (BitmapWorkerTask t : tasks) {
			if (!isLoading.get()) {
				MLog.d("===========Loading cancled");
				break;
			}
			if (t.getPosition() >= start && t.getPosition() <= end) {
				t.execute(t.getImg());
			}
		}
		if (isLoading.get())
			tasks.clear();
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (bitmap != null && getBitmapFromMemCache(key) == null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}

	public void shutdown() {
		mMemoryCache.evictAll();
	}

	public void setLoading(boolean isLoading) {
		this.isLoading.set(isLoading);
	}

	class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
		private ImageView iv;
		private int position;
		private String img;

		public int getPosition() {
			return position;
		}

		public String getImg() {
			return img;
		}

		private BitmapWorkerTask(ImageView iv) {
			this.iv = iv;
		}

		private BitmapWorkerTask(int position, ImageView iv, String img) {
			this.position = position;
			this.iv = iv;
			this.img = img;
		}

		// Decode image in background.
		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bm = null;
			String file = params[0];
			if (file == null || file.trim().length() < 1)
				return null;
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
				if (bm == null) {
					return null;
				}
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

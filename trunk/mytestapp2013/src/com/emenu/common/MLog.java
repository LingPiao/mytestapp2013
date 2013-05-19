package com.emenu.common;

import android.util.Log;

public class MLog {
	private static String TAG = "com.emenu";

	private static boolean logEnabled = true;

	public static void d(String logMe) {
		if (logEnabled) {
			Log.d(TAG, logMe);
		}
	}

	public static void i(String logMe) {
		if (logEnabled) {
			Log.i(TAG, logMe);
		}
	}

	public static void v(String logMe) {
		if (logEnabled) {
			Log.v(TAG, logMe);
		}
	}

	public static void w(String logMe) {
		if (logEnabled) {
			Log.w(TAG, logMe);
		}
	}
}

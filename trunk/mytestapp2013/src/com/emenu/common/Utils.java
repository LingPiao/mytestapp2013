package com.emenu.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import android.os.Environment;

public class Utils {

	private static DecimalFormat numberFormat = new DecimalFormat("####.00");

	public static String loadTitle() {
		String title = null;
		String tf = Environment.getExternalStorageDirectory().getPath() + Constants.TITLE_FILE;
		MLog.d("Load title file from:" + tf);
		File f = new File(tf);
		InputStream in = null;
		BufferedReader br = null;
		try {
			in = new BufferedInputStream(new FileInputStream(f));
			br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			title = br.readLine();
		} catch (IOException e) {
			MLog.d("Get title from file exception:" + e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return title;
	}

	public static String isDataReady() {
		String errorMsg = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (!sdCardExist) {
			return "No SDCard found!";
		}
		File data = new File(Environment.getExternalStorageDirectory().getPath() + Constants.DATA);
		if (!data.exists()) {
			return "Copy the data to " + Environment.getExternalStorageDirectory().getPath() + Constants.DATA + " before starting the app";
		}

		data = new File(XmlUtils.getInstance().getMainMenuXml());
		if (!data.exists()) {
			return "Missing file:" + XmlUtils.getInstance().getMainMenuXml();
		}

		data = new File(XmlUtils.getInstance().getDishesXml());
		if (!data.exists()) {
			return "Missing file:" + XmlUtils.getInstance().getDishesXml();
		}

		return errorMsg;

	}

	public static String formatPriceWithUnit(float price) {
		return Constants.DEFAULT_CURRENCY_UNIT + numberFormat.format(price);
	}

	public static String formatPrice(float price) {
		return numberFormat.format(price);
	}
}

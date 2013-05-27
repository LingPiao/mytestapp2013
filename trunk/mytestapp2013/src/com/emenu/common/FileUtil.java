package com.emenu.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Environment;

public class FileUtil {

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

}

package com.emenu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.widget.TextView;

import com.emenu.R;

public class DishDetail extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dish_detail);
		TextView tv = (TextView) findViewById(R.id.dishDetail);
		tv.setText("Environment.getDataDirectory()=" + Environment.getDataDirectory() + "\n");
		tv.append("Environment.getDownloadCacheDirectory()=" + Environment.getDownloadCacheDirectory() + "\n");
		tv.append("Environment.getExternalStorageDirectory()=" + Environment.getExternalStorageDirectory() + "\n");
		tv.append("Environment.getExternalStorageState()=" + Environment.getExternalStorageState() + "\n");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dish_detail, menu);
		return true;
	}

}

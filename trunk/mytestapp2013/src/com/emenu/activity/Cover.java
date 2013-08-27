package com.emenu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.emenu.R;
import com.emenu.common.BitmapLoader;
import com.emenu.common.MLog;

public class Cover extends Activity {

	private Button btnZh;
	private Button btnSw;
	private Button btnEn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cover);

		btnZh = (Button) findViewById(R.id.btnChinese);
		btnZh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});

		btnSw = (Button) findViewById(R.id.btnSwedish);
		btnSw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});

		btnEn = (Button) findViewById(R.id.btnEnglish);
		btnEn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		MLog.d("========Destory bitmaps");
		super.onDestroy();
		BitmapLoader.getInstance().recycleBitmaps();
	}
}

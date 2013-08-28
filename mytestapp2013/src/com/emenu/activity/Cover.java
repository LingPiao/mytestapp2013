package com.emenu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.emenu.R;
import com.emenu.common.BitmapLoader;
import com.emenu.common.Languages;
import com.emenu.common.MLog;
import com.emenu.common.XmlUtils;

public class Cover extends BaseActivity {

	private Button btnZh;
	private Button btnSw;
	private Button btnEn;
	private final Handler handler = new Handler();
	private Runnable defaultChoise;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cover);

		btnZh = (Button) findViewById(R.id.btnChinese);
		btnSw = (Button) findViewById(R.id.btnSwedish);
		btnEn = (Button) findViewById(R.id.btnEnglish);

		setAppTitle();
	}

	private void setLan(String lan) {
		handler.removeCallbacks(defaultChoise);
		Intent intent = new Intent(Cover.this, Main.class);
		String appPath = Environment.getExternalStorageDirectory().getPath();
		XmlUtils.build(appPath);
		XmlUtils.getInstance().setLanguage(lan);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();

		btnZh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setLan(Languages.zh_CN.name());
			}
		});

		btnSw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setLan(Languages.sv_SE.name());
			}
		});

		btnEn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				setLan(Languages.en_US.name());
			}
		});

		defaultChoise = new Runnable() {
			public void run() {
				setLan(Languages.en_US.name());
				handler.postDelayed(this, 30000);
			}
		};

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

package com.emenu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emenu.R;
import com.emenu.common.Languages;
import com.emenu.common.MLog;
import com.emenu.common.Utils;
import com.emenu.common.XmlUtils;

public class Cover extends BaseActivity {
	private static final String RST = " Restaurant";

	private Button btnZh;
	private Button btnSw;
	private Button btnEn;
	private Handler handler;
	private Runnable defaultChoise;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cover);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.coverLayout);
		layout.setBackground(getResources().getDrawable(R.drawable.cover_bg));

		btnZh = (Button) findViewById(R.id.btnChinese);
		btnSw = (Button) findViewById(R.id.btnSwedish);
		btnEn = (Button) findViewById(R.id.btnEnglish);

		setAppTitle();
		TextView rstName = (TextView) findViewById(R.id.rstName);
		rstName.setText(Utils.loadTitle() + RST);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void setLan(String lan) {
		if (handler != null && defaultChoise != null)
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

		handler = new Handler();
		defaultChoise = new Runnable() {
			@Override
			public void run() {
				MLog.d("===================Using English as the efault Language");
				setLan(Languages.en_US.name());
			}
		};
		handler.postDelayed(defaultChoise, 10000);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (handler != null && defaultChoise != null)
			handler.removeCallbacks(defaultChoise);
	}
}

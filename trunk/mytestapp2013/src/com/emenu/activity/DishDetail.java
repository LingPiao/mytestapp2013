package com.emenu.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.emenu.R;
import com.emenu.common.Constants;
import com.emenu.common.Order;
import com.emenu.common.XmlUtils;
import com.emenu.models.Dish;

public class DishDetail extends BaseActivity {

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dish_detail);

		final Dish dish = (Dish) getIntent().getSerializableExtra(Constants.DISH_KEY);

		setTitle(dish.getName());

		// Toast.makeText(this, "Dish[id=" + dish.getId() + ",Name=" +
		// dish.getName() + "] selected.", Toast.LENGTH_SHORT).show();
		String url = "file://" + XmlUtils.getInstance().getPath("/" + dish.getFile());

		WebView mWebView = (WebView) findViewById(R.id.dishView);
		mWebView.setWebChromeClient(new MyWebChromeClient());
		// mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginState(PluginState.ON);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.loadUrl(url);

		final EditText amount = (EditText) findViewById(R.id.amount);
		if (amount.getText() == null || amount.getText().length() < 1) {
			amount.setText("1");
		} else {
			int a = Integer.parseInt(amount.getText().toString());
			if (a < 0) {
				amount.setText("1");
			} else if (a > 1000) {
				amount.setText("1000");
			}
		}

		Button add = (Button) findViewById(R.id.btnAdd);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Order.getInstance().add(dish, Integer.parseInt(amount.getText().toString()));
				Toast.makeText(DishDetail.this, dish.getName() + " added to Favorite list", Toast.LENGTH_SHORT).show();
			}
		});
	}

	public class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			super.onShowCustomView(view, callback);
			if (view instanceof FrameLayout) {
				FrameLayout frame = (FrameLayout) view;
				if (frame.getFocusedChild() instanceof VideoView) {
					VideoView video = (VideoView) frame.getFocusedChild();
					frame.removeView(video);
					video.start();
				}
			}
		}
	}

}

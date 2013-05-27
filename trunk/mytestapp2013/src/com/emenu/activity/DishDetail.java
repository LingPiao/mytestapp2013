package com.emenu.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.VideoView;

import com.emenu.R;
import com.emenu.common.Constants;
import com.emenu.common.XmlUtils;
import com.emenu.models.Dish;

public class DishDetail extends Activity {

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dish_detail);

		Dish dish = (Dish) getIntent().getSerializableExtra(Constants.DISH_KEY);
		// Toast.makeText(this, "Dish[id=" + dish.getId() + ",Name=" +
		// dish.getName() + "] selected.", Toast.LENGTH_SHORT).show();
		String url = "file://" + XmlUtils.getInstance().getPath("/" + dish.getFile());

		WebView mWebView = (WebView) findViewById(R.id.dishView);
		mWebView.setWebChromeClient(new MyWebChromeClient());
		// mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		// mWebView.getSettings().setPluginState(PluginState.ON);
		mWebView.getSettings().setPluginsEnabled(true);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.loadUrl(url);

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

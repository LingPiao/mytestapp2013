package com.emenu.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.emenu.R;
import com.emenu.common.Constants;
import com.emenu.common.Order;
import com.emenu.common.XmlUtils;
import com.emenu.models.Dish;

public class DishDetail extends BaseActivity {

	private MyWebChromeClient myChromeClient = null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dish_detail);

		final Dish dish = (Dish) getIntent().getSerializableExtra(Constants.DISH_KEY);

		setTitle(dish.getName());

		String url = "file://" + XmlUtils.getInstance().getPath("/" + dish.getFile());

		WebView mWebView = (WebView) findViewById(R.id.dishView);
		myChromeClient = new MyWebChromeClient();
		mWebView.setWebChromeClient(myChromeClient);
		// mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginState(PluginState.ON);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setUseWideViewPort(true);
		mWebView.loadUrl(url);

		final TextView amount = (TextView) findViewById(R.id.amount);

		Button add = (Button) findViewById(R.id.btnAdd);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Order.getInstance().add(dish, Integer.parseInt(amount.getText().toString()));
				Toast.makeText(DishDetail.this, dish.getName() + " added to Favorite list", Toast.LENGTH_SHORT).show();
			}
		});

		ImageView ivMinus = (ImageView) findViewById(R.id.minus);
		ivMinus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int a = Integer.parseInt(amount.getText().toString());
				if (a > 1) {
					amount.setText(String.valueOf(a - 1));
				}
			}
		});

		ImageView ivAdd = (ImageView) findViewById(R.id.add);
		ivAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				int a = Integer.parseInt(amount.getText().toString());
				if (a < 100) {
					amount.setText(String.valueOf(a + 1));
				}
			}
		});

		TextView price = (TextView) findViewById(R.id.price);
		price.setText(String.valueOf(dish.getPrice()));
	}

	public class MyWebChromeClient extends WebChromeClient {
		private VideoView video = null;

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			super.onShowCustomView(view, callback);
			if (view instanceof FrameLayout) {
				FrameLayout frame = (FrameLayout) view;
				if (frame.getFocusedChild() instanceof VideoView) {
					video = (VideoView) frame.getFocusedChild();
					frame.removeView(video);
					video.start();
				}
			}
		}

		public void stop() {
			if (video != null) video.stopPlayback();
		}

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (myChromeClient != null) {
			myChromeClient.stop();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (myChromeClient != null) {
			myChromeClient.stop();
		}
	}

}

package com.emenu.activity;

import java.io.Serializable;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.emenu.R;
import com.emenu.common.Constants;
import com.emenu.common.OrderUtil;
import com.emenu.common.Utils;
import com.emenu.common.XmlUtils;
import com.emenu.models.Dish;

public class DishDetail extends BaseActivity {

	private MyWebChromeClient myChromeClient = null;
	private WebView mWebView = null;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		setContentView(R.layout.dish_detail);
		super.onResume();
		Serializable sobj = getIntent().getSerializableExtra(Constants.DISH_KEY);
		if (sobj == null) {
			msgbox("Data missing");
			return;
		}
		final Dish dish = (Dish) sobj;
		String dishFile = XmlUtils.getInstance().getPath("/" + dish.getFile());
		if (!Utils.isExists(dishFile)) {
			msgbox("Data Missing", "File[" + dishFile + "] not found!");
			return;
		}
		setTitle(Utils.getUniformTitle(dish.getName()));
		String url = "file://" + dishFile;

		mWebView = (WebView) findViewById(R.id.dishView);
		myChromeClient = new MyWebChromeClient();
		mWebView.setWebChromeClient(myChromeClient);
		// mWebView.setWebViewClient(new WebViewClient());
		// mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginState(PluginState.OFF);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setUseWideViewPort(true);
		// mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);
		// mWebView.setInitialScale(0);

		// String url="http://www.quirksmode.org/html5/tests/video.html";

		mWebView.loadUrl(url);

		final TextView dishNo = (TextView) findViewById(R.id.dishNumber);
		if (dish.getDishNumber() != null) {
			dishNo.setText("NO." + dish.getDishNumber());
		}

		final TextView amount = (TextView) findViewById(R.id.amount);

		Button add = (Button) findViewById(R.id.btnAdd);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				OrderUtil.getInstance().getOrder().add(dish, Integer.parseInt(amount.getText().toString()));
				Toast.makeText(DishDetail.this, dish.getName() + " added to MyOrder", Toast.LENGTH_SHORT).show();
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
		price.setText(Utils.formatPrice((dish.getPrice())));
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
		cleanWebView();
	}

	private void cleanWebView() {
		if (myChromeClient != null) {
			myChromeClient.stop();
		}
		if (mWebView != null) {
			mWebView.setFocusable(false);
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.webViewlayout);
			layout.removeView(mWebView);
			mWebView.removeAllViews();
			mWebView.destroy();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		cleanWebView();
	}

}

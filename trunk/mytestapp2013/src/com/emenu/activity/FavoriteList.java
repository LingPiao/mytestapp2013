package com.emenu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.emenu.R;
import com.emenu.adapter.FavoriteListAdapter;
import com.emenu.common.Order;

public class FavoriteList extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_list);

		final FavoriteListAdapter adapter = new FavoriteListAdapter(this);
		final ListView listview = (ListView) findViewById(R.id.favoriteList);
		listview.addHeaderView(LayoutInflater.from(this).inflate(R.layout.favorite_list_header, null));
		listview.setAdapter(adapter);

		final TextView totalPrice = (TextView) findViewById(R.id.txtTotalPrice);
		totalPrice.setText(String.valueOf(Order.getInstance().getTotalPrice()));

		Button cancle = (Button) findViewById(R.id.btnCancle);
		cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Order.getInstance().clear();
				startActivity(new Intent(FavoriteList.this, FavoriteList.class));
				finish();
			}
		});

		Button submit = (Button) findViewById(R.id.btnSubmit);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TO-DO Write the list to a file
			}
		});

	}

}

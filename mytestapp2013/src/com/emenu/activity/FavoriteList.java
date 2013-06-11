package com.emenu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

		updateTotalPrice();

		Button cancle = (Button) findViewById(R.id.btnCancle);
		cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Order.getInstance().clear();
				FavoriteList.this.startActivity(new Intent(FavoriteList.this, FavoriteList.class));
			}
		});

		Button submit = (Button) findViewById(R.id.btnSubmit);
		final TextView tbNo = (TextView) findViewById(R.id.txtTableNo);
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (Order.getInstance().getOrderItems() == null || Order.getInstance().getOrderItems().size() < 1) {
					FavoriteList.this.startActivity(new Intent(FavoriteList.this, DishList.class));
					finish();
				}
				boolean r = Order.getInstance().save(tbNo.getText().toString());
				if (r) {
					Order.getInstance().clear();
					FavoriteList.this.startActivity(new Intent(FavoriteList.this, DishList.class));
					finish();
				} else {
					Toast.makeText(FavoriteList.this, "Save order fail, retry later", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	public void updateTotalPrice() {
		TextView totalPrice = (TextView) findViewById(R.id.txtTotalPrice);
		totalPrice.setText(String.valueOf(Order.getInstance().getTotalPrice()));
	}

}

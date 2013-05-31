package com.emenu.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.emenu.R;
import com.emenu.adapter.FavoriteListAdapter;
import com.emenu.models.OrderItem;

public class FavoriteList extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorite_list);

		final FavoriteListAdapter adapter = new FavoriteListAdapter(this);
		final ListView listview = (ListView) findViewById(R.id.favoriteList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final OrderItem orderItem = (OrderItem) parent.getItemAtPosition(position);
				Toast.makeText(FavoriteList.this, "orderItem:" + orderItem.getDish().getName(), Toast.LENGTH_SHORT).show();
			}
		});

	}

}

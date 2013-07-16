package com.emenu.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.emenu.R;
import com.emenu.adapter.DishListAdapter;
import com.emenu.common.BitmapLoader;
import com.emenu.common.Constants;
import com.emenu.common.MLog;
import com.emenu.models.Dish;

public class DishList extends BaseActivity {
	private DishListAdapter adapter = null;
	boolean isDataReady = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish_list);
		isDataReady = checkEnv();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isDataReady) {
			return;
		}
		List<Dish> dishes = new ArrayList<Dish>();
		if (getIntent() != null && getIntent().getExtras() != null) {
			Object o = (com.emenu.models.MenuItem) getIntent().getSerializableExtra(Constants.SELECTED_MENU_ITEM_KEY);
			if (o != null) {
				com.emenu.models.MenuItem selectedMi = (com.emenu.models.MenuItem) o;
				selectedId = selectedMi.getId();
				if (selectedId > 0) {
					selectedCategory = selectedMi.getName();
					dishes = dao.loadDishes(selectedId);
				}
			}
			MLog.d("GetExtra data[selectedId=" + selectedId + "]");
		} else {
			dishes = dao.loadDishes();
		}

		setTitle("Dish List/" + selectedCategory);

		if (dishes.size() < 1) {
			msgbox("No Dishes found under Category[ " + selectedCategory + "]");
			return;
		}

		adapter = new DishListAdapter(this, dishes);
		final ListView listview = (ListView) findViewById(R.id.dishList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final Dish dish = (Dish) parent.getItemAtPosition(position);
				Intent intent = new Intent(DishList.this, DishDetail.class);
				intent.putExtra(Constants.DISH_KEY, dish);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onDestroy() {
		MLog.d("========Destory bitmaps");
		super.onDestroy();
		BitmapLoader.getInstance().recycleBitmaps();
	}
}

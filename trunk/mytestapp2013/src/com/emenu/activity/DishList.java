package com.emenu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.emenu.R;
import com.emenu.adapter.DishListAdapter;
import com.emenu.models.Dish;

public class DishList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish_list);

		final ListView listview = (ListView) findViewById(R.id.dishList);

		final DishListAdapter adapter = new DishListAdapter(this, getDishes());
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final Dish dish = (Dish) parent.getItemAtPosition(position);
				Toast.makeText(DishList.this, "Dish[id=" + dish.getId() + ",Name=" + dish.getName() + "] selected.",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private List<Dish> getDishes() {
		List<Dish> dishes = new ArrayList<Dish>();
		Dish ics = new Dish();
		ics.setId(1);
		ics.setName("Ice Cream Sandwich");
		ics.setPrice(8);

		dishes.add(ics);

		Dish ss = new Dish();
		ss.setId(2);
		ss.setName("Steak Sandwich");
		ss.setPrice(10);

		dishes.add(ss);

		return dishes;
	}

}

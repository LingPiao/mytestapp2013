package com.emenu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.emenu.R;
import com.emenu.adapter.DishListAdapter;

public class DishList extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish_list);

		final ListView listview = (ListView) findViewById(R.id.dishList);
		String[] values = new String[] { "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
				"Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2", "Android", "iPhone", "WindowsMobile" };
		final DishListAdapter adapter = new DishListAdapter(this, values);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				Toast.makeText(DishList.this, "Dish:" + item + " selected.", Toast.LENGTH_SHORT).show();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

package com.emenu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.emenu.R;
import com.emenu.adapter.DishListAdapter;
import com.emenu.common.MLog;
import com.emenu.common.XmlUtils;
import com.emenu.dao.EMenuDao;
import com.emenu.dao.impl.EMenuDaoImpl;
import com.emenu.models.Dish;

public class DishList extends Activity {
	private EMenuDao dao = new EMenuDaoImpl();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish_list);
		MLog.d("Environment.getExternalStorageDirectory().getPath()=" + Environment.getExternalStorageDirectory().getPath());
		XmlUtils.build(Environment.getExternalStorageDirectory().getPath());

		final ListView listview = (ListView) findViewById(R.id.dishList);

		final DishListAdapter adapter = new DishListAdapter(this, dao.loadDishes());
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.category) {
			startActivity(new Intent(this, DishDetail.class));
			// LinearLayout l1 = new LinearLayout(this);
			// ListView lv = new ListView(this);
			// l1.addView(lv);
			//
			// String[] values = new String[10];
			// for (int i = 0; i < 5; i++) {
			// values[i] = "" + i;
			// }
			//
			// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			// android.R.layout.simple_list_item_1, values);
			// lv.setAdapter(adapter);
			//
			// PopupWindow pw = new PopupWindow(l1,
			// LinearLayout.LayoutParams.WRAP_CONTENT,
			// LinearLayout.LayoutParams.WRAP_CONTENT, true);
			// pw.setOutsideTouchable(true);
			// pw.setBackgroundDrawable(new
			// ColorDrawable(android.R.color.transparent));
			// pw.setFocusable(true);
			// // popUp.showAtLocation(l1, Gravity.BOTTOM, 0, 0);
			// pw.showAsDropDown(this.findViewById(R.id.category));

		} else {
			return super.onOptionsItemSelected(item);
		}

		return true;
	}
}

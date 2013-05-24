package com.emenu.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

	private String[] getCategory() {
		String[] r = new String[0];
		List<com.emenu.models.MenuItem> mis = dao.loadMenus();
		if (mis.size() < 1) {
			return r;
		}
		r = new String[mis.size()];
		int i = 0;
		for (com.emenu.models.MenuItem mi : mis) {
			r[i++] = mi.getName();
		}
		return r;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.category) {
			// startActivity(new Intent(this, DishDetail.class));
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
			// l1.setBackgroundColor(Color.GRAY);
			// PopupWindow pw = new PopupWindow(l1, 100, 300, true);
			// //
			// pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_bg));
			// pw.setTouchable(true);
			// pw.setOutsideTouchable(true);
			// pw.setBackgroundDrawable(new BitmapDrawable());
			// // pw.showAtLocation(getWindow().getDecorView(),
			// Gravity.NO_GRAVITY,
			// // 30, 30);
			// pw.showAtLocation(this.findViewById(R.id.category), Gravity.LEFT
			// | Gravity.BOTTOM, 10, 10);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Chose a category");
			builder.setSingleChoiceItems(getCategory(), 0, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					dialog.dismiss();
					Toast.makeText(DishList.this, item + " selected", Toast.LENGTH_SHORT).show();
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

		} else {
			return super.onOptionsItemSelected(item);
		}

		return true;
	}
}
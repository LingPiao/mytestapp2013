package com.emenu.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.emenu.R;
import com.emenu.adapter.DishListAdapter;
import com.emenu.common.Constants;
import com.emenu.common.FileUtil;
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

		checkEnv();

		List<Dish> dishes = null;
		long selectedId = 0;
		if (getIntent() != null && getIntent().getExtras() != null) {
			selectedId = getIntent().getExtras().getLong(Constants.SELECTED_MENU_ITEM_KEY);
			MLog.d("GetExtra data[selectedId=" + selectedId + "]");
		}
		if (selectedId > 0) {
			dishes = dao.loadDishes(selectedId);
		} else {
			dishes = dao.loadDishes();
		}

		final DishListAdapter adapter = new DishListAdapter(this, dishes);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.category) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Chose a category");
			builder.setSingleChoiceItems(getCategory(), 0, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					dialog.dismiss();
					Intent intent = new Intent(DishList.this, DishList.class);
					long selecedId = getSelectedCategoryId(item);
					MLog.d("PutExtra data[selectedId=" + selecedId + "]");
					intent.putExtra(Constants.SELECTED_MENU_ITEM_KEY, selecedId);
					startActivity(intent);
				}
			});
			builder.create().show();
		} else {
			return super.onOptionsItemSelected(item);
		}

		return true;
	}

	private void checkEnv() {
		setTitle("Loading...");
		String title = FileUtil.loadTitle();
		String appPath = Environment.getExternalStorageDirectory().getPath();
		MLog.d("Loaded tile:" + title);
		if (title == null) {
			msgbox("Loading title error,check " + appPath + Constants.TITLE_FILE, true);
			// finish();
			return;
		}
		setTitle(title);

		MLog.d("Checking data...");
		String chkDataMsg = FileUtil.isDataReady();
		if (chkDataMsg != null) {
			msgbox(chkDataMsg, true);
			// finish();
			return;
		}
		MLog.d("Checking data passed.");

		XmlUtils.build(appPath);
	}

	private void msgbox(String msg, final boolean exitRequired) {
		new AlertDialog.Builder(this).setTitle("Information").setMessage(msg).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				if (exitRequired) {
					finish();
				}
			}
		}).show();
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

	private long getSelectedCategoryId(int index) {
		List<com.emenu.models.MenuItem> mis = dao.loadMenus();
		if (mis.size() < 1) {
			return 0;
		}
		int i = 0;
		for (com.emenu.models.MenuItem mi : mis) {
			if (index == i++) {
				return mi.getId();
			}
		}
		return 0;
	}
}

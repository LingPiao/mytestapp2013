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

import com.emenu.R;
import com.emenu.common.Constants;
import com.emenu.common.MLog;
import com.emenu.common.Utils;
import com.emenu.common.XmlUtils;
import com.emenu.dao.EMenuDao;
import com.emenu.dao.impl.EMenuDaoImpl;

public class BaseActivity extends Activity {
	protected EMenuDao dao = new EMenuDaoImpl();
	protected long selectedId = 0;
	protected String selectedCategory = "All";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
			builder.setSingleChoiceItems(getCategory(), (int) selectedId, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					dialog.dismiss();
					Intent intent = new Intent(BaseActivity.this, DishList.class);
					com.emenu.models.MenuItem selectedMi = getSelectedCategory(item);
					MLog.d("PutExtra data[selectedId=" + selectedMi.getId() + "]");
					intent.putExtra(Constants.SELECTED_MENU_ITEM_KEY, selectedMi);
					startActivity(intent);
					finish();
				}
			});
			builder.create().show();
		} else if (id == R.id.favorite) {
			Intent intent = new Intent(BaseActivity.this, FavoriteList.class);
			startActivity(intent);
			finish();
		} else {
			return super.onOptionsItemSelected(item);
		}

		return true;
	}

	protected void checkEnv() {
		setTitle("Loading...");
		String title = Utils.loadTitle();
		String appPath = Environment.getExternalStorageDirectory().getPath();
		MLog.d("Loaded tile:" + title);
		if (title == null) {
			msgbox("Loading title error,check " + appPath + Constants.TITLE_FILE, true);
			// finish();
			return;
		}
		setTitle(title);

		MLog.d("Checking data...");
		String chkDataMsg = Utils.isDataReady();
		if (chkDataMsg != null) {
			msgbox(chkDataMsg, true);
			// finish();
			return;
		}
		MLog.d("Checking data passed.");

		XmlUtils.build(appPath);
	}

	protected void msgbox(String msg) {
		msgbox(msg, false);
	}

	private void msgbox(String msg, final boolean exitRequired) {
		new AlertDialog.Builder(this).setTitle("Information").setMessage(msg)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (exitRequired) {
							finish();
						}
					}
				}).show();
	}

	protected String[] getCategory() {
		String[] r = new String[0];
		List<com.emenu.models.MenuItem> mis = dao.loadMenus();
		if (mis.size() < 1) {
			return r;
		}
		r = new String[mis.size() + 1];
		int i = 1;
		r[0] = "All";
		for (com.emenu.models.MenuItem mi : mis) {
			r[i++] = mi.getName();
		}
		return r;
	}

	protected com.emenu.models.MenuItem getSelectedCategory(int index) {
		List<com.emenu.models.MenuItem> mis = dao.loadMenus();
		com.emenu.models.MenuItem item = new com.emenu.models.MenuItem();
		if (mis.size() < 1) {
			return item;
		}
		int i = 0;
		index = index - 1; // The index of All excluded
		for (com.emenu.models.MenuItem mi : mis) {
			if (index == i++) {
				return mi;
			}
		}
		return item;
	}
}

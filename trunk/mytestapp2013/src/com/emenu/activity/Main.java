package com.emenu.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.emenu.R;
import com.emenu.adapter.CategoryListAdapter;
import com.emenu.common.BitmapLoader;
import com.emenu.common.Constants;
import com.emenu.common.MLog;
import com.emenu.models.Dish;

public class Main extends BaseActivity {

	private CategoryListAdapter adapter = null;
	private List<com.emenu.models.MenuItem> menus = new ArrayList<com.emenu.models.MenuItem>();
	private TextView speListTitle = null;
	private Timer timer = null;

	boolean isDataReady = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		isDataReady = checkEnv();
		speListTitle = (TextView) findViewById(R.id.speListTitle);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isDataReady) {
			return;
		}
		List<Dish> dishes = new ArrayList<Dish>();
		List<Dish> specials = new ArrayList<Dish>();
		menus = dao.loadMenus();
		dishes = dao.loadDishes();
		if (dishes.size() < 1) {
			msgbox("No Dishes found under Category[ " + selectedCategory + "]");
			return;
		}
		specials = getSpecials(dishes);
		if (specials.size() > 0) {
			showSpecials(specials);
			speListTitle.setVisibility(View.VISIBLE);
		} else {
			speListTitle.setVisibility(View.GONE);
		}

		adapter = new CategoryListAdapter(this, menus);
		final ListView listview = (ListView) findViewById(R.id.menuList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				final com.emenu.models.MenuItem mi = (com.emenu.models.MenuItem) parent.getItemAtPosition(position);
				Intent intent = new Intent(Main.this, DishList.class);
				intent.putExtra(Constants.SELECTED_MENU_ITEM_KEY, mi);
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

	private void showSpecials(List<Dish> specials) {
		List<Dish> speList = new ArrayList<Dish>();
		for (Dish dish : specials) {
			speList.add(dish);
		}
		final HorizontalScrollView speHsv = (HorizontalScrollView) findViewById(R.id.speHsv);
		speHsv.setVisibility(View.VISIBLE);

		LinearLayout specialListLayout = (LinearLayout) findViewById(R.id.specialListLayout);

		final int count = speList.size();
		for (final Dish dish : speList) {
			ImageView img = new ImageView(this, null, R.style.SpecialImage);
			BitmapLoader.getInstance().boundImage(img, dish.getImage());
			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Main.this, DishDetail.class);
					intent.putExtra(Constants.DISH_KEY, dish);
					startActivity(intent);
				}

			});
			LayoutParams lp = new LayoutParams(220, 220);
			lp.setMargins(3, 3, 3, 3);
			img.setLayoutParams(lp);
			img.setScaleType(ScaleType.FIT_XY);

			specialListLayout.addView(img);
		}

		TimerTask task = new TimerTask() {
			int i = 0;

			public void run() {
				// MLog.d("==========Scrolling ...");
				speHsv.smoothScrollTo(i * 220 + 3, 0);
				if (i >= count - 3) {
					i = 0;
				} else {
					i++;
				}
			}
		};
		if (timer == null) {
			timer = new Timer("ScrollTimer");
			timer.scheduleAtFixedRate(task, 1000, 2000);
		}
	}

	private List<Dish> getSpecials(List<Dish> dishes) {
		List<Dish> s = new ArrayList<Dish>();
		List<Long> speIds = new ArrayList<Long>();
		for (com.emenu.models.MenuItem mi : menus) {
			if (mi.isSpecial()) {
				speIds.add(mi.getId());
			}
		}
		for (Dish d : dishes) {
			List<Long> beLongsTo = d.getBelongsTo();
			for (Long id : beLongsTo) {
				if (speIds.contains(id)) {
					s.add(d);
				}
			}
		}
		return s;
	}

}

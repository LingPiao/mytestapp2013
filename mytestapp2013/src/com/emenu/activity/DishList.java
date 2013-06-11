package com.emenu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.emenu.R;
import com.emenu.adapter.DishListAdapter;
import com.emenu.common.Constants;
import com.emenu.common.MLog;
import com.emenu.models.Dish;

public class DishList extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish_list);

		if (!checkEnv()) {
			return;
		}

		List<Dish> dishes = new ArrayList<Dish>();
		List<Dish> specials = new ArrayList<Dish>();
		boolean showSpecials = true;

		if (getIntent() != null && getIntent().getExtras() != null) {
			Object o = (com.emenu.models.MenuItem) getIntent().getSerializableExtra(Constants.SELECTED_MENU_ITEM_KEY);
			if (o != null) {
				com.emenu.models.MenuItem selectedMi = (com.emenu.models.MenuItem) o;
				selectedId = selectedMi.getId();
				if (selectedId > 0) {
					selectedCategory = selectedMi.getName();
				}
			}
			MLog.d("GetExtra data[selectedId=" + selectedId + "]");
		}
		if (selectedId > 0) {
			dishes = dao.loadDishes(selectedId);
			showSpecials = false;
		} else {
			dishes = dao.loadDishes();
		}

		if (dishes.size() < 1) {
			showSpecials = false;
			msgbox("No Dishes found under Category[ " + selectedCategory + "]");
		}

		if (showSpecials) {
			specials = getSpecials(dishes);
			if (specials.size() > 0) {
				showSpecials(specials);
			}
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

	private void showSpecials(List<Dish> specials) {
		final HorizontalScrollView speHsv = (HorizontalScrollView) findViewById(R.id.speHsv);
		speHsv.setVisibility(View.VISIBLE);

		LinearLayout specialListLayout = (LinearLayout) findViewById(R.id.specialListLayout);
		final int count = specials.size();
		for (final Dish dish : specials) {
			ImageView img = new ImageView(this);
			File imgf = new File(dish.getImage());
			if (imgf.exists()) {
				Bitmap dimg = BitmapFactory.decodeFile(imgf.getAbsolutePath());
				img.setImageBitmap(dimg);
			} else {
				img.setImageResource(R.drawable.default_images);
			}

			img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(DishList.this, DishDetail.class);
					intent.putExtra(Constants.DISH_KEY, dish);
					startActivity(intent);
				}

			});
			LayoutParams lp = new LayoutParams(300, 300);
			lp.setMargins(3, 3, 3, 3);
			img.setLayoutParams(lp);
			specialListLayout.addView(img);
		}

		TimerTask task = new TimerTask() {
			int i = 0;

			public void run() {
				// MLog.d("==========Scrolling ...");
				speHsv.smoothScrollTo(i * 300, (i + 1) * 300);
				if (i >= count - 1) {
					i = 0;
				} else {
					i++;
				}
			}
		};

		new Timer().scheduleAtFixedRate(task, 1000, 3000);
	}

	private List<Dish> getSpecials(List<Dish> dishes) {
		List<Dish> s = new ArrayList<Dish>();
		List<com.emenu.models.MenuItem> menus = dao.loadMenus();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

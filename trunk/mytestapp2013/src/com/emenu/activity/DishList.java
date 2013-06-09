package com.emenu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

		checkEnv();

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
				final HorizontalScrollView speHsv = (HorizontalScrollView) findViewById(R.id.speHsv);
				speHsv.setVisibility(View.VISIBLE);

				LinearLayout specialListLayout = (LinearLayout) findViewById(R.id.specialListLayout);

				for (Dish dish : specials) {
					ImageView img = new ImageView(this);
					File imgf = new File(dish.getImage());
					if (imgf.exists()) {
						Bitmap dimg = BitmapFactory.decodeFile(imgf.getAbsolutePath());
						img.setImageBitmap(dimg);
					} else {
						img.setImageResource(R.drawable.default_images);
					}
					specialListLayout.addView(img);
				}

				speHsv.postDelayed(new Runnable() {
					public void run() {
						speHsv.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
					}
				}, 1000L);

				// Gallery gallery = (Gallery) findViewById(R.id.speGallery);
				// SpecialListAdapter imageAdapter = new
				// SpecialListAdapter(this, dishes);
				// gallery.setAdapter(imageAdapter);
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

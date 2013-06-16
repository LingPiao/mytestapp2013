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
import android.widget.AbsListView.RecyclerListener;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.emenu.R;
import com.emenu.adapter.DishListAdapter;
import com.emenu.common.BitmapLoader;
import com.emenu.common.Constants;
import com.emenu.common.MLog;
import com.emenu.models.Dish;

public class DishList extends BaseActivity {

	private DishListAdapter adapter;

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

		listview.setRecyclerListener(new RecyclerListener() {
			@Override
			public void onMovedToScrapHeap(View view) {
				// Release strong reference when a view is recycled
				final ImageView imageView = (ImageView) view.findViewById(R.id.img);
				if (imageView != null) {
					MLog.d("========= Remove image bitmap");
					imageView.setImageBitmap(null);
				}
			}
		});
	}

	private void showSpecials(List<Dish> specials) {

		List<Dish> speList = new ArrayList<Dish>();
		for (Dish dish : specials) {
			speList.add(dish);
		}
		// for (Dish dish : specials) {
		// speList.add(dish);
		// }
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
					Intent intent = new Intent(DishList.this, DishDetail.class);
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
				speHsv.smoothScrollTo(i * 300, 0);
				if (i >= count - 3) {
					i = 0;
				} else {
					i++;
				}
			}
		};

		new Timer().scheduleAtFixedRate(task, 1000, 2000);
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

	@Override
	public void onDestroy() {
		MLog.d("========Destory bitmaps");
		super.onDestroy();
		BitmapLoader.getInstance().recycleBitmaps();
	}

}

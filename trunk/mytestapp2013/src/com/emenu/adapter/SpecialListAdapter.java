package com.emenu.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.emenu.R;
import com.emenu.models.Dish;

public class SpecialListAdapter extends BaseAdapter {
	private List<Dish> dishes = new ArrayList<Dish>();
	private Context context = null;

	public SpecialListAdapter(Context context, List<Dish> dishes) {
		this.dishes = dishes;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Dish d = dishes.get(position);
		ImageView img = new ImageView(context);
		File imgf = new File(d.getImage());
		if (imgf.exists()) {
			Bitmap dimg = BitmapFactory.decodeFile(imgf.getAbsolutePath());
			img.setImageBitmap(dimg);
		} else {
			img.setImageResource(R.drawable.default_images);
		}
		return img;
	}

	@Override
	public int getCount() {
		return dishes.size();
	}

	@Override
	public Object getItem(int posistion) {
		return dishes.get(posistion);
	}

	@Override
	public long getItemId(int posistion) {
		return dishes.get(posistion).getId();
	}
}

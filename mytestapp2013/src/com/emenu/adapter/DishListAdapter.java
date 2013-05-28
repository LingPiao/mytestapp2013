package com.emenu.adapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emenu.R;
import com.emenu.common.MLog;
import com.emenu.common.Utils;
import com.emenu.models.Dish;

public class DishListAdapter extends BaseAdapter {
	private List<Dish> dishes = new ArrayList<Dish>();
	LayoutInflater inflater = null;

	public DishListAdapter(Context context, List<Dish> dishes) {
		this.dishes = dishes;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Dish d = dishes.get(position);
		View rowView = inflater.inflate(R.layout.dish_row, parent, false);
		TextView name = (TextView) rowView.findViewById(R.id.dishName);
		TextView desc = (TextView) rowView.findViewById(R.id.desc);
		TextView price = (TextView) rowView.findViewById(R.id.price);
		ImageView img = (ImageView) rowView.findViewById(R.id.img);

		File imgf = new File(d.getImage());
		MLog.d("==============ImageFile=" + d.getImage() + ",absPath=" + imgf.getPath() + ",exists=" + imgf.exists());
		if (imgf.exists()) {
			Bitmap dimg = BitmapFactory.decodeFile(imgf.getAbsolutePath());
			img.setImageBitmap(dimg);
		} else {
			img.setImageResource(R.drawable.default_images);
		}

		name.setText(d.getName());
		desc.setText(d.getIntroduction());
		price.setText(Utils.formatPrice(d.getPrice()));
		return rowView;
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

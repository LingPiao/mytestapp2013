package com.emenu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emenu.R;
import com.emenu.common.BitmapLoader;
import com.emenu.common.Utils;
import com.emenu.models.Dish;

public class DishListAdapter extends BaseAdapter {
	private List<Dish> dishes = new ArrayList<Dish>();
	private LayoutInflater inflater = null;

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

		BitmapLoader.getInstance().boundImage(img, d.getImage());

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

class ViewHolder {

	private TextView nameView;
	private TextView descView;
	private TextView priceView;
	private ImageView imgView;

	public TextView getNameView() {
		return nameView;
	}

	public void setNameView(TextView nameView) {
		this.nameView = nameView;
	}

	public TextView getDescView() {
		return descView;
	}

	public void setDescView(TextView descView) {
		this.descView = descView;
	}

	public TextView getPriceView() {
		return priceView;
	}

	public void setPriceView(TextView priceView) {
		this.priceView = priceView;
	}

	public ImageView getImgView() {
		return imgView;
	}

	public void setImgView(ImageView imgView) {
		this.imgView = imgView;
	}

}

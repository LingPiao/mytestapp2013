package com.emenu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emenu.R;

public class DishListAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;

	public DishListAdapter(Context context, String[] values) {
		super(context, R.layout.dish_row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.dish_row, parent, false);
		TextView desc = (TextView) rowView.findViewById(R.id.desc);
		TextView price = (TextView) rowView.findViewById(R.id.price);
		ImageView img = (ImageView) rowView.findViewById(R.id.img);
		desc.setText(values[position]);
		img.setImageResource(R.drawable.ic_launcher);
		price.setText("$10.00");
		return rowView;
	}
}

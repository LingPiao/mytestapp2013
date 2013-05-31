package com.emenu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emenu.R;
import com.emenu.common.Order;
import com.emenu.models.Dish;
import com.emenu.models.OrderItem;

public class FavoriteListAdapter extends BaseAdapter {
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	LayoutInflater inflater = null;

	public FavoriteListAdapter(Context context) {
		this.orderItems = Order.getInstance().getOrderItems();
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		OrderItem oi = orderItems.get(position);
		Dish dish = oi.getDish();
		View rowView = inflater.inflate(R.layout.favorite_row, parent, false);

		TextView sn = (TextView) rowView.findViewById(R.id.favorite_row_sn);
		TextView dn = (TextView) rowView.findViewById(R.id.favorite_row_dishName);
		TextView pr = (TextView) rowView.findViewById(R.id.favorite_row_price);
		TextView am = (TextView) rowView.findViewById(R.id.favorite_row_amount);
		TextView sm = (TextView) rowView.findViewById(R.id.favorite_row_sum);

		//sn.setText(position + 1);
		dn.setText(dish.getName());
		pr.setText(String.valueOf(dish.getPrice()));
		//am.setText(oi.getAmount());
		sm.setText(String.valueOf(oi.getAmount() * dish.getPrice()));

		return rowView;
	}

	@Override
	public int getCount() {
		return orderItems.size();
	}

	@Override
	public Object getItem(int posistion) {
		return orderItems.get(posistion);
	}

	@Override
	public long getItemId(int posistion) {
		return orderItems.get(posistion).getDish().getId();
	}
}

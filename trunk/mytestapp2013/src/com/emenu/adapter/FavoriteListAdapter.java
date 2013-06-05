package com.emenu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emenu.R;
import com.emenu.activity.DishList;
import com.emenu.common.Order;
import com.emenu.models.Dish;
import com.emenu.models.OrderItem;

public class FavoriteListAdapter extends BaseAdapter {
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	LayoutInflater inflater = null;
	private Context context;

	public FavoriteListAdapter(Context context) {
		this.context = context;
		this.orderItems = Order.getInstance().getOrderItems();
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final OrderItem oi = orderItems.get(position);
		Dish dish = oi.getDish();
		View rowView = inflater.inflate(R.layout.favorite_row, parent, false);

		TextView sn = (TextView) rowView.findViewById(R.id.favorite_row_sn);
		TextView dn = (TextView) rowView.findViewById(R.id.favorite_row_dishName);
		TextView pr = (TextView) rowView.findViewById(R.id.favorite_row_price);
		TextView am = (TextView) rowView.findViewById(R.id.favorite_row_amount);
		TextView sm = (TextView) rowView.findViewById(R.id.favorite_row_sum);
		ImageView ivRemove = (ImageView) rowView.findViewById(R.id.removeItem);

		sn.setText(String.valueOf(position + 1));
		dn.setText(dish.getName());
		pr.setText(String.valueOf(dish.getPrice()));
		am.setText(String.valueOf(oi.getAmount()));
		sm.setText(String.valueOf(oi.getAmount() * dish.getPrice()));

		ivRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Order.getInstance().remove(oi);
				context.startActivity(new Intent(context, DishList.class));
			}
		});

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

package com.emenu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emenu.R;
import com.emenu.activity.FavoriteList;
import com.emenu.common.Order;
import com.emenu.models.Dish;
import com.emenu.models.OrderItem;

public class FavoriteListAdapter extends BaseAdapter {
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	private LayoutInflater inflater = null;
	private FavoriteList listActivity = null;

	public FavoriteListAdapter(FavoriteList activity) {
		this.orderItems = Order.getInstance().getOrderItems();
		this.listActivity = activity;
		this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final OrderItem oi = orderItems.get(position);
		View rowView = inflater.inflate(R.layout.favorite_row, parent, false);
		fillView(position, oi, rowView);

		ImageView ivRemove = (ImageView) rowView.findViewById(R.id.removeItem);
		ivRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Order.getInstance().remove(oi);
				FavoriteListAdapter.this.notifyDataSetChanged();
			}
		});

		ImageView ivMinus = (ImageView) rowView.findViewById(R.id.minus);
		ivMinus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				oi.decline();
				FavoriteListAdapter.this.notifyDataSetChanged();
				listActivity.updateTotalPrice();
			}
		});

		ImageView ivAdd = (ImageView) rowView.findViewById(R.id.add);
		ivAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				oi.rise();
				FavoriteListAdapter.this.notifyDataSetChanged();
				listActivity.updateTotalPrice();
			}
		});
		return rowView;
	}

	private void fillView(int position, final OrderItem oi, View rowView) {
		Dish dish = oi.getDish();
		TextView sn = (TextView) rowView.findViewById(R.id.favorite_row_sn);
		TextView dn = (TextView) rowView.findViewById(R.id.favorite_row_dishName);
		TextView pr = (TextView) rowView.findViewById(R.id.favorite_row_price);
		TextView am = (TextView) rowView.findViewById(R.id.favorite_row_amount);
		TextView sm = (TextView) rowView.findViewById(R.id.favorite_row_sum);

		sn.setText(String.valueOf(position + 1));
		dn.setText(dish.getName());
		pr.setText(String.valueOf(dish.getPrice()));
		am.setText(String.valueOf(oi.getAmount()));
		sm.setText(String.valueOf(oi.getAmount() * dish.getPrice()));
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

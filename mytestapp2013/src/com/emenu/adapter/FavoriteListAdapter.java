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
import com.emenu.common.Utils;
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
		FavoriteViewHolder fvh = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.favorite_row, parent, false);
			fvh = new FavoriteViewHolder(convertView);
			convertView.setTag(fvh);
		} else {
			fvh = (FavoriteViewHolder) convertView.getTag();
		}
		fillData(position, oi, fvh);
		return convertView;
	}

	private void fillData(int position, final OrderItem oi, FavoriteViewHolder fvh) {
		Dish dish = oi.getDish();
		fvh.sn.setText(String.valueOf(position + 1));
		fvh.dn.setText(dish.getName());
		fvh.pr.setText(Utils.formatPrice(dish.getPrice()));
		fvh.am.setText(String.valueOf(oi.getAmount()));
		fvh.sm.setText(Utils.formatPrice(oi.getAmount() * dish.getPrice()));

		// Add event listeners for image buttons
		fvh.ivRemove.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Order.getInstance().remove(oi);
				FavoriteListAdapter.this.notifyDataSetChanged();
				listActivity.updateTotalPrice();
			}
		});

		fvh.ivMinus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				oi.decline();
				FavoriteListAdapter.this.notifyDataSetChanged();
				listActivity.updateTotalPrice();
			}
		});

		fvh.ivAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				oi.rise();
				FavoriteListAdapter.this.notifyDataSetChanged();
				listActivity.updateTotalPrice();
			}
		});
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

class FavoriteViewHolder {
	TextView sn;
	TextView dn;
	TextView pr;
	TextView am;
	TextView sm;
	ImageView ivRemove;
	ImageView ivMinus;
	ImageView ivAdd;

	public FavoriteViewHolder(View view) {
		sn = (TextView) view.findViewById(R.id.favorite_row_sn);
		dn = (TextView) view.findViewById(R.id.favorite_row_dishName);
		pr = (TextView) view.findViewById(R.id.favorite_row_price);
		am = (TextView) view.findViewById(R.id.favorite_row_amount);
		sm = (TextView) view.findViewById(R.id.favorite_row_sum);
		ivRemove = (ImageView) view.findViewById(R.id.removeItem);
		ivMinus = (ImageView) view.findViewById(R.id.minus);
		ivAdd = (ImageView) view.findViewById(R.id.add);
	}
}

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
import com.emenu.activity.OrderHis;
import com.emenu.common.Utils;
import com.emenu.models.Dish;
import com.emenu.models.OrderItem;

public class OrderHisListAdapter extends BaseAdapter {
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();
	private LayoutInflater inflater = null;

	public OrderHisListAdapter(OrderHis activity) {
		this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final OrderItem oi = orderItems.get(position);
		OrderHisViewHolder ohvh = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.order_his_row, parent, false);
			ohvh = new OrderHisViewHolder(convertView);
			convertView.setTag(ohvh);
		} else {
			ohvh = (OrderHisViewHolder) convertView.getTag();
		}
		fillData(position, oi, ohvh);
		return convertView;
	}

	private void fillData(int position, final OrderItem oi, OrderHisViewHolder ohvh) {
		Dish dish = oi.getDish();
		ohvh.sn.setText(String.valueOf(position + 1));
		if (dish.getDishNumber() != null) {
			ohvh.dn.setText(dish.getName() + "[" + dish.getDishNumber() + "]");
		} else {
			ohvh.dn.setText(dish.getName());
		}
		ohvh.pr.setText(Utils.formatPrice(dish.getPrice()));
		ohvh.am.setText(String.valueOf(oi.getAmount()));
		ohvh.sm.setText(Utils.formatPrice(oi.getAmount() * dish.getPrice()));
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

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

}

class OrderHisViewHolder {
	TextView sn;
	TextView dn;
	TextView pr;
	TextView am;
	TextView sm;

	public OrderHisViewHolder(View view) {
		sn = (TextView) view.findViewById(R.id.favorite_row_sn);
		dn = (TextView) view.findViewById(R.id.favorite_row_dishName);
		pr = (TextView) view.findViewById(R.id.favorite_row_price);
		am = (TextView) view.findViewById(R.id.favorite_row_amount);
		sm = (TextView) view.findViewById(R.id.favorite_row_sum);
	}
}

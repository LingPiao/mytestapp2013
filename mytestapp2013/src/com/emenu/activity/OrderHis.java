package com.emenu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emenu.R;
import com.emenu.adapter.OrderHisListAdapter;
import com.emenu.common.Order;
import com.emenu.common.OrderUtil;
import com.emenu.common.Utils;

public class OrderHis extends Activity {

	private int orderIndex = 0;
	private Order currentOrderHis = null;
	private List<Order> orderHisList = new ArrayList<Order>();
	private TextView tbNo = null;
	private TextView totalPrice = null;
	private TextView orderDate = null;
	private OrderHisListAdapter adapter = null;
	private ListView listview = null;
	private Button next;
	private Button pre;
	private TextView txtHisCountInfo = null;
	private int total = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_his);
		listview = (ListView) findViewById(R.id.hisList);
		listview.addHeaderView(LayoutInflater.from(this).inflate(R.layout.favorite_list_header, null));
		tbNo = (TextView) findViewById(R.id.txtTableNo);
		totalPrice = (TextView) findViewById(R.id.txtTotalPrice);
		orderDate = (TextView) findViewById(R.id.txtOrderDate);
		txtHisCountInfo = (TextView) findViewById(R.id.txtHisCountInfo);
		adapter = new OrderHisListAdapter(this);

		pre = (Button) findViewById(R.id.btnPrevious);
		pre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showPrevious();
			}
		});

		next = (Button) findViewById(R.id.btnNext);
		next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				showNext();
			}
		});

	}

	@Override
	protected void onResume() {
		super.onResume();

		orderHisList = OrderUtil.getInstance().loadHistory();
		total = orderHisList.size();
		if (total < 1) {
			Toast.makeText(OrderHis.this, "No order history found", Toast.LENGTH_SHORT).show();
			return;
		}
		currentOrderHis = orderHisList.get(orderIndex);
		adapter.setOrderItems(currentOrderHis.getOrderItems());
		listview.setAdapter(adapter);

		update();
	}

	private void showPrevious() {
		if (orderIndex > 0) {
			orderIndex--;
		}
		refresh();
	}

	private void refresh() {
		currentOrderHis = orderHisList.get(orderIndex);
		adapter.setOrderItems(currentOrderHis.getOrderItems());
		adapter.notifyDataSetChanged();

		update();
	}

	private void update() {
		if (orderIndex < 1 || orderHisList.size() < 1) {
			pre.setVisibility(View.GONE);
		} else {
			pre.setVisibility(View.VISIBLE);
		}

		if (orderHisList.size() - 1 > orderIndex) {
			next.setVisibility(View.VISIBLE);
		} else {
			next.setVisibility(View.GONE);
		}

		txtHisCountInfo.setText((orderIndex + 1) + "/" + total);

		tbNo.setText(currentOrderHis.getTableNo());
		totalPrice.setText(Utils.formatPrice(currentOrderHis.getTotalPrice()));
		orderDate.setText(currentOrderHis.getOrderDate());
	}

	private void showNext() {
		if (orderIndex < orderHisList.size() - 1) {
			orderIndex++;
		}
		refresh();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}

package com.emenu.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.emenu.models.Dish;
import com.emenu.models.OrderItem;

public class Order {
	private static Order instance = null;
	private List<OrderItem> orderItems = new ArrayList<OrderItem>();

	private Order() {
	}

	public static Order getInstance() {
		if (instance == null) {
			instance = new Order();
		}
		return instance;
	}

	public void add(OrderItem newItem) {
		OrderItem ordered = find(newItem);
		if (ordered != null) {
			ordered.setAmount(ordered.getAmount() + newItem.getAmount());
		} else {
			orderItems.add(newItem);
		}
	}

	public void clear() {
		this.orderItems.clear();
	}

	public void add(Dish dish) {
		OrderItem orderItem = new OrderItem(dish, 1);
		add(orderItem);
	}

	public void add(Dish dish, int amount) {
		OrderItem orderItem = new OrderItem(dish, amount);
		add(orderItem);
	}

	public void remove(OrderItem orderItem, boolean clear) {
		OrderItem ordered = find(orderItem);
		if (ordered != null) {
			if (clear || ordered.getAmount() == 1) {
				orderItems.remove(ordered);
			} else {
				ordered.setAmount(ordered.getAmount() - 1);
			}
		}
	}

	public void remove(OrderItem orderItem) {
		remove(orderItem, false);
	}

	private OrderItem find(OrderItem orderItem) {
		if (orderItem == null) {
			return null;
		}
		for (OrderItem item : orderItems) {
			if (orderItem.equals(item)) {
				return item;
			}
		}
		return null;
	}

	public float getTotalPrice() {
		float t = 0;
		for (OrderItem item : orderItems) {
			t += item.getTotalPrice();
		}
		return t;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public boolean save(String tableNumber) {
		boolean r = false;
		if (orderItems.isEmpty()) {
			return r;
		}

		FileWriter out = null;
		try {
			String appPath = Environment.getExternalStorageDirectory().getPath();

			File logFile = new File(appPath + Constants.ORDER_LOG);
			if (logFile.length() > Constants.MAX_LOG_FILE_SIZE) {
				File logBak = new File(appPath + Constants.ORDER_LOG_BAK);
				logBak.delete();
				logFile.renameTo(logBak);
				logFile = new File(appPath + Constants.ORDER_LOG);
			}
			if (logFile.exists()) {
				out = new FileWriter(logFile, true);
			} else {
				out = new FileWriter(logFile);
			}
			StringBuilder sb = new StringBuilder();
			sb.append(tableNumber).append("||");
			for (OrderItem it : orderItems) {
				sb.append(it.getDish().getName()).append("|").append(it.getAmount()).append("|").append(it.getTotalPrice())
						.append("||");
			}
			sb.append("\n");
			out.write(sb.toString());
			out.close();
			r = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return r;
	}
}
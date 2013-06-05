package com.emenu.common;

import java.util.ArrayList;
import java.util.List;

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
}
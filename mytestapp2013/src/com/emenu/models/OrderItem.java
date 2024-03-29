package com.emenu.models;

public class OrderItem {
	private Dish dish;
	private int amount;

	public OrderItem(Dish dish, int amount) {
		this.dish = dish;
		this.amount = amount;
	}

	public Dish getDish() {
		return dish;
	}

	public void setDish(Dish dish) {
		this.dish = dish;
	}

	public int getAmount() {
		return amount;
	}

	public float getTotalPrice() {
		return dish.getPrice() * this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void decline() {
		if (this.amount > 1) {
			this.amount = this.amount - 1;
		}
	}

	public void rise() {
		this.amount = this.amount + 1;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		OrderItem other = (OrderItem) obj;
		if (this.dish.getId() == other.getDish().getId()) {
			return true;
		} else {
			return false;
		}
	}

}

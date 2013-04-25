package com.emenu.dao;

import java.util.List;

import android.view.MenuItem;

import com.emenu.models.Dish;

public interface EMenuDao {

	List<MenuItem> loadMenus();

	List<MenuItem> loadMenuById(long id);

	List<Dish> loadDishes();

	List<Dish> loadDishes(long menuItemId);

	void saveMenu(MenuItem menuItem);

	void saveDish(Dish dish);

	void removeMenu(long menuItemId);

	void removeDish(long dishId);

}

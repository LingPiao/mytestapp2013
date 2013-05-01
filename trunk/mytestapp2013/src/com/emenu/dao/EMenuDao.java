package com.emenu.dao;

import java.util.List;

import com.emenu.models.Dish;
import com.emenu.models.MenuItem;

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

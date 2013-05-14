package com.emenu.dao.impl;

import java.util.List;

import com.emenu.dao.EMenuDao;
import com.emenu.models.Dish;
import com.emenu.models.MenuItem;

public class EMenuDaoImpl implements EMenuDao {

	@Override
	public List<MenuItem> loadMenus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MenuItem> loadMenuById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dish> loadDishes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dish> loadDishes(long menuItemId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveMenu(MenuItem menuItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateMenu(MenuItem menuItem) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void saveDish(Dish dish) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean updateDish(Dish dish) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeMenu(List<Long> ids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeDish(List<Long> ids) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long getMaxId4Menu() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getMaxId4Dish() {
		// TODO Auto-generated method stub
		return 0;
	}

}

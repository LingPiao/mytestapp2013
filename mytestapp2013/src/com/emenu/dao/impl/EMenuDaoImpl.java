package com.emenu.dao.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.emenu.common.MLog;
import com.emenu.common.XmlUtils;
import com.emenu.dao.EMenuDao;
import com.emenu.models.Dish;
import com.emenu.models.MenuItem;

public class EMenuDaoImpl implements EMenuDao {

	private static final String DISH_TAG = "dish";
	private static final String MENU_ITEM = "MenuItem";
	private static final String UTF_8 = "utf-8";

	@Override
	public List<MenuItem> loadMenus() {
		List<MenuItem> l = new ArrayList<MenuItem>();
		InputStream in = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			in = new FileInputStream(XmlUtils.getInstance().getMainMenuXml());
			xpp.setInput(in, UTF_8);
			int eventType = xpp.getEventType();
			MenuItem menu = new MenuItem();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					// System.out.println("Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					if (MENU_ITEM.equalsIgnoreCase(xpp.getName())) {
						menu = new MenuItem();
						menu.setId(Long.parseLong(xpp.getAttributeValue(null, "id")));
						menu.setName(xpp.getAttributeValue(null, "name"));
						menu.setMenuNumber(xpp.getAttributeValue(null, "menuNumber"));
					}
					// System.out.println("Start tag " + xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					// System.out.println("End tag " + xpp.getName());
					if (MENU_ITEM.equalsIgnoreCase(xpp.getName()) && menu != null) {
						l.add(menu);
						menu = null;
					}
				} else if (eventType == XmlPullParser.TEXT) {
					// Nothing to do here
				}
				eventType = xpp.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		sortMenus(l);

		return l;
	}

	@Override
	public List<MenuItem> loadMenuById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Dish> loadDishes() {
		List<Dish> l = new ArrayList<Dish>();
		InputStream in = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			in = new FileInputStream(XmlUtils.getInstance().getDishesXml());
			xpp.setInput(in, UTF_8);
			int eventType = xpp.getEventType();
			Dish dish = new Dish();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					// System.out.println("Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					if (DISH_TAG.equalsIgnoreCase(xpp.getName())) {
						dish = new Dish();
						dish.setDishNumber(xpp.getAttributeValue(null, "dishNumber"));
						dish.setId(Long.parseLong(xpp.getAttributeValue(null, "id")));
						dish.setName(xpp.getAttributeValue(null, "name"));
						dish.setBelongsTo(XmlUtils.getIds(xpp.getAttributeValue(null, "belongsTo")));
						dish.setImage(XmlUtils.getInstance().getPath("/" + xpp.getAttributeValue(null, "image")));
						dish.setFile(xpp.getAttributeValue(null, "file"));
						dish.setEnabled("true".equalsIgnoreCase(xpp.getAttributeValue(null, "enabled")) ? true : false);
						dish.setPrice(Float.parseFloat(xpp.getAttributeValue(null, "price")));
					}
					// System.out.println("Start tag " + xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					// System.out.println("End tag " + xpp.getName());
					if (DISH_TAG.equalsIgnoreCase(xpp.getName()) && dish != null) {
						if (dish.isEnabled()) {
							l.add(dish);
						}
						dish = null;
					}
				} else if (eventType == XmlPullParser.TEXT) {
					if (dish != null) {
						dish.setIntroduction(xpp.getText());
					}
				}
				eventType = xpp.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		sortDishes(l);

		return l;
	}

	@Override
	public List<Dish> loadDishes(long menuItemId) {
		List<Dish> l = new ArrayList<Dish>();
		InputStream in = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			in = new FileInputStream(XmlUtils.getInstance().getDishesXml());
			xpp.setInput(in, UTF_8);
			int eventType = xpp.getEventType();
			Dish dish = null;
			boolean r = false;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					// System.out.println("Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					if (DISH_TAG.equalsIgnoreCase(xpp.getName())) {
						List<Long> belongsTo = XmlUtils.getIds(xpp.getAttributeValue(null, "belongsTo"));
						r = belongsTo.contains(menuItemId);
						MLog.d(" belongsTo.contains(menuItemId) =" + r + ",menuItemId=" + menuItemId);
						if (r) {
							dish = new Dish();
							dish.setDishNumber(xpp.getAttributeValue(null, "dishNumber"));
							dish.setId(Long.parseLong(xpp.getAttributeValue(null, "id")));
							dish.setName(xpp.getAttributeValue(null, "name"));
							dish.setBelongsTo(belongsTo);
							dish.setImage(XmlUtils.getInstance().getPath("/" + xpp.getAttributeValue(null, "image")));
							dish.setFile(xpp.getAttributeValue(null, "file"));
							dish.setEnabled(getBoolean(xpp.getAttributeValue(null, "enabled")));
							dish.setRecommended(getBoolean(xpp.getAttributeValue(null, "recommended")));
							dish.setPrice(Float.parseFloat(xpp.getAttributeValue(null, "price")));
							MLog.d("Loaded Dish is:" + dish);
						}
					}
					// System.out.println("Start tag " + xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					// System.out.println("End tag " + xpp.getName());
					if (DISH_TAG.equalsIgnoreCase(xpp.getName()) && dish != null && r) {
						if (dish.isEnabled()) {
							l.add(dish);
						}
						dish = null;
						r = false;
					}
				} else if (eventType == XmlPullParser.TEXT) {
					if (dish != null) {
						dish.setIntroduction(xpp.getText());
					}
				}
				eventType = xpp.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
		sortDishes(l);
		return l;
	}

	@Override
	public List<Dish> loadRecommendedDishes() {

		List<Dish> l = new ArrayList<Dish>();
		InputStream in = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			in = new FileInputStream(XmlUtils.getInstance().getDishesXml());
			xpp.setInput(in, UTF_8);
			int eventType = xpp.getEventType();
			Dish dish = null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					// System.out.println("Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					if (DISH_TAG.equalsIgnoreCase(xpp.getName())) {
						boolean recommended = getBoolean(xpp.getAttributeValue(null, "recommended"));
						if (recommended) {
							dish = new Dish();
							List<Long> belongsTo = XmlUtils.getIds(xpp.getAttributeValue(null, "belongsTo"));
							dish.setDishNumber(xpp.getAttributeValue(null, "dishNumber"));
							dish.setId(Long.parseLong(xpp.getAttributeValue(null, "id")));
							dish.setName(xpp.getAttributeValue(null, "name"));
							dish.setBelongsTo(belongsTo);
							dish.setImage(XmlUtils.getInstance().getPath("/" + xpp.getAttributeValue(null, "image")));
							dish.setFile(xpp.getAttributeValue(null, "file"));
							dish.setEnabled(getBoolean(xpp.getAttributeValue(null, "enabled")));
							dish.setRecommended(recommended);
							dish.setPrice(Float.parseFloat(xpp.getAttributeValue(null, "price")));
							MLog.d("Loaded Dish is:" + dish);
						}
					}
					// System.out.println("Start tag " + xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					// System.out.println("End tag " + xpp.getName());
					if (DISH_TAG.equalsIgnoreCase(xpp.getName()) && dish != null) {
						l.add(dish);
						dish = null;
					}
				} else if (eventType == XmlPullParser.TEXT) {
					if (dish != null) {
						dish.setIntroduction(xpp.getText());
					}
				}
				eventType = xpp.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}
		sortDishes(l);
		return l;
	}

	private void sortDishes(List<Dish> dishes) {
		Collections.sort(dishes, new Comparator<Dish>() {
			public int compare(Dish d1, Dish d2) {
				Integer d1no = 0;
				if (d1.getDishNumber() != null && d1.getDishNumber().trim().length() > 0) {
					try {
						d1no = Integer.parseInt(d1.getDishNumber());
					} catch (Exception e) {
					}
				}
				Integer d2no = 0;
				if (d2.getDishNumber() != null && d2.getDishNumber().trim().length() > 0) {
					try {
						d2no = Integer.parseInt(d2.getDishNumber());
					} catch (Exception e) {
					}
				}
				return d1no.compareTo(d2no);
			}
		});
	}

	private void sortMenus(List<MenuItem> menus) {
		Collections.sort(menus, new Comparator<MenuItem>() {
			public int compare(MenuItem m1, MenuItem m2) {
				Integer m1no = 0;
				if (m1.getMenuNumber() != null && m1.getMenuNumber().trim().length() > 0) {
					try {
						m1no = Integer.parseInt(m1.getMenuNumber());
					} catch (Exception e) {
					}
				}
				Integer m2no = 0;
				if (m2.getMenuNumber() != null && m2.getMenuNumber().trim().length() > 0) {
					try {
						m2no = Integer.parseInt(m2.getMenuNumber());
					} catch (Exception e) {
					}
				}
				return m1no.compareTo(m2no);
			}
		});
	}

	private boolean getBoolean(String value) {
		if (value == null) {
			return false;
		}
		return "true".equalsIgnoreCase(value) ? true : false;
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

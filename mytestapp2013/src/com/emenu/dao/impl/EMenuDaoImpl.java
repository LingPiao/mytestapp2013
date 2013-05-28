package com.emenu.dao.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.emenu.common.MLog;
import com.emenu.common.XmlUtils;
import com.emenu.dao.EMenuDao;
import com.emenu.models.Dish;
import com.emenu.models.MenuItem;

public class EMenuDaoImpl implements EMenuDao {

	@Override
	public List<MenuItem> loadMenus() {
		List<MenuItem> l = new ArrayList<MenuItem>();
		InputStream in = null;
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			in = new FileInputStream(XmlUtils.getInstance().getMainMenuXml());
			xpp.setInput(in, "utf-8");
			int eventType = xpp.getEventType();
			MenuItem menu = new MenuItem();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					// System.out.println("Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					if ("MenuItem".equalsIgnoreCase(xpp.getName())) {
						menu = new MenuItem();
						menu.setId(Long.parseLong(xpp.getAttributeValue(null, "id")));
						menu.setName(xpp.getAttributeValue(null, "name"));
					}
					// System.out.println("Start tag " + xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					// System.out.println("End tag " + xpp.getName());
					if ("MenuItem".equalsIgnoreCase(xpp.getName()) && menu != null) {
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
			xpp.setInput(in, "utf-8");
			int eventType = xpp.getEventType();
			Dish dish = new Dish();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					// System.out.println("Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					if ("dish".equalsIgnoreCase(xpp.getName())) {
						dish = new Dish();
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
					if ("dish".equalsIgnoreCase(xpp.getName()) && dish != null) {
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
			xpp.setInput(in, "utf-8");
			int eventType = xpp.getEventType();
			Dish dish = null;
			boolean r = false;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
					// System.out.println("Start document");
				} else if (eventType == XmlPullParser.START_TAG) {
					if ("dish".equalsIgnoreCase(xpp.getName())) {
						List<Long> belongsTo = XmlUtils.getIds(xpp.getAttributeValue(null, "belongsTo"));
						r = belongsTo.contains(menuItemId);
						MLog.d(" belongsTo.contains(menuItemId) =" + r + ",menuItemId=" + menuItemId);
						if (r) {
							dish = new Dish();
							dish.setId(Long.parseLong(xpp.getAttributeValue(null, "id")));
							dish.setName(xpp.getAttributeValue(null, "name"));
							dish.setBelongsTo(belongsTo);
							dish.setImage(XmlUtils.getInstance().getPath("/" + xpp.getAttributeValue(null, "image")));
							dish.setFile(xpp.getAttributeValue(null, "file"));
							dish.setEnabled("true".equalsIgnoreCase(xpp.getAttributeValue(null, "enabled")) ? true : false);
							dish.setPrice(Float.parseFloat(xpp.getAttributeValue(null, "price")));
							MLog.d("Loaded Dish is:" + dish);
						}
					}
					// System.out.println("Start tag " + xpp.getName());
				} else if (eventType == XmlPullParser.END_TAG) {
					// System.out.println("End tag " + xpp.getName());
					if ("dish".equalsIgnoreCase(xpp.getName()) && dish != null && r) {
						l.add(dish);
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

		return l;
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

package com.emenu.common;

import java.util.HashMap;
import java.util.Map;

import com.emenu.dao.EMenuDao;
import com.emenu.dao.impl.EMenuDaoImpl;

public class XmlUtils {

	private final static String DATA = "data/";
	private final static String MAIN_MENU_XML = "/MainMenu.xml";
	private final static String DISHES_XML = "/Dishes.xml";
	private String appPath = "";

	private long maxId4Menu = 0;
	// private long maxId4Dish = 0;

	private EMenuDao dao;

	private static Map<String, XmlUtils> cache = new HashMap<String, XmlUtils>();
	private String language = Languages.en_US.name();
	private static XmlUtils instance = null;

	private XmlUtils() {
	}

	public static XmlUtils build(String language, String appPath) {
		XmlUtils xu = cache.get(language);
		if (xu != null) {
			instance = xu;
			return xu;
		}
		xu = new XmlUtils();
		xu.language = language;
		xu.appPath = appPath;
		instance = xu;
		xu.dao = new EMenuDaoImpl();
		xu.maxId4Menu = xu.dao.getMaxId4Menu();
		cache.put(language, xu);
		return xu;
	}

	public static XmlUtils getInstance() {
		return instance;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getMainMenuXml() {
		return getPath(MAIN_MENU_XML);
	}

	public String getDishesXml() {
		return getPath(DISHES_XML);
	}

	private String getPath(String xml) {
		return appPath + DATA + language + xml;
	}

	public long getMaxId4Menu() {
		this.maxId4Menu++;
		return maxId4Menu;
	}

}

package com.emenu.common;

import java.util.ArrayList;
import java.util.List;

public class XmlUtils {

	private final static String DATA = "/emenu/data/";
	private final static String MAIN_MENU_XML = "/MainMenu.xml";
	private final static String DISHES_XML = "/Dishes.xml";

	private String appPath = "";
	private String language = Languages.en_US.name();

	private static XmlUtils instance = null;

	private XmlUtils() {
	}

	public static void build(String appPath) {
		if (instance == null) {
			instance = new XmlUtils();
			instance.appPath = appPath;
		}
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

	public String getPath(String xml) {
		return appPath + DATA + language + xml;
	}

	public static List<Long> getIds(String ids) {
		String[] s = ids.split(",");
		List<Long> r = new ArrayList<Long>();
		for (String id : s) {
			r.add(Long.parseLong(id));
		}
		return r;
	}
}

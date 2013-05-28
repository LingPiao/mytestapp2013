package com.emenu.common;

import static com.emenu.common.Constants.DATA;
import static com.emenu.common.Constants.DISHES_XML;
import static com.emenu.common.Constants.MAIN_MENU_XML;

import java.util.ArrayList;
import java.util.List;

public class XmlUtils {

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
		instance.language = language;
	}

	public String getMainMenuXml() {
		return getPath(MAIN_MENU_XML);
	}

	public String getDishesXml() {
		return getPath(DISHES_XML);
	}

	public String getPath(String file) {
		return appPath + DATA + language + file;
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

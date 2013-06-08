package com.emenu.common;

public class Constants {

	public final static String DATA = "/emenu/data/";
	public final static String MAIN_MENU_XML = "/MainMenu.xml";
	public final static String DISHES_XML = "/Dishes.xml";
	public static final String TITLE_FILE = DATA + "title";
	public static final String ORDER_LOG = DATA + "order.log";
	public static final String ORDER_LOG_BAK = ORDER_LOG + ".bak";

	public static final String DISH_KEY = "dish_key";
	public static final String SELECTED_MENU_ITEM_KEY = "selected_menu_item_key";
	public static final String SELECTED_LANGUAGE_KEY = "selected_language_key";

	public static final String DEFAULT_CURRENCY_UNIT = "kr";

	public static final long MAX_LOG_FILE_SIZE = 1677721600; // 200MB=200*1024*1024*8;

}

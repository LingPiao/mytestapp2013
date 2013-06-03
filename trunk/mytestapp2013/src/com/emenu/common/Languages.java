package com.emenu.common;

import java.util.HashMap;
import java.util.Map;

public enum Languages {
	en_US, sv_SE, zh_CN;

	private static Map<Integer, Languages> lookup = new HashMap<Integer, Languages>();
	static {
		for (Languages language : Languages.values()) {
			lookup.put(language.ordinal(), language);
		}
	}

	public static Languages valueOf(int ordinal) {
		return lookup.get(ordinal);
	}
}

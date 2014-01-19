package com.emenu.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.os.Environment;

import com.emenu.models.Dish;
import com.emenu.models.OrderItem;

public class OrderUtil {
	private static final String ITEM_FIELD_SEPARATOR = "#";
	private static final String ORDER_FIELD_SEPARATOR = "##";
	private static OrderUtil instance = null;
	private Order order = new Order();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
	private String appPath = Environment.getExternalStorageDirectory().getPath();

	private OrderUtil() {
	}

	public static OrderUtil getInstance() {
		if (instance == null) {
			instance = new OrderUtil();
		}
		return instance;
	}

	public boolean save(String tableNumber) {
		boolean r = false;
		if (order.getOrderItems().isEmpty()) {
			return r;
		}

		FileWriter out = null;
		try {

			File logFile = new File(appPath + Constants.ORDER_LOG);
			if (logFile.length() > Constants.MAX_LOG_FILE_SIZE) {
				File logBak = new File(appPath + Constants.ORDER_LOG_BAK);
				logBak.delete();
				logFile.renameTo(logBak);
				logFile = new File(appPath + Constants.ORDER_LOG);
			}
			if (logFile.exists()) {
				out = new FileWriter(logFile, true);
			} else {
				out = new FileWriter(logFile);
			}
			StringBuilder sb = new StringBuilder();
			sb.append(tableNumber).append(ORDER_FIELD_SEPARATOR);
			sb.append(format.format(new Date(System.currentTimeMillis()))).append(ORDER_FIELD_SEPARATOR);
			for (OrderItem it : order.getOrderItems()) {
				sb.append(it.getDish().getName());
				if (it.getDish().getDishNumber() != null) {
					sb.append("[").append(it.getDish().getDishNumber()).append("]");
				}
				sb.append(ITEM_FIELD_SEPARATOR).append(it.getAmount())
						.append(ITEM_FIELD_SEPARATOR).append(it.getTotalPrice()).append(ORDER_FIELD_SEPARATOR);
			}
			sb.append("\n");
			out.write(sb.toString());
			out.close();
			r = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		return r;
	}

	public Order getOrder() {
		return order;
	}

	public List<Order> loadHistory() {
		List<Order> list = new ArrayList<Order>();
		List<Order> tl = new ArrayList<Order>();
		String ohis = appPath + Constants.ORDER_LOG;
		File logFile = new File(ohis);
		MLog.d("Load order history from: " + ohis);
		if (!logFile.exists()) {
			return list;
		}
		BufferedReader br = null;
		String line = null;
		String[] lines = new String[10];
		int i = 0;
		try {
			br = new BufferedReader(new FileReader(logFile));
			while ((line = br.readLine()) != null) {
				if (i == 9) {
					i = 0;
				}
				lines[i++] = line;
			}
		} catch (Exception e) {
			MLog.v("Load order history error:" + e.getMessage());
		}

		MLog.d(i + " line logs loaded");
		for (String l : lines) {
			if (l == null) {
				break;
			}
			Order o = decodeOrder(l);
			if (o != null) {
				tl.add(o);
			}
		}
		// resort the list
		for (int t = tl.size() - 1; t >= 0; t--) {
			list.add(tl.get(t));
		}

		MLog.d(list.size() + " orders decoded from the loaded log");
		return list;
	}

	private Order decodeOrder(String orderStr) {
		// tableNumber||date||OrderItem(dishName|amount|price)||OrderItem(dishName|amount|price)...||
		if (orderStr == null) {
			return null;
		}
		String[] orderFields = orderStr.split(ORDER_FIELD_SEPARATOR);
		if (orderFields.length < 3) {
			return null;
		}

		Order order = new Order();
		order.setTableNo(orderFields[0]);
		order.setOrderDate(orderFields[1]);
		List<OrderItem> items = new ArrayList<OrderItem>();
		for (int i = 2; i < orderFields.length; i++) {
			OrderItem oi = decodeOrderItem(orderFields[i]);
			if (oi != null) {
				items.add(oi);
			}
		}
		if (items.size() > 0) {
			order.setOrderItems(items);
		}
		return order;
	}

	private OrderItem decodeOrderItem(String itemStr) {
		if (itemStr == null) {
			return null;
		}
		String[] itemFields = itemStr.split(ITEM_FIELD_SEPARATOR);
		if (itemFields.length != 3) {
			return null;
		}
		Dish dish = new Dish();
		dish.setName(itemFields[0]);
		dish.setPrice(Float.parseFloat(itemFields[2]));
		int amount = Integer.parseInt(itemFields[1]);

		return new OrderItem(dish, amount);
	}

}
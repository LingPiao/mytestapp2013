package com.emenu.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.emenu.R;

public class CategoryListAdapter extends BaseAdapter {
	private List<com.emenu.models.MenuItem> menus = new ArrayList<com.emenu.models.MenuItem>();
	private LayoutInflater inflater = null;

	public CategoryListAdapter(Context context, List<com.emenu.models.MenuItem> menus) {
		this.menus = menus;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		com.emenu.models.MenuItem m = menus.get(position);
		MenuViewHolder mvh = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.menu_row, parent, false);
			mvh = new MenuViewHolder(convertView);
			convertView.setTag(mvh);
		} else {
			mvh = (MenuViewHolder) convertView.getTag();
		}
		mvh.getMenuName().setText(m.getName());
		return convertView;
	}

	@Override
	public int getCount() {
		return menus.size();
	}

	@Override
	public Object getItem(int posistion) {
		return menus.get(posistion);
	}

	@Override
	public long getItemId(int posistion) {
		return menus.get(posistion).getId();
	}

}

class MenuViewHolder {

	private TextView menuName;

	public MenuViewHolder(View view) {
		menuName = (TextView) view.findViewById(R.id.menuName);
	}

	public TextView getMenuName() {
		return menuName;
	}
}

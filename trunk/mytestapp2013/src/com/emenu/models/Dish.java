package com.emenu.models;

import java.io.Serializable;
import java.util.List;

public class Dish implements Serializable {

	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private List<Long> belongsTo;
	private String image;
	private String file;
	private boolean enabled;
	private float price;
	private String introduction;
	private String description;
	private boolean recommended;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getBelongsTo() {
		return belongsTo;
	}

	public void setBelongsTo(List<Long> belongsTo) {
		this.belongsTo = belongsTo;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRecommended() {
		return recommended;
	}

	public void setRecommended(boolean recommended) {
		this.recommended = recommended;
	}

	@Override
	public String toString() {
		return "Dish [id=" + id + ", name=" + name + ", belongsTo=" + belongsTo + ", image=" + image + ", file=" + file + ", enabled=" + enabled + ", price="
				+ price + ", introduction=" + introduction + ", description=" + description + "]";
	}

}

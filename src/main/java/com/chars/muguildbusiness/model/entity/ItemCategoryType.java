package com.chars.muguildbusiness.model.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "item_category_types", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"item_category_id", "item_type_id"} )}
)
public class ItemCategoryType implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long item_category_type_id;
	
	@ManyToOne
	@JoinColumn(name = "item_category_id", nullable = false)
	private ItemCategory itemCategory;
	
	@ManyToOne
	@JoinColumn(name = "item_type_id", nullable = false)
	private ItemType itemType;
	
	public Long getItem_category_type_id() {
		return item_category_type_id;
	}
	public void setItem_category_type_id(Long item_category_type_id) {
		this.item_category_type_id = item_category_type_id;
	}
	public ItemCategory getItemCategory() {
		return itemCategory;
	}
	public void setItemCategory(ItemCategory itemCategory) {
		this.itemCategory = itemCategory;
	}
	public ItemType getItemType() {
		return itemType;
	}
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}
	
}

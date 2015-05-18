package com.pramati.puzzle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Restaurent {
	
	private int restaurentID;

	private Set<String> items = new HashSet<String>();
	private Map<HashSet<String>, Float> itemsWithPrice= new HashMap<HashSet<String>, Float>();
	
	public Restaurent(int restaurentId) {
		this.restaurentID = restaurentId;
	}
	
	public int getRestaurentID() {
		return restaurentID;
	}
	
	public void addItems(Set<String> menuItems, Float price){
		items.addAll(menuItems);
		itemsWithPrice.put((HashSet<String>) menuItems, price);
	}

	public Map<HashSet<String>, Float> getAvailableItems() {
		
		return itemsWithPrice;
	}
}
package com.pramati.puzzle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;

public class OptimalRestaurantFinder {
	private static final Logger LOGGER = Logger.getLogger(OptimalRestaurantFinder.class.getName());
	private Set<String> itemsRequired = new HashSet<String>();
	private Set<Restaurent> restaurents = new HashSet<Restaurent>();

	public static void main(String[] args) {

		if(args.length == 0 || args.length == 1 || !args[0].contains(".csv")){
			System.out.println("Nil");
		}
		else {
			OptimalRestaurantFinder findRestaurent = new OptimalRestaurantFinder();
			findRestaurent.readDataFromFile(args[0]);
			findRestaurent.readItemsRequired(args);
			findRestaurent.searchOptimalRestaurent();
		}
	}

	private void readDataFromFile(String fileName){

		BufferedReader fileBufferedReader = null;
		String currentLine;
		int prevRestaurentId = 0;
		int currentRestaurentID = 1;
		Float price;
		Restaurent newRestaurent = new Restaurent(prevRestaurentId);
		try {

			fileBufferedReader = new BufferedReader(new FileReader(fileName));
			currentLine = fileBufferedReader.readLine();
			while (currentLine !=null) {
				Set<String> restaurentItems = new HashSet<String>();
				String[] tempStrings = currentLine.split(", ");
				currentRestaurentID = Integer.parseInt(tempStrings[0]);
				price = Float.parseFloat(tempStrings[1]);

				for(int i = 2; i<tempStrings.length; i++)
					restaurentItems.add(tempStrings[i]);
				
				if(prevRestaurentId == currentRestaurentID) {
					newRestaurent.addItems(restaurentItems, price);
				}
				else {
					newRestaurent = new Restaurent(currentRestaurentID);
					newRestaurent.addItems(restaurentItems, price);
					restaurents.add(newRestaurent);
					prevRestaurentId = currentRestaurentID;
				}

				currentLine = fileBufferedReader.readLine();
			}
		} 
		catch (IOException e) {
			if (LOGGER.isLoggable(Level.INFO)){
				LOGGER.severe(e.toString());
			}
		}catch (NumberFormatException e) {
			if (LOGGER.isLoggable(Level.INFO)){
				LOGGER.severe(e.toString());
			}
		}finally {
			try {
				if (fileBufferedReader != null)
					fileBufferedReader.close();
			} catch (IOException ex) {
				if (LOGGER.isLoggable(Level.INFO)){
					LOGGER.severe(ex.toString());
				}
			}
		}
	}

	private void readItemsRequired(String[] args) {
		for (int i=1;i<args.length;i++) {
			itemsRequired.add(args[i]);
		}

	}

	private void searchOptimalRestaurent() {
		Iterator<Restaurent> restaurentsIterator = restaurents.iterator();
		HashMap<HashSet<String>, Float> availableItems = new HashMap<HashSet<String>, Float>();
		Float minPrice = Float.MAX_VALUE;
		int restaurentID = 0;
		while (restaurentsIterator.hasNext()) {
			Restaurent currentRestaurent = restaurentsIterator.next();
			availableItems = (HashMap<HashSet<String>, Float>) currentRestaurent.getAvailableItems();
			Float availableForPrice = availableForPrice(availableItems);
			if((availableForPrice != (float)0.00) && availableForPrice < minPrice)
			{
				minPrice = availableForPrice;
				restaurentID = currentRestaurent.getRestaurentID();
			}
		}

		if(restaurentID == 0)
			System.out.println("Nil");
		else
			System.out.println(restaurentID+" "+minPrice);			
	}

	public Float availableForPrice(Map<HashSet<String>, Float> availableItems) {
		Float price = (float) 0.00;
		Set<String> updatedItemsRequired = new HashSet<String>(itemsRequired);

		for (Entry<HashSet<String>, Float> entry : availableItems.entrySet()) {
			if(entry.getKey().containsAll(updatedItemsRequired)){
				price = price + entry.getValue();
				updatedItemsRequired.clear();
				break;	
			}
			else if (updatedItemsRequired.containsAll(entry.getKey())){
				price = price + entry.getValue();
				updatedItemsRequired.removeAll(entry.getKey());
			}
		}
		if(updatedItemsRequired.isEmpty())
			return price;
		return (float) 0.00;
	}
}



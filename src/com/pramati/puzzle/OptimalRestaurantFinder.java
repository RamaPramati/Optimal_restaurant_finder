package com.pramati.puzzle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;

public class OptimalRestaurantFinder {
	private static final Logger LOGGER = Logger.getLogger(OptimalRestaurantFinder.class.getName());
	private Set<String> itemsRequired = new HashSet<String>();

	public static void main(String[] args) {

		if(args.length == 0 || args.length == 1 || !args[0].contains(".csv")){
			System.out.println("Nil");
		}
		else {
			OptimalRestaurantFinder findRestaurent = new OptimalRestaurantFinder();
			findRestaurent.readItemsRequired(args);
			findRestaurent.readDataFromFile(args[0]);
		}
	}

	private void readDataFromFile(String fileName){

		BufferedReader fileBufferedReader = null;
		String currentLine;
		int prevRestaurentId = 0;
		int currentRestaurentID = 1;
		try {

			fileBufferedReader = new BufferedReader(new FileReader("Input/"+fileName));
			currentLine = fileBufferedReader.readLine();
			Float minPrice = Float.MAX_VALUE;
			int restaurentID = 0;
			Set<String> updatedTempItemsRequired = new HashSet<String>(itemsRequired);
			Map<Set<String>, Float> itemsWithPrice= new HashMap<Set<String>, Float>();
			while (currentLine !=null) {
				Set<String> restaurentItems = new HashSet<String>();
				String[] tempStrings = currentLine.split(", ");
				currentRestaurentID = Integer.parseInt(tempStrings[0]);
				Float price = Float.parseFloat(tempStrings[1]);

				if (prevRestaurentId != currentRestaurentID) {
					prevRestaurentId = currentRestaurentID;
					itemsWithPrice.clear();
				}

				for(int i = 2; i<tempStrings.length; i++)
					restaurentItems.add(tempStrings[i]);

				if ((updatedTempItemsRequired.retainAll(restaurentItems)) && !(updatedTempItemsRequired.isEmpty())){
					itemsWithPrice.put(restaurentItems, price);
					Map<Set<String>, Float> tempItemPrice= new HashMap<Set<String>, Float>();
					for (Entry<Set<String>, Float> entry : itemsWithPrice.entrySet())
					{
						Set<String> tempItems = new HashSet<String>(entry.getKey());
						tempItems.addAll(restaurentItems);

						if(tempItemPrice.containsKey(tempItems)){
							if(entry.getValue()+price < tempItemPrice.get(tempItems)){
								tempItemPrice.put(tempItems, entry.getValue()+price);
							}
						}
						else
							tempItemPrice.put(tempItems, entry.getValue()+price);

						if(tempItems.containsAll(itemsRequired)){
							if((entry.getValue()+price) < minPrice){
								minPrice = entry.getValue()+price;
								restaurentID = currentRestaurentID;
							}
						}
					}

					for (Entry<Set<String>, Float> entry : tempItemPrice.entrySet())
					{
						if(itemsWithPrice.containsKey(entry.getKey())){
							if(entry.getValue() < itemsWithPrice.get(entry.getKey())){
								itemsWithPrice.put(entry.getKey(), entry.getValue());
							}
						}
						else
							itemsWithPrice.put(entry.getKey(), entry.getValue());
					}
				}

				updatedTempItemsRequired.clear();
				updatedTempItemsRequired.addAll(itemsRequired);
				currentLine = fileBufferedReader.readLine();
			}

			if(restaurentID == 0)
				System.out.println("Nil");
			else
				System.out.println(restaurentID+" "+minPrice);

		} 
		catch (IOException e) {
			if (LOGGER.isLoggable(Level.INFO)){
				LOGGER.severe(e.toString());
			}
		}catch (NumberFormatException e) {
			if (LOGGER.isLoggable(Level.INFO)){
				LOGGER.severe("Your file contains some empty lines, please remove...");
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
}



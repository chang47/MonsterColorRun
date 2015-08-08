package DB.Model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import metaModel.City;
import metaModel.Dungeon;
import metaModel.Route;
import android.util.Log;
import android.util.SparseArray;

/*
 * Reads and creates the map to be used in the game as the player travels
 * from city to city
 */
public class MapGraph {
	private static SparseArray<City> graph;
	private static SparseArray<Route> cityToRoutes;
	
	public static void createGraph() {
		graph = new SparseArray<City>();
		/**
		City city1 = new City(1);
		City city2 = new City(2);
		
		Route route1 = new Route(1, 1, 2);
		Route route2 = new Route(1, 2, 1);

		city1.routes.add(route1);
		city2.routes.add(route2);
		graph.put(city1.cityID, city1);
		graph.put(city2.cityID, city2);
		**/
		//constructGraph();
	}
	
	public static ArrayList<Integer> nearbyCities(int currentCityId) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		City currentCity = graph.get(currentCityId);
		for (Route route : currentCity.routes) {
			list.add(route.to);
		}
		return list;
	}
	
	public static void constructGraph(ArrayList<City> cities, ArrayList<Route> routes, 
			ArrayList<Dungeon> dungeons) {
		
	}
	
/*	private void constructGraph() {
        try {
			Scanner scanner = new Scanner(new File("src/Path.txt"));
	        String line;
	        while (scanner.hasNext()) {
	            line = scanner.nextLine();
	            String[] nodes = line.split(":");
	            String[] paths = nodes[1].split(",");
	            for (int i = 0; i < paths.length; i++) {
	            	graph.put(Integer.parseInt(nodes[0]), value);
	            }
	        }
	        scanner.close();
        } catch (IOException exception) {
        	Log.d("FILE READING Error!", exception.getLocalizedMessage());
        }
	}*/
}

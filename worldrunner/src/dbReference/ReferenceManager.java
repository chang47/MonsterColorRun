package dbReference;

import java.util.ArrayList;
import java.util.List;











import metaModel.City;
import metaModel.Dungeon;
import metaModel.DungeonMonsters;
import metaModel.ExpTable;
import metaModel.MetaAbility;
import metaModel.MetaRoute;
import metaModel.Route;
import metaModel.RouteMonsters;
import dbReference.CityManager;
import dbReference.DungeonManager;
import Abilities.Ability;
import Abilities.DamageAllAbility;
import Abilities.SupportAbility;
import DB.Model.BattleMonster;
import DB.Model.Equipment;
import DB.Model.Monster;
import DB.Model.Player;
import DB.Model.Sticker;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;

/**
 * The DB Manager centralizes and uses all DB related actions
 */
public class ReferenceManager extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "reference";
	private static final int DATABASE_VERSION = 1;
	private Context context;
	//public SQLiteDatabase db;  

	public ReferenceManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		if (ExpTable.expTable == null) 
			ExpTable.populateTable();
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		AbilitiesManager.create(db);
		CityManager.create(db);
		DungeonManager.create(db);
		MonsterManager.create(db);
		RouteManager.create(db);
	}

	@Override
	// drops and recreates everything
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		AbilitiesManager.drop(db);
		CityManager.drop(db);
		DungeonManager.drop(db);
		MonsterManager.drop(db);
		RouteManager.drop(db);
		onCreate(db);
	}
	
	public List<int[]> getExp() {
		return ExpTable.expTable;
	}
	
	public List<Sticker> monstersList() {
		SQLiteDatabase db = this.getWritableDatabase();
		return MonsterManager.getSticker(db);
	}
	
	public List<MetaAbility> abilitiesList() {
		SQLiteDatabase db = this.getWritableDatabase();
		return AbilitiesManager.getAbility(db);
	}
	
	public List<City> getCitiesList() {
		SQLiteDatabase db = this.getWritableDatabase();
		return CityManager.getCity(db);
	}
	
	public SparseArray<List<Dungeon>> getDungeonsList() {
		SQLiteDatabase db = this.getWritableDatabase();
		List<Dungeon> dungeons = DungeonManager.getDungeons(db);
		SparseArray<List<Dungeon>> list = new SparseArray<List<Dungeon>>();
		for (Dungeon dungeon : dungeons) {
			int cityId = dungeon.cityId;
			if (list.get(cityId) == null) {
				list.put(cityId, new ArrayList<Dungeon>());
			}
			list.get(cityId).add(dungeon);
		}
		return list;
	}
	
	// TODO populating with monsters
	// key = city id, value = routes that belong
	public SparseArray<List<Route>> getRoutesList() {
		SQLiteDatabase db = this.getWritableDatabase();
		List<Route> routes = RouteManager.getRoutes(db);
		SparseArray<List<Route>> list = new SparseArray<List<Route>>();
		for (Route route : routes) {
			int cityId = route.from;
			if (list.get(cityId) == null) {
				list.put(cityId, new ArrayList<Route>());
			}
			list.get(cityId).add(route);
		}
		return list;
	}
	
	public SparseArray<List<RouteMonsters>> getRouteMonstersList() {
		List<RouteMonsters> monsters = new ArrayList<RouteMonsters>();
		monsters.add(new RouteMonsters(1, 6, 1, 5, 7, 1));
		monsters.add(new RouteMonsters(2, 7, 1, 5, 7, 1));
		monsters.add(new RouteMonsters(3, 8, 1, 5, 7, 1));
		SparseArray<List<RouteMonsters>> list = new SparseArray<List<RouteMonsters>>();
		for (RouteMonsters monster : monsters) {
			int routeId = monster.routeId;
			if (list.get(routeId) == null) {
				list.put(routeId, new ArrayList<RouteMonsters>());
			}
			list.get(routeId).add(monster);
		}
		return list;
	}
	
	public SparseArray<List<DungeonMonsters>> getDungeonMonstersList() {
		List<DungeonMonsters> monsters = new ArrayList<DungeonMonsters>();
		//int id, int monsterId, int routeId, int capture, int level
		monsters.add(new DungeonMonsters(1, 4, 1, 5, 1));
		monsters.add(new DungeonMonsters(2, 5, 1, 5, 1));
		monsters.add(new DungeonMonsters(3, 6, 1, 5, 1));
		monsters.add(new DungeonMonsters(4, 5, 2, 5, 5));
		monsters.add(new DungeonMonsters(5, 6, 2, 5, 5));
		monsters.add(new DungeonMonsters(6, 7, 2, 5, 5));
		monsters.add(new DungeonMonsters(7, 7, 3, 5, 9));
		monsters.add(new DungeonMonsters(8, 8, 3, 5, 9));
		monsters.add(new DungeonMonsters(9, 9, 3, 5, 9));
		SparseArray<List<DungeonMonsters>> list = new SparseArray<List<DungeonMonsters>>();
		for (DungeonMonsters monster : monsters) {
			int dungeonId = monster.dungeonId;
			if (list.get(dungeonId) == null) {
				list.put(dungeonId, new ArrayList<DungeonMonsters>());
			}
			list.get(dungeonId).add(monster);
		}
		return list;
	}
	
	/**
	 	OTHER
	 */
	/**
	 * 
	 * City
	 * 
	 */
	
/*	public ArrayList<City> getCities() {
		SQLiteDatabase db = this.getWritableDatabase();
		return CityManager.getCity(db);
	}*/
	
	public int updateCity(City city) {
		SQLiteDatabase db = this.getWritableDatabase();
		return CityManager.updateCity(db, city);
	}

	public void deleteCity(City city) {
		SQLiteDatabase db = this.getWritableDatabase();
		CityManager.deleteCity(db, city);
		db.close();
	}
	
	public void addCity(City city) {
		SQLiteDatabase db = this.getWritableDatabase();
		CityManager.addCity(db, city);
		db.close();
	}
	
	/**
	 * 
	 * CITY MAPPINGs
	 * 
	 */
	
	/**
	 * Stub that setup the initial mapping cities to their routes
	 * @return a SparseArray that contains all of the mapping of cities and their cities
	 * 		   in the form of cityId -> List of routes id that belong to the city
	 */
/*	public SparseArray<ArrayList<Route>> getCityRoutes() {
		ArrayList<Route> routes = getRoutes();
		SparseArray<ArrayList<Route>> map = new SparseArray<ArrayList<Route>>();
		ArrayList<Route> city1Route = new ArrayList<Route>();
		// maps to route ID path so we can get more info from the route class
		
		// TODO note that we would need to get the mapping from the RouteManager
		// TODO also get the routing of monsters to the routes
		// and then loop through them and put the appropriate routes
		
		// adds the monsters in
		
		// TODO get the monster routing mapping
		// getRouteMonsters()
		// future, probably empty or doesn't exist and just get the list from db call
		for (int i = 0; i < routes.size(); i++) {
			routes.get(i).monsters.add(TURTLE);
			routes.get(i).monsters.add(SEAHORSE);
			routes.get(i).monsters.add(SNAKE);
		}
		city1Route.add(routes.get(0));
		ArrayList<Route> city2Route = new ArrayList<Route>();
		city2Route.add(routes.get(1));
		map.put(1, city1Route);
		map.put(2, city2Route);
		return map;
	}*/
	
	
	/**
	 * Stub that setup the initial mapping of cities to their dungeons
	 * @return a SparseArray that contains all of the mapping of cities to their dungeons
	 * 		   in the form of cityId ->list of dungeons
	 */
	/*public SparseArray<ArrayList<Dungeon>> getCityDungeons() {
		SparseArray<ArrayList<Dungeon>> map = new SparseArray<ArrayList<Dungeon>>();
		ArrayList<Dungeon> city1Dungeon = new ArrayList<Dungeon>();
		ArrayList<Dungeon> dungeons = getDungeons();
		
		// maps dungeons to their monsters, would have to loop through
		// the mapping to add the dungeon
		// getDungeonMonster()
		
		// future, probably empty or doesn't exist and just get the list from db call
		dungeons.get(0).monsters.add(MARTIN);
		dungeons.get(0).monsters.add(TURTLE);
		dungeons.get(1).monsters.add(SEAHORSE);
		dungeons.get(1).monsters.add(SNAKE);
		// maps from city to dungeon id 
		// similar to routes, need to get a mapping from the actual db
		city1Dungeon.add(dungeons.get(0));
		ArrayList<Dungeon> city2Dungeon = new ArrayList<Dungeon>();
		city2Dungeon.add(dungeons.get(1));
		map.put(1, city1Dungeon);
		map.put(2, city2Dungeon);
		return map;
	}*/
	
	/**
	 * 
	 * Dungeon Mappings
	 * 
	 */
	
	/**
	 * Stub that setup the initial mapping of monsters to their routes
	 * @return a SparseArray that contains all of the mapping of monsters to their routes
	 * 		   in the form of routeId -> list of monsters
	 */
/*	public SparseArray<ArrayList<Integer>> getRouteMonsters() {
		SparseArray<ArrayList<Integer>> map = new SparseArray<ArrayList<Integer>>();
		ArrayList<Integer> route1Monsters = new ArrayList<Integer>();
		route1Monsters.add(4);
		route1Monsters.add(5);
		route1Monsters.add(6);
		map.put(1, route1Monsters);
		map.put(2, route1Monsters);
		return map;
	}*/
	   
	/**
	 * Stub that setup the initial mapping of monsters to their dungeons
	 * @return a SparseArray that contains all of the mapping of monsters to their dungeons
	 * 		   in the form of dungeonId -> list of monsters
	 */
/*	public SparseArray<ArrayList<Integer>> getDungeonMonsters() {
		SparseArray<ArrayList<Integer>> map = new SparseArray<ArrayList<Integer>>();
		ArrayList<Integer> dungeon1Monsters = new ArrayList<Integer>();
		ArrayList<Integer> dungeon2Monsters = new ArrayList<Integer>();
		dungeon1Monsters.add(4);
		dungeon1Monsters.add(5);
		dungeon2Monsters.add(4);
		dungeon2Monsters.add(6);
		map.put(1, dungeon1Monsters);
		map.put(2, dungeon2Monsters);
		return map;
	}*/
	
	public int getNumMonsters() {
		SQLiteDatabase db = this.getWritableDatabase();
		return (int) DatabaseUtils.queryNumEntries(db, "monstersReference"); // TODO need to be changed to use the rare monster list

	}

	
}

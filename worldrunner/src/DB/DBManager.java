package DB;

import java.util.ArrayList;
import java.util.List;

















import Abilities.Ability;
import Abilities.DamageAbility;
import Abilities.SupportAbility;
import DB.Model.City;
import DB.Model.Dungeon;
import DB.Model.Equipment;
import DB.Model.Monster;
import DB.Model.Player;
import DB.Model.Route;
import DB.Model.Sticker;
import Model.BattleMonster;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;

/**
 * The DB Manager centralizes and uses all DB related actions
 */
public class DBManager extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "Player";
	private static final int DATABASE_VERSION = 1;
	private Context context;
	//public SQLiteDatabase db;  

	public DBManager(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		PlayerManager.create(db);
		EquipmentManager.create(db);
		StickerManager.create(db);
		CityManager.create(db);
		RouteManager.create(db);
		DungeonManager.create(db);
		CityMappingManager.create(db);
		MonsterManager.create(db);
	}

	@Override
	// drops and recreates everything
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		PlayerManager.drop(db);
		EquipmentManager.drop(db);
		StickerManager.drop(db);
		CityManager.drop(db);
		RouteManager.drop(db);
		DungeonManager.drop(db);
		CityMappingManager.drop(db);
		MonsterManager.drop(db);
		onCreate(db);
	}
	
	/**
	 * 
	 * PLAYER
	 * 
	 */
	
	public ArrayList<Monster> getParty() {
		ArrayList<Monster> list = new ArrayList<Monster>();
		list.add(new Monster(2, "Rose Deer", 2000, 125, 100, 150, 0.0,3, new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 3)));
		list.add(new Monster(3, "Fire Martin", 2000, 100, 150, 125, 0.0,1, new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 1)));
		list.add(new Monster(4, "Turtle", 1000, 100, 100, 100, 50.0,2, new SupportAbility("Increase attack", "Moderately increase attack", 1, 45, 1.5, 1,3)));
		list.add(new Monster(5, "Sea Horse",800, 120, 50, 130, 50.0,2, new SupportAbility("Increase defense", "Moderately increase defense", 1, 50, 1.5, 2,3)));
		list.add(new Monster(6, "Grass Snake", 1500, 70, 130, 70, 50.0,3, new SupportAbility("Increase speed", "Moderately increase speed", 1, 55, 1.5, 3,3)));
		return list;
	}
	
	
	public List<Player> getPlayer() {
		SQLiteDatabase db2 = this.getWritableDatabase();
		return PlayerManager.getPlayer(db2);
	}
	
	public int updatePlayer(Player player) {
		SQLiteDatabase db = this.getWritableDatabase();
		return PlayerManager.updatePlayer(db, player);
	}
	
	public void addPlayer(Player player) {
		SQLiteDatabase db = this.getWritableDatabase();
		PlayerManager.addPlayer(db, player);
		db.close();
	}
	
	/**
	 * 
	 * EQUIPMENT
	 * 
	 */
	
	public ArrayList<Equipment> getEquipments() {
		SQLiteDatabase db = this.getWritableDatabase();
		return EquipmentManager.getEquipment(db);
	}
	
	public int updateEquipment(Equipment equipment) {
		SQLiteDatabase db = this.getWritableDatabase();
		return EquipmentManager.updateEquipment(db, equipment);
	}
	
	public void addEquipment(Equipment equipment) {
		SQLiteDatabase db = this.getWritableDatabase();
		EquipmentManager.addEquipment(db, equipment);
		db.close();
	}
	
	public void deleteEquipment(Equipment equipment) {
		SQLiteDatabase db = this.getWritableDatabase();
		EquipmentManager.deleteEquipment(db, equipment);
		db.close();
	}
	
	public ArrayList<Equipment> getEquippedEquipment() {
		SQLiteDatabase db = this.getWritableDatabase();
		return EquipmentManager.getEquipped(db);
	}
	
	// Returns a list of equipments that has a certain category.
	public ArrayList<Equipment> getEquipmentCategory(int category) {
		SQLiteDatabase db = this.getWritableDatabase();
		return EquipmentManager.getEquipmentCategory(db, category);
	}
	
	
	/**
	 * 
	 * Sticker
	 * 
	 */
	
	public ArrayList<Sticker> getStickers() {
		SQLiteDatabase db = this.getWritableDatabase();
		return StickerManager.getSticker(db);
	}
	
	public int updateSticker(Sticker sticker) {
		SQLiteDatabase db = this.getWritableDatabase();
		return StickerManager.updateSticker(db, sticker);
	}
	
	public void addSticker(Sticker sticker) {
		SQLiteDatabase db = this.getWritableDatabase();
		StickerManager.addSticker(db, sticker);
		db.close();
	}
	
	public void deleteSticker(Sticker sticker) {
		SQLiteDatabase db = this.getWritableDatabase();
		StickerManager.deleteSticker(db, sticker);
		db.close();
	}
	
	public void addStickers(ArrayList<Sticker> stickers) {
		SQLiteDatabase db = this.getWritableDatabase();
		StickerManager.addStickers(db, stickers);
		db.close();
	}
	
	public ArrayList<Sticker> getEquippedStickers() {
		SQLiteDatabase db = this.getWritableDatabase();
		return StickerManager.getEquippedStickers(db);
	}
	
	public ArrayList<Sticker> getUnequipedStickers() {
		SQLiteDatabase db = this.getWritableDatabase();
		return StickerManager.getUnequppedStickersWithNull(db);
	}
	
	
	/**
	 * int pstid, int pid, int sid, String name, int color,
			int current_level, int current_exp, int current_speed,
			int current_reach, int spaid, int saaid, int evolve, int equipped, int position,
			int hp, int attack, int defense, int speed, double capture
	 */
	public ArrayList<Sticker> getFakeEquippedParty() {
		ArrayList<Sticker> list = new ArrayList<Sticker>();
		list.add(new Sticker(1, 1, 100, "Artic Babbit", 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 2000, 150, 125, 100, 0.0,2,new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 2)));
		list.add(new Sticker(2, 1, 101, "Rose Deer", 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2000, 125, 100, 150, 0.0,3, new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 3)));
		list.add(new Sticker(4, 1, 103, "Turtle", 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 1000, 100, 100, 100, 50.0,2, new SupportAbility("Increase attack", "Moderately increase attack", 1, 50, 1.5, 1,3)));
		list.add(new Sticker(5, 1, 104, "Sea Horse", 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 800, 120, 50, 130, 50.0,2, new SupportAbility("Increase defense", "Moderately increase defense", 1, 50, 1.5, 2,3)));
		return list;
	}
	
	/**
	 * 
	 * City
	 * 
	 */
	
	public ArrayList<City> getCities() {
		SQLiteDatabase db = this.getWritableDatabase();
		return CityManager.getCity(db);
	}
	
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
	 * Routes
	 * 
	 */
	
	public ArrayList<Route> getRoutes() {
		SQLiteDatabase db = this.getWritableDatabase();
		return RouteManager.getRoutes(db);
	}
	
	/**
	 * 
	 * Dungeons
	 * 
	 */
	
	public ArrayList<Dungeon> getDungeons() {
		SQLiteDatabase db = this.getWritableDatabase();
		return DungeonManager.getDungeons(db);
	}
	
	/**
	 * 
	 * MONSTERS
	 * 
	 */
	
	/**
	 * A stub that generates a list of monsters that can be encountered in the game
	 * @return a list of monsters the list index + 1 = monsterId
	 */
	
	//id,hp,attack,defense,speed,capture
	public ArrayList<Monster> getMonsters() {
		ArrayList<Monster> list = new ArrayList<Monster>();
		
		list.add(new Monster(1, "Artic Babbit" ,2000, 150, 125, 100, 0.0,2, new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 2)));
		list.add(new Monster(2, "Rose Deer", 2000, 125, 100, 150, 0.0,3, new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 3)));
		list.add(new Monster(3, "Fire Martin", 2000, 100, 150, 125, 0.0,1, new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 1)));
		list.add(new Monster(4, "Turtle", 1000, 100, 100, 100, 50.0,2, new SupportAbility("Increase attack", "Moderately increase attack", 1, 50, 1.5, 1,3)));
		list.add(new Monster(5, "Sea Horse",800, 120, 50, 130, 50.0,2, new SupportAbility("Increase defense", "Moderately increase defense", 1, 50, 1.5, 2,3)));
		list.add(new Monster(6, "Grass Snake", 1500, 70, 130, 70, 50.0,3, new SupportAbility("Increase speed", "Moderately increase speed", 1, 50, 1.5, 3,3)));
		return list;
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
	public SparseArray<ArrayList<Route>> getCityRoutes() {
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
			routes.get(i).monsters.add(new Monster(4, "Turtle", 1000, 100, 100, 100, 50.0,2, new SupportAbility("Increase attack", "Moderately increase attack", 1, 50, 1.5, 1,3)));
			routes.get(i).monsters.add(new Monster(5, "Sea Horse",800, 120, 50, 130, 50.0,2, new SupportAbility("Increase defense", "Moderately increase defense", 1, 50, 1.5, 2,3)));
			routes.get(i).monsters.add(new Monster(6, "Grass Snake", 1500, 70, 130, 70, 50.0,3, new SupportAbility("Increase speed", "Moderately increase speed", 1, 50, 1.5, 3,3)));
		}
		city1Route.add(routes.get(0));
		ArrayList<Route> city2Route = new ArrayList<Route>();
		city2Route.add(routes.get(1));
		map.put(1, city1Route);
		map.put(2, city2Route);
		return map;
	}
	
	/**
	 * Stub that setup the initial mapping of cities to their dungeons
	 * @return a SparseArray that contains all of the mapping of cities to their dungeons
	 * 		   in the form of cityId ->list of dungeons
	 */
	public SparseArray<ArrayList<Dungeon>> getCityDungeons() {
		SparseArray<ArrayList<Dungeon>> map = new SparseArray<ArrayList<Dungeon>>();
		ArrayList<Dungeon> city1Dungeon = new ArrayList<Dungeon>();
		ArrayList<Dungeon> dungeons = getDungeons();
		
		// maps dungeons to their monsters, would have to loop through
		// the mapping to add the dungeon
		// getDungeonMonster()
		
		// future, probably empty or doesn't exist and just get the list from db call
		dungeons.get(0).monsters.add(new Monster(3, "Fire Martin", 2000, 100, 150, 125, 0.0,1, new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 1)));
		dungeons.get(0).monsters.add(new Monster(4, "Turtle", 1000, 100, 100, 100, 50.0,2, new SupportAbility("Increase attack", "Moderately increase attack", 1, 50, 1.5, 1,3)));
		dungeons.get(1).monsters.add(new Monster(5, "Sea Horse",800, 120, 50, 130, 50.0,2, new SupportAbility("Increase defense", "Moderately increase defense", 1, 50, 1.5, 2,3)));
		dungeons.get(1).monsters.add(new Monster(6, "Grass Snake", 1500, 70, 130, 70, 50.0,3, new SupportAbility("Increase speed", "Moderately increase speed", 1, 50, 1.5, 3,3)));
		// maps from city to dungeon id 
		// similar to routes, need to get a mapping from the actual db
		city1Dungeon.add(dungeons.get(0));
		ArrayList<Dungeon> city2Dungeon = new ArrayList<Dungeon>();
		city2Dungeon.add(dungeons.get(1));
		map.put(1, city1Dungeon);
		map.put(2, city2Dungeon);
		return map;
	}
	
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
	public SparseArray<ArrayList<Integer>> getRouteMonsters() {
		SparseArray<ArrayList<Integer>> map = new SparseArray<ArrayList<Integer>>();
		ArrayList<Integer> route1Monsters = new ArrayList<Integer>();
		route1Monsters.add(4);
		route1Monsters.add(5);
		route1Monsters.add(6);
		map.put(1, route1Monsters);
		map.put(2, route1Monsters);
		return map;
	}
	   
	/**
	 * Stub that setup the initial mapping of monsters to their dungeons
	 * @return a SparseArray that contains all of the mapping of monsters to their dungeons
	 * 		   in the form of dungeonId -> list of monsters
	 */
	public SparseArray<ArrayList<Integer>> getDungeonMonsters() {
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
	}

}

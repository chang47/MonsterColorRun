package DB;

import java.util.ArrayList;
import java.util.List;

























import metaModel.City;
import metaModel.Dungeon;
import metaModel.Route;

import org.xml.sax.Parser;

import dbReference.CityManager;
import dbReference.DungeonManager;
import dbReference.RouteManager;
import Abilities.Ability;
import Abilities.DamageAbility;
import Abilities.SupportAbility;
import DB.Model.Equipment;
import DB.Model.Monster;
import DB.Model.Player;
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
	
	private static final int ATTACK = 150;
	private static final int HP = 1000;
	// int uid, String name, int hp, int attack, int defense, int speed, double capture, int element, Ability ability, 
	// int position, int equipped, int exp, int level, int evolve, int monsterId
	private static final Monster RABBIT  = new Monster(1, "Artic Babbit" , HP, ATTACK, 125, 100, 0.0,2, 
			new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 2, 1), 0, 0, 0, 1, 0, 1);
	private static final Monster DEER = new Monster(2, "Rose Deer", HP, ATTACK, 100, 150, 0.0,3, 
			new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 3, 1), 0, 0, 0, 1, 0, 2);
	private static final Monster MARTIN = new Monster(3, "Fire Martin", HP, ATTACK, 150, 125, 0.0,1, 
			new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 1, 1), 0, 0, 0, 1, 0, 3);
	private static final Monster TURTLE = new Monster(4, "Turtle", HP, ATTACK, 100, 100, 50.0,2, 
			new SupportAbility("Increase attack", "Moderately increase attack", 1, 50, 1.5, 1, 3, 2), 0, 0, 0, 1, 0, 4);
	private static final Monster SEAHORSE = new Monster(5, "Sea Horse",HP, ATTACK, 50, 130, 50.0,2, 
			new SupportAbility("Increase defense", "Moderately increase defense", 1, 50, 1.5, 2, 3, 2), 0, 0, 0, 1, 0, 5);
	private static final Monster SNAKE = new Monster(6, "Grass Snake", HP, ATTACK, 130, 70, 50.0,3, 
			new SupportAbility("Increase speed", "Moderately increase speed", 1, 50, 1.5, 3, 3, 2), 0, 0, 0, 1, 0, 6);

	@Override
	public void onCreate(SQLiteDatabase db) {
		PlayerManager.create(db);
		EquipmentManager.create(db);
		StickerManager.create(db);
		CityMappingManager.create(db);
	}

	@Override
	// drops and recreates everything
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		PlayerManager.drop(db);
		EquipmentManager.drop(db);
		StickerManager.drop(db);
		CityMappingManager.drop(db);
		onCreate(db);
	}
	
	/**
	 * 
	 * PLAYER
	 * 
	 */
	
/*	public ArrayList<Monster> getParty() {
		ArrayList<Monster> list = new ArrayList<Monster>();
		list.add(new Monster(2, "Rose Deer", 2000, 125, 100, 150, 0.0,3, new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 3)));
		list.add(new Monster(3, "Fire Martin", 2000, 100, 150, 125, 0.0,1, new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 1)));
		list.add(new Monster(4, "Turtle", 1000, 100, 100, 100, 50.0,2, new SupportAbility("Increase attack", "Moderately increase attack", 1, 45, 1.5, 1,3)));
		list.add(new Monster(5, "Sea Horse",800, 120, 50, 130, 50.0,2, new SupportAbility("Increase defense", "Moderately increase defense", 1, 50, 1.5, 2,3)));
		list.add(new Monster(6, "Grass Snake", 1500, 70, 130, 70, 50.0,3, new SupportAbility("Increase speed", "Moderately increase speed", 1, 55, 1.5, 3,3)));
		return list;
	}*/
	
	
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
	
	public ArrayList<Monster> getStickers() {
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Monster> monsters = new ArrayList<Monster>();
		for (Sticker sticker : StickerManager.getSticker(db)) {
			monsters.add(util.Parser.stickerToMonster(sticker));
		}
		return monsters;
	}
	
	public int updateSticker(Monster monster) {
		SQLiteDatabase db = this.getWritableDatabase();
		return StickerManager.updateSticker(db, util.Parser.MonsterToSticker(monster));
	}
	
	public void addSticker(Monster monster) {
		// TODO you need to reload the player sticker so they have the accurate information
		SQLiteDatabase db = this.getWritableDatabase();
		StickerManager.addSticker(db, util.Parser.CapturedMonsterToSticker(monster));
		db.close();
	}
	
	public void deleteSticker(Monster monster) {
		SQLiteDatabase db = this.getWritableDatabase();
		StickerManager.deleteSticker(db, monster.uid);
		// TODO you need to reload the player sticker so they have the accurate information
		db.close();
	}
	
	public void addStickers(ArrayList<Monster> monsters) {
		SQLiteDatabase db = this.getWritableDatabase();
		List<Sticker> stickers = new ArrayList<Sticker>();
		// TODO you need to reload the player sticker so they have the accurate information
		for (Monster monster : monsters) 
			stickers.add(util.Parser.CapturedMonsterToSticker(monster));
		StickerManager.addStickers(db, stickers);
		db.close();
	}
	
	public ArrayList<Monster> getEquippedStickers() {
		SQLiteDatabase db = this.getWritableDatabase();
		ArrayList<Monster> monsters = new ArrayList<Monster>();
		for (Sticker sticker : StickerManager.getEquippedStickers(db)) {
			if (sticker != null)
				monsters.add(util.Parser.stickerToMonster(sticker));
			else 
				monsters.add(null);
		}
		return monsters;
	}

/*	Not used anymore, it's a ui thing in how things get sorted.	
	public ArrayList<Sticker> getUnequipedStickers() {
		SQLiteDatabase db = this.getWritableDatabase();
		return StickerManager.getUnequppedStickersWithNull(db);
	}
*/
	
	/**
	 * int pstid, int pid, int sid, String name, int color,
			int current_level, int current_exp, int current_speed,
			int current_reach, int spaid, int saaid, int evolve, int equipped, int position,
			int hp, int attack, int defense, int speed, double capture
	 */
/*	public ArrayList<Sticker> getFakeEquippedParty() {
		ArrayList<Sticker> list = new ArrayList<Sticker>();
		list.add(new Sticker(1, 1, 100, "Artic Babbit", 1, 1, 0, 1, 1, 1, 1, 1, 2000, 150, 125, 100, 0.0,2,new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 2)));
		list.add(new Sticker(2, 1, 101, "Rose Deer", 2, 1, 1, 1, 1, 1, 1, 2, 2000, 125, 100, 150, 0.0,3, new DamageAbility("Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 3)));
		list.add(new Sticker(4, 1, 103, "Turtle", 1, 1, 1, 1, 1, 1, 1, 4, 1000, 100, 100, 100, 50.0,2, new SupportAbility("Increase attack", "Moderately increase attack", 1, 50, 1.5, 1, 3)));
		list.add(null);
		list.add(new Sticker(5, 1, 104, "Sea Horse", 1, 1, 1, 1, 1, 1, 1, 5, 800, 120, 50, 130, 50.0,2, new SupportAbility("Increase defense", "Moderately increase defense", 1, 50, 1.5, 2,3)));
		return list;
	}*/

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
		
		list.add(RABBIT);
		list.add(DEER);
		list.add(MARTIN);
		list.add(TURTLE);
		list.add(SEAHORSE);
		list.add(SNAKE);
		return list;
	}
	

}

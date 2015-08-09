package util;

import java.util.ArrayList;
import java.util.List;

import metaModel.DungeonMonsters;
import metaModel.RouteMonsters;

import com.brnleehng.worldrunner.Hub;

import Abilities.Ability;
import Abilities.DamageAbility;
import Abilities.SupportAbility;
import DB.Model.Monster;
import DB.Model.Sticker;

public class Parser {

	public String getPlayerName(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		return arr[0];
	}
	
	public int getMoney(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		return Integer.parseInt(arr[1]);
	}
	
	public int getGem(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		return Integer.parseInt(arr[2]);
	}
	
	public String[] getEquipment(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		String[] equipment = arr[3].split(",");
		if (equipment.length > 6) {
			throw new IllegalArgumentException("Equipment can't be greater than 5");
		}
		return equipment;
	}
	
	public String[] getInventory(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		String[] inventory = arr[4].split(",");
		return inventory;
	}
	
	public String[] getFriends(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		String[] inventory = arr[5].split(",");
		return inventory;
	}
	
	public int getLevel(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		return Integer.parseInt(arr[6]);
	}
	
	public int getExp(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		return Integer.parseInt(arr[7]);
	}
	
	private void checkError(String[] arr) {
		if (arr.length == 0) {
			throw new IllegalArgumentException("Give no elements");
		}
		if (arr.length != 5) {
			throw new IllegalArgumentException("List has incorrect # of elements");
		}
	}
	
	public static Monster stickerToMonster(Sticker sticker) {
		return new Monster(sticker.pstid, sticker.name, sticker.hp, sticker.attack, sticker.defense, 
				sticker.speed, sticker.capture, sticker.element, getAbility(sticker), sticker.position, sticker.equipped,
				sticker.current_exp, sticker.current_level, sticker.evolve, sticker.sid);
	}
	
	public static Sticker CapturedMonsterToSticker(Monster monster) {
		/*
		 	public Sticker(int pstid, int pid, int sid, String name, int color,
			int current_level, int current_exp, int spaid, int saaid, int evolve, int equipped, int position,
			int hp, int attack, int defense, int speed, double capture, int element) {
			*/
		// TODO need to get a list of default stats for stickers, for now we'll just put placeholders
		// generates new sticker id when being added
		return new Sticker(-1, Hub.player.pid, monster.monsterId, monster.name, monster.element,
				1, 0, monster.activeAbility.abilityId, monster.activeAbility.abilityId, 0, 0, 0, monster.hp, monster.attack, monster.defense, 
				monster.speed, monster.capture);
		
		 
	}
	
	public static Sticker MonsterToSticker(Monster monster) {
		// TODO might requires the list of all monsters
		return new Sticker(monster.uid, Hub.player.pid, monster.monsterId, monster.name, monster.element,
				monster.level, 
				monster.exp, 
				monster.activeAbility.abilityId, 
				monster.activeAbility.abilityId, monster.evolve, monster.equipped, monster.position, 
				monster.hp, monster.attack, monster.defense, 
				monster.speed, monster.capture);
		
	}
	
	/**
	 * Creates a list of monster for a route
	 * @param stickers
	 * @param routeMonsters
	 * @return
	 */
	public static List<Monster> enemyRouteStickersToEnemyMonsters(List<Sticker> stickers, List<RouteMonsters> routeMonsters) {
		List<Monster> monsters = new ArrayList<Monster>();
		for (RouteMonsters monster : routeMonsters) {
			Sticker sticker = stickers.get(monster.monsterId - 1);
			monsters.add(convertEnemyStickerToMonster(sticker, monster));
		}
		return monsters;
	}
	
	/**
	 * Creates a list of monsters for a dungeon
	 * @param stickers
	 * @param dungeonMonsters
	 * @return
	 */
	public static List<Monster> enemyDungeonStickersToEnemyMonsters(List<Sticker> stickers, List<DungeonMonsters> dungeonMonsters) {
		List<Monster> monsters = new ArrayList<Monster>();
		for (DungeonMonsters monster : dungeonMonsters) {
			Sticker sticker = stickers.get(monster.monsterId - 1);
			monsters.add(convertEnemyStickerToMonster(sticker, monster));
		}
		return monsters;
	}
	
	/**
	 * Converts a sticker to a route monster
	 * @param sticker
	 * @param routeMonster
	 * @return
	 */
	public static Monster convertEnemyStickerToMonster(Sticker sticker, RouteMonsters routeMonster) {
		
		return new Monster(-1, sticker.name, sticker.hp, sticker.attack, sticker.defense, sticker.speed, routeMonster.capture, sticker.element,
				null, -1, -1, 0, routeMonster.level, sticker.evolve, sticker.sid);
	}
	
	/**
	 * Converts a sticker to a dungeon monster
	 * @param sticker
	 * @param dungeonMonster
	 * @return
	 */
	public static Monster convertEnemyStickerToMonster(Sticker sticker, DungeonMonsters dungeonMonster) {
		return new Monster(-1, sticker.name, sticker.hp, sticker.attack, sticker.defense, sticker.speed, dungeonMonster.capture, sticker.element,
				getAbility(sticker), -1, -1, 0, dungeonMonster.level, sticker.evolve, sticker.sid);
	}
	
	/**
	 * Return the ability from the given sticker
	 * @param sticker
	 * @return
	 */
	private static Ability getAbility(Sticker sticker) {
		Ability ability;
		switch (sticker.saaid) {
		// damage ability
		case 1:
			// TODO need to have some table of all ability so that it can be used to get the needed data
			//"Damage all", "Does moderate damage to all enemies", 1, 10, 200.0, 2
			ability = new DamageAbility("Damage All", "Does moderate damage to one enemy", 1, 10, 200.0, 2, 1);
			break;
		case 2:
			//"Increase attack", "Moderately increase attack", 1, 50, 1.5, 1,3
			ability = new SupportAbility("Increase attack", "Modterately increase attack", 1, 50, 1.5, 1, 3, 2);
			break;
		default:
			throw new Error("ability id is not within the acceptable range, crashed at " + sticker.name + " with ability id: "
					 + sticker.saaid);
		}
		return ability;
	}
}
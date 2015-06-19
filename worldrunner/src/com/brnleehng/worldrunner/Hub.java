package com.brnleehng.worldrunner;	

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import DB.CreateDB;
import DB.DBManager;
import DB.Model.City;
import DB.Model.Dungeon;
import DB.Model.Equipment;
import DB.Model.Monster;
import DB.Model.Player;
import DB.Model.Route;
import DB.Model.Sticker;
import Items.EquipEquipment;
import Items.EquipItem;
import Items.EquipSticker;
import Items.SellEquipmentGrid;
import Items.SellStickerGrid;
import Items.ViewEquipment;
import Items.ViewSticker;
import Model.BattleMonster;
import Races.Result;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.SparseArray;

/**
 * Central Hub activity that manages everything the user does in
 * the game and uses Fragment to help move players around
 */
public class Hub extends Activity {
	private static FragmentManager fm;
	//Access to the game's DB
	private static DBManager db;	
	// stores all of the players information
	private static Player player;
	// contains all of the player's equipments
	private static ArrayList<Equipment> equipmentList;
	// contains all of the player's stickers
	private static ArrayList<Sticker> stickerList;
	private static HeaderBar header;
	private static FooterBar footer;
	// contains the list of the equipment the player is currently using
	private static ArrayList<Equipment> equippedEquipments;
	// contains the list of the stickers the player is currently using
	public static ArrayList<Sticker> equippedStickers;
	// the equipment that the player selected to be changed. Only used in equip 
	private static Equipment currentEquipment;
	// The sticker that the player selected to be changed. Only used in equip
	public static Sticker currentSticker;
	// the category/position of the equipment that the character selected to be replaced in equip
	private static int currentCategory;
	// the position of the sticker that the character selected to be replaced in equip
	public static int currentStickerPosition;
	// current location usually stored in the player model?
	private static City currentCity;
	public static ArrayList<Monster> monsterList;
	public static ArrayList<Monster> partyList;
	public static ArrayList<City> cities;
	public static ArrayList<BattleMonster> partyBattleList;
	
	//private static FragmentTransaction ft;
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@SuppressLint("CommitTransaction")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hub_activity);
		
		// Sets up all of the user's data  
		db = new DBManager(getApplicationContext());
		List<Player> playerList = db.getPlayer();
		player = playerList.get(0);
		equipmentList = db.getEquipments();
		stickerList = db.getStickers();
		equippedEquipments = db.getEquippedEquipment();
		
		// @TODO getting the mock data. Will probably break the equipped sticker until changed 
		//equippedStickers = db.getEquippedStickers();
		equippedStickers = db.getFakeEquippedParty();
		monsterList = db.getMonsters();
		partyList = db.getParty();
		SparseArray<ArrayList<Route>> cityRouteList = db.getCityRoutes();
		SparseArray<ArrayList<Dungeon>> cityDungeonList = db.getCityDungeons();
		cities = db.getCities();
		Iterator<City> iter = cities.iterator();
		while (iter.hasNext()) {
			City city = iter.next();
			city.routes = cityRouteList.get(city.cityId);
			city.dungeons = cityDungeonList.get(city.cityId);
		}
		currentCity = cities.get(0);
		db.close();
		currentEquipment = null;
		currentCategory = 0;
		currentSticker = null;
		currentStickerPosition = 0;
		if (equippedEquipments.size() != 0) {
			Collections.sort(equippedEquipments);
		}
		// prevents re-making hub if already made
		if (findViewById(R.id.hub) != null) {
			if (savedInstanceState != null) {
				return;
			}
			Race race = new Race();
			header = new HeaderBar();
			footer = new FooterBar();
			race.setArguments(getIntent().getExtras());
			fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.header, header);
			ft.replace(R.id.footer, footer);
			ft.replace(R.id.hub, race);
			ft.commit();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

    @Override
    public void onPause() {
        super.onPause();
    }
    
    
    /**
     * GET PLAYER INFO
     */
    public static Player getPlayer() {
    	return player;
    }
    
    public static ArrayList<Equipment> getEquipment() {
    	return equipmentList;
    }
    
    public static ArrayList<Sticker> getStickers() {
    	return stickerList;
    }
    
    public static ArrayList<Equipment> getEquippedEquipment() {
    	return equippedEquipments;
    }
    
    public static void setEquippedEquipment(ArrayList<Equipment> equipments) {
    	equippedEquipments = equipments;
    }
    
    public static City getCurrentCity() {
    	return currentCity;
    }
    
    public static void setCurrentCity(City newCity) {
    	currentCity = newCity;
    }
    
    /**
     * CHANGE CURRENT EQUIPMENTS 
     */
    
    
    public static void setCurrentEquipment(Equipment equipment) {
    	currentEquipment = equipment;
    }
    
    public static Equipment getCurrentEquipment() {
    	return currentEquipment;
    }
    
    public static int getCurrentCategory() {
    	return currentCategory;
    }
    
    public static void setCurrentCategory(int category) {
    	currentCategory = category;
    }
    
    /**
     * CHANGE CURRENT STICKER
     */
    
    public static void setCurrentSticker(Sticker sticker) {
    	currentSticker = sticker;
    }
    
    public static ArrayList<Sticker> getEquippedSticker() {
    	return equippedStickers;
    }
    
    /**
     * FRAGMENT CHANGES
     */
    public static void friends() {
    	FragmentTransaction ft = setFT();
    	Friends friend = new Friends();
    	ft.replace(R.id.hub, friend).commit();
    }
    
    public static void race() {
    	FragmentTransaction ft = setFT();
    	Race race = new Race();
    	ft.replace(R.id.hub, race).commit();
    }
    
    public static void items() {
    	FragmentTransaction ft = setFT();
    	Items items = new Items();
    	ft.replace(R.id.hub, items).commit();
    }
    
    public static void store() {
    	FragmentTransaction ft = setFT();
    	Store store = new Store();
    	ft.replace(R.id.hub, store).commit();
    }
    
    public static void selectFriend() {
    	FragmentTransaction ft = setFT();
    	Pregame pregame = new Pregame();
    	ft.replace(R.id.hub, pregame).commit();
    }

	public static void viewEquipment() {
		FragmentTransaction ft = setFT();
    	ViewEquipment viewEquipment = new ViewEquipment();
    	ft.replace(R.id.hub, viewEquipment).commit();
	}
	
	public static void viewSticker() {
		FragmentTransaction ft = setFT();
    	ViewSticker viewSticker = new ViewSticker();
    	ft.replace(R.id.hub, viewSticker).commit();
	}
	
	public static void sellEquipment() {
		FragmentTransaction ft = setFT();
    	SellEquipmentGrid sellEquipment = new SellEquipmentGrid();
    	ft.replace(R.id.hub, sellEquipment).commit();
	}
	
	public static void sellSticker() {
		FragmentTransaction ft = setFT();
    	SellStickerGrid sellEquipment = new SellStickerGrid();
    	ft.replace(R.id.hub, sellEquipment).commit();
	}
	
	public static void databaseOptions() {
		FragmentTransaction ft = setFT();
    	Database database = new Database();
    	ft.replace(R.id.hub, database).commit();
	}
	
	public static void createDB() {
		FragmentTransaction ft = setFT();
		CreateDB createDB = new CreateDB();
		ft.replace(R.id.hub, createDB).commit();
	}
	
	public static void startRun() {
		FragmentTransaction ft = setFT();
		FreeRun run = new FreeRun();
		ft.remove(header);
		ft.remove(footer);
		ft.replace(R.id.hub, run).commit();
	}
	
	public static void result() {
		stickerList = db.getStickers();
		FragmentTransaction ft = setFT();
		Result result = new Result();
		ft.replace(R.id.hub, result).commit();
	}
	
	public static void backToRace() {
		FragmentTransaction ft = setFT();
		Race race = new Race();
		ft.replace(R.id.header, header);
		ft.replace(R.id.footer, footer);
		ft.replace(R.id.hub, race).commit();
	}
	
	public static void equipItems() {
		FragmentTransaction ft = setFT();
		EquipItem equip = new EquipItem();
		ft.replace(R.id.hub, equip).commit();
	}
	
	public static void equipEquipment(int category, Equipment equipment) {
		setCurrentCategory(category);
		setCurrentEquipment(equipment);
		FragmentTransaction ft = setFT();
		EquipEquipment equip = new EquipEquipment();
		ft.replace(R.id.hub, equip).commit();		
	}
	
	/**
	 * Changes the sticker that will be equipped at the given position
	 * @param position - the position that the sticker will be replaced at: 0-4
	 * @param sticker - the sticker that is being replaced 
	 */
	public static void equipSticker(int position, Sticker sticker) {
		// is this needed?
		currentStickerPosition = position;
		currentSticker = sticker;
		FragmentTransaction ft = setFT();
		EquipSticker equip = new EquipSticker();
		ft.replace(R.id.hub, equip).commit();
	}
	
	public static void cityHub() {
		FragmentTransaction ft = setFT();
		CityHub townHub = new CityHub();
		ft.replace(R.id.hub, townHub).commit();
	}
	
	/**
	 * Selects the new route to run towards
	 * @param newCity - the city that the user is trying to run to.
	 */
	public static void changeCity(int newCity) {
		//@TODO check if you need to subtract 1
		setCurrentCity(cities.get(newCity - 1));
		
		// Needs to be changed to 
		selectFriend();
	}
	 
	
	
	/**
	 *  HELPER FUNCTIONS
	 */
	
	// Sets the animation to the Fragment Transaction
	private static FragmentTransaction setFT() {
		FragmentTransaction ft = fm.beginTransaction();
    	ft.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_right, 0, 0);
    	return ft;
	}
		
}

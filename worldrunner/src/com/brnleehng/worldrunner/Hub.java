package com.brnleehng.worldrunner;	

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.github.amlcurran.showcaseview.ShowcaseView;

import battleHelper.BackgroundChecker;
import battleHelper.BattleInfo;
import test.TestRun;
import dbReference.ReferenceManager;
import metaModel.City;
import metaModel.Dungeon;
import metaModel.DungeonMonsters;
import metaModel.MetaAbility;
import metaModel.Route;
import metaModel.RouteMonsters;
import DB.CreateDB;
import DB.DBManager;
import DB.Model.Equipment;
import DB.Model.Monster;
import DB.Model.Player;
import DB.Model.Sticker;
import Items.EquipEquipment;
import Items.EquipItem;
import Items.EquipSticker;
import Items.SellEquipmentGrid;
import Items.SellStickerGrid;
import Items.ViewEquipment;
import Items.ViewSticker;
import Races.DungeonRun;
import Races.Result;
import Races.RouteRun;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.FrameLayout;


/**
 * Central Hub activity that manages everything the user does in
 * the game and uses Fragment to help move players around
 */
public class Hub extends Activity {
	private static MediaPlayer backgroundMusic;
	private static FragmentManager fm;
	//Access to the game's DB
	private static DBManager db;	
	private static ReferenceManager refDb;
	// stores all of the players information
	public static Player player;
	// contains all of the player's equipments
	private static ArrayList<Equipment> equipmentList;
	// contains all of the player's stickers
	public static ArrayList<Monster> stickerList;
	private static HeaderBar header;
	private static FooterBar footer;
	public static FrameLayout hubContentContainer;
	// contains the list of the equipment the player is currently using
	private static ArrayList<Equipment> equippedEquipments;
	// contains the list of the stickers the player is currently using
	public static ArrayList<Monster> equippedStickers;
	// the equipment that the player selected to be changed. Only used in equip 
	private static Equipment currentEquipment;
	// The sticker that the player selected to be changed. Only used in equip
	public static Monster currentSticker;
	// the category/position of the equipment that the character selected to be replaced in equip
	private static int currentCategory;
	// the position of the sticker that the character selected to be replaced in equip
	public static int currentStickerPosition;
	// current location usually stored in the player model?
	public static City currentCity;
	public static ArrayList<Monster> partyList;
	//public static ArrayList<BattleMonster> partyBattleList;
	public static List<Monster> unequippedMonster;
	
	public static Route currentRoute;
	public static Dungeon currentDungeon;
	public static List<Monster> enemyList;
	
	//public static ArrayList<Monster> tempEquippedSticker;
	
	public static Monster viewSticker;
	
	// reference data
	public static SparseArray<List<RouteMonsters>> refRouteMonsters; // routeId : mosnterIds
	public static SparseArray<List<DungeonMonsters>> refDungeonMonsters; // dungeonId : mosnterIds
	public static SparseArray<List<Dungeon>> refDungeons; // cityId : dungeonIds
	public static SparseArray<List<Route>> refRoutes; // cityId : routeIds
	public static List<City> refCities;
	public static List<int[]> expTable;
	public static List<Sticker> refMonsters;
	public static List<MetaAbility> refAbilities;
	
	private static Context context;
	
	public static SoundPool sp;
	static int soundIds[];
	
	public static int weight;
	public static int feet;
	public static int inch;
	
	//private static FragmentTransaction ft;
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		backgroundMusic.release();
		Log.d("disconnect", "hub got destroued");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}

	@SuppressLint("CommitTransaction")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("notifTest", "ran hub create");
		setContentView(R.layout.hub_activity);
		
		SharedPreferences pref = getApplication().getSharedPreferences("MonsterColorRun", Context.MODE_PRIVATE);
		weight = pref.getInt(getString(R.string.weight), 150);
		feet = pref.getInt(getString(R.string.feet), 5);
		inch = pref.getInt(getString(R.string.inch), 7);
		Log.d("playerInfo", "weight " + weight + " feet " + feet + " inch " + inch);
		sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		soundIds = new int[2];
		soundIds[0] = sp.load(getBaseContext(), R.raw.click, 1);
		soundIds[1] = sp.load(getBaseContext(), R.raw.enterbattle, 1);
		
		
		context = getApplicationContext();
		db = new DBManager(getApplicationContext());
		
		hubContentContainer = (FrameLayout) findViewById(R.id.hubContentLayout);

		BackgroundChecker.finish();
		
		refDb = new ReferenceManager(getApplicationContext());
		
		// creates the reference information
/*		SparseArray<List<Route>> cityRouteList = refDb.getCityRoutes();
		SparseArray<List<Dungeon>> cityDungeonList = refDb.getCityDungeons();*/
		refCities = refDb.getCitiesList();
		refRouteMonsters = refDb.getRouteMonstersList();
		refDungeonMonsters = refDb.getDungeonMonstersList();
		refDungeons = refDb.getDungeonsList();
		refRoutes = refDb.getRoutesList();
		expTable = refDb.getExp();
		refMonsters = refDb.monstersList();
		refAbilities = refDb.abilitiesList();
		
		// setup the user's data into the app
		getPlayerData(db);

		currentCity = refCities.get(player.city - 1);
		db.close(); // necessary to close the db?
		refDb.close();
		
		int resId = getResources().getIdentifier("background" + (currentCity.cityId - 1), "drawable", getPackageName());
		Log.d("background res", "" + resId);
		if (resId != 0) {
			hubContentContainer.setBackgroundResource(resId);
		}
		
		int musicId = getResources().getIdentifier("music" + (currentCity.cityId - 1), "raw", getPackageName());
		if (musicId != 0) {
			backgroundMusic = MediaPlayer.create(this, musicId);
			backgroundMusic.setLooping(true);
			backgroundMusic.start();
		}
		// set default values;
		currentEquipment = null;
		currentCategory = 0;
		currentSticker = null;
		viewSticker = null;
		currentStickerPosition = 0;
		if (equippedEquipments.size() != 0) {
			Collections.sort(equippedEquipments);
		}
		
		enemyList = null;
		
		// prevents re-making hub if already made
		if (findViewById(R.id.hub) != null) {
			if (savedInstanceState != null) {
				return;
			}
			CityHub cityHub = new CityHub();
			header = new HeaderBar();
			footer = new FooterBar();
			cityHub.setArguments(getIntent().getExtras());
			fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.header, header);
			ft.replace(R.id.footer, footer);
			ft.replace(R.id.hub, cityHub);
			ft.commit();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (BackgroundChecker.inResult && !BackgroundChecker.battleStarted) {
			BackgroundChecker.inResult = false;
			Hub.backToCity();
		} else if (!BackgroundChecker.battleStarted) {
			finish();
			Intent intent = new Intent(this, SplashPage.class);
			startActivity(intent);
		} else {
			//finish();
		}
	}
	
	/**
	 * Used to re-create/update the user database so that users
	 * can see changes when they are made
	 */
	public static void getPlayerData(DBManager db) {
		// Sets up all of the user's data 
		List<Player> playerList = db.getPlayer();
		player = playerList.get(0);
		equipmentList = db.getEquipments();
		stickerList = db.getStickers();
		equippedEquipments = db.getEquippedEquipment();
		
		// @TODO the real one is the one being used below with mock data
		// this is just for testing
		//tempEquippedSticker = db.getEquippedStickers();
		
		// @TODO getting the mock data. Will probably break the equipped sticker until changed 
		equippedStickers = db.getEquippedStickers();
		//equippedStickers = db.getFakeEquippedParty();
		
		//monsterList = db.getMonsters();
		//partyList = db.getParty();
		
		unequippedMonster = new ArrayList<Monster>();
		unequippedMonster.add(null);
		for (Monster monster : stickerList) {
			if (monster.equipped == 0)
				unequippedMonster.add(monster);
		}
	}

	
	
	@Override
	public void onResume() {
		try {
			super.onResume();
			if (backgroundMusic != null && !BackgroundChecker.battleStarted) {
	        	backgroundMusic.start();
	        }
			Intent intent = getIntent();
			try {
				int resumeValue = intent.getIntExtra("runmode", 0);
				// 1 = dungeon run, 2 = route run, anything else = nothing
				switch (resumeValue) {
					case 1:
						Log.d("notifTest", "route run");
						break;
					case 2:
						Log.d("notifTest", "dungeon run");
						break;
					default:
						Log.d("notifTest", "n/a");
						break;
				}
			} catch (Exception e) {
				Log.d("codeError", "failure in resuming game via notification from run");
				Log.d("codeError",  e.getClass().getName(), e);
			}
		} catch (Exception e) {
    		Log.d("clutter crash", "random hub crash");
			if (partyList == null) { Log.d("random hub crashes", "partyList is null"); }
			if (currentCity == null) { Log.d("random hub crashes", "current City is null"); }
			if (context == null) { Log.d("random hub crashes", "context is null"); } 
			if (BattleInfo.partyMonsterBattleList == null) {
	    		Log.d("random hub crashes", "partyMonsterBattleList is null");
	    		Log.d("random hub crashes", "finished current battle status: " + BackgroundChecker.finishedCurrentBattle);
	    		Log.d("random hub crashes", "has the combat started? " + BackgroundChecker.battleStarted);
	    		Log.d("random hub crashes", "was the monster attacked? " + BackgroundChecker.monsterWasAttacked);
	    		Log.d("random hub crashes", "was the player monster attacked? " + BackgroundChecker.playerMonsterWasAttacked);
	    		Log.d("random hub crashes", "was in the background? " + BackgroundChecker.isBackground);
	    		Log.d("random hub crashes", "Are there now new enemies? " + BackgroundChecker.newEnemies);
	    		if (BattleInfo.partyList == null) {
	    			Log.d("random crash", "partyList is null");
	    		} else {
	    			Log.d("random hub crashes", "partyList is not null");	
	    		}
	    	}
			Log.e("random hub crash", e.getClass().getName(), e);
		}
	}

    @Override
    public void onPause() {
        super.onPause();
        if (backgroundMusic != null && !BackgroundChecker.battleStarted && !BackgroundChecker.inResult) {
        	Log.d("showInfoStuff", "is being called " + BackgroundChecker.inResult);
        	backgroundMusic.pause();
        }
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
    
    public static ArrayList<Monster> getStickers() {
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
    
    public static void setCurrentSticker(Monster monster) {
    	currentSticker = monster;
    }
    
    public static ArrayList<Monster> getEquippedSticker() {
    	return equippedStickers;
    }
    
    /**
     * FRAGMENT CHANGES
     */
    public static void friends() {
		sp.play(soundIds[0], 1, 1, 1, 0, 1);
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
		sp.play(soundIds[0], 1, 1, 1, 0, 1);
    	FragmentTransaction ft = setFT();
    	Items items = new Items();
    	ft.replace(R.id.hub, items).commit();
    }
    
    public static void store() {
		sp.play(soundIds[0], 1, 1, 1, 0, 1);
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
		sp.play(soundIds[0], 1, 1, 1, 0, 1);
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
		enemyList = util.Parser.enemyDungeonStickersToEnemyMonsters(refMonsters, refDungeonMonsters.get(1));
		TestRun run = new TestRun();
		ft.remove(header);
		ft.remove(footer);
		ft.replace(R.id.hub, run).commit();
	}
	
	public static void goToResult() {
		FragmentTransaction ft = setFT();
		Result result = new Result();
		ft.replace(R.id.hub, result).commit();
	}
	
	public static void backToCity() {
		sp.play(soundIds[1], 1, 1, 1, 0, 1);
		FragmentTransaction ft = setFT();
		
		DBManager db = new DBManager(context);
		getPlayerData(db);
		CityHub townHub = new CityHub();
		int musicId = context.getResources().getIdentifier("music" + (currentCity.cityId - 1), "raw", context.getPackageName());
		if (musicId != 0) {
			backgroundMusic = MediaPlayer.create(context, musicId);
			backgroundMusic.setLooping(true);
			backgroundMusic.start();
		}
		ft.replace(R.id.header, header);
		ft.replace(R.id.footer, footer);
		ft.replace(R.id.hub, townHub).commit();
	}
	
	/**
	 * Might not be needed anymore with new refactoring
	 * Selects the new route to run towards
	 * @param newCity - the city that the user is trying to run to.
	 */
/*	public static void moveCity(int newCity) {
		//@TODO check if you need to subtract 1
		
		// updates the city
		player.city = newCity;
		setCurrentCity(refCities.get(newCity - 1));
		Log.d("newCity", refCities.get(newCity - 1).cityName);
		int resId = context.getResources().getIdentifier("background" + (newCity - 1), "drawable", context.getPackageName());
		if (resId != 0)
			hubContentContainer.setBackgroundResource(resId);
		backToCity();
	}*/
	
	public static void backToCity2() {
		DBManager db = new DBManager(context);
		getPlayerData(db);
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
	public static void equipSticker(int position, Monster sticker) {
		// is this needed?
		currentStickerPosition = position;
		currentSticker = sticker;
		FragmentTransaction ft = setFT();
		EquipSticker equip = new EquipSticker();
		ft.replace(R.id.hub, equip).commit();
	}
	
	public static void cityHub() {
		sp.play(soundIds[0], 1, 1, 1, 0, 1);
		FragmentTransaction ft = setFT();
		CityHub townHub = new CityHub();
		ft.replace(R.id.hub, townHub).commit();
	}
	
	public static void selectDungeons() {
		FragmentTransaction ft = setFT();
		CityDungeon townDungeon = new CityDungeon();
		ft.replace(R.id.hub, townDungeon).commit();
	}
	
	public static void selectRoute() {
		FragmentTransaction ft = setFT();
		CityRoute townRoute = new CityRoute();
		ft.replace(R.id.hub, townRoute).commit();
	}
	
	public static void startDungeonRun(Dungeon dungeon) {
		// to be filled
		backgroundMusic.release();
		FragmentTransaction ft = setFT();
		DungeonRun dunRun = new DungeonRun();
		currentDungeon = dungeon;
		enemyList = util.Parser.enemyDungeonStickersToEnemyMonsters(refMonsters, refDungeonMonsters.get(dungeon.dungeonId));
		ft.remove(header);
		ft.remove(footer);
		ft.replace(R.id.hub, dunRun).commit();
	}
	
	public static void startRouteRun(Route route) {
		// to be filledetFT();
		backgroundMusic.release();
		FragmentTransaction ft = setFT();
		sp.play(soundIds[1], 1, 1, 1, 0, 1);
		RouteRun cityRun = new RouteRun();
				currentRoute = route;
		enemyList = util.Parser.enemyRouteStickersToEnemyMonsters(refMonsters, refRouteMonsters.get(route.monsterRouteId));
		ft.remove(header);
		ft.remove(footer);
		ft.replace(R.id.hub, cityRun).commit();

	}
	
	public static void startRouteRun2(Route route) {
		currentRoute = route;
		enemyList = util.Parser.enemyRouteStickersToEnemyMonsters(refMonsters, refRouteMonsters.get(route.monsterRouteId));
		Intent intent = new Intent(context, RouteRun.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	 
	public static int partySize() {
		int count = 0;
		for (Monster monster : equippedStickers) {
			if (monster != null) {
				count++;
			}
		}
		return count;
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

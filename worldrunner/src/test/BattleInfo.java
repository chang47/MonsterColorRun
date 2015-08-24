package test;

import java.util.ArrayList;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;

import test.TestStepService.StepBinder;
import DB.DBManager;
import DB.Model.BattleMonster;
import DB.Model.Monster;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class BattleInfo {

	Intent intent;
	
    public static int exp = 0;
    
    // list of stickers that were found, temporarily changed to be a list
    // of monsters
    public static ArrayList<Monster> found;
    
    private long startTime;
   
    // calculate running metrics
    public static int steps;
    public static long countUp;
    public static double distance;
    public static int coins;
    public static int iPartyAttacked;
    
    // shouldn't have since the DB technically should only be accessed via
    // the controller (Hub), but we'll just have it here anyways for now
    private DBManager db;
    
    //For logging purposes
	public static ArrayList<String> list = new ArrayList<String>();

    
    // list of messages that are used to display the progress
    // made in the game. You just add it into an adapter and it'll do everything for you
    //private List<String> list; 
    private ArrayAdapter<String> adapter;
    private Handler mHandler = new Handler();
    
    //Sets the list of monsters for various purposes
    public static ArrayList<Monster> monsterList;
    public static ArrayList<Monster> partyList;
    public static ArrayList<BattleMonster> partyMonsterBattleList;
    public static ArrayList<BattleMonster> enemyMonsterBattleList;
    public static ArrayList<ProgressBar> enemyProgressBarList;
    public static ProgressBar[] playerProgressBarList;
    
    //Shows the player's log
    public static Dialog showLog;
    
    public static int partyMonstersSize;

    // Player
    public static int deadPartyMonsters;
    // Enemy
    public static int deadEnemies;
    
    //Sets amount of enemies
	public static int enemyPartySize;
	
	//Sets how many monsters are needed to be beaten
	public static int monsterPartiesNeeded = 1;
	
	//Sets if the finish button can be used
	public static boolean finishEnabled = false;
	public static boolean caughtAlready = false;
	
	public static boolean battleStarted = false;
	
	/**
	 * Should only be called at the start of a battle and never again 
	 * anywhere else
	 */
	public static void startCombat() {
		if (!battleStarted) {
			// ensures that method can't be called again
			battleStarted = true;
			
			// initialize fields
			partyList = Hub.equippedStickers;
	        partyMonsterBattleList = new ArrayList<BattleMonster>();
	        enemyMonsterBattleList = new ArrayList<BattleMonster>();
	        // TODO what's the best way to add the stickers in?
	        /*enemyProgressBarList = new ArrayList<ProgressBar>();
	        playerProgressBarList = new ProgressBar[5];*/
	        deadPartyMonsters = 0;
	        deadEnemies = 0;
	        enemyPartySize = 0;
	        partyMonstersSize = 0;
	        steps = 0;
	        distance = 0;
	        coins = 0;
			countUp = 0;
			found = new ArrayList<Monster>();
			
	        generateEnemies();
	        generateParty();

		}
	} 
	
	public static void generateEnemies() {
		BackgroundChecker.newEnemies = true;
    	caughtAlready = false;
    	deadEnemies = 0;
    	enemyPartySize = (int) ((Math.random() * 3.0) + 1);
    	list.add("new enemy party with " + enemyPartySize + " monsters");
		enemyProgressBarList.clear();
		enemyMonsterBattleList.clear();
		
		//Creates the monsters and adds the UI elements for them
		for (int i = 0; i < enemyPartySize; i++) {
    		int monsterGen = (int) (Math.random() * Hub.enemyList.size());
    		
    		enemyMonsterBattleList.add(new BattleMonster(Hub.enemyList.get(monsterGen)));
    		Log.d("enemy health", "current: " + enemyMonsterBattleList.get(i).currentHp + " max: " +
    				enemyMonsterBattleList.get(i).hp);
    		
		}
    }
	
	 /**
     * Generates the player's party
     */
    private static void generateParty() {
    	for (int i = 0; i < partyList.size(); i++) {
			
    		if (partyList.get(i) == null) {
    			partyMonsterBattleList.add(null);
    			playerProgressBarList[i] = null;
    		} else {
    			// setup real monsters, only creates progress bar if real monster exists
        		ProgressBar progBar = new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleHorizontal);
        		progBar.setId((i + 1) * 101);
        		progBar.setProgress(100);
        		txt.setText("monster");
        		
        		int resId = getResources().getIdentifier("head" + partyList.get(i).monsterId, "drawable", getActivity().getPackageName());
        		Log.d("imageId", partyList.get(i).name + " id is: " + partyList.get(i).monsterId + " id got was: " + resId);
        		if (resId != 0) {
        			imgView.setBackgroundResource(resId);;
        		} else {
        			imgView.setBackgroundResource(R.drawable.ic_launcher);
        		}
        		progBar.setLayoutParams(relLayoutParamProg);
        		
        		// sets the progress bar
        		relLayoutParamProg.addRule(RelativeLayout.BELOW, (i + 1) * 11);
    			
        		relLayout.addView(progBar);
        		playerProgressBarList[i] = progBar;
        		Log.d("size", "size of list is" + enemyProgressBarList.size());
    			partyMonstersSize++;
    			// TODO Quick hack, but needs to be fixed properly.
    			//partyMonsterBattleList.add(new BattleMonster(partyList.get(i), partyList.get(i).hp, 1000 / partyList.get(i).speed));
    			//partyMonsterBattleList.add(new BattleMonster(partyList.get(i)));
    			partyMonsterBattleList.add(new BattleMonster(partyList.get(i), true));
    			Log.d("party health", "current: " + partyMonsterBattleList.get(i).currentHp + " max: " +
        				partyMonsterBattleList.get(i).hp);
        	
    		}
    		
    		playerPartyLayout.addView(relLayout);
    	}
    }
}

package step.detector;

import java.util.ArrayList;

import step.detector.StepListener;
import battleHelper.BackgroundChecker;
import battleHelper.BattleInfo;
import DB.DBManager;
import DB.Model.BattleMonster;
import DB.Model.Monster;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;;

public class StepService extends Service implements SensorEventListener, StepListener {
	public static final String PREF_NAME = "RouteRunPref";
	public static final String EXP = "exp";
	public static final String FOUND = "found";
	public static final String STEPS = "steps";
	public static final String COINS = "coins";
	public static final String DISTANCE = "distance";
	public static final String BATTLESTEPS = "battlesteps";
	public static final String LIST = "list";
	public static final String PLAYER_MONSTER = "playerMonsters";
	public static final String ENEMY_MONSTER = "enemyMonsters";
	public static final String ENEMY_BTATLEMONSTER = "enemyBattleMonsters";
	public static final String PLAYER_BATTLEMONSTER = "playerBattleMonsters";
	public static final String DEAD_PLAYER_MONSTER = "deadPlayerMonster";
	public static final String DEAD_ENEMIES = "deadEnemies";
	public static final String PARTY_MONSTERS_SIZE = "partyMonsterSize";
	public static final String ENEMY_PARTY_SIZE = "enemyPartySize";
	public static final String OBJECTIVE = "objective";
	public static final String FINISHED = "finished";
	public static final String CAUGHT = "caught";
	
	private SimpleStepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    public static final String BROADCAST_ACTION = "joshchangreal";
    
    public static int iPartyAttacked;
	private final IBinder mBinder = new StepBinder();
	Intent intent;
	
	int mId;
    
	// Creates a binder that gives access to Test Step Service that we can
	// access anywhere from the acitivities
	public class StepBinder extends Binder {
		public StepService getService() {
			return StepService.this;
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mId = startId;
		return super.onStartCommand(intent, flags, startId);
	}
	
	// Sets up everything when created
	@Override
	public void onCreate() {
		// init values
        intent = new Intent(BROADCAST_ACTION);        
        Log.d("disconnect", "on service created inside step service");
		// start detecting steps
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new SimpleStepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	// TODO Might be good to have in the future to save all of your monsters in
	// a shared pref to access for the results and maybe it's also important
	// to get rid of the sensor manager
	@Override
	public void onDestroy() {
		Log.d("disconnect", "step service closing");
		stopSelf(mId);
		BackgroundChecker.endService = true;
		// need to un-register otherwise service will continue to run forever
		sensorManager.unregisterListener(this);
		simpleStepDetector = null;
	    sensorManager = null;
	    accel = null;
	}
	
	// Gives access to other services so that they can access
	// the methods in the service
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	// used to detect a step
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			simpleStepDetector.updateAccel(
					event.timestamp, event.values[0], event.values[1], event.values[2]);
	    }
	}
	
	// not used
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Calls everytime a step is detected. This is where all the battle logic goes
	 */
	@Override
	public void step(long timeNs) {
		try {
			// for testing multiple steps only
			//stepCopy();
			if (Math.random() < 0.5) {
	            BattleInfo.coins++;
	        }
			BattleInfo.battleSteps++;
	    	BattleInfo.steps++;
	    	BattleInfo.distance = (BattleInfo.steps * .91) / 1000;
	    	
	    	// prevents the possibility of running the code in an unexpected state
			//if (BackgroundChecker.finishedCurrentBattle) {
		        // monster turn
		        BattleInfo.enemyTurn();
		        Log.d("disconnect", "inside step " + BattleInfo.steps);
		        // stops monsters from attacking, resets their steps (except abilities)
		        // when monsters are dead and if the screen is on updates screen
		        if (BackgroundChecker.finishedCurrentBattle) {
		        	saveData();
		        	BackgroundChecker.finishedCurrentBattle = false;
		        	if (!BackgroundChecker.isBackground) {
		    			sendBroadcast(intent);
		    		}
		        	return;
		        }
		        
		        // user party attacks
		        BattleInfo.playerTurn();
		        if (BackgroundChecker.finishedCurrentBattle) {
		        	saveData();
		        	BackgroundChecker.finishedCurrentBattle = false;
		        	if (!BackgroundChecker.isBackground) {
		    			sendBroadcast(intent);
		    		}
		        	return;
		        }
		        
		        // user party ability
		        BattleInfo.playerAbilityTurn();
		        if (BackgroundChecker.finishedCurrentBattle) {
		        	saveData();
		        	BackgroundChecker.finishedCurrentBattle = false;
		        	if (!BackgroundChecker.isBackground) {
		    			sendBroadcast(intent);
		    		}
		        	return;
		        }
		        
				// sends ui updates to the user when their phones are on
				if (!BackgroundChecker.isBackground) {
					sendBroadcast(intent);
				}
			//}
		} catch (Exception e) {
			// TODO in actual prod code we would just restart everything, chances are, users
			// won't be looking on the screen anyways and something probably went wrong anyways
			if (BattleInfo.partyMonsterBattleList == null) {
	    		Log.d("random crash2", "partyMonsterBattleList is null");
	    		Log.d("random crash2", "finished current battle status: " + BackgroundChecker.finishedCurrentBattle);
	    		Log.d("random crash2", "has the combat started? " + BackgroundChecker.battleStarted);
	    		Log.d("random crash2", "was the monster attacked? " + BackgroundChecker.monsterWasAttacked);
	    		Log.d("random crash2", "was the player monster attacked? " + BackgroundChecker.playerMonsterWasAttacked);
	    		Log.d("random crash2", "was in the background? " + BackgroundChecker.isBackground);
	    		Log.d("random crash2", "Are there now new enemies? " + BackgroundChecker.newEnemies);
	    		if (BattleInfo.partyList == null) {
	    			Log.d("random crash", "partyList is null");
	    		} else {
	    			Log.d("random crash", "partyList is not null");	
	    		}
	    	}
			Log.e("MonsterColorRun", e.getClass().getName(), e);
			recoverFromLastPoint();
			return;
    		//e.printStackTrace();
    	}
	}
	
	public long getTime() {
		return BattleInfo.countUp;
	}
	
	public int getStep() {
		return BattleInfo.steps;
	}
	
	/**
	 * Saves the data after every battle so that it can be
	 * reloaded in the event the android life cycle decides
	 * to wipe all memory
	 */
	public void saveData() {
		Log.d("crash data", "data is being saved in case of crash");
		Gson gson = new Gson();
		SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
		editor.putInt(EXP, BattleInfo.exp);
		editor.putString(FOUND, gson.toJson(BattleInfo.found));
		editor.putInt(STEPS, BattleInfo.steps);
		editor.putInt(COINS, BattleInfo.coins);
		editor.putLong(DISTANCE, (long) BattleInfo.distance);
		editor.putInt(BATTLESTEPS, 0); // should be 0, because we just finished a battleeditor.putInt(, BattleInfo);
		editor.putString(LIST, gson.toJson(BattleInfo.list));
		editor.putString(PLAYER_MONSTER, gson.toJson(BattleInfo.partyList)); // probably only needs to be saved once in the beginning
		editor.putString(ENEMY_MONSTER, gson.toJson(BattleInfo.monsterList)); // probably only needs to be saved once in the beginning
		editor.putString(ENEMY_BTATLEMONSTER, gson.toJson(BattleInfo.enemyMonsterBattleList));
		editor.putString(PLAYER_BATTLEMONSTER, gson.toJson(BattleInfo.partyMonsterBattleList));
		editor.putInt(DEAD_PLAYER_MONSTER, BattleInfo.deadPartyMonsters);
		editor.putInt(DEAD_ENEMIES, 0); // because we just killed them all might not even need to retrieve
		editor.putInt(PARTY_MONSTERS_SIZE, BattleInfo.partyMonstersSize);
		editor.putInt(ENEMY_PARTY_SIZE, BattleInfo.enemyPartySize);
		editor.putInt(OBJECTIVE, BattleInfo.fightObjective);
		editor.putBoolean(FINISHED, BattleInfo.finishEnabled);
		editor.putBoolean(CAUGHT, false); // since we're starting a new round

		editor.commit();// apply might seem better since it allows everything to be asynchronous so only need to be used once
						// in the beginning, but might also save the null.
	}
	
	// TODO restart everything in the event that nothing was saved.
	// will probably have problems on the first load if there is a problem there
	@SuppressWarnings("unchecked")
	public void recoverFromLastPoint() {
		Gson gson = new Gson();
		SharedPreferences pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		BattleInfo.exp = pref.getInt(EXP, 0);
		BattleInfo.found = gson.fromJson(pref.getString(FOUND, null), ArrayList.class);
		BattleInfo.coins = pref.getInt(COINS, 0);
		BattleInfo.distance = pref.getLong(DISTANCE, 0);
		BattleInfo.list = gson.fromJson(pref.getString(LIST, null), ArrayList.class);
		BattleInfo.partyList = gson.fromJson(pref.getString(PLAYER_MONSTER, null), ArrayList.class);
		BattleInfo.monsterList = gson.fromJson(pref.getString(ENEMY_MONSTER, null), ArrayList.class);
		BattleInfo.enemyMonsterBattleList = gson.fromJson(pref.getString(ENEMY_BTATLEMONSTER, null), ArrayList.class);
		BattleInfo.partyMonsterBattleList = gson.fromJson(pref.getString(PLAYER_BATTLEMONSTER, null), ArrayList.class);
		BattleInfo.partyList = gson.fromJson(pref.getString(PLAYER_MONSTER, null), ArrayList.class);
		BattleInfo.deadPartyMonsters = pref.getInt(DEAD_PLAYER_MONSTER, 0);
		BattleInfo.deadEnemies = 0; // sanitary reasons
		BattleInfo.partyMonstersSize = pref.getInt(PARTY_MONSTERS_SIZE, 0); 
		BattleInfo.enemyPartySize = pref.getInt(ENEMY_PARTY_SIZE, 0); // will most likely crash if problems here
		BattleInfo.fightObjective = pref.getInt(OBJECTIVE, 0);
		BattleInfo.finishEnabled = pref.getBoolean(FINISHED, false);
		BattleInfo.caughtAlready = false;
	}
	
	/**
	 * Only to be used to test and speed up battles
	 */
	public void stepCopy() {
		for (int i = 0; i < 10000; i++) {
			if (Math.random() < 0.5) {
	            BattleInfo.coins++;
	        }
			BattleInfo.battleSteps++;
	    	BattleInfo.steps++;
	    	BattleInfo.distance = (BattleInfo.steps * .91) / 1000;
	        
	        // monster turn
	        BattleInfo.enemyTurn();
	        
	        // stops monsters from attacking, resets their steps (except abilities)
	        // when monsters are dead and if the screen is on updates screen
	        if (BackgroundChecker.finishedCurrentBattle) {
	        	BackgroundChecker.finishedCurrentBattle = false;
	        	if (!BackgroundChecker.isBackground) {
	    			sendBroadcast(intent);
	    		}
	        	//return;
	        }
	        
	        // user party attacks
	        BattleInfo.playerTurn();
	        if (BackgroundChecker.finishedCurrentBattle) {
	        	BackgroundChecker.finishedCurrentBattle = false;
	        	if (!BackgroundChecker.isBackground) {
	    			sendBroadcast(intent);
	    		}
	        	//return;
	        }
	        
	        // user party ability
	        BattleInfo.playerAbilityTurn();
	        if (BackgroundChecker.finishedCurrentBattle) {
	        	BackgroundChecker.finishedCurrentBattle = false;
	        	if (!BackgroundChecker.isBackground) {
	    			sendBroadcast(intent);
	    		}
	        	//return;
	        }
	        
			// sends ui updates to the user when their phones are on
			/*if (!BackgroundChecker.isBackground) {
				sendBroadcast(intent);
			}*/
		}
	}	
	
	@Override
	public boolean onUnbind(Intent intent) {
		super.onUnbind(intent);
		stopSelf();
		
		Log.d("disconnect", "service called on unbind");
		return true;
	}
	
	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		Log.d("disconnect", "service called re-bind");
	}
}

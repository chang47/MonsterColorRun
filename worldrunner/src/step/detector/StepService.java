package step.detector;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.lang.reflect.Type;

import util.*;
import step.detector.StepListener;
import battleHelper.BackgroundChecker;
import battleHelper.BattleInfo;
import Abilities.Ability;
import DB.Model.BattleMonster;
import DB.Model.Monster;
import Races.RouteRun;
import android.R;
import android.app.Notification;
import android.app.PendingIntent;
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
//TODO import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.brnleehng.worldrunner.Hub;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class StepService extends Service implements SensorEventListener, StepListener {
	public static final String PREF_NAME = "RouteRunPref"; // should be all refactored to monster color run 
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
	public static final String CURRENT_STEP = "currentStep";
	public static final String CURRENT_TIME = "currentTime";
	public static final String CURRENT_CALORIES = "currentCalories";	
	public static final String IS_RUNNING = "isRunning";	
	
	private SimpleStepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    public static final String BROADCAST_ACTION = "joshchangreal";
    
    public static int iPartyAttacked;
	private final IBinder mBinder = new StepBinder();
	Intent intent;
	
	int mId;
    
	private SharedPreferences pref;
	private Calendar now;
	
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
		Intent notif = new Intent(getApplicationContext(), Hub.class);
		// 1 = dungeon run, 2 = route run, anything else = nothing
		intent.putExtra("runmode", 1);
        PendingIntent pend = PendingIntent.getActivity(this, 0, notif, /*PendingIntent.FLAG_UPDATE_CURRENT*/ Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Notification notif2 = new Notification.Builder(this)
        	.setSmallIcon(com.brnleehng.worldrunner.R.drawable.icon)
        	.setContentTitle("Monster Color Run")
        	.setContentText("Running in " + BackgroundChecker.locationName)
        	.setContentIntent(pend).build();
        startForeground(1337, notif2);
		// this might also be the place to start the foreground service
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
        
        // Starts the run so we know what to do when resuming
		pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		Log.d("recover", "status: " + pref.getBoolean(IS_RUNNING, false));
        if (pref.getBoolean(IS_RUNNING, false)) {
        	recoverFromLastPoint();
        	//TODO might need to do something to update the existing monster health
        	sendBroadcast(intent);
        } else {
        	getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putBoolean(IS_RUNNING, true).apply();
        }
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
        getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit().putBoolean(IS_RUNNING, false).apply();
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
			now = Calendar.getInstance();
			
			int step = pref.getInt("" + now.get(Calendar.HOUR_OF_DAY), 0);
			pref.edit().putInt("" + now.get(Calendar.HOUR_OF_DAY), ++step).apply();
			Log.d("a1s2", "lol");
			Log.d("steps1", "" + step);
			if (Math.random() < 0.5) {
	            BattleInfo.coins += 10;
	        }
			
			BattleInfo.battleSteps++;
	    	BattleInfo.steps++;
	    	Log.d("recover", "post steps " + BattleInfo.steps);
	    	long newTime = System.currentTimeMillis();
	    	Log.d("time", "" + newTime);
	    	if ((newTime - BattleInfo.currentTime) / 5000 >= 1) {
	    		// calculate the steps and their distance
	    		BattleInfo.currentTime = newTime;
	    		int stepsTaken = BattleInfo.steps - BattleInfo.currentStep;
	    		BattleInfo.currentStep = BattleInfo.steps;
	    		// assumption:
	    		// Fact 1: average speed between running and walking is 4.5 MPH, which translates to 6.6 feet per second
	    		// Fact 2: An average walking step is 2.2 feet (mine anyways)
	    		// Conclusion: 3 steps will give 6.6 feet per second which is fast walk/running speed
	    		// calorie = pounds / 2.2 * MET * 5 seconds / 60 seconds / 60 minutes /
	    		// MET 3 for walking, 6 for running, good enough!
	    		double met = 0;
	    		double distanceTraveled = 0;
	    		// formula for calories: CALORIES/MIN	
	    		// walk: .03 x wt in lbs	
	    		// run: .07 x wt in lbs
	    		if (stepsTaken >= 15) {
	    			// running pace is 3 feet per step
	    			met = 9;
	    			distanceTraveled = (double) stepsTaken * (3.0 / 5280);
	    			// TODO test
	    			BattleInfo.distance += distanceTraveled * 1000;
	    			// TODO test
	    			BattleInfo.calories += 0.72 * Hub.weight * distanceTraveled * 1000; 
	    		} else {
	    			// running pace is 2.2 feet per step
	    			met = 3.5;
	    			distanceTraveled = (double) stepsTaken * (3.0 / 5280);
	    			BattleInfo.distance += distanceTraveled;	
	    			BattleInfo.calories += 0.57 * Hub.weight * distanceTraveled; 
	    		}
	    		//BattleInfo.calories += Hub.weight / 2.2 * met * 5 / 60 / 60;
	    	}
	    	
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
			Log.d("clutter crash", "step service");
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
		Log.d("crash data", "the steps are" + BattleInfo.steps);
		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Ability.class, new InterfaceAdapter<Ability>());
		Gson gson = builder.create();
		SharedPreferences.Editor editor = getSharedPreferences(PREF_NAME, MODE_PRIVATE).edit();
		editor.putInt(EXP, BattleInfo.exp);
		editor.putString(FOUND, gson.toJson(BattleInfo.found));
		editor.putInt(STEPS, BattleInfo.steps);
		editor.putInt(COINS, BattleInfo.coins);
		editor.putLong(DISTANCE, (long) (BattleInfo.distance * 100));
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
		editor.putInt(CURRENT_STEP, BattleInfo.currentStep);
		editor.putLong(CURRENT_TIME, BattleInfo.currentTime);
		editor.putLong(CURRENT_CALORIES, (long) (BattleInfo.calories * 100));

		editor.commit();// apply might seem better since it allows everything to be asynchronous so only need to be used once
						// in the beginning, but might also save the null.
	}
	
	// TODO restart everything in the event that nothing was saved.
	// will probably have problems on the first load if there is a problem there
	@SuppressWarnings("unchecked")
	public void recoverFromLastPoint() {
		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Ability.class, new InterfaceAdapter<Ability>());
		Gson gson = builder.create();
		Type battleMonsterType = new TypeToken<List<BattleMonster>>(){}.getType();
		Type monsterType = new TypeToken<List<Monster>>(){}.getType();
		pref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
		BattleInfo.exp = pref.getInt(EXP, 0);
		BattleInfo.found = gson.fromJson(pref.getString(FOUND, null), monsterType);
		BattleInfo.coins = pref.getInt(COINS, 0);
		BattleInfo.distance = (double) pref.getLong(DISTANCE, 0) / 100.0;
		BattleInfo.list = gson.fromJson(pref.getString(LIST, null), ArrayList.class);
		BattleInfo.partyList = gson.fromJson(pref.getString(PLAYER_MONSTER, null), monsterType);
		BattleInfo.monsterList = gson.fromJson(pref.getString(ENEMY_MONSTER, null), monsterType);
		BattleInfo.enemyMonsterBattleList = gson.fromJson(pref.getString(ENEMY_BTATLEMONSTER, null), battleMonsterType);
		BattleInfo.partyMonsterBattleList = gson.fromJson(pref.getString(PLAYER_BATTLEMONSTER, null), battleMonsterType);
		BattleInfo.partyList = gson.fromJson(pref.getString(PLAYER_MONSTER, null), monsterType);
		BattleInfo.deadPartyMonsters = pref.getInt(DEAD_PLAYER_MONSTER, 0);
		BattleInfo.deadEnemies = 0; // sanitary reasons
		BattleInfo.partyMonstersSize = pref.getInt(PARTY_MONSTERS_SIZE, 0); 
		BattleInfo.enemyPartySize = pref.getInt(ENEMY_PARTY_SIZE, 0); // will most likely crash if problems here
		BattleInfo.fightObjective = pref.getInt(OBJECTIVE, 0);
		BattleInfo.finishEnabled = pref.getBoolean(FINISHED, false);
		BattleInfo.caughtAlready = false;
		BattleInfo.currentStep = pref.getInt(STEPS, 0);//pref.getInt(CURRENT_STEP, 0);
		BattleInfo.steps = pref.getInt(STEPS, 0);
		BattleInfo.currentTime = pref.getLong(CURRENT_TIME, 0);
		BattleInfo.calories = (double) pref.getLong (CURRENT_CALORIES, 0) / 100.0;
		Log.d("recover", "calories " + BattleInfo.calories);
		Log.d("recover", "current time " + BattleInfo.currentTime);
		Log.d("recover", "current step " + BattleInfo.currentStep);
		Log.d("recover", "exp " + BattleInfo.exp);
		Log.d("recover", "coin" + BattleInfo.coins);
		Log.d("recover", "distance " + BattleInfo.distance);
		Log.d("recover", "real current time" + pref.getLong(CURRENT_TIME, 0));
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

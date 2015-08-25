package test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import util.BattleHelper;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;
import com.brnleehng.worldrunner.StepDetector.SimpleStepDetector;
import com.brnleehng.worldrunner.StepDetector.StepListener;

import Abilities.Buff;
import Abilities.DamageAbility;
import Abilities.SupportAbility;
import DB.DBManager;
import DB.Model.BattleMonster;
import DB.Model.Monster;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.LinearLayout.LayoutParams;

public class TestStepService extends Service implements SensorEventListener, StepListener {
	private TestSimpleStepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    public static final String BROADCAST_ACTION = "joshchang";
    
    public static int iPartyAttacked;
	private final IBinder mBinder = new StepBinder();
	Intent intent;
    
	// Creates a binder that gives access to Test Step Service that we can
	// access anywhere from the acitivities
	public class StepBinder extends Binder {
		public TestStepService getService() {
			return TestStepService.this;
		}
	}
	
	// Sets up everything when created
	@Override
	public void onCreate() {
		// init values
        intent = new Intent(BROADCAST_ACTION);        
        
		// start detecting steps
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new TestSimpleStepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	// TODO Might be good to have in the future to save all of your monsters in
	// a shared pref to access for the results and maybe it's also important
	// to get rid of the sensor manager
	@Override
	public void onDestroy() {
		Toast.makeText(this, "stopped", Toast.LENGTH_SHORT).show();
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

	@Override
	public void step(long timeNs) {
		if (Math.random() < 0.5) {
            BattleInfo.coins++;
        }
    	BattleInfo.steps++;
    	BattleInfo.distance = (BattleInfo.steps * .91) / 1000;
        
        // monster turn
        for (int i = 0; i < BattleInfo.enemyPartySize; i++) {
        	//Attacks Regularly
        	if (BattleInfo.enemyMonsterBattleList.get(i).currentHp > 0) {
		        if (BattleInfo.steps % BattleInfo.enemyMonsterBattleList.get(i).step == 0) {
		        	iPartyAttacked = BattleHelper.AIAttack(BattleInfo.enemyMonsterBattleList.get(i), BattleInfo.partyMonsterBattleList);

		        	if (iPartyAttacked == -1) {
		        		throw new Error("attacked index is -1, impossible!");
		        	}
		        	BackgroundChecker.playerMonsterWasAttacked = true;
		        	BattleInfo.partyMonsterBattleList.get(iPartyAttacked).currentHp -= BattleHelper.Attack(BattleInfo.enemyMonsterBattleList.get(i), 
		        			BattleInfo.partyMonsterBattleList.get(iPartyAttacked));
		        	// TODO remove
		        	if (BattleInfo.list.size() < 100) 
		        		BattleInfo.list.add("Enemy " + BattleInfo.enemyMonsterBattleList.get(i).monster.name + " Attacks " + 
		            BattleInfo.partyMonsterBattleList.get(iPartyAttacked).monster.name + " For " + 
		            BattleHelper.Attack(BattleInfo.enemyMonsterBattleList.get(i), BattleInfo.partyMonsterBattleList.get(iPartyAttacked)));
		
					if (BattleInfo.partyMonsterBattleList.get(iPartyAttacked).currentHp <= 0) {
						BattleInfo.deadPartyMonsters++;
						if (BattleInfo.deadPartyMonsters >= BattleInfo.partyMonstersSize) {
							//Entire Party is dead, resurrect them and change monsters
							reviveParty(BattleInfo.partyMonsterBattleList.size());
							BattleInfo.generateEnemies();
							return;
						}
					}
		        }
        	}
        }
        
        // user party attacks
        for (int i = 0; i < BattleInfo.partyMonsterBattleList.size(); i++) {
        	if (BattleInfo.partyMonsterBattleList.get(i) != null && BattleInfo.partyMonsterBattleList.get(i).currentHp > 0) {
	        	if (BattleInfo.steps % BattleInfo.partyMonsterBattleList.get(i).step == 0) {
	        		BackgroundChecker.monsterWasAttacked = true;
	        		int iEnemyAttacked = BattleHelper.AIAttack(BattleInfo.partyMonsterBattleList.get(i), BattleInfo.enemyMonsterBattleList);
	        		
	        		//Log.d("Speed","Current step speed for " + partyMonsterBattleList.get(i).monster.name + " is " + partyMonsterBattleList.get(i).step);
	        		//Log.d("index problems", "" + iEnemyAttacked);
	        		double damage = BattleHelper.Attack(BattleInfo.partyMonsterBattleList.get(i), BattleInfo.enemyMonsterBattleList.get(iEnemyAttacked));
	        		BattleInfo.enemyMonsterBattleList.get(iEnemyAttacked).currentHp -= damage;
	        		// TODO remove
	        		if (BattleInfo.list.size() < 10)
	        			BattleInfo.list.add(BattleInfo.partyMonsterBattleList.get(i).monster.name + " Attacks " + BattleInfo.enemyMonsterBattleList.get(iEnemyAttacked).monster.name + " For " + damage + "!");
	        		checkEnemyDead(iEnemyAttacked);
	        		
	        		Iterator iterator = BattleInfo.partyMonsterBattleList.get(i).buffs.entrySet().iterator();
	        		// decrease buff of monsters
	        		while (iterator.hasNext()) {
	        			Map.Entry<Integer, Buff> pair = (Entry<Integer, Buff>) iterator.next();
	        			int attribute = pair.getKey();
	        			Buff buff = pair.getValue();
	        			//Log.d("buff with duration at: ", "" + buff.duration);
	        			buff.duration--;
	        			Log.d("duration", "" + BattleInfo.partyMonsterBattleList.get(i).monster.name + " buff " + buff.name + " has duration " + buff.duration);
	        			//partyBattleList.get(i).buffs.get(iterator).duration--;
	        			//Check if above code actually decreases
	        			if (buff.duration <= 0) {
	        				//Log.d("removed attribute", "" + iterator);
	        				// partyBattleList.get(b).buffs.get(3).duration
	        				iterator.remove();
	        				
	        				// important to be after, becauase recalculate checks for the attribute key
	        				if (attribute == 3) {
	        					BattleInfo.partyMonsterBattleList.get(i).RecalculateSpeed();
	        	        		//Log.d("Speed","New Speed Calculated (Buff Removed): " + partyMonsterBattleList.get(i).currentStep);
	        				}
	        				//partyBattleList.get(i).buffs.remove(iterator);
	        			}
	        		}
	        	}
        	
	        	
	        	// check monster status
	        	//Log.d("buff size", ""+ partyBattleList.get(0).buffs.size());
	        	
	        	// checks for user's party's ability
	        	if (BattleInfo.steps % BattleInfo.partyMonsterBattleList.get(i).monster.activeAbility.steps == 0) { 
	        		//Applies ability to attack enemy
	        		if (BattleInfo.partyMonsterBattleList.get(i).monster.activeAbility.getClass() == DamageAbility.class) {
	        			BackgroundChecker.monsterWasAttacked = true;
		        		int iEnemyAttack = BattleHelper.AIAttack(BattleInfo.partyMonsterBattleList.get(i), BattleInfo.enemyMonsterBattleList);
		        		DamageAbility dAbility = (DamageAbility) BattleInfo.partyMonsterBattleList.get(i).monster.activeAbility;
	        			double damage = dAbility.damage * BattleInfo.partyMonsterBattleList.get(i).monster.attack;
	        			BattleInfo.enemyMonsterBattleList.get(iEnemyAttack).currentHp -= damage;
//	            		list.add(partyMonsterBattleList.get(i).monster.name + " Used Ability " +  partyMonsterBattleList.get(i).monster.ability.name + 
//	            				" For " + damage + "!");
	            		
	            		//Checks if all enemies are dead 
	        			checkEnemyDead(iEnemyAttack);

	        		} else if (BattleInfo.partyMonsterBattleList.get(i).monster.activeAbility.getClass() == SupportAbility.class) {
	        			//Applies party buffs
	        			SupportAbility support = (SupportAbility)BattleInfo.partyMonsterBattleList.get(i).monster.activeAbility;
	        	        for (int b = 0; b < BattleInfo.partyMonsterBattleList.size(); b++) {
	        	        	if (BattleInfo.partyMonsterBattleList.get(b) != null) {
		        	        	Buff newBuff = new Buff(support.name, support.description, support.duration, support.attribute, support.modifier);
		        	        	BattleInfo.partyMonsterBattleList.get(b).buffs.put(support.attribute, newBuff);           	        
		        	        	if (support.attribute == 3) {
		        	        		BattleInfo.partyMonsterBattleList.get(b).RecalculateSpeed();
		        	        		Log.d("Speed","New Speed Calculated for : " + BattleInfo.partyMonsterBattleList.get(b).monster.name + " is " + BattleInfo.partyMonsterBattleList.get(b).step + " duration is: " + BattleInfo.partyMonsterBattleList.get(b).buffs.get(3).duration);
		            	        }
	        	        	}
	        	        }
//	            		list.add(partyMonsterBattleList.get(i).monster.name + " Used Ability " +  partyMonsterBattleList.get(i).monster.ability.name + "!");
	        		}
	        	}
        	}
        }
		
		// sends ui updates to the user when their phones are on
		if (!BackgroundChecker.isBackground) {
			sendBroadcast(intent);
		}
	}
	
	public long getTime() {
		return BattleInfo.countUp;
	}
	
	public int getStep() {
		return BattleInfo.steps;
	}
	
	// The function to create a new random monster
    // use your list of monster to generate a new monster to fight
	
	private void reviveParty(int size) {
		BattleInfo.deadPartyMonsters = 0;
		// TODO remove
		if (BattleInfo.list.size() < 100)
			BattleInfo.list.add("Your party was wiped");
	    for (int i = 0; i < size; i++) {
	    if (BattleInfo.partyMonsterBattleList.get(i) != null)
	    	BattleInfo.partyMonsterBattleList.get(i).resetHp();
	    }
	}
	
    private void checkEnemyDead(int iPartyAttack) {
    	Log.d("into check", "checking if " + BattleInfo.enemyMonsterBattleList.get(iPartyAttack).monster.name + " is dead");
		if (BattleInfo.enemyMonsterBattleList.get(iPartyAttack).currentHp <= 0) {
			Log.d("dead check", BattleInfo.enemyMonsterBattleList.get(iPartyAttack).monster.name + " is dead");
			// TODO add to other
			BattleInfo.exp += BattleInfo.enemyMonsterBattleList.get(iPartyAttack).monster.exp * BattleInfo.enemyMonsterBattleList.get(iPartyAttack).monster.level / 2;
    		BattleInfo.list.add(BattleInfo.enemyMonsterBattleList.get(iPartyAttack).monster.name + " has been defeated!");
    		BattleInfo.deadEnemies++;
    		captureMonster(iPartyAttack);
    		checkEnemyMonsterAllDead();
		}
    }
    
    private void captureMonster(int iPartyAttack) {
    	//if (!caughtAlready && (double) ((Math.random() * 100.0) + 1) > enemyMonsterBattleList.get(iPartyAttack).monster.capture) {
			Log.d("capture monster", "caught " + BattleInfo.enemyMonsterBattleList.get(iPartyAttack).monster.name );
			// TODO remove
			if (BattleInfo.found.size() < 10) {
				if (BattleInfo.list.size() < 10) 
					BattleInfo.list.add(BattleInfo.enemyMonsterBattleList.get(iPartyAttack).monster.name + " has been captured!");
				BattleInfo.found.add(BattleInfo.enemyMonsterBattleList.get(iPartyAttack).monster);
			}
			BattleInfo.caughtAlready = true;
		//}
    }
    
    private void checkEnemyMonsterAllDead() {
    	if (BattleInfo.deadEnemies >= BattleInfo.enemyPartySize) {
    		// TODO remove
    		if (BattleInfo.list.size() < 100)
    			BattleInfo.list.add("Defeated all enemies");
			BattleInfo.monsterPartiesNeeded--;	
			if (BattleInfo.monsterPartiesNeeded <= 0) {
				BattleInfo.finishEnabled = true;
			}
			BattleInfo.generateEnemies();
		}
    }
}

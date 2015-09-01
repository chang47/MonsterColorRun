package step.detector;

import step.detector.StepListener;
import battleHelper.BackgroundChecker;
import battleHelper.BattleInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class StepService extends Service implements SensorEventListener, StepListener {
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
		Toast.makeText(this, "stopped", Toast.LENGTH_SHORT).show();
		BackgroundChecker.endService = true;
		// need to un-register otherwise service will continue to run forever
		sensorManager.unregisterListener(this);
		simpleStepDetector = null;
	    sensorManager = null;
	    accel = null;
		//sendBroadcast(intent);
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
	        
	        // monster turn
	        BattleInfo.enemyTurn();
	        Log.d("disconnect", "inside step " + BattleInfo.steps);
	        // stops monsters from attacking, resets their steps (except abilities)
	        // when monsters are dead and if the screen is on updates screen
	        if (BackgroundChecker.finishedCurrentBattle) {
	        	BackgroundChecker.finishedCurrentBattle = false;
	        	if (!BackgroundChecker.isBackground) {
	    			sendBroadcast(intent);
	    		}
	        	return;
	        }
	        
	        // user party attacks
	        BattleInfo.playerTurn();
	        if (BackgroundChecker.finishedCurrentBattle) {
	        	BackgroundChecker.finishedCurrentBattle = false;
	        	if (!BackgroundChecker.isBackground) {
	    			sendBroadcast(intent);
	    		}
	        	return;
	        }
	        
	        // user party ability
	        BattleInfo.playerAbilityTurn();
	        if (BackgroundChecker.finishedCurrentBattle) {
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
		} catch (Exception e) {
			// TODO in actual prod code we would just restart everything, chances are, users
			// won't be looking on the screen anyways and something probably went wrong anyways
			Log.e("MonsterColorRun", e.getClass().getName(), e);
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

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
		BattleInfo.battleSteps++;
    	BattleInfo.steps++;
    	BattleInfo.distance = (BattleInfo.steps * .91) / 1000;
        
        // monster turn
        BattleInfo.enemyTurn();
        if (BackgroundChecker.finishedCurrentBattle) {
        	BackgroundChecker.finishedCurrentBattle = false;
        	return;
        }
        
        // user party attacks
        BattleInfo.playerTurn();
        if (BackgroundChecker.finishedCurrentBattle) {
        	BackgroundChecker.finishedCurrentBattle = false;
        	return;
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
	
	
}

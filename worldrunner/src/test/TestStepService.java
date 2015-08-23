package test;

import com.brnleehng.worldrunner.R;
import com.brnleehng.worldrunner.StepDetector.SimpleStepDetector;
import com.brnleehng.worldrunner.StepDetector.StepListener;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;
import android.widget.Chronometer.OnChronometerTickListener;

public class TestStepService extends Service implements SensorEventListener, StepListener {
	private TestSimpleStepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    
    // game time values
	public int step;
	private long countUp;
	
	private final IBinder mBinder = new StepBinder();

	// Creates a binder that gives access to Test Step Service that we can
	// access anywhere from the acitivities
	public class StepBinder extends Binder {
		TestStepService getService() {
			return TestStepService.this;
		}
	}
	
	@Override
	public void onCreate() {
		// init values
		step = 0;
		
		// sets the time
        Chronometer stopWatch = new Chronometer(getApplicationContext());
        stopWatch.setOnChronometerTickListener(new OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                countUp = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;        
            }
        });
        stopWatch.start();
        
		
		
		// start detecting steps
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new TestSimpleStepDetector();
        simpleStepDetector.registerListener(this);
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

	@Override
	public void onSensorChanged(SensorEvent event) {
		 if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
	            simpleStepDetector.updateAccel(
	                    event.timestamp, event.values[0], event.values[1], event.values[2]);
	        }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void step(long timeNs) {
		step++;
	}
	
	public long getTime() {
		return countUp;
	}
	
	public int getStep() {
		return step;
	}
	
}

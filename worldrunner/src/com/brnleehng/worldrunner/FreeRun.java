package com.brnleehng.worldrunner;

import DB.DBManager;
import DB.Model.Sticker;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brnleehng.worldrunner.StepDetector.SimpleStepDetector;
import com.brnleehng.worldrunner.StepDetector.StepListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Brian on 3/20/2015.
 */
public class FreeRun extends Fragment implements SensorEventListener, StepListener{
    private SimpleStepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    
    private TextView tvDistance;
    private TextView tvTime;
    private TextView tvPace;
    private TextView tvCoin;
    private ListView listView;
    private TextView monsterName;
    private ProgressBar monsterHealth;
    
    private Button btnStop;
    private ArrayList<Sticker> found;
    
    private long startTime;
    private long countUp;
    
    private int steps;
    private double distance;
    private int coins;
    private double powerStep;
    
    private DBManager db;
    
    private static final double FRIEND1 = 1.1;
    private static final double FRIEND2 = 1.1;
    private static final double FRIEND3 = 1.1;
    private static final double FRIEND4 = 1.1;
    private static final double FRIEND5 = 1.1;
   
    private Sticker monster;
    private double monsterProgress;
    private double currentProgress;
    
    private List<String> list; 
    private ArrayAdapter<String> adapter;
    private Handler mHandler = new Handler();
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.ingame_activity, container, false);
        
        // setup intitial objects
        startTime = SystemClock.elapsedRealtime();
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        tvPace = (TextView) view.findViewById(R.id.tvPage);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        tvCoin = (TextView) view.findViewById(R.id.tvCoin);
        listView = (ListView) view.findViewById(R.id.item_list);
        monsterName = (TextView) view.findViewById(R.id.monsterName);
        monsterHealth = (ProgressBar) view.findViewById(R.id.monsterProgress);
        
        //Hub.partyList
        
        btnStop = (Button) view.findViewById(R.id.stopMission);
        
        // initialize fields
        steps = 0;
        distance = 0;
        coins = 0;
        db = new DBManager(getActivity().getApplicationContext());
        found = new ArrayList<Sticker>();
        powerStep = 5 * FRIEND1 * FRIEND2 * FRIEND3 * FRIEND4 * FRIEND5;

        // Initializes first monster encounter
        generateMonster();
        
        
        btnStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				db.addStickers(found);
				found.clear();
				Hub.result();
			}
		});
        
        list = new ArrayList<String>();
        
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        
        // setup timer
        Chronometer stopWatch = (Chronometer) view.findViewById(R.id.chronometer);
        stopWatch.setOnChronometerTickListener(new OnChronometerTickListener(){
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                countUp = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
                String asText = (countUp / 60) + ":";
                if (countUp % 60 < 10) {
                	asText += "0" + countUp % 60;
                } else {
                	asText += "" + countUp % 60;
                }
                tvTime.setText(asText);
                
                // user passes in the dungeon type that would all inherit the
                // methods with just varied difficulties
                monsterHealth.setProgress((int) (currentProgress / monsterProgress * 100));
                // FreeRun.checkRun(); this class is the different variation?
                if (monsterProgress < currentProgress) {
                	list.add("got " + monster.name + "!");
                	found.add(monster);
                	generateMonster();
                }
            }
        });
        stopWatch.start();
        
		// starts the step counter
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new SimpleStepDetector();
        simpleStepDetector.registerListener(this);
        return view;
    }
    
    private void generateMonster() {
    	double chance = Math.random() * 1.0;
    	if (chance < 0.33) {
    		monster = new Sticker(0, 1, 7, "Sample Rock1", 1, 1, 0, 200, 100, 1, 1, 0, 0, 0, 2000, 100, 100, 100, 5);
    	} else if (chance < 0.66) {
    		monster = new Sticker(0, 1, 7, "Sample Rock2", 1, 1, 0, 125, 140, 1, 1, 0, 0, 0, 2000, 100, 100, 100, 5);
    	} else {
    		monster = new Sticker(0, 1, 7, "Sample Rock3", 1, 1, 0, 159, 153, 1, 1, 0, 0, 0, 2000, 100, 100, 100, 5);
    	}
    	monsterProgress = monster.current_speed + monster.current_reach;
    	currentProgress = 0;
    	monsterName.setText(monster.name);
    	//startProgressBar();
    }
    
    private void startProgressBar() {
    	new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (monsterProgress > currentProgress) {
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							monsterHealth.setProgress((int) (currentProgress / monsterProgress * 100));
						}
					});
				}
				
			}
		}).start();;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
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
    	
    }

    @Override
    public void step(long timeNs) {
    	if (Math.random() < 0.5) {
            coins++;
        }
    	steps++;
    	currentProgress += powerStep;
    	distance = (steps * .91) / 1000;
        tvDistance.setText("Distance: " + distance);
        tvPace.setText("Steps: " + steps);
        tvCoin.setText("Coins: " + coins);
    }
    
}

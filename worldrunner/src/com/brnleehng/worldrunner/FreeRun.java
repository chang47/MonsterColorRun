package com.brnleehng.worldrunner;

import DB.DBManager;
import DB.Model.Monster;
import DB.Model.Sticker;
import Model.BattleMonster;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.renderscript.Sampler.Value;
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
	// setup the step detectors
    private SimpleStepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    
    // setup the layout files
    private TextView tvDistance;
    private TextView tvTime;
    private TextView tvPace;
    private TextView tvCoin;
    private ListView listView;
    private TextView monsterName;
    private ProgressBar monsterHealth;
    private ProgressBar partyHealth1;
    private ProgressBar partyHealth2;
    private ProgressBar partyHealth3;
    private ProgressBar partyHealth4;
    private ProgressBar partyHealth5;
    private Button btnStop;
    private Button btnLog;
    
    // list of stickers that were found, temporarily changed to be a list
    // of monsters
    private ArrayList<Sticker> found;
    //private ArrayList<Monster> found;
    
   
    private long startTime;
    private long countUp;
   
    // calculate running metrics
    private int steps;
    private double distance;
    private int coins;
    private double powerStep;
    
    // shouldn't have since the DB technically should only be accessed via
    // the controller (Hub), but we'll just have it here anyways for now
    private DBManager db;
    
    // Ignore
    private static final double FRIEND1 = 1.1;
    private static final double FRIEND2 = 1.1;
    private static final double FRIEND3 = 1.1;
    private static final double FRIEND4 = 1.1;
    private static final double FRIEND5 = 1.1;
   
    // Ignore until sticker fixed
    //private Sticker monster;
    
    // the current monster
    private BattleMonster monster = null;

    
    //The Party
    private BattleMonster party1,party2,party3,party4,party5 = null;

    
    // list of messages that are used to display the progress
    // made in the game. You just add it into an adapter and it'll do everything for you
    //private List<String> list; 
    private ArrayAdapter<String> adapter;
    private Handler mHandler = new Handler();
    
    //
    public ArrayList<Monster> monsterList;
    public ArrayList<Monster> partyList;
    
    public Dialog showLog;
    
  
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
        monsterHealth = (ProgressBar) view.findViewById(R.id.enemyProgress);
        partyHealth1 = (ProgressBar) view.findViewById(R.id.monsterProgress1);
        partyHealth2 = (ProgressBar) view.findViewById(R.id.monsterProgress2);
        partyHealth3 = (ProgressBar) view.findViewById(R.id.monsterProgress3);
        partyHealth4 = (ProgressBar) view.findViewById(R.id.monsterProgress4);
        partyHealth5 = (ProgressBar) view.findViewById(R.id.monsterProgress5);
        monsterList = Hub.monsterList;
        partyList = Hub.partyList;
        //monsterHealth.getProgressDrawable().setColorFilter(Color.RED,);
        //Need to set the mode in order to change the color.
        Log.d("size of party list", "" + partyList.size());
        //Hub.partyList
        
        btnLog = (Button) view.findViewById(R.id.btnLog);
        btnStop = (Button) view.findViewById(R.id.stopMission);
        
        // initialize fields
        steps = 0;
        distance = 0;
        coins = 0;
        db = new DBManager(getActivity().getApplicationContext());
        found = new ArrayList<Sticker>();
        //found = new ArrayList<Monster>();
        
        // ignore, will be added once you can add friends 
        powerStep = 5 * FRIEND1 * FRIEND2 * FRIEND3 * FRIEND4 * FRIEND5;

        // Initializes first monster encounter
        generateMonster();
        generateParty();
        
        // Once you're done with your run you can save all of the
        // new monsters that you've caught. Ignore for now
		btnLog.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						ArrayList<String> list = new ArrayList<String>();
						list.add("I got hit for 10 points");
						list.add("I ran 5 miles");
						list.add("I did 50 steps");
						list.add("My babbit died");
						list.add("I found a lolipop on the floor. It was delciious");
						Bundle bundle = new Bundle();
						bundle.putStringArrayList("Log", list);
						
						
						RunLogDialog newFragment = new RunLogDialog();
						
						newFragment.setArguments(bundle);
						
						newFragment.show(getFragmentManager(), "Run Log");
					}
				});
/*        btnStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				db.addStickers(found);
				found.clear();
				Hub.result();
			}
		});
  */      
       
        
        
        // setup the timer that just displays passage of time
        // every second it checks to see if the monster is dead.
        // and if it is, replace it. You might first try replacing whenever the user
        // takes a step, but the problem might be that the code will get called multiple
        // times before a step is finished so I put it here.
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
                monsterHealth.setProgress((int) (monster.currentHp / monster.monster.hp * 100));
                partyHealth1.setProgress((int) (party1.currentHp / party1.monster.hp * 100));
                partyHealth2.setProgress((int) (party2.currentHp / party2.monster.hp * 100));
                partyHealth3.setProgress((int) (party3.currentHp / party3.monster.hp * 100));
                partyHealth4.setProgress((int) (party4.currentHp / party4.monster.hp * 100));
                partyHealth5.setProgress((int) (party5.currentHp / party5.monster.hp * 100));
                // FreeRun.checkRun(); this class is the different variation?
                if (monster.currentHp <= 0) {
                	// TODO change to not only check when the monster is dead but you
                	// also need to check the probability of catching it before you add it
                	// into the list of found monsters
                	//list.add("got " + monster.name + "!");
                	// TODO convert a monster to a sticker
                	//found.add(monster);
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
    
    // The function to create a new random monster
    // use your list of monster to generate a new monster to fight
    private void generateMonster() {
    	double chance = Math.random() * 1.0;
    	/** Replace to randomly generate from your list of monsters in a good style
    	 * HINT you'll probably need to use the size of the list of monsters somehow to
    	 * select a random index of yourlist
    	if (chance < 0.33) {
    		monster = new Sticker(0, 1, 7, "Sample Rock1", 1, 1, 0, 200, 100, 1, 1, 0, 0, 0, 2000, 100, 100, 100, 5);
    	} else if (chance < 0.66) {
    		monster = new Sticker(0, 1, 7, "Sample Rock2", 1, 1, 0, 125, 140, 1, 1, 0, 0, 0, 2000, 100, 100, 100, 5);
    	} else {
    		monster = new Sticker(0, 1, 7, "Sample Rock3", 1, 1, 0, 159, 153, 1, 1, 0, 0, 0, 2000, 100, 100, 100, 5);
    	}**/
    	
    	// don't need this anymore, you'll change the progress bar to be the enemies hp
    	//monsterProgress = monster.current_speed + monster.current_reach;
    	monster = new BattleMonster(monsterList.get(0), monsterList.get(0).hp, 1000 / monsterList.get(0).speed);
    	//startProgressBar();
    }
    private void generateParty() {
    	if (partyList.get(0) != null) {
    		party1 = new BattleMonster(partyList.get(0), partyList.get(0).hp, 1000 / partyList.get(0).speed);
    	}
    	if (partyList.get(1) != null) {
    		party2 = new BattleMonster(partyList.get(1), partyList.get(1).hp, 1000 / partyList.get(1).speed);
    	}
    	if (partyList.get(2) != null) {
    		party3 = new BattleMonster(partyList.get(2), partyList.get(2).hp, 1000 / partyList.get(2).speed);
    	}
    	if (partyList.get(3) != null) {
    		party4 = new BattleMonster(partyList.get(3), partyList.get(3).hp, 1000 / partyList.get(3).speed);
    	}
    	if (partyList.get(4) != null) {
    		party5 = new BattleMonster(partyList.get(4), partyList.get(4).hp, 1000 / partyList.get(4).speed);
    	}
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

    /**
     * Detects whenever a step occurs. Combat and distance travelled are calculated here
     * @TODO I imagine this is the place where you will add the battle code, new monster code,
     * one potential problem is that you might accidentally call some code like attack or generate monster
     * multiple times because you can take a lot steps within a second so it really depends on the calculation speed
     * of your phone. You might have to put some sort of boolean indicator to stop any game related steps to happen.
     */
    @Override
    public void step(long timeNs) {
    	if (Math.random() < 0.5) {
            coins++;
        }
    	steps++;
    	//currentProgress += powerStep;
    	distance = (steps * .91) / 1000;
        tvDistance.setText("Distance: " + distance);
        tvPace.setText("Steps: " + steps);
        tvCoin.setText("Coins: " + coins);
        
        if (steps % monster.currentStep == 0)
        {
            party1.currentHp -= (monster.monster.attack - party1.monster.defense);
            party2.currentHp -= (monster.monster.attack - party2.monster.defense);
            party3.currentHp -= (monster.monster.attack - party3.monster.defense);
            party4.currentHp -= (monster.monster.attack - party4.monster.defense);
            party5.currentHp -= (monster.monster.attack - party5.monster.defense);
            
        }
        if (steps % party1.currentStep == 0)
        {
        	monster.currentHp -= (party1.monster.attack - monster.monster.defense);
        	Log.d("1 attacks", "monster health at " + monster.currentHp);
        	
        }
        if (steps % party2.currentStep == 0)
        {
        	monster.currentHp -= (party2.monster.attack - monster.monster.defense);
        	Log.d("2 attacks", "monster health at " + monster.currentHp);
        }
        if (steps % party3.currentStep == 0)
        {
        	monster.currentHp -= (party3.monster.attack - monster.monster.defense);
        	Log.d("3 attacks", "monster health at " + monster.currentHp);
        }
        if (steps % party4.currentStep == 0)
        {
        	monster.currentHp -= (party4.monster.attack - monster.monster.defense);
        	Log.d("4 attacks", "monster health at " + monster.currentHp);
        }
        if (steps % party5.currentStep == 0)
        {
            monster.currentHp -= (party5.monster.attack - monster.monster.defense);
        	Log.d("5 attacks", "monster health at " + monster.currentHp);
        }
    }
    
}

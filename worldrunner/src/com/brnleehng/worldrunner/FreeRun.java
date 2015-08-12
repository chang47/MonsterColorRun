package com.brnleehng.worldrunner;

import java.util.ArrayList;
import DB.DBManager;
import DB.Model.BattleMonster;
import DB.Model.Monster;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.brnleehng.worldrunner.StepDetector.SimpleStepDetector;
import com.brnleehng.worldrunner.StepDetector.StepListener;

public class FreeRun extends Run {
	// setup the step detectors
    private SimpleStepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    
    // setup the layout files
    private TextView tvDistance;
    private TextView tvTime;
    private TextView tvPace;
    private TextView tvCoin;
    private Button stopMission;
    private Button btnLog;
    private LinearLayout enemyPartyLayout;
    private LinearLayout playerPartyLayout;
    public int exp = 0;
    // list of stickers that were found, temporarily changed to be a list
    // of monsters
    private ArrayList<Monster> found;
    //private ArrayList<Monster> found;
    
   
    private long startTime;
    private long countUp;
   
    // calculate running metrics
    private int steps;
    private double distance;
    private int coins;
    private int iPartyAttacked;
    
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

    //For logging purposes
	private ArrayList<String> list = new ArrayList<String>();
    
    // list of messages that are used to display the progress
    // made in the game. You just add it into an adapter and it'll do everything for you
    //private List<String> list; 
    private ArrayAdapter<String> adapter;
    private Handler mHandler = new Handler();
    
    //Sets the list of monsters for various purposes
    public ArrayList<Monster> monsterList;
    public ArrayList<Monster> partyList;
    public ArrayList<BattleMonster> partyMonsterBattleList;
    public ArrayList<BattleMonster> enemyMonsterBattleList;
    public ArrayList<ProgressBar> enemyProgressBarList;
    public ProgressBar[] playerProgressBarList;
    
    //Shows the player's log
    public Dialog showLog;
    
    public int partyMonstersSize;
    
    //Sets amount of monsters defeated based on sides
    public int deadPartyMonsters;
    //Player
    public int deadEnemies;
    //Enemy
    
    //Sets amount of enemies
	int enemyPartySize;
	
	//Sets how many monsters are needed to be beaten
	public int monsterPartiesNeeded = 1;
	
	//Sets if the finish button can be used
	public boolean finishEnabled = false;
	
	public boolean caughtAlready = false;
    
  
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO seperated routes!!!!
		View view = inflater.inflate(R.layout.routeingame_activity, container, false);
		
        // setup intitial objects
        startTime = SystemClock.elapsedRealtime();
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        tvPace = (TextView) view.findViewById(R.id.tvPage);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        tvCoin = (TextView) view.findViewById(R.id.tvCoin);

        monsterList = Hub.monsterList;
        partyList = Hub.equippedStickers;
        partyMonsterBattleList = new ArrayList<BattleMonster>();
        enemyMonsterBattleList = new ArrayList<BattleMonster>();
        enemyProgressBarList = new ArrayList<ProgressBar>();
        playerProgressBarList = new ProgressBar[5];
        enemyPartyLayout = (LinearLayout) view.findViewById(R.id.enemyParty);
        playerPartyLayout = (LinearLayout) view.findViewById(R.id.playerParty);
        deadPartyMonsters = 0;
        deadEnemies = 0;
        enemyPartySize = 0;
        partyMonstersSize = 0;
        //monsterHealth.getProgressDrawable().setColorFilter(Color.RED,);
        //Need to set the mode in order to change the color.
        Log.d("size of party list", "" + partyList.size());
        //Hub.partyList
        
        btnLog = (Button) view.findViewById(R.id.btnLog);
        stopMission = (Button) view.findViewById(R.id.stopMission);
        
        // initialize fields
        steps = 0;
        distance = 0;
        coins = 0;
        db = new DBManager(getActivity().getApplicationContext());
        found = new ArrayList<Monster>();
        //found = new ArrayList<Monster>();
        
        // Initializes first monster encounter
        super.generateEnemies();
        super.generateParty();
        
        //partyMonstersSize = partyBattleList.size();
        
        // Once you're done with your run you can save all of the
        // new monsters that you've caught. Ignore for now
		btnLog.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						Bundle bundle = new Bundle();
						bundle.putStringArrayList("Log", list);	
						
						RunLogDialog newFragment = new RunLogDialog();
						
						newFragment.setArguments(bundle);
						
						newFragment.show(getFragmentManager(), "Run Log");
					}
				});
		
		// TODO for super class, pass in a function that can be overwrited 
        stopMission.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO add sticker and then once we move out, we would re-load the 
				// the sticker list
				db.addStickers(found);
				found.clear();
				Hub.backToCity();
			}
		});      
        
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
                
                // sets the enemy's hp
                // @TODO change it so that the hp gets updated when they get hit and not by the second?
                for (int b = 0; b < enemyProgressBarList.size(); b++) {
                	Log.d("EnemProgBar", "" + enemyMonsterBattleList.get(b).currentHp);
                	enemyProgressBarList.get(b).setProgress((int) (enemyMonsterBattleList.get(b).currentHp / enemyMonsterBattleList.get(b).monster.hp * 100));
                }
                
                for (int iPlayer = 0; iPlayer < partyMonsterBattleList.size(); iPlayer++) {
                	if (partyMonsterBattleList.get(iPlayer) != null)
                		playerProgressBarList[iPlayer].setProgress((int) (partyMonsterBattleList.get(iPlayer).currentHp / partyMonsterBattleList.get(iPlayer).monster.hp * 100));
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
}

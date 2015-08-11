package com.brnleehng.worldrunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import metaModel.Route;
import util.BattleHelper;
import Abilities.Buff;
import Abilities.DamageAbility;
import Abilities.SupportAbility;
import DB.DBManager;
import DB.Model.BattleMonster;
import DB.Model.Monster;
import DB.Model.Sticker;
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

public class RouteRun extends Fragment implements SensorEventListener, StepListener {
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
        //db = new DBManager(getActivity().getApplicationContext());
        found = new ArrayList<Monster>();
        //found = new ArrayList<Monster>();
        
        // Initializes first monster encounter
        generateEnemies();
        generateParty();
        
        
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
				
				// added the new stickers
				db = new DBManager(getActivity());
				db.addStickers(found);
				
				// updating current monsters
				for (Monster monster : partyList) {
					if (monster != null && monster.level != 100) {
						monster.exp += exp / partyMonstersSize;
						int[] exp;
						// level 1, would need index 1 ie level 2 info
						for (int i = monster.level; i < Hub.expTable.size(); i++) {
							exp = Hub.expTable.get(i);
							if (monster.exp >= exp[0]) {
								monster.level++;
							}
						}
						// if they get to level 100
						if (monster.level == 100)
							monster.exp = Hub.expTable.get(99)[0];
						db.updateSticker(monster);
					}					
				}
				
				// finishing the race
				if (finishEnabled) {
					found.clear();
					Hub.moveCity(Hub.currentRoute.to);
				} else {
					found.clear();
					Hub.backToCity();
				}
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
                	enemyProgressBarList.get(b).setProgress((enemyMonsterBattleList.get(b).currentHp * 100 / enemyMonsterBattleList.get(b).monster.hp));
                }
                
                for (int iPlayer = 0; iPlayer < partyMonsterBattleList.size(); iPlayer++) {
                	if (partyMonsterBattleList.get(iPlayer) != null)
                		playerProgressBarList[iPlayer].setProgress((partyMonsterBattleList.get(iPlayer).currentHp * 100 / partyMonsterBattleList.get(iPlayer).monster.hp));
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
    private void generateEnemies() {
    	caughtAlready = false;
    	deadEnemies = 0;
    	enemyPartySize = (int) ((Math.random() * 3.0) + 1);
    	list.add("new enemy party with " + enemyPartySize + " monsters");
		enemyPartyLayout.removeAllViews();
		enemyProgressBarList.clear();
		enemyMonsterBattleList.clear();
		
		//Creates the monsters and adds the UI elements for them
		for (int i = 0; i < enemyPartySize; i++) {
			RelativeLayout relLayout = new RelativeLayout(getActivity());
			
    		LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
    		relLayout.setLayoutParams(linLayoutParam);
			
    		RelativeLayout.LayoutParams relLayoutParamTxt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		RelativeLayout.LayoutParams relLayoutParamImg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		RelativeLayout.LayoutParams relLayoutParamProg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		
    		// Assigned id for enemy ui
    		TextView txt = new TextView(getActivity());
    		txt.setId((i + 1));
    		ImageView imgView = new ImageView(getActivity());
    		imgView.setId((i + 1) * 10 );
    		ProgressBar progBar = new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleHorizontal);
    		progBar.setId((i + 1) * 100);
    		progBar.setProgress(100);
    		
    		imgView.setBackgroundResource(R.drawable.ic_launcher);
    		
    		txt.setText("text");
    		txt.setTextColor(Color.RED);
    		txt.setGravity(Gravity.CENTER);

    		relLayoutParamImg.addRule(RelativeLayout.BELOW, (i + 1));
    		relLayoutParamProg.addRule(RelativeLayout.BELOW, (i + 1) * 10);

    		txt.setLayoutParams(relLayoutParamTxt);
    		imgView.setLayoutParams(relLayoutParamImg);
    		progBar.setLayoutParams(relLayoutParamProg);
    		
    		
    		relLayout.addView(txt);
    		relLayout.addView(imgView);
    		relLayout.addView(progBar);
    		
    		enemyProgressBarList.add(progBar);
    		Log.d("size", "size of list is" + enemyProgressBarList.size());
    		
    		enemyPartyLayout.addView(relLayout);

    		int monsterGen = (int) (Math.random() * Hub.enemyList.size());
    		
       	   	//monster = new BattleMonster(Hub.currentRoute.monsters.get(monsterGen), 
       	   			//Hub.currentRoute.monsters.get(monsterGen).hp, 1000 / Hub.currentRoute.monsters.get(monsterGen).speed);
       	 	//enemyMonsterBattleList.add(new BattleMonster(Hub.enemyList.get(monsterGen), 
       	   	//		Hub.enemyList.get(monsterGen).hp, 1000 / Hub.enemyList.get(monsterGen).speed));
    		enemyMonsterBattleList.add(new BattleMonster(Hub.enemyList.get(monsterGen)));
    		
    		
		}
    	
    	//While might work better here, but depends on how the effectiveness of that.
    	 
    	//startProgressBar();
    }
    
    /**
     * Generates the player's party
     */
    private void generateParty() {
    	playerPartyLayout.removeAllViews();
    	
    	Log.d("user party size", "" + partyList.size());
    	for (int i = 0; i < partyList.size(); i++) {
    		RelativeLayout relLayout = new RelativeLayout(getActivity());
			
    		LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
    		relLayout.setLayoutParams(linLayoutParam);
			
    		RelativeLayout.LayoutParams relLayoutParamTxt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		RelativeLayout.LayoutParams relLayoutParamImg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		RelativeLayout.LayoutParams relLayoutParamProg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		
    		// Assign ui id for monsters
    		TextView txt = new TextView(getActivity());
    		txt.setId((i + 10));
    		ImageView imgView = new ImageView(getActivity());
    		imgView.setId((i + 1) * 11 );
    		
    		// assigns text
    		txt.setTextColor(Color.RED);
    		txt.setGravity(Gravity.CENTER);
    		
    		// assigns the rule for pictures
    		relLayoutParamImg.addRule(RelativeLayout.BELOW, (i + 10));
    		
       		txt.setLayoutParams(relLayoutParamTxt);
    		imgView.setLayoutParams(relLayoutParamImg);
    		
    		relLayout.addView(txt);
    		relLayout.addView(imgView);
    		
    		if (partyList.get(i) == null) {
    			partyMonsterBattleList.add(null);
    			txt.setText("empty");
    			imgView.setBackgroundResource(R.drawable.colorworld);
    			playerProgressBarList[i] = null;
    		} else {
    			// setup real monsters, only creates progress bar if real monster exists
        		ProgressBar progBar = new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleHorizontal);
        		progBar.setId((i + 1) * 101);
        		progBar.setProgress(100);
        		txt.setText("monster");
        		imgView.setBackgroundResource(R.drawable.ic_launcher);
        		
        		progBar.setLayoutParams(relLayoutParamProg);
        		
        		// sets the progress bar
        		relLayoutParamProg.addRule(RelativeLayout.BELOW, (i + 1) * 11);
    			
        		relLayout.addView(progBar);
        		playerProgressBarList[i] = progBar;
        		Log.d("size", "size of list is" + enemyProgressBarList.size());
    			partyMonstersSize++;
    			// TODO Quick hack, but needs to be fixed properly.
    			//partyMonsterBattleList.add(new BattleMonster(partyList.get(i), partyList.get(i).hp, 1000 / partyList.get(i).speed));
    			partyMonsterBattleList.add(new BattleMonster(partyList.get(i)));
    		}
    		
    		playerPartyLayout.addView(relLayout);
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
    
    private void reviveParty(int size) {
    	deadPartyMonsters = 0;
    	for (int i = 0; i < size; i++) {
    		if (partyMonsterBattleList.get(i) != null)
    			partyMonsterBattleList.get(i).resetHp();
    	}
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
    	distance = (steps * .91) / 1000;
    	
        tvDistance.setText("Distance: " + String.format("%.2f", distance));
        tvPace.setText("Steps: " + steps);
        tvCoin.setText("Coins: " + coins);
        
        // monster turn
        for (int i = 0;i < enemyPartySize; i++) {
        	//Attacks Regularly
        	if (enemyMonsterBattleList.get(i).currentHp > 0) {
		        if (steps % enemyMonsterBattleList.get(i).step == 0) {
		        	iPartyAttacked = BattleHelper.AIAttack(enemyMonsterBattleList.get(i), partyMonsterBattleList);

		        	if (iPartyAttacked == -1) {
		        		throw new Error("attacked index is -1, impossible!");
		        	}
		        	
		        	partyMonsterBattleList.get(iPartyAttacked).currentHp -= BattleHelper.Attack(enemyMonsterBattleList.get(i), partyMonsterBattleList.get(iPartyAttacked));
		        	
		            list.add("Enemy " + enemyMonsterBattleList.get(i).monster.name + " Attacks " + partyMonsterBattleList.get(iPartyAttacked).monster.name + " For " + 
		            BattleHelper.Attack(enemyMonsterBattleList.get(i), partyMonsterBattleList.get(iPartyAttacked)));
		
					if (partyMonsterBattleList.get(iPartyAttacked).currentHp <= 0) {
						deadPartyMonsters++;
						if (deadPartyMonsters >= partyMonstersSize) {
							//Entire Party is dead, resurrect them and change monsters
							reviveParty(partyMonsterBattleList.size());
							generateEnemies();
							return;
						}
					}
		        }
		        
		        // monster uses their ability
		        // TODO nothing for now, only do this if working in special battles
/*		        if (steps % enemyMonsterBattleList.get(i).monster.ability.steps == 0) {
		        	if (enemyMonsterBattleList.get(i).monster.ability.getClass() == DamageAbility.class) {
			        	iPartyAttacked = BattleHelper.AIAttack(enemyMonsterBattleList.get(i), partyMonsterBattleList);
						double damage = enemyMonsterBattleList.get(i).monster.ability.activateAbility();
						
						partyMonsterBattleList.get(iPartyAttacked).currentHp -= damage;
						
						
			    		list.add("Enemy " + enemyMonsterBattleList.get(i).monster.name + " Used Ability " +  enemyMonsterBattleList.get(i).monster.ability.name
						+" On " + partyMonsterBattleList.get(iPartyAttacked).monster.name + " For " + damage + "!");
			    		
						if (partyMonsterBattleList.get(iPartyAttacked).currentHp <= 0) {
							
							deadMonsters++;
							
							if (deadMonsters >= partyMonstersSize) {
								//Entire Party is dead, resurrect them and change monsters
								reviveParty(partyMonsterBattleList.size());
								generateEnemies();
								return;
							}
						}
						// TODO never used their support ability
			        } else if (enemyMonsterBattleList.get(i).monster.ability.getClass() == SupportAbility.class) {
	        			SupportAbility support = (SupportAbility)enemyMonsterBattleList.get(i).monster.ability;
	        	        for (int b = 0; b < enemyMonsterBattleList.size(); b++) {
	        	        	Buff newBuff = new Buff(support.name, support.description, support.duration, support.attribute, support.modifier);
	        	        	BattleMonster monster = enemyMonsterBattleList.get(b);
	        	        	enemyMonsterBattleList.get(b).buffs.put(support.attribute, newBuff);
	            	        Log.d("buffs", monster.monster.name + " received the buff" + partyBattleList.get(i).monster.ability.name + " from " + 
	            	        		partyBattleList.get(i).monster.name + " size of party list " + partyBattleList.size());
	            	        Log.d("party", "" + partyBattleList.get(0) + " " + partyBattleList.get(1) + " " + partyBattleList.get(2) + " " +partyBattleList.get(3) 
	            	        		+ " " + partyBattleList.get(4));
	            	        		
	            	        
	        	        	if (support.attribute == 3) {
	        	        		enemyMonsterBattleList.get(b).RecalculateSpeed();
	        	        		Log.d("Speed","New Speed Calculated for : " + enemyMonsterBattleList.get(b).monster.name + " is " + 
	        	        		enemyMonsterBattleList.get(b).currentStep + " duration is: " + enemyMonsterBattleList.get(b).buffs.get(3).duration);
	            	        }
	        	        }
	 //           		list.add(enemyMonsterBattleList.get(i).monster.name + " Used Ability " +  enemyMonsterBattleList.get(i).monster.ability.name + "!");
			        }        		
		        }*/
        	}
        }
        
        // user party attacks
        for (int i = 0; i < partyMonsterBattleList.size(); i++) {
        	if (partyMonsterBattleList.get(i) != null && partyMonsterBattleList.get(i).currentHp > 0) {
	        	if (steps % partyMonsterBattleList.get(i).step == 0) {
	        		int iEnemyAttacked = BattleHelper.AIAttack(partyMonsterBattleList.get(i), enemyMonsterBattleList);
	        		
	        		//Log.d("Speed","Current step speed for " + partyMonsterBattleList.get(i).monster.name + " is " + partyMonsterBattleList.get(i).step);
	        		//Log.d("index problems", "" + iEnemyAttacked);
	        		double damage = BattleHelper.Attack(partyMonsterBattleList.get(i), enemyMonsterBattleList.get(iEnemyAttacked));
	        		enemyMonsterBattleList.get(iEnemyAttacked).currentHp -= damage;
//	        		list.add(partyMonsterBattleList.get(i).monster.name + " Attacks " + enemyMonsterBattleList.get(iEnemyAttacked).monster.name + " For " + damage + "!");
	        		checkEnemyDead(iEnemyAttacked);
	        		
	        		Iterator iterator = partyMonsterBattleList.get(i).buffs.entrySet().iterator();
	        		// decrease buff of monsters
	        		while (iterator.hasNext()) {
	        			Map.Entry<Integer, Buff> pair = (Entry<Integer, Buff>) iterator.next();
	        			int attribute = pair.getKey();
	        			Buff buff = pair.getValue();
	        			//Log.d("buff with duration at: ", "" + buff.duration);
	        			buff.duration--;
	        			Log.d("duration", "" + partyMonsterBattleList.get(i).monster.name + " buff " + buff.name + " has duration " + buff.duration);
	        			//partyBattleList.get(i).buffs.get(iterator).duration--;
	        			//Check if above code actually decreases
	        			if (buff.duration <= 0) {
	        				//Log.d("removed attribute", "" + iterator);
	        				// partyBattleList.get(b).buffs.get(3).duration
	        				iterator.remove();
	        				
	        				// important to be after, becauase recalculate checks for the attribute key
	        				if (attribute == 3) {
	        					partyMonsterBattleList.get(i).RecalculateSpeed();
	        	        		//Log.d("Speed","New Speed Calculated (Buff Removed): " + partyMonsterBattleList.get(i).currentStep);
	        				}
	        				//partyBattleList.get(i).buffs.remove(iterator);
	        			}
	        		}
	        	}
        	
	        	
	        	// check monster status
	        	//Log.d("buff size", ""+ partyBattleList.get(0).buffs.size());
	        	
	        	// checks for user's party's ability
	        	if (steps % partyMonsterBattleList.get(i).monster.activeAbility.steps == 0) { 
	        		//Applies ability to attack enemy
	        		if (partyMonsterBattleList.get(i).monster.activeAbility.getClass() == DamageAbility.class) {
		        		int iEnemyAttack = BattleHelper.AIAttack(partyMonsterBattleList.get(i), enemyMonsterBattleList);
		        		DamageAbility dAbility = (DamageAbility) partyMonsterBattleList.get(i).monster.activeAbility;
	        			double damage = dAbility.damage * partyMonsterBattleList.get(i).monster.attack;
	        			enemyMonsterBattleList.get(iEnemyAttack).currentHp -= damage;
//	            		list.add(partyMonsterBattleList.get(i).monster.name + " Used Ability " +  partyMonsterBattleList.get(i).monster.ability.name + 
//	            				" For " + damage + "!");
	            		
	            		//Checks if all enemies are dead 
	        			checkEnemyDead(iEnemyAttack);

	        		} else if (partyMonsterBattleList.get(i).monster.activeAbility.getClass() == SupportAbility.class) {
	        			//Applies party buffs
	        			SupportAbility support = (SupportAbility)partyMonsterBattleList.get(i).monster.activeAbility;
	        	        for (int b = 0; b < partyMonsterBattleList.size(); b++) {
	        	        	if (partyMonsterBattleList.get(b) != null) {
		        	        	Buff newBuff = new Buff(support.name, support.description, support.duration, support.attribute, support.modifier);
		        	        	partyMonsterBattleList.get(b).buffs.put(support.attribute, newBuff);           	        
		        	        	if (support.attribute == 3) {
		        	        		partyMonsterBattleList.get(b).RecalculateSpeed();
		        	        		Log.d("Speed","New Speed Calculated for : " + partyMonsterBattleList.get(b).monster.name + " is " + partyMonsterBattleList.get(b).step + " duration is: " + partyMonsterBattleList.get(b).buffs.get(3).duration);
		            	        }
	        	        	}
	        	        }
//	            		list.add(partyMonsterBattleList.get(i).monster.name + " Used Ability " +  partyMonsterBattleList.get(i).monster.ability.name + "!");
	        		}
	        	}
        	}
        }
    }
    
    private void checkEnemyDead(int iPartyAttack) {
		if (enemyMonsterBattleList.get(iPartyAttack).currentHp <= 0) {
			exp += enemyMonsterBattleList.get(iPartyAttack).monster.exp;
    		list.add(enemyMonsterBattleList.get(iPartyAttack).monster.name + " has been defeated!");
    		deadEnemies++;
    		captureMonster(iPartyAttack);
    		checkEnemyMonsterAllDead();
		}
    }
    
    private void captureMonster(int iPartyAttack) {
    	if (!caughtAlready && (double) ((Math.random() * 100.0) + 1) > enemyMonsterBattleList.get(iPartyAttack).monster.capture) {
			
			list.add(enemyMonsterBattleList.get(iPartyAttack).monster.name + " has been captured!");
			
			found.add(enemyMonsterBattleList.get(iPartyAttack).monster);
			caughtAlready = true;
		}
    }
    
    private void checkEnemyMonsterAllDead() {
    	if (deadEnemies >= enemyPartySize) {
			//deadMonsters++;	
    		list.add("Defeated all enemies");
			monsterPartiesNeeded--;	
			if (monsterPartiesNeeded <= 0) {
				finishEnabled = true;
			}
			generateEnemies();
		}
    }
}

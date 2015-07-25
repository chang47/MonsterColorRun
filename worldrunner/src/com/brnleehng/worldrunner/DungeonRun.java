package com.brnleehng.worldrunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Abilities.Buff;
import Abilities.DamageAbility;
import Abilities.SupportAbility;
import DB.DBManager;
import DB.Model.Monster;
import DB.Model.Sticker;
import Model.BattleMonster;
import Other.BattleHelper;
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

import com.brnleehng.worldrunner.StepDetector.SimpleStepDetector;
import com.brnleehng.worldrunner.StepDetector.StepListener;

public class DungeonRun extends Fragment implements SensorEventListener, StepListener{
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
    private Button btnLog;
    private Button stopMission;
    private LinearLayout enemyPartyLayout;
    
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
    private int enemyAttack;
    
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
    public ArrayList<BattleMonster> partyBattleList;
    public ArrayList<BattleMonster> monsterBattleList;
    public ArrayList<ProgressBar> progBarList;
    
    //Shows the player's log
    public Dialog showLog;
    
    public int partyMonsters;
    
    //Sets amount of monsters defeated based on sides
    public int deadMonsters;
    //Player
    public int deadEnemies;
    //Enemy
    
    //Sets amount of enemies
	int amount;
    
  
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
        //monsterName = (TextView) view.findViewById(R.id.monsterName);
        //monsterHealth = (ProgressBar) view.findViewById(R.id.enemyProgress);
        partyHealth1 = (ProgressBar) view.findViewById(R.id.monsterProgress1);
        partyHealth2 = (ProgressBar) view.findViewById(R.id.monsterProgress2);
        partyHealth3 = (ProgressBar) view.findViewById(R.id.monsterProgress3);
        partyHealth4 = (ProgressBar) view.findViewById(R.id.monsterProgress4);
        partyHealth5 = (ProgressBar) view.findViewById(R.id.monsterProgress5);
        
        monsterList = Hub.monsterList;
        partyList = Hub.partyList;
        partyBattleList = new ArrayList<BattleMonster>();
        monsterBattleList = new ArrayList<BattleMonster>();
        progBarList = new ArrayList<ProgressBar>();
        enemyPartyLayout = (LinearLayout) view.findViewById(R.id.enemyParty);
        deadMonsters = 0;
        deadEnemies = 0;
        amount = 0;
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
        found = new ArrayList<Sticker>();
        //found = new ArrayList<Monster>();
        
        // ignore, will be added once you can add friends 
        powerStep = 5 * FRIEND1 * FRIEND2 * FRIEND3 * FRIEND4 * FRIEND5;

        // Initializes first monster encounter
        generateMonster();
        generateParty();
        
        partyMonsters = partyBattleList.size();
        
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
		
		stopMission.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.addStickers(found);
				found.clear();
				Hub.backToCity();
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
                
                for (int b = 0;b<progBarList.size();b++) {
                	
                	progBarList.get(b).setProgress((int) (monsterBattleList.get(b).currentHp / monsterBattleList.get(b).monster.hp * 100));
                	Log.d("size time", "size of list: " + progBarList.size());
                }
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
    	deadEnemies = 0;
    	double chance = Math.random() * 1.0;
    	amount = (int) ((Math.random() * 3.0) + 1);
    	
		enemyPartyLayout.removeAllViews();
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
		/*
		 * Creates the monsters and adds the UI elements for them
		 */
		for (int l=0;l<amount;l++) {
			RelativeLayout relLayout = new RelativeLayout(getActivity());
			
    		LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
    		relLayout.setLayoutParams(linLayoutParam);
			
    		RelativeLayout.LayoutParams relLayoutParamTxt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		RelativeLayout.LayoutParams relLayoutParamImg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		RelativeLayout.LayoutParams relLayoutParamProg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		
    		// Assigned id for enemy ui
    		TextView txt = new TextView(getActivity());
    		txt.setId((l + 1));
    		ImageView imgView = new ImageView(getActivity());
    		imgView.setId((l + 1) * 10 );
    		ProgressBar progBar = new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleHorizontal);
    		progBar.setId((l + 1) * 100);
    		progBar.setProgress(100);
    		
    		imgView.setBackgroundResource(R.drawable.ic_launcher);
    		
    		txt.setText("text");
    		txt.setTextColor(Color.RED);
    		txt.setGravity(Gravity.CENTER);

    		relLayoutParamImg.addRule(RelativeLayout.BELOW, (l + 1));
    		relLayoutParamProg.addRule(RelativeLayout.BELOW, (l + 1) * 10);

    		txt.setLayoutParams(relLayoutParamTxt);
    		imgView.setLayoutParams(relLayoutParamImg);
    		progBar.setLayoutParams(relLayoutParamProg);
    		
    		
    		relLayout.addView(txt);
    		relLayout.addView(imgView);
    		relLayout.addView(progBar);
    		
    		progBarList.add(l, progBar);
    		Log.d("size", "size of list is" + progBarList.size());
    		
    		enemyPartyLayout.addView(relLayout);
    		
    		int monsterGen = (int) (Math.random() * Hub.currentDungeon.monsters.size());
    		
       	   	monster = new BattleMonster(Hub.currentDungeon.monsters.get(monsterGen), 
       	   			Hub.currentDungeon.monsters.get(monsterGen).hp, 1000 / Hub.currentDungeon.monsters.get(monsterGen).speed);
       	 	monsterBattleList.add(monster);
    		
		}
    	
    	//While might work better here, but depends on how the effectiveness of that.
    	 
    	//startProgressBar();
    }
    
    /**
     * Generates the player's party
     */
    private void generateParty() {
    	if (partyList.get(0) != null) {
    		party1 = new BattleMonster(partyList.get(0), partyList.get(0).hp, 1000 / partyList.get(0).speed);
    		partyBattleList.add(party1);
    	}
    	if (partyList.get(1) != null) {
    		party2 = new BattleMonster(partyList.get(1), partyList.get(1).hp, 1000 / partyList.get(1).speed);
    		partyBattleList.add(party2);
    	}
    	if (partyList.get(2) != null) {
    		party3 = new BattleMonster(partyList.get(2), partyList.get(2).hp, 1000 / partyList.get(2).speed);
    		partyBattleList.add(party3);
    	}
    	if (partyList.get(3) != null) {
    		party4 = new BattleMonster(partyList.get(3), partyList.get(3).hp, 1000 / partyList.get(3).speed);
    		partyBattleList.add(party4);
    	}
    	if (partyList.get(4) != null) {
    		party5 = new BattleMonster(partyList.get(4), partyList.get(4).hp, 1000 / partyList.get(4).speed);
    		partyBattleList.add(party5);
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
    
    public void reviveParty(int size) {
    	deadMonsters = 0;
    	for (int i = 0; i < size; i++) {
    		partyBattleList.get(i).currentHp = partyBattleList.get(i).monster.hp;
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
    	//currentProgress += powerStep;
    	distance = (steps * .91) / 1000;
    	tvDistance.setText("Distance: " + String.format("%.2f", distance));
        tvPace.setText("Steps: " + steps);
        tvCoin.setText("Coins: " + coins);
        
        // monster turn
        for (int i = 0;i < amount; i++) {
        	//Attacks Regularly
        	if (monsterBattleList.get(i).currentHp > 0) {
		        if (steps % monsterBattleList.get(i).currentStep == 0) {
		        	enemyAttack = BattleHelper.AIAttack(monsterBattleList.get(i), partyBattleList);
		        	
		        	partyBattleList.get(enemyAttack).currentHp -= BattleHelper.Attack(monster, partyBattleList.get(enemyAttack));
		        	
		            list.add("Enemy " + monsterBattleList.get(i).monster.name + " Attacks " + partyBattleList.get(enemyAttack).monster.name + " For " + 
		            BattleHelper.Attack(monsterBattleList.get(i),partyBattleList.get(enemyAttack)));
		
					if (partyBattleList.get(enemyAttack).currentHp <= 0) {
						
						deadMonsters++;
						
						if (deadMonsters >= partyMonsters) {
							//Entire Party is dead, resurrect them and change monsters
							reviveParty(partyBattleList.size());
							
							generateMonster();
							return;
						}
					}
		        }
		        
		        // monster uses their ability
		        if (steps % monsterBattleList.get(i).monster.ability.steps == 0) {
		        	enemyAttack = BattleHelper.AIAttack(monster, partyBattleList);
					double damage = monsterBattleList.get(i).monster.ability.activateAbility();
					
					partyBattleList.get(enemyAttack).currentHp -= damage;
					
					
		    		list.add("Enemy " + monsterBattleList.get(i).monster.name + " Used Ability " +  monsterBattleList.get(i).monster.ability.name
					+" On " + partyBattleList.get(enemyAttack).monster.name + " For " + damage + "!");
		    		
		
					if (partyBattleList.get(enemyAttack).currentHp <= 0) {
						
						deadMonsters++;
						
						if (deadMonsters >= partyMonsters) {
							//Entire Party is dead, resurrect them and change monsters
							reviveParty(partyBattleList.size());
		
							generateMonster();
							return;
						}
					}
		        } else if (monsterBattleList.get(i).monster.ability.getClass() == SupportAbility.class) {
        			SupportAbility support = (SupportAbility)monsterBattleList.get(i).monster.ability;
        	        for (int b = 0; b < monsterBattleList.size(); b++) {
        	        	Buff newBuff = new Buff(support.name, support.description, support.duration, support.attribute, support.modifier);
        	        	BattleMonster monster = monsterBattleList.get(b);
        	        	monsterBattleList.get(b).buffs.put(support.attribute, newBuff);
/*            	        Log.d("buffs", monster.monster.name + " received the buff" + partyBattleList.get(i).monster.ability.name + " from " + 
            	        		partyBattleList.get(i).monster.name + " size of party list " + partyBattleList.size());
            	        Log.d("party", "" + partyBattleList.get(0) + " " + partyBattleList.get(1) + " " + partyBattleList.get(2) + " " +partyBattleList.get(3) 
            	        		+ " " + partyBattleList.get(4));*/
            	        		
            	        
        	        	if (support.attribute == 3) {
        	        		monsterBattleList.get(b).RecalculateSpeed();
        	        		Log.d("Speed","New Speed Calculated for : " + monsterBattleList.get(b).monster.name + " is " + 
        	        		monsterBattleList.get(b).currentStep + " duration is: " + monsterBattleList.get(b).buffs.get(3).duration);
            	        }
        	        }
            		list.add(monsterBattleList.get(i).monster.name + " Used Ability " +  monsterBattleList.get(i).monster.ability.name + "!");
        		}
        	}
        }
        
        // user party attacks
        for (int i = 0; i < partyBattleList.size(); i++) {
        	if (partyBattleList.get(i).currentHp > 0) {
	        	if (steps % partyBattleList.get(i).currentStep == 0) {
	        		int partyAttack = BattleHelper.AIAttack(partyBattleList.get(i), monsterBattleList);
	        		
	        		Log.d("Speed","Current step speed for " + partyBattleList.get(i).monster.name + " is " + partyBattleList.get(i).currentStep);
	        		double damage = BattleHelper.Attack(partyBattleList.get(i), monster);
	        		monsterBattleList.get(partyAttack).currentHp -= damage;
	        		list.add(partyBattleList.get(i).monster.name + " Attacks " + monsterBattleList.get(partyAttack).monster.name + " For " + damage + "!");

	        		if (monsterBattleList.get(partyAttack).currentHp <= 0) {
		        		list.add(partyBattleList.get(i).monster.name + "has been defeated!");
		        		deadEnemies++;

        				if ((double) ((Math.random() * 100) + 1) > monsterBattleList.get(partyAttack).monster.capture) {
        					
        					list.add(monsterBattleList.get(partyAttack).monster.name + " has been captured!");
        					
        					Hub.addSticker(new Sticker(0,0,0,monsterBattleList.get(partyAttack).monster.name,0,0,0,0,0,0,0,0,
        							monsterBattleList.get(partyAttack).monster.hp,monsterBattleList.get(partyAttack).monster.attack,
        							monsterBattleList.get(partyAttack).monster.defense,monsterBattleList.get(partyAttack).monster.speed,
        							monsterBattleList.get(partyAttack).monster.capture,monsterBattleList.get(partyAttack).monster.element,
        							monsterBattleList.get(partyAttack).monster.ability));
        				}
        				
	        			if (deadEnemies >= amount) {
		        			generateMonster();
	        			}
	        		}
	        		
	        		Iterator iterator = partyBattleList.get(i).buffs.entrySet().iterator();
	        		// decrease buff of monsters
	        		while (iterator.hasNext()) {
	        			Map.Entry<Integer, Buff> pair = (Entry<Integer, Buff>) iterator.next();
	        			int attribute = pair.getKey();
	        			Buff buff = pair.getValue();
	        			//Log.d("buff with duration at: ", "" + buff.duration);
	        			buff.duration--;
	        			Log.d("duration", "" + partyBattleList.get(i).monster.name + " buff " + buff.name + " has duration " + buff.duration);
	        			//partyBattleList.get(i).buffs.get(iterator).duration--;
	        			//Check if above code actually decreases
	        			if (buff.duration <= 0) {
	        				//Log.d("removed attribute", "" + iterator);
	        				// partyBattleList.get(b).buffs.get(3).duration
	        				iterator.remove();
	        				
	        				// important to be after, becauase recalculate checks for the attribute key
	        				if (attribute == 3) {
	        					partyBattleList.get(i).RecalculateSpeed();
	        	        		Log.d("Speed","New Speed Calculated (Buff Removed): " + partyBattleList.get(i).currentStep);
	        				}
	        				//partyBattleList.get(i).buffs.remove(iterator);
	        			}
	        		}
	        	}
        	
	        	
	        	// check monster status
	        	//Log.d("buff size", ""+ partyBattleList.get(0).buffs.size());
	        	
	        	
	        	// checks for user's party's ability
	        	if (steps % partyBattleList.get(i).monster.ability.steps == 0) { 
	        		//Applies ability to attack enemy
	        		if (partyBattleList.get(i).monster.ability.getClass() == DamageAbility.class) {
		        		int partyAttack = BattleHelper.AIAttack(partyBattleList.get(i), monsterBattleList);
	        			double damage = partyBattleList.get(i).monster.ability.activateAbility();
	        			monsterBattleList.get(partyAttack).currentHp -= damage;
	            		list.add(partyBattleList.get(i).monster.name + " Used Ability " +  partyBattleList.get(i).monster.ability.name + 
	            				" For " + damage + "!");
	            		
	            		//Checks if all enemies are dead
		        		if (monsterBattleList.get(partyAttack).currentHp <= 0) {
			        		list.add(partyBattleList.get(i).monster.name + "has been defeated!");
			        		deadEnemies++;

	        				if ((double) ((Math.random() * 100) + 1) > monsterBattleList.get(partyAttack).monster.capture) {
	        					
	        					list.add(monsterBattleList.get(partyAttack).monster.name + " has been captured!");
	        					
	        					Hub.addSticker(new Sticker(0,0,0,monsterBattleList.get(partyAttack).monster.name,0,0,0,0,0,0,0,0,
	        							monsterBattleList.get(partyAttack).monster.hp,monsterBattleList.get(partyAttack).monster.attack,
	        							monsterBattleList.get(partyAttack).monster.defense,monsterBattleList.get(partyAttack).monster.speed,
	        							monsterBattleList.get(partyAttack).monster.capture,monsterBattleList.get(partyAttack).monster.element,
	        							monsterBattleList.get(partyAttack).monster.ability));
	        				}
	        				
		        			if (deadEnemies >= amount) {
			        			generateMonster();
		        			}
		        		}
	        		} else if (partyBattleList.get(i).monster.ability.getClass() == SupportAbility.class) {
	        			//Applies party buffs
	        			SupportAbility support = (SupportAbility)partyBattleList.get(i).monster.ability;
	        	        for (int b = 0; b < partyBattleList.size(); b++) {
	        	        	Buff newBuff = new Buff(support.name, support.description, support.duration, support.attribute, support.modifier);
	        	        	BattleMonster monster = partyBattleList.get(b);
	        	        	partyBattleList.get(b).buffs.put(support.attribute, newBuff);
	/*            	        Log.d("buffs", monster.monster.name + " received the buff" + partyBattleList.get(i).monster.ability.name + " from " + 
	            	        		partyBattleList.get(i).monster.name + " size of party list " + partyBattleList.size());
	            	        Log.d("party", "" + partyBattleList.get(0) + " " + partyBattleList.get(1) + " " + partyBattleList.get(2) + " " +partyBattleList.get(3) 
	            	        		+ " " + partyBattleList.get(4));*/
	            	        		
	            	        
	        	        	if (support.attribute == 3) {
	        	        		partyBattleList.get(b).RecalculateSpeed();
	        	        		Log.d("Speed","New Speed Calculated for : " + partyBattleList.get(b).monster.name + " is " + partyBattleList.get(b).currentStep + " duration is: " + partyBattleList.get(b).buffs.get(3).duration);
	            	        }
	        	        }
	            		list.add(partyBattleList.get(i).monster.name + " Used Ability " +  partyBattleList.get(i).monster.ability.name + "!");
	        		}
	        	}
        	}
        }
    }
    
}

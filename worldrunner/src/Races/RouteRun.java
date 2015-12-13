package Races;

import java.util.ArrayList;
import java.util.List;

import step.detector.StepService;
import DB.DBManager;
import DB.Model.BattleMonster;
import DB.Model.Monster;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.LinearLayout.LayoutParams;
import battleHelper.BackgroundChecker;
import battleHelper.BattleInfo;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;
import com.brnleehng.worldrunner.RunLogDialog;

public class RouteRun extends Fragment {
	// setup the step detectors
	private TextView tvDistance;
    private TextView tvTime;
    private TextView tvPace;
    private TextView tvCalories;
    private TextView tvCoin;
    private TextView monsterSet;
    private Button stopMission;
    private Button btnLog;
    private LinearLayout enemyPartyLayout;
    private LinearLayout playerPartyLayout;
 
    // variables
    private long countUp;
    public ArrayList<ProgressBar> enemyProgressBarList;
    public ProgressBar[] playerProgressBarList;
    public TextView[] playerMonsterStepCounters;
    public TextView[] enemyMonsterStepCounters;
    private List<ImageView> playerMonsterImage;
    private List<ImageView> enemyMonsterImage;
    // calculate running metrics
    private int steps;
    Intent intent;
    public TextView txtRouteName;
    
    // TODO add back? Probably not needed
    //StepService mService;
    //boolean mBound = false;
    
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
	    try {
			super.onCreate(savedInstanceState);
			
			//TODO seperated routes!!!!
			View view = inflater.inflate(R.layout.routeingame_activity, container, false);
			//setContentView(R.layout.routeingame_activity);
			
			// initializes the game
			BattleInfo.combatStart();
			Log.d("recover", "combat started");
			
	        // setup intitial objects
	        tvDistance = (TextView) view.findViewById(R.id.routeRunDistanceTxt);
	        tvPace = (TextView) view.findViewById(R.id.routeRunPaceTxt);
	        tvTime = (TextView) view.findViewById(R.id.routeRunTimedTxt);
	        tvCalories = (TextView) view.findViewById(R.id.routeRunCaloriesTxt);
	        tvCoin = (TextView) view.findViewById(R.id.coinsEarned);
	        txtRouteName = (TextView) view.findViewById(R.id.CurrentRouteText);
	        monsterSet = (TextView) view.findViewById(R.id.monstersToDefeat);
	        //monsterList = Hub.monsterList;
	        enemyProgressBarList = new ArrayList<ProgressBar>();
	        playerProgressBarList = new ProgressBar[5];
	        enemyPartyLayout = (LinearLayout) view.findViewById(R.id.enemyPartyLayout1);
	        playerPartyLayout = (LinearLayout) view.findViewById(R.id.playerPartyLayout);
	        btnLog = (Button) view.findViewById(R.id.btnLog);
	        stopMission = (Button) view.findViewById(R.id.stopMission);
	        // loads the screens for the user
	        createNewMonsters();
	        createPartyMonsters();
	        
	        // initialize fields
	        steps = 0;
	        txtRouteName.setText(Hub.currentRoute.name);
	        tvDistance.setText("0.00");
	        
	        BackgroundChecker.locationName = Hub.currentRoute.name;
	        
	        // Once you're done with your run you can save all of the
	        // new monsters that you've caught. Ignore for now
	        btnLog.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					/*Bundle bundle = new Bundle();
					bundle.putStringArrayList("Log", BattleInfo.list);
					RunLogDialog newFragment = new RunLogDialog();
					newFragment.setArguments(bundle);
					newFragment.show(getFragmentManager(), "Run Log");*/
				}
			});
			
	    	// TODO for super class, pass in a function that can be overwrited 
			stopMission.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO add sticker and then once we move out, we would re-load the 
					// the sticker list
					
					// updates the player's new monsters
					DBManager db = new DBManager(getActivity());
					db.addStickers(BattleInfo.found);
					
					// update player's current monsters
					for (Monster monster : BattleInfo.partyList) {
						if (monster != null && monster.level != 100) {
							monster.exp += BattleInfo.exp / BattleInfo.partyMonstersSize;
							Log.d("monsterexp", "added " + (BattleInfo.exp / BattleInfo.partyMonstersSize) + "" + BattleInfo.exp + "exp to " + monster.name
									+ " who has" + monster.exp);
							int[] exp;
							// level 1, would need index 1 ie level 2 info
							for (int i = monster.level; i < Hub.expTable.size(); i++) {
								exp = Hub.expTable.get(i);
								Log.d("exp table1", "" + exp[0]);
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
					BackgroundChecker.time = tvTime.getText().toString();
					Log.d("mytime", tvTime.getText().toString() + BackgroundChecker.time);
					// updates the player's status
					if (BattleInfo.finishEnabled) {
						int newCity = Hub.currentRoute.to;
						Hub.player.city = newCity;
						Hub.setCurrentCity(Hub.refCities.get(newCity - 1));
						Log.d("newCity", Hub.refCities.get(newCity - 1).cityName);
						int resId = getActivity().getResources().getIdentifier("background" + (newCity - 1), "drawable", getActivity().getPackageName());
						if (resId != 0)
							Hub.hubContentContainer.setBackgroundResource(resId);
					}
					Hub.player.coin += BattleInfo.coins;
					db.updatePlayer(Hub.player);
					
					BattleInfo.combatFinish();
					// finishing the race and also updates the player info
					Hub.goToResult();
					/*if (BattleInfo.finishEnabled) {
						Log.d("objective finish", "finish is enabled");
						Hub.moveCity(Hub.currentRoute.to);
					} else {
						Hub.backToCity();
					}*/
				}
			});         
	        
			Chronometer stopWatch = (Chronometer) view.findViewById(R.id.chronometer);
	        stopWatch.setOnChronometerTickListener(new OnChronometerTickListener(){
	            @Override
	            public void onChronometerTick(Chronometer chronometer) {
	            	// does time reset in event of crash? 
	                countUp = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
	                String asText = (countUp / 3600) + ":"; 
	                if (countUp % 3600 / 60 < 10) {
	                	asText += "0";
	                }
	                asText += (countUp / 60 % 60) + ":";	                		
	                if (countUp % 60 < 10) {
	                	asText += "0";
	                }
                	asText += (countUp % 60);
	                tvTime.setText(asText);            
	            }
	        });
	        stopWatch.start();
	        return view;
    	} catch (Exception e) {
    		Log.d("clutter crash", "route run create view crash");
    		if (Hub.partyList == null) { Log.d("random route run crash", "partyList is null"); }
			if (Hub.currentCity == null) { Log.d("random route run crash", "current City is null"); }
			if (BattleInfo.partyMonsterBattleList == null) {
	    		Log.d("random route run crash on create view", "partyMonsterBattleList is null");
	    		Log.d("random route run crash on create view", "finished current battle status: " + BackgroundChecker.finishedCurrentBattle);
	    		Log.d("random route run crash on create view", "has the combat started? " + BackgroundChecker.battleStarted);
	    		Log.d("random route run crash on create view", "was the monster attacked? " + BackgroundChecker.monsterWasAttacked);
	    		Log.d("random route run crash on create view", "was the player monster attacked? " + BackgroundChecker.playerMonsterWasAttacked);
	    		Log.d("random route run crash on create view", "was in the background? " + BackgroundChecker.isBackground);
	    		Log.d("random route run crash on create view", "Are there now new enemies? " + BackgroundChecker.newEnemies);
	    		if (BattleInfo.partyList == null) {
	    			Log.d("random route run crash on create view", "partyList is null");
	    		} else {
	    			Log.d("random route run crash on create view", "partyList is not null");	
	    		}
	    	}
			Log.e("random route run crash on create view", e.getClass().getName(), e);
    		throw new Error(e);
    		//e.printStackTrace();
    		//return null;
    	}
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	if (!BackgroundChecker.boundStepService) {
    		getActivity().getApplicationContext().registerReceiver(broadcastReceiver, new IntentFilter(StepService.BROADCAST_ACTION));
    		BackgroundChecker.boundStepService = true;
    	}
    	BackgroundChecker.isBackground = false;
    	tvPace.setText("Step: " + BattleInfo.steps);
    	// TODO same thing with the onReceive 
    	updateUI();
    }
    
    public void onPause() {
    	super.onPause();
    	BackgroundChecker.isBackground = true;
    }    
    
    @Override
    public void onStart() {
    	super.onStart();
    	intent = new Intent(getActivity().getApplicationContext(), StepService.class);
    	// TODO we don't really need to bind?
    	//getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    	getActivity().getApplicationContext().startService(intent);
    	
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Log.d("destroy", "do we destroy?");
    	//getActivity().unbindService(mConnection);
    	Intent intent = new Intent(getActivity().getApplicationContext(), StepService.class);
    	getActivity().getApplicationContext().startService(intent);
    	getActivity().getApplicationContext().stopService(intent);
    	getActivity().getApplicationContext().unregisterReceiver(broadcastReceiver);
    	BackgroundChecker.boundStepService = false;
    	broadcastReceiver = null;
    	intent = null;
    	//mService.stopSelf();
    }
    
    /**
     * If the app is on the screen, update the UI to reflect
     * what has happenned
     */
    private void updateUI() {
    	// adds new monsters
    	try {
    		/*
    		 TODO
    		 
    		 In the future event where we need to re-update the GUI
    		 we can probably have some sort of check with BackgroundChecker
    		 and we can probably also get something from StepService to get
    		 Everything we need
    		 */
    		
    		
    		//updateMonsterSteps();
			if (BackgroundChecker.newEnemies) {
	    		BackgroundChecker.newEnemies = false;
				animateEnemyDefeat();
			}
			
			// changes the hp
			if (BackgroundChecker.monsterWasAttacked) {
				updateMonsterHealth();
			}
			
			if (BackgroundChecker.playerMonsterWasAttacked) {
				updatePlayerMonsterHealth();
			}
			updateMonsterSteps();
    	} catch (Exception e) {  
    		Log.d("clutter crash", "route run crash");
    		if (Hub.partyList == null) { Log.d("random route run crash", "partyList is null"); }
			if (Hub.currentCity == null) { Log.d("random route run crash", "current City is null"); }
			if (BattleInfo.partyMonsterBattleList == null) {
	    		Log.d("random route run crash", "partyMonsterBattleList is null");
	    		Log.d("random route run crash", "finished current battle status: " + BackgroundChecker.finishedCurrentBattle);
	    		Log.d("random route run crash", "has the combat started? " + BackgroundChecker.battleStarted);
	    		Log.d("random route run crash", "was the monster attacked? " + BackgroundChecker.monsterWasAttacked);
	    		Log.d("random route run crash", "was the player monster attacked? " + BackgroundChecker.playerMonsterWasAttacked);
	    		Log.d("random route run crash", "was in the background? " + BackgroundChecker.isBackground);
	    		Log.d("random route run crash", "Are there now new enemies? " + BackgroundChecker.newEnemies);
	    		if (BattleInfo.partyList == null) {
	    			Log.d("random route run crash", "partyList is null");
	    		} else {
	    			Log.d("random route run crash", "partyList is not null");	
	    		}
	    	}
			Log.e("random route run crash", e.getClass().getName(), e);
			
    		Log.e("MonsterColorRun", e.getClass().getName(), e);
    		throw new Error(e);
    		//e.printStackTrace();
    	}
    }
    
    public void animateEnemyDefeat() {
		Animation normalAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.defeat);
		Log.d("defeatanim", "has been called");
		for (TextView txt : enemyMonsterStepCounters) {
			if (txt != null) {
				txt.setText("");
			}
		}
		for (ProgressBar prog : enemyProgressBarList) {
			if (prog != null) {
				prog.setProgress(0);
			}
		}
    	for (int i = 0; i < enemyMonsterImage.size(); i++) {
    		if (i != enemyMonsterImage.size() - 1) {
        		enemyMonsterImage.get(i).startAnimation(normalAnim);
    		} else {
    			Animation endAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.defeat);
    			endAnim.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) { }
					
					@Override
					public void onAnimationRepeat(Animation animation) { }
					
					@Override
					public void onAnimationEnd(Animation animation) {
						createNewMonsters();
					}
				});
        		enemyMonsterImage.get(i).startAnimation(endAnim);
    		}
    	}
    }
    
    private void updateMonsterSteps() {
    	for (int i = 0; i < BattleInfo.enemyMonsterBattleList.size(); i++) {
    		BattleMonster monster = BattleInfo.enemyMonsterBattleList.get(i);
    		if (monster != null && monster.currentHp > 0) {
    			Log.d("size of battle list", "" + BattleInfo.enemyMonsterBattleList.size());
    			Log.d("size of battle list", "" + BattleInfo.battleSteps);
    			Log.d("size of battle list", "" + monster.step);
    			Log.d("enemy health", "index " + i + "monster" + monster.monster.name + " health " + monster.currentHp);
    			int toGo = monster.step - (BattleInfo.battleSteps % monster.step);
    			
    			// TODO bug introduced by making animation not end. Random null
    			if (enemyMonsterStepCounters[i] != null) {
    				enemyMonsterStepCounters[i].setText("" + toGo);
    			}
    		}
    	}
    	
    	for (int i = 0; i < BattleInfo.partyMonsterBattleList.size(); i++) {
    		BattleMonster monster = BattleInfo.partyMonsterBattleList.get(i);
    		if (monster != null && monster.currentHp > 0) {
    			int toGo = monster.step - (BattleInfo.battleSteps % monster.step);
    			playerMonsterStepCounters[i].setText("" + toGo);
    		}
    	}
    }
    
    /**
     * 1. Creates a new view of monster for the user when they first load the app, 
     * 2. defeat an enemy when they're either in the background and came back 
     * or 3. when they finish off the enemy with the app open on the phone
     */
    private void createNewMonsters() {
    	enemyMonsterStepCounters = new TextView[5];
    	enemyMonsterImage = new ArrayList<ImageView>();
    	enemyPartyLayout.removeAllViews();
		BackgroundChecker.newEnemies = false;
		enemyProgressBarList.clear();
		for (int i = 0; i < BattleInfo.enemyMonsterBattleList.size(); i++) {
			BattleMonster battleMonster = BattleInfo.enemyMonsterBattleList.get(i);
			if (battleMonster != null) {
				RelativeLayout relLayout = new RelativeLayout(getActivity());
				
				// adds the relative layout to the overall linear layout
				enemyPartyLayout.addView(relLayout);
				
				// param for relative layout
	    		LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT); //1f = 1 weight
	    		relLayout.setLayoutParams(linLayoutParam);
				
	    		// params for the other Uui
	    		RelativeLayout.LayoutParams relLayoutParamTxt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
	    		RelativeLayout.LayoutParams relLayoutParamImg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
	    		RelativeLayout.LayoutParams relLayoutParamProg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
	    		RelativeLayout.LayoutParams relLayoutParamTxtStep = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
	    		
	    		relLayoutParamProg.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    		relLayoutParamImg.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    		relLayoutParamTxt.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    		relLayoutParamTxtStep.addRule(RelativeLayout.CENTER_HORIZONTAL);
	    		
	    		// Assigned id for enemy ui
	    		TextView txt = new TextView(getActivity());
	    		txt.setId((i + 1));
	    		ImageView imgView = new ImageView(getActivity());
	    		enemyMonsterImage.add(imgView);
	    		imgView.setId((i + 1) * 10 );
	    		ProgressBar progBar = new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleHorizontal);
	    		progBar.setId((i + 1) * 100);
	    		TextView monsterStep = new TextView(getActivity());
	    		
	    		txt.setText(battleMonster.monster.name);
	    		txt.setTextColor(Color.RED);
	    		txt.setTypeface(null, Typeface.BOLD);
	    		txt.setGravity(Gravity.CENTER);
	    		
	    		int toGo = battleMonster.step - (BattleInfo.battleSteps % battleMonster.step);
	    		monsterStep.setText("" + toGo);
	    		monsterStep.setTextColor(Color.BLACK);
	    		monsterStep.setTypeface(null, Typeface.BOLD);
	    		enemyMonsterStepCounters[i] = monsterStep;
	    		
	    		relLayoutParamImg.addRule(RelativeLayout.BELOW, (i + 1));
	    		relLayoutParamProg.addRule(RelativeLayout.BELOW, (i + 1) * 10);
	    		relLayoutParamTxtStep.addRule(RelativeLayout.BELOW, (i + 1) * 100);
	    		monsterStep.setLayoutParams(relLayoutParamTxtStep);
	    		txt.setLayoutParams(relLayoutParamTxt);
	    		imgView.setLayoutParams(relLayoutParamImg);
	    		progBar.setLayoutParams(relLayoutParamProg);
	    		progBar.setProgress((battleMonster.currentHp * 100 / battleMonster.hp));
	    		relLayout.addView(monsterStep);
	    		relLayout.addView(txt);
	    		relLayout.addView(imgView);
	    		relLayout.addView(progBar);
	    		
	    		enemyProgressBarList.add(progBar);
	    		Log.d("size", "size of list is" + enemyProgressBarList.size());
	
	    		int resId = getResources().getIdentifier("body" + battleMonster.monster.monsterId, "drawable", getActivity().getPackageName());
	    		if (resId != 0) {
	    			imgView.setImageResource(resId);
	    		} else {
	    			imgView.setImageResource(R.drawable.ic_launcher);
	    		}
			} else {
				enemyMonsterStepCounters[i] = null;
			}
		}
    }
    
    /**
     * Creates a new view of the user's party when they first load the run 
     */
    private void createPartyMonsters() {
    	playerMonsterStepCounters = new TextView[5];
    	playerPartyLayout.removeAllViews();
    	playerMonsterImage = new ArrayList<ImageView>();
    	for (int i = 0; i < BattleInfo.partyMonsterBattleList.size(); i++) {
    		RelativeLayout relLayout = new RelativeLayout(getActivity());
			
    		LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1f);
    		relLayout.setLayoutParams(linLayoutParam);
			
    		RelativeLayout.LayoutParams relLayoutParamTxt = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		RelativeLayout.LayoutParams relLayoutParamImg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		RelativeLayout.LayoutParams relLayoutParamProg = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		//RelativeLayout.LayoutParams relLayoutParamTxtStep = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    		
    		relLayoutParamProg.addRule(RelativeLayout.CENTER_HORIZONTAL);
    		relLayoutParamImg.addRule(RelativeLayout.CENTER_HORIZONTAL);
    		relLayoutParamTxt.addRule(RelativeLayout.CENTER_HORIZONTAL);
    		
    		// Assign ui id for monsters
    		TextView txt = new TextView(getActivity());
    		txt.setId((i + 10));
    		ImageView imgView = new ImageView(getActivity());
    		playerMonsterImage.add(imgView);
    		imgView.setId((i + 1) * 11 );
    		TextView monsterStep = new TextView(getActivity());
    		
    		// assigns text
    		txt.setTextColor(Color.BLACK);
    		txt.setGravity(Gravity.CENTER);
    		txt.setTypeface(null, Typeface.BOLD);
    		
    		//monsterStep.setTextColor(Color.BLACK);
    		//monsterStep.setGravity(Gravity.CENTER);
    		
    		// assigns the rule for pictures
    		relLayoutParamImg.addRule(RelativeLayout.BELOW, (i + 10));
    		
       		txt.setLayoutParams(relLayoutParamTxt);
    		imgView.setLayoutParams(relLayoutParamImg);
    		relLayout.addView(txt);
    		relLayout.addView(imgView);
    		
    		BattleMonster battleMonster = BattleInfo.partyMonsterBattleList.get(i);
    		if (battleMonster == null) {
    			txt.setText("");
    			//monsterStep.setText("this does work!");
    			imgView.setBackgroundResource(R.drawable.colorworld);
    			playerProgressBarList[i] = null;
    			playerMonsterStepCounters[i] = null;
    		} else {
    			// setup real monsters, only creates progress bar if real monster exists
        		ProgressBar progBar = new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleHorizontal);
        		progBar.setId((i + 1) * 101);
        		progBar.setProgress(battleMonster.currentHp * 100 / battleMonster.hp);
        		
        		int toGo = battleMonster.step - (BattleInfo.battleSteps % battleMonster.step);
        		txt.setText("" + toGo);
        		
        		playerMonsterStepCounters[i] = txt;
        		
        		
        		int resId = getResources().getIdentifier("head" + battleMonster.monster.monsterId, "drawable", getActivity().getPackageName());
        		Log.d("imageId", battleMonster.monster.name + " id is: " + battleMonster.monster.monsterId + " id got was: " + resId);
        		if (resId != 0) {
        			imgView.setBackgroundResource(resId);;
        		} else {
        			imgView.setBackgroundResource(R.drawable.ic_launcher);
        		}
        		progBar.setLayoutParams(relLayoutParamProg);
        		
        		// positions the progress bar
        		relLayoutParamProg.addRule(RelativeLayout.BELOW, (i + 1) * 11);
    			
        		relLayout.addView(progBar);
        		playerProgressBarList[i] = progBar;
    		}
    		
    		playerPartyLayout.addView(relLayout);
    	}
    }
    
    /**
     * Updates the user's monster's hp bar whenever they get hit either when the user
     * first opens the background or when the app is open on the screen and the
     * user walks
     */
    private void updatePlayerMonsterHealth() {
    	BackgroundChecker.playerMonsterWasAttacked = false;
    	for (int i = 0; i < BattleInfo.partyMonsterBattleList.size(); i++) {
    		BattleMonster battleMonster = BattleInfo.partyMonsterBattleList.get(i);
    		if (battleMonster != null) {
    			playerProgressBarList[i].setProgress(battleMonster.currentHp * 100 / battleMonster.hp);
    			if (battleMonster.currentHp <= 0) {
    				playerMonsterStepCounters[i].setText("");
    			}
    		}
    	}
    }
    
    /**
     * Updates the enemies hp bar whenever they get hit either when the user
     * first opens the background or when the app is open on the screen and the
     * user walks
     */
    private void updateMonsterHealth() {
    	BackgroundChecker.monsterWasAttacked = false;
    	for (int i = 0; i < BattleInfo.enemyMonsterBattleList.size(); i++) {
    		BattleMonster battleMonster = BattleInfo.enemyMonsterBattleList.get(i);
    		if (battleMonster != null) {
    			enemyProgressBarList.get(i).setProgress(battleMonster.currentHp * 100 / battleMonster.hp);
    			if (battleMonster.currentHp <= 0) {
    				enemyMonsterStepCounters[i].setText("");
    			}
    		}
    	}
    }
    
    /**
     * Binds this fragment to the service allowing access to the functions
     * that the service has
     */
    private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d("disconnect", "on service connected");
			BackgroundChecker.boundStepService = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			BackgroundChecker.boundStepService  = false;
			Log.d("disconnect", "disconnect on close");
		}
    };
    
    /**
     * Receives calls from the StepService every time the service sends one to the user
     * note: only happens when the user is in the foreground
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent newIntent) {
			Log.d("disconnect", "added new steps " + BattleInfo.steps);
			steps = BattleInfo.steps;
			tvPace.setText("steps: " + steps);
			tvDistance.setText("" + (double) Math.round(BattleInfo.distance * 100) / 100);
			tvCoin.setText("" + BattleInfo.coins + " coin");
			int monstersToGo = BattleInfo.destinationObjective - BattleInfo.fightObjective;
			// TODO not very efficient as we have to recalculate forever
			if (monstersToGo <= 0) {
				monsterSet.setText("Arrived at destination");
			} else {
				monsterSet.setText("" + monstersToGo + " More Sets");
			}
			// TODO improve the calculation by letting the user save their weight and height
			tvCalories.setText("" + Math.round(BattleInfo.calories * 100) / 100);
			updateUI();
		}
	};
}

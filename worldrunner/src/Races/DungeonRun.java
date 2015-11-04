package Races;

import java.util.ArrayList;
import java.util.List;

import step.detector.StepService;
import step.detector.StepService.StepBinder;
import util.TutorialTest;
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
import android.content.SharedPreferences;
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
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
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
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class DungeonRun extends Fragment {
	// setup the step detectors
	private TextView tvDistance;
    private TextView tvTime;
    private TextView tvPace;
    private TextView tvCalories;
    private TextView tvCoin;
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
    public List<ImageView> playerMonsterImage;
    public List<ImageView> enemyMonsterImage;
    
    // calculate running metrics
    private int steps;
    Intent intent;
    public TextView txtRouteName;
        
    StepService mService;
    boolean mBound = false;
    
    private boolean firstTime;
    private SharedPreferences pref;
    private ShowcaseView showIntro;
    private ShowcaseView showEnemy;
    private ShowcaseView showParty;
    private ShowcaseView showStep;
    private ShowcaseView showHealth;
    private ShowcaseView showFinish;
    private View view;
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
    	try { 
			super.onCreate(savedInstanceState);
			
			//TODO seperated routes!!!!
			view = inflater.inflate(R.layout.routeingame_activity, container, false);
			
			// initializes the game
			BattleInfo.combatStart();
			
	        // setup intitial objects
			tvDistance = (TextView) view.findViewById(R.id.routeRunDistanceTxt);
	        tvPace = (TextView) view.findViewById(R.id.routeRunPaceTxt);
	        tvTime = (TextView) view.findViewById(R.id.routeRunTimedTxt);
	        tvCalories = (TextView) view.findViewById(R.id.routeRunCaloriesTxt);
	        tvCoin = (TextView) view.findViewById(R.id.coinsEarned);
	        txtRouteName = (TextView) view.findViewById(R.id.CurrentRouteText);

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
	        
	        // difference
	        txtRouteName.setText(Hub.currentDungeon.dungeonName);
	        tvDistance.setText("0.00");
	        
	        BackgroundChecker.locationName = Hub.currentDungeon.dungeonName;
	        
	        // Once you're done with your run you can save all of the
	        // new monsters that you've caught. Ignore for now
	        btnLog.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putStringArrayList("Log", BattleInfo.list);
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
					DBManager db = new DBManager(getActivity());
					db.addStickers(BattleInfo.found);
					
					// updating current monsters
					for (Monster monster : BattleInfo.partyList) {
						if (monster != null && monster.level != 100) {
							monster.exp += BattleInfo.exp / BattleInfo.partyMonstersSize;
							Log.d("monsterexp", "added " + BattleInfo.exp + " spread over " + BattleInfo.partyMonstersSize + " monsters" + " " + BattleInfo.exp + " exp to " + monster.name
									+ " who has " + monster.exp);
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
					
					// updates the player's status
					BackgroundChecker.time = tvTime.getText().toString();
					
					Hub.player.coin += BattleInfo.coins;
					db.updatePlayer(Hub.player);
					BattleInfo.combatFinish();
					// finishing the race
					Hub.goToResult(); // might need some sort of check for dungeons vs routes
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
	        //firstTime = TutorialTest.showBattle;
	        firstTime = false;
	        view.post(new Runnable() {

				@Override
				public void run() {
					if (firstTime) {
						showIntro = new ShowcaseView.Builder(getActivity())
						.setTarget(new ViewTarget(view.findViewById(R.id.stopMission)))
						.setContentText("Showcase View")
						.setContentText("Equip party!@#!@#FSJ TGKOLR JWELGJLGJ AWLJTLAWJGLJ RTLAWJERFLS JGLAJETGLJW WEJT LWJLSDG JWET LWJ")
						.build();
						
						showIntro.overrideButtonClick(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								showIntro.hide();
								commonHide(showIntro);
								((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showIntro);
				
								showEnemy = new ShowcaseView.Builder(getActivity())
								.setTarget(new ViewTarget(view.findViewById(10)))
								.setContentText("Showcase View")
								.setContentText("Equip party")
								.build();
								
								showEnemy.overrideButtonClick(new View.OnClickListener() {
									
									@Override
									public void onClick(View v) {
										showEnemy.hide();
										commonHide(showEnemy);
										((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showEnemy);
										
										showParty = new ShowcaseView.Builder(getActivity())
										.setTarget(new ViewTarget(view.findViewById(11)))
										.setContentText("Showcase View")
										.setContentText("Equip party")
										.build();
										
										showParty.overrideButtonClick(new View.OnClickListener() {
											
											@Override
											public void onClick(View v) {
												showParty.hide();
												commonHide(showParty);
												((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showParty);
												
												showStep = new ShowcaseView.Builder(getActivity())
												.setTarget(new ViewTarget(view.findViewById(1000)))
												.setContentText("Showcase View")
												.setContentText("Equip party")
												.build();
												
												showStep.overrideButtonClick(new View.OnClickListener() {
													
													@Override
													public void onClick(View v) {
														showStep.hide();
														commonHide(showStep);
														((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showStep);
														
														showHealth = new ShowcaseView.Builder(getActivity())
														.setTarget(new ViewTarget(view.findViewById(100)))
														.setContentText("Showcase View")
														.setContentText("Equip party")
														.build();
														
														showHealth.overrideButtonClick(new View.OnClickListener() {
															
															@Override
															public void onClick(View v) {
																showHealth.hide();
																commonHide(showHealth);
																((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showHealth);
																
																showFinish = new ShowcaseView.Builder(getActivity())
																.setTarget(new ViewTarget(view.findViewById(R.id.stopMission)))
																.setContentText("Showcase View")
																.setContentText("Equip party")
																.build();
																
																showFinish.overrideButtonClick(new View.OnClickListener() {
																	
																	@Override
																	public void onClick(View v) {
																		showFinish.hide();
																		commonHide(showFinish);
																		((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showFinish);
																		TutorialTest.showDungeon = false;
																	}
																});
																
															}
														});
													}
												});
											}
										});
									}
								});
								
							}
						});
					}
				}
	        	
	        });
	        return view;
    	} catch (Exception e) {
    		Log.e("MonsterColorRun", e.getClass().getName(), e);
    		throw new Error(e);
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
    	intent = new Intent(getActivity(), StepService.class);
    	// TODO we don't really need to bind?
    	getActivity().getApplicationContext().startService(intent);
    	
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	Intent intent = new Intent(getActivity().getApplicationContext(), StepService.class);
    	getActivity().getApplicationContext().startService(intent);
    	getActivity().getApplicationContext().stopService(intent);
    	getActivity().getApplicationContext().unregisterReceiver(broadcastReceiver);
    	BackgroundChecker.boundStepService = false;
    	broadcastReceiver = null;
    	intent = null;
    }
    
    /**
     * If the app is on the screen, update the UI to reflect
     * what has happenned
     */
    private void updateUI() {
    	// adds new monsters
		try {
	    	if (BackgroundChecker.newEnemies) {
	    		BackgroundChecker.newEnemies = false;
	    		animateEnemyDefeat();
				//createNewMonsters();
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
			Log.e("MonsterColorRun", e.getClass().getName(), e);
    		//e.printStackTrace();
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
    			
    			// TODO bug here, random null
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
				
	    		LinearLayout.LayoutParams linLayoutParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	    		relLayout.setLayoutParams(linLayoutParam);
				
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
	    		imgView.setId((i + 1) * 10 );
	    		enemyMonsterImage.add(imgView);
	    		
	    		ProgressBar progBar = new ProgressBar(getActivity(),null,android.R.attr.progressBarStyleHorizontal);
	    		progBar.setId((i + 1) * 100);
	    		TextView monsterStep = new TextView(getActivity());
	    		monsterStep.setId((i + 1) * 1000);
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
    	playerMonsterImage = new ArrayList<ImageView>();
    	playerPartyLayout.removeAllViews();
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
    		txt.setId((i + 1 + 11));
    		ImageView imgView = new ImageView(getActivity());
    		imgView.setId((i + 1) * 11 );
    		playerMonsterImage.add(imgView);
    		TextView monsterStep = new TextView(getActivity());
    		
    		// assigns text
    		txt.setTextColor(Color.BLACK);
    		txt.setGravity(Gravity.CENTER);
    		txt.setTypeface(null, Typeface.BOLD);
    		
    		//monsterStep.setTextColor(Color.BLACK);
    		//monsterStep.setGravity(Gravity.CENTER);
    		
    		// assigns the rule for pictures
    		relLayoutParamImg.addRule(RelativeLayout.BELOW, (i + 1 + 11));
    		
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
			mBound = false;
		}
    };
    
    /**
     * Receives calls from the StepService every time the service sends one to the user
     * note: only happens when the user is in the foreground
     */
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			steps = BattleInfo.steps;
			tvPace.setText("steps: " + steps);
			tvDistance.setText("" + (double) Math.round(BattleInfo.distance * 100) / 100);
			tvCoin.setText("" + BattleInfo.coins + " coin");
			// TODO improve the calculation by letting the user save their weight and height
			tvCalories.setText("" + Math.round(BattleInfo.calories * 100) / 100);
			updateUI();
		}
	};
	
	public static void commonHide(ShowcaseView scv) {
		scv.setOnClickListener(null);
		scv.setOnShowcaseEventListener(null);
		scv.setOnTouchListener(null);
	}
}

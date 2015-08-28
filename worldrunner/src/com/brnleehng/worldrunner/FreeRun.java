package com.brnleehng.worldrunner;

import java.util.ArrayList;

import step.detector.SimpleStepDetector;
import step.detector.StepListener;
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

public class FreeRun extends Run {
	
	public FreeRun() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflateFragment(R.layout.routeingame_activity, inflater, container); 
        return view;
	}
	
	@Override
	public Button setFinishButton(Button stopMission) {
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
        return stopMission;
    }
}

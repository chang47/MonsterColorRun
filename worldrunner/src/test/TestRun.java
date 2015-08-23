package test;

import java.util.ArrayList;

import test.TestStepService.StepBinder;
import DB.DBManager;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brnleehng.worldrunner.R;

public class TestRun extends Fragment {
    // setup the layout files
    private TextView tvDistance;
    private TextView tvTime;
    private TextView tvPace;
    private TextView tvCoin;
    private Button stopMission;
    private Button btnLog;
 
    private long countUp;
   
    // calculate running metrics
    private int steps;
    private double distance;
    private int coins;

    
    TestStepService mService;
    boolean mBound;
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO seperated routes!!!!
		View view = inflater.inflate(R.layout.routeingame_activity, container, false);
		
        // setup intitial objects
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        tvPace = (TextView) view.findViewById(R.id.tvPage);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        tvCoin = (TextView) view.findViewById(R.id.tvCoin);
        
        btnLog = (Button) view.findViewById(R.id.btnLog);
        stopMission = (Button) view.findViewById(R.id.stopMission);
        
        // initialize fields
        steps = 0;
        distance = 0;
        coins = 0;

        // Once you're done with your run you can save all of the
        // new monsters that you've caught. Ignore for now
		btnLog.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
					}
				});
		
		// TODO for super class, pass in a function that can be overwrited 
        stopMission.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mBound) {
					steps = mService.getStep();
					countUp = mService.getTime();
					tvPace.setText("Steps: " + steps);
					tvTime.setText("Time: " + countUp);
				}
			}
		});      

        return view;
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    	Intent intent = new Intent(getActivity(), TestStepService.class); // this?
    	getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			StepBinder binder = (StepBinder) service;
			mService = binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBound = false;
		}
    	
    };
    
}

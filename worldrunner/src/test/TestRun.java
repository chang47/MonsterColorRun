package test;

import java.util.ArrayList;

import test.TestStepService.StepBinder;
import DB.DBManager;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brnleehng.worldrunner.R;
import com.brnleehng.worldrunner.R.id;
import com.brnleehng.worldrunner.R.layout;

public class TestRun extends Fragment {
    // setup the layout files
    private TextView tvTime;
    private TextView tvPace;
    private Button stopMission;
 
    private long countUp;
   
    // calculate running metrics
    private int steps;
    Intent intent;
    
    TestStepService mService;
    boolean mBound = false;
    
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//TODO seperated routes!!!!
		View view = inflater.inflate(R.layout.testrun_activity, container, false);
		
        // setup intitial objects
        tvPace = (TextView) view.findViewById(R.id.testStep);
        tvTime = (TextView) view.findViewById(R.id.testTime);
        
        stopMission = (Button) view.findViewById(R.id.stopBtnTest2);
        
        // initialize fields
        steps = 0;

        // Once you're done with your run you can save all of the
        // new monsters that you've caught. Ignore for now
        stopMission.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tvPace.setText("initial " + steps);
				Toast.makeText(getActivity(), "pressed", Toast.LENGTH_SHORT).show();
				if (mBound) {
					steps = mService.getStep();
					countUp = mService.getTime();
					tvPace.setText("Steps: " + steps);
					tvTime.setText("Time: " + countUp);
					Toast.makeText(getActivity(), "got response", Toast.LENGTH_LONG).show();
				}
			}
		});      

        return view;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	getActivity().registerReceiver(broadcastReceiver, new IntentFilter(TestStepService.BROADCAST_ACTION));
    	BackgroundChecker.isBackground = false;
    	
    	// TODO same thing with the onReceive 
    }
    
    public void onPause() {
    	super.onPause();
    	BackgroundChecker.isBackground = true;
    }
    
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			steps = mService.getStep();
			tvPace.setText("steps: " + steps);
			
			// adds new monsters
			if (BackgroundChecker.newEnemies) {
				BackgroundChecker.newEnemies = false;
			}
			
			// changes the hp
			if (BackgroundChecker.monsterWasAttacked) {
				// do the updating
				BackgroundChecker.monsterWasAttacked = false;
			}
		}
	};
    
    @Override
    public void onStart() {
    	super.onStart();
    	Log.d("service1", "ran the on Start");
    	intent = new Intent(getActivity(), TestStepService.class); // this?
    	getActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    	getActivity().startService(intent);
    }
    
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d("service1", "on service connected");
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

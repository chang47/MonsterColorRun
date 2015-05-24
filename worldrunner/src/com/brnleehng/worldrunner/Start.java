package com.brnleehng.worldrunner;

import com.brnleehng.worldrunner.R;
import com.brnleehng.worldrunner.R.id;
import com.brnleehng.worldrunner.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

// The start page. Currently not used and skipped to the next step.
public class Start extends Activity {
	private Button startButton;
	public static final String PREFS_NAME = "MyPrefsFile";
	
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        startButton = (Button) findViewById(R.id.gameStartButton);
        startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				Intent intent;
				if (settings.getBoolean("first_time", true)) {
					intent = new Intent(Start.this, Register.class);
				} else {
					//@TODO change the correct menu
					intent = new Intent(Start.this, Pregame.class);
				}
				Start.this.startActivity(intent);	
			}
		});
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    
}
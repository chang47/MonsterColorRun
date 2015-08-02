package util;

import com.brnleehng.worldrunner.R;

import android.app.Activity;
import android.os.Bundle;

public class Template extends Activity {
	
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
		//setContentView(R.layout.race_activity);
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

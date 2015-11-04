package com.brnleehng.worldrunner;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ShowcaseActivity extends Activity {
	private ShowcaseView sv;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showercase_activity);
		Button button1 = (Button) findViewById(R.id.showbutton1);
		Button button2 = (Button) findViewById(R.id.showbutton2);
		
		ViewTarget target = new ViewTarget(R.id.showbutton1, this);
		sv = new ShowcaseView.Builder(this)
			.setTarget(target)
			.setContentText("Showcase View")
			.setContentText("this is highlighting the home button")  
			.build();
		sv.hideButton();
		button1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sv.hide();
				
			}
		});
		
		button2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "text", Toast.LENGTH_LONG).show();
			}
		});

		
	}
}

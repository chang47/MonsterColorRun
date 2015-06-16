package com.brnleehng.worldrunner;

import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SplashPage extends Activity {
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Gets the window size so that it can be used to stretch and scale the image as needed
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splashpage_activity);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		
		Bitmap bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.background), size.x, size.y, true);
		BitmapDrawable background = new BitmapDrawable(bmp);
		
		RelativeLayout screen = (RelativeLayout) findViewById(R.id.splashScreen);
		screen.setBackground(background);
		
		// sets the flashing start text
		Animation animation = new AlphaAnimation(1, 0);
		animation.setDuration(1000);
		animation.setInterpolator(new LinearInterpolator());
		animation.setRepeatCount(Animation.INFINITE);
		animation.setRepeatMode(Animation.REVERSE);
		
		// attaches the animation to the text view
		TextView startView = (TextView) findViewById(R.id.startTextView);
		startView.startAnimation(animation);
		
		// clicks on splash page
		screen.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "clicked on the screen", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(getApplicationContext(), Hub.class);
				startActivity(intent);
			}
		});
		
	}
}

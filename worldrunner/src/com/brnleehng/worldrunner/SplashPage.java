package com.brnleehng.worldrunner;

import intro.NameRequest;
import intro.WalkThrough;
import DB.DBManager;
import android.R.anim;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SplashPage extends Activity {
	
	MediaPlayer backgroundMusic;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// sets up the sound effects to be used 
		final SoundPool sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		final int soundIds[] = new int[2];
		soundIds[0] = sp.load(getBaseContext(), R.raw.click, 1);
		soundIds[1] = sp.load(getBaseContext(), R.raw.enterbattle, 1);
		
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
		
		backgroundMusic = MediaPlayer.create(this, R.raw.thecurtainrises);
		backgroundMusic.setLooping(false);
		backgroundMusic.start();
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
				DBManager db = new DBManager(getApplicationContext());
				db.close();
				Intent intent = new Intent(getApplicationContext(), NameRequest.class);
				sp.play(soundIds[1], 1, 1, 1, 0, 1);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("destroy", "destroyed is call on splash");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("destroy", "puased is call on splash");
		backgroundMusic.pause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		backgroundMusic.start();
	}
}

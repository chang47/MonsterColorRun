package Races;

import battleHelper.BattleInfo;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Result extends Fragment {
	private static MediaPlayer backgroundMusic;
	public TextView coin;
	public TextView time;
	public TextView steps;
	public TextView distance;
	public TextView calories;
	public Button button;
	private Animation animation;
	private Animation animation2;
	private Animation animation3;
	private Animation animation4;
	private Animation animation5;
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		backgroundMusic = MediaPlayer.create(getActivity(), R.raw.victory);
		backgroundMusic.setLooping(true);
		backgroundMusic.start();
		View view = inflater.inflate(R.layout.result_activity, container, false);
		coin = (TextView) view.findViewById(R.id.resultCoins);
		time = (TextView) view.findViewById(R.id.resultTime);
		steps = (TextView) view.findViewById(R.id.resultSteps);
		distance = (TextView) view.findViewById(R.id.resultDistances1);
		calories = (TextView) view.findViewById(R.id.resultCalories);
		button = (Button) view.findViewById(R.id.resultDone);
		RelativeLayout done = (RelativeLayout) view.findViewById(R.id.overallResultContainer);
		
		coin.setText("Coins earned: " + BattleInfo.coins);
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.backToCity();		
				backgroundMusic.release();
			}
		});

		// redundant, but only way to get the animation to work
		animation = AnimationUtils.loadAnimation(getActivity(), R.anim.result_slide_in_from_left);/*new TranslateAnimation(
				Animation.ABSOLUTE, -300.0f, Animation.ABSOLUTE, 0.0f,
				Animation.ABSOLUTE, 0.0f, Animation.ABSOLUTE, 0.0f);*/
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
					steps.startAnimation(animation2);
					steps.setVisibility(View.VISIBLE);
			}
		});
		
		animation2 = AnimationUtils.loadAnimation(getActivity(), R.anim.result_slide_in_from_left);;
		animation2.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
					distance.startAnimation(animation3);
					distance.setVisibility(View.VISIBLE);
			}
		});
		
		animation3 = AnimationUtils.loadAnimation(getActivity(), R.anim.result_slide_in_from_left);
		animation3.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
					calories.startAnimation(animation4);
					calories.setVisibility(View.VISIBLE);
			}
		});
		
		animation4 = AnimationUtils.loadAnimation(getActivity(), R.anim.result_slide_in_from_left);
		animation4.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
					button.startAnimation(animation5);
					button.setVisibility(View.VISIBLE);
			}
		});
		
		animation5 = AnimationUtils.loadAnimation(getActivity(), R.anim.result_slide_in_from_left);
		
		animateTextView(1, BattleInfo.coins);
		return view;
	}

	public void animateTextView(int start, int end) {
		final ValueAnimator valueAnimator = ValueAnimator.ofFloat((float)start, (float)end);
		valueAnimator.setDuration(1000);
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				coin.setText("Coins earned: " + valueAnimator.getAnimatedValue());
			}
		});
		valueAnimator.addListener(new AnimatorListenerAdapter() {
			
			@Override
		    public void onAnimationEnd(Animator anim) 
		    {
				time.startAnimation(animation);
				time.setVisibility(View.VISIBLE);
		    }
		});
		valueAnimator.start();
	}
}

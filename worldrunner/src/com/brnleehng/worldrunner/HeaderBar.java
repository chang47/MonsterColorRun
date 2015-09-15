package com.brnleehng.worldrunner;

import com.brnleehng.worldrunner.R;
import com.brnleehng.worldrunner.R.layout;

import DB.Model.Player;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

// The header to display your stats.
public class HeaderBar extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflate, ViewGroup container,
			Bundle savedInstance) {
		super.onCreate(savedInstance);
		View view = inflate.inflate(R.layout.header_activity, container, false);
		
		Player player = Hub.getPlayer();
		TextView rank = (TextView) view.findViewById(R.id.Rank);
		rank.setText("Rank: " + player.level);
		
		TextView gem = (TextView) view.findViewById(R.id.headerGem);
		gem.setText("Gems: " + player.gem);
		
		TextView coins = (TextView) view.findViewById(R.id.headerCoin);
		coins.setText("Coin: " + player.coin);
		
		ProgressBar exp = (ProgressBar) view.findViewById(R.id.expBar);
		exp.setProgress(player.exp);
		
		return view;
	}
	
}

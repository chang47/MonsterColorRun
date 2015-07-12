package com.brnleehng.worldrunner;

import java.util.ArrayList;

import DB.Model.Sticker;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewStickerDialog extends DialogFragment {
	public TextView attack;
	public TextView defense;
	public TextView speed;
	public TextView hp;
	public ProgressBar expBar;
	public TextView exp;
	public TextView level;
	public Sticker sticker;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.viewstickerdialog_activity, container, false);
        
        Button exitButton = (Button) view.findViewById(R.id.viewExitButton2);
        attack = (TextView) view.findViewById(R.id.stickerAtk);
        defense = (TextView) view.findViewById(R.id.stickerDef);
        speed = (TextView) view.findViewById(R.id.stickerSpd);
        hp = (TextView) view.findViewById(R.id.stickerHp);
        expBar = (ProgressBar) view.findViewById(R.id.expProgress);
        exp = (TextView) view.findViewById(R.id.stickerExp);
        level = (TextView) view.findViewById(R.id.stickerLevel);
        
        sticker = Hub.viewSticker;
        
        attack.setText("Atk: " + sticker.attack);
        defense.setText("Def: " + sticker.defense);
        speed.setText("Spd: " + sticker.speed);
        hp.setText("HP: " + sticker.hp);
        exp.setText("EXP to go: " + sticker.current_exp);
        level.setText("Level: " + sticker.current_level);
        
        expBar.setProgress(10);
        
        exitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
        });
		return view;
	}
	
}

package com.brnleehng.worldrunner;

import java.util.ArrayList;

import DB.Model.Monster;
import DB.Model.Sticker;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
	public Monster monster;
	public ImageView portrait;
	public ImageView headImage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.viewstickerdialog_activity, container, false);
        
        Button exitButton = (Button) view.findViewById(R.id.viewExitButton2);
        portrait = (ImageView) view.findViewById(R.id.monsterImage);
        headImage = (ImageView) view.findViewById(R.id.headImage);
        attack = (TextView) view.findViewById(R.id.stickerAtk);
        defense = (TextView) view.findViewById(R.id.stickerDef);
        speed = (TextView) view.findViewById(R.id.stickerSpd);
        hp = (TextView) view.findViewById(R.id.stickerHp);
        expBar = (ProgressBar) view.findViewById(R.id.expProgress);
        exp = (TextView) view.findViewById(R.id.stickerExp);
        level = (TextView) view.findViewById(R.id.stickerLevel);
        
        monster = Hub.viewSticker;
        
        attack.setText("Atk: " + monster.attack);
        defense.setText("Def: " + monster.defense);
        speed.setText("Spd: " + monster.speed);
        hp.setText("HP: " + monster.hp);
        exp.setText("EXP to go: " + monster.exp);
        level.setText("Level: " + monster.level);
        
        expBar.setProgress(10);
        
        int portraitResId = getResources().getIdentifier("monster" + monster.monsterId, "drawable", getActivity().getPackageName());
        Log.d("imageId2", "monster " + monster.name + " id is: " + portraitResId + " actual is: " + R.drawable.monster2);
        if (portraitResId != 0) {
        	portrait.setImageResource(portraitResId);
        } else {
        	portrait.setImageResource(R.drawable.land);
        }
        
        int headResId = getResources().getIdentifier("head" + monster.monsterId, "drawable", getActivity().getPackageName());
        if (headResId != 0) {
        	headImage.setImageResource(headResId);
        } else {
        	headImage.setImageResource(R.drawable.icon   );
        }
        
        
        
        exitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
        });
		return view;
	}
	
}

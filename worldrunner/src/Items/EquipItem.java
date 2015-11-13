package Items;

import java.util.ArrayList;

import util.TutorialTest;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import DB.Model.Equipment;
import DB.Model.Monster;
import DB.Model.Sticker;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Equip sticker and equipment fragment from the Item Hub
 */
public class EquipItem extends Fragment {
	private ArrayList<Monster> equippedMonsters;
	private SharedPreferences pref;
	private boolean firstTime;
	private boolean secondTime;
	private ImageView[] stickerViews;
	private TextView[] stickerLevelViews;
	private ShowcaseView equipMonster;
	private ShowcaseView showSpecific;
	private ShowcaseView showReturn;
	
	/**
	 * The method that's called to create a View
	 * Sets and creates everything on the view when selected.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.equippeditem_activity, container, false);

		equippedMonsters = Hub.equippedStickers;
		//equippedSticker = Hub.equippedStickers;
		stickerViews = new ImageView[5];
		stickerLevelViews = new TextView[5];

		stickerViews[0] = (ImageView) view.findViewById(R.id.Sticker1);
		stickerViews[1] = (ImageView) view.findViewById(R.id.Sticker2);
		stickerViews[2] = (ImageView) view.findViewById(R.id.Sticker3);
		stickerViews[3] = (ImageView) view.findViewById(R.id.Sticker4);
		stickerViews[4] = (ImageView) view.findViewById(R.id.Sticker5);
		
		stickerLevelViews[0] = (TextView) view.findViewById(R.id.monsterLevel1);
		stickerLevelViews[1] = (TextView) view.findViewById(R.id.monsterLevel2);
		stickerLevelViews[2] = (TextView) view.findViewById(R.id.monsterLevel3);
		stickerLevelViews[3] = (TextView) view.findViewById(R.id.monsterLevel4);
		stickerLevelViews[4] = (TextView) view.findViewById(R.id.monsterLevel5);
				
		// Adds in sticker image
		// TODO no need, but we need a default empty picture
		for (int i = 0; i < equippedMonsters.size(); i++) {
			if (equippedMonsters.get(i) != null) {
				int resId = getResources().getIdentifier("head" + equippedMonsters.get(i).monsterId, "drawable", getActivity().getPackageName());
	    		if (resId != 0) {
	    			stickerViews[i].setImageResource(resId);
	    			stickerLevelViews[i].setText("Lv." + equippedMonsters.get(i).level);
	    		}
			}
		}
	
		// no listeners for the pictures. You can see them, but you go somewhere else to select them
		for (int i = 0; i < stickerViews.length; i++) {
			final int temp = i;
			stickerViews[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// goes to EquipSticker
					
					// position is 1 index based
					Hub.equipSticker(temp + 1, equippedMonsters.get(temp));
				}
			});
		}
		pref = getActivity().getSharedPreferences("MonsterColorRun", Context.MODE_PRIVATE);
		firstTime = pref.getBoolean(getString(R.string.equipMonsters), true);
		//firstTime = TutorialTest.equipItem;
		secondTime = pref.getBoolean(getString(R.string.equipMonsterSecond), false);
		//secondTime = TutorialTest.equipItem2;
		
		view.post(new Runnable() {
			public void run() {
				if (firstTime) {
					
					equipMonster = new ShowcaseView.Builder(getActivity())
					.setTarget(new ViewTarget(R.id.Sticker1, getActivity()))
					.setContentText("Showcase View")
					.setContentText("Click edit party to edit your party")
					.build();
					
					
					
					equipMonster.overrideButtonClick(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							equipMonster.hide();
							commonHide(equipMonster);
							((ViewGroup)getActivity().getWindow().getDecorView()).removeView(equipMonster);
							
							showSpecific = new ShowcaseView.Builder(getActivity())
							.setTarget(new ViewTarget(R.id.Sticker1, getActivity()))
							.setContentText("Showcase View")
							.setContentText("Click edit party to edit your party")
							.build();
							
							showSpecific.hideButton();
							
							showSpecific.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									showSpecific.hide();
									commonHide(showSpecific);
									((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showSpecific);
									pref.edit().putBoolean(getString(R.string.equipMonsters), false).apply();
									TutorialTest.equipItem = false;
									Hub.equipSticker(1, equippedMonsters.get(1)); // is it right?
								}
							});
							
						}
					});
				} else if (secondTime) {
					showReturn = new ShowcaseView.Builder(getActivity())
					.setTarget(new ViewTarget(R.id.menuCity, getActivity()))
					.setContentText("Showcase View")
					.setContentText("Click edit party to edit your party")
					.build();
					
					showReturn.hideButton();
					
					showReturn.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							showReturn.hide();
							commonHide(showReturn);
							((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showReturn);
							pref.edit().putBoolean(getString(R.string.equipMonsterSecond), false).apply();
							pref.edit().putBoolean(getString(R.string.secondTime), true).apply();
							TutorialTest.equipItem2 = false;
							TutorialTest.cityHub2 = true;
							Hub.cityHub();
						}
					});;
				}
			}
		});
		
		return view;
	}
	
	public static void commonHide(ShowcaseView scv) {
		scv.setOnClickListener(null);
		scv.setOnShowcaseEventListener(null);
		scv.setOnTouchListener(null);
	}
}

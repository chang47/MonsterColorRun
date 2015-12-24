package Items;

import java.util.ArrayList;
import java.util.List;

import util.TutorialTest;
import DB.DBManager;
import DB.Model.Equipment;
import DB.Model.Monster;
import DB.Model.Sticker;
import Items.Adapters.StickerAdapter;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;
import com.brnleehng.worldrunner.RunLogDialog;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Fragment to equip stickers. Very similar to EquipEquipment
 */
public class EquipSticker extends Fragment {
	GridView gridview;	
	List<Monster> unequippedMonsters;
	ArrayList<Monster> equippedMonsters;
	Monster currentSticker;
	int currentPosition;
	private boolean firstTime;
	private SharedPreferences pref;
	private ShowcaseView showPickMonster;
	
	/**
	 * Creates the sticker equipment screen. Allows user to equip any stickers that
	 * aren't already equipped. Shows stickers via custom adapter
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.viewitems_activity, container, false);
		final DBManager db = new DBManager(getActivity());
		// get whatever category that the user decides to change
		currentSticker = Hub.currentSticker;
		currentPosition = Hub.currentStickerPosition;
		
		// TODO this is a ui thing we have to just organize the stickers 
		// Note: the first value is set to be null so that we can make the remove
		// button in the gridview.
		unequippedMonsters = Hub.unequippedMonster;
		// TODO temp that needs to be replace soon
		equippedMonsters = Hub.equippedStickers;
		//equippedSticker = Hub.equippedStickers;
		
		final StickerAdapter adapter = new StickerAdapter(getActivity(), R.layout.mylist, unequippedMonsters);
		gridview = (GridView) view.findViewById(R.id.viewGridView);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// gets the user's selected equipment
				Monster newMonster = adapter.getItem(position);
				
				// Adds new monster in, even if null
				equippedMonsters.set(currentPosition - 1, newMonster);
				
				// updates the monster that used to be equipped
				if (currentSticker != null) {
					//equippedMonsters.remove(currentSticker);
					unequippedMonsters.add(currentSticker);
					currentSticker.equipped = 0;
					currentSticker.position = 0;
					// TODO instead of relying on database for everything, store it all locally and then send it once it's ready
					db.updateSticker(currentSticker);
					Hub.stickerList.add(currentSticker);
				}
				
				// if the monster selected wasn't the un-equipped null
				// update the monster
				if (newMonster != null) {
					newMonster.equipped = 1;
					newMonster.position = currentPosition;
					//equippedMonsters.set(newMonster.position - 1, newMonster);
					unequippedMonsters.remove(newMonster);
					//Hub.equippedStickers = equippedMonsters;
					// TODO instead of relying on database for everything, store it all locally and then send it once it's ready
					db.updateSticker(newMonster);
					Hub.stickerList.remove(newMonster);
				}
				Hub.equipItems();
				//Goes back to equipped page.
			}
		});
		
		gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (adapter.getItem(position) != null) {
					Hub.viewSticker = adapter.getItem(position);
					ViewStickerDialog newFragment = new ViewStickerDialog();
					//newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.ViewStickerDialog);
					newFragment.setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light);
					newFragment.show(getFragmentManager(), "View Sticker");
				}
				return true;
			}
		});
		
		pref = getActivity().getSharedPreferences("MonsterColorRun", Context.MODE_PRIVATE);
		firstTime = pref.getBoolean(getString(R.string.chooseMonster), true);
		//firstTime = TutorialTest.equipsticker;
		view.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (firstTime) {
					showPickMonster = new ShowcaseView.Builder(getActivity())
					.setTarget(new ViewTarget(R.id.viewGridView, getActivity()))
					.setContentText("Showcase View")
					.setContentText("Click edit party to edit your party")
					.build();
					
					showPickMonster.overrideButtonClick(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							showPickMonster.hide();
							commonHide(showPickMonster);
							((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showPickMonster);
							pref.edit().putBoolean(getString(R.string.chooseMonster), false).apply();
							pref.edit().putBoolean(getString(R.string.equipMonsterSecond), true).apply();
							TutorialTest.equipsticker = false;
							TutorialTest.equipItem2 = true;
							
							
						}
					});

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

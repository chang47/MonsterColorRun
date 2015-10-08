package Items;

import java.util.ArrayList;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;

import DB.Model.Equipment;
import DB.Model.Monster;
import DB.Model.Sticker;
import android.app.Fragment;
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
	private ArrayList<Equipment> equippedEquipment;
	private ArrayList<Equipment> equipmentMapping;
	
	private ArrayList<Monster> equippedMonsters;
	private ArrayList<Monster> stickerMapping;
	
	private ImageView equipment1;
	private ImageView equipment2;
	private ImageView equipment3;
	private ImageView equipment4;
	private ImageView equipment5;
	private ImageView[] equipmentViews;
	
	private ImageView[] stickerViews;
	private TextView[] stickerLevelViews;
	
	
	/**
	 * The method that's called to create a View
	 * Sets and creates everything on the view when selected.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.equippeditem_activity, container, false);
		equippedEquipment = Hub.getEquippedEquipment();

		equippedMonsters = Hub.equippedStickers;
		//equippedSticker = Hub.equippedStickers;
		stickerViews = new ImageView[5];
		equipmentViews = new ImageView[5];
		stickerLevelViews = new TextView[5];
		
		// setup equipments
	/*	equipment1 = (ImageView) view.findViewById(R.id.Equipment1);
		equipment2 = (ImageView) view.findViewById(R.id.Equipment2);
		equipment3 = (ImageView) view.findViewById(R.id.Equipment3);
		equipment4 = (ImageView) view.findViewById(R.id.Equipment4);
		equipment5 = (ImageView) view.findViewById(R.id.Equipment5);
		*/

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
		return view;
	}
}

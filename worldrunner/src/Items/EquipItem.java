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
	
	private ImageView sticker1;
	private ImageView sticker2;
	private ImageView sticker3;
	private ImageView sticker4;
	private ImageView sticker5;
	private ImageView[] stickerViews;
	private Button equipStickerButton;
	
	/**
	 * The method that's called to create a View
	 * Sets and creates everything on the view when selected.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.equippeditem_activity, container, false);
		equippedEquipment = Hub.getEquippedEquipment();
		
		// TODO Needs to be changed to equipped sticker, this is a temp because
		// Justin is using the original
		equippedMonsters = Hub.equippedStickers;
		//equippedSticker = Hub.equippedStickers;
		stickerViews = new ImageView[5];
		equipmentViews = new ImageView[5];
		
		// setup equipments
		equipment1 = (ImageView) view.findViewById(R.id.Equipment1);
		equipment2 = (ImageView) view.findViewById(R.id.Equipment2);
		equipment3 = (ImageView) view.findViewById(R.id.Equipment3);
		equipment4 = (ImageView) view.findViewById(R.id.Equipment4);
		equipment5 = (ImageView) view.findViewById(R.id.Equipment5);
		
		// Sets default empty
		equipment1.setImageResource(R.drawable.colorworld);
		equipment2.setImageResource(R.drawable.colorworld);
		equipment3.setImageResource(R.drawable.colorworld);
		equipment4.setImageResource(R.drawable.colorworld);
		equipment5.setImageResource(R.drawable.colorworld);
		
		stickerViews[0] = (ImageView) view.findViewById(R.id.Sticker1);
		stickerViews[1] = (ImageView) view.findViewById(R.id.Sticker2);
		stickerViews[2] = (ImageView) view.findViewById(R.id.Sticker3);
		stickerViews[3] = (ImageView) view.findViewById(R.id.Sticker4);
		stickerViews[4] = (ImageView) view.findViewById(R.id.Sticker5);
				
		// initializes arrays
		equipmentMapping = new ArrayList<Equipment>();
		//stickerMapping = new ArrayList<Sticker>();
		
		//equipStickerButton = (Button) view.findViewById(R.id.equipStickerButton);
		
		// adds in starting values to the array
		for (int i = 0; i < 5; i++) {
			equipmentMapping.add(null);
			//stickerMapping.add(null);
		}
		
		// adds the equipment to their appropriate location
		for (Equipment equipment : equippedEquipment) {
			equipmentMapping.set(equipment.getCategory() - 1, equipment);
			if (equipment.getCategory() == 1) {
				equipment1.setImageResource(R.drawable.ic_launcher);
			} else if (equipment.getCategory() == 2) {
				equipment2.setImageResource(R.drawable.ic_launcher);
			} else if (equipment.getCategory() == 3) {
				equipment3.setImageResource(R.drawable.ic_launcher);
			} else if (equipment.getCategory() == 4) {
				equipment4.setImageResource(R.drawable.ic_launcher);
			} else {
				equipment5.setImageResource(R.drawable.ic_launcher);
			}
		}
		
		// Adds in sticker image
		// TODO no need, but we need a default empty picture
		for (int i = 0; i < equippedMonsters.size(); i++) {
			if (equippedMonsters.get(i) != null) {
				int resId = getResources().getIdentifier("head" + equippedMonsters.get(i).monsterId, "drawable", getActivity().getPackageName());
	    		if (resId != 0) {
	    			stickerViews[i].setImageResource(resId);
	    		}
			}
		}
		
		equipment1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// calls the hub to change equipment on slot 1
				Hub.equipEquipment(1, equipmentMapping.get(0));
			}
		});
		
		equipment2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.equipEquipment(2, equipmentMapping.get(1));
			}
		});
		
		equipment3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.equipEquipment(3, equipmentMapping.get(2));
			}
		});
		
		equipment4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.equipEquipment(4, equipmentMapping.get(3));
			}
		});
		
		equipment5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.equipEquipment(5, equipmentMapping.get(4));
			}
		});
/*		
		equipStickerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.equipNewSticker();	
			}
		});
		
		*/
	
		// no listeners for the pictures. You can see them, but you go somewhere else to select them
		for (int i = 0; i < stickerViews.length; i++) {
			final int temp = i;
			stickerViews[i].setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// goes to EquipSticker
					Hub.equipSticker(temp + 1, equippedMonsters.get(temp));
				}
			});
		}
		return view;
	}
}

package Items;

import java.util.ArrayList;

import DB.DBManager;
import DB.Model.Equipment;
import Items.Adapters.EquipmentAdapter;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;

/**
 * Equip equipments. Refer to Equip Sticker for almost exact same documentation.
 *
 */
public class EquipEquipment extends Fragment {
	GridView gridview;	
	ArrayList<Equipment> categoryEquipment;
	ArrayList<Equipment> equippedEquipment;
	Equipment currentEquipment;
	int currentCategory;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.viewitems_activity, container, false);
		final DBManager db = new DBManager(getActivity());
		// get whatever category that the user decides to change
		currentEquipment = Hub.getCurrentEquipment();
		currentCategory = Hub.getCurrentCategory();
		categoryEquipment = db.getEquipmentCategory(currentCategory);
		equippedEquipment = Hub.getEquippedEquipment();
		final EquipmentAdapter adapter = new EquipmentAdapter(getActivity(), R.layout.mylist, categoryEquipment);
		gridview = (GridView) view.findViewById(R.id.viewGridView);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// gets the user's selected equipment
				Equipment newEquipment = adapter.getItem(position);
				
				// updates the equipment list
				if (currentEquipment != null) {
					// maybe instead of removing the list itself, just call set and remove methods?
					equippedEquipment.remove(currentEquipment);
					currentEquipment.setEquipped("0");
					
					// instead of relying on database for everything, maybe store it all locally and then send it once it's ready
					db.updateEquipment(currentEquipment);
				}
				
				newEquipment.setEquipped("1");
				equippedEquipment.add(newEquipment);
				Hub.setEquippedEquipment(equippedEquipment);
				// TODO - Still need to save everything to the DB, simple loop update
				db.updateEquipment(newEquipment);
				Hub.equipItems();
				//Goes back to equipped page.
			}
		});
		return view;
	}  
}

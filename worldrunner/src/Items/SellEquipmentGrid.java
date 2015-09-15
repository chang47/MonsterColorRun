package Items;

import java.util.ArrayList;
import java.util.List;

import DB.DBManager;
import DB.EquipmentManager;
import DB.Model.Equipment;
import Items.Adapters.EquipmentAdapter;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;

/**
 * Not used. Equipments have been discontinued
 * @author JoshDesktop
 *
 */
public class SellEquipmentGrid extends Fragment {
	GridView gridview;
	
	ArrayList<Equipment> list;
	private EquipmentAdapter adapter;
	private ArrayList<Equipment> sellList;
	private DBManager db;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.sellequipmentgrid_activity, container, false);
		list = Hub.getEquipment();
		db = new DBManager(getActivity());
		sellList = new ArrayList<Equipment>();
		adapter = new EquipmentAdapter(getActivity(), R.layout.mylist, list);
		
		gridview = (GridView) view.findViewById(R.id.gridview);
		gridview.setAdapter(adapter);
		gridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				boolean checked = adapter.toggleSelection(position);
				//@TODO add the item id into the sell list and then
				//erase the equipments from the database as it gets erased
				Equipment equipment = adapter.getItem(position);			
				if (checked) {
					sellList.remove(equipment);
					view.setBackgroundColor(Color.TRANSPARENT);
				} else {
					sellList.add(equipment);
					view.setBackgroundColor(Color.BLUE);
				}
			}
		});
		
		
		Button sellBtn = (Button) view.findViewById(R.id.sellBtn2);
		
		sellBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				SparseBooleanArray selected = adapter.getSelectedIds();
				for (int i = (selected.size() - 1); i >= 0; i--) {
					if (selected.valueAt(i)) {
						Equipment selecteditem = adapter.getItem(selected.keyAt(i));
						// Remove selected items following the ids
						adapter.remove(selecteditem);
						db.deleteEquipment(selecteditem);
					}
				}
				adapter.removeSelection();
				for (int i = 0; i < gridview.getCount(); i++) {
					gridview.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);;
				}
			}
		});
		
		return view;
	}  
}

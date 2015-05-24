package Items;

import java.util.ArrayList;
import java.util.List;

import Items.Adapters.EquipmentViewAdapter;
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
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.brnleehng.worldrunner.R;
/*
 * OUTDATED, this is in list form, LOOK AT SellEquipmentGrid
 */
public class SellEquipment extends Fragment {
	ListView listview;
	String[] itemname ={
			 "Safari",
			 "Camera",
			 "Global"
			 };
	
	ArrayList<String> list;
	private EquipmentViewAdapter adapter;
	private ArrayList<String> sellList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.sellequipment_activity, container, false);
		list = new ArrayList<String>();
		for (String item : itemname) {
			list.add(item);
		}
		
		
		sellList = new ArrayList<String>();
		adapter = new EquipmentViewAdapter(getActivity(), itemname, list);
		
		Button sellBtn = (Button) view.findViewById(R.id.sellBtn);
		
		sellBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			/*	SparseBooleanArray itemPos = listview.getCheckedItemPositions();
				int itemCount = listview.getCount();
				for (int i = itemCount - 1; i >= 0; i--) {
					if (itemPos.get(i)) {
						adapter.remove(list.get(i));
					}
				}
				listview.clearChoices();
				adapter.notifyDataSetChanged();*/
				
				for (String item: sellList) {
					adapter.remove(item);
				}
				sellList.clear();
				listview.clearChoices();
				adapter.notifyDataSetChanged();
				
			}
		});
		listview = (ListView) view.findViewById(R.id.list); //was list
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String item = adapter.getItem( position );
				//String SelectedItem = itemname[position];
				if (!sellList.contains(item)) {
					listview.setItemChecked(position, true);
					view.setBackgroundColor(Color.BLUE);
					sellList.add(item);
				} else {
					listview.setItemChecked(position, false);
					view.setBackgroundColor(Color.TRANSPARENT);
					sellList.remove(item);
				}
				//list.remove(position);
				//adapter.notifyDataSetChanged();
				//Toast.makeText(getActivity().  getApplicationContext(), SelectedItem, Toast.LENGTH_SHORT).show();
			}
		});
		return view;
	}  
}

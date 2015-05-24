package Items;

import java.util.ArrayList;
import java.util.List;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;

import DB.Model.Equipment;
import Items.Adapters.EquipmentAdapter;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class ViewEquipment extends Fragment {
	GridView listview;	
	ArrayList<Equipment> equipments;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.viewitems_activity, container, false);
		equipments = Hub.getEquipment();
		EquipmentAdapter adapter = new EquipmentAdapter(getActivity(), R.layout.mylist, equipments);
		//Toast.makeText(getActivity().getApplicationContext(), " " + equipments.size(), Toast.LENGTH_SHORT).show();
		listview = (GridView) view.findViewById(R.id.viewGridView);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String SelectedItem = equipments.get(position).getName();
				Toast.makeText(getActivity().getApplicationContext(), SelectedItem, Toast.LENGTH_SHORT).show();
			}
		});
		return view;
	}  
}

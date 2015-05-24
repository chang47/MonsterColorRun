package Items.Adapters;

import java.util.ArrayList;
import java.util.List;

import com.brnleehng.worldrunner.R;

import android.app.Activity;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Not used?
 */
public class ImageAdapter extends ArrayAdapter<String> {
 
	Context context;
	LayoutInflater inflater;
	List<String> list;
	private SparseBooleanArray mSelectedItemsIds;
 
	public ImageAdapter(Context context, int resourceID, ArrayList<String> list) {
		super(context, resourceID, list);
		// TODO Auto-generated constructor stub
		mSelectedItemsIds = new SparseBooleanArray();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	private class ViewHolder {
		TextView txtTitle;
		ImageView imageView;
		TextView lvl;
		TextView spd;
		TextView rch;
	}
	
 	@Override
 	public View getView(int position,View view,ViewGroup parent) {
 		final ViewHolder holder;
 		if (view == null) {
 			holder = new ViewHolder();
 			view = inflater.inflate(R.layout.mylist, null);
 			
 			holder.txtTitle = (TextView) view.findViewById(R.id.item);
 			holder.imageView = (ImageView) view.findViewById(R.id.icon);
 			holder.lvl = (TextView) view.findViewById(R.id.textView1);
 			holder.spd = (TextView) view.findViewById(R.id.textView2);
 			holder.rch = (TextView) view.findViewById(R.id.textView3);
 			view.setTag(holder);
 		} else {
 			holder = (ViewHolder) view.getTag();
 		}
		 
		holder.txtTitle.setText(list.get(position));
		//@todo get data from array list or 2D array
		holder.imageView.setImageResource(R.drawable.ic_launcher); // have the pictured ordered correctly
		holder.lvl.setText("lvl: " + list.get(position));
		holder.spd.setText("spd: " + list.get(position));
		holder.rch.setText("rch: " + list.get(position));
		return view;
 	}
 	
	@Override
	public void remove(String object) {
		list.remove(object);
		notifyDataSetChanged();
	}
 
	public List<String> getArray() {
		return list;
	}
 
	public boolean toggleSelection(int position) {
		return selectView(position, !mSelectedItemsIds.get(position));
	}
	
	public void removeSelection() {
		mSelectedItemsIds = new SparseBooleanArray();
		notifyDataSetChanged();
	}
 	
	public boolean selectView(int position, boolean value) {
		if (value) {
			mSelectedItemsIds.put(position, value);
			notifyDataSetChanged();
			return false;
		} else {
			mSelectedItemsIds.delete(position);
			notifyDataSetChanged();
			return true;
		}
	}
	
	public SparseBooleanArray getSelectedIds() {
		return mSelectedItemsIds;
	}
	
}
package Items.Adapters;

import java.util.ArrayList;
import java.util.List;

import com.brnleehng.worldrunner.R;

import DB.Model.Equipment;
import DB.Model.Sticker;
import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
/**
 * Adapter used to show Stickers in a grid view. Used for viewing and equipping
 * stickers. Similar to equipmentAdapter
 */
public class StickerAdapter extends ArrayAdapter<Sticker> {
 
	Context context;
	LayoutInflater inflater;
	ArrayList<Sticker> list;
	private SparseBooleanArray mSelectedItemsIds;
 
	// Constructor that takes in the list that is being used to read the 
	// whole grid
	public StickerAdapter(Context context, int resourceID, ArrayList<Sticker> list) {
		super(context, resourceID, list);
		mSelectedItemsIds = new SparseBooleanArray();
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	// private class to hold the Sticker. It's more efficient
	// to store everything inside a class as oppose to calling
	// them out individually
	private class ViewHolder {
		TextView txtTitle;
		ImageView imageView;
		TextView lvl;
		TextView spd;
		TextView rch;
	}
	
 	@Override
 	// Creates the actual object that the user sees
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
		 
		// checks for the null remove sticker case
		if (getItem(position) == null) {
			holder.imageView.setImageResource(R.drawable.icon);
		} else {
			holder.txtTitle.setText(list.get(position).name);
			holder.imageView.setImageResource(R.drawable.ic_launcher); // have the pictured ordered correctly
			holder.lvl.setText("lvl: " + list.get(position).current_level);
			holder.spd.setText("spd: " + list.get(position).current_speed);
			holder.rch.setText("rch: " + list.get(position).current_reach);
			if (mSelectedItemsIds.get(position)) {
				view.setBackgroundColor(Color.BLUE);
			} else {
				view.setBackgroundColor(Color.TRANSPARENT);
			}
		}
		return view;
 	}
 	
	@Override
	// removes the sticker from the grid view list. Used for selling
	public void remove(Sticker object) {
		list.remove(object);
		notifyDataSetChanged();
	}
 
	// Gets the list that represents the list of stickers
	public ArrayList<Sticker> getArray() {
		return list;
	}
 
	// toggles the selected item. Selects or un-selects them based off of it's
	// position in the list
	public boolean toggleSelection(int position) {
		return selectView(position, !mSelectedItemsIds.get(position));
	}
	
	// Removes all selected items in the list
	public void removeSelection() {
		mSelectedItemsIds = new SparseBooleanArray();
		notifyDataSetChanged();
	}
 	
	/**
	 * Selects or de-selects the items in the list
	 * @param position - the item to be selected or deselected
	 * @param value - whether the item has not been selected already or not
	 * @return false if added, true if removed
	 */
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
	
	/**
	 * Returns an array of items that were selected by the user
	 * @return list of items selected by the user
	 */
	public SparseBooleanArray getSelectedIds() {
		return mSelectedItemsIds;
	}
	
}
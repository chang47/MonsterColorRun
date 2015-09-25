package util;

import com.brnleehng.worldrunner.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class WalkThrough extends Activity {
	private static final int MAX_VIEWS = 3;
	
	ViewPager mViewPager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.walkthrough_activity);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new WalkthroughPageAdapter());
        //mViewPager.setOnPageChangeListener(new WalkthroughPageChangeListener());
    }
    
    class WalkthroughPageAdapter extends PagerAdapter {
    	
    	@Override
    	public int getCount() {
    		return MAX_VIEWS;
    	}
    	
    	@Override
    	public boolean isViewFromObject(View view, Object object) {
    		return view == (View) object;
    	}
    	
    	@Override
    	public Object instantiateItem(View container, int position) {
    		 Log.e("walkthrough", "instantiateItem(" + position + ");");
             LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             View imageViewContainer = inflater.inflate(R.layout.walkthrough_single_view, null);
             ImageView imageView = (ImageView) imageViewContainer.findViewById(R.id.image_view);

             switch(position) {
             case 0:
                 imageView.setImageResource(R.drawable.background);
                 break;

             case 1:
                 imageView.setImageResource(R.drawable.background0);
                 break;

             case 2:
                 imageView.setImageResource(R.drawable.background1);
                 break;
             }

             ((ViewPager) container).addView(imageViewContainer, 0);
             return imageViewContainer;
    	}
    	
    	@Override
    	public void destroyItem(View container, int position, Object object) {
    		((ViewPager) container).removeView((View) object);
    	}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}
    }
    
    
}

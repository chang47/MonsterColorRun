package util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

public class SlidingLinearLayout extends LinearLayout {
	
	private float xFraction = 0;
	private float yFraction = 0;
	
    public SlidingLinearLayout(Context context) {
        super(context);
    }

    public SlidingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private ViewTreeObserver.OnPreDrawListener preDrawListener = null;
    
    public void setXFraction(float fraction) {
    	this.xFraction = fraction;
    	if (getWidth() == 0) {
    		if (preDrawListener == null) {
    			preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
					
					@Override
					public boolean onPreDraw() {
						getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
						setXFraction(xFraction);
						return true;
					}
				};
				getViewTreeObserver().addOnPreDrawListener(preDrawListener);
    		}
    		return;
    	}
    	float translationX = getWidth() * fraction;
    	setTranslationX(translationX);
    }
    
    public float getXFraction() {
    	return this.xFraction;
    }
    
    public void setYFraction(float fraction) {

        this.yFraction = fraction;

        if (getHeight() == 0) {
            if (preDrawListener == null) {
                preDrawListener = new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        getViewTreeObserver().removeOnPreDrawListener(preDrawListener);
                        setYFraction(yFraction);
                        return true;
                    }
                };
                getViewTreeObserver().addOnPreDrawListener(preDrawListener);
            }
            return;
        }

        float translationY = getHeight() * fraction;
        setTranslationY(translationY);
    }

    public float getYFraction() {
        return this.yFraction;
    }

}

package com.grant.privately.file.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/***
 * 正方形布局
 * 
 */
public class SquareLayout extends RelativeLayout {
	public static final int BASE_HEIGHT = 1;
	public static final int BASE_WIDTH= 2;
	private int base = BASE_WIDTH;
    public SquareLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
 
    public SquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
 
    public SquareLayout(Context context) {
        super(context);
    }
    
    public void setBaseOn(int base){
    	this.base = base;
    	requestLayout();
    }
 
    @SuppressWarnings("unused")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // For simple implementation, or internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view. We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
 
        // Children are just made to fill our space.
        int childWidthSize = getMeasuredWidth();
        int childHeightSize = getMeasuredHeight();
        //高度和宽度一样
        if(base == BASE_WIDTH){
        	heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        }else{
        	heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}


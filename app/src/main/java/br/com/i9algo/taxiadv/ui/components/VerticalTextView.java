package br.com.i9algo.taxiadv.ui.components;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class VerticalTextView extends TextView {

    private boolean topDown;

    public VerticalTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    	initialize();
    }

    public VerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    	initialize();
    }

    public VerticalTextView(Context context) {
    	super(context);
    	initialize();
    }
    
    private void initialize() {
    	final int gravity = getGravity();
		if(Gravity.isVertical(gravity) && (gravity&Gravity.VERTICAL_GRAVITY_MASK) == Gravity.BOTTOM) {
			setGravity((gravity&Gravity.HORIZONTAL_GRAVITY_MASK) | Gravity.TOP);
			topDown = true;
		}else
			topDown = false;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
       super.onMeasure(heightMeasureSpec, widthMeasureSpec);
       setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
    	TextPaint textPaint = getPaint(); 
		textPaint.setColor(getCurrentTextColor());
		textPaint.drawableState = getDrawableState();
		
		canvas.save();
		
		if(topDown){
			canvas.translate(getWidth(), 0);
			canvas.rotate(90);
		}else {
			canvas.translate(0, getHeight());
			canvas.rotate(-90);
		}
		
		canvas.translate(getCompoundPaddingLeft(), getExtendedPaddingTop());
		getLayout().draw(canvas);
		canvas.restore();
    }
}
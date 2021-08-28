package br.com.i9algo.taxiadv.ui.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import br.com.i9algo.taxiadv.R;
import br.com.i9algo.taxiadv.libs.utilcode.util.ImageUtils;

public class ImageTextView extends LinearLayout {

	private TextView mTextView;

	private ImageView mImageView;
	private AttributeSet mImgAttrs = null;
	private int mImgDefStyleAttr;

	private int mImageSize = 50;
	
	public ImageTextView(Context context, int sizeImage, @Nullable AttributeSet imgAttrs, int imgDefStyleAttr) {
		super(context);
		this.mImageSize = sizeImage;
		this.mImgAttrs = imgAttrs;
		this.mImgDefStyleAttr = imgDefStyleAttr;
		instanciate(null, 0);
	}

	public ImageTextView(Context context, int sizeImage) {
		super(context);
		this.mImageSize = sizeImage;
		instanciate(null, 0);
	}
	
	public ImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		String text = attrs.getAttributeValue(null, "text");
		int resId = attrs.getAttributeResourceValue(null, "image_resource", 0);
		this.mImageSize = attrs.getAttributeIntValue(null, "image_size", 0);
		instanciate(text, resId);
	}
	
	private void instanciate(String text, int resId) {
		LayoutParams l = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.setLayoutParams(l);
		this.setOrientation(LinearLayout.VERTICAL);
		//this.setGravity(Gravity.CENTER);

		// Get the image view
		this.mImageView = new ImageView(getContext(), this.mImgAttrs, this.mImgDefStyleAttr);
		LayoutParams ilp = new LayoutParams(mImageSize, mImageSize);
		//ilp.weight = 1;
		ilp.gravity = Gravity.CENTER;
		this.mImageView.setLayoutParams(ilp);
		this.mImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		this.mImageView.setColorFilter(this.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);

		// Create the caption view
		this.mTextView = new TextView(getContext());
		LayoutParams clp = new LayoutParams(160, LayoutParams.WRAP_CONTENT);
		//clp.weight = 0;
		clp.gravity = Gravity.CENTER_HORIZONTAL;
		clp.topMargin = 10;
		//this.mTextView.setLines(1);
		this.mTextView.setSingleLine(false);
		this.mTextView.setLayoutParams(clp);
		this.mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
		this.mTextView.setTextAppearance(getContext(), R.style.textLargeWhite);
		
		// Add views to the linear layout
		this.addView(this.mImageView);
		this.addView(this.mTextView);
		
		if (text != null)
			this.mTextView.setText(text);
		
		if (resId != 0)
			this.mImageView.setImageResource(resId);
	}

	public ImageView getImageView() {
		return this.mImageView;
	}
	
	public void setIconBackgroundResource(@DrawableRes int resid) {
		this.mImageView.setBackgroundResource(resid);
	}
	
	public void setImageResource(@DrawableRes int resid) {
		this.mImageView.setImageResource(resid);
	}
	
	public void setImageDrawable(@NonNull Drawable drawable) {
		this.mImageView.setImageDrawable(drawable);
	}
	
	public void setImageBitmap(Bitmap bm) {
		this.mImageView.setImageBitmap(bm);
	}
	
	public void setImageBitmapRounded(Bitmap bm) {
		setImageBitmapRounded(bm, mImageSize);
	}
	
	public void setImageBitmapRounded(Bitmap bm, int radius) {
		Bitmap bitmap = ImageUtils.toRoundCorner(bm, radius);
		this.mImageView.setImageBitmap(bitmap);
	}
	
	public void setText(CharSequence text) {
		this.mTextView.setText(text);
	}
	
	public void setText(int resid) {
		this.mTextView.setText(resid);
	}
	
	public void setTextColor(int color) {
		this.mTextView.setTextColor(color);
	}
	
	public void setBackgroundTextView(int resid, int horizontalPadding, int verticalPadding) {
		this.mTextView.setBackgroundResource(resid);
		this.mTextView.setPadding(horizontalPadding, verticalPadding, horizontalPadding, verticalPadding);
		this.mTextView.setWidth(mImageSize);
	}
}

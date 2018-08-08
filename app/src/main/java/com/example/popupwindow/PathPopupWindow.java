package com.example.popupwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PathPopupWindow extends PopupWindow {
	
	private final int CONST_R = 100;//圆半径
	private final int CONST_ICON_SIZE = 35;

	private Context context;
	
	private FrameLayout frameLayout;
	
	private ImageView imageView;
	
	private View view_circle;
	
	private List<View> itemViews = new ArrayList<View>();
	
	private int itempadding = 0;
	
	private OnPathItemClickListener mOnPathItemClickListener;
	
	public void setOnPathItemClickListener(OnPathItemClickListener mOnPathItemClickListener){
		this.mOnPathItemClickListener = mOnPathItemClickListener;
	}
	
	protected PathPopupWindow() {
	}
	
	public PathPopupWindow(Context context, List<PathItem> items) {
		this(context,items, Color.BLACK,12, Color.TRANSPARENT);
	}
	
	public PathPopupWindow(Context context, List<PathItem> items, int textColor, int textSize, int itembgResId) {
		super(context);
		this.context = context;
		setFocusable(true);
		itempadding = DensityUtil.dip2px(context, 8);
		int itemWidth = DensityUtil.dip2px(context,CONST_ICON_SIZE + textSize + 4) + itempadding * 2;
		int width = DensityUtil.dip2px(context, CONST_R * 2 + 30) + itemWidth; 
		int height = DensityUtil.dip2px(context, CONST_R * 2 + 30 + textSize) + itemWidth;
		setWidth(width);
		setHeight(height);
		setBackgroundDrawable(new ColorDrawable());
		frameLayout = new FrameLayout(context);
		view_circle = new View(context);
		view_circle.setBackgroundResource(R.drawable.bg_circle);
		int Radius = DensityUtil.dip2px(context, CONST_R);
		int pading = (width - Radius * 2)/2;
		FrameLayout.LayoutParams view_flp = new FrameLayout.LayoutParams(Radius * 2, Radius * 2);
		view_flp.leftMargin = view_flp.topMargin = (width - Radius * 2) / 2;
		frameLayout.addView(view_circle, view_flp);
		for(int i = 0;i<items.size();i++){
			LinearLayout linearLayout = createItem(i,items.get(i),textColor,textSize,itembgResId);
			int x = (int) (Math.cos(2 * Math.PI / items.size() * i + Math.PI / 2) * Radius);
			int y = (int) (Math.sin(2 * Math.PI / items.size() * i + Math.PI / 2) * Radius);
			FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(itemWidth,itemWidth);
			flp.leftMargin = Radius + x + pading - itemWidth / 2;
			flp.topMargin = Radius - y + pading - itemWidth / 2;
			frameLayout.addView(linearLayout, flp);
			itemViews.add(linearLayout);
		}
		imageView = new ImageView(context);
		imageView.setImageResource(R.drawable.tianmao);
		FrameLayout.LayoutParams iv_flp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		iv_flp.gravity = Gravity.CENTER;
		iv_flp.topMargin = -DensityUtil.dip2px(context, textSize ) / 2;
		frameLayout.addView(imageView,iv_flp);
		setContentView(frameLayout);
		frameLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	
	public void show(View view){
		showAtLocation(view, Gravity.CENTER, 0, 0);
		{
			RotateAnimation rotate = new RotateAnimation(360, 0,
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotate.setDuration(500);
			rotate.setFillAfter(true);
			imageView.startAnimation(rotate);
		}
		{
			ScaleAnimation scaleAnimation = new ScaleAnimation(0.3f, 1f, 0.3f, 1f, Animation.RELATIVE_TO_SELF,0.5f,
					Animation.RELATIVE_TO_SELF,0.5f);
			scaleAnimation.setDuration(300);
			scaleAnimation.setFillAfter(true);
			view_circle.startAnimation(scaleAnimation);
		}
		int Radius = DensityUtil.dip2px(context, CONST_R);
		for(int i = 0;i<itemViews.size();i++){
			int x = (int) (Math.cos(2 * Math.PI / itemViews.size() * i + Math.PI / 2) * Radius);
			int y = (int) (Math.sin(2 * Math.PI / itemViews.size() * i + Math.PI / 2) * Radius);
			TranslateAnimation translateAnimation = new TranslateAnimation(-x, 0F, y, 0F);
			translateAnimation.setDuration(500);
			translateAnimation.setFillAfter(true);
			translateAnimation.setInterpolator(new OvershootInterpolator(2F));
			itemViews.get(i).startAnimation(translateAnimation);
		}

	}
	
	private boolean isdimissing = false;
	
	@Override
	public void dismiss() {
		if(isdimissing || !isShowing()){
			return;
		}
		isdimissing = true;
		{
			RotateAnimation rotate = new RotateAnimation(0, 360,
					Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			rotate.setDuration(500);
			rotate.setFillAfter(false);
			imageView.startAnimation(rotate);
		}
		{
			ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.3f, 1f, 0.3f, Animation.RELATIVE_TO_SELF,0.5f,
					Animation.RELATIVE_TO_SELF,0.5f);
			scaleAnimation.setDuration(500);
			scaleAnimation.setFillAfter(false);
			view_circle.startAnimation(scaleAnimation);
		}
		int Radius = DensityUtil.dip2px(context, CONST_R);
		for(int i = 0;i<itemViews.size();i++){
			int x = (int) (Math.cos(2 * Math.PI / itemViews.size() * i + Math.PI / 2) * Radius);
			int y = (int) (Math.sin(2 * Math.PI / itemViews.size() * i + Math.PI / 2) * Radius);
			TranslateAnimation translateAnimation = new TranslateAnimation(0F, -x, 0F, y);
			translateAnimation.setDuration(300);
			translateAnimation.setFillAfter(false);
			if(i == itemViews.size() - 1){
				translateAnimation.setAnimationListener(new AnimationListener() {
					public void onAnimationStart(Animation animation) {
					}
					public void onAnimationRepeat(Animation animation) {
					}
					public void onAnimationEnd(Animation animation) {
						PathPopupWindow.super.dismiss();
						isdimissing = false;
					}
				});
			}
			itemViews.get(i).startAnimation(translateAnimation);
		}

	}
	
	private LinearLayout createItem(final int position, final PathItem item, int textColor, int textSize, int itembgResId){
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setPadding(itempadding, itempadding, itempadding, itempadding);
		if(item.backgroundResId != -1){
			linearLayout.setBackgroundResource(item.backgroundResId);
		}else if(itembgResId != -1){
			linearLayout.setBackgroundResource(itembgResId);
		}else{
			linearLayout.setBackgroundColor(Color.TRANSPARENT);
		}
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		final ImageView imageView = new ImageView(context);
		if(item.imageResId != -1){
			imageView.setImageResource(item.imageResId);
			ColorMatrix cMatrix = new ColorMatrix();
			cMatrix.set(new float[]{1, 0, 0, 0, 255, 0, 1, 0, 0, 255, // 改变亮度
					0, 0, 1, 0, 255, 0, 0, 0, 1, 0});
			imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));
		}else{
			try {
				imageView.setImageDrawable(context.getPackageManager().getApplicationIcon(context.getPackageName()));
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		LinearLayout.LayoutParams llp_imageview = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, CONST_ICON_SIZE),
				DensityUtil.dip2px(context, CONST_ICON_SIZE));
		llp_imageview.gravity = Gravity.CENTER_HORIZONTAL;
		linearLayout.addView(imageView, llp_imageview);
		LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		llp.gravity = Gravity.CENTER_HORIZONTAL;
		TextView textView = new TextView(context);
		textView.setTextColor(textColor);
		textView.setTextSize(textSize);
		textView.setText(item.name);
		linearLayout.addView(textView, llp);
		linearLayout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(mOnPathItemClickListener != null){
					mOnPathItemClickListener.onItemClick(position, item);
				}
			}
		});
		return linearLayout;
	}
	
	public interface OnPathItemClickListener{
		void onItemClick(int position, PathItem item);
	}
	
}

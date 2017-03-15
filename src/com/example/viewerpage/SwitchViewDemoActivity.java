package com.example.viewerpage;

import com.example.viewerpage.scroll.MyScrollLayout;
import com.example.viewerpage.scroll.OnViewChangeListener;
import com.zhy.sample_circlemenu.CircleActivity;
import com.zhy.sample_circlemenu.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class SwitchViewDemoActivity extends Activity implements
		OnViewChangeListener, OnClickListener {

	private MyScrollLayout mScrollLayout;

	private ImageView[] mImageViews;

	private int mViewCount;

	private int mCurSel;
	
	private Button button;
	
	/** Activity对象 **/
	public static Activity MY_ACTIVITY;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		button=(Button) findViewById(R.id.button);
		MY_ACTIVITY = this;
		
		init();
	}

	private void init() {
		mScrollLayout = (MyScrollLayout) findViewById(R.id.ScrollLayout);

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.llayout);

		mViewCount = mScrollLayout.getChildCount();
		mImageViews = new ImageView[mViewCount];

		for (int i = 0; i < mViewCount; i++) {
			mImageViews[i] = (ImageView) linearLayout.getChildAt(i);
			mImageViews[i].setEnabled(true);
			mImageViews[i].setOnClickListener(this);
			mImageViews[i].setTag(i);
		}

		mScrollLayout.setPageSize(mImageViews.length);
		mCurSel = 0;
		mImageViews[mCurSel].setEnabled(false);

		mScrollLayout.SetOnViewChangeListener(this);
	}

	private void setCurPoint(int index) {
		if (index < 0 || index > mViewCount - 1 || mCurSel == index) {
			return;
		}
		
		mImageViews[mCurSel].setEnabled(true);
		mImageViews[index].setEnabled(false);
		mScrollLayout.setPosition(index);
		mCurSel = index;
		if (mCurSel==mViewCount-1)
		{
			button.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View arg0)
				{
					// TODO Auto-generated method stub
					Intent intent = new Intent(SwitchViewDemoActivity.this, CircleActivity.class);
					startActivity(intent);
				}
			});
		}
	}

	@Override
	public void OnViewChange(int view) {
		setCurPoint(view);
	}

	@Override
	public void onClick(View v) {
		int pos = (Integer) (v.getTag());
		setCurPoint(pos);
		mScrollLayout.snapToScreen(pos);
	}
}
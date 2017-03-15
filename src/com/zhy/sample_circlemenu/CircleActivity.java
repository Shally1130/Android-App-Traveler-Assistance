package com.zhy.sample_circlemenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.example.contactus.ContactUsActivity;
import com.example.flashmorse.FlashMorseMainActivity;
import com.example.newWeatherPart.WeatherPartActivity;
import com.juhe.petrolstation.activity.MainActivity;
import com.zhy.view.CircleMenuLayout;
import com.zhy.view.CircleMenuLayout.OnMenuItemClickListener;
/**
 * <pre>
 * @author zhy 
 * http://blog.csdn.net/lmj623565791/article/details/43131133
 * </pre>
 */
public class CircleActivity extends Activity
{

	private CircleMenuLayout mCircleMenuLayout;
	Intent intent;

	private String[] mItemTexts = new String[] { "摩尔斯电码","天气预报 ", "加油讯息", "联系我们"};
	private int[] mItemImgs = new int[] { R.drawable.flash_light, R.drawable.cloud, R.drawable.gas_pump,
			 R.drawable.phone};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		//自已切换布局文件看效果
		setContentView(R.layout.activity_main02);
//		setContentView(R.layout.activity_main);

		mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
		mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
		
		

		mCircleMenuLayout.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			
			@Override
			public void itemClick(View view, int pos)
			{
				switch (mItemTexts[pos])
				{
				case "天气预报 ":
					intent = new Intent(CircleActivity.this, WeatherPartActivity.class);
					Toast.makeText(getApplicationContext(), "天气预报", Toast.LENGTH_LONG).show();
					break;
				case "摩尔斯电码":
					intent = new Intent(CircleActivity.this, FlashMorseMainActivity.class);
					Toast.makeText(getApplicationContext(), "摩尔斯电码", Toast.LENGTH_LONG).show();
					break;
				case "加油讯息":
					intent = new Intent(CircleActivity.this, MainActivity.class);
					Toast.makeText(getApplicationContext(), "加油讯息", Toast.LENGTH_LONG).show();
					break;
				case "联系我们":
					intent = new Intent(CircleActivity.this, ContactUsActivity.class);
					Toast.makeText(getApplicationContext(), "联系我们", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
				startActivity(intent);

			}
			
			@Override
			public void itemCenterClick(View view)
			{
				Toast.makeText(CircleActivity.this,
						"无法操作，请按周围的按钮",
						Toast.LENGTH_SHORT).show();
				
			}
		});
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			com.example.viewerpage.SwitchViewDemoActivity.MY_ACTIVITY.finish();
		}

		return super.onKeyDown(keyCode, event);
	}

}

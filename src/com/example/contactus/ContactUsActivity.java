package com.example.contactus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhy.sample_circlemenu.R;

public class ContactUsActivity extends Activity
{
	Button phonebutton;
	TextView title,name,phone,bottom,bottom2,qq,weibo;
	String phoneNumber="18745726437";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactus_main);
		phonebutton=(Button)findViewById(R.id.phonebutton);
		title=(TextView) findViewById(R.id.title);
		name=(TextView) findViewById(R.id.name);
		phone=(TextView) findViewById(R.id.phone);
		bottom=(TextView) findViewById(R.id.bottom);
		bottom2=(TextView) findViewById(R.id.bottom2);
		qq=(TextView) findViewById(R.id.qq);
		weibo=(TextView) findViewById(R.id.weibo);
		Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/lishu.ttf");
		Typeface typeface2 = Typeface.createFromAsset(getAssets(), "fonts/pingguolihei.ttf");
		title.setTypeface(typeface1);
		title.setTextColor(Color.WHITE);
		name.setTypeface(typeface2);
		name.setTextColor(Color.WHITE);
		phone.setTypeface(typeface2);
		phone.setTextColor(Color.WHITE);
		phonebutton.setTypeface(typeface2);
		phonebutton.setTextColor(Color.WHITE);
		bottom.setTypeface(typeface2);
		bottom.setTextColor(Color.WHITE);
		bottom2.setTypeface(typeface2);
		bottom2.setTextColor(Color.WHITE);
		qq.setTypeface(typeface2);
		qq.setTextColor(Color.WHITE);
		weibo.setTypeface(typeface2);
		weibo.setTextColor(Color.WHITE);
		phonebutton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_DIAL);
				intent.setData(Uri.parse("tel:" + phoneNumber));
				if (intent.resolveActivity(getPackageManager()) != null) {
					startActivity(intent);
				}
			}
		});
	}
	
}

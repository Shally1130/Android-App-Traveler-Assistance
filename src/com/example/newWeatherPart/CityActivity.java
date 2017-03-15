package com.example.newWeatherPart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newWeatherPart.adapter.CityListAdapter;
import com.zhy.sample_circlemenu.R;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CityActivity extends Activity
{
	SelectedApplication selectedApplication = new SelectedApplication();
	private ListView lv_city;
	private List<String> list;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		initViews();
		getCities();
	}

	private void initViews(){
		findViewById(R.id.iv_back).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				finish();
			}
		});
		lv_city = (ListView) findViewById(R.id.lv_city);
	}

	/*
	 * 获取城市信息
	 */
	private void getCities(){
		Parameters params = new Parameters();
		params.add("dtype", "json");
		JuheData.executeWithAPI(39, "http://v.juhe.cn/weather/citys", JuheData.GET, params, new DataCallBack() {
			@Override
			public void resultLoaded(int err, String reason, String result) {
				// TODO Auto-generated method stub
				try
				{

					System.out.println(result);
					JSONObject json = new JSONObject(result);
					int error_code = json.getInt("error_code");
					int resultcode=json.getInt("resultcode");
					if (error_code==0&&resultcode==200)
					{
						list = new ArrayList<String>();
						JSONArray resultArray = json.getJSONArray("result");
						Set<String> citySet = new HashSet<String>();
						for(int i = 0; i < resultArray.length(); i++)
						{
							String city = resultArray.getJSONObject(i).getString("city");
							citySet.add(city);
						}
						list.addAll(citySet);
						CityListAdapter adapter = new CityListAdapter(CityActivity.this, list);
						lv_city.setAdapter(adapter);
						lv_city.setOnItemClickListener(new OnItemClickListener()
						{
							
							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3)
							{
								boolean isSelected=true;
								// TODO Auto-generated method stub
								selectedApplication.setSelected(isSelected);
								Intent intent = new Intent();
								intent.putExtra("city", list.get(arg2));
								setResult(1,intent);
								finish();
							}
						});
					}
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});
	}
}

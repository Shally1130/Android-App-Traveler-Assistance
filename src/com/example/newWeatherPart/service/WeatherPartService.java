package com.example.newWeatherPart.service;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.newWeatherPart.bean.FutureWeatherBean;
import com.example.newWeatherPart.bean.HoursWeatherBean;
import com.example.newWeatherPart.bean.PMBean;
import com.example.newWeatherPart.bean.WeatherPartBean;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class WeatherPartService extends Service
{
	//标记,判断回调函数的执行
	private int count=0;
	private boolean isRunning=false;

	private String city="北京";
	private final String tag="WeatherPartService";
	private WeatherPartServiceBinder binder=new WeatherPartServiceBinder();
	private List<HoursWeatherBean> list;
	private PMBean pmBean;
	private WeatherPartBean weatherPartBean;
	public static final int REPEAT_MSG = 0x01;
	private OnParserCallBack callBack;
	private double lon;
	private double lat;



	public interface OnParserCallBack{
		public void OnParserCompelete(List<HoursWeatherBean> list, PMBean pmBean, WeatherPartBean weatherPartBean);

	} 

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return binder;
	}


	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		//city="杭州";
		Log.v(tag, "onCreate");
		mHandler.sendEmptyMessage(REPEAT_MSG);

	}

	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		Log.v(tag, "onDestroy");
		super.onDestroy();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		// TODO Auto-generated method stub
		Log.v(tag, "onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	/*
	 * 每半小时刷新一次
	 */
	Handler mHandler=new Handler(){

		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			if (msg.what == REPEAT_MSG)
			{
				getCityWeather();
				sendEmptyMessageDelayed(REPEAT_MSG, 30*60*1000);
			}
		}

	};


	public void setCallBack(OnParserCallBack callBack){
		this.callBack=callBack;
	}

	public void removeCallBack(){
		callBack=null;
	}

	/*
	 * 获取Location通过LocationManger获取
	 */
	public Location getLocation() {
		LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (loc == null) {
			loc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  
		}
		return loc;
	}

	public void getCityWeather(String city)
	{
		this.city=city;
		getAnotherCityWeather();
	}

	public void getCityWeather( )
	{
		Location loc = getLocation();
		if(loc == null){
			lon=116;
			lat=40;
		}else {
			lon=loc.getLongitude();
			lat=loc.getLatitude();
		}
		if(isRunning )
		{
			return;
		}
		isRunning=true;
		count=0;
		Parameters param1 = new Parameters();
		param1.add("lon", lon);
		param1.add("lat", lat);
		JuheData.executeWithAPI(39, "http://v.juhe.cn/weather/geo", JuheData.GET, param1, new DataCallBack() {
			@Override
			public void resultLoaded(int err, String reason, String result) {
				// TODO Auto-generated method stub
				if (err == 0) {
					weatherPartBean=parserWeather(result);
					System.out.println(result.toString());
					city=weatherPartBean.getCity();
					System.out.println(weatherPartBean.getCity());
					count++;
					if (weatherPartBean != null)
					{
						//						setWeatherViews(bean);
						Log.d("判断bean","dddddddddddddddddd");
					}
				} else {
					Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
				}
				if(count == 3)
				{
					//					mPullToRefreshScrollView.onRefreshComplete();
					if (callBack != null)
					{
						callBack.OnParserCompelete(list, pmBean, weatherPartBean);
					}
					isRunning=false;
				}
			}
		});

		Parameters params2 = new Parameters();
		params2.add("cityname",city );
		params2.add("dtype", "json");
		/*
		 * 未来三小时预测
		 */
		JuheData.executeWithAPI(39, "http://v.juhe.cn/weather/forecast3h", JuheData.GET, params2, new DataCallBack() {
			@Override
			public void resultLoaded(int err, String reason, String result) {
				// TODO Auto-generated method stub
				if (err == 0) {
					list = parserForecast3h(result);
					System.out.println(result.toString());
					count++;
					if (list!=null&&list.size()>=5)
					{
						//						setHoursViews(list);
						Log.d("判断bean","dddddddddddddddddd");
					}
				} else {
					Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
				}
				if(count==3)
				{
					if (callBack != null)
					{
						callBack.OnParserCompelete(list, pmBean, weatherPartBean);
					}
					isRunning=false;;
				}
			}
		});

		/*
		 * 获取PM2.5
		 */
		Parameters params3 = new Parameters();
		params3.add("city", city);
		params3.add("dtype", "json");
		JuheData.executeWithAPI(33, "http://web.juhe.cn:8080/environment/air/pm", JuheData.GET, params3, new DataCallBack()
		{

			@Override
			public void resultLoaded(int err, String reason, String result)
			{
				// TODO Auto-generated method stub
				if (err==0)
				{
					pmBean=parserPM(result);
					count++;
					if(pmBean!=null)
					{
						//						setPMViews(bean);
						System.out.println(result.toString());
					}

				}
				if(count==3)
				{
					if (callBack != null)
					{
						callBack.OnParserCompelete(list, pmBean, weatherPartBean);
					}
					isRunning=false;
				}
			}
		});
	}

	public void getAnotherCityWeather( )
	{
		if(isRunning )
		{
			return;
		}
		isRunning=true;
		count=0;
		Parameters params = new Parameters();
		params.add("cityname", city);
		params.add("dtype", "json");
		JuheData.executeWithAPI(39, "http://v.juhe.cn/weather/index", JuheData.GET, params, new DataCallBack() {
			@Override
			public void resultLoaded(int err, String reason, String result) {
				// TODO Auto-generated method stub
				if (err == 0) {
					weatherPartBean=parserWeather(result);
					System.out.println(result.toString());
					count++;
					if (weatherPartBean != null)
					{
						Log.d("判断bean","dddddddddddddddddd");
					}
				} else {
					Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
				}
				if(count == 3)
				{
					if (callBack != null)
					{
						callBack.OnParserCompelete(list, pmBean, weatherPartBean);
					}
					isRunning=false;
				}
			}
		});

		/*
		 * 未来三小时预测
		 */
		JuheData.executeWithAPI(39, "http://v.juhe.cn/weather/forecast3h", JuheData.GET, params, new DataCallBack() {
			@Override
			public void resultLoaded(int err, String reason, String result) {
				// TODO Auto-generated method stub
				if (err == 0) {
					list = parserForecast3h(result);
					System.out.println(result.toString());
					count++;
					if (list!=null&&list.size()>=5)
					{
						//						setHoursViews(list);
						Log.d("判断bean","dddddddddddddddddd");
					}
				} else {
					Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
				}
				if(count==3)
				{
					if (callBack != null)
					{
						callBack.OnParserCompelete(list, pmBean, weatherPartBean);
					}
					isRunning=false;;
				}
			}
		});

		/*
		 * 获取PM2.5
		 */
		Parameters params2 = new Parameters();
		params2.add("city", city);
		params2.add("dtype", "json");
		JuheData.executeWithAPI(33, "http://web.juhe.cn:8080/environment/air/pm", JuheData.GET, params2, new DataCallBack()
		{

			@Override
			public void resultLoaded(int err, String reason, String result)
			{
				// TODO Auto-generated method stub
				if (err==0)
				{
					pmBean=parserPM(result);
					count++;
					if(pmBean!=null)
					{
						//						setPMViews(bean);
						System.out.println(result.toString());
					}

				}
				if(count==3)
				{
					if (callBack != null)
					{
						callBack.OnParserCompelete(list, pmBean, weatherPartBean);
					}
					isRunning=false;
				}
			}
		});
	}

	/*
	 * 解析PM2.5的数据
	 */
	private  PMBean parserPM(String result)
	{
		PMBean bean=new PMBean();
		try
		{
			JSONObject json = new JSONObject(result);
			int error_code=json.getInt("error_code");
			int resultcode=json.getInt("resultcode");
			if (error_code==0&&resultcode==200)
			{
				JSONObject PMJson=json.getJSONArray("result").getJSONObject(0);
				bean.setAqi(PMJson.getString("AQI"));
				bean.setAquality(PMJson.getString("quality"));
			}


		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;

	}

	/*
	 * 解析天气数据
	 */
	private WeatherPartBean parserWeather(String result){


		WeatherPartBean bean=null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");

		try
		{
			/*
			 * 判断code
			 */
			JSONObject json = new JSONObject(result);
			int error_code=json.getInt("error_code");
			int resultcode=json.getInt("resultcode");
			if (error_code==0&&resultcode==200)
			{
				JSONObject resultJson = json.getJSONObject("result");
				bean= new WeatherPartBean();

				/*
				 * today
				 */
				JSONObject todayJson = resultJson.getJSONObject("today");
				bean.setCity(todayJson.getString("city"));
				bean.setUv_index(todayJson.getString("uv_index"));
				bean.setTemp(todayJson.getString("temperature"));
				bean.setWeather_str(todayJson.getString("weather"));
				bean.setWeather_id(todayJson.getJSONObject("weather_id").getString("fa"));
				bean.setDressing_index(todayJson.getString("dressing_index"));
				Log.d("setViews","ddddddddddddddddddddddddd");

				/*
				 * sk
				 */
				JSONObject skJson=resultJson.getJSONObject("sk");
				bean.setWind(skJson.getString("wind_direction")+skJson.getString("wind_strength"));
				bean.setNow_temp(skJson.getString("temp"));
				bean.setRelease(skJson.getString("time"));
				bean.setHumidity(skJson.getString("humidity"));
				Log.d("sk","ddddddddddddddddddddddddd");

				/*
				 * future
				 */
				Date date=new Date(System.currentTimeMillis());//获取系统时间，设置为当前时间
				JSONObject futureArray=resultJson.getJSONObject("future");
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(date);
				String name="day_";
				List<FutureWeatherBean> futureList=new ArrayList<FutureWeatherBean>();
				for (int i = 0; i < 3; i++)
				{
					rightNow.add(Calendar.DAY_OF_YEAR,1);//日期加1天
					java.util.Date dt1= rightNow.getTime();
					String reStr = sdf.format(dt1);
					JSONObject futureJson=futureArray.getJSONObject(name+reStr);
					FutureWeatherBean futureBean=new FutureWeatherBean();
					java.util.Date dateSDF=sdf.parse(futureJson.getString("date")); //格式化
					Log.d("dateSDF","ddddddddddddddddddddddddd");
					futureBean.setTemp(futureJson.getString("temperature"));
					futureBean.setWeek(futureJson.getString("week"));
					futureBean.setWeather_id(futureJson.getJSONObject("weather_id").getString("fa"));
					futureList.add(futureBean);
					Log.d("futureList.add(futureBean)","ddddddddddddddddddddddddd");
				}

				bean.setFutureList(futureList);

			}else {
				Toast.makeText(getApplicationContext(), "WEATHER_ERROR", Toast.LENGTH_SHORT).show();
			}

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}

		return bean;
	}

	/*
	 * 解析一天内每隔三小时的天气
	 */
	private List<HoursWeatherBean> parserForecast3h(String result)
	{
		List<HoursWeatherBean> list=null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
		Date date=new Date(System.currentTimeMillis());
		try
		{
			/*
			 * 判断code
			 */
			JSONObject json = new JSONObject(result);
			int error_code = json.getInt("error_code");
			int resultcode=json.getInt("resultcode");
			if (error_code==0&&resultcode==200)
			{
				list=new ArrayList<HoursWeatherBean>();
				JSONArray resultArray = json.getJSONArray("result");
				for(int i=0; i<resultArray.length();i++)
				{
					JSONObject hourJson= resultArray.getJSONObject(i);
					java.util.Date hDate= sdf.parse(hourJson.getString("sfdate"));
					if(!hDate.after(date))
					{
						continue;
					}
					HoursWeatherBean bean= new HoursWeatherBean();
					bean.setWeather_id(hourJson.getString("weatherid"));
					bean.setTemp(hourJson.getString("temp1")+"°");
					Calendar c=Calendar.getInstance();
					c.setTime(hDate);
					bean.setTime(c.get(Calendar.HOUR_OF_DAY)+"");
					list.add(bean);
					if(list.size()==5)
					{
						break;
					}
				}
			}else {
				Toast.makeText(getApplicationContext(), "HOURS_ERROR", Toast.LENGTH_SHORT).show();
			}

		}

		catch (JSONException | ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return list;
	}


	public WeatherPartService()
	{
		// TODO Auto-generated constructor stub
	}

	/*
	 * 实现service和activity通信，用Binder
	 */
	public class WeatherPartServiceBinder extends Binder{
		/*
		 * 返回当前的WeatherPartService
		 */
		public WeatherPartService getService(){
			return WeatherPartService.this;

		}
	}
}

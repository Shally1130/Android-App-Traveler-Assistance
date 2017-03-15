package com.example.newWeatherPart;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.security.auth.PrivateCredentialPermission;

import com.example.newWeatherPart.bean.FutureWeatherBean;
import com.example.newWeatherPart.bean.HoursWeatherBean;
import com.example.newWeatherPart.bean.PMBean;
import com.example.newWeatherPart.bean.WeatherPartBean;
import com.example.newWeatherPart.service.WeatherPartService;
import com.example.newWeatherPart.service.WeatherPartService.OnParserCallBack;
import com.example.newWeatherPart.service.WeatherPartService.WeatherPartServiceBinder;
import com.example.newWeatherPart.swiperefresh.PullToRefreshBase;
import com.example.newWeatherPart.swiperefresh.PullToRefreshScrollView;
import com.example.newWeatherPart.swiperefresh.PullToRefreshBase.OnRefreshListener;
import com.zhy.sample_circlemenu.R;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class WeatherPartActivity extends Activity{
	private PullToRefreshScrollView mPullToRefreshScrollView;
	private ScrollView mScrollView;
	Context mContext;
	public SelectedApplication selectedApplication = new SelectedApplication();

	private WeatherPartService mService;

	private TextView tv_city,// 城市
	tv_release, // 发布时间
	tv_now_weather,// 天气
	tv_today_temp,// 今天温度
	tv_now_temp,// 当前温度
	tv_aqi,// 空气质量指数
	tv_quality,// 空气质量
	tv_next_three,// 3小时
	tv_next_six,// 6小时
	tv_next_nine,// 9小时
	tv_next_twelve,// 12小时
	tv_next_fifteen,// 15小时
	tv_next_three_temp,// 3小时温度
	tv_next_six_temp,// 6小时温度
	tv_next_nine_temp,// 9小时温度
	tv_next_twelve_temp,// 12小时温度
	tv_next_fifteen_temp,// 15小时温度
	tv_today_temp_a,// 今天温度a
	tv_today_temp_b,// 今天温度b
	tv_tommorrow,// 明天
	tv_tommorrow_temp_a,// 明天温度a
	tv_tommorrow_temp_b,// 明天温度b
	tv_thirdday,// 第三天
	tv_thirdday_temp_a,// 第三天温度a
	tv_thirdday_temp_b,// 第三天温度b
	tv_fourthday,// 第四天
	tv_fourthday_temp_a,// 第四天温度a
	tv_fourthday_temp_b,// 第四天温度b
	tv_humidity,// 湿度
	tv_wind, tv_uv_index,// 紫外线指数
	tv_dressing_index;// 穿衣指数

	private ImageView iv_now_weather,// 现在
	iv_next_three,// 3小时
	iv_next_six,// 6小时
	iv_next_nine,// 9小时
	iv_next_twelve,// 12小时
	iv_next_fifteen,// 15小时
	iv_today_weather,// 今天
	iv_tommorrow_weather,// 明天
	iv_thirdday_weather,// 第三天
	iv_fourthday_weather;// 第四天

	private static final int UPDATE_TIME = 5000;
	private static int LOCATION_COUTNS = 0;

	private RelativeLayout rl_city;	
	
	/*
	 * 动态背景
	 */
	private ImageView yun_left, yun_right;
	private Animation myAnimation_Translate_left, myAnimation_Translate_left2,
			myAnimation_Translate_right, myAnimation_Translate_right2;
	private TextView text;
	private long number;
	private Random rd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weatherpart);
		//		getCityWeather();  		
		mContext = this;
		init();
		initView();
		initService();
	}
	
	/*
	 * 动态背景
	 */
	private void initView() {

		yun_left = (ImageView) findViewById(R.id.yun_left);
		yun_right = (ImageView) findViewById(R.id.yun_right);
		effectCloud();

	}

	private void randomDuration() {
		rd = new Random();
		number = (rd.nextInt(10) + 6) * 1000;
	}

	private void effectCloud() {
		randomDuration();
		myAnimation_Translate_left = AnimationUtils.loadAnimation(this,
				R.anim.my_translate_action_left);
		myAnimation_Translate_right = AnimationUtils.loadAnimation(this,
				R.anim.my_translate_action_right);
		myAnimation_Translate_left2 = AnimationUtils.loadAnimation(this,
				R.anim.my_translate_action_left);
		myAnimation_Translate_right2 = AnimationUtils.loadAnimation(this,
				R.anim.my_translate_action_right);

		myAnimation_Translate_left.setDuration((rd.nextInt(10) + 6) * 1000);
		myAnimation_Translate_right.setDuration((rd.nextInt(10) + 6) * 1000);
		myAnimation_Translate_left2.setDuration((rd.nextInt(10) + 6) * 1000);
		myAnimation_Translate_right2.setDuration((rd.nextInt(10) + 6) * 1000);

		myAnimation_Translate_left
				.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation arg0) {

					}

					@Override
					public void onAnimationRepeat(Animation arg0) {

					}

					@Override
					public void onAnimationEnd(Animation arg0) {
						randomDuration();
						myAnimation_Translate_left.setDuration(number);
						yun_left.startAnimation(myAnimation_Translate_right);
					}
				});

		myAnimation_Translate_right
				.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation arg0) {

					}

					@Override
					public void onAnimationRepeat(Animation arg0) {

					}

					@Override
					public void onAnimationEnd(Animation arg0) {
						randomDuration();
						myAnimation_Translate_left.setDuration(number);
						yun_left.startAnimation(myAnimation_Translate_left);
					}
				});

		myAnimation_Translate_left2
				.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation arg0) {

					}

					@Override
					public void onAnimationRepeat(Animation arg0) {

					}

					@Override
					public void onAnimationEnd(Animation arg0) {
						randomDuration();
						myAnimation_Translate_right2.setDuration(number);
						yun_right.startAnimation(myAnimation_Translate_right2);
					}
				});
		myAnimation_Translate_right2
				.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation arg0) {

					}

					@Override
					public void onAnimationRepeat(Animation arg0) {

					}

					@Override
					public void onAnimationEnd(Animation arg0) {
						randomDuration();
						myAnimation_Translate_left2.setDuration(number);
						yun_right.startAnimation(myAnimation_Translate_left2);
					}
				});
		yun_left.startAnimation(myAnimation_Translate_left);
		yun_right.startAnimation(myAnimation_Translate_right2);
	}
	

	/*
	 * 随时绑定
	 */
	 private void initService(){
		Intent intent =new Intent(mContext,WeatherPartService.class);
		startService(intent);
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
	 }

	 ServiceConnection conn =new ServiceConnection()
	 {

		 @Override
		 public void onServiceDisconnected(ComponentName name)
		 {
			 // TODO Auto-generated method stub
			 mService.removeCallBack();
		 }

		 /*
		  * ComponentName绑定名
		  * @see android.content.ServiceConnection#onServiceConnected(android.content.ComponentName, android.os.IBinder)
		  */
		 @Override
		 public void onServiceConnected(ComponentName name, IBinder service)
		 {
			 // TODO Auto-generated method stub
			 mService = ((WeatherPartServiceBinder) service).getService();
			 mService.setCallBack(new OnParserCallBack()
			 {

				 public void OnParserCompelete(List<HoursWeatherBean> list,
						 PMBean pmBean, WeatherPartBean weatherPartBean)
				 {
					 // TODO Auto-generated method stub
					 mPullToRefreshScrollView.onRefreshComplete();

					 if(list != null && list.size()>=5)
					 {
						 setHoursViews(list);
					 }

					 if(pmBean != null)
					 {
						 setPMViews(pmBean);
					 }

					 if (weatherPartBean != null)
					 {
						 setWeatherViews(weatherPartBean);
					 }
				 }
			 });

			 mService.getCityWeather();
		 }
	 };

	 private void setPMViews(PMBean bean)
	 {
		 tv_aqi.setText(bean.getAqi());
		 tv_quality.setText(bean.getAquality());
	 }


	 private void setWeatherViews(WeatherPartBean bean)
	 {
		 tv_city.setText(bean.getCity());
		 tv_release.setText(bean.getRelease()+"更新"); 
		 tv_now_weather.setText(bean.getWeather_str()); 
		 iv_today_weather.setImageResource(getResources().getIdentifier("d"+bean.getWeather_id(), "drawable", "com.zhy.sample_circlemenu"));
		 //6℃~18℃
		 String[] tempArray = bean.getTemp().split("~");//拆分数据
		 String temp_str_a = tempArray[1].substring(0, tempArray[1].indexOf("℃"));
		 String temp_str_b = tempArray[0].substring(0, tempArray[0].indexOf("℃"));
		 tv_today_temp.setText("↑"+temp_str_a+"°     ↓"+temp_str_b+"°"); 
		 tv_now_temp.setText(bean.getNow_temp()+"℃");
		 tv_today_temp_a.setText(temp_str_a+"°");
		 tv_today_temp_b.setText(temp_str_b+"° ");
		 List<FutureWeatherBean> futureList=bean.getFutureList();
		 tv_tommorrow.setText(futureList.get(0).getWeek());
		 if (futureList.size()==3)
		 {
			 setFutureData(tv_tommorrow, iv_tommorrow_weather, tv_tommorrow_temp_a, tv_tommorrow_temp_b, futureList.get(0));
			 setFutureData(tv_thirdday, iv_thirdday_weather, tv_thirdday_temp_a, tv_thirdday_temp_b, futureList.get(1));
			 setFutureData(tv_fourthday, iv_fourthday_weather, tv_fourthday_temp_a, tv_fourthday_temp_b, futureList.get(2));
		 }
		 Calendar c=Calendar.getInstance();//当前时间
		 int time=c.get(Calendar.HOUR_OF_DAY);
		 String prefixStr=null;
		 if (time>6&&time<18)
		 {
			 prefixStr="d";
		 }else{
			 prefixStr="n";
		 }
		 iv_now_weather.setImageResource(getResources().getIdentifier(prefixStr+bean.getWeather_id(), "drawable", "com.zhy.sample_circlemenu"));
		 tv_humidity.setText(bean.getHumidity());
		 tv_dressing_index.setText(bean.getDressing_index());
		 tv_uv_index.setText(bean.getUv_index());
		 tv_wind.setText(bean.getWind());
	 }

	 private void setHoursViews(List<HoursWeatherBean> list)
	 {
		 setHoursData(tv_next_three, iv_next_three, tv_next_three_temp, list.get(0));
		 setHoursData(tv_next_six, iv_next_six, tv_next_six_temp, list.get(1));
		 setHoursData(tv_next_nine, iv_next_nine, tv_next_nine_temp, list.get(2));
		 setHoursData(tv_next_twelve, iv_next_twelve, tv_next_twelve_temp, list.get(3));
		 setHoursData(tv_next_fifteen, iv_next_fifteen, tv_next_fifteen_temp, list.get(4));
	 }

	 private void setHoursData(TextView tv_hour, ImageView iv_weather, TextView tv_temp, HoursWeatherBean bean)
	 {
		 String prefixStr=null;
		 int time=Integer.valueOf(bean.getTime());
		 if (time>6&&time<18)
		 {
			 prefixStr="d";
		 }else{
			 prefixStr="n";
		 }
		 tv_hour.setText(bean.getTime()+"时");
		 iv_weather.setImageResource(getResources().getIdentifier(prefixStr+bean.getWeather_id(), "drawable", "com.zhy.sample_circlemenu"));
		 tv_temp.setText(bean.getTemp());
	 }


	 /*
	  * 未来
	  */
	 private void setFutureData(TextView tv_week, ImageView iv_weather, TextView tv_temp_a,TextView tv_temp_b, FutureWeatherBean futureWeatherBean)
	 {
		 tv_week.setText(futureWeatherBean.getWeek());
		 iv_weather.setImageResource(getResources().getIdentifier("d"+futureWeatherBean.getWeather_id(), "drawable", "com.zhy.sample_circlemenu"));
		 String[] tempArray = futureWeatherBean.getTemp().split("~");//拆分数据
		 String temp_str_a = tempArray[1].substring(0, tempArray[1].indexOf("℃"));
		 String temp_str_b = tempArray[0].substring(0, tempArray[0].indexOf("℃"));
		 tv_temp_a.setText(temp_str_a+"° ");
		 tv_temp_b.setText(temp_str_b+"° ");
	 }

	 private void init(){
		 mPullToRefreshScrollView=(PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		 /*
		  * 滚动条
		  */
		 mPullToRefreshScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			 @Override
			 public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				 // TODO Auto-generated method stub
				 if(selectedApplication.isSelected()==false){
					 mService.getCityWeather( );
				 }else 
					 mService.getAnotherCityWeather();

			 }
		 });
		 mScrollView=mPullToRefreshScrollView.getRefreshableView();
		 rl_city = (RelativeLayout) findViewById(R.id.rl_city);
		 rl_city.setOnClickListener(new OnClickListener() {

			 @Override
			 public void onClick(View v) {
				 // TODO Auto-generated method stub
				 Intent intent=new Intent(mContext, CityActivity.class);
				 startActivityForResult(intent, 1);
			 }
		 });
		 tv_city = (TextView) findViewById(R.id.tv_city);
		 tv_release = (TextView) findViewById(R.id.tv_release);
		 tv_now_weather = (TextView) findViewById(R.id.tv_now_weather);
		 tv_today_temp = (TextView) findViewById(R.id.tv_today_temp);
		 tv_now_temp = (TextView) findViewById(R.id.tv_now_temp);
		 tv_aqi = (TextView) findViewById(R.id.tv_aqi);
		 tv_quality = (TextView) findViewById(R.id.tv_quality);
		 tv_next_three = (TextView) findViewById(R.id.tv_next_three);
		 tv_next_six = (TextView) findViewById(R.id.tv_next_six);
		 tv_next_nine = (TextView) findViewById(R.id.tv_next_nine);
		 tv_next_twelve = (TextView) findViewById(R.id.tv_next_twelve);
		 tv_next_fifteen = (TextView) findViewById(R.id.tv_next_fifteen);
		 tv_next_three_temp = (TextView) findViewById(R.id.tv_next_three_temp);
		 tv_next_six_temp = (TextView) findViewById(R.id.tv_next_six_temp);
		 tv_next_nine_temp = (TextView) findViewById(R.id.tv_next_nine_temp);
		 tv_next_twelve_temp = (TextView) findViewById(R.id.tv_next_twelve_temp);
		 tv_next_fifteen_temp = (TextView) findViewById(R.id.tv_next_fifteen_temp);
		 tv_today_temp_a = (TextView) findViewById(R.id.tv_today_temp_a);
		 tv_today_temp_b = (TextView) findViewById(R.id.tv_today_temp_b);
		 tv_tommorrow = (TextView) findViewById(R.id.tv_tommorrow);
		 tv_tommorrow_temp_a = (TextView) findViewById(R.id.tv_tommorrow_temp_a);
		 tv_tommorrow_temp_b = (TextView) findViewById(R.id.tv_tommorrow_temp_b);
		 tv_thirdday = (TextView) findViewById(R.id.tv_thirdday);
		 tv_thirdday_temp_a = (TextView) findViewById(R.id.tv_thirdday_temp_a);
		 tv_thirdday_temp_b = (TextView) findViewById(R.id.tv_thirdday_temp_b);
		 tv_fourthday = (TextView) findViewById(R.id.tv_fourthday);
		 tv_fourthday_temp_a = (TextView) findViewById(R.id.tv_fourthday_temp_a);
		 tv_fourthday_temp_b = (TextView) findViewById(R.id.tv_fourthday_temp_b);
		 tv_humidity = (TextView) findViewById(R.id.tv_humidity);
		 tv_wind = (TextView) findViewById(R.id.tv_wind);
		 tv_uv_index = (TextView) findViewById(R.id.tv_uv_index);
		 tv_dressing_index = (TextView) findViewById(R.id.tv_dressing_index);

		 iv_now_weather = (ImageView) findViewById(R.id.iv_now_weather);
		 iv_next_three = (ImageView) findViewById(R.id.iv_next_three);
		 iv_next_six = (ImageView) findViewById(R.id.iv_next_six);
		 iv_next_nine = (ImageView) findViewById(R.id.iv_next_nine);
		 iv_next_twelve = (ImageView) findViewById(R.id.iv_next_twelve);
		 iv_next_fifteen = (ImageView) findViewById(R.id.iv_next_fifteen);
		 iv_today_weather = (ImageView) findViewById(R.id.iv_today_weather);
		 iv_tommorrow_weather = (ImageView) findViewById(R.id.iv_tommorrow_weather);
		 iv_thirdday_weather = (ImageView) findViewById(R.id.iv_thirdday_weather);
		 iv_fourthday_weather = (ImageView) findViewById(R.id.iv_fourthday_weather);

	 }



	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data)
	 {
		 // TODO Auto-generated method stub
		 super.onActivityResult(requestCode, resultCode, data);
		 if(requestCode == 1 && resultCode == 1){
			 String city = data.getStringExtra("city");
			 mService.getCityWeather(city);
		 }
	 }


	 @Override
	 protected void onPause()
	 {
		 // TODO Auto-generated method stub
		 super.onPause();
		 selectedApplication.setSelected(false);
	 }


	 @Override
	 protected void onDestroy()
	 {
		 // TODO Auto-generated method stub
		 unbindService(conn);
		 super.onDestroy();
		 selectedApplication.setSelected(false);
	 }


}

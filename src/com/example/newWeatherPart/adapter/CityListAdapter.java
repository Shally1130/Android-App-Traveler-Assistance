package com.example.newWeatherPart.adapter;

import java.util.List;

import com.zhy.sample_circlemenu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityListAdapter extends BaseAdapter
{
	public List<String> list;
	private LayoutInflater mInflater;//装载布局
	
	public  CityListAdapter(Context context, List<String> list)
	{
		// TODO Auto-generated constructor stub
		this.list = list;
		mInflater = LayoutInflater.from(context);//动态加载布局
		
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int) 获取对象
	 */
	public String getItem(int position)
	{
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	/*
	 * 当滚动出屏幕后会把值存入convertView中
	 */
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		View rowView = null;
		if (convertView == null)
		{
			rowView = mInflater.inflate(R.layout.item_city_list, null);//如果没有就新建一个convertView
		}else {
			rowView = convertView;
		}
		
		TextView tv_city = (TextView) rowView.findViewById(R.id.tv_city);
		tv_city.setText(getItem(position));
		
		return rowView;
	}

}

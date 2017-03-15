package com.example.newWeatherPart;

import android.app.Application;

public class  SelectedApplication extends Application
{
	public static boolean isSelected = false;

	public static boolean isSelected()
	{
		return isSelected;
	}

	public static void setSelected(boolean isSelected)
	{
		SelectedApplication.isSelected = isSelected;
	}
	
}

package com.example.flashmorse;


import com.zhy.sample_circlemenu.R;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.Camera;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;



public class FlashLight extends Activity
{
	protected ImageView mImageViewFlashlight;
	protected ImageView mImageViewFlashlightController;
	protected Camera mCamera;
	protected android.hardware.Camera.Parameters mParameters;
	protected EditText mEditTextMorseCode;
	protected Button button;

	
	Handler handler=new Handler();
	Runnable openRunnable;
	Runnable closeRunnable;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.morse);
		mEditTextMorseCode=(EditText) findViewById(R.id.editext_morse_code);
		mImageViewFlashlight=(ImageView) findViewById(R.id.imageview_flashlight);
		mImageViewFlashlightController=(ImageView) findViewById(R.id.imageview_flashlight_controller);
	}


	/*
	 * 打开闪光灯
	 */
	protected void openFlashLight()
	{

		if(!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) //检测闪光灯
		{
			Toast.makeText(this, "当前设备没有闪光灯", Toast.LENGTH_LONG).show();
			return;
		}


		try
		{
			mCamera=Camera.open(); //打开照相机
			int textureId = 0;
			mCamera.setPreviewTexture(new SurfaceTexture(textureId)); //设置纹理
			mCamera.startPreview();
			mParameters = mCamera.getParameters();
			mParameters.setFlashMode(mParameters.FLASH_MODE_TORCH); // 打开闪光灯
			mCamera.setParameters(mParameters);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
	}

	/*
	 * 关闭闪光灯
	 */
	protected void closeFlashLight()
	{
//		TransitionDrawable drawable = (TransitionDrawable) mImageViewFlashlight
//				.getDrawable();
//		if (((Boolean) mImageViewFlashlight.getTag())) 
//		{
//			drawable.reverseTransition(200);
//			mImageViewFlashlight.setTag(false);
//			//					Toast.makeText(this, "摩尔斯电码已经发送完成！", Toast.LENGTH_LONG).show();
//			android.util.Log.i("!!!!", "关闭灯！！！！！！！！！！");
//		}

		if (mCamera != null)
		{
			mParameters=mCamera.getParameters();
			mParameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
			mCamera.setParameters(mParameters);
			mCamera.stopPreview();
			mCamera.release();
			mCamera=null;
		}
	}


	@Override
	protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();
		closeFlashLight();
	}


	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		closeFlashLight();
	}

}

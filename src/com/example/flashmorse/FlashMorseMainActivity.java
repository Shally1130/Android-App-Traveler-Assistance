package com.example.flashmorse;

import java.util.HashMap;
import java.util.Map;

import android.view.ViewGroup.LayoutParams;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;


public class FlashMorseMainActivity extends FlashLight
{

	private final int DOT_TIME = 200; // 点停留的时间，单位：毫秒
	private final int LINE_TIME = DOT_TIME * 3; // 线停留的时间
	private final int DOT_LINE_TIME = DOT_TIME; // 点到线的时间间隔
	private final int CHAR_CHAR_TIME = DOT_TIME * 3; // 字符到字符之间的时间间隔
	private final int WORD_WORD_TIME = DOT_TIME * 7; // 单词到单词直接的时间间隔

	private String mMorseCode;

	private Map<Character, String> mMorseCodeMap = new HashMap<Character, String>();

	private int i;
	private String[] words;
	int flag=0;
	//	private Thread thread;

	@Override

	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		closeRunnable = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				TransitionDrawable drawable = (TransitionDrawable) mImageViewFlashlight
						.getDrawable();
				drawable.reverseTransition(20);
				android.util.Log.i("!!!!", "关闭灯！！！！！！！！！！");
			}
		};

		mImageViewFlashlight.setTag(false);
		Point point = new Point();
		getWindowManager().getDefaultDisplay().getSize(point);
		mImageViewFlashlight.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if (verifyMorseCode())
				{
					TransitionDrawable drawable = (TransitionDrawable) mImageViewFlashlight
							.getDrawable();
					drawable.startTransition(200);
					android.util.Log.i("!!!!", "打开灯！！！！！！！！！！");
				}
				Thread thread=new Thread(){
					public void run(){ 
						Looper.prepare();
						sendSentense(mMorseCode);
						Looper.loop();
					}
				};
				thread.start();
			}
		});

		LayoutParams layoutParams = (LayoutParams) mImageViewFlashlightController.getLayoutParams();
		layoutParams.height=point.y/3;
		layoutParams.width=point.x/2;
		mImageViewFlashlightController.setLayoutParams(layoutParams);

		mMorseCodeMap.put('a', ".-");
		mMorseCodeMap.put('b', "-...");
		mMorseCodeMap.put('c', "-.-.");
		mMorseCodeMap.put('d', "-..");
		mMorseCodeMap.put('e', ".");
		mMorseCodeMap.put('f', "..-.");
		mMorseCodeMap.put('g', "--.");
		mMorseCodeMap.put('h', "....");
		mMorseCodeMap.put('i', "..");
		mMorseCodeMap.put('j', ".---");
		mMorseCodeMap.put('k', "-.-");
		mMorseCodeMap.put('l', ".-..");
		mMorseCodeMap.put('m', "--");
		mMorseCodeMap.put('n', "-.");
		mMorseCodeMap.put('o', "---");
		mMorseCodeMap.put('p', ".--.");
		mMorseCodeMap.put('q', "--.-");
		mMorseCodeMap.put('r', ".-.");
		mMorseCodeMap.put('s', "...");
		mMorseCodeMap.put('t', "-");
		mMorseCodeMap.put('u', "..-");
		mMorseCodeMap.put('v', "...-");
		mMorseCodeMap.put('w', ".--");
		mMorseCodeMap.put('x', "-..-");
		mMorseCodeMap.put('y', "-.--");
		mMorseCodeMap.put('z', "--..");

		mMorseCodeMap.put('0', "-----");
		mMorseCodeMap.put('1', ".----");
		mMorseCodeMap.put('2', "..---");
		mMorseCodeMap.put('3', "...--");
		mMorseCodeMap.put('4', "....-");
		mMorseCodeMap.put('5', ".....");
		mMorseCodeMap.put('6', "-....");
		mMorseCodeMap.put('7', "--...");
		mMorseCodeMap.put('8', "---..");
		mMorseCodeMap.put('9', "----.");

	}


	private void sleep(long time){
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 发送点
	 */
	private void sendDot()
	{
		openFlashLight();
		sleep(DOT_TIME);
		closeFlashLight();
	}

	/*
	 * 发送线
	 */
	private void sendLine()
	{
		openFlashLight();
		sleep(LINE_TIME);
		closeFlashLight();
	}

	/*
	 * 发送字符
	 */
	private void sendChar(char c)
	{
		String morseCode=mMorseCodeMap.get(c);
		if (morseCode != null)
		{
			char LastChar=' ';
			for(int i=0; i<morseCode.length();i++)
			{
				char dotLine = morseCode.charAt(i);
				if (dotLine == '.')
				{
					sendDot();
				}
				else if (dotLine == '-') {
					sendLine();
				}
				if (i > 0 && i < morseCode.length()-1)
				{
					if (LastChar == '.' && dotLine == '-')
					{
						sleep(DOT_LINE_TIME);
					}
				}
				LastChar = dotLine;
			}
		}
	}

	/*
	 *发送单词 
	 */
	private void sendWord(String s)
	{
		for (int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			sendChar(c);
			if (i<s.length()-1)
			{
				sleep(CHAR_CHAR_TIME);
			}
		}
	}

	/*
	 * 发送句子
	 */
	private void sendSentense(String s)
	{
		android.util.Log.i("sendSentence","不可按按钮！！！！！！！！！！");
		mImageViewFlashlight.setClickable(false);
		words = s.split(" +");
		for (i = 0; i < words.length; i++)
		{
			sendWord(words[i]);
			if (i<words.length)
			{
				sleep(WORD_WORD_TIME);
			}
		}
		handler.post(closeRunnable);
		android.util.Log.i("sendSentence","线程！！！！！！！！！！");
		mImageViewFlashlight.setClickable(true);
		android.util.Log.i("sendSentence","可以按按钮！！！！！！！！！！");
		Toast.makeText(this, "摩尔斯电码已经发送完成！", Toast.LENGTH_LONG).show();
	}

	/*
	 * 验证文本发送的是字母和数字
	 */
	private boolean verifyMorseCode(){
		mMorseCode=mEditTextMorseCode.getText().toString().toLowerCase();
		if ("".equals(mMorseCode))
		{
			Toast.makeText(this, "请输入摩尔斯电码！", Toast.LENGTH_LONG).show();
			return false;
		}else {
			for (int i = 0; i < mMorseCode.length(); i++)
			{
				char c = mMorseCode.charAt(i);
				if(!(c >= 'a' && c <='z') && !(c >= '0' && c<='9') && c !=' '){
					Toast.makeText(this, "莫尔斯电码只能是字母和数字！", Toast.LENGTH_LONG).show();
					return false;
				}
			}
		}
		return true;
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

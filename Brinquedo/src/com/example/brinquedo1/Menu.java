package com.example.brinquedo1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class Menu extends View implements Runnable
{
	private Bitmap background;
	private Bitmap [] options;
	private Rect [] areaOptions;
	ImageManager picture;
	Paint paint;
	View fase01;
	Activity activity;
	
	public Menu(Context context) 
	{	
		super(context);
		
		setFocusableInTouchMode(true);
		setClickable(true);
		setLongClickable(true);
		
		picture = new ImageManager();
		paint = new Paint();

		activity = (Activity) context;
		options = new Bitmap[3];
		areaOptions = new Rect[3];

		background = picture.ImageManager("fundomenu.png", context);
		options[0] = picture.ImageManager("play.png", context);
		options[1] = picture.ImageManager("credits.png", context);
		options[2] = picture.ImageManager("quit.png", context);

		// TODO Auto-generated constructor stub
	}
	
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		
		canvas.drawBitmap(background, 0,0, paint);
		canvas.drawBitmap(options[0], getWidth()/3, getHeight()/2, paint);
		canvas.drawBitmap(options[1], getWidth()/14, getHeight()/1.6f, paint);
		canvas.drawBitmap(options[2], getWidth()/1.5f, getHeight()/1.5f, paint);
		
		areaOptions[0] = new Rect(getWidth()/3,getHeight()/2, getWidth()/3 + (int)options[0].getWidth(),getHeight()/2 + (int)options[0].getHeight());
	
}
	
	public boolean onTouchEvent(MotionEvent event) 
	{	
		if (event.getAction() == MotionEvent.ACTION_DOWN) 
		{
			Log.i(MainActivity.TAG, "Entrou no action down");
		}
		
		if (event.getAction() == MotionEvent.ACTION_MOVE) 
		{
			Log.i(MainActivity.TAG, "Entrou no action move");
		}
		
		if (event.getAction() == MotionEvent.ACTION_UP) 
		{
			Log.i(MainActivity.TAG, "Entrou no action up");
			int a = (int)event.getRawX();
			int b = (int)event.getRawY();

			// Play
			if(areaOptions[0].contains(a,b))
			{
				Log.i(MainActivity.TAG, "Entrou no Play !! ");
				fase01 = new Fase01(activity);
				activity.setContentView(fase01);			
			}	
		}
		
		return super.onTouchEvent(event);
	}
	
	public void run() 
	{
		while(true)
		{
			try
			{	
				Thread.sleep(1);
			}
			
			catch(Exception e)
			{
				Log.e("Deu erro", "Quem sabe mete o pe");
			}
			
			postInvalidate();
		}
		// TODO Auto-generated method stub
	}
	
}
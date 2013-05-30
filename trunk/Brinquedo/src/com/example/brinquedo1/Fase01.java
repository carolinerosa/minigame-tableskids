package com.example.brinquedo1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public  class Fase01 extends View implements Runnable{

	private long time = 1;
	Bitmap [] geometricFigures=new Bitmap[7];
	Bitmap [] Backgrounds;
	private int period = 10;
	int counter;
	private Paint paint;	
	private Rect Back = new Rect();
	int totalPoints = 1;
	int hitPoints = 0;	
	Rect object_down ;
	int[] order = new int[3];
	Rect [] areaObjectsUp = new Rect[3];
	private static float positionX;
	private static float positionY;
	private Canvas MyCanvas;
	private Random rnd = new Random();
	private int current;
	private int currentTime;
	ImageManager img;
	View game;
	private Activity activity;
	
	public Fase01(Context context) 
	{
		super(context);
		
		setFocusableInTouchMode(true);
		setClickable(true);
		setLongClickable(true);

		Backgrounds = new Bitmap[3];
		img = new ImageManager();
		paint = new Paint();
		activity = (Activity) context;

		
		paint.setColor(Color.BLACK); 
		paint.setTextSize(20); 

		current = rnd.nextInt(3);
		geometricFigures[0] = img.ImageManager("hexagono.png", context);
		geometricFigures[1] = img.ImageManager("hexagonoColor.png", context);

		Backgrounds[0] = img.ImageManager("bgCongrats.bmp", context);
		Backgrounds[1] = img.ImageManager("bgGameOver.bmp", context);
		Backgrounds[2] = img.ImageManager("Background1.png", context);
			
		Thread processo = new Thread(this);
		processo.start();
		
		// TODO Auto-generated constructor stub
	}
	
	protected void onSizeChanged(int w, int h, int oldw, int oldh) 
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		positionX= getWidth()/2;
		positionY=getHeight()/2;
		Back.set(0, 0, getWidth(), getHeight());
	}

	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		
		if(period!=0 && hitPoints != totalPoints)
		{
			MyCanvas=canvas;
		
			canvas.drawBitmap(Backgrounds[2], null, Back, paint);
			
			// Figuras geométricas que ficarão na parte de cima
			canvas.drawBitmap(geometricFigures[0], getWidth()/3, 0, paint);
			
			// Imagem que ficará na parte de baixo (figura geométrica em PB).
			canvas.drawBitmap(geometricFigures[1], positionX, positionY, paint);
	
			// Textos na tela.
			canvas.drawText("Score:" + hitPoints + "/" + totalPoints, getWidth()/13 ,getHeight()/1.1f, paint);
			canvas.drawText("Time:" +" " +  period , getWidth()/1.5f, getHeight()/1.1f, paint);
			
			int a = (int)positionX;
			int b = (int)positionY;
			object_down= new Rect(a,b, a+ (int)geometricFigures[1].getWidth(), b+(int)geometricFigures[1].getHeight());
			
			areaObjectsUp[0]= new Rect(getWidth()/3,0, (int)getWidth()/3+ (int)geometricFigures[1].getWidth(), (int)geometricFigures[1].getHeight());
		}
		
		// Condição de derrota.
		if(period == 0)
		{
			canvas.drawBitmap(Backgrounds[1], null, Back, paint);
		}
		
		// Condição de vitória.
		if(hitPoints == totalPoints)
		{
			game = new Fase02(activity);
			activity.setContentView(game);	
		}
	}
	
	public boolean onTouchEvent(MotionEvent event) 
	{	
		if (event.getAction() == MotionEvent.ACTION_DOWN) 
		{
			Log.i(MainActivity.TAG, "down baby down !! ");
		}
		
		if (event.getAction() == MotionEvent.ACTION_MOVE) 
		{
			Log.i(MainActivity.TAG, "SHAKE !!!");
			int c = (int)event.getRawX();
			int d = (int)event.getRawY();
			
			
			// Testes de Colisão da imagem preto e branco de acordo com a sua respectiva imagem colorida. 
			if(object_down.contains(c,d))
			{
				positionX = event.getRawX()-geometricFigures[1].getWidth()/2;
				positionY = event.getRawY()-geometricFigures[1].getHeight()/2;
				
						if(object_down.contains((int)areaObjectsUp[0].exactCenterX(), (int)areaObjectsUp[0].exactCenterY()))
						{
							currentTime = period;
							positionX=areaObjectsUp[0].left;
							positionY=areaObjectsUp[0].top;
						}
			}
		}
		
		return super.onTouchEvent(event);
	}


	public void update()
	{
		if (period !=0)
		{
			counter ++;
		}
		
		if (counter == 1000)
		{
			period-=1;
			counter = 0;
		}
		
		if(currentTime!=0)
		{
			if(currentTime-period>=1 )
			{
				geometricFigures[current]=geometricFigures[6];
				
				// Verificação para não vir nenhuma figura geométrica repetida.
				while(current+3==order[0] || current+3==order[1] )
				{
						current=rnd.nextInt(3);
						
				}
				
				positionX=getWidth()/2;
				positionY=getHeight()/2;
				hitPoints++;
				currentTime=0;	
			 }
		 }
	}
	
	public void run() 
	{
		while(true)
		{
			try
			{	
				Thread.sleep(time);
			}
			
			catch(Exception e)
			{
				Log.e("Deu erro", "Quem sabe mete o pe");
			}
			
			update();
			postInvalidate();
		}
		// TODO Auto-generated method stub
	}
}

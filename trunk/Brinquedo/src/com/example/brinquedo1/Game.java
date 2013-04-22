package com.example.brinquedo1;


import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

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

public  class Game extends View implements Runnable{

	private long time = 1;
	Bitmap [] geometricFigures=new Bitmap[7];
	Bitmap [] Backgrounds;
	int period = 60;
	int counter;
	private Paint paint;	
	private Rect Back = new Rect();
	int totalPoints = 3;
	int hitPoints = 0;	
	Rect object_down ;
	int[] oi = new int[3];
	Rect [] areaObjectsUp = new Rect[3];
	private static float positionX;
	private static float positionY;
	private Canvas MyCanvas;
	private Random rnd = new Random();
	private Boolean pode = true;
	private int current;
	private int currentTime;
	
	public Game(Context context) 
	{
		super(context);
		
		setFocusableInTouchMode(true);
		setClickable(true);
		setLongClickable(true);

		Backgrounds = new Bitmap[3];
		ImageManager img = new ImageManager();
		paint = new Paint();
		
		paint.setColor(Color.BLACK); 
		paint.setTextSize(20); 

			current = rnd.nextInt(3);
			geometricFigures[0] = img.ImageManager("circulo.png", context);
			geometricFigures[1] = img.ImageManager("hexagono.png", context);
			geometricFigures[2] = img.ImageManager("triangulo.png", context);
			geometricFigures[3] = img.ImageManager("circuloColor.png", context);
			geometricFigures[4] = img.ImageManager("hexagonoColor.png", context);
			geometricFigures[5] = img.ImageManager("trianguloColor.png", context);
			geometricFigures[6] = geometricFigures[current+3];
			oi[0] = current+3;
			oi[1]=0;
			oi[2]=0;
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
		
			//canvas.drawBitmap(Backgrounds[2], 0, 0, paint);	
			canvas.drawBitmap(Backgrounds[2], null, Back, paint);
			
			// 3 imagens que ficarão na parte de cima
			canvas.drawBitmap(geometricFigures[0], 0, 0, paint);		
			canvas.drawBitmap(geometricFigures[1], getWidth()/3, 0, paint);
			canvas.drawBitmap(geometricFigures[2], getWidth() - 100 , 0, paint);
			// Imagem que ficará na parte de baixo
			canvas.drawBitmap(geometricFigures[6], positionX, positionY, paint);
	
			// Textos na tela.
			canvas.drawText("Score:" + hitPoints + "/" + totalPoints, getWidth()/12 ,getHeight() - 12, paint);
			canvas.drawText("Time:" +" " +  period , getWidth() - 140, getHeight() - 12, paint);
			
			int a = (int)positionX;
			int b = (int)positionY;
			object_down= new Rect(a,b, a+ (int)geometricFigures[5].getWidth(), b+(int)geometricFigures[3].getHeight());
			
			 areaObjectsUp[0]= new Rect(0,0, 0+ (int)geometricFigures[0].getWidth(), (int)geometricFigures[0].getHeight());
			 areaObjectsUp[1]= new Rect(getWidth()/3,0, (int)getWidth()/3+ (int)geometricFigures[1].getWidth(), (int)geometricFigures[1].getHeight());
			 areaObjectsUp[2]= new Rect(getWidth()-100,0, (int)getWidth()-100+ (int)geometricFigures[2].getWidth(), (int)geometricFigures[2].getHeight());
		}
		
		if(period == 0)
		{
			canvas.drawText("Você Perdeu", getWidth() / 3, getHeight() / 2, paint);
			canvas.drawBitmap(Backgrounds[1], null, Back, paint);
		}
		
		if(hitPoints == totalPoints)
		{
			canvas.drawText("Você Venceu", getWidth() / 3, getHeight() / 2, paint);
			canvas.drawBitmap(Backgrounds[0], null, Back, paint);
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
				positionX = event.getRawX()-geometricFigures[3].getWidth()/2;
				positionY = event.getRawY()-geometricFigures[3].getHeight()/2;
				
				for(int i =0;i<3;i++){
					if(geometricFigures[6] == geometricFigures[i+3])
					{
						if(object_down.contains((int)areaObjectsUp[i].exactCenterX(), (int)areaObjectsUp[i].exactCenterY()))
						{
							currentTime = period;
							positionX=areaObjectsUp[i].left;
							positionY=areaObjectsUp[i].top;
						}
					}
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
				
			
				
				while(current+3==oi[0] || current+3==oi[1] )
				{
						current=rnd.nextInt(3);
						
				}
				
				for(int i = 0 ;i<oi.length;i++){
					if(oi[i]==0){
						oi[i]=current+3;				
						break;
					}
					
				}
				
					
				geometricFigures[6]=geometricFigures[current+3];
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
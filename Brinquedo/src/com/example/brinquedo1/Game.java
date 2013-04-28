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
	int[] order = new int[3];
	Rect [] areaObjectsUp = new Rect[3];
	private static float positionX;
	private static float positionY;
	private Canvas MyCanvas;
	private Random rnd = new Random();
	private int current;
	private int currentTime;
	ImageManager img;
	
	public Game(Context context) 
	{
		super(context);
		
		setFocusableInTouchMode(true);
		setClickable(true);
		setLongClickable(true);

		Backgrounds = new Bitmap[3];
		img = new ImageManager();
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
			geometricFigures[6] = geometricFigures[current + 3];
			order[0] = current + 3;
			order[1]= 0;
			order[2]= 0;
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
			canvas.drawBitmap(geometricFigures[0], 0, 0, paint);		
			canvas.drawBitmap(geometricFigures[1], getWidth()/3, 0, paint);
			canvas.drawBitmap(geometricFigures[2], getWidth() - 100 , 0, paint);
			
			// Imagem que ficará na parte de baixo (figura geométrica em PB).
			canvas.drawBitmap(geometricFigures[6], positionX, positionY, paint);
	
			// Textos na tela.
			canvas.drawText("Score:" + hitPoints + "/" + totalPoints, getWidth()/13 ,getHeight()/1.1f, paint);
			canvas.drawText("Time:" +" " +  period , getWidth()/1.5f, getHeight()/1.1f, paint);
			
			int a = (int)positionX;
			int b = (int)positionY;
			object_down= new Rect(a,b, a+ (int)geometricFigures[5].getWidth(), b+(int)geometricFigures[3].getHeight());
			
			areaObjectsUp[0]= new Rect(0,0, 0+ (int)geometricFigures[0].getWidth(), (int)geometricFigures[0].getHeight());
			areaObjectsUp[1]= new Rect(getWidth()/3,0, (int)getWidth()/3+ (int)geometricFigures[1].getWidth(), (int)geometricFigures[1].getHeight());
			areaObjectsUp[2]= new Rect(getWidth()-100,0, (int)getWidth()-100+ (int)geometricFigures[2].getWidth(), (int)geometricFigures[2].getHeight());
		}
		
		// Condição de derrota.
		if(period == 0)
		{
			canvas.drawBitmap(Backgrounds[1], null, Back, paint);
		}
		
		// Condição de vitória.
		if(hitPoints == totalPoints)
		{
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
				
				for(int i =0;i<3;i++)
				{
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
				
				// Verificação para não vir nenhuma figura geométrica repetida.
				while(current+3==order[0] || current+3==order[1] )
				{
						current=rnd.nextInt(3);
						
				}
				
				for(int i = 0 ;i<order.length;i++){
					if(order[i]==0){
						order[i]=current+3;				
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
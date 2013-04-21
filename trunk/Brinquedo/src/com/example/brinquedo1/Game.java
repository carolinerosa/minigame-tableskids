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
	Bitmap [] geometricFigures;	
	Bitmap [] Backgrounds;
	int period = 60;
	int counter;
	private Paint paint;	
	int totalPoints = 3;
	int hitPoints = 0;	
	Rect object_down ;
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

		geometricFigures = new Bitmap[7];
		Backgrounds = new Bitmap[3];
		
		paint = new Paint();
		paint.setColor(Color.BLACK); 
		paint.setTextSize(20); 

		try 
		{
			current = rnd.nextInt(3);
			InputStream circulo = context.getAssets().open("circulo.png");
			InputStream hexagono = context.getAssets().open("hexagono.png");
			InputStream triangulo = context.getAssets().open("triangulo.png");
			InputStream circulo_colorido = context.getAssets().open("circuloColor.png");
			InputStream hexagono_colorido = context.getAssets().open("hexagonoColor.png");
			InputStream triangulo_colorido = context.getAssets().open("trianguloColor.png");
			InputStream Vitoria = context.getAssets().open("bgCongrats.bmp");
			InputStream Derrota = context.getAssets().open("bgGameOver.bmp");
			InputStream Fundo = context.getAssets().open("Background1.png");
			
			geometricFigures[0] = BitmapFactory.decodeStream(circulo);
			geometricFigures[1] = BitmapFactory.decodeStream(hexagono);
			geometricFigures[2] = BitmapFactory.decodeStream(triangulo);
			geometricFigures[3] = BitmapFactory.decodeStream(circulo_colorido);
			geometricFigures[4] = BitmapFactory.decodeStream(hexagono_colorido);
			geometricFigures[5] = BitmapFactory.decodeStream(triangulo_colorido);
			geometricFigures[6] = geometricFigures[current+3];
			Backgrounds[0] = BitmapFactory.decodeStream(Vitoria);
			Backgrounds[1] = BitmapFactory.decodeStream(Derrota);
			Backgrounds[2] = BitmapFactory.decodeStream(Fundo);
			
		} 
		catch (IOException e) 
		{
			Log.e(MainActivity.TAG, "Erro carregando imagem");
		}
		Thread processo = new Thread(this);
		processo.start();
		
		// TODO Auto-generated constructor stub
	}
	protected void onSizeChanged(int w, int h, int oldw, int oldh) 
	{
		super.onSizeChanged(w, h, oldw, oldh);
		
		positionX= getWidth()/2;
		positionY=getHeight()/2;
	}

	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		
		if(period!=0 && hitPoints != totalPoints)
		{
			MyCanvas=canvas;
			
			canvas.drawBitmap(Backgrounds[2], 0, 0, paint);	
			
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
			canvas.drawBitmap(Backgrounds[1], 0, 0, paint);
		}
		
		if(hitPoints == totalPoints)
		{
			canvas.drawText("Você Venceu", getWidth() / 3, getHeight() / 2, paint);
			canvas.drawBitmap(Backgrounds[0], 0, 0, paint);
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
				current = rnd.nextInt(3);
						
				while(geometricFigures[6]==geometricFigures[current+3])
				{
					current=rnd.nextInt(3);
				}
				
				if(geometricFigures[6]!=geometricFigures[current+3])
				{		
					geometricFigures[6]=geometricFigures[current+3];
					positionX=getWidth()/2;
					positionY=getHeight()/2;
					hitPoints++;
					currentTime=0;
				}
						
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
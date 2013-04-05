package com.example.brinquedo1;

import java.io.IOException;
import java.io.InputStream;

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
	Bitmap [] geometric_figures;	
	int period = 30000;
	private Paint paint;	
	int totalPoints = 4;
	int hitPoints = 0;	
	Rect[] objetosBaixo = new Rect[3];
	Rect [] areasObjetosCima = new Rect[3];
	private static float positionX;
	private static float positionY;
	private Canvas MyCanvas;
	
	public Game(Context context) 
	{
		super(context);
		
		setFocusableInTouchMode(true);
		setClickable(true);
		setLongClickable(true);

		geometric_figures = new Bitmap[7];
		
		paint = new Paint();
		paint.setColor(Color.BLACK); 
		paint.setTextSize(20); 

		try 
		{
			InputStream circulo = context.getAssets().open("circulo.png");
			InputStream hexagono = context.getAssets().open("hexagono.png");
			InputStream triangulo = context.getAssets().open("triangulo.png");
			InputStream circulo_colorido = context.getAssets().open("circuloColor.png");
			InputStream hexagono_colorido = context.getAssets().open("hexagonoColor.png");
			InputStream triangulo_colorido = context.getAssets().open("trianguloColor.png");

			geometric_figures[0] = BitmapFactory.decodeStream(circulo);
			geometric_figures[1] = BitmapFactory.decodeStream(hexagono);
			geometric_figures[2] = BitmapFactory.decodeStream(triangulo);
			geometric_figures[3] = BitmapFactory.decodeStream(circulo_colorido);
			geometric_figures[4] = BitmapFactory.decodeStream(hexagono_colorido);
			geometric_figures[5] = BitmapFactory.decodeStream(triangulo_colorido);

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
		MyCanvas=canvas;
		
		// 3 imagens que ficarão na parte de cima
		canvas.drawBitmap(geometric_figures[0], 0, 0, paint);		
		canvas.drawBitmap(geometric_figures[1], getWidth()/3, 0, paint);
		canvas.drawBitmap(geometric_figures[2], getWidth() - 100 , 0, paint);
		
		// imagem que ficará na parte de baixo
		
		canvas.drawBitmap(geometric_figures[3], positionX, positionY, paint);
	//	canvas.drawBitmap(geometric_figures[4], positionX + 110, positionY, paint);
	//	canvas.drawBitmap(geometric_figures[5], positionX - 110, positionY, paint);
		
		// Textos na tela.
		canvas.drawText("Score:" + hitPoints + "/" + totalPoints, getWidth()/12 ,getHeight() - 12, paint);
		canvas.drawText("Time:" + period , getWidth() - 140, getHeight() - 12, paint);
		int o = (int)getWidth()/3;
		
		int a = (int)positionX;
		int b = (int)positionY;
		objetosBaixo[2]= new Rect(a,b, a+ (int)geometric_figures[3].getWidth(), b+(int)geometric_figures[3].getHeight());
		
		 for (int i = 0; i < areasObjetosCima.length; i ++)
		 {
			 areasObjetosCima[i]= new Rect(o,0, o+ (int)geometric_figures[i].getWidth(), (int)geometric_figures[i].getHeight());
			 canvas.drawRect(areasObjetosCima[i], paint);
		 }
	}
	public boolean onTouchEvent(MotionEvent event) 
	{
	
		if (event.getAction() == MotionEvent.ACTION_DOWN) 
		{
			Log.i(MainActivity.TAG, "down baby down !! ");
			int a = (int)event.getRawX();
			int b = (int)event.getRawY();

		
			if(objetosBaixo[2].contains(a,b))

			{
				//positionX = event.getRawX()-geometric_figures[3].getWidth()/2;
				//positionY = event.getRawY()-geometric_figures[3].getHeight()/2;
			}

		}
		

		if (event.getAction() == MotionEvent.ACTION_MOVE) 
		{
			Log.i(MainActivity.TAG, "SHAKE !!!");
			int a = (int)event.getRawX();
			int b = (int)event.getRawY();
			
			if(objetosBaixo[2].contains(a,b))

			{
				positionX = event.getRawX()-geometric_figures[3].getWidth()/2;
				positionY = event.getRawY()-geometric_figures[3].getHeight()/2;
				
				//if(areasObjetosCima[0].se(objetosbaixo[2]))
		 //		if (area)
				{
					positionX=areasObjetosCima[1].top;
					positionY=areasObjetosCima[1].left;
				}
			}
		}
		
		return super.onTouchEvent(event);
	}

	@Override
	public void run() {
		while(true)
		{
			
			try{
				
				Thread.sleep(time);
			}
			catch(Exception e){
				Log.e("Deu erro", "Quem sabe mete o pe");
			}
			
			update();
			postInvalidate();
		}
		// TODO Auto-generated method stub
		
	}
	
	public void update()
	{
		period-=1;
		
	}
	
	

	
}
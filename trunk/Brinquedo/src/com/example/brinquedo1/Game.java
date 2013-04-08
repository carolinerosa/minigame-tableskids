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
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public  class Game extends View implements Runnable{

	private long time = 1;
	Bitmap [] geometric_figures;	
	Bitmap [] Backgrounds;
	int period = 60;
	int contador;
	private Paint paint;	
	int totalPoints = 3;
	int hitPoints = 0;	
	Rect objetosBaixo ;
	Rect [] areasObjetosCima = new Rect[3];
	private static float positionX;
	private static float positionY;
	private Canvas MyCanvas;
	private Random rnd = new Random();
	private int current;
	private int currentTime;

	public Game(Context context) 
	{
		super(context);
		
		setFocusableInTouchMode(true);
		setClickable(true);
		setLongClickable(true);

		geometric_figures = new Bitmap[7];
		Backgrounds = new Bitmap[3];
		
		
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
			InputStream Vitoria = context.getAssets().open("bgCongrats.bmp");
			InputStream Derrota = context.getAssets().open("bgGameOver.bmp");
			InputStream Fundo = context.getAssets().open("Background1.png");
			
			geometric_figures[0] = BitmapFactory.decodeStream(circulo);
			geometric_figures[1] = BitmapFactory.decodeStream(hexagono);
			geometric_figures[2] = BitmapFactory.decodeStream(triangulo);
			geometric_figures[3] = BitmapFactory.decodeStream(circulo_colorido);
			geometric_figures[4] = BitmapFactory.decodeStream(hexagono_colorido);
			geometric_figures[5] = BitmapFactory.decodeStream(triangulo_colorido);
			geometric_figures[6] = geometric_figures[rnd.nextInt(3)+3];
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
		
		if(period!=0 && hitPoints != totalPoints){
		MyCanvas=canvas;
		
		canvas.drawBitmap(Backgrounds[2], 0, 0, paint);	
		// 3 imagens que ficarão na parte de cima
		canvas.drawBitmap(geometric_figures[0], 0, 0, paint);		
		canvas.drawBitmap(geometric_figures[1], getWidth()/3, 0, paint);
		canvas.drawBitmap(geometric_figures[2], getWidth() - 100 , 0, paint);
		
		// imagem que ficará na parte de baixo
		canvas.drawBitmap(geometric_figures[6], positionX, positionY, paint);

		// Textos na tela.
		canvas.drawText("Score:" + hitPoints + "/" + totalPoints, getWidth()/12 ,getHeight() - 12, paint);
		canvas.drawText("Time:" +" " +  period , getWidth() - 140, getHeight() - 12, paint);
		int o = (int)getWidth()/3;
		
		int a = (int)positionX;
		int b = (int)positionY;
		objetosBaixo= new Rect(a,b, a+ (int)geometric_figures[5].getWidth(), b+(int)geometric_figures[3].getHeight());
		
		 for (int i = 0; i < areasObjetosCima.length; i ++)
		 {
			 areasObjetosCima[i]= new Rect(o,0, o+ (int)geometric_figures[i].getWidth(), (int)geometric_figures[i].getHeight());
			 //canvas.drawRect(areasObjetosCima[i], paint);
		 }
		}
		if(period == 0){
			canvas.drawText("Você Perdeu", getWidth() / 3, getHeight() / 2, paint);
			canvas.drawBitmap(Backgrounds[1], 0, 0, paint);
		}
		if(hitPoints == totalPoints){
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
			int a = (int)event.getRawX();
			int b = (int)event.getRawY();
			
			if(objetosBaixo.contains(a,b))

			{
				positionX = event.getRawX()-geometric_figures[3].getWidth()/2;
				positionY = event.getRawY()-geometric_figures[3].getHeight()/2;
				if(objetosBaixo.contains((int)areasObjetosCima[1].exactCenterX(), (int)areasObjetosCima[1].exactCenterY()))
				{
					currentTime = period;
					positionX=areasObjetosCima[1].left;
					positionY=areasObjetosCima[1].top;
					
					
					
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
		if (period !=0){
			contador ++;
		}
		
		if (contador == 1000){
			period-=1;
			contador = 0;
		}
		
		if(currentTime!=0){
		if(currentTime-period>=1){
						
						
						current =rnd.nextInt(3);
						
						while(geometric_figures[6]==geometric_figures[current+3])
						{
							current=rnd.nextInt(3);
						}
						if(geometric_figures[6]!=geometric_figures[current+3]){
								geometric_figures[6]=geometric_figures[current+3];
								positionX=getWidth()/2;
								positionY=getHeight()/2;
								hitPoints++;
								currentTime=0;
						
						}
						
					}
		}
	}
	
	

	
}
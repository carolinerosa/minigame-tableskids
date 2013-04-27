package com.example.brinquedo1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;


import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

// Classe responsável pelo gerenciamento das imagens. 
public  class ImageManager  
{
	Bitmap  geometric_figures;	
	int positionX;
	int positionY;
	
	// Método pra carregar imagem da pasta Assets.
	public  Bitmap ImageManager(String name, Context context) 
	{
		try 
		{
			InputStream img = context.getAssets().open(name);
			geometric_figures = BitmapFactory.decodeStream(img);
		}
		
		catch (IOException e) 
		{
			Log.e("fooi", "Erro carregando imagem");
		}
		return geometric_figures;
	}
}

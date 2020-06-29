//Класс с паттерном "Одиночка"

package com.example.myapplication_makeup.makeupapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class MakeupService {
	//базоввая ссылка
	//вс¤ ссылка: http://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline
	private static final String BASE_URL = "http://makeup-api.herokuapp.com/api/v1/";

	private static MakeupService mInstance;
	private Retrofit retrofit;
	
	private MakeupService(){
		retrofit = new Retrofit
							.Builder()
							.baseUrl(BASE_URL)
							.addConverterFactory(GsonConverterFactory.create())
							.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
							.build();
	}
	
	public static MakeupService getInstance(){
		if(mInstance == null)
			mInstance = new MakeupService();
		
		return mInstance;
	}
	
	public MakeupAPI getMakeupApi(){
		return retrofit.create(MakeupAPI.class);
	}
}
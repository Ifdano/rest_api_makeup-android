//Сам запрос по API

package com.example.myapplication_makeup.makeupapi;

import retrofit2.http.GET;
import retrofit2.http.Query;

import io.reactivex.Flowable;

import java.util.ArrayList;

public interface MakeupAPI {
	//общий запрос: http://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline&product_type=blush&price_greater_than=12&price_less_than=20 
	@GET("products.json")
	Flowable<ArrayList<MakeupProduct>> getMakeupProducts(
			@Query("brand") String brand,
			@Query("product_type") String product_type,
			@Query("price_greater_than") String min_price,
			@Query("price_less_than") String max_price
		);
}
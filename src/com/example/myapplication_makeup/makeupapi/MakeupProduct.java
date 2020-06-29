//Класс POJO для получаемых данных 
//Пример получаемых данных: http://makeup-api.herokuapp.com/api/v1/products.json?brand=maybelline

package com.example.myapplication_makeup.makeupapi;

import com.google.gson.annotations.SerializedName;

public class MakeupProduct {
	@SerializedName("id")
	private int id;
	
	@SerializedName("brand")
	private String brand;
	
	@SerializedName("name")
	private String name;
	
	@SerializedName("price")
	private String price;
	
	@SerializedName("description")
	private String description;
	
	@SerializedName("image_link")
	private String image_link;
	
	@SerializedName("created_at")
	private String created_at;
	
	public MakeupProduct(){}
	
	public int getId(){ return id; }
	public String getBrand(){ return brand; }
	public String getName(){ return name; }
	public String getPrice(){ return price; }
	public String getDescription(){ return description; }
	public String getImage(){ return image_link; }
	public String getCreatedDate(){ return created_at; }

}
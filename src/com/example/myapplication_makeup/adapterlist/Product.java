//Класс POJO для элементов списка
//Каждый пункт списка это название товара и изображение

package com.example.myapplication_makeup.adapterlist;

public class Product {
	private String name;
	private String imageUrl;
	
	public Product(String name, String imageUrl){
		this.name = name;
		this.imageUrl = imageUrl;
	}
	
	public String getName(){ return name; }
	public String getImageUrl(){ return imageUrl; }
}
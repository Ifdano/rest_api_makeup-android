//Кастомный адаптер для списка

package com.example.myapplication_makeup.adapterlist;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.widget.ImageView;
import android.widget.TextView;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import com.example.myapplication_makeup.R;

public class CustomAdapter extends ArrayAdapter{
	private Context context;
	private int layoutId;
	private ArrayList<Product> products;
	
	public CustomAdapter(Context context, int layoutId, ArrayList<Product> products){
		super(context, layoutId, products);
		
		this.context = context;
		this.layoutId = layoutId;
		this.products = products;
	}
	
	public View getView(int position, View customView, ViewGroup container){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(layoutId, container, false);
		
		Product product = products.get(position);
		
		TextView textProduct = (TextView)view.findViewById(R.id.textProduct);
		ImageView imageProduct = (ImageView)view.findViewById(R.id.imageProduct);
		
		textProduct.setText(product.getName());
		
		Callback callback = new Callback(){
			public void onSuccess(){}
			
			public void onError(){}
		};
		
		Picasso
			.with(context)
			.load(product.getImageUrl())
			.placeholder(R.drawable.loadingimage)
			.error(R.drawable.errorimage)
			.into(imageProduct);
			
		return view;
	}
}
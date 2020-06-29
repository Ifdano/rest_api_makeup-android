//Класс с формой для заполнения

package com.example.myapplication_makeup;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;

import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;

import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import android.content.Intent;

import android.util.Log;

import com.example.myapplication_makeup.dialogs.FormDialog;

public class MainForm extends FragmentActivity implements OnTouchListener, OnItemSelectedListener{
	//ключи для передачи данных формы
	public static final String KEY_BRAND = "brand";
	public static final String KEY_PRODUCT = "product";
	public static final String KEY_MIN_PRICE = "min_price";
	public static final String KEY_MAX_PRICE = "max_price";
	
	//ключ для уточнения, что мы перешли из данного окна
	public static final String KEY_FROM_FROM = "from_form";

	//компоненты
	private Button buttonLoad;
	
	private Spinner spinnerBrands;
	private Spinner spinnerProducts;
	
	private EditText editMinPrice;
	private EditText editMaxPrice;
	
	//адаптеры для выпадающих списков
	private ArrayAdapter<String> brandsAdapter;
	private ArrayAdapter<String> productsAdapter;
	
	//данные формы
	private String selectedBrand;
	private String selectedProduct;
	private String minPrice;
	private String maxPrice;
	
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_form);
		
		//начальная инициализация
		init();
	}
	
	//начальная инициализация
	public void init(){
		//находим компоненты и устанавливаем слушателей
		buttonLoad = (Button)findViewById(R.id.buttonLoad);
		
		spinnerBrands = (Spinner)findViewById(R.id.spinnerBrand);
		spinnerProducts = (Spinner)findViewById(R.id.spinnerProducts);
		
		editMinPrice = (EditText)findViewById(R.id.editPriceMin);
		editMaxPrice = (EditText)findViewById(R.id.editPriceMax);
		
		buttonLoad.setOnTouchListener(this);
		spinnerBrands.setOnItemSelectedListener(this);
		spinnerProducts.setOnItemSelectedListener(this);
		
		selectedBrand = "";
		selectedProduct = "";
		minPrice = "";
		maxPrice = "";
		
		//установка выпадающих списков
		String[] brands = getResources().getStringArray(R.array.brands);
		String[] products = getResources().getStringArray(R.array.products);
		
		brandsAdapter = new ArrayAdapter<String>(
					this,
					R.layout.brand_spinner_item,
					R.id.textBrands,
					brands
				);
		spinnerBrands.setAdapter(brandsAdapter);
		
		productsAdapter = new ArrayAdapter<String>(
					this,
					R.layout.products_spinner_item,
					R.id.textProducts,
					products
				);
		
		spinnerProducts.setAdapter(productsAdapter);
	}
	
	//метод для перехода на окно загрузки данных
	public void loadData(){
		intent = new Intent(this, MainList.class);
		
		//получаем данные из полей
		String minPrice = editMinPrice.getText().toString().trim();
		String maxPrice = editMaxPrice.getText().toString().trim();
		
		//передаем данные в окно загрузки
		intent.putExtra(KEY_BRAND, selectedBrand);
		intent.putExtra(KEY_PRODUCT, selectedProduct);
		intent.putExtra(KEY_MIN_PRICE, minPrice);
		intent.putExtra(KEY_MAX_PRICE, maxPrice);
		
		/*для того, чтобы определить перешли мы из окна с заполнением формы или нет
		  это нужно, чтобы знать: делать запрос по api, чтобы получить данные или нет*/
		intent.putExtra(KEY_FROM_FROM, 1);
		
		startActivity(intent);
	}
	
	//обработка выбора выпадающих списков
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
		//получаем данные из выпадающих списков
		selectedBrand = (String)spinnerBrands.getSelectedItem();
		selectedProduct = (String)spinnerProducts.getSelectedItem();
	}
	
	public void onNothingSelected(AdapterView<?> parent){}
	
	public boolean onTouch(View view, MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//кнопка запроса данных по данным из формы
			if(view == buttonLoad){
				//диалоговое окно для подтверждения запроса
				FragmentManager manager = getSupportFragmentManager();
				FormDialog dialog = new FormDialog();
				dialog.show(manager, "form_dialog");
			}
		}
		
		return false;
	}
}
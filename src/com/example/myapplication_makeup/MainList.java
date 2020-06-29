//Класс со списком загруженных товаров

package com.example.myapplication_makeup;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import android.content.Intent;
import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.annotations.NonNull;

import android.util.Log;

import java.util.ArrayList;

import com.example.myapplication_makeup.adapterlist.CustomAdapter;
import com.example.myapplication_makeup.adapterlist.Product;

import com.example.myapplication_makeup.data.MakeupContract.MakeupEntry;
import com.example.myapplication_makeup.data.MakeupDbHelper;

import com.example.myapplication_makeup.dialogs.ListDialogDelete;
import com.example.myapplication_makeup.dialogs.ListDialogDeleteAll;

import com.example.myapplication_makeup.makeupapi.MakeupProduct;
import com.example.myapplication_makeup.makeupapi.MakeupService;

public class MainList extends FragmentActivity implements OnTouchListener, OnItemClickListener, OnItemLongClickListener{
	//ключи для передачи данных формы
	public static final String KEY_BRAND = "brand";
	public static final String KEY_PRODUCT = "product";
	public static final String KEY_MIN_PRICE = "min_price";
	public static final String KEY_MAX_PRICE = "max_price";
	
	//ключ для уточнения, что мы перешли из данного окна
	public static final String KEY_FROM_FORM = "from_form";
	
	//ключ для уточнения: какой элемент из списка мы выбрали
	public static final String KEY_ID = "key_id";
	
	//компоненты
	private Button buttonClear;
	private Button buttonDelete;
	private Button buttonBack;
	
	//список товаров
	private ListView listProducts;
	private CustomAdapter customAdapter;
	//массив с товарами
	private ArrayList<Product> products;
	//массив с id товаров [id по базе данных]
	private ArrayList<Integer> productsId;
	
	private Intent intent;
	
	//данные из форм, которые мы передали в это окно
	private String selectedBrand;
	private String selectedProduct;
	private String minPrice;
	private String maxPrice;
	private int fromForm;
	
	//id товара, который мы хотим удалить
	private int productIdForDelete;
	
	//база данных
	private MakeupDbHelper DbHelper;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		//начальная инициализация
		init();
		//получение данных из форм
		getFormData();
	}
	
	protected void onStart(){
		super.onStart();
	
		//очистка массивов с товарами
		products.clear();
		productsId.clear();
		
		//отображение данных из базы данных
		displayDatabaseInfo();
		
		//запрос по API, чтобы получить товары
		getMakeupData();
	}
	
	//начальная инициализация
	public void init(){
		//находим компоненты и устанавливаем слушателей
		buttonClear = (Button)findViewById(R.id.buttonClear);
		buttonDelete = (Button)findViewById(R.id.buttonDelete);
		buttonBack = (Button)findViewById(R.id.buttonBack);
		
		listProducts = (ListView)findViewById(R.id.listProducts);
		
		buttonClear.setOnTouchListener(this);
		buttonDelete.setOnTouchListener(this);
		buttonBack.setOnTouchListener(this);
		
		listProducts.setOnItemClickListener(this);
		listProducts.setOnItemLongClickListener(this);
		
		selectedBrand = "";
		selectedProduct = "";
		minPrice = "";
		maxPrice = "";
		fromForm = -1;
		
		productsId = new ArrayList<Integer>();
		products = new ArrayList<Product>();
		
		DbHelper = new MakeupDbHelper(this);
		
		productIdForDelete = -1;
	}
	
	//получение данных из форм
	public void getFormData(){
		intent = getIntent();
		
		fromForm = intent.getIntExtra(KEY_FROM_FORM, -1);
		
		//получать данные будем, если перешли из окна с заполнением форм
		if(fromForm == 1){
			//если выбрали все фирмы, то строка будет пустая
			String tempSelectedBrand = intent.getStringExtra(KEY_BRAND);
			if(!tempSelectedBrand.contains("all"))
				selectedBrand = tempSelectedBrand;

			//если выбрали все продукты, то строка будет пустая
			String tempSelectedProduct = intent.getStringExtra(KEY_PRODUCT);
			if(!tempSelectedProduct.contains("all"))
				selectedProduct = tempSelectedProduct;
		
			minPrice = intent.getStringExtra(KEY_MIN_PRICE);
			maxPrice = intent.getStringExtra(KEY_MAX_PRICE);
		}
	}
	
	public void getMakeupData(){
		/*проверяем, перешли ли мы из окна с формой для заполнения
		  проверка нужна, чтобы убедиться, что мы запросили данные, а не просто
		  перешли на экран с продукциями*/
		if(fromForm == 1){
			/*сначала очищаем базу данных, чтобы новые данные
			  не смешались со старыми*/
			deleteAllProducts();
			
			fromForm = -1;
			
			//получение данных
			Subscriber<ArrayList<MakeupProduct>> subscriber = new Subscriber<ArrayList<MakeupProduct>>(){
				public void onNext(@NonNull ArrayList<MakeupProduct> makeupProducts){
					if(makeupProducts.size() > 0){
						//пробегаемся по загруженным товарам
						for(int i = 0; i < makeupProducts.size(); i++){
							//берем товар
							MakeupProduct product = makeupProducts.get(i);
							
							//получаем его данные
							int id = product.getId();
							String brand = product.getBrand();
							String productName = product.getName();
							String price = product.getPrice();
							String description = product.getDescription();
							
							String tempCreatedDate = product.getCreatedDate();
							String createdDate = tempCreatedDate.substring(0, 10);
							
							String image = product.getImage();
							
							//добавляем данные в базу данных
							SQLiteDatabase db = DbHelper.getWritableDatabase();
							ContentValues values = new ContentValues();
							
							values.put(MakeupEntry.COLUMN_ID, id);
							values.put(MakeupEntry.COLUMN_BRAND, brand);
							values.put(MakeupEntry.COLUMN_PRODUCT_NAME, productName);
							values.put(MakeupEntry.COLUMN_PRICE, price);
							values.put(MakeupEntry.COLUMN_DESCRIPTION, description);
							values.put(MakeupEntry.COLUMN_CREATED, createdDate);
							values.put(MakeupEntry.COLUMN_IMAGE_URL, image);
							
							long newRowId = db.insert(
									MakeupEntry.TABLE_NAME,
									null,
									values
								);
							
							//обновляем массивы
							products.clear();
							productsId.clear();
							
							//повторно выводим товары на экран
							displayDatabaseInfo();
							
						}
					}else{
						Toast.makeText(
								getApplicationContext(),
								"Подходящих товаров не найдено",
								Toast.LENGTH_LONG
							).show();
					}
				}
				
				public void onError(Throwable e){
					Toast.makeText(
							getApplicationContext(),
							"Ошибка получения данных: " + e,
							Toast.LENGTH_LONG
						).show();
				}
				
				public void onComplete(){
					Toast.makeText(
							getApplicationContext(),
							"Данные успешно загружены и будут выведены на экран!",
							Toast.LENGTH_LONG
						).show();
				}
				
				public void onSubscribe(Subscription s){
					s.request(Long.MAX_VALUE);
				}
			};
			
			//сам запрос
			//передаем название фирмы, тип продукта и цены
			MakeupService
					.getInstance()
					.getMakeupApi()
					.getMakeupProducts(selectedBrand, selectedProduct, minPrice, maxPrice)
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(subscriber);
					
		}
	}
	
	//отображение данных из базы данных
	public void displayDatabaseInfo(){
		SQLiteDatabase db = DbHelper.getReadableDatabase();
		
		String[] projection = {
				MakeupEntry._ID,
				MakeupEntry.COLUMN_ID, 
				MakeupEntry.COLUMN_PRODUCT_NAME,
				MakeupEntry.COLUMN_IMAGE_URL
		};
		
		Cursor cursor = db.query(
				MakeupEntry.TABLE_NAME,
				projection,
				null,
				null,
				null,
				null,
				null
			);
		
		try{
		
			int idIndex = cursor.getColumnIndex(MakeupEntry._ID);
			int idProductIndex = cursor.getColumnIndex(MakeupEntry.COLUMN_ID);
			int productIndex = cursor.getColumnIndex(MakeupEntry.COLUMN_PRODUCT_NAME);
			int imageIndex = cursor.getColumnIndex(MakeupEntry.COLUMN_IMAGE_URL);
		
			while(cursor.moveToNext()){
				//получаем данные каждого товара из базы данных
				int idProduct = cursor.getInt(idIndex);
				int idCurrentProduct = cursor.getInt(idProductIndex);
				String productName = cursor.getString(productIndex);
				String productImage = cursor.getString(imageIndex);
				
				//формирующая строка для списка
				String tempProduct = idCurrentProduct + ". " + productName;
				
				//добавляем название товара и его id в список
				products.add(new Product(tempProduct, productImage));
				//добавляет id товара в массив
				productsId.add(idProduct);
				
				customAdapter = new CustomAdapter(
							this,
							R.layout.product_list_item_1,
							products
						);
				
				listProducts.setAdapter(customAdapter);
				
				customAdapter.notifyDataSetChanged();
			}
			
			
		}catch(Exception ex){
			
		}finally{
			db.close();
			cursor.close();
		}
	}
	
	//удаление выбранного товара
	public void deleteProduct(){
		if(productIdForDelete >= 0){
			SQLiteDatabase db = DbHelper.getWritableDatabase();
			
			//удаляем по id
			db.delete(
					MakeupEntry.TABLE_NAME,
					MakeupEntry._ID + "=?",
					new String[]{"" + productIdForDelete}
				);
			
			//обновляем массивы с товарами
			products.clear();
			productsId.clear();
			
			//сообщаем адаптеру об изменении данных
			customAdapter.notifyDataSetChanged();
			
			//повторно выводим список товаров на экран
			displayDatabaseInfo();
			
			productIdForDelete = -1;
			
			Toast.makeText(
					this, 
					"Удалено!",
					Toast.LENGTH_LONG
				).show();
		}else{
			Toast.makeText(
					this, 
					"Ошибка удаления продукции!",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//полное очищение всех товаров из базы данных
	public void deleteAllProducts(){
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		
		if(productsId.size() > 0){
			//пробегаемся по всем товарам по id и удаляем
			for(int i = 0; i < productsId.size(); i++){
				db.delete(
						MakeupEntry.TABLE_NAME,
						MakeupEntry._ID + "=?",
						new String[]{"" + productsId.get(i)}
					);
			}
			
			products.clear();
			productsId.clear();
			
			customAdapter.notifyDataSetChanged();
			
			displayDatabaseInfo();
			
			/*
			Toast.makeText(
					this, 
					"Очищено!!",
					Toast.LENGTH_LONG
				).show();
				*/
		}else{
			/*
			Toast.makeText(
					this, 
					"Ошибка очистки!!",
					Toast.LENGTH_LONG
				).show();
				*/
		}
	}
	
	//при выборе товара из списка - переходим в окно с подробной информацией о товаре
	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		intent = new Intent(this, MainInfo.class);
		
		//id, чтобы мы знали, информацию о каком товаре выводить
		int productId = productsId.get(position);
		
		//передаем id в другое окно
		intent.putExtra(KEY_ID, productId);
		startActivity(intent);
	}
	
	//при долгом нажатиии на товар - можно его удалить
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
		//узнаем id элемента, на который мы нажали
		productIdForDelete = productsId.get(position);
		
		//выводим диалоговое окно о подтверждении удаления
		FragmentManager manager = getSupportFragmentManager();
		ListDialogDelete dialog = new ListDialogDelete();
		dialog.show(manager, "list_dialog_delete");
		
		return true;
	}
	
	public boolean onTouch(View view, MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//очистка данных из базы данных
			if(view == buttonClear){
				FragmentManager manager = getSupportFragmentManager();
				ListDialogDeleteAll dialog = new ListDialogDeleteAll();
				dialog.show(manager, "list_dialog_delete_all");
			}
			
			//удаление товара из списка
			if(view == buttonDelete){
				Toast.makeText(
						getApplicationContext(),
						"Для удаления товара - удерживайте его нажатым",
						Toast.LENGTH_LONG
					).show();
			}
			
			//возврат в окно с заполнения формы
			if(view == buttonBack){
				intent = new Intent(this, MainForm.class);
				startActivity(intent);
			}
		}
		
		return false;
	}
}
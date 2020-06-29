//Класс с подробной информацией о выбранном товаре

package com.example.myapplication_makeup;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication_makeup.data.MakeupContract.MakeupEntry;
import com.example.myapplication_makeup.data.MakeupDbHelper;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;

import android.content.Intent;

import com.example.myapplication_makeup.dialogs.InfoDialogDelete;

public class MainInfo extends FragmentActivity implements OnTouchListener{
	//переданный id товара, подробную информацию которого мы выведем
	public static final String KEY_ID = "key_id";
	
	//компоненты
	private Button buttonUpdate;
	private Button buttonDelete;
	private Button buttonBack;
	
	private ImageView productImage;
	
	private TextView textBrand;
	private TextView textProduct;
	private TextView textPrice;
	private TextView textDescription;
	private TextView textCreated;
	
	private Intent intent;
	
	//база данных
	private MakeupDbHelper DbHelper;
	
	//id товара, который мы выбрали
	private int currentId;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		init();
		//определение id товара
		getCurrentId();
	}
	
	//начальная инициализация
	public void init(){
		//находим компоненты и устанавливаем слушателей
		buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
		buttonDelete = (Button)findViewById(R.id.buttonDelete);
		buttonBack = (Button)findViewById(R.id.buttonBack);
		
		productImage = (ImageView)findViewById(R.id.image);
		
		textBrand = (TextView)findViewById(R.id.textBrand);
		textProduct = (TextView)findViewById(R.id.textName);
		textPrice = (TextView)findViewById(R.id.textPrice);
		textDescription = (TextView)findViewById(R.id.textDescription);
		textCreated = (TextView)findViewById(R.id.textCreated);
		
		buttonUpdate.setOnTouchListener(this);
		buttonDelete.setOnTouchListener(this);
		buttonBack.setOnTouchListener(this);
		
		DbHelper = new MakeupDbHelper(this);
		
		currentId = -1;
	}
	
	//получение id товара, информацию которого мы выведем
	public void getCurrentId(){
		intent = getIntent();
		
		//получаем переданный id
		currentId = intent.getIntExtra(KEY_ID, -1);
		
		//если id корректное - то выводим данные по товару
		if(currentId >= 0)
			displayDatabaseInfo();
		else{
			Toast.makeText(
					getApplicationContext(),
					"Ошибка получения ID!",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	//выводим данные по товару из базы данных по id
	public void displayDatabaseInfo(){
		SQLiteDatabase db = DbHelper.getWritableDatabase();
		
		String[] projection = {
				MakeupEntry.COLUMN_BRAND,
				MakeupEntry.COLUMN_PRODUCT_NAME,
				MakeupEntry.COLUMN_PRICE,
				MakeupEntry.COLUMN_DESCRIPTION,
				MakeupEntry.COLUMN_CREATED,
				MakeupEntry.COLUMN_IMAGE_URL
		};
		
		String selection = MakeupEntry._ID + "=?";
		String[] selectionArgs = {"" + currentId};
		
		Cursor cursor = db.query(
					MakeupEntry.TABLE_NAME,
					projection,
					selection,
					selectionArgs,
					null,
					null,
					null
				);
		
		try{
			
			int brandIndex = cursor.getColumnIndex(MakeupEntry.COLUMN_BRAND);
			int nameIndex = cursor.getColumnIndex(MakeupEntry.COLUMN_PRODUCT_NAME);
			int priceIndex = cursor.getColumnIndex(MakeupEntry.COLUMN_PRICE);
			int descriptionIndex = cursor.getColumnIndex(MakeupEntry.COLUMN_DESCRIPTION);
			int createdIndex = cursor.getColumnIndex(MakeupEntry.COLUMN_CREATED);
			int imageIndex = cursor.getColumnIndex(MakeupEntry.COLUMN_IMAGE_URL);
			
			while(cursor.moveToNext()){
				//получаем данные из базы данных
				String currentBrand = cursor.getString(brandIndex);
				String currentName = cursor.getString(nameIndex);
				String currentPrice = cursor.getString(priceIndex);
				String currentDescription = cursor.getString(descriptionIndex);
				String currentCreated = cursor.getString(createdIndex);
				String currentImage = cursor.getString(imageIndex);
				
				//для загрузки изображения с Picasso
				Callback callback = new Callback(){
					public void onSuccess(){}
					
					public void onError(){}
				};
				
				//берем ссылку изобрежения товара из базы данных и загружаем
				Picasso
					.with(getApplicationContext())
					.load(currentImage)
					.placeholder(R.drawable.loadingimage)
					.error(R.drawable.errorimage)
					.into(productImage);
				
				//устанавливаем в текстовые поля данные по товару
				textBrand.setText(currentBrand);
				textProduct.setText(currentName);
				textPrice.setText(currentPrice);
				textDescription.setText(currentDescription);
				textCreated.setText(currentCreated);
			}
			
		}catch(Exception ex){
			
		}finally{
			db.close();
			cursor.close();
		}
	}
	
	//удаление товара
	public void deleteProduct(){
		if(currentId >= 0){
			SQLiteDatabase db = DbHelper.getWritableDatabase();
			
			//удаляем по id
			db.delete(
					MakeupEntry.TABLE_NAME,
					MakeupEntry._ID + "=?",
					new String[]{"" + currentId}
				);
			
			Toast.makeText(
					getApplicationContext(),
					"Удалено!",
					Toast.LENGTH_LONG
				).show();
			
			//после удаления возвращаеся на экран со списком товаров
			intent = new Intent(this, MainList.class);
			startActivity(intent);
		}else{
			Toast.makeText(
					getApplicationContext(),
					"Ошибка удаления!",
					Toast.LENGTH_LONG
				).show();
		}
	}
	
	public boolean onTouch(View view, MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(view == buttonUpdate){
				//для обновления данных по товару - переходим в окно с обновлением
				intent = new Intent(this, MainUpdate.class);
				
				//не забываем передать id товара
				intent.putExtra(KEY_ID, currentId);
				
				startActivity(intent);
			}
			
			//удаление товара из базы данных
			if(view == buttonDelete){
				//диалоговое окно с подтверждением удаления товара
				FragmentManager manager = getSupportFragmentManager();
				InfoDialogDelete dialog = new InfoDialogDelete();
				dialog.show(manager, "info_dialog_delete");
			}
			
			//возврат в окно со списком товаров
			if(view == buttonBack){
				intent = new Intent(this, MainList.class);
				startActivity(intent);
			}
		}
		
		return false;
	}
	
}
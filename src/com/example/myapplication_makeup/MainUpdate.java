//Класс с обновлением информации о товаре

package com.example.myapplication_makeup;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

import android.content.Intent;
import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication_makeup.data.MakeupContract.MakeupEntry;
import com.example.myapplication_makeup.data.MakeupDbHelper;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import com.example.myapplication_makeup.dialogs.UpdateDialog;
import com.example.myapplication_makeup.dialogs.UpdateDialogBack;
import com.example.myapplication_makeup.dialogs.UpdateDialogBackMain;

public class MainUpdate extends FragmentActivity implements OnTouchListener{
	//id товара, информацию которого мы хотим обновить
	public static final String KEY_ID = "key_id";
	
	private Button buttonUpdate;
	private Button buttonBack;
	private Button buttonMain;
	
	private ImageView productImage;
	
	private EditText editBrand;
	private EditText editProduct;
	private EditText editPrice;
	private EditText editDescription;
	private EditText editCreated;
	private EditText editImage;
	
	private Intent intent;
	
	private MakeupDbHelper DbHelper;
	
	private int currentId;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update);
		
		init();
		getCurrentId();
	}
	
	public void init(){
		buttonUpdate = (Button)findViewById(R.id.buttonUpdate);
		buttonBack = (Button)findViewById(R.id.buttonBack);
		buttonMain = (Button)findViewById(R.id.buttonMain);
		
		productImage = (ImageView)findViewById(R.id.image);
		
		editBrand = (EditText)findViewById(R.id.editBrand);
		editProduct = (EditText)findViewById(R.id.editName);
		editPrice = (EditText)findViewById(R.id.editPrice);
		editDescription = (EditText)findViewById(R.id.editDescription);
		editCreated = (EditText)findViewById(R.id.editCreated);
		editImage = (EditText)findViewById(R.id.editImage);
		
		buttonUpdate.setOnTouchListener(this);
		buttonBack.setOnTouchListener(this);
		buttonMain.setOnTouchListener(this);
		
		DbHelper = new MakeupDbHelper(this);
		
		currentId = -1;
	}
	
	/*метод, который будет вызван из диалогового окна, чтобы
	  перейти на предыдущий экран*/
	public void setBack(){
		intent = new Intent(this, MainInfo.class);
		
		intent.putExtra(KEY_ID, currentId);
		
		startActivity(intent);
	}
	
	/*метод, который будет вызван из диалогового окна, чтобы
	  вернуться на экран со списком товаров*/
	public void setMainBack(){
		intent = new Intent(this, MainList.class);
		startActivity(intent);
	}
	
	//получение id товара
	public void getCurrentId(){
		intent = getIntent();
		
		currentId = intent.getIntExtra(KEY_ID, -1);
		
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
	
	//заполнение полей для дальнейшего обновления данных
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
				//берем данные
				String currentBrand = cursor.getString(brandIndex);
				String currentName = cursor.getString(nameIndex);
				String currentPrice = cursor.getString(priceIndex);
				String currentDescription = cursor.getString(descriptionIndex);
				String currentCreated = cursor.getString(createdIndex);
				String currentImage = cursor.getString(imageIndex);
				
				Callback callback = new Callback(){
					public void onSuccess(){}
					
					public void onError(){}
				};
				
				Picasso
					.with(getApplicationContext())
					.load(currentImage)
					.placeholder(R.drawable.loadingimage)
					.error(R.drawable.errorimage)
					.into(productImage);
				
				//и заполняем поля данными
				editBrand.setText(currentBrand);
				editProduct.setText(currentName);
				editPrice.setText(currentPrice);
				editDescription.setText(currentDescription);
				editCreated.setText(currentCreated);
				editImage.setText(currentImage);
			}
			
		}catch(Exception ex){
			
		}finally{
			db.close();
			cursor.close();
		}
	}
	
	//внесение изменений в товар
	public void insertProduct(){
		//получаем данные из полей
		String tempBrand = editBrand.getText().toString().trim();
		String tempProductName = editProduct.getText().toString().trim();
		String tempPrice = editPrice.getText().toString().trim();
		String tempDescription = editDescription.getText().toString().trim();
		String tempCreated = editCreated.getText().toString().trim();
		String tempImage = editImage.getText().toString().trim();
		
		//если все поля заполнены
		if(tempBrand.length() > 0 && tempProductName.length() > 0 && tempPrice.length() > 0 &&
		   tempDescription.length() > 0 && tempCreated.length() > 0 && tempImage.length() > 0){
			
			//открываем базу данных
			SQLiteDatabase db = DbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			
			//указываем какие данные мы хотим изменить
			values.put(MakeupEntry.COLUMN_BRAND, tempBrand);
			values.put(MakeupEntry.COLUMN_PRODUCT_NAME, tempProductName);
			values.put(MakeupEntry.COLUMN_PRICE, tempPrice);
			values.put(MakeupEntry.COLUMN_DESCRIPTION, tempDescription);
			values.put(MakeupEntry.COLUMN_CREATED, tempCreated);
			values.put(MakeupEntry.COLUMN_IMAGE_URL, tempImage);
			
			//и делаем запрос на обновление информации о товаре
			db.update(
					MakeupEntry.TABLE_NAME,
					values,
					MakeupEntry._ID + "=?",
					new String[]{"" + currentId}
				);
			
			//и сразу возвращаемся на экран с подробной информацией о товаре
			intent = new Intent(this, MainInfo.class);
			
			intent.putExtra(KEY_ID, currentId);
			startActivity(intent);
		}else{
			Toast.makeText(
					getApplicationContext(),
					"Заполните все поля!",
					Toast.LENGTH_LONG
				).show();
		}
		
	}
	
	public boolean onTouch(View view, MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			//подтверждение обновления
			if(view == buttonUpdate){
				FragmentManager manager = getSupportFragmentManager();
				UpdateDialog dialog = new UpdateDialog();
				dialog.show(manager, "update_dialog");
			}
			
			//подтверждение возврата на предыдущий экран
			if(view == buttonBack){
				FragmentManager manager = getSupportFragmentManager();
				UpdateDialogBack dialog = new UpdateDialogBack();
				dialog.show(manager, "update_dialog_back");
			}
			
			//подтверждение возврата на экран со списком товаров
			if(view == buttonMain){
				FragmentManager manager = getSupportFragmentManager();
				UpdateDialogBackMain dialog = new UpdateDialogBackMain();
				dialog.show(manager, "update_dialog_back_main");
			}
		}
		
		return false;
	}
	
}
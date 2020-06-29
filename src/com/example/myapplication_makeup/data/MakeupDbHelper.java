//Создание базы данных

package com.example.myapplication_makeup.data;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myapplication_makeup.data.MakeupContract.MakeupEntry;

public class MakeupDbHelper extends SQLiteOpenHelper{
	//название и версия база данных
	public static final String DATABASE_NAME = "makeup";
	public static final int DATABASE_VERSION = 1;
	
	//шаблон запроса для создания таблицы
	public static final String CREATE_TABLE = "CREATE TABLE " + 
			MakeupEntry.TABLE_NAME + " (" + 
			MakeupEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			MakeupEntry.COLUMN_ID + " INTEGER NOT NULL, " +
			MakeupEntry.COLUMN_BRAND + " TEXT NOT NULL, " +
			MakeupEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
			MakeupEntry.COLUMN_PRICE + " TEXT NOT NULL, " +
			MakeupEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
			MakeupEntry.COLUMN_CREATED + " TEXT NOT NULL, " +
			MakeupEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL);"
			;
	
	public MakeupDbHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	//создание таблицы
	public void onCreate(SQLiteDatabase db){
		db.execSQL(CREATE_TABLE);
	}
	
	//обновление базы данных
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS " + "" + MakeupEntry.TABLE_NAME + ";");
		onCreate(db);
	}
}

//Таблица базы данных

package com.example.myapplication_makeup.data;

import android.provider.BaseColumns;

public final class MakeupContract {
	public static final class MakeupEntry implements BaseColumns{
		//название таблицы
		public static final String TABLE_NAME = "makeup_products";
		
		//названия столбцов
		public static final String _ID = BaseColumns._ID;
		public static final String COLUMN_ID = "id";
		public static final String COLUMN_BRAND = "brand";
		public static final String COLUMN_PRODUCT_NAME = "name";
		public static final String COLUMN_PRICE = "price";
		public static final String COLUMN_DESCRIPTION = "description";
		public static final String COLUMN_CREATED = "created";
		public static final String COLUMN_IMAGE_URL = "image";
	} 
}
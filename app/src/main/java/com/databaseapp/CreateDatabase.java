package com.databaseapp;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class CreateDatabase {

	public DatabaseHelper mDbHelper;
	public SQLiteDatabase mDb;	
	public static final String DATABASE_NAME= "Detail_database";    // Internal Database
//	public static final String DATABASE_NAME= Environment.getExternalStorageDirectory()+File.separator+"Detail_database.sqlite";      //External Database
	public static final String TAG = "tag";
	public static final int DATABASE_VERSION = 1;

	public static final String Id = "id";

	// Definition for Detail Table
	public static final String Key = "key";
	public static final String Name = "name";
	public static final String City = "city";
	public static final String Age = "age";
	public static final String DATABASE_TABLE_DETAIL = "DETAIL";

	/** * Database creation sql statement */

	//Detail Table creation
	private static final String DATABASE_CREATE_DETAIL = "CREATE TABLE IF NOT EXISTS "
			+ DATABASE_TABLE_DETAIL + " (Key INTEGER PRIMARY KEY AUTOINCREMENT, "+" Name text, "+" City text" +
			", "+" Age text)";


	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) { 
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		/**
		 * Creating Tables
		 */

		@Override
		public void onCreate(SQLiteDatabase db) {
			try{
				db.execSQL(DATABASE_CREATE_DETAIL);

				Log.i(TAG, "DATABASE IS CREATING.............");
			}
			catch(SQLException e){

			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//	String alter_Query = "ALTER TABLE AREA ADD COLUMN Comment text";
			//	db.execSQL(alter_Query);

		}
	}
	public CreateDatabase(Context ctx) {

		this.mCtx = ctx;
	}

	public void DeleteDatabase(String table)
	{
		mDb.delete(table, null, null);   
	}

	public void DeleteDatabaseContent(String table){
		String query = "DELETE FROM " + table;
		mDb.execSQL(query);
	}

	public void DeleteLastRecord(String key)
	{
		String selection = "Key = ?";
		String[] selectionArgs = new String[]{key};
		mDb.delete(DATABASE_TABLE_DETAIL, selection, selectionArgs);
	}

	public CreateDatabase open() throws SQLException {

		//	Log.i(TAG, "OPening DataBase Connection....");
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public SQLiteDatabase openDB() throws SQLException{
		//	Log.i(TAG, "OPening DataBase Connection....");
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return mDb;
	}
	public void close() {
		//	Log.i(TAG, "Closing DataBase Connection....");
		mDbHelper.close();

	}

	// Inserting record values for Detail
	public long createElementsDetail(String name, String city, String age) {
		//	Log.i(TAG, "Inserting Detail   ...");

		ContentValues initialValues = new ContentValues();
		initialValues.put(Name, name);
		initialValues.put(City, city);
		initialValues.put(Age, age);

		return mDb.insert(DATABASE_TABLE_DETAIL, null, initialValues);
	}

	/**
	 * Cursor for Detail
	 * @return
	 * @throws android.database.SQLException
	 */
	public Cursor fetchDetail() throws SQLException{
		Cursor mCursor = mDb.query(DATABASE_TABLE_DETAIL, new String[] {Key, Name, City, Age},null,null,null,null,null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
}
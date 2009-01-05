package net.it4myself.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

public class Settings {
    private static final String TAG = "Settings";
    private static final int DB_VERSION = 1;
    private static final String DEFAULT_DB_NAME = "settings.db";
    private static String db_name = DEFAULT_DB_NAME;
    private static final String TABLE_NAME = "settings";
    
    private HashMap<String,String> map = new HashMap<String,String>();


    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, db_name, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table " + TABLE_NAME + " ("
                    + "key TEXT primary key, "
                    + "value TEXT"
                    + ");");
            db.execSQL("insert into " + TABLE_NAME + "(key, value) values('seed', '" + MakeKey() + "')");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database." + TABLE_NAME + " from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
        
    	private static String MakeKey(){
    		MessageDigest digest;
			try {
				digest = MessageDigest.getInstance("MD5");
	    		return digest.digest(Long.toBinaryString(System.currentTimeMillis()).getBytes()).toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return "";
    	}

    }
	public static final int COLUMN_INDEX_KEY = 0;
	public static final int COLUMN_INDEX_VALUE = 1;

	private static final String[] PROJECTION = new String[] {
        "key",
        "value"
	};
	private DatabaseHelper mOpenHelper;

	
	public Settings(Context context, String special_db_name){
    	if(null != special_db_name) {
    		db_name = special_db_name;
    	}
    	mOpenHelper = new DatabaseHelper(context);
    	restore();
	}
	
	public void set(String key, String value){
		map.put(key, value);
		if(hasKeyInDB(key)){
			updateDB(key, value);
		} else {
			insertToDB(key, value);
		}
	}
	
	public String get(String key){
		return map.get(key);
	}
	
	public int delete(String key){
		map.remove(key);
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        return db.delete(TABLE_NAME, "key = ?", new String[] {key});
	}
	
	public List<String> keysInDB(){
		List<String> result = new ArrayList<String>();
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT key FROM " + TABLE_NAME, null);
		while(cursor.moveToNext()){
			result.add(cursor.getString(COLUMN_INDEX_KEY));
		}
		return result;
	}
	
	private void restore(){
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT key, value FROM " + TABLE_NAME, null);
		while(cursor.moveToNext()){
			map.put(cursor.getString(COLUMN_INDEX_KEY), cursor.getString(COLUMN_INDEX_VALUE));
		}
	}
	
	private long insertToDB(String key, String value){
		ContentValues values = new ContentValues();
		values.put("key", key);
		values.put("value", value);
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(TABLE_NAME, "DEFAULT",values);
        if (rowId > 0) {
            return rowId;
        }
        throw new SQLException("Failed to insert location");
	}
	
	private boolean updateDB(String key, String value){
		ContentValues values = new ContentValues();
		values.put("value", value);
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int num = db.update(TABLE_NAME, values, "key = ?", new String[] {key});
        if(0 < num){
        	return true;
        } else {
        	return false;
        }
	}
	
	public boolean hasKeyInDB(String key){
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] selectionArgs = new String[] {key};
		qb.setTables(TABLE_NAME);
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor cursor = qb.query(db, PROJECTION, "key = ?", selectionArgs, null, null, null, null);
        if(1 == cursor.getCount()){
        	return true;
        }else{
        	return false;
        }
	}
	
	public boolean hasKeyInMap(String key){
		return map.containsKey(key);
	}
	

}

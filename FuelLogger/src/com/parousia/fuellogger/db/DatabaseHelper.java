package com.parousia.fuellogger.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.parousia.fuellogger.constants.AppConstants;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "fuel.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement
	  private static final String DATABASE_CREATE = "create table "
	      + AppConstants.TABLE_FUELLOG + "(" + AppConstants.COLUMN_ID
	      + " integer primary key autoincrement, " + AppConstants.COLUMN_DATE 
	      +" date not null,"+ AppConstants.COLUMN_ODOMETER
	      +" bigint not null, " + AppConstants.COLUMN_FUELAMOUNT
	      +" double not null, " + AppConstants.COLUMN_FUELPRICE
	      +" double);";

	  private static DatabaseHelper mInstance = null;
	  
	  public static DatabaseHelper getInstance(Context ctx) {
	      
		    if (mInstance == null) {
		      mInstance = new DatabaseHelper(ctx.getApplicationContext());
		    }
		    return mInstance;
		  }
	
	  public DatabaseHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
		}
	  
	  public DatabaseHelper(Context context) {
		  this(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	  public void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }
	  @Override
	  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    Log.w(DatabaseHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + AppConstants.TABLE_FUELLOG);
	    onCreate(db);
	  }
}



	
package com.parousia.fuellogger.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.parousia.fuellogger.constants.AppConstants;
import com.parousia.fuellogger.model.FuelEntry;

public class FuelDataSource {

	// Database fields
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { AppConstants.COLUMN_ID,
			AppConstants.COLUMN_DATE, AppConstants.COLUMN_FUELAMOUNT,
			AppConstants.COLUMN_FUELPRICE, AppConstants.COLUMN_ODOMETER };

	public FuelDataSource(Context context) {
		dbHelper = DatabaseHelper.getInstance(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public FuelEntry createEntry(Date dateTime, long odo, double fuelamount,
			double fuelprice) {
		ContentValues values = new ContentValues();
		values.put(AppConstants.COLUMN_DATE, persistDate(dateTime));
		values.put(AppConstants.COLUMN_ODOMETER, odo);
		values.put(AppConstants.COLUMN_FUELAMOUNT, fuelamount);
		values.put(AppConstants.COLUMN_FUELPRICE, fuelprice);
		long insertId = database.insert(AppConstants.TABLE_FUELLOG, null,
				values);
		Cursor cursor = database.query(AppConstants.TABLE_FUELLOG, allColumns,
				AppConstants.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		FuelEntry newEntry = cursorToEntry(cursor);
		cursor.close();
		return newEntry;
	}

	public void deleteComment(FuelEntry entry) {
		long id = entry.getId();
		System.out.println("Entry deleted with id: " + id);
		database.delete(AppConstants.TABLE_FUELLOG, AppConstants.COLUMN_ID
				+ " = " + id, null);
	}

	public List<FuelEntry> getAllEntries() {
		List<FuelEntry> entries = new ArrayList<FuelEntry>();

		Cursor cursor = database.query(AppConstants.TABLE_FUELLOG, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FuelEntry entry = cursorToEntry(cursor);
			entries.add(entry);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return entries;
	}

	public String fetchLastFuelFillDate() {
		Cursor c = database.rawQuery("SELECT " + AppConstants.COLUMN_DATE
				+ " FROM " + AppConstants.TABLE_FUELLOG + " ORDER BY "
				+ AppConstants.COLUMN_DATE + " DESC", null);
		String lastDate = "";
		Log.d("DB Logs", "" + c.getCount());
		Log.d("DB Logs", "" + c.getColumnIndex(AppConstants.COLUMN_DATE));
		if (c.moveToFirst()) {
			lastDate = c.getString(c.getColumnIndex(AppConstants.COLUMN_DATE));
		}
		Log.d("Db Logs ", "Last Date - " + lastDate);
		return lastDate;
	}

	public Cursor getAllEntriesAsCursor() {
		String buildSQL = "Select * from " + AppConstants.TABLE_FUELLOG
				+ " ORDER BY " + AppConstants.COLUMN_DATE + " DESC";
		Log.d("DB Logs", buildSQL);
		if (database != null) {
			Cursor c = database.rawQuery(buildSQL, null);
			return c;
		}
		return null;
	}

	private FuelEntry cursorToEntry(Cursor cursor) {
		FuelEntry entry = new FuelEntry();
		entry.setId(cursor.getLong(0));
		entry.setFuelDateTime(cursor.getString(1));
		entry.setOdometer(cursor.getLong(2));
		entry.setFuelAmount(cursor.getDouble(3));
		entry.setFuelPrice(cursor.getDouble(4));
		return entry;
	}

	public static Long persistDate(Date date) {
		if (date != null) {
			return date.getTime();
		}
		return null;
	}

}

package com.parousia.fuellogger.views.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parousia.fuellogger.R;
import com.parousia.fuellogger.constants.AppConstants;
import com.parousia.fuellogger.db.FuelDataSource;

public class FuelSummaryFragment extends Fragment {
	
	public static Fragment newInstance(Context context) {
		FuelSummaryFragment fuelSummaryFragment = new FuelSummaryFragment();
		return fuelSummaryFragment;
	}

	private ListView summaryList;
	private FuelDataSource dataSource;
	private TextView summaryError;
	private Cursor cursor;
	private CustomListAdapter adapter;
	private ProgressDialog progress;
	private TextView dashboardValue;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fuelsummarysection, container, false);

		summaryList = (ListView) root.findViewById(R.id.fuel_summary_list);
		summaryError = (TextView) root.findViewById(R.id.summary_error_message);
		
		dashboardValue = (TextView) root.findViewById(R.id.dashboard_days_value);
		

		dataSource = new FuelDataSource(getActivity());
		dataSource.open();
		
		progress = new ProgressDialog(getActivity());

		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		cursor = dataSource.getAllEntriesAsCursor();

		if (cursor == null || cursor.getCount() == 0) {
			summaryList.setVisibility(View.GONE);
			summaryError.setVisibility(View.VISIBLE);
		} else {
			summaryError.setVisibility(View.GONE);
			summaryList.setVisibility(View.VISIBLE);
			adapter = new CustomListAdapter(getActivity(), cursor, true);
			summaryList.setAdapter(adapter);
		}
		
		calculateDashBoardValue();
	}

	private void calculateDashBoardValue() {
		
		
		long difference = 0;
		try {
			String dateStr = dataSource.fetchLastFuelFillDate();
			Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dateStr);
			Date now = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(new Date().toString());
			
			difference = getDateDiff(date, now, TimeUnit.DAYS);
			Log.d("Logic Logs", "Difference - "+difference);
			
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dashboardValue.setText(String.valueOf(difference));
		
		
	}
	
	
	/**
	 * Get a diff between two dates
	 * @param date1 the oldest date
	 * @param date2 the newest date
	 * @param timeUnit the unit in which you want the diff
	 * @return the diff value, in the provided unit
	 */
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
	    long diffInMillies = date2.getTime() - date1.getTime();
	    return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}

	private class CustomListAdapter extends CursorAdapter {

		public CustomListAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery); 
			// TODO Auto-generated constructor stub
		}

		public CustomListAdapter(Context context, Cursor c, int flags) {
			super(context, c, flags);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			
			Log.d("View Logs", "BindView called!");
			
			Log.d("DB Logs", "Cursor Count = " + cursor.getCount());
			
			TextView monthText = (TextView) view.findViewById(R.id.calendar_month);
			TextView dayText = (TextView) view.findViewById(R.id.calendar_day);
			TextView fuelAmountText = (TextView) view.findViewById(R.id.fuel_amount);
			TextView fuelPriceText = (TextView) view.findViewById(R.id.fuel_price);
			TextView fuelSpentText = (TextView) view.findViewById(R.id.fuel_spent);
			
			String dateTime = cursor.getString(cursor.getColumnIndex(AppConstants.COLUMN_DATE));
			String amount = cursor.getString(cursor.getColumnIndex(AppConstants.COLUMN_FUELAMOUNT));
			String price = cursor.getString(cursor.getColumnIndex(AppConstants.COLUMN_FUELPRICE));
			
			StringTokenizer st = new StringTokenizer(dateTime, "-");
			String day="", month="", year="";
			while (st.hasMoreTokens())
			{
				day = st.nextToken();
				month = st.nextToken();
				year = st.nextToken();
			}
			
			monthText.setText(month);
			dayText.setText(day);
			fuelAmountText.setText(amount+" Litres");
			fuelPriceText.setText(price +" cents");
			Double spent = (Double.parseDouble(amount) * Double.parseDouble(price))/100.0;
			fuelSpentText.setText("$"+String.valueOf(spent));
			
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			View rowView = inflater.inflate(R.layout.fuel_entry_list_row, parent, false);
			return rowView;
		}

	}

}
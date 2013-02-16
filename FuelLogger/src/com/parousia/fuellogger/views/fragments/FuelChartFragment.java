package com.parousia.fuellogger.views.fragments;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parousia.fuellogger.R;
import com.parousia.fuellogger.db.FuelDataSource;
import com.parousia.fuellogger.model.FuelEntry;

public class FuelChartFragment extends Fragment {

	public static Fragment newInstance(Context context) {
		FuelChartFragment fuelChartFragment = new FuelChartFragment();
		return fuelChartFragment;
	}

	private FuelDataSource dataSource;
	private Cursor cursor;
	private List<FuelEntry> dataItems;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.fuelchartsection, container, false);
		
		dataSource = new FuelDataSource(getActivity());
		dataSource.open();

		computeChartInfo();
		
		return root;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		dataItems = dataSource.getAllEntries();

	}

	private void computeChartInfo() {
		
		
	}

}

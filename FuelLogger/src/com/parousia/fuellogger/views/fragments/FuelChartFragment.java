package com.parousia.fuellogger.views.fragments;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.parousia.fuellogger.R;
import com.parousia.fuellogger.db.FuelDataSource;
import com.parousia.fuellogger.model.FuelEntry;
import com.parousia.fuellogger.views.fragments.components.GraphView;
import com.parousia.fuellogger.views.fragments.components.InteractiveLineGraphView;

public class FuelChartFragment extends Fragment {

	public static Fragment newInstance(Context context) {
		FuelChartFragment fuelChartFragment = new FuelChartFragment();
		return fuelChartFragment;
	}

	private FuelDataSource dataSource;
	private Cursor cursor;
	private List<FuelEntry> dataItems;
	private ViewGroup root;
	private Spinner chartSelector;
	private ArrayAdapter<CharSequence> adapter;
	private InteractiveLineGraphView chartView;
	private static final int ODO_VS_PRICE = 0;
	private static final int ODO_VS_PERFORMANCE = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		root = (ViewGroup) inflater.inflate(R.layout.fuelchartsection,
				container, false);
		
		chartView = (InteractiveLineGraphView) root.findViewById(R.id.graphview);
		
		chartSelector = (Spinner)root.findViewById(R.id.chartselector);
		
		adapter = ArrayAdapter.createFromResource(this.getActivity(),
		        R.array.chart_selector_options, android.R.layout.simple_spinner_dropdown_item);
		
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		chartSelector.setAdapter(adapter);
		
		
		chartSelector.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				drawChart(pos);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				removeChart();
			}
			
		});

		dataSource = new FuelDataSource(getActivity());
		dataSource.open();
		return root;
	}

	protected void removeChart() {
		chartView.setVisibility(View.GONE);
	}

	protected void drawChart(int pos) {
		
		chartView.setVisibility(View.VISIBLE);
		
		switch(pos){
		case ODO_VS_PRICE:
			
			chartView.setCurrentViewport(new RectF(1f, 2f, 3f, 4f));
			break;
		case ODO_VS_PERFORMANCE:
			chartView.setCurrentViewport(new RectF(11f, 12f, 13f, 14f));
			break;
		default:
			break;
		}
		
	}
}

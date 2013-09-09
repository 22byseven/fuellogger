package com.parousia.fuellogger.views.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.parousia.fuellogger.R;
import com.parousia.fuellogger.R.string;
import com.parousia.fuellogger.db.FuelDataSource;
import com.parousia.fuellogger.model.FuelEntry;
import com.parousia.fuellogger.utils.DateTimeUtil;
import com.parousia.fuellogger.views.FuelLogActivity;
import com.parousia.fuellogger.views.FuelSummaryUpdater;
import com.parousia.fuellogger.views.dialogs.DateTimePickerDialog;
import com.parousia.fuellogger.views.dialogs.DateTimePickerDialog.DateTimeDialogListener;

public class FuelLogFragment extends Fragment implements
		DateTimeDialogListener, FuelSummaryUpdater {

	public static Fragment newInstance(Context context) {
		FuelLogFragment fuelLogFragment = new FuelLogFragment();
		return fuelLogFragment;
	}

	private TextView dateTimeLabel;
	private ImageButton dateTimeEditButton;
	private EditText odometerInput;
	private EditText fuelAmountIput;
	private EditText fuelPriceInput;
	private Button saveButton;
	private String currentDateTimeString;
	private DateTimePickerDialog dateTimeDialog;
	private FuelDataSource datasource;
	private FuelEntry entry;
	private ProgressDialog progress;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fuellogsection,
				container, false);
		dateTimeLabel = (TextView) root.findViewById(R.id.datetimelabel);
		dateTimeEditButton = (ImageButton) root.findViewById(R.id.datetimeedit);
		odometerInput = (EditText) root.findViewById(R.id.odoinput);
		fuelAmountIput = (EditText) root.findViewById(R.id.fuelamoutinput);
		fuelPriceInput = (EditText) root.findViewById(R.id.fuelpriceinput);
		saveButton = (Button) root.findViewById(R.id.savebutton);

		progress = new ProgressDialog(getActivity());

		datasource = new FuelDataSource(this.getActivity());
		datasource.open();

		SpannableString spanStr = new SpannableString(
				getString(R.string.odo_input_hint));
		spanStr.setSpan(new TextAppearanceSpan(getActivity()
				.getApplicationContext(), R.style.style_edittext_hint), 0,
				(getString(R.string.odo_input_hint)).length(),
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		odometerInput.setHint(spanStr);

		spanStr = new SpannableString(getString(R.string.fuel_amount_hint));
		spanStr.setSpan(new TextAppearanceSpan(getActivity()
				.getApplicationContext(), R.style.style_edittext_hint), 0,
				(getString(R.string.fuel_amount_hint)).length(),
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		fuelAmountIput.setHint(spanStr);

		spanStr = new SpannableString(getString(R.string.price_input_hint));
		spanStr.setSpan(new TextAppearanceSpan(getActivity()
				.getApplicationContext(), R.style.style_edittext_hint), 0,
				(getString(R.string.price_input_hint)).length(),
				Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		fuelPriceInput.setHint(spanStr);

		return root;
	}

	@Override
	public void onResume() {
		super.onResume();
		odometerInput.setText("");
		fuelAmountIput.setText("");
		fuelPriceInput.setText("");

	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		currentDateTimeString = DateTimeUtil.TODAY;

		dateTimeLabel.setClickable(false);
		dateTimeLabel.setText(currentDateTimeString);

		dateTimeLabel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDateTimeDialog();
			}
		});

		dateTimeEditButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				showDateTimeDialog();

			}
		});

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				saveFuelLogEntry();
			}
		});

		super.onViewCreated(view, savedInstanceState);
	}

	protected void showDateTimeDialog() {

		dateTimeDialog = DateTimePickerDialog
				.newInstance(R.string.date_time_dialog_title);
		dateTimeDialog.setDateTimePickerDialogListener(this);
		dateTimeDialog.show(getFragmentManager(), null);

	}

	protected void saveFuelLogEntry() {

		Log.d("DB Logs",
				"Date = "
						+ dateTimeLabel.getText().toString()
						+ ", Odo = "
						+ Long.parseLong(odometerInput.getText().toString())
						+ ", Amount = "
						+ Double.parseDouble(fuelAmountIput.getText()
								.toString())
						+ ", Price = "
						+ Double.parseDouble(fuelPriceInput.getText()
								.toString()));

		new AsyncTask<String, Integer, String>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				progress.setMessage("Saving data... ");
				progress.setCancelable(false);
				progress.show();
			}

			@Override
			protected String doInBackground(String... params) {
				Date date = null;
				try {
					date = new SimpleDateFormat("yyyy-MMM-dd", Locale.ENGLISH)
							.parse(dateTimeLabel.getText().toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				entry = datasource
						.createEntry(date, Long.parseLong(odometerInput
								.getText().toString()), Double
								.parseDouble(fuelAmountIput.getText()
										.toString()), Double
								.parseDouble(fuelPriceInput.getText()
										.toString()));
				return "";
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (progress.isShowing()) {
					progress.dismiss();
				}
				((FuelLogActivity) getActivity()).updateSummaryTable();
			}

		}.execute("");

	}

	@Override
	public void onPositiveClick(Date selectedDateTime) {

		dateTimeLabel.setText(DateTimeUtil
				.convertDateToString(selectedDateTime));
		dateTimeDialog.dismiss();

	}

	@Override
	public void onNegativeClick() {
		dateTimeLabel.setText(DateTimeUtil.TODAY);
		dateTimeDialog.dismiss();

	}

	@Override
	public void updateSummaryTable() {

	}

}
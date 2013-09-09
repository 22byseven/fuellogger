package com.parousia.fuellogger.views.dialogs;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.parousia.fuellogger.R;

public class DateTimePickerDialog extends DialogFragment implements
		DialogInterface.OnClickListener {

	private DateTimeDialogListener listener;
	private DatePicker datePicker;
	private TimePicker timePicker;

	public static DateTimePickerDialog newInstance(int title) {
		DateTimePickerDialog dialog = new DateTimePickerDialog();
		Bundle args = new Bundle();
		args.putInt("title", title);
		dialog.setArguments(args);
		return dialog;
	}

	public interface DateTimeDialogListener {
		public void onPositiveClick(Date selectedDateTime);
		public void onNegativeClick();

	}
	
	public void setDateTimePickerDialogListener(DateTimeDialogListener listener)
	{
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		int title = getArguments().getInt("title");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		builder.setView(inflater.inflate(R.layout.datetime_picker, null));
		builder.setIcon(android.R.drawable.ic_lock_idle_alarm);
		builder.setTitle(title);
		builder.setPositiveButton(android.R.string.ok, this);
		builder.setNegativeButton(R.string.reset, this);
		return builder.create();

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		
		

		if (listener != null) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				listener.onPositiveClick(retreiveAndFormatDate());
				break;
			default:
				listener.onNegativeClick();
			}
		}
	}
	
	public Date retreiveAndFormatDate()
	{
		datePicker = (DatePicker) DateTimePickerDialog.this.getDialog().findViewById(R.id.datePicker);
//		DateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
//		String selectedDate = df.format(datePicker.getCalendarView().getDate());
		Date d = new Date(datePicker.getCalendarView().getDate());
		return d;
	}

}

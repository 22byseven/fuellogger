package com.parousia.fuellogger.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.parousia.fuellogger.R;

public class InitialOdometerDialog extends DialogFragment implements
		OnClickListener {

	public static InitialOdometerDialog newInstance(int title) {
		InitialOdometerDialog dialog = new InitialOdometerDialog();
		Bundle args = new Bundle();
		args.putInt("title", title);
		dialog.setArguments(args);
		return dialog;
	}

	public interface InitialOdoDialogListener {
		public void onClickSave(long initialOdoReading);
	}

	private InitialOdoDialogListener listener;

	public void setInitialOdoDialogListener(InitialOdoDialogListener listener) {
		this.listener = listener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		int title = getArguments().getInt("title");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		builder.setView(inflater.inflate(R.layout.initial_odo_input, null));
		builder.setIcon(android.R.drawable.btn_star);
		builder.setTitle(title);
		builder.setPositiveButton(R.string.save, this);
		return builder.create();

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		if (listener != null) {
			switch (which) {
			case DialogInterface.BUTTON_POSITIVE:
				listener.onClickSave(getInitialOdoReading());
				break;
			}
		}
	}

	private long getInitialOdoReading() {

		EditText initialOdoInput = (EditText) InitialOdometerDialog.this.getDialog()
				.findViewById(R.id.initial_odo_input_text);
		String initialOdoString = initialOdoInput.getText().toString();
		long initialOdoValue = 0;
		if (initialOdoString.length() > 0) {
			initialOdoValue = Long.parseLong(initialOdoString);
		}

		return initialOdoValue;
	}

}

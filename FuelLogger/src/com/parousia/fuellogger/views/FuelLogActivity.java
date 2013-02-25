package com.parousia.fuellogger.views;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.parousia.fuellogger.R;
import com.parousia.fuellogger.constants.AppConstants;
import com.parousia.fuellogger.views.dialogs.InitialOdometerDialog;
import com.parousia.fuellogger.views.dialogs.InitialOdometerDialog.InitialOdoDialogListener;
import com.parousia.fuellogger.views.fragments.FuelChartFragment;
import com.parousia.fuellogger.views.fragments.FuelLogFragment;
import com.parousia.fuellogger.views.fragments.FuelSummaryFragment;

public class FuelLogActivity extends FragmentActivity implements
		InitialOdoDialogListener, FuelSummaryUpdater {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private SharedPreferences preferences;

	private Editor preferenceEditor;

	private InitialOdometerDialog initialOdoDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fuel_log);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(this,
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(AppConstants.SCREEN_COUNT);

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				if (position == 1) {
					mViewPager.getAdapter().notifyDataSetChanged();
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mViewPager.setCurrentItem(1);

		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

	}

	@Override
	protected void onResume() {
		super.onResume();
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		boolean previouslyInitialised = preferences.getBoolean(
				getString(R.string.preference_first_launch), false);
		if (!previouslyInitialised) {
			initialOdoDialog = InitialOdometerDialog
					.newInstance(R.string.initial_odo_dialog_title);
			initialOdoDialog.setInitialOdoDialogListener(this);
			initialOdoDialog.show(getSupportFragmentManager(), null);
		}
	}

	@Override
	public void onClickSave(long initialOdoReading) {

		preferenceEditor = preferences.edit();
		preferenceEditor.putLong(getString(R.string.initial_odo_value),
				initialOdoReading);
		preferenceEditor.putBoolean(
				getString(R.string.preference_first_launch), Boolean.TRUE);
		preferenceEditor.commit();
		if (initialOdoDialog.isVisible()) {
			initialOdoDialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_fuel_log, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		if (mViewPager.getCurrentItem() == 0) {
			// If the user is currently looking at the first step, allow the
			// system to handle the
			// Back button. This calls finish() on this activity and pops the
			// back stack.
			super.onBackPressed();
		} else {
			// Otherwise, select the previous step.
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
		}
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

		private Context _context;

		public SectionsPagerAdapter(Context context, FragmentManager fm) {
			super(fm);
			_context = context;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new Fragment();
			switch (position) {
			case 0:
				fragment = FuelLogFragment.newInstance(_context);
				break;
			case 1:
				fragment = FuelSummaryFragment.newInstance(_context);
				break;
			case 2:
				fragment = FuelChartFragment.newInstance(_context);
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return AppConstants.SCREEN_COUNT;
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale locale = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(locale);
			case 1:
				return getString(R.string.title_section2).toUpperCase(locale);
			case 2:
				return getString(R.string.title_section3).toUpperCase(locale);
			}
			return null;
		}

	}

	@Override
	public void updateSummaryTable() {
		final InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
		mViewPager.setCurrentItem(1);
	}

}

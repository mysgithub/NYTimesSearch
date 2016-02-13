package com.codepath.nytimessearch.activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.SearchFilter;
import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Shyam Rokde on 2/12/16.
 */
public class SettingsDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{
  @Bind(R.id.etBeginDate) EditText etBeginDate;
  @Bind(R.id.sortOrderSpinner) Spinner sortOrderSpinner;
  @Bind(R.id.cbArts) CheckBox cbArts;
  @Bind(R.id.cbFashionStyle) CheckBox cbFashionStyle;
  @Bind(R.id.cbSports) CheckBox cbSports;

  private SearchFilter searchFilter;
  private OnSettingChangedListener listener;

  ArrayAdapter<CharSequence> spinnerAdapter;
  int year;
  int month;
  int day;
  public static final String DATEPICKER_TAG = "datepicker";

  public SettingsDialog(){

  }

  public static SettingsDialog newInstance(){
    SettingsDialog settingsDialog = new SettingsDialog();
    return settingsDialog;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_settings, container);
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    ButterKnife.bind(this, view);

    return view;
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Set Values
    spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sort_order_array, R.layout.support_simple_spinner_dropdown_item);
    sortOrderSpinner.setAdapter(spinnerAdapter);
    if(searchFilter.getBeginDate() != null && !searchFilter.getBeginDate().isEmpty()){
      etBeginDate.setText(searchFilter.getBeginDate());
    }
    if(searchFilter.getSortOrder() != null){
      int spinnerPosition = spinnerAdapter.getPosition(searchFilter.getSortOrder());
      sortOrderSpinner.setSelection(spinnerPosition);
    }
    cbArts.setChecked(searchFilter.getArts());
    cbFashionStyle.setChecked(searchFilter.getFashionStyle());
    cbSports.setChecked(searchFilter.getSports());
    showDatePicker();
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    if(activity instanceof OnSettingChangedListener){
      listener = (OnSettingChangedListener) activity;
    }else {
      throw new ClassCastException(activity.toString()
          + " must implement SettingsDialog.OnSettingChangedListener");
    }
  }

  @Override
  public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
    final Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month);
    calendar.set(Calendar.DAY_OF_MONTH, day);

    // Set Date to Text View
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String formatted = dateFormat.format(calendar.getTime());
    etBeginDate.setText(formatted);

    Toast.makeText(getContext(), "new date: " + formatted, Toast.LENGTH_LONG).show();
  }

  public interface OnSettingChangedListener {
    public void onSettingChanged(SearchFilter searchFilter);
  }

  @OnClick(R.id.btnSave)
  public void onSave(View view){
    searchFilter.setBeginDate(etBeginDate.getText().toString());
    searchFilter.setSortOrder(sortOrderSpinner.getSelectedItem().toString());
    searchFilter.setArts(cbArts.isChecked());
    searchFilter.setFashionStyle(cbFashionStyle.isChecked());
    searchFilter.setSports(cbSports.isChecked());

    listener.onSettingChanged(searchFilter);
    dismiss();
  }

  public void setSearchFilter(SearchFilter filter){
    searchFilter = filter;
  }

  public void showDatePicker(){
    // set Date Picker to current date or already selected date
    final Calendar calendar = Calendar.getInstance();
    if(searchFilter.getBeginDate() != null && !searchFilter.getBeginDate().isEmpty()){
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
      try {
        Date date = dateFormat.parse(searchFilter.getBeginDate());
        calendar.setTime(date);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    year = calendar.get(Calendar.YEAR);
    month = calendar.get(Calendar.MONTH);
    day = calendar.get(Calendar.DAY_OF_MONTH);
    final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, year, month, day, false);
    etBeginDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        datePickerDialog.setYearRange(1985, 2028);
        datePickerDialog.setCloseOnSingleTapDay(true);
        datePickerDialog.show(getFragmentManager(), DATEPICKER_TAG);
      }
    });
  }
}

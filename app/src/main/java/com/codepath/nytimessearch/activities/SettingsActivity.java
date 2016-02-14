package com.codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

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

public class SettingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

  @Bind(R.id.etBeginDate) EditText etBeginDate;
  @Bind(R.id.sortOrderSpinner) Spinner sortOrderSpinner;
  @Bind(R.id.cbArts) CheckBox cbArts;
  @Bind(R.id.cbFashionStyle) CheckBox cbFashionStyle;
  @Bind(R.id.cbSports) CheckBox cbSports;
  @Bind(R.id.toolbar) Toolbar toolbar;

  SearchFilter searchFilter;

  public static final String DATEPICKER_TAG = "datepicker";

  ArrayAdapter<CharSequence> spinnerAdapter;

  int year;
  int month;
  int day;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    ButterKnife.bind(this);

    setupViews();
  }

  public void setupViews(){
    setSupportActionBar(toolbar);
    // Extract SearchFilter object from intent extras
    searchFilter = (SearchFilter) getIntent().getParcelableExtra("searchFilter");

    spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sort_order_array, R.layout.support_simple_spinner_dropdown_item);
    sortOrderSpinner.setAdapter(spinnerAdapter);

    showDatePicker();
    setData();
  }

  public void setData(){

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
  }

  @OnClick(R.id.btnSave)
  public void onSave(View view){
    // Set values
    searchFilter.setBeginDate(etBeginDate.getText().toString());
    searchFilter.setSortOrder(sortOrderSpinner.getSelectedItem().toString());
    searchFilter.setArts(cbArts.isChecked());
    searchFilter.setFashionStyle(cbFashionStyle.isChecked());
    searchFilter.setSports(cbSports.isChecked());
    // Prepare data intent
    Intent i = new Intent();
    i.putExtra("searchFilter", searchFilter);
    // Activity finished ok, return the data
    setResult(RESULT_OK, i); // set result code and bundle data for response
    finish(); // closes the activity, pass data to parent
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
        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
      }
    });
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

    //Toast.makeText(this, "new date:" + formatted, Toast.LENGTH_LONG).show();
  }
}

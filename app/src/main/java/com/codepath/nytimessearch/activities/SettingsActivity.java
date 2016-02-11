package com.codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class SettingsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

  EditText etBeginDate;
  Spinner sortOrderSpinner;
  CheckBox cbArts;
  CheckBox cbFashionStyle;
  CheckBox cbSports;
  Button btnSave;

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
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    // Extract SearchFilter object from intent extras
    searchFilter = (SearchFilter) getIntent().getParcelableExtra("searchFilter");

    setupViews();
    setData();
  }

  public void setupViews(){
    etBeginDate = (EditText) findViewById(R.id.etBeginDate);
    sortOrderSpinner = (Spinner) findViewById(R.id.sortOrderSpinner);
    cbArts = (CheckBox) findViewById(R.id.cbArts);
    cbFashionStyle = (CheckBox) findViewById(R.id.cbFashionStyle);
    cbSports = (CheckBox) findViewById(R.id.cbSports);
    btnSave = (Button) findViewById(R.id.btnSave);

    spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sort_order_array, R.layout.support_simple_spinner_dropdown_item);
    sortOrderSpinner.setAdapter(spinnerAdapter);

    showDatePicker();
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
      Date date = new Date();
      try {
        date = dateFormat.parse(searchFilter.getBeginDate());
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

    Toast.makeText(this, "new date:" + formatted, Toast.LENGTH_LONG).show();
  }
}
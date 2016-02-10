package com.codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.nytimessearch.R;

public class SettingsActivity extends AppCompatActivity {

  EditText etBeginDate;
  Button btnSave;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    setupViews();
  }

  public void setupViews(){
    etBeginDate = (EditText) findViewById(R.id.etBeginDate);
    btnSave = (Button) findViewById(R.id.btnSave);
  }

  public void onSave(View view){
    // Prepare data intent
    Intent i = new Intent();
    i.putExtra("beginDate", etBeginDate.getText().toString());
    // Activity finished ok, return the data
    setResult(RESULT_OK, i); // set result code and bundle data for response
    finish(); // closes the activity, pass data to parent
  }

}

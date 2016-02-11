package com.codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapters.ArticleArrayAdapter;
import com.codepath.nytimessearch.models.Article;
import com.codepath.nytimessearch.models.SearchFilter;
import com.codepath.nytimessearch.network.NewYorkTimesClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

  EditText etQuery;
  GridView gvResults;
  Button btnSearch;

  ArrayList<Article> articles;
  ArticleArrayAdapter adapter;

  SearchFilter searchFilter;



  private final int REQUEST_CODE = 200;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if(searchFilter == null){
      searchFilter = new SearchFilter();
    }

    setupViews();
  }

  public void setupViews(){
    etQuery = (EditText) findViewById(R.id.etQuery);
    gvResults = (GridView) findViewById(R.id.gvResults);
    btnSearch = (Button) findViewById(R.id.btnSearch);

    articles = new ArrayList<>();
    adapter = new ArticleArrayAdapter(this, articles);
    gvResults.setAdapter(adapter);

    // listener for grid click
    gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // create
        Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
        // get
        Article article = articles.get(position);
        // pass
        i.putExtra("article", article);

        // launch
        startActivity(i);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_search, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    switch (id){
      case R.id.action_settings:
        showSettings();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }

  }

  public void onArticleSearch(View view) {
    String query = etQuery.getText().toString();

    // Call NYTimes
    NewYorkTimesClient newYorkTimesClient = new NewYorkTimesClient();

    newYorkTimesClient.getArticles(query, searchFilter, new JsonHttpResponseHandler() {
      @Override
      public void onStart() {
        Log.d("DEBUG", "Request: " + super.getRequestURI().toString());
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // CLEAR OUT old items before appending in the new ones
        adapter.clear();
        Log.d("DEBUG", "Response: " + response.toString());
        JSONArray articleJsonResults = null;
        try {
          articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
          adapter.addAll(Article.fromJSONArray(articleJsonResults));
          //Log.d("DEBUG", articles.toString());
        }catch (JSONException ex){
          ex.printStackTrace();
        }
      }
      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        // TODO: Add Correct Error Handling if not able to communicate to NYTimes
        Log.d("DEBUG", responseString);
      }
    });

  }



  public void showSettings(){
    // create
    Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
    // pass data
    i.putExtra("searchFilter", searchFilter);
    // launch
    startActivityForResult(i, REQUEST_CODE);
  }

  /**
   * Get Data Back from SettingsActivity
   * Time to handle the result of the settings-activity
   */
  public void onActivityResult(int requestCode, int resultCode, Intent i){

    if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
      // Extract name value from result extras
      searchFilter = (SearchFilter) i.getParcelableExtra("searchFilter");
    }

  }
}

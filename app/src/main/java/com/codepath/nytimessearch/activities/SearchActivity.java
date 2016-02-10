package com.codepath.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.codepath.nytimessearch.network.NewYorkTimesClient;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

  EditText etQuery;
  GridView gvResults;
  Button btnSearch;

  ArrayList<Article> articles;
  ArticleArrayAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

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

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public void onArticleSearch(View view) {
    String query = etQuery.getText().toString();

    //Toast.makeText(this, "Serach query: " + query, Toast.LENGTH_LONG).show();
    // Call NYTimes
    NewYorkTimesClient newYorkTimesClient = new NewYorkTimesClient();
    newYorkTimesClient.fetchArticles(query, adapter);
  }
}

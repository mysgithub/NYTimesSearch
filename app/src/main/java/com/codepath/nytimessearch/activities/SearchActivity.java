package com.codepath.nytimessearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.adapters.ArticleAdapter;
import com.codepath.nytimessearch.listeners.EndlessRecyclerViewScrollListener;
import com.codepath.nytimessearch.models.Article;
import com.codepath.nytimessearch.models.SearchFilter;
import com.codepath.nytimessearch.network.NewYorkTimesClient;
import com.codepath.nytimessearch.utils.ItemClickSupport;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity implements SettingsDialog.OnSettingChangedListener {

  @Bind(R.id.rvArticles) RecyclerView rvItems;

  ArrayList<Article> articles;
  ArticleAdapter adapter;

  SearchFilter searchFilter;
  String mQuery;

  private final int REQUEST_CODE = 200;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    ButterKnife.bind(this);

    setupViews();
  }

  public void setupViews(){
    if(searchFilter == null){
      searchFilter = new SearchFilter();
    }
    articles = new ArrayList<>();
    //adapter = new ArticleArrayAdapter(this, articles);
    adapter = new ArticleAdapter(articles, getApplicationContext());
    rvItems.setAdapter(adapter);

    // Click Listener
    ItemClickSupport.addTo(rvItems).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
      @Override
      public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
        Article article = articles.get(position);
        i.putExtra("article", article);
        startActivity(i);
      }
    });

    // Configure the RecyclerView
    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
    rvItems.setLayoutManager(layoutManager);

    rvItems.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        // Call NYTimes
        NewYorkTimesClient newYorkTimesClient = new NewYorkTimesClient();
        newYorkTimesClient.getArticles(mQuery, searchFilter, page, getResponseHandler());
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_search, menu);

    // Handle Search
    MenuItem searchItem = menu.findItem(R.id.action_search);
    final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        mQuery = query;
        adapter.clear(); // CLEAR OUT old items before appending in the new ones
        // Call NYTimes
        NewYorkTimesClient newYorkTimesClient = new NewYorkTimesClient();
        newYorkTimesClient.getArticles(mQuery, searchFilter, 0, getResponseHandler());
        searchView.clearFocus();
        return true;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        return false;
      }
    });

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    switch (id){
      case R.id.action_settings:
        //showSettings();
        showSettingsDialog();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }

  }

  /**
   * JsonHttpResponseHandler
   * @return JsonHttpResponseHandler
   */
  public JsonHttpResponseHandler getResponseHandler(){
    return new JsonHttpResponseHandler() {
      @Override
      public void onStart() {
        Log.d("DEBUG", "Request: " + super.getRequestURI().toString());
      }

      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // CLEAR OUT old items before appending in the new ones
        Log.d("DEBUG", "Response: " + response.toString());
        JSONArray articleJsonResults = null;
        try {
          articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
          articles.addAll(Article.fromJSONArray(articleJsonResults));
          int curSize = adapter.getItemCount();
          adapter.notifyItemRangeInserted(curSize, articles.size() - 1);
          //Log.d("DEBUG", articles.toString());
        } catch (JSONException ex) {
          ex.printStackTrace();
        }
      }

      @Override
      public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        // TODO: Add Correct Error Handling if not able to communicate to NYTimes
        Log.d("DEBUG", responseString);
      }
    };
  }

  /**
   * Setting Activity
   */
  public void showSettings(){
    // create & pass data
    Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
    i.putExtra("searchFilter", searchFilter);
    // launch
    startActivityForResult(i, REQUEST_CODE);
  }

  /**
   * Setting DialogFragment
   */
  public void showSettingsDialog(){
    if(searchFilter == null){
      searchFilter = new SearchFilter();
    }
    FragmentManager fragmentManager = getSupportFragmentManager();
    SettingsDialog settingsDialog = SettingsDialog.newInstance();
    settingsDialog.setSearchFilter(searchFilter);
    settingsDialog.show(fragmentManager, "settings");
  }


  /**
   * Check network availability
   * @return true|false
   */
  private Boolean isNetworkAvailable(){
    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    return networkInfo != null && networkInfo.isConnectedOrConnecting();
  }

  /**
   * Get Data Back from SettingsActivity
   * Time to handle the result of the settings-activity
   *
   * @param requestCode
   * @param resultCode
   * @param i
   */
  public void onActivityResult(int requestCode, int resultCode, Intent i){
    if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
      // Extract name value from result extras
      searchFilter = (SearchFilter) i.getParcelableExtra("searchFilter");
    }
  }

  /**
   * This method id used for DialogFragment
   * @param searchFilter
   */
  @Override
  public void onSettingChanged(SearchFilter searchFilter) {
    this.searchFilter = searchFilter;
    Toast.makeText(this, "new date: " + searchFilter.getBeginDate(), Toast.LENGTH_LONG).show();
  }
}

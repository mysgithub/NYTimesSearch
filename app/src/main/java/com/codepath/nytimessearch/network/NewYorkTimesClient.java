package com.codepath.nytimessearch.network;

import android.util.Log;

import com.codepath.nytimessearch.adapters.ArticleArrayAdapter;
import com.codepath.nytimessearch.models.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Shyam Rokde on 2/9/16.
 */
public class NewYorkTimesClient {
  private static final String API_KEY = "b63b81d7aaa01e9808bd21e049c85f67:15:74340628";
  private static AsyncHttpClient client = new AsyncHttpClient();
  private static final String ARTICLE_SEARCH_URL = "http://api.nytimes.com/svc/search/v2/articlesearch.json";


  public void fetchArticles(String query, final ArticleArrayAdapter adapter){
    RequestParams params = new RequestParams();
    params.put("api-key", API_KEY);
    params.put("page", 0);
    params.put("q", query);

    client.get(ARTICLE_SEARCH_URL, params, new JsonHttpResponseHandler(){
      @Override
      public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        // CLEAR OUT old items before appending in the new ones
        adapter.clear();
        Log.d("DEBUG", response.toString());
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
}

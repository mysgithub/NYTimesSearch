package com.codepath.nytimessearch.network;

import com.codepath.nytimessearch.models.SearchFilter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Shyam Rokde on 2/9/16.
 */
public class NewYorkTimesClient {
  private static final String API_KEY = "b63b81d7aaa01e9808bd21e049c85f67:15:74340628";
  private static final String API_BASE_URL = "http://api.nytimes.com/";

  private AsyncHttpClient client;


  public NewYorkTimesClient(){
    client = new AsyncHttpClient();
  }

  private String getApiUrl(String relativeUrl) {
    return API_BASE_URL + relativeUrl;
  }

  /**
   * Get Articles
   * @param query
   * @param handler
   */
  public void getArticles(String query, SearchFilter searchFilter, int page, JsonHttpResponseHandler handler){
    RequestParams params = new RequestParams();
    params.put("api-key", API_KEY);
    params.put("page", page);
    params.put("q", query);
    if(searchFilter.getBeginDate() != null && !searchFilter.getBeginDate().isEmpty()){
      try {
        Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(searchFilter.getBeginDate());
        params.put("begin_date", new SimpleDateFormat("yyyyMMdd", Locale.US).format(date));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    if(searchFilter.getSortOrder() != null){
      params.put("sort", searchFilter.getSortOrder().toLowerCase());
    }

    //&fq=news_desk:("Sports" "Foreign")
    StringBuilder newsDesk = new StringBuilder();
    if(searchFilter.getArts()){
      newsDesk.append("\"Arts\" ");
    }
    if(searchFilter.getFashionStyle()){
      newsDesk.append("\"Fashion & Style\" ");
    }
    if(searchFilter.getSports()){
      newsDesk.append("\"Sports\"");
    }
    if(newsDesk.length() > 0){
      newsDesk.insert(0, "news_desk:(");
      newsDesk.append(")");
      params.put("fq", newsDesk.toString());
    }

    client.get(getApiUrl("svc/search/v2/articlesearch.json"), params, handler);

  }

}

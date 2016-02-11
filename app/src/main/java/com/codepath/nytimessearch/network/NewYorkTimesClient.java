package com.codepath.nytimessearch.network;

import com.codepath.nytimessearch.models.Setting;
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
  public void getArticles(String query, Setting setting, JsonHttpResponseHandler handler){
    RequestParams params = new RequestParams();
    params.put("api-key", API_KEY);
    params.put("page", 0);
    params.put("q", query);
    if(setting.getBeginDate() != null && !setting.getBeginDate().isEmpty()){
      try {
        Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(setting.getBeginDate());
        params.put("begin_date", new SimpleDateFormat("yyyyMMdd", Locale.US).format(date));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }
    if(setting.getSortOrder() != null){
      params.put("sort", setting.getSortOrder().toLowerCase());
    }

    //&fq=news_desk:("Sports" "Foreign")
    StringBuilder newsDesk = new StringBuilder();
    if(setting.getArts()){
      newsDesk.append("\"Arts\" ");
    }
    if(setting.getFashionStyle()){
      newsDesk.append("\"Fashion & Style\" ");
    }
    if(setting.getSports()){
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

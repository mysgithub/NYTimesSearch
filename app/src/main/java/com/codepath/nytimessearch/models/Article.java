package com.codepath.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Shyam Rokde on 2/9/16.
 */
public class Article implements Serializable{
  private String webUrl;
  private String headline;
  private String thumbnail;

  public String getWebUrl() {
    return webUrl;
  }

  public String getHeadline() {
    return headline;
  }

  public String getThumbnail() {
    return thumbnail;
  }

  public Article(){

  }


  public static Article fromJson(JSONObject jsonObject){
    Article a = new Article();
    // Deserialize json into object fields
    try{
      a.webUrl = jsonObject.getString("web_url");
      a.headline = jsonObject.getJSONObject("headline").getString("main");
      JSONArray multimedia = jsonObject.getJSONArray("multimedia");
      if(multimedia.length() > 0){
        int imgIndex = new Random().nextInt(multimedia.length());
        JSONObject multimediaJson = multimedia.getJSONObject(imgIndex);
        a.thumbnail = "http://www.nytimes.com/" + multimediaJson.getString("url");
      }else{
        a.thumbnail = "";
      }
    }catch (JSONException ex){
      ex.printStackTrace();
      return null;
    }
    // Return new object
    return a;
  }

  public static ArrayList<Article> fromJson(JSONArray jsonArray){
    ArrayList<Article> articles = new ArrayList<Article>(jsonArray.length());

    for (int i=0; i < jsonArray.length(); i++) {
      JSONObject articleJson = null;
      try{
        articleJson = jsonArray.getJSONObject(i);
      }catch (Exception e){
        e.printStackTrace();
        continue;
      }
      Article article = Article.fromJson(articleJson);
      if(article != null){
        articles.add(article);
      }
    }

    return articles;
  }

}

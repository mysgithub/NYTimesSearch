package com.codepath.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Shyam Rokde on 2/9/16.
 */
public class Article implements Parcelable {
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
        Log.d("DEBUG", "No Thumbnail Image...");
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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.webUrl);
    dest.writeString(this.headline);
    dest.writeString(this.thumbnail);
  }

  protected Article(Parcel in) {
    this.webUrl = in.readString();
    this.headline = in.readString();
    this.thumbnail = in.readString();
  }

  public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
    @Override
    public Article createFromParcel(Parcel source) {
      return new Article(source);
    }

    @Override
    public Article[] newArray(int size) {
      return new Article[size];
    }
  };
}

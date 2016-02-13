package com.codepath.nytimessearch.models.nytimes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shyam Rokde on 2/13/16.
 */
public class Multimedia {
  @SerializedName("url")
  private String url;

  @SerializedName("subtype")
  private String subtype;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getSubtype() {
    return subtype;
  }

  public void setSubtype(String subtype) {
    this.subtype = subtype;
  }
}

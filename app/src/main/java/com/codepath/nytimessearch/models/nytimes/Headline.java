package com.codepath.nytimessearch.models.nytimes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shyam Rokde on 2/13/16.
 */

public class Headline {
  @SerializedName("main")
  private String main;

  public String getMain() {
    return main;
  }

  public void setMain(String main) {
    this.main = main;
  }
}

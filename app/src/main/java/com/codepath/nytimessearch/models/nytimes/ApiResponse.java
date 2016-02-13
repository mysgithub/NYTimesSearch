package com.codepath.nytimessearch.models.nytimes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shyam Rokde on 2/13/16.
 */
public class ApiResponse {
  @SerializedName("response")
  private Response response;

  public static ApiResponse fromJson(String responseString){
    Gson gson = new GsonBuilder().create();
    ApiResponse response = gson.fromJson(responseString, ApiResponse.class);
    return response;
  }
}

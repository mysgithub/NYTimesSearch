package com.codepath.nytimessearch.models.nytimes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shyam Rokde on 2/13/16.
 */
public class Response {
  @SerializedName("docs")
  private List<Doc> docs;

  public List<Doc> getDocs() {
    return docs;
  }

  public void setDocs(List<Doc> docs) {
    this.docs = docs;
  }

  public Response(){
    docs = new ArrayList<Doc>();
  }


}

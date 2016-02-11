package com.codepath.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Shyam Rokde on 2/10/16.
 */
public class Setting implements Parcelable{
  public String getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(String beginDate) {
    this.beginDate = beginDate;
  }

  public String getSortOrder() {
    return sortOrder;
  }

  public void setSortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
  }

  public Boolean getArts() {
    return arts;
  }

  public void setArts(Boolean arts) {
    this.arts = arts;
  }

  public Boolean getFashionStyle() {
    return fashionStyle;
  }

  public void setFashionStyle(Boolean fashionStyle) {
    this.fashionStyle = fashionStyle;
  }

  public Boolean getSports() {
    return sports;
  }

  public void setSports(Boolean sports) {
    this.sports = sports;
  }

  private String beginDate;
  private String sortOrder;
  private Boolean arts = false;
  private Boolean fashionStyle = false;
  private Boolean sports = false;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.beginDate);
    dest.writeString(this.sortOrder);
    dest.writeValue(this.arts);
    dest.writeValue(this.fashionStyle);
    dest.writeValue(this.sports);
  }

  public Setting() {
  }

  protected Setting(Parcel in) {
    this.beginDate = in.readString();
    this.sortOrder = in.readString();
    this.arts = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.fashionStyle = (Boolean) in.readValue(Boolean.class.getClassLoader());
    this.sports = (Boolean) in.readValue(Boolean.class.getClassLoader());
  }

  public static final Creator<Setting> CREATOR = new Creator<Setting>() {
    public Setting createFromParcel(Parcel source) {
      return new Setting(source);
    }

    public Setting[] newArray(int size) {
      return new Setting[size];
    }
  };
}

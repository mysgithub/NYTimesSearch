package com.codepath.nytimessearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.codepath.nytimessearch.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shyam Rokde on 7/28/16.
 */
public class ViewHolderHeadlineOnly extends RecyclerView.ViewHolder {
  @Bind(R.id.tvTitle) TextView tvTitle;

  public ViewHolderHeadlineOnly(View itemView) {
    super(itemView);

    ButterKnife.bind(this, itemView);
  }
}

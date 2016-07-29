package com.codepath.nytimessearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimessearch.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shyam Rokde on 7/28/16.
 */
public class ViewHolderArticle extends RecyclerView.ViewHolder{

  @Bind(R.id.ivImage) ImageView ivImage;
  @Bind(R.id.tvTitle) TextView tvTitle;

  public ViewHolderArticle(View itemView) {
    super(itemView);

    ButterKnife.bind(this, itemView);
  }
}

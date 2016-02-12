package com.codepath.nytimessearch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shyam Rokde on 2/11/16.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

  public ArrayList<Article> mArticles;
  Context mContext;

  public ArticleAdapter(ArrayList<Article> articles, Context context){
    mArticles = articles;
    mContext = context;
  }

  /**
   * ViewHolder
   */
  public static class ViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.ivImage) ImageView ivImage;
    @Bind(R.id.tvTitle) TextView tvTitle;

    public ViewHolder(final View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    // Inflate custom layout
    View articleResultView = inflater.inflate(R.layout.item_article_result, parent, false);

    // Return new holder instance
    return new ViewHolder(articleResultView);
  }

  @Override
  public void onBindViewHolder(ViewHolder viewHolder, int position) {
    Article article = mArticles.get(position);

    // Set items
    viewHolder.tvTitle.setText(article.getHeadline());
    String thumbnail = article.getThumbnail();
    if(!TextUtils.isEmpty(thumbnail)){
      Picasso.with(mContext).load(thumbnail).into(viewHolder.ivImage);
    }
  }

  @Override
  public int getItemCount() {
    return mArticles.size();
  }

  public void clear() {
    final int size = getItemCount();
    mArticles.clear();
    notifyItemRangeRemoved(0, size);
  }

}

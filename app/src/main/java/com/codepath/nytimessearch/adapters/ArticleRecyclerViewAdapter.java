package com.codepath.nytimessearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Article;

import java.util.ArrayList;

/**
 * Created by Shyam Rokde on 7/28/16.
 */
public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private ArrayList<Article> mArticles;

  private final int THUMBNAIL_HEADLINE = 0, HEADLINE = 1;

  public ArticleRecyclerViewAdapter(ArrayList<Article> articles){
    this.mArticles = articles;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    RecyclerView.ViewHolder viewHolder;
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());

    switch (viewType){
      case THUMBNAIL_HEADLINE:
        View view1 = inflater.inflate(R.layout.item_article_result, parent, false);
        viewHolder = new ViewHolderArticle(view1);
        break;
      case HEADLINE:
        View view2 = inflater.inflate(R.layout.item_article_title, parent, false);
        viewHolder = new ViewHolderHeadlineOnly(view2);
        break;
      default:
        Log.d("DEBUG", "Something is wrong, <<headline>> and <<thumbnail>> missing");
        viewHolder = null;
        break;
    }

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    switch (holder.getItemViewType()){
      case THUMBNAIL_HEADLINE:
        ViewHolderArticle viewHolderArticle = (ViewHolderArticle) holder;
        configureViewHolderArticle(viewHolderArticle, position);
        break;
      case HEADLINE:
        ViewHolderHeadlineOnly viewHolderHeadlineOnly = (ViewHolderHeadlineOnly) holder;
        configureViewHolderHeadline(viewHolderHeadlineOnly, position);
        break;
    }
  }

  @Override
  public int getItemCount() {
    return this.mArticles.size();
  }

  @Override
  public int getItemViewType(int position) {

    Article article = mArticles.get(position);

    if(!article.getThumbnail().isEmpty() && !article.getHeadline().isEmpty()){
      // Title and Thumbnail - Complete Article
      return THUMBNAIL_HEADLINE;
    }else if(!article.getHeadline().isEmpty()){
      // Title Only - Article
      return HEADLINE;
    }else {
      return -1;
    }
  }


  private void configureViewHolderArticle(ViewHolderArticle viewHolder, int position){
    Article article = mArticles.get(position);
    if(article != null){
      viewHolder.tvTitle.setText(article.getHeadline());
      viewHolder.ivImage.setImageResource(0);
      Glide.with(viewHolder.itemView.getContext())
          .load(article.getThumbnail())
          .into(viewHolder.ivImage);
    }
  }

  private void configureViewHolderHeadline(ViewHolderHeadlineOnly viewHolder, int position){
    Article article = mArticles.get(position);
    if(article != null){
      viewHolder.tvTitle.setText(article.getHeadline());
    }
  }

  public void clear(){
    final int size = getItemCount();
    mArticles.clear();
    notifyItemRangeRemoved(0, size);
  }

}

package com.codepath.nytimessearch.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.nytimessearch.R;
import com.codepath.nytimessearch.models.Article;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

  @Bind(R.id.wvArticle) WebView wvArticle;

  private ShareActionProvider mShareActionProvider;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_article);

    ButterKnife.bind(this);
    // Enable up icon
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    // Get Data
    //Article article = (Article) getIntent().getSerializableExtra("article");
    Article article = getIntent().getParcelableExtra("article");

    // Title
    ActionBar actionBar = getSupportActionBar();
    if(actionBar != null)
      actionBar.setTitle(article.getHeadline());

    wvArticle.setWebViewClient(new WebViewClient(){
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
      }
    });
    wvArticle.loadUrl(article.getWebUrl());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu_article, menu);

    MenuItem item = menu.findItem(R.id.menu_item_share);

    // Fetch and store ShareActionProvider
    mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
    Intent shareIntent = new Intent(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    // get reference to WebView
    shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());

    mShareActionProvider.setShareIntent(shareIntent);

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // TODO: Lets check if this is needed or not if Manifest file already has UP..
    switch (item.getItemId()) {
      // This is the up button
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }

    return super.onOptionsItemSelected(item);
  }
}

/*
 * To-Do Copyright (C) 2017 Fatih.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fs.todo.views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import javax.inject.Inject;
import org.fs.core.AbstractActivity;
import org.fs.todo.BuildConfig;
import org.fs.todo.R;
import org.fs.todo.ToDoApplication;
import org.fs.todo.commons.components.AppComponent;
import org.fs.todo.commons.components.DaggerActivityComponent;
import org.fs.todo.commons.modules.ActivityModule;
import org.fs.todo.presenters.MainActivityPresenter;

public class MainActivity extends AbstractActivity<MainActivityPresenter>
    implements MainActivityView {

  @Inject MainActivityPresenter presenter;
  private Unbinder unbinder;

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.viewPager) ViewPager viewPager;
  @BindView(R.id.progress) ProgressBar progress;

  @Override public void onCreate(Bundle restoreState) {
    super.onCreate(restoreState);
    setContentView(R.layout.view_main_activity);
    this.unbinder = ButterKnife.bind(this);
    //inject it this way
    DaggerActivityComponent.builder()
        .appComponent(component())
        .activityModule(new ActivityModule(this))
        .build()
        .inject(this);

    presenter.queryIntent(getIntent());
    presenter.restoreState(restoreState != null ? restoreState : getIntent().getExtras());
    presenter.onCreate();
  }

  @Override protected void onNewIntent(Intent intent) {
    presenter.queryIntent(intent);
  }

  @Override public void setUp() {
    setSupportActionBar(toolbar);
    ActionBar supportActionBar = getSupportActionBar();
    if (supportActionBar != null) {
      supportActionBar.setDisplayShowTitleEnabled(false);
      supportActionBar.setDisplayHomeAsUpEnabled(false);
    }
  }

  @Override public void onSaveInstanceState(Bundle storeState) {
    super.onSaveInstanceState(storeState);
    presenter.storeState(storeState);
  }

  @Override public void onStart() {
    super.onStart();
    presenter.onStart();
  }

  @Override public void onStop() {
    presenter.onStop();
    super.onStop();
  }

  @Override protected void onDestroy() {
    presenter.onDestroy();
    if(unbinder != null) {
      unbinder.unbind();
    }
    super.onDestroy();
  }

  @Override public void showError(String errorString) {
    final View view = view();
    if (view != null) {
      Snackbar.make(view, errorString, Snackbar.LENGTH_LONG).show();
    }
  }

  @Override public void showError(String errorString, String actionTextString,
      View.OnClickListener callback) {
    final View view = view();
    if (view != null) {
      final Snackbar snackbar = Snackbar.make(view, errorString, Snackbar.LENGTH_LONG);
      snackbar.setAction(actionTextString, v -> {
        if (callback != null) {
          callback.onClick(v);
        }
        snackbar.dismiss();
      });
      snackbar.show();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.search_options, menu);
    //only way of doing this is this
    Context context = getContext();
    SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
    SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
    searchView.setIconified(true);
    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
    return super.onCreateOptionsMenu(menu);
  }

  @Override public void showProgress() {
    progress.setVisibility(View.VISIBLE);
    progress.setIndeterminate(true);
  }

  @Override public void hideProgress() {
    progress.setIndeterminate(false);
    progress.setVisibility(View.INVISIBLE);
  }

  @Override public void finish() {
    super.finish();
    overridePendingTransition(R.anim.scale_in, R.anim.translate_right_out);
  }

  @Override public String getStringResource(@StringRes int stringId) {
    return getString(stringId);
  }

  @Override public Context getContext() {
    return this;
  }

  @Override public boolean isAvailable() {
    return !isFinishing();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  @Override protected String getClassTag() {
    return MainActivity.class.getSimpleName();
  }

  private View view() {
    return findViewById(android.R.id.content);
  }

  private AppComponent component() {
    ToDoApplication app = (ToDoApplication) getApplication();
    if (app != null) {
      return app.provideAppComponent();
    }
    return null;
  }
}
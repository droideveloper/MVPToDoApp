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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import javax.inject.Inject;
import org.fs.core.AbstractActivity;
import org.fs.todo.BuildConfig;
import org.fs.todo.R;
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
        .activityModule(new ActivityModule(this))
        .build()
        .inject(this);
    presenter.restoreState(restoreState != null ? restoreState : getIntent().getExtras());
    presenter.onCreate();
  }

  @Override public void setUp() {
    toolbar.setTitle(null);
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
}
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

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import javax.inject.Inject;
import org.fs.core.AbstractActivity;
import org.fs.todo.BuildConfig;
import org.fs.todo.R;
import org.fs.todo.presenters.MainActivityPresenter;
import org.fs.todo.views.adapters.StateToDoAdapter;

public class MainActivity extends AbstractActivity<MainActivityPresenter>
    implements MainActivityView {

  private final static int OFFSET_LIMIT = 3;

  @Inject StateToDoAdapter stateToDoAdapter;

  private EditText viewEditText;
  private ViewPager viewPager;
  private ProgressBar viewProgress;

  @Override public void onCreate(Bundle restoreState) {
    super.onCreate(restoreState);
    setContentView(R.layout.view_main_activity);

    viewEditText = findViewById(R.id.viewEditText);
    viewPager = findViewById(R.id.viewPager);
    viewProgress = findViewById(R.id.viewProgress);

    presenter.restoreState(restoreState != null ? restoreState : getIntent().getExtras());
    presenter.onCreate();
  }

  @Override public void setUp() {
    viewEditText.addTextChangedListener(presenter.provideTextWatcher());
    viewEditText.setOnEditorActionListener(presenter.provideEditorActionListener());
    viewPager.setOffscreenPageLimit(OFFSET_LIMIT);
    viewPager.setAdapter(stateToDoAdapter);
  }

  @Override public void setTextStyle(int textStyle) {
    viewEditText.setTypeface(null, textStyle);
  }

  @Override public void showProgress() {
    viewProgress.setVisibility(View.VISIBLE);
    viewProgress.setIndeterminate(true);
  }

  @Override public void hideProgress() {
    viewProgress.setIndeterminate(false);
    viewProgress.setVisibility(View.GONE);
  }

  @Override public FragmentManager provideSupportFragmentManager() {
    return getSupportFragmentManager();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  @Override protected String getClassTag() {
    return MainActivity.class.getSimpleName();
  }
}
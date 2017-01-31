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
package org.fs.todo.presenters;

import android.content.Intent;
import android.util.Log;
import java.util.Locale;
import org.fs.common.AbstractPresenter;
import org.fs.common.ThreadManager;
import org.fs.todo.BuildConfig;
import org.fs.todo.views.MainActivityView;

public class MainActivityPresenterImp extends AbstractPresenter<MainActivityView>
    implements MainActivityPresenter {

  public MainActivityPresenterImp(MainActivityView view) {
    super(view);
  }

  @Override public void onCreate() {
    view.setUp();
    view.showProgress();
  }

  @Override public void onStart() {
    view.showError(String.format(Locale.getDefault(), "Will hide progress, in %d seconds.", 5), "UNDO", (callback) -> {
      log(Log.ERROR, "Changed Context");
    });
    ThreadManager.runOnUiThreadDelayed(5000, () -> {
      if(view.isAvailable()) {
        view.hideProgress();
      }
    });
  }

  @Override public void onStop() { }

  @Override public void queryIntent(Intent query) {
    log(query.toString());
  }

  @Override protected String getClassTag() {
    return MainActivityPresenterImp.class.getSimpleName();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }
}
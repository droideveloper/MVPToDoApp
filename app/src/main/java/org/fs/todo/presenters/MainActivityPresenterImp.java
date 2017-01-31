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
import javax.inject.Inject;
import org.fs.common.AbstractPresenter;
import org.fs.todo.BuildConfig;
import org.fs.todo.commons.components.DaggerPresenterComponent;
import org.fs.todo.commons.modules.PresenterModule;
import org.fs.todo.views.MainActivityView;
import org.fs.todo.views.adapters.StateToDoAdapter;

public class MainActivityPresenterImp extends AbstractPresenter<MainActivityView>
    implements MainActivityPresenter {

  @Inject StateToDoAdapter todoAdapter;

  public MainActivityPresenterImp(MainActivityView view) {
    super(view);
  }

  @Override public void onCreate() {
    view.setUp();
    DaggerPresenterComponent.builder()
        .appComponent(view.provideAppComponent())
        .presenterModule(new PresenterModule(view.provideFragmentManager()))
        .build()
        .inject(this);
  }

  @Override public void onStart() {
    if(view.isAvailable()) {
      view.setStateAdapter(todoAdapter);
    }
  }

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
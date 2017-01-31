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
package org.fs.todo.commons.modules;

import dagger.Module;
import dagger.Provides;
import org.fs.todo.commons.scopes.PerActivity;
import org.fs.todo.presenters.MainActivityPresenter;
import org.fs.todo.presenters.MainActivityPresenterImp;
import org.fs.todo.views.MainActivityView;

@Module public class ActivityModule {

  private final MainActivityView view;

  public ActivityModule(final MainActivityView view) {
    this.view = view;
  }

  @PerActivity @Provides MainActivityPresenter providePresenter() {
    return new MainActivityPresenterImp(this.view);
  }
}
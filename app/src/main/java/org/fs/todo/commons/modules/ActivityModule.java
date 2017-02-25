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

import android.support.v4.util.Pair;
import dagger.Module;
import dagger.Provides;
import java.util.Arrays;
import org.fs.todo.commons.ToDoStorage;
import org.fs.todo.commons.scopes.PerActivity;
import org.fs.todo.entities.DisplayOptions;
import org.fs.todo.presenters.MainActivityPresenter;
import org.fs.todo.presenters.MainActivityPresenterImp;
import org.fs.todo.views.MainActivityView;
import org.fs.todo.views.adapters.StateToDoAdapter;

@Module public class ActivityModule {

  private final MainActivityView view;

  public ActivityModule(final MainActivityView view) {
    this.view = view;
  }

  @PerActivity @Provides MainActivityPresenter providePresenter(StateToDoAdapter todoAdapter, ToDoStorage storage) {
    return new MainActivityPresenterImp(this.view, todoAdapter, storage);
  }

  @Provides public StateToDoAdapter provideStateAdapter() {
    return new StateToDoAdapter(view.provideFragmentManager(),
        Arrays.asList(
            new Pair<>(DisplayOptions.ALL, "ALL"),
            new Pair<>(DisplayOptions.ACTIVE, "ACTIVE"),
            new Pair<>(DisplayOptions.INACTIVE, "INACTIVE")));
  }
}
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

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import dagger.Module;
import dagger.Provides;
import org.fs.todo.commons.scopes.PerFragment;
import org.fs.todo.presenters.TaskStateFragmentPresenter;
import org.fs.todo.presenters.TaskStateFragmentPresenterImp;
import org.fs.todo.views.TaskStateFragmentView;
import org.fs.todo.views.adapters.ToDoAdapter;

@Module public class FragmentModule {

  private final TaskStateFragmentView view;

  public FragmentModule(final TaskStateFragmentView view) {
    this.view = view;
  }

  @PerFragment @Provides public TaskStateFragmentPresenter providePresenter() {
    return new TaskStateFragmentPresenterImp(view);
  }

  @PerFragment @Provides public RecyclerView.LayoutManager provideLayoutManager() {
    return new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
  }

  @PerFragment @Provides public RecyclerView.ItemAnimator provideItemAnimator() {
    return new DefaultItemAnimator();
  }

  @PerFragment @Provides public ToDoAdapter provideAdapter(TaskStateFragmentPresenter presenter) {
    return new ToDoAdapter(presenter.provideObservableList(), view.getContext());
  }
}
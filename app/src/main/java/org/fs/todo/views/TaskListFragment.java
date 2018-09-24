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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import javax.inject.Inject;
import org.fs.core.AbstractFragment;
import org.fs.todo.BuildConfig;
import org.fs.todo.R;
import org.fs.todo.presenters.TaskListFragmentPresenter;
import org.fs.todo.presenters.TaskListFragmentPresenterImp;
import org.fs.todo.views.adapters.ToDoAdapter;

public class TaskListFragment extends AbstractFragment<TaskListFragmentPresenter>
    implements TaskListFragmentView {

  private final static int RECYCLER_CACHE_SIZE = 10;

  @BindView(R.id.viewRecycler) RecyclerView viewRecycler;

  @Inject ToDoAdapter todoAdapter;

  private Unbinder unbinder;

  public static TaskListFragment newInstance(final int displayOption) {
    Bundle args = new Bundle();
    args.putInt(TaskListFragmentPresenterImp.KEY_OPTIONS, displayOption);

    TaskListFragment fragment = new TaskListFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(@NonNull LayoutInflater factory, ViewGroup parent, Bundle restoreState) {
    final View view = factory.inflate(R.layout.view_task_state_fragment, parent, false);
    unbinder = ButterKnife.bind(view);
    return view;
  }

  @Override public void onActivityCreated(Bundle restoreState) {
    super.onActivityCreated(restoreState);

    presenter.restoreState(restoreState != null ? restoreState : getArguments());
    presenter.onCreate();
  }

  @Override public void setUp() {
    viewRecycler.setHasFixedSize(true);
    viewRecycler.setItemViewCacheSize(RECYCLER_CACHE_SIZE);
    viewRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    viewRecycler.setAdapter(todoAdapter);
  }

  @Override public void onDestroy() {
    if (unbinder != null) {
      unbinder.unbind();
    }
    super.onDestroy();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  @Override protected String getClassTag() {
    return TaskListFragment.class.getSimpleName();
  }

}
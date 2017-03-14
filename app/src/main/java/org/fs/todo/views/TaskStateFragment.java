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
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import javax.inject.Inject;
import org.fs.core.AbstractFragment;
import org.fs.todo.BuildConfig;
import org.fs.todo.R;
import org.fs.todo.ToDoApplication;
import org.fs.todo.commons.components.AppComponent;
import org.fs.todo.commons.components.DaggerFragmentComponent;
import org.fs.todo.commons.modules.FragmentModule;
import org.fs.todo.presenters.TaskStateFragmentPresenter;
import org.fs.todo.presenters.TaskStateFragmentPresenterImp;
import org.fs.todo.views.adapters.ToDoAdapter;
import org.fs.util.ViewUtility;

public class TaskStateFragment extends AbstractFragment<TaskStateFragmentPresenter>
    implements TaskStateFragmentView {

  private RecyclerView recycler;

  @Inject TaskStateFragmentPresenter presenter;
  @Inject RecyclerView.LayoutManager layoutManager;
  @Inject RecyclerView.ItemAnimator itemAnimator;
  @Inject ToDoAdapter todoAdapter;

  public static TaskStateFragment newInstance(final int displayOption) {
    Bundle args = new Bundle();
    args.putInt(TaskStateFragmentPresenterImp.KEY_OPTIONS, displayOption);

    TaskStateFragment fragment = new TaskStateFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater factory, ViewGroup parent, Bundle restoreState) {
    final View view = factory.inflate(R.layout.view_task_state_fragment, parent, false);
    recycler = ViewUtility.findViewById(view, R.id.recycler);
    return view;
  }

  @Override public void onActivityCreated(Bundle restoreState) {
    super.onActivityCreated(restoreState);
    DaggerFragmentComponent.builder()
        .appComponent(dependency())
        .fragmentModule(new FragmentModule(this))
        .build()
        .inject(this);
    presenter.restoreState(restoreState != null ? restoreState : getArguments());
    presenter.onCreate();
  }

  @Override public void setUp() {
    recycler.setHasFixedSize(true);
    recycler.setLayoutManager(layoutManager);
    recycler.setItemAnimator(itemAnimator);
    recycler.setAdapter(todoAdapter);
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    presenter.storeState(outState);
  }

  @Override public void onStart() {
    super.onStart();
    presenter.onStart();
  }

  @Override public void onStop() {
    presenter.onStop();
    super.onStop();
  }

  @Override public AppComponent provideAppComponent() {
    ToDoApplication app = (ToDoApplication) getActivity().getApplication();
    if(app != null) {
      return app.provideAppComponent();
    }
    return null;
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  @Override protected String getClassTag() {
    return TaskStateFragment.class.getSimpleName();
  }

  private AppComponent dependency() {
    FragmentActivity activity = getActivity();
    if (activity != null) {
      ToDoApplication application = (ToDoApplication) activity.getApplication();
      return application.provideAppComponent();
    }
    return null;
  }
}
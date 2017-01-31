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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.inject.Inject;
import org.fs.core.AbstractFragment;
import org.fs.todo.BuildConfig;
import org.fs.todo.R;
import org.fs.todo.commons.components.DaggerFragmentComponent;
import org.fs.todo.commons.modules.FragmentModule;
import org.fs.todo.entities.Task;
import org.fs.todo.presenters.TaskStateFragmentPresenter;
import org.fs.todo.views.adapters.ToDoAdapter;

public class TaskStateFragment extends AbstractFragment<TaskStateFragmentPresenter>
    implements TaskStateFragmentView {

  @BindView(R.id.recycler) RecyclerView recycler;

  @Inject TaskStateFragmentPresenter presenter;
  @Inject RecyclerView.LayoutManager layoutManager;
  @Inject RecyclerView.ItemAnimator itemAnimator;
  @Inject ToDoAdapter todoAdapter;

  private WeakReference<View> viewReference;
  private Unbinder unbinder;

  @Nullable @Override
  public View onCreateView(LayoutInflater factory, ViewGroup parent, Bundle restoreState) {
    final View view = factory.inflate(R.layout.view_task_state_fragment, parent, false);
    this.unbinder = ButterKnife.bind(view);
    viewReference = new WeakReference<>(view);
    return view;
  }

  @Override public void onActivityCreated(Bundle restoreState) {
    super.onActivityCreated(restoreState);
    DaggerFragmentComponent.builder()
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

  @Override public void onDestroy() {
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

  @Override public void setTaskList(List<Task> taskList) {
    todoAdapter.appendData(taskList, false);
  }

  @Override public String getStringResource(@StringRes int stringId) {
    return getActivity().getString(stringId);
  }

  @Override public Context getContext() {
    return getActivity();
  }

  @Override public boolean isAvailable() {
    return super.isCallingSafe();
  }

  @Override public void finish() {
    throw new IllegalArgumentException("fragment instances does not support finish options");
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  @Override protected String getClassTag() {
    return TaskStateFragment.class.getSimpleName();
  }

  private View view() {
    return viewReference != null ? viewReference.get() : null;
  }
}
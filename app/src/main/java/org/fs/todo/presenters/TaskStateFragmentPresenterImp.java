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

import android.os.Bundle;
import javax.inject.Inject;
import org.fs.common.AbstractPresenter;
import org.fs.common.BusManager;
import org.fs.todo.BuildConfig;
import org.fs.todo.commons.ToDoStorage;
import org.fs.todo.commons.components.DaggerPresenterComponent;
import org.fs.todo.commons.modules.PresenterModule;
import org.fs.todo.entities.DisplayOptions;
import org.fs.todo.entities.Task;
import org.fs.todo.entities.TaskState;
import org.fs.todo.views.TaskStateFragmentView;
import org.fs.util.Collections;
import org.fs.util.ObservableList;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TaskStateFragmentPresenterImp extends AbstractPresenter<TaskStateFragmentView>
    implements TaskStateFragmentPresenter {

  public final static String KEY_DATA_SET = "key.task.array";
  public final static String KEY_OPTIONS  = "key.display.option";

  @Inject ToDoStorage storage;

  private ObservableList<Task> dataSet;
  private int                  displayOption;
  private Subscription         register;

  public TaskStateFragmentPresenterImp(TaskStateFragmentView view) {
    super(view);
    this.dataSet = new ObservableList<>();
  }

  @Override public void onCreate() {
    view.setUp();
    DaggerPresenterComponent.builder()
      .appComponent(view.provideAppComponent())
      .presenterModule(new PresenterModule(null))
      .build()
      .inject(this);
  }

  @Override public void restoreState(Bundle restoreState) {
    if(restoreState != null) {
      // this was the reason why we do not have new context in position.
      if(restoreState.containsKey(KEY_DATA_SET)) {
        dataSet.addAll(restoreState.getParcelableArrayList(KEY_DATA_SET));
      }
      displayOption = restoreState.getInt(KEY_OPTIONS, DisplayOptions.ALL);
    }
  }

  @Override public void storeState(Bundle storeState) {
    if(!Collections.isNullOrEmpty(dataSet)) {
      storeState.putParcelableArrayList(KEY_DATA_SET, dataSet.toArrayList());
    }
    storeState.putInt(KEY_OPTIONS, displayOption);
  }

  @Override public void onStart() {
    if(view.isAvailable()) {
      if (Collections.isNullOrEmpty(dataSet)) {
        storage.all()
          .flatMap(Observable::from)
          .filter(this::accepted)
          .toList()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(tasks -> {
            if(view.isAvailable()) {
              dataSet.addAll(tasks);
            }
          }, this::log);
      }
      // register added
      register = BusManager.add((evt) -> {
        // change
      });
    }
  }

  @Override public ObservableList<Task> provideObservableList() {
    return dataSet;
  }

  @Override public void onStop() {
    BusManager.remove(register);
  }

  @Override protected String getClassTag() {
    return TaskStateFragmentPresenterImp.class.getSimpleName();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  private boolean accepted(Task task) {
    switch (displayOption) {
      case DisplayOptions.ACTIVE:
        return task.getState() == TaskState.ACTIVE;
      case DisplayOptions.INACTIVE:
        return task.getState() == TaskState.INACTIVE;
      case DisplayOptions.ALL:
        return task.getState() != TaskState.COMPLETED;
      default:
        return false;
    }
  }
}
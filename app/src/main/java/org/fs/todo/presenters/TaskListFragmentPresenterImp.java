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
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.List;
import javax.inject.Inject;
import org.fs.common.AbstractPresenter;
import org.fs.common.BusManager;
import org.fs.common.scope.ForFragment;
import org.fs.todo.BuildConfig;
import org.fs.todo.entities.DisplayOptions;
import org.fs.todo.entities.Option;
import org.fs.todo.entities.Task;
import org.fs.todo.entities.TaskState;
import org.fs.todo.entities.events.DisplayEvent;
import org.fs.todo.repository.TaskRepository;
import org.fs.todo.views.TaskListFragmentView;
import org.fs.util.Collections;
import org.fs.util.ObservableList;
import org.fs.util.RxUtility;

@ForFragment
public class TaskListFragmentPresenterImp extends AbstractPresenter<TaskListFragmentView>
    implements TaskListFragmentPresenter {

  private final static String KEY_DATA_SET = "key.task.array";
  public final static String KEY_OPTIONS  = "key.display.option";

  private final TaskRepository taskRepository;
  private final ObservableList<Task> dataSet;

  private int displayOption;

  private final CompositeDisposable disposeBag = new CompositeDisposable();

  @Inject public TaskListFragmentPresenterImp(TaskListFragmentView view, ObservableList<Task> dataSet, TaskRepository taskRepository) {
    super(view);
    this.dataSet = dataSet;
    this.taskRepository = taskRepository;
  }

  @Override public void onCreate() {
    if (view.isAvailable()) {
      view.setUp();
    }
  }

  @Override public void restoreState(Bundle restoreState) {
    if(restoreState != null) {
      // this was the reason why we do not have new context in position.
      if(restoreState.containsKey(KEY_DATA_SET)) {
        final List<Task> tasks = restoreState.getParcelableArrayList(KEY_DATA_SET);
        if (!Collections.isNullOrEmpty(tasks)) {
          dataSet.addAll(tasks);
        }
      }
      displayOption = restoreState.getInt(KEY_OPTIONS, DisplayOptions.ALL);
    }
  }

  @Override public void storeState(Bundle storeState) {
    if(!Collections.isNullOrEmpty(dataSet)) {
      storeState.putParcelableArrayList(KEY_DATA_SET, dataSet);
    }
    storeState.putInt(KEY_OPTIONS, displayOption);
  }

  @Override public void onStart() {
    if(view.isAvailable()) {
      // bus manager disposable
      final Disposable busManagerDisposable = BusManager.add((e) -> {
        if (e instanceof DisplayEvent) {
          DisplayEvent event = (DisplayEvent) e;
          if (event.option() == Option.ADD) {
            optionAdd(event.task());
          } else if (event.option() == Option.CHANGE) {
            optionChange(event.task());
          } else if (event.option() == Option.REMOVE) {
            optionRemove(event.task());
          }
        }
      });
      // add bag
      disposeBag.add(busManagerDisposable);
      // check if initial load needed
      checkIfInitialLoadNeeded();
    }
  }

  @Override public void onStop() {
    disposeBag.clear();
  }

  @Override protected String getClassTag() {
    return TaskListFragmentPresenterImp.class.getSimpleName();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  private void optionRemove(Task task) {
    final int index = dataSet.indexOf(task::equals);
    if (index != -1) {
      dataSet.remove(index);
    }
  }

  private void optionAdd(Task task) {
    dataSet.add(task);
  }

  private void optionChange(Task task) {
    final int index = dataSet.indexOf(task::equals);
    if (displayOption == DisplayOptions.ALL) {
      if (index != -1) {
        dataSet.set(index, task);
      }
    } else if (displayOption == DisplayOptions.ACTIVE) {
      if (task.getTaskState() == TaskState.ACTIVE) {
        if (index == -1) {
          dataSet.add(task);
        }
      } else if (task.getTaskState() == TaskState.INACTIVE) {
        if (index != -1) {
          dataSet.remove(index);
        }
      }
    } else if (displayOption == DisplayOptions.INACTIVE) {
      if (task.getTaskState() == TaskState.ACTIVE) {
        if (index != -1) {
          dataSet.remove(index);
        }
      } else if (task.getTaskState() == TaskState.INACTIVE) {
        if (index == -1) {
          dataSet.add(task);
        }
      }
    }
  }

  private void checkIfInitialLoadNeeded() {
    if (!Collections.isNullOrEmpty(dataSet)) {
      load();
    }
  }

  private void load() {
    final int state = taskStateForDisplayOption();
    final Disposable loadDataDisposable = taskRepository.queryByTaskState(state)
      .compose(RxUtility.toAsyncFlowable(view))
      .subscribe(data -> {
        if (view.isAvailable()) {
          if (!Collections.isNullOrEmpty(data)) {
            dataSet.addAll(data);
          }

        }
      }, error -> {
        if (view.isAvailable()) {
          view.showError(error.getLocalizedMessage());
        }
        log(error);
      });
    // add bag
    disposeBag.add(loadDataDisposable);
  }

  private int taskStateForDisplayOption() {
    switch (displayOption) {
      case DisplayOptions.ACTIVE:
        return TaskState.of(TaskState.ACTIVE);
      case DisplayOptions.INACTIVE:
        return TaskState.of(TaskState.INACTIVE);
      case DisplayOptions.ALL:
        return TaskState.of(TaskState.COMPLETED);
      default:
        return -1;
    }
  }
}
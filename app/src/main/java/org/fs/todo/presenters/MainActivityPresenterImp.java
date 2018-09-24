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

import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.Date;
import javax.inject.Inject;
import org.fs.common.AbstractPresenter;
import org.fs.common.BusManager;
import org.fs.common.scope.ForActivity;
import org.fs.todo.BuildConfig;
import org.fs.todo.commons.SimpleTextWatcher;
import org.fs.todo.entities.Option;
import org.fs.todo.entities.Task;
import org.fs.todo.entities.TaskState;
import org.fs.todo.entities.events.AddTaskEvent;
import org.fs.todo.entities.events.ChangeTaskEvent;
import org.fs.todo.entities.events.DisplayEvent;
import org.fs.todo.entities.events.RemoveTaskEvent;
import org.fs.todo.repository.TaskRepository;
import org.fs.todo.views.MainActivityView;
import org.fs.util.RxUtility;
import org.fs.util.StringUtility;

@ForActivity
public class MainActivityPresenterImp extends AbstractPresenter<MainActivityView> implements MainActivityPresenter {

  private final TaskRepository taskRepository;

  private final CompositeDisposable disposeBag = new CompositeDisposable();

  @Inject
  public MainActivityPresenterImp(MainActivityView view, TaskRepository taskRepository) {
    super(view);
    this.taskRepository = taskRepository;
  }

  @Override public void onCreate() {
    if (view.isAvailable()) {
      view.setUp();
    }
  }

  @Override public void onStart() {
    if(view.isAvailable()) {
      // observe bus manager
      final Disposable busManagerDisposable = BusManager.add((e) -> {
        if (e instanceof AddTaskEvent) {
          AddTaskEvent event = (AddTaskEvent) e;
          addTask(event.text());
        } else if (e instanceof RemoveTaskEvent) {
          RemoveTaskEvent event = (RemoveTaskEvent) e;
          removeTask(event.task());
        } else if (e instanceof ChangeTaskEvent) {
          ChangeTaskEvent event = (ChangeTaskEvent) e;
          updateTask(event.task());
        }
      });

      disposeBag.add(busManagerDisposable);
    }
  }

  @Override public void onStop() {
    disposeBag.clear();
  }

  @Override public void onBackPressed() {
    if (view.isAvailable()) {
      view.finish();
    }
  }

  @Override public TextWatcher provideTextWatcher() {
    return new SimpleTextWatcher() {
      @Override public void afterTextChanged(Editable s) {
        if(view.isAvailable()) {
          String str = s.toString();
          if (StringUtility.isNullOrEmpty(str)) {
            view.setTextStyle(Typeface.ITALIC);
          } else {
            view.setTextStyle(Typeface.NORMAL);
          }
        }
      }
    };
  }

  @Override public TextView.OnEditorActionListener provideEditorActionListener() {
    return (textView, actionId, event) -> {
      if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
        if (view.isAvailable()) {
          String str = textView.getText().toString();
          if (!StringUtility.isNullOrEmpty(str)) {
            BusManager.send(new AddTaskEvent(str));
            textView.setText(null);
          }
        }
      }
      return false;
    };
  }

  @Override protected String getClassTag() {
    return MainActivityPresenterImp.class.getSimpleName();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  private void addTask(String text) {
    // add those here
    Task task = new Task.Builder()
      .updatedAt(new Date())
      .createdAt(new Date())
      .text(text)
      .state(TaskState.ACTIVE)
      .build();
    // do task repository
    final Disposable addTaskDisposable = taskRepository.insert(task)
      .compose(RxUtility.toAsyncCompletable())
      .subscribe(() -> BusManager.send(new DisplayEvent(task, Option.ADD)), error -> {
        if (view.isAvailable()) {
          view.showError(error.getLocalizedMessage());
        }
        log(error);
      });
    disposeBag.add(addTaskDisposable);
  }

  private void removeTask(Task task) {
    final Disposable removeTaskDisposable = taskRepository.delete(task)
      .compose(RxUtility.toAsyncCompletable())
      .subscribe(() -> BusManager.send(new DisplayEvent(task, Option.REMOVE)), error -> {
        if (view.isAvailable()) {
          view.showError(error.getLocalizedMessage());
        }
        log(error);
      });
    // add bag
    disposeBag.add(removeTaskDisposable);
  }

  private void updateTask(Task task) {
    final Disposable updateTaskDisposable = taskRepository.update(task)
      .compose(RxUtility.toAsyncCompletable())
      .subscribe(() -> BusManager.send(new DisplayEvent(task, Option.CHANGE)), error -> {
        if (view.isAvailable()) {
          view.showError(error.getLocalizedMessage());
        }
        log(error);
      });
    // add bag
    disposeBag.add(updateTaskDisposable);
  }
}
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
import java.util.Date;
import javax.inject.Inject;
import org.fs.common.AbstractPresenter;
import org.fs.common.BusManager;
import org.fs.todo.BuildConfig;
import org.fs.todo.commons.SimpleTextWatcher;
import org.fs.todo.commons.ToDoStorage;
import org.fs.todo.commons.components.DaggerPresenterComponent;
import org.fs.todo.commons.modules.PresenterModule;
import org.fs.todo.entities.Option;
import org.fs.todo.entities.Task;
import org.fs.todo.entities.TaskState;
import org.fs.todo.entities.events.AddTaskEvent;
import org.fs.todo.entities.events.ChangeTaskEvent;
import org.fs.todo.entities.events.DisplayEvent;
import org.fs.todo.entities.events.RemoveTaskEvent;
import org.fs.todo.views.MainActivityView;
import org.fs.todo.views.adapters.StateToDoAdapter;
import org.fs.util.StringUtility;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivityPresenterImp extends AbstractPresenter<MainActivityView>
    implements MainActivityPresenter {

  @Inject StateToDoAdapter todoAdapter;
  @Inject ToDoStorage storage;

  private Subscription register;

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
    register = BusManager.add((e) -> {
      if (e instanceof AddTaskEvent) {
        AddTaskEvent event = (AddTaskEvent) e;
        Task task = new Task.Builder()
          .updatedAt(new Date())
          .createdAt(new Date())
          .text(event.text())
          .state(TaskState.ACTIVE)
          .build();

        storage.create(task)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(created -> {
            if (created) {
              BusManager.send(new DisplayEvent(task, Option.ADD));
            }
          }, this::log);
      } else if (e instanceof RemoveTaskEvent) {
        RemoveTaskEvent event = (RemoveTaskEvent) e;

        storage.delete(event.task())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(deleted -> {
            if (deleted) {
              BusManager.send(new DisplayEvent(event.task(), Option.REMOVE));
            }
          }, this::log);
      } else if (e instanceof ChangeTaskEvent) {
        ChangeTaskEvent event = (ChangeTaskEvent) e;

        storage.update(event.task())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(updated -> {
            if (updated) {
              BusManager.send(new DisplayEvent(event.task(), Option.CHANGE));
            }
          }, this::log);
      }
    });
  }

  @Override public void onStop() {
    BusManager.remove(register);
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
}
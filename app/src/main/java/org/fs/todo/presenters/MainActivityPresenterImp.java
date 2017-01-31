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
import javax.inject.Inject;
import org.fs.common.AbstractPresenter;
import org.fs.common.BusManager;
import org.fs.todo.BuildConfig;
import org.fs.todo.commons.SimpleTextWatcher;
import org.fs.todo.commons.components.DaggerPresenterComponent;
import org.fs.todo.commons.modules.PresenterModule;
import org.fs.todo.entities.events.AddTaskEvent;
import org.fs.todo.views.MainActivityView;
import org.fs.todo.views.adapters.StateToDoAdapter;
import org.fs.util.StringUtility;

public class MainActivityPresenterImp extends AbstractPresenter<MainActivityView>
    implements MainActivityPresenter {

  @Inject StateToDoAdapter todoAdapter;

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
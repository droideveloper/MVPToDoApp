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

import org.fs.common.AbstractPresenter;
import org.fs.todo.BuildConfig;
import org.fs.todo.views.TaskStateFragmentView;

public class TaskStateFragmentPresenterImp extends AbstractPresenter<TaskStateFragmentView>
    implements TaskStateFragmentPresenter {

  public TaskStateFragmentPresenterImp(TaskStateFragmentView view) {
    super(view);
  }

  @Override public void onStart() {
    //TODO implement
  }

  @Override public void onStop() {
    //TODO implement
  }

  @Override protected String getClassTag() {
    return TaskStateFragmentPresenterImp.class.getSimpleName();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }
}
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
package org.fs.todo.commons.components;

import dagger.Component;
import org.fs.todo.commons.modules.PresenterModule;
import org.fs.todo.commons.scopes.PerPresenter;
import org.fs.todo.presenters.MainActivityPresenterImp;
import org.fs.todo.presenters.TaskStateFragmentPresenterImp;

@PerPresenter
@Component(modules = PresenterModule.class, dependencies = AppComponent.class)
public interface PresenterComponent {
  void inject(MainActivityPresenterImp presenter);
  void inject(TaskStateFragmentPresenterImp presenter);
}

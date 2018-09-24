/*
 * To-Do Copyright (C) 2018 Fatih.
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
package org.fs.todo.commons.modules;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.android.FragmentKey;
import org.fs.common.scope.ForActivity;
import org.fs.common.scope.ForFragment;
import org.fs.todo.presenters.MainActivityPresenter;
import org.fs.todo.presenters.MainActivityPresenterImp;
import org.fs.todo.views.MainActivity;
import org.fs.todo.views.MainActivityView;
import org.fs.todo.views.TaskListFragment;

@Module
public abstract class ActivityModule {

  @Binds @ForActivity public abstract MainActivityView bindMainActivityView(MainActivity activity);
  @Binds @ForActivity public abstract MainActivityPresenter bindMainActivityPresenter(MainActivityPresenterImp presenter);

  @ForFragment @ContributesAndroidInjector(modules = { FragmentModule.class, ProviderFragmentModule.class })
  public abstract TaskListFragment bindTaskListFragment();
}
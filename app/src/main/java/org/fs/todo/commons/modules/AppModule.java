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

import android.app.Application;
import android.content.Context;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import javax.inject.Singleton;
import org.fs.common.scope.ForActivity;
import org.fs.todo.ToDoApplication;
import org.fs.todo.repository.TaskRepository;
import org.fs.todo.repository.TaskRepositoryImp;
import org.fs.todo.views.MainActivity;

@Module
public abstract class AppModule {

  @Binds @Singleton public abstract Application bindApplication(ToDoApplication app);
  @Binds @Singleton public abstract Context bindContext(Application app);
  @Binds @Singleton public abstract TaskRepository bindTaskRepository(TaskRepositoryImp repo);

  @ForActivity @ContributesAndroidInjector(modules = { ActivityModule.class, ProviderActivityModule.class })
  public abstract MainActivity bindMainActivity();
}
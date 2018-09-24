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

import android.arch.persistence.room.Room;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import org.fs.todo.entities.db.LocalStorage;
import org.fs.todo.entities.db.TaskDao;

@Module
public class ProviderAppModule {

  private final static String DB_NAME = "local_storage.db";

  @Provides @Singleton public LocalStorage provideLocalStorage(Context context) {
    return Room.databaseBuilder(context, LocalStorage.class, DB_NAME)
      .build();
  }

  @Provides @Singleton public TaskDao provideTaskDao(LocalStorage localStorage) {
    return localStorage.taskDao();
  }
}
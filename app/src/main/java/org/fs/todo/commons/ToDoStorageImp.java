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
package org.fs.todo.commons;

import android.content.Context;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.List;
import org.fs.core.AbstractOrmliteHelper;
import org.fs.todo.BuildConfig;
import org.fs.todo.R;
import org.fs.todo.entities.Task;
import rx.Observable;
import rx.functions.Func1;

public final class ToDoStorageImp extends AbstractOrmliteHelper implements ToDoStorage {

  private final static String DB_NAME = "todo.db";
  private final static int DB_VERSION = 1;

  private RuntimeExceptionDao<Task, Long> taskContext;

  public ToDoStorageImp(Context context) {
    super(context, DB_NAME, DB_VERSION, R.raw.ormlite_config);
  }

  @Override protected void createTables(ConnectionSource conn) throws SQLException {
    TableUtils.createTableIfNotExists(conn, Task.class);
  }

  @Override protected void dropTables(ConnectionSource conn) throws SQLException {
    TableUtils.dropTable(conn, Task.class, false);
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  @Override protected String getClassTag() {
    return ToDoStorageImp.class.getSimpleName();
  }

  @Override public Observable<List<Task>> all() {
    return Observable.just(checkIfDaoLoaded())
        .map(RuntimeExceptionDao::queryForAll);
  }

  @Override public Observable<Task> find(Func1<Task, Boolean> filter) {
    return all().flatMap(Observable::from)
        .takeFirst(filter);
  }

  @Override public Observable<Boolean> create(Task task) {
    return Observable.just(checkIfDaoLoaded())
        .map(ctx -> ctx.create(task) != 0);
  }

  @Override public Observable<Boolean> update(Task task) {
    return Observable.just(checkIfDaoLoaded())
        .map(ctx -> ctx.update(task) != 0);
  }

  @Override public Observable<Boolean> delete(Task task) {
    return Observable.just(checkIfDaoLoaded())
        .map(ctx -> ctx.delete(task) != 0);
  }

  private RuntimeExceptionDao<Task, Long> checkIfDaoLoaded() {
    if(taskContext == null) {
      taskContext = getRuntimeExceptionDao(Task.class);
    }
    return taskContext;
  }
}

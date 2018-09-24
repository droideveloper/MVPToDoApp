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
package org.fs.todo.repository;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.fs.todo.entities.Task;
import org.fs.todo.entities.db.TaskDao;

@Singleton
public class TaskRepositoryImp implements TaskRepository {

  private final TaskDao taskDao;

  @Inject public TaskRepositoryImp(TaskDao taskDao) {
    this.taskDao = taskDao;
  }

  @Override public Single<List<Task>> queryAll() {
    return taskDao.queryAll();
  }

  @Override public Single<Task> queryByTaskId(long taskId) {
    return taskDao.queryByTaskId(taskId);
  }

  @Override public Single<List<Task>> queryByTaskState(int state) {
    return taskDao.queryByTaskState(state);
  }

  @Override public Completable insert(Task task) {
    return Completable.fromAction(() -> taskDao.insert(task));
  }

  @Override public Completable update(Task task) {
    return Completable.fromAction(() -> taskDao.update(task));
  }

  @Override public Completable delete(Task task) {
    return Completable.fromAction(() -> taskDao.delete(task));
  }
}
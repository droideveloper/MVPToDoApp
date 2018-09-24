/*
 * MVP Android Copyright (C) 2018 Fatih.
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
import java.util.List;
import org.fs.todo.entities.Task;

public interface TaskRepository {

  Flowable<List<Task>> queryAll();
  Maybe<Task> queryByTaskId(long taskId);
  Flowable<List<Task>> queryByTaskState(int state);
  Completable insert(Task task);
  Completable update(Task task);
  Completable delete(Task task);
}
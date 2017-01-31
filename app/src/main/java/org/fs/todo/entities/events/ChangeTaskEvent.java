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
package org.fs.todo.entities.events;

import org.fs.common.IEvent;
import org.fs.todo.entities.Task;

public final class ChangeTaskEvent implements IEvent {

  private final boolean change;
  private final Task task;

  public ChangeTaskEvent(final boolean change, final Task task) {
    this.change = change;
    this.task = task;
  }

  public final boolean change() {
    return this.change;
  }

  public final Task task() {
    return this.task;
  }
}
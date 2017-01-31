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
package org.fs.todo.entities;

import org.fs.exception.AndroidException;

public enum TaskState {
  ACTIVE(0),
  INACTIVE(1),
  COMPLETED(2);

  private final int state;

  TaskState(final int state) {
    this.state = state;
  }

  public static int of(TaskState state) {
    return state.state;
  }

  public static TaskState of(final int state) {
    for(TaskState s : TaskState.values()) {
      if(s.state == state) {
        return s;
      }
    }
    throw new AndroidException("invalid state");
  }
}

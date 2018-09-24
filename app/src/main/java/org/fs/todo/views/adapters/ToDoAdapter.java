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
package org.fs.todo.views.adapters;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import javax.inject.Inject;
import org.fs.common.scope.ForFragment;
import org.fs.core.AbstractRecyclerAdapter;
import org.fs.todo.BuildConfig;
import org.fs.todo.entities.Task;
import org.fs.todo.views.vh.ToDoViewHolder;
import org.fs.util.ObservableList;

@ForFragment
public class ToDoAdapter extends AbstractRecyclerAdapter<Task, ToDoViewHolder> {

  @Inject
  public ToDoAdapter(ObservableList<Task> dataSet) {
    super(dataSet);
  }

  @Override public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ToDoViewHolder(parent);
  }

  @Override protected String getClassTag() {
    return ToDoAdapter.class.getSimpleName();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }
}
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.fs.core.AbstractRecyclerAdapter;
import org.fs.exception.AndroidException;
import org.fs.todo.BuildConfig;
import org.fs.todo.R;
import org.fs.todo.entities.Task;
import org.fs.todo.views.vh.ToDoViewHolder;
import org.fs.util.ObservableList;

public class ToDoAdapter extends AbstractRecyclerAdapter<Task, ToDoViewHolder> {

  public ToDoAdapter(ObservableList<Task> dataSet) {
    super(dataSet);
  }

  @Override public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    final LayoutInflater factory = LayoutInflater.from(parent.getContext());
    if(factory != null) {
      final View view = factory.inflate(R.layout.view_task_item, parent, false);
      return new ToDoViewHolder(view);
    }
    throw new AndroidException("Context is dead, no layoutInflater for death context");
  }

  @Override protected String getClassTag() {
    return ToDoAdapter.class.getSimpleName();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }
}
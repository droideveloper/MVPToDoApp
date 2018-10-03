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
package org.fs.todo.views.vh;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import org.fs.common.BusManager;
import org.fs.core.AbstractRecyclerViewHolder;
import org.fs.todo.BuildConfig;
import org.fs.todo.R;
import org.fs.todo.entities.Task;
import org.fs.todo.entities.TaskState;
import org.fs.todo.entities.events.ChangeTaskEvent;
import org.fs.todo.entities.events.RemoveTaskEvent;

public class ToDoViewHolder extends AbstractRecyclerViewHolder<Task> {

  private TextView viewTextTile;
  private View viewClose;
  private RadioButton viewCheckBox;

  private int strikeColor = Color.parseColor("#D9D9D9");
  private int defaultColor = Color.parseColor("#4D4D4D");

  public ToDoViewHolder(ViewGroup parent) {
    this(LayoutInflater.from(parent.getContext())
       .inflate(R.layout.view_task_item, parent, false));
  }

  private ToDoViewHolder(View view) {
    super(view);
    viewTextTile = view.findViewById(R.id.viewTextTitle);
    viewCheckBox = view.findViewById(R.id.viewCheckBox);
    viewClose = view.findViewById(R.id.viewClose);
  }

  @Override protected String getClassTag() {
    return ToDoViewHolder.class.getSimpleName();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  @Override public void unbind() {
    /*no-opt*/
  }

  @Override public final void bind(Task entity) {
    this.entity = entity;
    switch (entity.getTaskState()) {
      case ACTIVE: {
        bindActiveState(entity);
        break;
      }
      case INACTIVE: {
        bindInactiveState(entity);
        break;
      }
      case COMPLETED:
      default: {
        break;
      }
    }
    viewCheckBox.setOnClickListener(v -> {
      TaskState state = this.entity.getTaskState() == TaskState.INACTIVE ? TaskState.ACTIVE : TaskState.INACTIVE;
      BusManager.send(new ChangeTaskEvent(this.entity.newBuilder().state(state).build()));
    });
    viewClose.setOnClickListener((v) -> BusManager.send(new RemoveTaskEvent(entity)));
  }

  private void bindActiveState(Task task) {
    viewTextTile.setText(task.getText());
    viewTextTile.setTextColor(defaultColor);
    viewCheckBox.setChecked(false);
  }

  private void bindInactiveState(Task task) {
    SpannableString str = new SpannableString(task.getText());
    str.setSpan(new StrikethroughSpan(), 0, task.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    viewTextTile.setText(str);
    viewTextTile.setTextColor(strikeColor);
    viewCheckBox.setChecked(true); // check if this state only send by change
  }
}
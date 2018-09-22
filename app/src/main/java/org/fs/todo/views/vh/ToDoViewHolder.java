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
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import org.fs.common.BusManager;
import org.fs.core.AbstractRecyclerViewHolder;
import org.fs.todo.BuildConfig;
import org.fs.todo.R;
import org.fs.todo.entities.Task;
import org.fs.todo.entities.TaskState;
import org.fs.todo.entities.events.ChangeTaskEventType;
import org.fs.todo.entities.events.RemoveTaskEventType;

import static org.fs.util.ViewUtility.findViewById;

public class ToDoViewHolder extends AbstractRecyclerViewHolder<Task> {

  private TextView text;
  private View close;
  private RadioButton checkbox;

  private int strikeColor  = Color.parseColor("#D9D9D9");
  private int defaultColor = Color.parseColor("#4D4D4D");

  private Task data;

  public ToDoViewHolder(View view) {
    super(view);
    text = findViewById(view, R.id.text);
    close = findViewById(view, R.id.close);
    checkbox = findViewById(view, R.id.checkbox);
  }

  @Override protected String getClassTag() {
    return ToDoViewHolder.class.getSimpleName();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  @Override public void unbind() {
    /*
     * no opt
     */
  }

  @Override public final void bind(Task data) {
    this.data = data;
    switch (data.getState()) {
      case ACTIVE: {
        text.setText(data.getText());
        text.setTextColor(defaultColor);
        checkbox.setChecked(false);
        break;
      }
      case INACTIVE: {
        SpannableString str = new SpannableString(data.getText());
        str.setSpan(new StrikethroughSpan(), 0, data.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setText(str);
        text.setTextColor(strikeColor);
        checkbox.setChecked(true); // check if this state only send by change
        break;
      }
      case COMPLETED:
      default: {
        break;
      }
    }
    checkbox.setOnClickListener(v -> {
      TaskState state = this.data.getState() == TaskState.INACTIVE ? TaskState.ACTIVE : TaskState.INACTIVE;
      BusManager.send(new ChangeTaskEventType(this.data.newBuilder().state(state).build()));
    });
    close.setOnClickListener((v) -> BusManager.send(new RemoveTaskEventType(data)));
  }


}
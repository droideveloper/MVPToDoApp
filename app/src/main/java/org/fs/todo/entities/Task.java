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

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.text.TextUtils;
import java.util.Date;
import org.fs.core.AbstractEntity;
import org.fs.todo.BuildConfig;
import org.fs.util.StringUtility;

@Entity(tableName = "tasks")
public final class Task extends AbstractEntity {

  @PrimaryKey(autoGenerate = true)
  private Long taskId;
  private String text;
  private int state;
  private Date createdAt;
  private Date updatedAt;

  public Task() {/*default constructor*/}

  @Ignore private Task(Long taskId, String text, int state, Date createdAt, Date updatedAt) {
    this.taskId = taskId;
    this.text = text;
    this.state = state;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  @Ignore private Task(Parcel input) {
    super(input);
  }

  public Long getTaskId() {
    return taskId;
  }
  public String getText() {
    return text;
  }
  public TaskState getTaskState() {
    return TaskState.of(state);
  }
  public int getState() { return state; }
  public Date getCreatedAt() {
    return createdAt;
  }
  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setTaskId(Long taskId) {
    this.taskId = taskId;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setState(int state) {
    this.state = state;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override protected void readParcel(Parcel input) {
    boolean hasTaskId = input.readInt() == 1;
    if (hasTaskId) {
      taskId = input.readLong();
    }
    boolean hasText = input.readInt() == 1;
    if (hasText) {
      this.text = input.readString();
    }
    this.state = input.readInt();
    this.createdAt = new Date(input.readLong());
    this.updatedAt = new Date(input.readLong());
  }

  @Override public void writeToParcel(Parcel out, int flags) {
    boolean hasTaskId = !StringUtility.isNullOrEmpty(taskId);
    out.writeInt(hasTaskId ? 1 : 0);
    if (hasTaskId) {
      out.writeLong(taskId);
    }
    boolean hasText = !StringUtility.isNullOrEmpty(text);
    out.writeInt(hasText ? 1 : 0);
    if (hasText) {
      out.writeString(text);
    }
    out.writeInt(this.state);
    out.writeLong(this.createdAt.getTime());
    out.writeLong(this.updatedAt.getTime());
  }

  @Override public boolean equals(Object obj) {
    if (obj == null) return false;
    if (obj instanceof Task) {
      Task t = (Task) obj;
      if (t.getTaskId() != null && getTaskId() != null) {
        return t.getTaskId().equals(getTaskId());
      } else if (t.getText() != null && getText() != null) {
        return TextUtils.equals(t.getText(), getText());
      }
    }
    return false;
  }

  @Override protected String getClassTag() {
    return Task.class.getSimpleName();
  }

  @Override protected boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  @Override public int describeContents() {
    return 0;
  }

  public Builder newBuilder() {
    return new Builder()
      .taskId(taskId)
      .text(text)
      .state(TaskState.of(state))
      .createdAt(createdAt)
      .updatedAt(updatedAt);
  }

  public final static Creator<Task> CREATOR = new Creator<Task>() {

    @Override public Task createFromParcel(Parcel input) {
      return new Task(input);
    }

    @Override public Task[] newArray(int size) {
      return new Task[size];
    }
  };

  public static class Builder {

    private Long taskId;
    private String text;
    private TaskState state;
    private Date createdAt;
    private Date updatedAt;

    public Builder() { }
    public Builder taskId(Long taskId) { this.taskId = taskId; return this; }
    public Builder text(String text) { this.text = text; return this; }
    public Builder state(TaskState state) { this.state = state; return this; }
    public Builder createdAt(Date createdAt) { this.createdAt = createdAt; return this; }
    public Builder updatedAt(Date updatedAt) { this.updatedAt = updatedAt; return this; }

    public Task build() {
      return new Task(taskId, text, TaskState.of(state), createdAt, updatedAt);
    }
  }
}
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

import android.os.Parcel;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.util.Date;
import org.fs.core.AbstractEntity;
import org.fs.todo.BuildConfig;
import org.fs.util.StringUtility;

@DatabaseTable(tableName = "Tasks")
public final class Task extends AbstractEntity {

  @DatabaseField(id = true, generatedId = true)
  private long id;
  @DatabaseField(columnName = "text", dataType = DataType.LONG_STRING)
  private String text;
  @DatabaseField(columnName = "state", dataType = DataType.ENUM_INTEGER)
  private TaskState state;
  @DatabaseField(columnName = "createdAt", dataType = DataType.DATE_TIME)
  private Date createdAt;
  @DatabaseField(columnName = "updatedAt", dataType = DataType.DATE_TIME)
  private Date updatedAt;

  public Task() {/*default constructor*/}

  private Task(long id, String text, TaskState state, Date createdAt, Date updatedAt) {
    this.id = id;
    this.text = text;
    this.state = state;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  private Task(Parcel input) {
    super(input);
  }

  public long getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public TaskState getState() {
    return state;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  @Override protected void readParcel(Parcel input) {
    this.id = input.readLong();
    boolean hasText = input.readInt() == 1;
    if (hasText) {
      this.text = input.readString();
    }
    this.state = TaskState.of(input.readInt());
    this.createdAt = new Date(input.readLong());
    this.updatedAt = new Date(input.readLong());
  }

  @Override public void writeToParcel(Parcel out, int flags) {
    out.writeLong(this.id);
    boolean hasText = !StringUtility.isNullOrEmpty(text);
    out.writeInt(hasText ? 1 : 0);
    if (hasText) {
      out.writeString(text);
    }
    out.writeInt(TaskState.of(this.state));
    out.writeLong(this.createdAt.getTime());
    out.writeLong(this.updatedAt.getTime());
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
        .id(id)
        .text(text)
        .state(state)
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

    private long id;
    private String text;
    private TaskState state;
    private Date createdAt;
    private Date updatedAt;

    public Builder() { }
    public Builder id(long id) { this.id = id; return this; }
    public Builder text(String text) { this.text = text; return this; }
    public Builder state(TaskState state) { this.state = state; return this; }
    public Builder createdAt(Date createdAt) { this.createdAt = createdAt; return this; }
    public Builder updatedAt(Date updatedAt) { this.updatedAt = updatedAt; return this; }

    public Task build() {
      return new Task(id, text, state, createdAt, updatedAt);
    }
  }
}
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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;
import org.fs.todo.BuildConfig;
import org.fs.todo.entities.Task;
import org.fs.todo.entities.TaskState;
import org.fs.todo.views.TaskStateFragment;

public class StateToDoAdapter extends FragmentPagerAdapter {

  private Map<TaskState, List<Task>> dataSet;

  public StateToDoAdapter(FragmentManager fm, List<Task> data) {
    super(fm);
    dataSet = new HashMap<>();
    StreamSupport.stream(Arrays.asList(TaskState.ACTIVE, TaskState.INACTIVE, TaskState.COMPLETED))
        .forEach(state -> dataSet.put(state, StreamSupport.stream(data)
                                                          .filter(entry -> entry.getState() == state)
                                                          .collect(Collectors.toList())));
  }

  @Override public Fragment getItem(int position) {
    if(dataSet != null) {
      int counter = 0;
      for (Map.Entry<TaskState, List<Task>> entry: dataSet.entrySet()) {
        if(counter == position) {
          return TaskStateFragment.newInstance(entry.getValue());
        }
      }
    }
    return null;
  }

  @Override public CharSequence getPageTitle(int position) {
    if(dataSet != null) {
      int counter = 0;
      for (Map.Entry<TaskState, List<Task>> entry : dataSet.entrySet()) {
        if (position == counter) {
          return String.valueOf(entry.getKey());
        }
        counter++;
      }
    }
    return super.getPageTitle(position);
  }

  @Override public int getCount() {
    return dataSet != null ? dataSet.size() : 0;
  }

  private void log(String msg) {
    log(Log.DEBUG, msg);
  }

  private void log(Throwable exp) {
    StringWriter strWriter = new StringWriter(128);
    PrintWriter ptrWriter = new PrintWriter(strWriter);
    exp.printStackTrace(ptrWriter);
    log(Log.ERROR, strWriter.toString());
  }

  private void log(int lv, String msg) {
    if (isLogEnabled()) {
      Log.println(lv, getClassTag(), msg);
    }
  }

  private boolean isLogEnabled() {
    return BuildConfig.DEBUG;
  }

  private String getClassTag() {
    return StateToDoAdapter.class.getSimpleName();
  }
}

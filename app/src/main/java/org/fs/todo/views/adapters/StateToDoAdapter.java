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
import android.support.v4.util.Pair;
import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import javax.inject.Inject;
import org.fs.common.scope.ForActivity;
import org.fs.todo.BuildConfig;
import org.fs.todo.views.TaskListFragment;

@ForActivity
public class StateToDoAdapter extends FragmentPagerAdapter {

  private List<Pair<Integer, String>> dataSet;

  @Inject
  public StateToDoAdapter(FragmentManager fm, List<Pair<Integer, String>> dataSet) {
    super(fm);
    this.dataSet = dataSet;
  }

  @Override public Fragment getItem(int position) {
    if(dataSet != null) {
      final Pair<Integer, String> item = dataSet.get(position);
      if (item != null && item.first != null) {
        return TaskListFragment.newInstance(item.first);
      }
    }
    return null;
  }

  @Override public CharSequence getPageTitle(int position) {
    if(dataSet != null) {
      return dataSet.get(position).second;
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

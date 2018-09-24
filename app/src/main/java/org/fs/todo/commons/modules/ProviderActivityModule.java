/*
 * To-Do Copyright (C) 2018 Fatih.
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
package org.fs.todo.commons.modules;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;
import java.util.List;
import org.fs.common.scope.ForActivity;
import org.fs.todo.R;
import org.fs.todo.entities.DisplayOptions;
import org.fs.todo.views.MainActivityView;

@Module
public class ProviderActivityModule {

  @Provides @ForActivity public List<Pair<Integer, String>> provideTaskStateDataSet(Context context) {
    final List<Pair<Integer, String>> dataSet =  new ArrayList<>();
    dataSet.add(Pair.create(DisplayOptions.ALL, context.getString(R.string.str_all_title)));
    dataSet.add(Pair.create(DisplayOptions.ACTIVE, context.getString(R.string.str_active_title)));
    dataSet.add(Pair.create(DisplayOptions.INACTIVE, context.getString(R.string.str_inactive_title)));
    return dataSet;
  }

  @Provides @ForActivity public FragmentManager provideFragmentManager(MainActivityView view) {
    return view.provideSupportFragmentManager();
  }
}
/*
 * MVP Android Copyright (C) 2018 Fatih.
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
package org.fs.todo.commons.components;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;
import org.fs.todo.ToDoApplication;
import org.fs.todo.commons.modules.AppModule;
import org.fs.todo.commons.modules.ProviderAppModule;

@Singleton
@Component(modules = { AndroidSupportInjectionModule.class, AppModule.class, ProviderAppModule.class })
public interface AppComponent extends AndroidInjector<ToDoApplication> {

  @Component.Builder
  abstract class Builder extends AndroidInjector.Builder<ToDoApplication> { }
}
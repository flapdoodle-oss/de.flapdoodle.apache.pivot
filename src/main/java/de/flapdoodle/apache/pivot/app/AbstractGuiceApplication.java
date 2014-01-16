/**
 * Copyright (C) 2014
 *   Michael Mosmann <michael@mosmann.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.flapdoodle.apache.pivot.app;

import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Display;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import de.flapdoodle.apache.pivot.components.Applications;

public abstract class AbstractGuiceApplication implements Application {

	private Injector injector;

	public AbstractGuiceApplication(Module... modules) {
		injector = Guice.createInjector(modules);
		injector.injectMembers(this);
	}

	@Override
	public void startup(Display display, Map<String, String> properties) throws Exception {
		Applications.set(this, display);
	}
}

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
package de.flapdoodle.apache.pivot.demo;

import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Display;

import com.google.inject.Inject;

import de.flapdoodle.apache.pivot.app.AbstractGuiceApplication;

public class DemoApp extends AbstractGuiceApplication {

	@Inject
	DemoWindow _demoWindow;

	@Override
	public void startup(Display display, Map<String, String> properties) throws Exception {
		super.startup(display, properties);
		_demoWindow.open(display);
	}

	@Override
	public boolean shutdown(boolean optional) throws Exception {
		_demoWindow.close();
		return false;
	}

	@Override
	public void suspend() throws Exception {

	}

	@Override
	public void resume() throws Exception {

	}

	public static void main(String[] args) {
		DesktopApplicationContext.main(DemoApp.class, args);
	}

}

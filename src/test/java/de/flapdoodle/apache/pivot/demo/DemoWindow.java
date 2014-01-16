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

import org.apache.pivot.wtk.FillPane;
import org.apache.pivot.wtk.Panel;

import de.flapdoodle.apache.pivot.components.MainWindow;


public class DemoWindow extends MainWindow {

	
	public DemoWindow() {
		getStyles().put("backgroundColor", "#808080");
		Panel content = new Panel();
		content.getStyles().put("backgroundColor", "#f0f0f0");
		content.add(new ColorPanel());
		content.add(new ColorPanel());
		content.add(new ColorPanel());
		content.add(new ColorPanel());
		content.add(new ColorPanel());
		setContent(content);
	}
}

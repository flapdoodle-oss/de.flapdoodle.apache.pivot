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
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.Panel;

import de.flapdoodle.apache.pivot.components.MainWindow;
import de.flapdoodle.apache.pivot.layout.WeightPane;


public class DemoWindow extends MainWindow {

	
	public DemoWindow() {
		getStyles().put("backgroundColor", "#808080");
		Orientation main=Orientation.HORIZONTAL;
		WeightPane content = new WeightPane(main);
		content.setName("main");
		content.getStyles().put("backgroundColor", "#f0f0f0");
		content.add(new ColorPanel(main));
		content.add(new ColorPanel(main));
		content.add(new ColorPanel(main));
		content.add(new ColorPanel(main));
		Orientation subOrient=Orientation.VERTICAL;
		WeightPane sub = new WeightPane(subOrient);
		sub.getStyles().put("backgroundColor", "#fff0f0");
		WeightPane.setWeight(sub, 4);
		sub.setName("sub");
		sub.add(new ColorPanel(subOrient));
		sub.add(new ColorPanel(subOrient));
		sub.add(new ColorPanel(subOrient));
		sub.add(new ColorPanel(subOrient));
		sub.add(new ColorPanel(subOrient));
		content.add(sub);
		setContent(content);
	}
}

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
package de.flapdoodle.apache.pivot.components;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentListener;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;

public class MainWindow extends Window {

	Display display = null;
	private SizeListener listener;

	@Override
	public void open(Display display, Window ownerArgument) {
		super.open(display, ownerArgument);

		this.display = display;
		setPreferredSize(display.getWidth(), display.getHeight());

		this.listener = new SizeListener();
		display.getComponentListeners().add(listener);
	}

	@Override
	public void close() {
		if (display != null) {
			display.getComponentListeners().remove(listener);
			listener = null;
			display = null;
		}
		super.close();
	}

	private final class SizeListener extends ComponentListener.Adapter {

		@Override
		public void sizeChanged(Component component, int previousWidth, int previousHeight) {
			super.sizeChanged(component, previousWidth, previousHeight);

			MainWindow.this.setPreferredSize(component.getWidth(), component.getHeight());
		}
	}

}

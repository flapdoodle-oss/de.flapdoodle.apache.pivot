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
package de.flapdoodle.apache.pivot.visitor;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.ApplicationContext;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Panel;
import org.apache.pivot.wtk.PushButton;
import org.apache.pivot.wtk.Window;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.flapdoodle.apache.pivot.components.Applications;

public class VisitorsTest {

	@Test
	public void visitAllParents() {
		Window w = new Window();
		PushButton button = new PushButton();
		w.add(button);

		CollectAllElementsVisitor<Object> visitor = new CollectAllElementsVisitor<Object>();
		Visitors.parents(button, Object.class).traverse(visitor);
		ImmutableList<Object> visitedElements = visitor.visitedElements();

		assertEquals(2, visitedElements.size());
		assertEquals(button, visitedElements.get(0));
		assertEquals(w, visitedElements.get(1));
	}

	@Test
	public void visitAllChildren() {
		Window w = new Window();
		PushButton button = new PushButton();
		w.add(button);
		Panel panel = new Panel();
		w.add(panel);
		PushButton button2 = new PushButton();
		panel.add(button2);

		CollectAllElementsVisitor<Object> visitor = new CollectAllElementsVisitor<Object>();
		Visitors.children(w, Object.class).traverse(visitor);
		ImmutableList<Object> visitedElements = visitor.visitedElements();

		assertEquals(4, visitedElements.size());
		assertEquals(w, visitedElements.get(0));
		assertEquals(button, visitedElements.get(1));
		assertEquals(panel, visitedElements.get(2));
		assertEquals(button2, visitedElements.get(3));
	}

	@Test
	public void visitAll() {
		Window w = new Window();
		PushButton button = new PushButton();
		w.add(button);
		Panel panel = new Panel();
		w.add(panel);
		PushButton button2 = new PushButton();
		panel.add(button2);

		CollectAllElementsVisitor<Object> visitor = new CollectAllElementsVisitor<Object>();
		Visitors.all(button2, Object.class).traverse(visitor);
		ImmutableList<Object> visitedElements = visitor.visitedElements();

		assertEquals(4, visitedElements.size());
		assertEquals(w, visitedElements.get(0));
		assertEquals(button, visitedElements.get(1));
		assertEquals(panel, visitedElements.get(2));
		assertEquals(button2, visitedElements.get(3));
	}

	@Test
	public void visitDisplayAndApplication() {
		Display display = new Display(new ApplicationContext.DisplayHost());
		Application.Adapter application = new Application.Adapter();
		Applications.set(application, display);

		CollectAllElementsVisitor<Object> visitor = new CollectAllElementsVisitor<Object>();
		Visitors.all(display, Object.class).traverse(visitor);
		ImmutableList<Object> visitedElements = visitor.visitedElements();

		assertEquals(2, visitedElements.size());
		assertEquals(application, visitedElements.get(0));
		assertEquals(display, visitedElements.get(1));
	}

	@Test
	public void stopTraversal() {
		Window w = new Window();
		PushButton button = new PushButton();
		w.add(button);
		Panel panel = new Panel();
		w.add(panel);
		PushButton button2 = new PushButton();
		panel.add(button2);

		CollectAllElementsVisitor<Object> visitor = new StoppingVisitor<Object>(Panel.class);
		Visitors.all(button2, Object.class).traverse(visitor);
		ImmutableList<Object> visitedElements = visitor.visitedElements();

		assertEquals(3, visitedElements.size());
		assertEquals(w, visitedElements.get(0));
		assertEquals(button, visitedElements.get(1));
		assertEquals(panel, visitedElements.get(2));
	}

	@Test
	public void abortTraversal() {
		Window w = new Window();
		Panel panel = new Panel();
		PushButton button2 = new PushButton();
		panel.add(button2);
		w.add(panel);
		PushButton button = new PushButton();
		w.add(button);

		CollectAllElementsVisitor<Object> visitor = new AbortingVisitor(Panel.class);
		Visitors.all(button2, Object.class).traverse(visitor);
		ImmutableList<Object> visitedElements = visitor.visitedElements();

		assertEquals(2, visitedElements.size());
		assertEquals(w, visitedElements.get(0));
		assertEquals(panel, visitedElements.get(1));
	}

	private class CollectAllElementsVisitor<T> implements IVisitor<T> {

		List<Object> visitedElements = Lists.newArrayList();

		@Override
		public void visit(IVisit visit, T element) {
			visitedElements.add(element);
		}

		public ImmutableList<Object> visitedElements() {
			return ImmutableList.copyOf(visitedElements);
		}
	}

	private class AbortingVisitor<T> extends CollectAllElementsVisitor<T> {

		private final Class<? extends T> abortType;

		public AbortingVisitor(Class<? extends T> abortType) {
			this.abortType = abortType;
		}

		public void visit(IVisit visit, T element) {
			super.visit(visit, element);
			if (abortType.isInstance(element)) {
				visit.abort();
			}
		};
	}

	private class StoppingVisitor<T> extends CollectAllElementsVisitor<T> {

		private final Class<? extends T> abortType;

		public StoppingVisitor(Class<? extends T> abortType) {
			this.abortType = abortType;
		}

		public void visit(IVisit visit, T element) {
			super.visit(visit, element);
			if (abortType.isInstance(element)) {
				visit.stop();
			}
		};
	}
}

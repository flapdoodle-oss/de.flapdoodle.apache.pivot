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
package de.flapdoodle.apache.pivot.events;

import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Container;
import org.apache.pivot.wtk.Display;

import de.flapdoodle.apache.pivot.components.Applications;
import de.flapdoodle.apache.pivot.visitor.ITraversal;
import de.flapdoodle.apache.pivot.visitor.IVisit;
import de.flapdoodle.apache.pivot.visitor.IVisitor;
import de.flapdoodle.apache.pivot.visitor.Visitors;

public abstract class Events {

	private Events() {
		// no instance
	}

	public static IEventSink sink(Container container) {
		return new ContainerSink(container);
	}

	public static IEventSink sink(Component component) {
		return new ComponentSink(component);
	}

	static class ComponentSink implements IEventSink {

		private final Component component;

		public ComponentSink(Component component) {
			this.component = component;
		}

		@Override
		public void send(Event event, EventPropagation propagation) {
			final ITraversal<IEventListener> traversal;
			switch (propagation) {
				case BubbleUp:
					traversal = Visitors.parents(component, IEventListener.class);
					break;
				case All:
					traversal = Visitors.all(component, IEventListener.class);
					break;
				case AllChilds:
				default:
					throw new IllegalArgumentException("" + propagation + " not possible for Component like " + component);
			}
			traversal.traverse(new EventListenerVisitor(event));

			/*
			 * sendTo(component, event); switch (propagation) { case BubbleUp:
			 * sendBubbleUp(component, event); break; case All:
			 * sendToAll(component.getParent(), component, event); break; case
			 * AllChilds: throw new IllegalArgumentException(""+propagation+
			 * " not possible for Component like "+component); }
			 */
		}

	}

	static class ContainerSink implements IEventSink {

		private final Container container;

		public ContainerSink(Container container) {
			this.container = container;
		}

		@Override
		public void send(Event event, EventPropagation propagation) {
			final ITraversal<IEventListener> traversal;
			switch (propagation) {
				case BubbleUp:
					traversal = Visitors.parents(container, IEventListener.class);
					break;
				case All:
					traversal = Visitors.all(container, IEventListener.class);
					break;
				case AllChilds:
				default:
					traversal = Visitors.children(container, IEventListener.class);
					break;
			}
			traversal.traverse(new EventListenerVisitor(event));
			/*
			 * sendTo(container, event);
			 * switch (propagation) {
			 * case AllChilds:
			 * sendToAllChilds(container, event);
			 * break;
			 * case All:
			 * sendToAll(container, event);
			 * break;
			 * case BubbleUp:
			 * sendBubbleUp(container, event);
			 * break;
			 * }
			 */
		}

	}

	private static void sendToAll(Container container, Event event) {
		sendToAll(container, null, event);
	}

	private static void sendToAll(Container container, Component excludeChild, Event event) {

	}

	private static void sendBubbleUp(Component container, Event event) {
		Container parent = container.getParent();
		if (parent != null) {
			sendTo(parent, event);
			sendBubbleUp(parent, event);
		}
		if (container instanceof Display) {
			Application app = Applications.get((Display) container);
			sendTo(app, event);
		}
	}

	private static void sendToAllChilds(Container container, Event event) {
		sendToAllChilds(container, null, event);
	}

	private static void sendToAllChilds(Container container, Component excludeChild, Event event) {
		for (Component component : container) {
			sendTo(component, event);
			if (component instanceof Container) {
				sendToAllChilds((Container) component, event);
			}
		}
	}

	private static void sendTo(Object reciver, Event event) {
		if (reciver instanceof IEventListener) {
			IEventListener listener = (IEventListener) reciver;
			listener.onEvent(event);
		}
	}

	private static final class EventListenerVisitor implements IVisitor<IEventListener> {

		private final Event event;

		public EventListenerVisitor(Event event) {
			this.event = event;
		}

		@Override
		public void visit(IVisit visit, IEventListener listener) {
			listener.onEvent(event);
		}
	}
}

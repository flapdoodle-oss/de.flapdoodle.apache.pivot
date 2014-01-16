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

import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Container;
import org.apache.pivot.wtk.Display;

import de.flapdoodle.apache.pivot.components.Applications;
import de.flapdoodle.apache.pivot.visitor.Visitors.Visit.State;

public abstract class Visitors {

	private Visitors() {
		// no instance
	}

	public static <T> ITraversal<T> parents(Component start, Class<T> type) {
		return new VisitParents<T>(start, type);
	}

	public static <T> ITraversal<T> children(Container start, Class<T> type) {
		return new VisitChildren<T>(start, type);
	}

	public static <T> ITraversal<T> all(Component start, Class<T> type) {
		return new VisitAll<T>(start, type);
	}

	public static Container root(Container start) {
		if (start != null) {
			Container cur = start;
			while (true) {
				Container parent = cur.getParent();
				if (parent != null) {
					cur = parent;
				} else {
					return cur;
				}
			}
		}
		return null;
	}

	public static Container rootOfComponent(Component start) {
		if (start != null) {
			Container root = root(start.getParent());
			if ((root == null) && (start instanceof Container)) {
				return (Container) start;
			}
			return root;
		}
		return null;
	}

	static abstract class AbstractTraversal<T, C extends Component> {

		private final C start;
		private final Class<T> type;

		public AbstractTraversal(C start, Class<T> type) {
			this.start = start;
			this.type = type;
		}

		protected C start() {
			return start;
		}

		protected void traverseChildren(Visit parentVisit, Container current, IVisitor<T> visitor) {
			Visit visit = new Visit();
			visitIfTypeMatch(visit, current, visitor, type);
			if (visit.state() == State.Running) {
				for (Component c : current) {
					if (c instanceof Container) {
						traverseChildren(visit, (Container) c, visitor);
					} else {
						visitIfTypeMatch(visit, c, visitor, type);
					}

					if (visit.state() == State.Aborted) {
						parentVisit.abort();
						return;
					}
				}
			} else {
				if (visit.state() == State.Aborted) {
					parentVisit.abort();
				}
			}
		}

		protected void traverseParents(Visit visit, Component current, IVisitor<T> visitor) {
			while (current != null) {
				visitIfTypeMatch(visit, current, visitor, type);
				if (visit.state() == State.Running) {
					visitApplication(visit, current, visitor);
					current = current.getParent();
				}
			}
		}

		private void visitApplication(Visit visit, Component current, IVisitor<T> visitor) {
			if (current instanceof Display) {
				// display has no parent
				Application application = Applications.get((Display) current);
				visitIfTypeMatch(visit, application, visitor, type);
			}
		}

		protected void traverseAll(Visit visit, Component start, IVisitor<T> visitor) {
			Container root = rootOfComponent(start);
			visitApplication(visit, root, visitor);
			if (visit.state() == State.Running) {
				traverseChildren(visit, root, visitor);
			}
		}
	}

	static class VisitAll<T> extends AbstractTraversal<T, Component> implements ITraversal<T> {

		public VisitAll(Component start, Class<T> type) {
			super(start, type);
		}

		@Override
		public void traverse(IVisitor<T> visitor) {
			traverseAll(new Visit(), start(), visitor);
		}
	}

	static class VisitChildren<T> extends AbstractTraversal<T, Container> implements ITraversal<T> {

		public VisitChildren(Container start, Class<T> type) {
			super(start, type);
		}

		@Override
		public void traverse(IVisitor<T> visitor) {
			traverseChildren(new Visit(), start(), visitor);
		}

	}

	static class VisitParents<T> extends AbstractTraversal<T, Component> implements ITraversal<T> {

		public VisitParents(Component start, Class<T> type) {
			super(start, type);
		}

		@Override
		public void traverse(IVisitor<T> visitor) {
			traverseParents(new Visit(), start(), visitor);
		}

	}

	private static <T> void visitIfTypeMatch(Visit visit, Object current, IVisitor<T> visitor, Class<T> type) {
		if (type.isInstance(current)) {
			visitor.visit(visit, (T) current);
		}
	}

	static class Visit implements IVisit {

		enum State {
			Running,
			Stopped,
			Aborted;
		}

		private State state = State.Running;

		@Override
		public void stop() {
			this.state = State.Stopped;
		}

		@Override
		public void abort() {
			this.state = State.Aborted;
		}

		public State state() {
			return state;
		}
	}
}

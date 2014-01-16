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
package de.flapdoodle.apache.pivot.containers;

import org.apache.pivot.wtk.Component;

import com.google.common.base.Optional;

import de.flapdoodle.apache.pivot.visitor.IVisit;
import de.flapdoodle.apache.pivot.visitor.IVisitor;
import de.flapdoodle.apache.pivot.visitor.Visitors;

public abstract class Containers {

	private Containers() {
		// no instance
	}

	public static <T> ParentMatcher<T> parent(Class<T> containerType) {
		return new ParentMatcher<T>(containerType);
	}

	public static final class ParentMatcher<T> {

		private final Class<T> containerType;

		public ParentMatcher(Class<T> containerType) {
			this.containerType = containerType;
		}

		public Optional<T> of(Component start) {
			return findParent(start, containerType);
		}
	}

	private static <T> Optional<T> findParent(Component start, Class<T> containerType) {
		FirstMatchVisitor<T> visitor = new FirstMatchVisitor<T>();
		Visitors.parents(start, containerType).traverse(visitor);
		return visitor.match();
	}

	private static final class FirstMatchVisitor<T> implements IVisitor<T> {

		Optional<T> match = Optional.absent();

		public void visit(IVisit visit, T element) {
			match = Optional.of(element);
			visit.abort();
		}

		public Optional<T> match() {
			return match;
		}
	}

}

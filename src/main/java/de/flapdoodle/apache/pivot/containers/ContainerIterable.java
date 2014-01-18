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

import java.util.Iterator;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Container;

final class ContainerIterable implements Iterable<Component> {

	private final Container _container;

	public ContainerIterable(Container container) {
		_container = container;
	}

	@Override
	public Iterator<Component> iterator() {
		return new ContainerIterable.ContainerIterator(_container);
	}

	private static final class ContainerIterator implements Iterator<Component> {
		
		private final Container _container;
		int _index = 0;
	
		public ContainerIterator(Container container) {
			_container = container;
		}
	
		@Override
		public boolean hasNext() {
			return _container.getLength() > _index;
		}
	
		@Override
		public Component next() {
			Component ret = _container.get(_index);
			_index++;
			return ret;
		}
	
		@Override
		public void remove() {
			throw new IllegalAccessError("remove not supported");
		}
	
	}
}
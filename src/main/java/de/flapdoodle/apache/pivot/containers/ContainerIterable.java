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
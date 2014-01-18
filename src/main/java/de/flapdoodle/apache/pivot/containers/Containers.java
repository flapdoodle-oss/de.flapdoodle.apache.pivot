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

import java.util.Collection;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Container;
import org.apache.pivot.wtk.Limits;
import org.apache.pivot.wtk.Orientation;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

import de.flapdoodle.apache.pivot.components.Components;
import de.flapdoodle.guava.Foldleft;
import de.flapdoodle.guava.Folds;

public abstract class Containers {

	private Containers() {
		// no instance
	}

	public static <T> ParentMatcher<T> parent(Class<T> containerType) {
		return new ParentMatcher<T>(containerType);
	}

	public static Iterable<Component> components(Container container) {
		return new ContainerIterable(container);
	}

	public static ImmutableList<Component> componentsAsList(Container container) {
		return ImmutableList.copyOf(components(container));
	}
	
	public static int min(Collection<Component> components, Orientation orientation) {
		return Folds.foldLeft(components, new ComponentSizeFold(Components.limits(orientation),new LimitMin()), 0);
	}

	public static int max(Collection<Component> components, Orientation orientation) {
		return Folds.foldLeft(components, new ComponentSizeFold(Components.limits(orientation),new LimitMax()), 0);
	}

	private static final class ComponentSizeFold<A> implements Foldleft<Component, Integer> {

		private final Function<Component, A> _attributeTransformation;
		private final Function<A, Integer> _attributeValueTransformation;

		public ComponentSizeFold(Function<Component, A> attributeTransformation, Function<A, Integer> attributeValueTransformation) {
			_attributeTransformation = attributeTransformation;
			_attributeValueTransformation = attributeValueTransformation;
		}
		
		@Override
		public Integer apply(Integer left, Component right) {
			return left+_attributeValueTransformation.apply(_attributeTransformation.apply(right));
		}
	}

	private static final class LimitMin implements Function<Limits, Integer> {

		@Override
		public Integer apply(Limits input) {
			return input.minimum;
		}
		
	}

	private static final class LimitMax implements Function<Limits, Integer> {

		@Override
		public Integer apply(Limits input) {
			return input.maximum;
		}
		
	}
}

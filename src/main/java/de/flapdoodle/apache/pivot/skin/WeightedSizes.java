package de.flapdoodle.apache.pivot.skin;

import java.util.Collection;
import java.util.List;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Limits;
import org.apache.pivot.wtk.Orientation;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;

import de.flapdoodle.apache.pivot.components.Components;
import de.flapdoodle.apache.pivot.layout.WeightPane;
import de.flapdoodle.guava.Foldleft;
import de.flapdoodle.guava.Folds;

abstract class WeightedSizes {

	public WeightedSizes() {
		// no instance
	}

	static ImmutableList<ComponentSizeAndWeight<Component>> componentSizes(Iterable<Component> components,
			Orientation orientation) {
		return transform(Lists.newArrayList(components), new SizeAndWeight(orientation));
	}

	static <T> ImmutableList<ComponentAndSize<T>> resize(ImmutableList<ComponentSizeAndWeight<T>> sizes, int newSize) {
		ImmutableList<ComponentAndSize<T>> minSizes = min(sizes);
		ImmutableList<ComponentAndSize<T>> maxSizes = max(sizes);

		int min = sum(minSizes);
		int max = sum(maxSizes);
		if (newSize <= min)
			return minSizes;
		if (newSize >= max)
			return maxSizes;

		return resized(sizes, newSize);
	}

	private static <T> int sum(ImmutableList<ComponentAndSize<T>> sizes) {
		return Folds.foldLeft(sizes, new Foldleft<ComponentAndSize<T>, Integer>() {

			@Override
			public Integer apply(Integer left, ComponentAndSize<T> right) {
				return left + right.size();
			}
		}, 0);
	}

	private static <T> ImmutableList<ComponentAndSize<T>> min(ImmutableList<? extends ComponentAndLimits<T>> sizes) {
		return transform(sizes, new Function<ComponentAndLimits<T>, ComponentAndSize<T>>() {

			@Override
			public ComponentAndSize<T> apply(ComponentAndLimits<T> input) {
				return new ComponentAndSize<T>(input.component(), input.limits(), input.limits().minimum);
			}
		});
	}

	private static <T> ImmutableList<ComponentAndSize<T>> max(ImmutableList<? extends ComponentAndLimits<T>> sizes) {
		return transform(sizes, new Function<ComponentAndLimits<T>, ComponentAndSize<T>>() {

			@Override
			public ComponentAndSize<T> apply(ComponentAndLimits<T> input) {
				return new ComponentAndSize<T>(input.component(), input.limits(), input.limits().maximum);
			}
		});
	}

	private static <T> ImmutableList<ComponentAndSize<T>> resized(ImmutableList<ComponentSizeAndWeight<T>> sizes, int sizeOfAll) {

		int weightOfAll = weights(sizes);
		ImmutableList<ComponentAndSize<T>> weighted = weighted(sizes, sizeOfAll, weightOfAll);
		ImmutableList<ComponentAndSize<T>> toBigOrSmall = filter(weighted, new Predicate<ComponentAndSize<T>>() {

			@Override
			public boolean apply(ComponentAndSize<T> input) {
				return (input.size() > input.limits().maximum) || (input.size() < input.limits().minimum);
			}
		});

		if (!toBigOrSmall.isEmpty()) {
			final List<T> componentsToBig = transform(toBigOrSmall, new Function<ComponentAndSize<T>, T>() {

				@Override
				public T apply(ComponentAndSize<T> input) {
					return input.component();
				}
			});
			ImmutableList<ComponentSizeAndWeight<T>> left = filter(sizes, new Predicate<ComponentSizeAndWeight<T>>() {

				@Override
				public boolean apply(ComponentSizeAndWeight<T> input) {
					return !componentsToBig.contains(input.component());
				}
			});

			ImmutableList<ComponentAndSize<T>> maxOfToBig = sizeFixedToLimit(toBigOrSmall);
			int sizeOfToBig = sum(maxOfToBig);

			Builder<ComponentAndSize<T>> builder = ImmutableList.<ComponentAndSize<T>> builder();
			builder.addAll(maxOfToBig);
			builder.addAll(resized(left, sizeOfAll - sizeOfToBig));
			return builder.build();
		}
		return weighted;
	}
	
	private static <T> ImmutableList<ComponentAndSize<T>> sizeFixedToLimit(ImmutableList<ComponentAndSize<T>> source) {
		return transform(source, new Function<ComponentAndSize<T>, ComponentAndSize<T>>() {
			@Override
			public ComponentAndSize<T> apply(ComponentAndSize<T> input) {
				int size=input.size();
				if (size<input.limits().minimum) size=input.limits().minimum;
				if (size>input.limits().maximum) size=input.limits().maximum;
				return new ComponentAndSize<T>(input.component(), input.limits(), size);
			}
		});
	}

	private static <T> ImmutableList<ComponentAndSize<T>> weighted(ImmutableList<ComponentSizeAndWeight<T>> sizes, int sizeOfAll,
			final int weightOfAll) {
		Builder<ComponentAndSize<T>> builder = ImmutableList.<ComponentAndSize<T>> builder();
		int sizeLeft = sizeOfAll;
		int weigtLeft = weightOfAll;
		for (ComponentSizeAndWeight<T> s : sizes) {
			Preconditions.checkArgument(s.weight()>0, "weight is <= 0");
			
			int wishedSize = (sizeLeft * s.weight()) / weigtLeft;
			builder.add(new ComponentAndSize<T>(s.component(), s.limits(), wishedSize));
			sizeLeft = sizeLeft - wishedSize;
			weigtLeft = weigtLeft - s.weight();
		}
		return builder.build();
	}

	private static <T> int weights(ImmutableList<ComponentSizeAndWeight<T>> sizes) {
		return Folds.foldLeft(sizes, new Foldleft<ComponentSizeAndWeight<T>, Integer>() {

			@Override
			public Integer apply(Integer left, ComponentSizeAndWeight<T> right) {
				return left + right.weight();
			}
		}, 0);
	}

	private static <S, D> ImmutableList<D> transform(List<? extends S> collection, Function<S, D> transformation) {
		return ImmutableList.copyOf(Lists.transform(collection, transformation));
	}

	private static <T> ImmutableList<T> filter(Collection<T> collection, Predicate<T> predicate) {
		return ImmutableList.copyOf(Collections2.filter(collection, predicate));
	}

	private static final class SizeAndWeight implements Function<Component, ComponentSizeAndWeight<Component>> {

		private final Orientation _orientation;
		private Function<Component, Limits> _limitsFunction;

		public SizeAndWeight(Orientation orientation) {
			_orientation = orientation;
			_limitsFunction = Components.limits(orientation);
		}

		@Override
		public ComponentSizeAndWeight<Component> apply(Component component) {
			return new ComponentSizeAndWeight<Component>(component, _limitsFunction.apply(component), weight(component));
		}

		private int weight(Component component) {
			return WeightPane.getWeight(component);
		}
	}

	interface ComponentAndLimits<T> {

		Limits limits();

		T component();

	}

	static class ComponentSizeAndWeight<T> implements ComponentAndLimits<T> {

		private final T _component;
		private final Limits _limits;
		private final int _weight;

		public ComponentSizeAndWeight(T component, Limits limits, int weight) {
			_component = component;
			_limits = limits;
			_weight = weight;
		}

		@Override
		public Limits limits() {
			return _limits;
		}

		public int weight() {
			return _weight;
		}

		@Override
		public T component() {
			return _component;
		}
	}

	static class ComponentAndSize<T> implements ComponentAndLimits<T> {

		private final T _component;
		private final int _size;
		private final Limits _limits;

		public ComponentAndSize(T component, Limits limits, int size) {
			_component = component;
			_limits = limits;
			_size = size;
		}

		public int size() {
			return _size;
		}

		public T component() {
			return _component;
		}

		@Override
		public Limits limits() {
			return _limits;
		}

	}

}

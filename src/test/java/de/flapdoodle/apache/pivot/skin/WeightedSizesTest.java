package de.flapdoodle.apache.pivot.skin;

import static org.junit.Assert.*;

import org.apache.pivot.wtk.Limits;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;

import de.flapdoodle.apache.pivot.skin.WeightedSizes.ComponentAndSize;
import de.flapdoodle.apache.pivot.skin.WeightedSizes.ComponentSizeAndWeight;
import de.flapdoodle.guava.Transformations;
import de.flapdoodle.guava.Types;


public class WeightedSizesTest {

	@Test
	public void smallerThanMinimumReturnMin() {
		ImmutableList<ComponentSizeAndWeight<String>> sizes=ImmutableList.<ComponentSizeAndWeight<String>>builder()
				.add(entry("a",limits(10,100),1))
				.add(entry("b",limits(20,90),1))
				.add(entry("c",limits(30,80),1))
				.build();
		ImmutableList<ComponentAndSize<String>> resized = WeightedSizes.resize(sizes, 3);
		
		assertEquals(3, resized.size());
		assertEquals(10, withLabel("a", resized).size());
		assertEquals(20, withLabel("b", resized).size());
		assertEquals(30, withLabel("c", resized).size());
	}
	
	@Test
	public void biggerThanMaximumReturnsMax() {
		ImmutableList<ComponentSizeAndWeight<String>> sizes=ImmutableList.<ComponentSizeAndWeight<String>>builder()
				.add(entry("a",limits(10,100),1))
				.add(entry("b",limits(20,90),1))
				.add(entry("c",limits(30,80),1))
				.build();
		ImmutableList<ComponentAndSize<String>> resized = WeightedSizes.resize(sizes, 400);
		
		assertEquals(3, resized.size());
		assertEquals(100, withLabel("a", resized).size());
		assertEquals(90, withLabel("b", resized).size());
		assertEquals(80, withLabel("c", resized).size());
	}

	@Test
	public void relaxedEqualSized() {
		ImmutableList<ComponentSizeAndWeight<String>> sizes=ImmutableList.<ComponentSizeAndWeight<String>>builder()
				.add(entry("a",limits(10,100),1))
				.add(entry("b",limits(20,90),1))
				.add(entry("c",limits(30,80),1))
				.build();
		ImmutableList<ComponentAndSize<String>> resized = WeightedSizes.resize(sizes, 150);
		
		assertEquals(3, resized.size());
		assertEquals(50, withLabel("a", resized).size());
		assertEquals(50, withLabel("b", resized).size());
		assertEquals(50, withLabel("c", resized).size());
	}

	@Test
	public void littleBiggerTriggersMax() {
		ImmutableList<ComponentSizeAndWeight<String>> sizes=ImmutableList.<ComponentSizeAndWeight<String>>builder()
				.add(entry("a",limits(10,100),1))
				.add(entry("b",limits(20,100),1))
				.add(entry("c",limits(30,50),1))
				.build();
		ImmutableList<ComponentAndSize<String>> resized = WeightedSizes.resize(sizes, 210);
		
		assertEquals(3, resized.size());
		assertEquals(80, withLabel("a", resized).size());
		assertEquals(80, withLabel("b", resized).size());
		assertEquals(50, withLabel("c", resized).size());
	}

	@Test
	public void littleSmallerTriggersMin() {
		ImmutableList<ComponentSizeAndWeight<String>> sizes=ImmutableList.<ComponentSizeAndWeight<String>>builder()
				.add(entry("a",limits(10,100),1))
				.add(entry("b",limits(20,100),1))
				.add(entry("c",limits(30,50),1))
				.build();
		ImmutableList<ComponentAndSize<String>> resized = WeightedSizes.resize(sizes, 80);
		
		assertEquals(3, resized.size());
		assertEquals(25, withLabel("a", resized).size());
		assertEquals(25, withLabel("b", resized).size());
		assertEquals(30, withLabel("c", resized).size());
	}

	@Test
	public void weightZeroMeansMin() {
		ImmutableList<ComponentSizeAndWeight<String>> sizes=ImmutableList.<ComponentSizeAndWeight<String>>builder()
				.add(entry("a",limits(10,100),1))
				.add(entry("b",limits(20,100),100))
				.add(entry("c",limits(30,50),100))
				.build();
		ImmutableList<ComponentAndSize<String>> resized = WeightedSizes.resize(sizes, 210);
		
		assertEquals(3, resized.size());
		assertEquals(10, withLabel("a", resized).size());
		assertEquals(100, withLabel("b", resized).size());
		assertEquals(50, withLabel("c", resized).size());
	}

	static WeightedSizes.ComponentSizeAndWeight<String> entry(String label, Limits limits, int weight) {
		return new ComponentSizeAndWeight<String>(label, limits, weight);
	}
	
	static Limits limits(int min, int max) {
		return new Limits(min, max);
	}
	
	static ComponentAndSize<String> withLabel(final String label, ImmutableList<ComponentAndSize<String>> elements) {
		return Transformations.firstOf(Collections2.filter(elements, new Predicate<ComponentAndSize<String>>() {
			@Override
			public boolean apply(ComponentAndSize<String> input) {
				return label.equals(input.component());
			}
		})).get();
	}
}

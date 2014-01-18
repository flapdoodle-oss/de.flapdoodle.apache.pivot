package de.flapdoodle.apache.pivot.components;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Limits;
import org.apache.pivot.wtk.Orientation;

import com.google.common.base.Function;

public abstract class Components {

	private Components() {
		// no instance
	}
	
	public static Function<Component, Limits> limits(final Orientation orientation) {
		return new Function<Component, Limits>() {
			
			@Override
			public Limits apply(Component component) {
				if (orientation==Orientation.HORIZONTAL) {
					return component.getWidthLimits();
				}
				return component.getHeightLimits();
			}
		};
	}
}

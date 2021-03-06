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

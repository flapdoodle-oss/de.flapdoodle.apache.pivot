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
package de.flapdoodle.apache.pivot.layout;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Container;
import org.apache.pivot.wtk.Limits;
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.Theme;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import de.flapdoodle.apache.pivot.containers.Containers;
import de.flapdoodle.apache.pivot.skin.WeightPaneSkin;

public class WeightPane extends Container {

	enum Attr {
		Weight
	}
	
	static {
		Theme.getTheme().set(WeightPane.class, WeightPaneSkin.class);
	}
	
	private Orientation orientation;

	
	public WeightPane() {
		this(Orientation.HORIZONTAL);
	}
	
	public WeightPane(Orientation orientation) {
		setOrientation(orientation);

		installSkin(WeightPane.class);
	}

	public void setOrientation(Orientation orientation) {
		Preconditions.checkNotNull(orientation, "orientation is null");

		if (this.orientation != orientation) {
			this.orientation = orientation;
		}
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public static void setWeight(Component component, int weight) {
		Preconditions.checkArgument(weight>0,"weight <= 0");
		component.setAttribute(Attr.Weight, weight);
	}
	
	public static int getWeight(Component component) {
		Integer attribute = (Integer) component.getAttribute(Attr.Weight);
		if (attribute!=null) {
			return attribute;
		}
		return 1;
	}

	@Override
	public Limits getWidthLimits() {
		ImmutableList<Component> list = Containers.componentsAsList(this);
		Limits ret = new Limits(Containers.min(list, Orientation.HORIZONTAL),Containers.max(list, Orientation.HORIZONTAL));
		System.out.println("Width: "+ret);
		return ret;
	}
	
	@Override
	public Limits getHeightLimits() {
		ImmutableList<Component> list = Containers.componentsAsList(this);
		Limits ret = new Limits(Containers.min(list, Orientation.VERTICAL),Containers.max(list, Orientation.VERTICAL));
		System.out.println("Height: "+ret);
		return ret;
	}

}

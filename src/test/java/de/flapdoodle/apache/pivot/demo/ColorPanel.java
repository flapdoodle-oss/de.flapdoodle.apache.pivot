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
package de.flapdoodle.apache.pivot.demo;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import org.apache.pivot.wtk.Bounds;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.Panel;
import org.apache.pivot.wtk.effects.Decorator;
import org.apache.pivot.wtk.effects.FadeDecorator;

import de.flapdoodle.apache.pivot.layout.WeightPane;

public class ColorPanel extends Panel {

	public ColorPanel(Orientation orientation) {
		getStyles().put("backgroundColor", randomColor());
		getStyles().put("backgroundColor", randomColor());
		setX(random(400));
		setY(random(400));
		setSize(100, 100);
		WeightPane.setWeight(this, random(3)+1);
		int f=random(3)+1;
		switch (orientation) {
			case HORIZONTAL:
				setWidthLimits(f*2, (10-f)*10);
				break;
			case VERTICAL:
				setHeightLimits(f*2, (10-f)*10);
				break;
		}
		getDecorators().add(new FadeDecorator(0.33f));
	}

	static String randomColor() {
		return new StringBuffer().append("#").append(singleHexValue()).append(singleHexValue()).append(singleHexValue()).append(
				singleHexValue()).append(singleHexValue()).append(singleHexValue()).toString();
	}

	private static String singleHexValue() {
		return Integer.toHexString(random(15));
	}

	static int random(int range) {
		return (int) (Math.random() * range);
	}
}

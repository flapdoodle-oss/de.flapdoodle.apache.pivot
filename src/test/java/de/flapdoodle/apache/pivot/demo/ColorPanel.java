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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import org.apache.pivot.wtk.Bounds;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Label;
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.Panel;
import org.apache.pivot.wtk.effects.BlurDecorator;
import org.apache.pivot.wtk.effects.Decorator;
import org.apache.pivot.wtk.effects.FadeDecorator;

import de.flapdoodle.apache.pivot.layout.WeightPane;

public class ColorPanel extends Panel {

	private Label label;

	public ColorPanel(Orientation orientation) {
		getStyles().put("backgroundColor", randomColor());
		getStyles().put("backgroundColor", randomColor());
		//setX(random(400));
		//setY(random(400));
		//setSize(100, 100);
		int weight = random(5)+1;
		WeightPane.setWeight(this, weight);
		int f=random(5)+1;
		switch (orientation) {
			case HORIZONTAL:
				setWidthLimits(f*2, (10-f)*50);
				break;
			case VERTICAL:
				setHeightLimits(f*2, (10-f)*50);
				break;
		}
		//getDecorators().add(new BlurDecorator(2));
		Label label = new Label("w("+weight+")");
		label.getStyles().put("color", "#ffffff");
		label.getStyles().put("font", new Font("Arial", Font.BOLD, 32));
		label.setLocation(5, 5);
		label.setSize(30, 30);
		add(label);
		
		this.label=label;
	}
	
	@Override
	public void setSize(int width, int height) {
		super.setSize(width, height);
		label.setSize(width-5, height-5);
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

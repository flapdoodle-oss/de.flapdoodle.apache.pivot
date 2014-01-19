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
package de.flapdoodle.apache.pivot.skin;

import java.util.List;
import java.util.Map;

import org.apache.pivot.wtk.BoxPane;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Limits;
import org.apache.pivot.wtk.Orientation;
import org.apache.pivot.wtk.skin.ContainerSkin;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;

import de.flapdoodle.apache.pivot.components.Components;
import de.flapdoodle.apache.pivot.containers.Containers;
import de.flapdoodle.apache.pivot.layout.WeightPane;
import de.flapdoodle.apache.pivot.skin.WeightedSizes.ComponentAndSize;
import de.flapdoodle.apache.pivot.skin.WeightedSizes.ComponentSizeAndWeight;
import de.flapdoodle.guava.Foldleft;
import de.flapdoodle.guava.Folds;
import de.flapdoodle.guava.Transformations;


public class WeightPaneSkin extends ContainerSkin {

	@Override
	public void layout() {
		WeightPane pane = (WeightPane)getComponent();
    int n = pane.getLength();
		String prefix=pane.getName();
		if (prefix==null) {
			prefix="#"+pane.hashCode();
		}
		prefix=prefix+">";

    int width = getWidth();
    int height = getHeight();
    int startX = pane.getX();
    int startY = pane.getY();
//    System.out.println(prefix+"pane("+startX+","+startY+","+width+"x"+height+")");
    
    Orientation orientation = pane.getOrientation();

    ImmutableList<Component> components = Containers.componentsAsList(pane);
    
    ImmutableList<ComponentSizeAndWeight<Component>> sizes=WeightedSizes.componentSizes(components, orientation);
//    System.out.println(prefix+"sizes="+sizes);
    
    ImmutableList<ComponentAndSize<Component>> newSizes=WeightedSizes.resize(sizes,orientation==Orientation.HORIZONTAL ? width : height);
//    System.out.println(prefix+"resized="+newSizes);
    
  	Orientation invertedOrientation = orientation==Orientation.HORIZONTAL ? Orientation.VERTICAL : Orientation.HORIZONTAL;
  	
    int offset=0;//orientation==Orientation.HORIZONTAL ? startX : startY;
    
    for (ComponentAndSize<Component> cs : newSizes) {
    	Component c=cs.component();
			Limits limits=Components.limits(invertedOrientation).apply(c);
    	
    	switch (orientation) {
    		case HORIZONTAL:
//    			System.out.println(prefix+limits.maximum+"?"+height+"->"+min(limits.maximum, height)+","+cs.size());
    			c.setLocation(offset,0 /*startY*/);
    			c.setSize(cs.size(), min(limits.maximum, height));
    			break;
    		case VERTICAL:
//    			System.out.println(prefix+limits.maximum+"?"+width+"->"+min(limits.maximum, width)+","+cs.size());
    			c.setLocation(0 /*startX*/,offset);
    			c.setSize(min(limits.maximum, width),cs.size());
    			break;
    	}
			offset=offset+cs.size();
    }
	}
	
	static int min(int a, int b) {
		return a<b ? a : b;
	}
}

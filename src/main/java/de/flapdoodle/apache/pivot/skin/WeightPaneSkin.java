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

    int width = getWidth();
    int height = getHeight();
    Orientation orientation = pane.getOrientation();

    ImmutableList<Component> components = Containers.componentsAsList(pane);
    
    ImmutableList<ComponentSizeAndWeight<Component>> sizes=WeightedSizes.componentSizes(components, orientation);
    ImmutableList<ComponentAndSize<Component>> newSizes=WeightedSizes.resize(sizes,orientation==Orientation.HORIZONTAL ? width : height);
    Map<Component, ComponentAndSize<Component>> asMap=Transformations.map(newSizes, new Function<ComponentAndSize<Component>, Component>() {
    	@Override
    	public Component apply(ComponentAndSize<Component> input) {
    		return input.component();
    	}
    });
    
    int offset=0;
    for (Component c : components) {
    	ComponentAndSize<Component> cs=asMap.get(c);
    	Limits limits=Components.limits(orientation).apply(c);
    	
    	switch (orientation) {
    		case HORIZONTAL:
    			c.setLocation(offset,0);
    			c.setSize(cs.size(), min(limits.maximum, height));
    			break;
    		case VERTICAL:
    			c.setLocation(0,offset);
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

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

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

import java.util.List;

import org.apache.pivot.collections.ArrayList;
import org.apache.pivot.wtk.ListButton;
import org.apache.pivot.wtk.content.ListItem;

import com.google.common.collect.ImmutableList;

public class TypedListButton<T> extends ListButton {

	private ImmutableList<T> selection;
	private T selected;

	public TypedListButton(T selected, List<T> selection, IItemConverter<T> converter) {
		super(converter.convert(selected), asListItemList(selection, converter));
		this.selected = selected;
		this.selection = ImmutableList.copyOf(selection);
	}

	private static <T> org.apache.pivot.collections.List<?> asListItemList(List<T> selection, IItemConverter<T> converter) {

		ArrayList<ListItem> ret = new ArrayList<ListItem>();
		for (T s : selection) {
			ret.add(converter.convert(s));
		}
		return ret;
	}

	public interface IItemConverter<T> {

		ListItem convert(T value);
	}

	public T getSelection() {
		if (getSelectedIndex() != -1) {
			return selection.get(getSelectedIndex());
		}
		return selected;
	}
}

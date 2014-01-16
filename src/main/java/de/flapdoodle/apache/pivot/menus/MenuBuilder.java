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
package de.flapdoodle.apache.pivot.menus;

import java.util.List;

import org.apache.pivot.wtk.Keyboard.KeyStroke;
import org.apache.pivot.wtk.Menu;
import org.apache.pivot.wtk.MenuBar;
import org.apache.pivot.wtk.MenuBar.Item;
import org.apache.pivot.wtk.MenuBar.ItemSequence;
import org.apache.pivot.wtk.MenuItemSelectionListener;
import org.apache.pivot.wtk.content.MenuItemData;

import com.google.common.collect.Lists;

import de.flapdoodle.apache.pivot.components.IBuilder;

public final class MenuBuilder {

	static enum MenuAttr {
		Action;
	}

	List<IBuilder<MenuBar.Item>> menuBuilders = Lists.newArrayList();

	public MenuBuilder() {
	}

	public MenuBuilder menu(IBuilder<MenuBar.Item> menuBuilder) {
		menuBuilders.add(menuBuilder);
		return this;
	}

	public void apply(MenuBar menuBar) {
		ItemSequence items = menuBar.getItems();
		for (IBuilder<MenuBar.Item> builder : menuBuilders) {
			items.add(builder.build());
		}
	}

	public static final class SubMenu implements IBuilder<MenuBar.Item> {

		List<IBuilder<Menu.Item>> menuBuilders = Lists.newArrayList();
		private Object menuData;

		public SubMenu setData(Object menuData) {
			this.menuData = menuData;
			return this;
		}

		@Override
		public Item build() {
			MenuBar.Item ret = new MenuBar.Item(menuData);
			Menu menu = new Menu();
			Menu.Section section = new Menu.Section();
			for (IBuilder<Menu.Item> builder : menuBuilders) {
				section.add(builder.build());
			}
			menu.getSections().add(section);
			menu.getMenuItemSelectionListeners().add(new MenuItemSelectionListener() {

				@Override
				public void itemSelected(Menu.Item menuItem) {
					IMenuAction action = (IMenuAction) menuItem.getAttribute(MenuAttr.Action);
					if (action != null) {
						action.onSelect(menuItem);
					}
				}
			});

			ret.setMenu(menu);
			return ret;
		}

		public SubMenu entry(IBuilder<Menu.Item> entryBuilder) {
			menuBuilders.add(entryBuilder);
			return this;
		}
	}

	public static final class Entry implements IBuilder<Menu.Item> {

		private IMenuAction action;
		private String text;
		private KeyStroke keyboardShortcut;

		public Entry setText(String text) {
			this.text = text;
			return this;
		}

		public Entry setKeyStroke(KeyStroke keyboardShortcut) {
			this.keyboardShortcut = keyboardShortcut;
			return this;
		}

		public Entry onSelect(IMenuAction action) {
			this.action = action;
			return this;
		}

		@Override
		public Menu.Item build() {
			MenuItemData menuItemData = new MenuItemData();
			menuItemData.setText(text);
			menuItemData.setKeyboardShortcut(keyboardShortcut);

			Menu.Item ret = new Menu.Item(menuItemData);
			ret.setAttribute(MenuAttr.Action, action);
			//            if (action!=null) {
			//                ret.getButtonPressListeners().add(new MenuActionAdapter(action, ret));
			//                
			//            }
			return ret;
		}

	}

	public interface IMenuAction {

		public void onSelect(Menu.Item item);
	}

	//    private static final class MenuActionAdapter implements ButtonPressListener {
	//        private final Menu.Item item;
	//        private final IMenuAction action;
	//
	//        public MenuActionAdapter(IMenuAction action, Menu.Item item) {
	//            this.action = action;
	//            this.item = item;
	//        }
	//
	//        @Override
	//        public void buttonPressed(Button button) {
	//            action.onSelect(item);
	//        }
	//    }

}

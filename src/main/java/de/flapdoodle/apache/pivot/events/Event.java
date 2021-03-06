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
package de.flapdoodle.apache.pivot.events;

import com.google.common.base.Preconditions;

public abstract class Event {

	private final IEventSink sink;

	public Event(IEventSink sink) {
		Preconditions.checkNotNull(sink, "sink is null");
		this.sink = sink;
	}

	public void fire(EventPropagation propagation) {
		sink.send(this, propagation);
	}
}

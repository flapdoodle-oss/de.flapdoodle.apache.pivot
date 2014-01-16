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

import com.google.common.base.Optional;

public abstract class AsyncEvent<T> extends Event {

	private final T payload;
	private final Throwable fault;

	public AsyncEvent(IEventSink sink, T payload) {
		super(sink);
		this.payload = payload;
		this.fault = null;
	}

	public AsyncEvent(IEventSink sink, Throwable fault) {
		super(sink);
		this.fault = fault;
		this.payload = null;
	}

	public T payload() {
		return payload;
	}

	public <X> Optional<X> payload(Class<X> type) {
		if (!failed()) {
			if (type.isInstance(payload)) {
				return Optional.of((X) payload);
			}
		}
		return Optional.absent();
	}

	public Throwable fault() {
		return fault;
	}

	public boolean failed() {
		return fault != null;
	}

}

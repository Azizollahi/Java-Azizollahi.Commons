package azizollahi.commons.pooling.services;

import azizollahi.commons.services.Event;
import azizollahi.commons.services.Service;

import java.util.function.Function;

public class ServiceEvent {
	private Service service;
	private Event<Function> event;

	public ServiceEvent() {
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public Event<Function> getEvent() {
		return event;
	}

	public void setEvent(Event<Function> event) {
		this.event = event;
	}
}

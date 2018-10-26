package azizollahi.commons.pooling.services;

import azizollahi.commons.services.Event;

import java.util.List;
import java.util.Queue;
import java.util.function.Function;

public interface ServicePool {
	void start();

	void stop();

	void run(Function action);

	void lock();

	void unlock();

	int getMaximumServices();

	int getMinimumServices();

	int getIncreaseStep();

	int getDecreaseStep();

	int getIncreasePercentage();

	int getDecreasePercentage();

	Queue<Event<Function>> getEvents();

	List<ServiceEvent> getServices();

	String getName();
}

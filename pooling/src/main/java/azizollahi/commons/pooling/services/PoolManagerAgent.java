package azizollahi.commons.pooling.services;

import azizollahi.commons.services.Agent;
import azizollahi.commons.services.Impl.BasicEvent;
import azizollahi.commons.services.ListeningEventService;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class PoolManagerAgent implements Agent<String, String> {
	private ServicePool servicePool;

	public PoolManagerAgent(ServicePool servicePool) {
		this.servicePool = servicePool;
	}

	@Override
	public String run(String data) {
		if (checkIncrementalProcess()) {
			for (var counter = 0; counter < servicePool.getIncreaseStep(); counter++) {
				startService();
			}
		}
		if (checkDecrementalProcess()) {
			for (var counter = 0; counter < servicePool.getDecreaseStep(); counter++) {
				stopService();
			}
		}
		return null;
	}

	private void stopService() {
		try {
			servicePool.lock();
			var service = servicePool
					.getServices()
					.stream().filter(x ->
							x.getService().isAlive() &&
									x.getEvent().isWaiting() &&
									x.getEvent().isSafeToDelete())
					.findFirst();
			if (service.isPresent()) {
				service.get().getService().stop();
				servicePool.getServices().remove(service.get());
				servicePool.getEvents().remove(service.get().getEvent());
			}
		} finally {
			servicePool.unlock();
		}
	}

	private void startService() {
		var newService = createNewServiceEvent();
		try {
			servicePool.lock();
			servicePool.getEvents().add(newService.getEvent());
			servicePool.getServices().add(newService);
		} finally {
			servicePool.unlock();
		}
		newService.getService().start();
	}

	private ServiceEvent createNewServiceEvent() {
		var newService = new ServiceEvent();
		var event = new BasicEvent<Function>();
		var serviceName = servicePool.getName() + "-" + ServiceIdGenerator.generateNewId();
		newService.setService(new ListeningEventService<>(
				servicePool.getName() + "-" + ServiceIdGenerator.generateNewId(),
				new ServiceAgent(),
				LoggerFactory.getLogger(serviceName), event));
		newService.setEvent(event);
		return newService;
	}

	private boolean checkDecrementalProcess() {
		return waitingPercentage() >= servicePool.getDecreasePercentage() && servicePool.getMinimumServices() <
				servicePool.getServices().stream().filter(x -> x.getService().isAlive()).count();
	}

	private boolean checkIncrementalProcess() {
		return waitingPercentage() < servicePool.getIncreasePercentage() && servicePool.getMaximumServices() >
				servicePool.getServices().stream().filter(x -> x.getService().isAlive()).count();

	}

	private long waitingPercentage() {
		long waitingCount = 0;
		long total = 1;
		waitingCount = servicePool.getServices().stream().filter(x -> x.getEvent().isWaiting() && x.getService().isAlive()).count();
		total = servicePool.getServices().stream().filter(x -> x.getService().isAlive()).count();

		if (total == 0)
			return 0;
		return (waitingCount / total) * 100;

	}

}

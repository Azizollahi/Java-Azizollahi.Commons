package azizollahi.commons.pooling.services;

import azizollahi.commons.services.Event;
import azizollahi.commons.services.Impl.BasicEvent;
import azizollahi.commons.services.ListeningEventService;
import azizollahi.commons.services.PeriodicService;
import azizollahi.commons.services.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

public class AzizollahiServicePool implements ServicePool {
	private int minimumServices;
	private int maximumServices;
	private int increaseStep;
	private int decreaseStep;
	private int increasePercentage;
	private int decreasePercentage;
	private Service poolManager;
	private Logger logger;
	private String name;
	private Queue<Event<Function>> events;
	private List<ServiceEvent> services;
	private Lock lock;

	public AzizollahiServicePool(String name, Logger logger) {
		initialize(name, logger, 10, 100, 2, 1, 10, 90);
	}

	public AzizollahiServicePool(String name, Logger logger,
								 int minimumServices) {
		initialize(name, logger, minimumServices, 100, 2, 1, 10, 90);
	}

	public AzizollahiServicePool(String name, Logger logger,
								 int minimumServices, int maximumServices) {
		initialize(name, logger, minimumServices, maximumServices, 2, 1, 10, 90);
	}

	public AzizollahiServicePool(String name, Logger logger,
								 int minimumServices, int maximumServices,
								 int incrementStep) {
		initialize(name, logger, minimumServices, maximumServices, incrementStep, 1, 10, 90);
	}

	public AzizollahiServicePool(String name, Logger logger,
								 int minimumServices, int maximumServices,
								 int incrementStep, int decrementStep) {
		initialize(name, logger, minimumServices, maximumServices, incrementStep, decrementStep, 10, 90);
	}

	public AzizollahiServicePool(String name, Logger logger,
								 int minimumServices, int maximumServices,
								 int incrementStep, int decrementStep,
								 int IncrementPercentage) {
		initialize(name, logger, minimumServices, maximumServices, incrementStep, decrementStep, IncrementPercentage, 90);
	}

	public AzizollahiServicePool(String name, Logger logger,
								 int minimumServices, int maximumServices,
								 int incrementStep, int decrementStep,
								 int IncrementPercentage, int decrementPercentage) {
		initialize(name, logger, minimumServices, maximumServices, incrementStep, decrementStep, IncrementPercentage, decrementPercentage);
	}

	private void initialize(String name, Logger logger, int minimumServices, int maximumServices,
							int incrementStep, int decrementStep,
							int incrementPercentage, int decrementPercentage) {
		this.minimumServices = minimumServices;
		this.maximumServices = maximumServices;
		this.increaseStep = incrementStep;
		this.decreaseStep = decrementStep;
		this.increasePercentage = incrementPercentage;
		this.decreasePercentage = decrementPercentage;
		this.name = name;
		this.logger = logger;
		this.poolManager = new PeriodicService(this.name + "-pool-manager", new PoolManagerAgent(this), this.logger, 100);
		events = new ConcurrentLinkedQueue<>();
		services = new LinkedList<>();
		lock = new ReentrantLock();
		initializeMinimumServices();
	}

	private void initializeMinimumServices(){
		lock();

		for(int counter = 0; counter < minimumServices; counter++) {
			var newService = new ServiceEvent();
			var event = new BasicEvent<Function>();
			var serviceName = getName() + "-" + ServiceIdGenerator.generateNewId();
			newService.setService(new ListeningEventService<Function>(
					getName() + "-" + ServiceIdGenerator.generateNewId(),
					new ServiceAgent(),
					LoggerFactory.getLogger(serviceName), event));
			newService.setEvent(event);


			events.add(newService.getEvent());
			newService.getService().start();
			services.add(newService);
		}

		unlock();
	}

	@Override
	public void start() {
		this.poolManager.start();
	}

	@Override
	public void stop() {
		this.poolManager.stop();
	}

	@Override
	public void run(Function action) {
		try {
			lock();
			var event = events.poll();
			event.notify(action);
			events.add(event);
		} finally {
			unlock();
		}
	}

	@Override
	public void lock() {
		lock.lock();
	}

	@Override
	public void unlock() {
		lock.unlock();
	}

	@Override
	public int getMinimumServices() {
		return minimumServices;
	}

	@Override
	public int getMaximumServices() {
		return maximumServices;
	}

	@Override
	public int getIncreaseStep() {
		return increaseStep;
	}

	@Override
	public int getDecreaseStep() {
		return decreaseStep;
	}

	@Override
	public int getIncreasePercentage() {
		return increasePercentage;
	}

	@Override
	public int getDecreasePercentage() {
		return decreasePercentage;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Queue<Event<Function>> getEvents() {
		return events;
	}

	@Override
	public List<ServiceEvent> getServices() {
		return services;
	}
}

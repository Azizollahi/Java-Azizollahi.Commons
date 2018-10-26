package azizollahi.commons.services;

import org.slf4j.Logger;

import java.util.concurrent.Semaphore;

public abstract class AService<I, O> implements Service, Runnable {
	private boolean isRunning;
	private boolean isPaused;
	private Thread trd;
	private ThreadGroup threadGroup;
	private Semaphore pauseSemaphore;
	private Agent<I, O> agent;
	protected String name;
	protected Logger logger;

	protected AService(String name, Agent<I, O> agent, Logger logger) {
		initialize(null, name, agent, logger);
	}

	protected AService(ThreadGroup threadGroup, String name, Agent<I, O> agent, Logger logger) {
		initialize(threadGroup, name, agent, logger);
	}

	private void initialize(ThreadGroup threadGroup, String name, Agent<I, O> agent, Logger logger) {
		this.threadGroup = threadGroup;
		trd = new Thread(threadGroup, this);
		isRunning = false;
		isPaused = true;
		pauseSemaphore = new Semaphore(0);
		this.name = name;
		this.logger = logger;
		this.agent = agent;
	}

	protected abstract void onStart();

	protected abstract void onStop();

	protected abstract void onPause();

	protected abstract void onResume();

	protected abstract I onStartProcess() throws InterruptedException;

	protected abstract void onEndProcess(O data) throws InterruptedException;

	protected abstract void onException(Exception exception);

	public ThreadGroup getThreadGroup() {
		return threadGroup;
	}

	public void start() {
		trd.start();
	}

	public void stop() {
		isRunning = false;
		trd.interrupt();
	}

	public void pause() {
		isPaused = true;
	}

	public void resume() {
		isPaused = false;
		pauseSemaphore.release();
	}

	public boolean isAlive() {
		return isRunning;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void run() {
		isPaused = false;
		isRunning = true;
		while (isRunning) {
			try {
				logger.trace("Going to process");
				var data = onStartProcess();
				if (isPaused) {
					pauseSemaphore.wait();
					continue;
				}
				var result = agent.run(data);
				onEndProcess(result);
				logger.trace("Process is finished");
			} catch (InterruptedException exception) {
				logger.trace("Process is aborted");
				break;
			} catch (Exception exception) {
				onException(exception);
				logger.error("[" + name + "]: ", exception);
			}
		}
		isPaused = true;
	}
}

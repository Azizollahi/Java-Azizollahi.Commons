package azizollahi.commons.services.Impl;

import azizollahi.commons.services.Event;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

public class BasicEvent<T> implements Event<T> {
	private Queue<T> messages;
	private Semaphore semaphore;
	private boolean waitingState;
	public BasicEvent() {
		initialize();
	}

	private void initialize() {
		semaphore = new Semaphore(0);
		messages = new ConcurrentLinkedQueue<>();
	}

	@Override
	public T waitOne() throws InterruptedException {
		if(messages.size() == 0)
			waitingState = true;
		semaphore.acquire();
		return messages.poll();
	}

	@Override
	public void notify(T input) {
		messages.add(input);
		waitingState = false;
		semaphore.release();
	}

	@Override
	public boolean isWaiting() {
		return waitingState;
	}

	@Override
	public boolean isSafeToDelete() {
		return messages.isEmpty();
	}

}

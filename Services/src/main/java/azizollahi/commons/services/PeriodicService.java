package azizollahi.commons.services;

import org.slf4j.Logger;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PeriodicService extends AService<String, String> {
	private int periodTime;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;

	public PeriodicService(String name, Agent<String, String> agent, Logger logger, int periodTime) {
		super(name, agent, logger);
		this.periodTime = periodTime;
	}

	public PeriodicService(ThreadGroup trdGroup, String name, Agent<String, String> agent, Logger logger, int periodTime) {
		super(trdGroup, name, agent, logger);
		this.periodTime = periodTime;
	}


	@Override
	protected void onStart() {

	}

	@Override
	protected void onStop() {

	}

	@Override
	protected void onPause() {

	}

	@Override
	protected void onResume() {

	}

	@Override
	protected String onStartProcess() {
		startDateTime = LocalDateTime.now();
		return null;
	}

	@Override
	protected void onEndProcess(String data) throws InterruptedException {
		waitForPeriodicTime();
	}

	@Override
	protected void onException(Exception exception) {

	}

	private void waitForPeriodicTime() throws InterruptedException {
		endDateTime = LocalDateTime.now();
		var period = ChronoUnit.MILLIS.between(startDateTime, endDateTime);
		var timeLeftForWait = periodTime - period;
		if (timeLeftForWait > 0)
			Thread.sleep(timeLeftForWait);
	}
}

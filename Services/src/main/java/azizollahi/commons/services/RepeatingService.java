package azizollahi.commons.services;

import org.slf4j.Logger;

public class RepeatingService extends AService<String, String> {

	public RepeatingService(String name, Agent<String, String> agent, Logger logger) {
		super(name, agent, logger);
	}

	public RepeatingService(ThreadGroup trdGroup, String name, Agent<String, String> agent, Logger logger) {
		super(trdGroup, name, agent, logger);
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
		return null;
	}

	@Override
	protected void onEndProcess(String data) {

	}

	@Override
	protected void onException(Exception exception) {

	}

}

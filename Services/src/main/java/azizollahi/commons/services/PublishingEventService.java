package azizollahi.commons.services;

import org.slf4j.Logger;

public class PublishingEventService<O> extends AService<String, O> {
    private Event<O> event;
    public PublishingEventService(String name, Agent<String, O> agent, Logger logger, Event<O> event) {
        super(name, agent, logger);
        this.event = event;
    }

    public PublishingEventService(ThreadGroup threadGroup, String name, Agent<String, O> agent, Logger logger, Event<O> event) {
        super(threadGroup, name, agent, logger);
        this.event = event;
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
    protected void onEndProcess(O data) throws InterruptedException {
        event.notify(data);
    }

    @Override
    protected void onException(Exception exception) {

    }
}

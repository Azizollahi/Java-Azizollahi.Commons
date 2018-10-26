package azizollahi.commons.services;

import org.slf4j.Logger;

public class ListeningEventService<I> extends AService<I,String>{

    private Event<I> event;
    public ListeningEventService(String name, Agent<I, String> agent, Logger logger, Event<I> event) {
        super(name, agent, logger);
        this.event = event;
    }

    public ListeningEventService(ThreadGroup threadGroup, String name, Agent<I, String> agent, Logger logger, Event<I> event) {
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
    protected I onStartProcess() throws InterruptedException {
        return event.waitOne();
    }

    @Override
    protected void onEndProcess(String data) throws InterruptedException {

    }

    @Override
    protected void onException(Exception exception) {

    }
}

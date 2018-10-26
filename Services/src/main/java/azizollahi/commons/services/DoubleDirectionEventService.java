package azizollahi.commons.services;

import org.slf4j.Logger;

public class DoubleDirectionEventService<I,O> extends AService<I,O> {
    private Event<I> inputEvent;
    private Event<O> outputEvent;
    public DoubleDirectionEventService(String name, Agent<I, O> agent, Logger logger, Event<I> inputEvent, Event<O> outputEvent) {
        super(name, agent, logger);
        this.inputEvent = inputEvent;
        this.outputEvent = outputEvent;
    }

    public DoubleDirectionEventService(ThreadGroup threadGroup, String name, Agent<I, O> agent, Logger logger, Event<I> inputEvent, Event<O> outputEvent) {
        super(threadGroup, name, agent, logger);
        this.inputEvent = inputEvent;
        this.outputEvent = outputEvent;
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
        return inputEvent.waitOne();
    }

    @Override
    protected void onEndProcess(O data) throws InterruptedException {
        outputEvent.notify(data);
    }

    @Override
    protected void onException(Exception exception) {

    }
}

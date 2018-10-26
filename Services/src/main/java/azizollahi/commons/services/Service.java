package azizollahi.commons.services;

public interface Service {
    void start();
    void stop();
    void pause();
    void resume();

    boolean isAlive();

    boolean isPaused();
    ThreadGroup getThreadGroup();
}

package azizollahi.commons.services;

public interface Event<T> {
    T waitOne() throws InterruptedException;
    void notify(T input);
    boolean isWaiting();
    boolean isSafeToDelete();
}

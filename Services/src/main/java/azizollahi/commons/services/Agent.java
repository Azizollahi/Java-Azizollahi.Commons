package azizollahi.commons.services;

public interface Agent<I,O> {
    O run(I data) throws InterruptedException;
}

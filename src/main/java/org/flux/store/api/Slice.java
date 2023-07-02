package org.flux.store.api;

import java.util.function.Consumer;

public interface Slice<T extends State> {
    T getState();
    Consumer getAction(String type) throws InvalidActionException;
}

package org.flux.store.api;

@FunctionalInterface
public interface Reducer<T extends State> {

    T reduce(Action action, T state);
}

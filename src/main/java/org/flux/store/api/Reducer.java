package org.flux.store.api;

public interface Reducer<T extends State> {

    T reduce(Action action, T state);
}

package org.flux.store.api;

import java.util.function.Consumer;

@FunctionalInterface
public interface Middleware<T extends State> {

    void run(Store<T> store, Consumer<Action> next, Action action);
}
package org.flux.store.api;

import java.lang.reflect.Type;
import java.util.function.Consumer;

public interface Slice<T extends State> {
    T getState();
    Consumer getAction(String type) throws InvalidActionException;
    void restore(StoreBackup<T> backup);
    StoreBackup<T> backup();
    void goBack();
    void goForward();
}

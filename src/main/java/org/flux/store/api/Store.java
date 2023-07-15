package org.flux.store.api;

import java.util.function.Consumer;

public interface Store<T extends State> {

    void subscribe(Consumer<T> fn);
    void dispatch(Action action);
    void dispatch(Thunk<T> thunk);
    T getState();
    void replaceReducer(Reducer<T> newReducer);
    void restore(StoreBackup<T> backup);
    StoreBackup<T> backup();
    void goBack();
    void goForward();
}

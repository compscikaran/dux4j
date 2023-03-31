package org.flux.store.main;

import org.apache.commons.lang3.builder.DiffResult;
import org.flux.store.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class DuxStore<T extends State> implements Store<T> {

    public static final String INITIAL_ACTION = "STORE_INITIALIZATION";
    private Reducer<T> reducer;
    private TimeTravel<T> timeTravel;
    private T state;
    public List<Consumer<T>> listeners = new ArrayList<>();
    private Middleware<T> middleware;

    public DuxStore(T initialState, Reducer<T> reducer) {
        this.state = initialState;
        this.reducer = reducer;
        this.timeTravel = new TimeTravel<>();
        this.timeTravel.recordChange(INITIAL_ACTION, initialState);
    }

    public DuxStore(T initialState, Reducer<T> reducer, Middleware<T> middleware) {
        this(initialState, reducer);
        this.middleware = middleware;
    }

    @Override
    public void subscribe(Consumer<T> fn) {
        this.listeners.add(fn);
    }

    @Override
    public void dispatch(Action action) {
        if(middleware != null) {
            middleware.run(this, this::dispatchInternal, action);
        } else {
            dispatchInternal(action);
        }
    }

    @Override
    public void dispatch(Thunk<T> action) {
        action.process(this::dispatch, this::getState);
    }

    private void dispatchInternal(Action action) {
        T newState = reducer.reduce(action, (T) state.clone());
        DiffResult diffResult = newState.diff(state);
        boolean isChanged = diffResult.getNumberOfDiffs() > 0;
        if(isChanged) {
            this.state = newState;
            this.timeTravel.recordChange(action.getType(), newState);
            this.notifyListeners();
        }
    }

    @Override
    public T getState() {
        return this.state;
    }

    @Override
    public void replaceReducer(Reducer<T> newReducer) {
        this.reducer = newReducer;
    }

    private void notifyListeners() {
        this.listeners.forEach(l -> l.accept(this.state));
    }

    public void goBack() {
        this.timeTravel.goBack();
        this.state = this.timeTravel.getCurrentState();
        this.notifyListeners();
    }

    public void goForward() {
        this.timeTravel.goForward();
        this.state = this.timeTravel.getCurrentState();
        this.notifyListeners();
    }

    public List<String> getActionHistory() {
        return this.timeTravel.getActionHistory();
    }
}

package org.flux.store.main;

import org.apache.commons.lang3.builder.DiffResult;
import org.flux.store.api.Action;
import org.flux.store.api.Reducer;
import org.flux.store.api.State;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Store<T extends State> {

    public static final String INITIAL_ACTION = "STORE_INITIALIZATION";
    private Reducer<T> reducer;
    private TimeTravel<T> timeTravel;
    private T state;
    public List<Consumer<T>> listeners = new ArrayList<>();

    public Store(T initialState, Reducer<T> reducer) {
        this.state = initialState;
        this.reducer = reducer;
        this.timeTravel = new TimeTravel<>();
        this.timeTravel.recordChange(INITIAL_ACTION, initialState);
    }

    public void subscribe(Consumer<T> fn) {
        this.listeners.add(fn);
    }

    public void dispatch(Action action) {
        T newState = reducer.reduce(action, (T) state.clone());
        DiffResult diffResult = newState.diff(state);
        boolean isChanged = diffResult.getNumberOfDiffs() > 0;
        if(isChanged) {
            this.state = newState;
            this.timeTravel.recordChange(action.getType(), newState);
            this.notifyListeners();
        }
    }

    public T getState() {
        return this.state;
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

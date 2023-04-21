package org.flux.store.main;

import org.flux.store.api.InvalidActionException;
import org.flux.store.api.Reducer;
import org.flux.store.api.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class DuxSlice<T extends State> {

    private DuxStore<T> store;
    private List<String> actions;
    private String name;

    private DuxSlice(DuxStore<T> store, List<String> actions, String name) {
        this.store = store;
        this.actions = actions;
        this.name = name;
    }

    public static <T extends State> DuxSlice<T> createSlice(String name, T initialState, Map<String, Reducer<T>> reducers, List<Consumer<T>> subscribers) {
        Reducer<T> reducer = (action, state) -> {
            for (String key: reducers.keySet()) {
                if(action.getType().equalsIgnoreCase(key)) {
                    Reducer<T> current = reducers.get(key);
                    state = current.reduce(action,state);
                }
            }
            return state;
        };
        DuxStore<T> myStore = new DuxStore<>(initialState, reducer);
        for (Consumer<T> subscriber: subscribers) {
            myStore.subscribe(subscriber);
        }
        DuxSlice<T> slice = new DuxSlice<>(myStore, new ArrayList<>(reducers.keySet()), name);
        return slice;
    }

    public Consumer getAction(String type) throws InvalidActionException {
        if(!actions.contains(type))
            throw new InvalidActionException("Action type does not exist on slice" + this.name);
        return payload -> store.dispatch(Utilities.actionCreator(type, payload));
    }

    public T getState() {
        return store.getState();
    }
}

package org.flux.store.main;

import org.flux.store.api.Action;
import org.flux.store.api.Reducer;
import org.flux.store.api.State;

import java.util.Arrays;

public class Utilities {
    public static <T> Action<T> actionCreator(String actionType, T payload) {
        return new Action<T>(actionType, payload);
    }

    public static <T extends State> Reducer<T> combineReducer(Reducer<T>... reducers) {
        return (action, state) -> {
            T newState = state;
            for (Reducer<T> reducer: reducers){
                newState = reducer.reduce(action, newState);
            }
            return newState;
        };
    }
}

package org.flux.store.main;

import org.flux.store.api.*;

import java.util.Arrays;
import java.util.function.Consumer;

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

    public static <T extends State> Middleware<T> compose(Middleware<T>... middlewares) {
        return (store, next, action) -> {
            for (Middleware<T> middleware: middlewares) {
                middleware.run(store, next, action);
            }
        };
    }
}

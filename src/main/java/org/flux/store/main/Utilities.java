package org.flux.store.main;

import org.flux.store.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class Utilities {

    public static final String INITIAL_ACTION = "STORE_INITIALIZATION";

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
            AtomicReference<Action> updatedAction = new AtomicReference<>(action);
            for (int i = 0; i < middlewares.length; i++) {
                Middleware<T> element = middlewares[i];
                if(i < middlewares.length -1) {
                    element.run(store, updatedAction::set, updatedAction.get());
                } else {
                    element.run(store, next, updatedAction.get());
                }
            }
        };
    }
}

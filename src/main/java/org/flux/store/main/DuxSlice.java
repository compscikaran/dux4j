package org.flux.store.main;

import lombok.AllArgsConstructor;
import org.flux.store.api.Action;
import org.flux.store.api.ActionCall;
import org.flux.store.api.State;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class DuxSlice<T extends State> {

    private DuxStore<T> store;
    private List<String> actions;

    public T getState() {
        return store.getState();
    }

    public void dispatch(String type, Object payload) {
        if(!actions.contains(type))
            throw new IllegalArgumentException("Action type " + type + " does not exist on slice");
        store.dispatch(Utilities.actionCreator(type, payload));
    }
}

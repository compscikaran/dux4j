package org.flux.store.main;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TimeTravel<T> {

    private List<String> actions;
    private List<T> history;
    private Integer index;

    public TimeTravel() {
        this.actions = new ArrayList<>();
        this.history = new ArrayList<>();
        this.index = -1;
    }

    public void recordChange(String action, T state) {
        actions.add(action);
        history.add(state);
        index ++;
    }

    public T getCurrentState() {
        return history.get(index);
    }

    public void goForward() {
        if(index < history.size() - 1)
            index ++;
    }

    public void goBack() {
        if(index > 0)
            index --;
    }

}

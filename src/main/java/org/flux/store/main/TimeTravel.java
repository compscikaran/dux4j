package org.flux.store.main;

import lombok.Getter;
import org.flux.store.api.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimeTravel<T> {

    private List<Action> actions;
    private Integer index;

    public TimeTravel() {
        this.actions = new ArrayList<>();
        this.index = -1;
    }

    public void recordChange(Action action) {
        actions.add(action);
        index ++;
    }

    public void goForward() {
        if(index < actions.size() - 1)
            index ++;
    }

    public void goBack() {
        if(index > 0)
            index --;
    }

    public List<String> getActionTypeHistory() {
        return this.actions.stream()
                .map(x -> x.getType())
                .collect(Collectors.toList())
                .subList(0,index+1);
    }

    public List<Action> getActionHistory() {
        return this.actions.subList(1,index+1);
    }

    public Action getLatestAction() {
        return index > 0 ? this.actions.get(index) : null;
    }

    public T getInitialState() {
        return (T) this.actions.get(0).getPayload();
    }


}

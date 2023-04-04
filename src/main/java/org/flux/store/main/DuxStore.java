package org.flux.store.main;

import com.google.gson.Gson;
import org.apache.commons.lang3.builder.DiffResult;
import org.flux.store.api.*;
import org.flux.store.kafka.Producer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

public class DuxStore<T extends State> implements Store<T> {

    private Reducer<T> reducer;
    private TimeTravel<T> timeTravel;
    private T state;
    public List<Consumer<T>> listeners = new ArrayList<>();
    private Middleware<T> middleware;
    private Producer producer;
    private Gson gson = new Gson();

    public DuxStore(T initialState, Reducer<T> reducer) {
        this.state = initialState;
        this.reducer = reducer;
        this.timeTravel = new TimeTravel<>();
        this.timeTravel.recordChange(new Action<>(Utilities.INITIAL_ACTION, initialState));
        Runtime.getRuntime().addShutdownHook(new Thread(this::killProducer));
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
            this.timeTravel.recordChange(action);
            this.notifyListeners();
        }
    }

    private void dispatchNoNotify(Action action) {
        T newState = reducer.reduce(action, (T) state.clone());
        this.state = newState;
    }

    private void dispatchTimeTravel(Action action) {
        if(middleware != null) {
            middleware.run(this, this::dispatchNoNotify, action);
        } else {
            dispatchNoNotify(action);
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
        T initialState = this.timeTravel.getInitialState();
        this.state = initialState;
        List<Action> actionsToRecreateState = this.timeTravel.getActionHistory();
        for (Action action: actionsToRecreateState) {
            dispatchTimeTravel(action);
        }
        this.notifyListeners();
    }

    public void goForward() {
        this.timeTravel.goForward();
        Action latestAction = this.timeTravel.getLatestAction();
        T newState = reducer.reduce(latestAction, (T) state.clone());
        this.state = newState;
        this.notifyListeners();
    }

    public List<String> getActionHistory() {
        return this.timeTravel.getActionTypeHistory();
    }

    public String exportStore() {
        return this.gson.toJson(this.state);
    }

    public void importStore(String json, Type type) {
        T state = this.gson.fromJson(json, type);
        this.state = state;
        this.notifyListeners();
    }

    public void initializeKafkaProducer(Properties properties, String topic) {
        try {
            this.producer = new Producer(properties, topic);
            this.subscribe(x -> this.sendMessageToKafka());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void killProducer() {
        if(this.producer != null)
            this.producer.kill();
    }

    private void sendMessageToKafka(){
        try {
            if (producer != null) {
                producer.sendMessage(this.exportStore());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

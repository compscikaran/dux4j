package org.flux.store.api;

import lombok.Getter;

@Getter
public final class Action<T> {

    private final String type;
    private final T payload;

    public Action(String type, T payload) {
        this.type = type;
        this.payload = payload;
    }
}

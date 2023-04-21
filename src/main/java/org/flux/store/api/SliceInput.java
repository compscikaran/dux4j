package org.flux.store.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@AllArgsConstructor
@Getter
public class SliceInput<T extends State> {
    String name;
    T initialState;
    Map<String, Reducer<T>> reducers;
    List<Consumer<T>> subscribers;
}

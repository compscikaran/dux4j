package org.flux.store.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class SliceInput<T extends State> {
    String name;
    T initialState;
    Map<String, Reducer<T>> reducers;
}

package org.flux.store.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StoreBackup<T extends State> {

    private T currentState;
    private T latestSnapshot;
    private List<Action> actions = new ArrayList<>();
    private Integer currentIndex;
}

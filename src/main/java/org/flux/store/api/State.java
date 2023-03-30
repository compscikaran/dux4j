package org.flux.store.api;

import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ReflectionDiffBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public interface State extends Diffable<State>, Cloneable {

    @Override
    default DiffResult diff(State p) {
        return new ReflectionDiffBuilder<>(this, p, ToStringStyle.SIMPLE_STYLE)
                .build();
    }
}


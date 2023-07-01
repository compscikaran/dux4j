package org.flux.store.tests;

import org.flux.store.api.InvalidActionException;
import org.flux.store.main.DuxSlice;
import org.flux.store.main.DuxSliceBuilder;
import org.flux.store.tests.domain.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SliceBuilderTest {

    private DuxSlice<UserProfile> slice;

    @BeforeEach
    public void init() {
        this.slice = new DuxSliceBuilder<UserProfile>()
                .setInitialState(new UserProfile("Karan Gupta", "karan@hello.com"))
                .addReducer("setEmail", (action, state) -> {
                    state.setEmail(action.getPayload().toString());
                    return state;
                })
                .addReducer("setName", (action, state) -> {
                    state.setName(action.getPayload().toString());
                    return state;
                })
                .addSubscriber((state) -> System.out.println(state))
                .addSubscriber((state) -> System.out.println("State has changed lol.."))
                .build();
    }

    @Test
    public void canUpdateSliceWithValidAction() throws InvalidActionException {
        String newName = "Manoj Gupta";
        String newEmail = "manoj@hello.com";
        Consumer setEmail = slice.getAction("setEmail");
        Consumer setName = slice.getAction("setName");
        setName.accept(newName);
        setEmail.accept(newEmail);
        assertEquals(newName, slice.getState().getName());
        assertEquals(newEmail, slice.getState().getEmail());
    }

    @Test
    public void failUpdateSliceWithInvalidAction() {
        assertThrows(InvalidActionException.class, () -> slice.getAction("updateEmail"));
    }
}

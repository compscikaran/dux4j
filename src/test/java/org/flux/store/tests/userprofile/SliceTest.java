package org.flux.store.tests.userprofile;
import org.flux.store.api.Action;
import org.flux.store.api.Reducer;
import org.flux.store.api.SliceInput;
import org.flux.store.main.DuxSlice;
import org.flux.store.main.Utilities;
import org.flux.store.tests.userprofile.domain.User;
import org.flux.store.tests.userprofile.domain.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SliceTest {

    private DuxSlice<UserProfile> slice;

    @BeforeEach
    public void init() {
        this.slice = Utilities.createSlice(
                new SliceInput<>(
                        "slice1",
                        new UserProfile("Karan Gupta", "karan@hello.com"),
                        Map.ofEntries(
                                Map.entry("setEmail", (action, state) -> {
                                    state.setEmail(action.getPayload().toString());
                                    return state;
                                }),
                                Map.entry("setName", (action, state) -> {
                                    state.setName(action.getPayload().toString());
                                    return state;
                                })),
                        Arrays.asList(
                                (state) -> System.out.println(state),
                                (state) -> System.out.println("State has changed lol..")
                        )));
    }

    @Test
    public void canUpdateSliceWithValidAction() {
        String newName = "Manoj Gupta";
        String newEmail = "manoj@hello.com";
        slice.dispatch("setName", newName);
        slice.dispatch("setEmail", newEmail);
        assertEquals(newName, slice.getState().getName());
        assertEquals(newEmail, slice.getState().getEmail());
    }

    @Test
    public void failUpdateSliceWithInvalidAction() {
        String newEmail = "karan@gmail.com";
        assertThrows(IllegalArgumentException.class, () -> slice.dispatch("updateEmail", newEmail));
    }
}

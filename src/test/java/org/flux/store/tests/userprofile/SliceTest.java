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
                                }))));
    }

    @Test
    public void canUpdateSliceWithValidAction() {
        String newName = "Manoj Gupta";
        slice.dispatch("setName", newName);
        assertEquals(newName, slice.getState().getName());
    }

    @Test
    public void failUpdateSliceWithInvalidAction() {
        String newEmail = "karan@gmail.com";
        assertThrows(IllegalArgumentException.class, () -> slice.dispatch("updateEmail", newEmail));
    }
}

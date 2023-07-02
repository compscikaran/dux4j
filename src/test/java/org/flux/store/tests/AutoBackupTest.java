package org.flux.store.tests;

import org.flux.store.api.InvalidActionException;
import org.flux.store.api.Slice;
import org.flux.store.main.DuxSlice;
import org.flux.store.main.DuxSliceBuilder;
import org.flux.store.main.DuxStore;
import org.flux.store.tests.domain.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AutoBackupTest {
    private DuxSlice<UserProfile> slice;

    private static final String newName = "Manoj Gupta";

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
                .enableAsyncNotifications()
                .setBackupPath(System.getProperty("backupPath"))
                .enableAutoBackup()
                .build();
    }

    @Test
    public void canSaveAndRestoreState() throws InvalidActionException {
        Consumer setName = slice.getAction("setName");
        setName.accept(newName);
        slice.backupToFile();
        assertEquals(newName, slice.getState().getName());
        this.init();
        slice.restoreFromFile(UserProfile.class);
        assertEquals(newName, slice.getState().getName());
    }

}

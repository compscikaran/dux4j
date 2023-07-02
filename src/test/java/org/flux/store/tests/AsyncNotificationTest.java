package org.flux.store.tests;

import org.flux.store.api.Action;
import org.flux.store.main.DuxStore;
import org.flux.store.utils.Utilities;
import org.flux.store.tests.domain.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AsyncNotificationTest {
    public static final String INITIAL_EMAIL = "karan@hello.com";
    public static final String INITIAL_NAME = "Karan Gupta";
    public static final String ACTION_SET_EMAIL = "SET_EMAIL";
    public static final String ACTION_SET_NAME = "SET_NAME";

    private DuxStore<UserProfile> myStore;
    private boolean sampleState;

    @BeforeEach
    public void init() {
        UserProfile initialState = new UserProfile(INITIAL_NAME, INITIAL_EMAIL);
        myStore = new DuxStore<>(initialState, (action, state) -> {
            switch (action.getType()) {
                case ACTION_SET_EMAIL:
                    String newEmail = action.getPayload().toString();
                    state.setEmail(newEmail);
                    break;
                case ACTION_SET_NAME:
                    String newName = action.getPayload().toString();
                    state.setName(newName);
                    break;
                default:
                    throw new RuntimeException("Action Type not supported by reducer");
            }
            return state;
        });
        myStore.subscribe(x -> {
            try {
                Thread.sleep(5000);
                sampleState = true;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Test
    public void subsctibersRunAsync() {
        myStore.enableAsyncNotifications();
        String newEmail = "karan@gmail.com";
        Action<String> action = Utilities.actionCreator(ACTION_SET_EMAIL, newEmail);
        myStore.dispatch(action);
        assertEquals(false, sampleState);
        assertEquals(newEmail, myStore.getState().getEmail());
    }

    @Test
    public void subscribersRunSync() {
        String newEmail = "karan@gmail.com";
        Action<String> action = Utilities.actionCreator(ACTION_SET_EMAIL, newEmail);
        myStore.dispatch(action);
        assertEquals(true, sampleState);
        assertEquals(newEmail, myStore.getState().getEmail());
    }
}

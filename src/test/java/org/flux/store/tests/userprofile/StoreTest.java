package org.flux.store.tests.userprofile;

import org.flux.store.api.Action;
import org.flux.store.main.Utilities;
import org.flux.store.main.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {

    private Store<UserProfile> myStore;

    @BeforeEach
    public void init() {
        UserProfile initialState = new UserProfile("Karan", "karan@hello.com");
        myStore = new Store<>(initialState, (action, state) -> {
            UserProfile newState = state.clone();
            switch (action.getType()) {
                case "SET_EMAIL":
                    String newEmail = action.getPayload().toString();
                    newState.setEmail(newEmail);
                    break;
                default:
                    throw new RuntimeException("Action Type not supported by reudcer");
            }
            return newState;
        });
    }

    @Test
    public void canChangeEmail() {
        String newEmail = "karan@gmail.com";
        Action<String> action = Utilities.actionCreator("SET_EMAIL", newEmail);
        myStore.dispatch(action);
        UserProfile newState = myStore.getState();
        assertEquals(newEmail, newState.getEmail());
    }

    @Test
    public void canDetectEmailChange() {
        String newEmail = "karan@gmail.com";
        Action<String> action = Utilities.actionCreator("SET_EMAIL", newEmail);
        myStore.subscribe(x -> {
            System.out.println("State has changed...");
            assertNotNull(x);
        });
        myStore.dispatch(action);
    }
}

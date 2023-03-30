package org.flux.store.tests.userprofile;

import org.flux.store.api.Action;
import org.flux.store.main.Utilities;
import org.flux.store.main.Store;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {

    public static final String INITIAL_EMAIL = "karan@hello.com";
    public static final String INITIAL_NAME = "Karan";

    private Store<UserProfile> myStore;

    @BeforeEach
    public void init() {
        UserProfile initialState = new UserProfile(INITIAL_NAME, INITIAL_EMAIL);
        myStore = new Store<>(initialState, (action, state) -> {
            switch (action.getType()) {
                case "SET_EMAIL":
                    String newEmail = action.getPayload().toString();
                    state.setEmail(newEmail);
                    break;
                default:
                    throw new RuntimeException("Action Type not supported by reudcer");
            }
            return state;
        });
    }

    @Test
    public void canChangeEmail() {
        String newEmail = "karan@gmail.com";
        Action<String> action = Utilities.actionCreator("SET_EMAIL", newEmail);
        myStore.dispatch(action);
        assertEquals(newEmail, myStore.getState().getEmail());
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

    @Test
    public void canTravelBack() {
        String newEmail = "karan@gmail.com";
        Action<String> action = Utilities.actionCreator("SET_EMAIL", newEmail);
        myStore.dispatch(action);
        myStore.goBack();
        assertEquals(INITIAL_EMAIL, myStore.getState().getEmail());
    }

    @Test
    public void canTravelForward() {
        String newEmail = "karan@gmail.com";
        Action<String> action = Utilities.actionCreator("SET_EMAIL", newEmail);
        myStore.dispatch(action);
        myStore.goBack();
        myStore.goForward();
        assertEquals(newEmail, myStore.getState().getEmail());
    }
}

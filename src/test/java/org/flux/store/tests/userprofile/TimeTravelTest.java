package org.flux.store.tests.userprofile;

import org.flux.store.api.Action;
import org.flux.store.main.DuxStore;
import org.flux.store.main.Utilities;
import org.flux.store.tests.userprofile.domain.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TimeTravelTest {

    public static final String INITIAL_EMAIL = "karan@hello.com";
    public static final String INITIAL_NAME = "Karan Gupta";
    public static final String ACTION_SET_EMAIL = "SET_EMAIL";
    public static final String ACTION_SET_NAME = "SET_NAME";

    private DuxStore<UserProfile> myStore;

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
    }

    @Test
    public void canTravelBack() {
        String newEmail = "karan@gmail.com";
        String newEmail2 = "karan@hotmail.com";
        Action<String> action = Utilities.actionCreator(ACTION_SET_EMAIL, newEmail);
        Action<String> action2 = Utilities.actionCreator(ACTION_SET_EMAIL, newEmail2);
        System.out.println(myStore.getState());
        myStore.dispatch(action);
        myStore.dispatch(action2);
        myStore.goBack();
        myStore.goBack();
        System.out.println(myStore.getState());
        assertEquals(INITIAL_EMAIL, myStore.getState().getEmail());
    }

    @Test
    public void canTravelForward() {
        String newEmail = "karan@gmail.com";
        String newEmail2 = "karan@hotmail.com";
        Action<String> action = Utilities.actionCreator(ACTION_SET_EMAIL, newEmail);
        Action<String> action2 = Utilities.actionCreator(ACTION_SET_EMAIL, newEmail2);
        System.out.println(myStore.getState());
        myStore.dispatch(action);
        myStore.dispatch(action2);
        myStore.goBack();
        myStore.goBack();
        myStore.goForward();
        System.out.println(myStore.getState());
        assertEquals(newEmail, myStore.getState().getEmail());
    }

    @Test
    public void historyIsAccurate() {
        String newEmail = "manoj@gmail.com";
        String newName = "Manoj Gupta";
        Action<String> action = Utilities.actionCreator(ACTION_SET_EMAIL, newEmail);
        myStore.dispatch(action);
        Action<String> action2 = Utilities.actionCreator(ACTION_SET_NAME, newName);
        myStore.dispatch(action2);
        assertEquals(newEmail, myStore.getState().getEmail());
        assertEquals(newName, myStore.getState().getName());
        List<String> history = myStore.getActionHistory();
        assertTrue(history.size() == 3);
        assertEquals(ACTION_SET_EMAIL, history.get(1));
        assertEquals(ACTION_SET_NAME, history.get(2));
    }

    @Test
    public void canTravelHalfway() {
        String newEmail = "karan@gmail.com";
        String newEmail2 = "karan@hotmail.com";
        String newName = "Sandeep Rajwar";
        String newName2 = "Hardik Pathak";
        Action<String> action = Utilities.actionCreator(ACTION_SET_EMAIL, newEmail);
        Action<String> action3 = Utilities.actionCreator(ACTION_SET_EMAIL, newEmail2);
        Action<String> action2 = Utilities.actionCreator(ACTION_SET_NAME, newName);
        Action<String> action4 = Utilities.actionCreator(ACTION_SET_NAME, newName2);
        System.out.println(myStore.getState());
        Arrays.asList(action, action2, action3, action4).forEach(x -> myStore.dispatch(x));
        myStore.goBack();
        myStore.goBack();
        myStore.goBack();
        myStore.goBack();
        myStore.goForward();
        myStore.goForward();
        System.out.println(myStore.getState());
        assertEquals(newEmail, myStore.getState().getEmail());
        assertEquals(newName, myStore.getState().getName());
    }

}

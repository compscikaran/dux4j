package org.flux.store.tests.userprofile;

import com.google.gson.Gson;
import okhttp3.*;
import org.flux.store.api.Action;
import org.flux.store.api.Thunk;
import org.flux.store.main.DuxStore;
import org.flux.store.main.Utilities;
import org.flux.store.tests.userprofile.domain.User;
import org.flux.store.tests.userprofile.domain.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

public class ThunkTest {


    public static final String ACTION_SET_USER = "SET_USER";
    public static final String SAMPLE_NAME = "Leanne Graham";
    public static final String SAMPLE_EMAIL = "Sincere@april.biz";
    private DuxStore<UserProfile> myStore;
    private OkHttpClient client;

    @BeforeEach
    public void init() {
        myStore = new DuxStore<>(new UserProfile("", ""), (action, state) -> {
            switch (action.getType()) {
                case ACTION_SET_USER:
                    UserProfile newUser = (UserProfile)action.getPayload();
                    state.setEmail(newUser.getEmail());
                    state.setName(newUser.getName());
                    break;
            }
            return state;
        });

        client = new OkHttpClient();
    }

    @Test
    public void canFetchAndUpdateUserFromAPI() throws IOException, ExecutionException, InterruptedException {

        Thunk<UserProfile> updateUserFromAPI = (dispatch, getState) -> {
            System.out.println("Old State: " + getState.get().toString());
            Request request = new Request.Builder()
                        .url("https://jsonplaceholder.typicode.com/users/1")
                        .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                public void onResponse(Call call, Response response) throws IOException {
                    Gson gson = new Gson();
                    User user = gson.fromJson(response.body().string(), User.class);
                    Action<UserProfile> newUser = Utilities.actionCreator(ACTION_SET_USER,
                            new UserProfile(user.getName(), user.getEmail()));
                    dispatch.accept(newUser);
                }

                public void onFailure(Call call, IOException e) {
                    System.out.println("API Call failure simulating response");
                    Action<UserProfile> newUser = Utilities.actionCreator(ACTION_SET_USER,
                            new UserProfile(SAMPLE_NAME, SAMPLE_EMAIL));
                    dispatch.accept(newUser);
                }
            });
        };
        myStore.dispatch(updateUserFromAPI);
        Thread.sleep(5000);
        System.out.println("New State: " + myStore.getState());
        assertEquals(SAMPLE_NAME, myStore.getState().getName());
        assertEquals(SAMPLE_EMAIL, myStore.getState().getEmail());
    }

}

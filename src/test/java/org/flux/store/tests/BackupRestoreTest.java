package org.flux.store.tests;

import org.flux.store.api.Reducer;
import org.flux.store.main.DuxStore;
import org.flux.store.utils.Utilities;
import org.flux.store.tests.domain.Author;
import org.flux.store.tests.domain.Book;
import org.flux.store.tests.domain.CombinedState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BackupRestoreTest {


    public static final String INITIAL_BOOK = "The Making of a manager";
    public static final String INITIAL_AUTHOR = "Julie Zhou";
    public static final String ACTION_SET_BOOK = "SET_BOOK";
    public static final String ACTION_SET_AUTHOR = "SET_AUTHOR";

    private DuxStore<CombinedState> myStore;

    @BeforeEach
    public void init() {

        CombinedState initialState = new CombinedState(new Book(INITIAL_BOOK),new Author(INITIAL_AUTHOR));

        Reducer<CombinedState> reducer = (action, state) -> {
            switch (action.getType()) {
                case ACTION_SET_AUTHOR:
                    Author author = (Author) action.getPayload();
                    state.setAuthor(author);
                    break;
                case ACTION_SET_BOOK:
                    Book book = (Book) action.getPayload();
                    state.setBook(book);
                    break;
            }
            return state;
        };
        myStore = new DuxStore<>(initialState, reducer);
    }

    @Test
    public void canExportIntermediateState() {
        String newBook = "Dark Matter";
        myStore.dispatch(Utilities.actionCreator(ACTION_SET_BOOK, new Book(newBook)));
        String json = myStore.exportStore();
        System.out.println(json);
        assertTrue(json.contains(newBook));
    }

    @Test
    public void canRestoreStateFromJson() {
        String json = "{\"book\":{\"name\":\"Recursion\"},\"author\":{\"name\":\"Blake Crouch\"}}";
        String newBook = "Recursion";
        String newAuthor = "Blake Crouch";
        myStore.importStore(json, CombinedState.class);
        assertEquals(newBook, myStore.getState().getBook().getName());
        assertEquals(newAuthor, myStore.getState().getAuthor().getName());
    }
}

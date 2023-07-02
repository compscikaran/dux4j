package org.flux.store.tests;

import org.flux.store.api.Reducer;
import org.flux.store.main.DuxStore;
import org.flux.store.utils.Utilities;
import org.flux.store.tests.domain.Author;
import org.flux.store.tests.domain.Book;
import org.flux.store.tests.domain.CombinedState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestoreHistoryTest {

    public static final String INITIAL_BOOK = "The Making of a manager";
    public static final String INITIAL_AUTHOR = "Julie Zhou";
    public static final String ACTION_SET_BOOK = "SET_BOOK";
    public static final String ACTION_SET_AUTHOR = "SET_AUTHOR";

    private DuxStore<CombinedState> myStore;

    private String backup;

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
    public void goBackWithoutErrorAfterRestore() {
        String newBook = "Dark Matter";
        myStore.dispatch(Utilities.actionCreator(ACTION_SET_BOOK, new Book(newBook)));
        String newAuthor = "Blake Crouch";
        myStore.dispatch(Utilities.actionCreator(ACTION_SET_AUTHOR, new Author(newAuthor)));
        String json = myStore.exportStore();
        backup = json;
        this.init();
        myStore.importStore(backup, CombinedState.class);
        String newBook2 = "Recursion";
        myStore.dispatch(Utilities.actionCreator(ACTION_SET_BOOK, new Book(newBook2)));
        System.out.println(myStore.getState());
        myStore.goBack();
        System.out.println(myStore.getState());
        assertEquals(newBook, myStore.getState().getBook().getName());
        assertEquals(newAuthor, myStore.getState().getAuthor().getName());
    }
}

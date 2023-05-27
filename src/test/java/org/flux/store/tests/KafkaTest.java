package org.flux.store.tests;

import org.flux.store.api.Action;
import org.flux.store.api.Reducer;
import org.flux.store.main.DuxStore;
import org.flux.store.main.Utilities;
import org.flux.store.tests.domain.Author;
import org.flux.store.tests.domain.Book;
import org.flux.store.tests.domain.CombinedState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

public class KafkaTest {

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
        myStore.subscribe(System.out::println);
    }

    @Test
    public void canSendFullFeedToKafkaTopic() {
        Properties properties = new Properties();
        String jaasConfig = System.getProperty("jaas.config");
        String cluster = System.getProperty("bootstrap.servers");
        String topicName = System.getProperty("kafka.topic");
        properties.setProperty("security.protocol", "SASL_SSL");
        properties.setProperty("sasl.mechanism", "PLAIN");
        properties.setProperty("sasl.jaas.config", jaasConfig);
        properties.setProperty("bootstrap.servers", cluster);
        myStore.initializeKafkaProducer(properties, topicName);
        String newBook = "Invent and Wander";
        String newAuthor = "Jeff Bezos";
        Action<Author> action1 = Utilities.actionCreator(ACTION_SET_AUTHOR, new Author(newAuthor));
        Action<Book> action2 = Utilities.actionCreator(ACTION_SET_BOOK, new Book(newBook));
        myStore.dispatch(action1);
        myStore.dispatch(action2);
    }

}

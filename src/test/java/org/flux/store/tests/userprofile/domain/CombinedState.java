package org.flux.store.tests.userprofile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.flux.store.api.State;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CombinedState implements State {
    private Book book;
    private Author author;

    @Override
    public CombinedState clone() {
        try {
            return (CombinedState) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "CombinedState{" +
                "book=" + book.getName() +
                ", author=" + author.getName() +
                '}';
    }
}

package org.flux.store.tests.userprofile.domain;

import lombok.*;
import org.flux.store.api.State;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserProfile implements State {
    private String name;
    private String email;

    @Override
    public UserProfile clone() {
        try {
            return (UserProfile) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}

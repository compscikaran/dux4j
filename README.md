# DuxJava

![workflow](https://github.com/compscikaran/dux-java/actions/workflows/ci-build.yml/badge.svg)

Redux like unidirectional state management implementation for Java.

## How the idea came about

The idea came about when I had finished Dan Abramov's interview and demo of redux, and I thought to myself why this kind of pattern does not exist in java.
So I set out to implement the same in the language I use everyday.

------------------------
## How to install it

PENDING

## How to use it

1. Create class representing your Application State
```java
@Getter
@Setter
@AllArgsConstructor
public class UserProfile implements State {
    private String name;
    private String email;

    @Override
    public UserProfile clone() {
     //...
    }
}

```
2. Create a new Store by passing in initial state and a Reducer function
```java
Store<UserProfile> myStore = new Store<>(initialState, (action, state) -> {
            switch (action.getType()) {
                case "SET_EMAIL":
                    //...
                    break;
                case "SET_NAME": 
                    //...
                    break;
            }
            return newState;
        });
```
3. Dispatching Actions
```java
myStore.dispatch(new Action<String>("SET_EMAIL", "karan@gmail.com"));
```

## Usefull Utilities

Adding subscribers functions to store
```java
myStore.subscribe(x -> {
    System.out.println("State has changed...");
});
```
Creating Actions using actionCreator
```java
Action<String> action = Utilities.actionCreator("SET_EMAIL", newEmail);
```
Combining Reducers
```java
Reducer<UserProfile> combined = Utilities.combineReducer(<reducer 1>, <reducer 2>...);
```

## Time Travel Debugging

While you are debugging your application you can move backwards and forwards through the application.
This works by saving a complete history of states

```java
myStore.goBack();

myStore.goForward();
```
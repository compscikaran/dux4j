# DuxJava

![workflow](https://github.com/compscikaran/dux-java/actions/workflows/ci-build.yml/badge.svg)

Redux like unidirectional state management implementation for Java.

## How the idea came about

The idea came about when I had finished Facebook's Talk on Flux Architecture and seen few of Dan Abramov's interviews, and I thought to myself why this kind of pattern does not exist in java.
So I set out to implement the same in the language I use everyday.

------------------------

## What is DuxJava

Here is some documentation - https://github.com/compscikaran/dux-java/wiki

Key Features -
1. Simple Redux like Unidirectional application store
2. Supports all the familiar patterns such as reducers, subscribers, actions, action creators, thunks, middlewares
3. Time travel debugging which allows to go to any previous or forward state in the store's history
4. Allow backup and restore of application state and syncing application state to persistant storage
5. Native Kafka Producer built in to stream state changes to specified Kafka topic

## How to install it

Add GitHub maven repository to your pom.xml
```xml
<distributionManagement>
    <repository>
        <id>github</id>
        <name>GitHub compscikaran Apache Maven Packages</name>
        <url>https://maven.pkg.github.com/compscikaran/dux-java</url>
    </repository>
</distributionManagement>
```

Add the library as a maven dependency
```xml
<dependency>
    <groupId>org.compscikaran</groupId>
    <artifactId>dux-java</artifactId>
    <version>get the latest version from packages section</version>
</dependency>
```

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

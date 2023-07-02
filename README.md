# DuxJava

![workflow](https://github.com/compscikaran/dux-java/actions/workflows/ci-build.yml/badge.svg)

Redux like unidirectional state management implementation for Java.

## How the idea came about

The idea came about when I had finished Facebook's Talk on Flux Architecture and seen few of Dan Abramov's interviews, and I thought to myself why this kind of pattern does not exist in java.
So I set out to implement the same in the language I use everyday.

------------------------

## What is DuxJava

Get Started - https://github.com/compscikaran/dux-java/wiki

Key Features -
1. Simple Redux like Unidirectional application store
2. Supports all the familiar patterns such as reducers, subscribers, actions, action creators, thunks, middlewares
3. Time travel debugging which allows to go to any previous or forward state in the store's history
4. Allow backup and restore of application state and syncing application state to persistant storage
5. Native Kafka Producer built in to stream state changes to specified Kafka topic

# mars

[![Build Status](https://travis-ci.org/rodolfodpk/mars.svg?branch=master)](https://travis-ci.org/rodolfodpk/mars)
[![codecov](https://codecov.io/gh/rodolfodpk/mars/branch/master/graph/badge.svg)](https://codecov.io/gh/rodolfodpk/mars)

## How to run it

```
mvn clean install
java -jar mars-rest/target/mars-rest.jar 
```

Then in another window (this app will talk with REST api)
```
cd mars-console
mvn exec:java
```

## These rules are assumed

* Plateau can resize to up and down, no mater how many rovers are within it.
* A rover cannot move to a cell already occupied by another rover.

## Inspiration

* http://vvgomes.com/cqrs-and-rest/
* https://en.wikipedia.org/wiki/Post/Redirect/Get
* http://codebetter.com/gregyoung/2010/07/12/command-handlers-and-the-domain-model/
* http://restcookbook.com/HTTP%20Methods/put-vs-post/
* https://www.thoughtworks.com/insights/blog/rest-api-design-resource-modeling

## Random notes

* It tries CQRS but not Event Sourcing
* Integration tests for REST app are included
* Acceptance tests for REST app would be much easier with [JUnit 5 execution order](https://junit.org/junit5/docs/snapshot/user-guide/#writing-tests-test-execution-order)
since with Vert.x WebClient I would reach a callback hell. I didn't tried to use the JUnit 5 snapshot.
* I tried to use [Vertx's OpenApi contract](https://vertx.io/docs/vertx-web-api-contract/java/) but gave up since i'm not proficient with it yet. 
* Error and complex cases are probably still untested (only happy ending scenarios are tested so far)
* Sometimes I really was not careful with commit messages 
* Core and Rest modules has reasonable test coverage but Console is missing a lot.
* ~~Console app output is still in JSON format. More work is needed to render the text format required by the challenge. And I just realized REST app could generate responses with the text content-type instead of json~~. *update* text media type added. 
## Developer notes

### To run the rest app with code changes reload:

``` 
mvn clean install
cd mars-rest
mvn vertx:run
```

### Javadocs are avaliable:

``
mvn javadocs:javadocs
``


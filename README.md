# mars


[![Build Status](https://travis-ci.org/rodolfodpk/mars.svg?branch=master)](https://travis-ci.org/rodolfodpk/mars)
[![codecov](https://codecov.io/gh/rodolfodpk/mars/branch/master/graph/badge.svg)](https://codecov.io/gh/rodolfodpk/mars)

## How to run it

```
mvn clean install

```

## These rules are assumed:
* plateau can resize to up and down, no mater how many rovers are within it.
* a rover cannot move to a cell already occupied by another rover.

# links
* http://vvgomes.com/cqrs-and-rest/
* https://www.thoughtworks.com/insights/blog/rest-api-design-resource-modeling
* http://codebetter.com/gregyoung/2010/07/12/command-handlers-and-the-domain-model/
* http://restcookbook.com/HTTP%20Methods/put-vs-post/

PUT /plateau/xy
PUT /plateau/xy/rover/xy moveCommand as Json


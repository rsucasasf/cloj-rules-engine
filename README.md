# cloj-rules-engine

[![Build Status](https://travis-ci.org/rsucasasf/cloj-rules-engine.svg?branch=master)](https://travis-ci.org/rsucasasf/cloj-rules-engine)

**cloj-rules-engine** is a very simple rules engine written in Clojure and designed to work with Java.

-----------------------

[Description](#description)

[Prerequisites](#prerequisites)

[Usage](#usage)

[Mini tutorials and links - Clojure, Travis](#mini-tutorials-and-links)

[License](#license)

-----------------------

## Description

....

-----------------------

## Prerequisites

1. Java version 8

2. [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

-----------------------

## Usage

### From Clojure


### From Java

-----------------------

## Mini tutorials and links

### Clojure
[Readable Clojure](http://tonsky.me/blog/readable-clojure/) : "*This is how you can make Clojure code more pleasant to work with*"

### Integration with Travis

1. Enable github project in [Travis](https://travis-ci.org/profile) (entering as the github user)

2. Add a *.travis.yml* file in the project's root folder with the following content:

```
language: clojure

script:
- lein test
```

3. Use Travis to synchronize the project and execute the tests or do a *push*

4. Take the url of the image (i.e., *build | passing*) from Travis (in the project) and add it to the README.md file

```
[![Build Status](https://travis-ci.org/rsucasasf/cloj-rules-engine.svg?branch=master)](https://travis-ci.org/rsucasasf/cloj-rules-engine)
```

-----------------------

## License

Distributed under the Eclipse Public License either version 1.0 or any later version.

# cloj-rules-engine

[![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0)
[![Build Status](https://travis-ci.org/rsucasasf/cloj-rules-engine.svg?branch=master)](https://travis-ci.org/rsucasasf/cloj-rules-engine)

**cloj-rules-engine** is a very simple rules engine written in [Clojure](https://clojure.org/) and designed to work with Java.

-----------------------

**Table of Contents**

- [Description](#description)
  - Things to do / limitations
- [Prerequisites](#prerequisites)
- [Usage](#usage)
- [Mini tutorials and links](#mini-tutorials-and-links)
  - Clojure
  - Travis
  - Integration with Travis
  - Markdown tips and links
  - Export to maven (local repo)
- [License](#license)

-----------------------

## Description

**cloj-rules-engine** is a rules engine written in Clojure. Features:

- Each rule has a condition (composed of conditional expressions -written in Clojure- that refer to facts), and a set of actions that are activated if the condition is satisfied. Facts are the data upon which rules operate.

- Rules are expressed in a simple and easy to read Clojure format ([clojure *maps*](http://www.deadcoderising.com/2015-04-clojure-basics-dealing-wit-maps/))

```
{
  :RULE_1 {:cond "(and (< #A 10) (> #B 50))"
           :actions ["action-1"]}
  ...
}
```

- Facts can be expressed via clojure maps (Clojure) or via PersistentArrayMap objects (Java):

```
(update-map-facts {"#A" "14"})
```

```
PersistentArrayMap facts_map = new PersistentArrayMap(new Object[] {
		"#A", "14"
	});
clrules.updateMapFacts(facts_map);
```

- This library can be used from Java or Clojure code

- Main methods:
  - **initialize** loads rules map from absolute or relative path. Returns *true* if everything is okay.
  ```
  (initialize "rules.clj")
  ```

  ```
  clrules.initialize("rules.clj");
  ```

  - **update-map-facts** update / initialize facts
  ```
  (update-map-facts {"#A" "14"})
  ```

  ```
  clrules.updateMapFacts(facts_map);
  ```

  - **get-rules-actions** evaluates rules based on current facts, and return a list (String) of 'fired' actions
  ```
  (get-rules-actions)
  ```

  ```
  clrules.getRulesActions();
  ```

  - **get-fired-rules**

### Things to do / limitations

- When creating the set of rules, use a hash for each of the parameters (i.e. *#A* and *#B*):

```
:RULE_1 {:cond "(and (< #A 10) (> #B 50))"
         :actions ["action-1"]}
```

- The set of rules are defined using Clojure syntax => Clojure maps

- Conditions are clojure expressions surrounded by quotes.

- If a rule is evaluated and 'fired', it won't be fired until facts are updated. In order to get all the 'fired' rules, call the **get-fired-rules** method / function 

-----------------------

## Prerequisites

1. Java version 8

2. [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

-----------------------

## Usage

First, define a set of rules (*"rules.clj"*):

```
{
  ;; RULE_1
  :RULE_1 {:cond "(and (< #A 10) (> #B 50))"
           :actions ["action-1"]
           :desc "Rule description: 'launch' action-1 if 'a' is lower than 10 and if 'b' is greater than 50"}
  ;; RULE_2
  :RULE_2 {:cond "(> #A 10)"
           :actions ["action-2"]
           :desc "Rule description: 'launch' action-2 and action-C if ..."}
}
```
And then, ...

### From Clojure

```
(initialize "rules.clj")

(update-map-facts {"#A" "14"})

(get-rules-actions)

(get-fired-rules)
```

### From Java

1. [Create a jar or add dependency to maven](#export-to-maven-local-repo)

2. Java code to use the library:

```
cloj_rules_engine.ClojRules clrules = new cloj_rules_engine.ClojRules();
...
clrules.initialize("rules.clj");

PersistentArrayMap facts_map = new PersistentArrayMap(new Object[] {
		"#A", "5",
		"#B", "51"
	});

clrules.updateMapFacts(facts_map);

clrules.getRulesActions();

clrules.getFiredRules();  // get fired rules in json format

```

-----------------------

## Mini tutorials and links

### Clojure
[Readable Clojure](http://tonsky.me/blog/readable-clojure/) : "*This is how you can make Clojure code more pleasant to work with*"

[Deploying to Maven Central](DEPLOY.md), taken from https://github.com/technomancy/leiningen

### Travis

[Getting Started](https://docs.travis-ci.com/user/getting-started/)

[Customizing the Build](https://docs.travis-ci.com/user/customizing-the-build/)

[Building a Clojure project](https://docs.travis-ci.com/user/languages/clojure/)

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

### Markdown tips and links

[Markdown License badges](https://gist.github.com/lukas-h/2a5d00690736b4c3a7ba)

Other badges from [shields.io](https://shields.io/)


### Export to maven local repo

- Create jar:

> lein uberjar

- Create artifact in local repo:

> cd target

> cd uberjar

> mvn install:install-file -Dfile=cloj-rules-engine-0.1.1-standalone.jar -DgroupId=cloj-libs -DartifactId=cloj-rules-engine -Dversion=0.1.1 -Dpackaging=jar

- Use in a Java project:
    - Add to maven dependencies:

```
<dependency>
  <groupId>cloj-libs</groupId>
  <artifactId>cloj-rules-engine</artifactId>
  <version>0.1.1</version>
</dependency>
```

-----------------------

## License

Copyright © 2017

Distributed under the Eclipse Public License, the same as Clojure.

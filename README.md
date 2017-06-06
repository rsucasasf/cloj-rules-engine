# cloj-rules-engine

[![Build Status](https://travis-ci.org/rsucasasf/cloj-rules-engine.svg?branch=master)](https://travis-ci.org/rsucasasf/cloj-rules-engine)
[![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0)
[![GitHub release](https://img.shields.io/badge/version-0.1.1--pre-yellowgreen.svg)](https://github.com/rsucasasf/cloj-rules-engine/releases/tag/0.1.1-alpha)
[![Codecov](https://img.shields.io/codecov/c/github/rsucasasf/cloj-rules-engine.svg)](https://codecov.io/gh/rsucasasf/cloj-rules-engine)

**cloj-rules-engine** is a very simple rules engine written in [Clojure](https://clojure.org/) and designed to work with Java.

-----------------------

**Table of Contents**

- [Description](#description)
  - Things to do / limitations
- [Prerequisites](#prerequisites)
- [Usage](#usage)
- [Complex Rules](#complex-rules)
- [License](#license)

-----------------------

## Description

**cloj-rules-engine** is a rules engine written in Clojure.

![Rules Engine](doc/rules-engine.png)


**Features**:

- Each **rule** has a **condition** (composed of conditional expressions -written in Clojure- that refer to facts), and a set of **actions** that are activated if the condition is satisfied. **Facts** are the data upon which rules operate. The fired actions are a represented as a set of identifiers (strings).

- Rules are expressed in a simple and easy to read Clojure format ([clojure *maps*](http://www.deadcoderising.com/2015-04-clojure-basics-dealing-wit-maps/))

```clojure
{
  :RULE_1 {:cond "(and (< #A 10) (> #B 50))"
           :actions ["action-1"]}
  ...
}
```

- Facts can be expressed via clojure maps (Clojure) or via PersistentArrayMap objects (Java):

```clojure
(update-map-facts {"#A" "14"})
```

```java
PersistentArrayMap facts_map = new PersistentArrayMap(new Object[] {
		"#A", "14"
	});
clrules.updateMapFacts(facts_map);
```

- This library can be used from Java or Clojure code

- Third party libraries used in this project:

| Libs                      | Version       | License                                   |
| ------------------------- |:-------------:| -----------------------------------------:|
| [clojure](https://clojure.org/) | 1.8.0 | [![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0) |
| [tools.logging](https://github.com/clojure/tools.logging)  | 0.3.1   | [![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0) |
| [log4j](http://logging.apache.org/log4j/1.2/)  | 1.2.17  | [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) |
| [data.json](https://github.com/clojure/data.json)   | 0.2.6   | [![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0) |
| [proto-repl](https://github.com/jasongilman/proto-repl) | 0.3.1   | [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT) |
| [math.numeric-tower](https://github.com/clojure/math.numeric-tower/) | 0.0.4 | [![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0) |

- Main methods:
  - **initialize** loads rules map from absolute or relative path. Returns *true* if everything is okay.

  ```clojure
  (initialize "rules.clj")
  ```

  ```java
  clrules.initialize("rules.clj");
  ```

  - **update-map-facts** update / initialize facts

  ```clojure
  (update-map-facts {"#A" "14"})
  ```

  ```java
  clrules.updateMapFacts(facts_map);
  ```

  - **get-rules-actions** evaluates rules based on current facts, and return a list (String) of 'fired' actions

  ```clojure
  (get-rules-actions)
  ```

  ```java
  clrules.getRulesActions();
  ```

  - **get-fired-rules**

  - **initialize-from-json**


### Things to do / limitations

- (**RULES DEFINITION**) The set of rules are defined using Clojure syntax => Clojure maps. Parameters / facts have the following formatt: #*FACTNAME*
  - No underscores allowed.
  - Regular expression used to validate fact / parameter names: `#"\#[A-Za-z][A-Za-z0-9]*"`


- (**RULES EVALUATION**) The rules and facts are evaluated following the steps of the next example:

1. In the example we have 2 facts or parameters: *#A* and *#B*

2. Rules conditions:

```clojure
:RULE_1 {:cond "(and (< #A 10) (> #B 50))"
         :actions ["action-1"]}
```

3. Facts are set or updated

```clojure
(update-map-facts {"#A" 33, "#B" 66}))
```

4. Rules conditions are transformed to clojure syntax in the following way:

```clojure
(when (and (< 33 10) (> 66 50)) :RULE_1)
```

5. If condition is satisfied (using clojure *eval* function inside **get-rules-actions** method), rule is tagged as fired


- (**RULES DEFINITION**) Conditions are clojure expressions surrounded by quotes.

- (**RULES DEFINITION**) When creating the set of rules, use a hash for each of the parameters / facts (i.e. *#A* and *#B*):

```clojure
:RULE_1 {:cond "(and (< #A 10) (> #B 50))"
         :actions ["action-1"]}
```

- (**RULES DEFINITION**) Use `str` function, single quotes or double quotes (and escape character) if you want to eval String variables.

```clojure
:RULE_5 {:cond "(= (str #D) (str 50))"
         :actions ["action-E"]}
:RULE_6 {:cond "(= #D \"goldenaxe\")"
         :actions ["action-F"]}
:RULE_7 {:cond "(= #D 'goldenaxe2')"
        :actions ["action-G"]}
```

- (**TESTING FACTS**) When creating / updating facts, escape string values that will be used as string

```clojure
(update-map-facts {"#A" 15, "#D" "\"goldenaxe\""}))
```

- If a rule is evaluated and 'fired', it won't be fired until facts are updated. In order to get all the 'fired' rules, call the **get-fired-rules** method / function

-----------------------

## Prerequisites

1. Java version 8

2. [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

-----------------------

## Usage

First, define a set of rules (*"rules.clj"*):

```clojure
{
  :RULE_1 {:cond "(and (< #A 10) (> #B 50))"
           :actions ["action-1"]
           :desc "Rule description: 'launch' action-1 if 'a' is lower than 10 and if 'b' is greater than 50"}

  :RULE_2 {:cond "(> #A 10)"
           :actions ["action-2"]}
}
```

And then, ...

### From Clojure

```clojure
(initialize "rules.clj")
(update-map-facts {"#A" "14"})
(get-rules-actions)
(get-fired-rules)
```

Or...

```clojure
(if (initialize "rules.clj")
  (when (update-map-facts {"#A" "15", "#B" 13, "#D" "\"goldenaxe\""})
    (get-rules-actions))
  false)
```

### From Java

1. [Create a jar or add dependency to maven](#export-to-maven-local-repo)

2. Java code to use the library:

```java
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

## Complex Rules

- You can use functions from [clojure.math.numeric-tower](https://clojure.github.io/math.numeric-tower/) when defining rules: `expt`, `abs`, `gcd`, `lcm`, `floor` ...

```clojure
:RULE_8 {:cond "(> (sqrt #C) 10)"
         :actions ["action-H-sqrt"]
         :desc "Rule description: 'launch' action-H-sqrt if square root of #C is greater than 10."}
```

- You can also use clojure functions (from **org.clojure/clojure**) that return boolean values: `every?`, `even?`, `odd?` ...

```clojure
:RULE_9 {:cond "(every? even? (list #A #B #C))"
         :actions ["action-I-even?"]
         :desc "Rule description: 'launch' action-I-even? if all elements from list are even."}
```

- Or custom functions: `#(> % 10)`

```clojure
:RULE_10 {:cond "(every? #(> % 10) [#A #B #C])"
          :actions ["action-J-func"]
          :desc "Rule description: 'launch' action-J-func if all elements from list / vector are greater than 10"}
```

- (**warning**: not ready yet - only works with **string** vectors or lists) Use lists or vectors as parameters:

```clojure
:RULE_11 {:cond "(every? #(> % 100) #LIST1)"
          :actions ["action-K-func"]
          :desc "Rule description: 'launch' action-K-func if all elements from list / vector '#LIST1' are greater than 10"}
```

```clojure
(update-map-facts {"#A" "21", "#B" 43, "#C" 1000, "#LIST1" "[121 321 123 122 1233]"})
```

-----------------------

## License

Copyright © 2017 Roi Sucasas Font

Distributed under the Eclipse Public License, the same as Clojure.

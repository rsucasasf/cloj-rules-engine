(ns cloj-rules-engine.rules-mng-java
  "Rules-engine library used by Java"
  (:require [cloj-rules-engine.conds-eval :as conds-eval]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.common :as common])
  (:gen-class
    :name cloj_rules_engine.ClojRules
    :state state
    :init init
    :prefix "-"
    :main false
    :methods [[initialize [String] boolean]
              [updateMapValues [clojure.lang.PersistentArrayMap] void]
              [getRulesActions [] java.util.ArrayList]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PUBLIC FUNCTIONS:

;; FUNCTION:-init
;; Set defaults
(defn -init []
  "store fields: values & rules"
  [[] (atom {:values {}
             :rules {}})])

;; FUNCTION: initialize
;; 'rules-file': relative/absolute path of the rules-file
(defn -initialize "Intializes atom variables"
  [this rules-file]
  (common/set-field this :rules (common/read-content rules-file)))

;; FUNCTION: updateMapValues
(defn -updateMapValues "Updates values-map content"
  [this m]
  (common/set-field this :values m))

;; FUNCTION: getRulesActions
;; Returns an ArrayList of Strings, where each of them is an action identifier (:actions ["id" ] ===> "id")
(defn -getRulesActions "Returns an ArrayList of Strings, where each of the items is an action identifier"
  [this]
  (try
    (java.util.ArrayList.
      (remove nil?
        (distinct
          (let [rules-map   (common/get-field this :rules)
                values-map  (common/get-field this :values)]
            (apply  concat
                    (for [x (conds-eval/eval-conditions (conds-eval/gen-conds-map rules-map) values-map)]
                      (get-in rules-map [x :actions])))))))
    (catch Exception e
      (do (logs/log-exception e) nil))))

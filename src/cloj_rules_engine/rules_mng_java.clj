(ns cloj-rules-engine.rules-mng-java
  "Rules-engine library used by Java"
  (:require [cloj-rules-engine.conds-eval :as ce])
  (:use [cloj-rules-engine.common])
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
;; when we are created we can set defaults if we want.
(defn -init []
  "store our fields: map-values & rules"
  [[] (atom {:map-values {}
             :rules {}})])

;; FUNCTION:initialize
;; 'rules-file': relative/absolute path of the rules-file
(defn -initialize "Intializes atom variables"
  [this rules-file]
  (set-field this :rules (read-content rules-file)))

;; FUNCTION:updateMapValues
;;
(defn -updateMapValues ""
  [this m]
  (set-field this :map-values m))

;; FUNCTION:getRulesActions
;; Returns an ArrayList of Strings, where each of them is an action identifier (:actions ["id" ] ===> "id")
(defn -getRulesActions "Returns an ArrayList of Strings, where each of them is an action identifier"
  [this]
  (java.util.ArrayList.
    (remove nil?
      (distinct
        (apply concat (for [x (ce/eval-conditions (ce/gen-conds-map (get-field this :rules)) (get-field this :map-values))]
                        (get-in (get-field this :rules) [x :actions])))))))

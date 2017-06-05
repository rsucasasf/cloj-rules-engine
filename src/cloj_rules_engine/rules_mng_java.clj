(ns cloj-rules-engine.rules-mng-java
  "Rules-engine library used by Java"
  (:use [clojure.math.numeric-tower])
  (:require [cloj-rules-engine.conds-eval :as conds-eval]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.common :as common]
            [clojure.data.json :as json])
  (:gen-class
    :name cloj_rules_engine.ClojRules
    :state state
    :init init
    :prefix "-"
    :main false
    :methods [[initialize [String] boolean]
              [initializeFromJson [String] boolean]
              [updateMapFacts [clojure.lang.PersistentArrayMap] void]
              [getRulesActions [] java.util.ArrayList]
              [getFiredRules [] java.util.ArrayList]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION:-init
;; Set defaults
(defn -init []
  "store fields: values, conds & rules"
  [[] (atom {:values {}
             :rules {}
             :conds {}})])

;; FUNCTION: initialize
;; 'rules-file': rules file (relative/absolute path of the rules-file)
(defn -initialize "Intializes rules and conditions map"
  [this rules-file]
  (if-let [rules-map-content (common/read-content rules-file)]
    (do
      (common/set-field this :rules rules-map-content)
      (common/set-field this :conds (conds-eval/gen-conds-map (common/get-field this :rules)))
      true)
    false))

;; FUNCTION: initialize-from-json
;; (json/read-str json-map-str :key-fn keyword)
(defn -initializeFromJson "Intializes rules and conditions map from json string"
  [this json-map-str]
  (try
    (do
      (common/set-field this :rules (json/read-str json-map-str :key-fn keyword))
      (common/set-field this :conds (conds-eval/gen-conds-map (common/get-field this :rules)))
      true)
    (catch Exception e
      (do (logs/log-exception e) false))))

;; FUNCTION: updateMapFacts
(defn -updateMapFacts "Updates values-map (facts) content"
  [this map-values]
  (let [m (common/parse-m map-values)]
    (if-not (every? common/valid? m)
      (do (logs/log-warning "Fact map's values are not valid") false)
      (do
        ; reset / update facts
        (common/set-field this :values m)
        ; creates new map with fired attribute set to false
        (common/set-field this :rules
          (common/add-new-attr-to-map (common/get-field this :rules) :fired false))
        true))))

;; FUNCTION: getRulesActions
;; Returns an ArrayList of Strings, where each of them is an action identifier (:actions ["id" ] ===> "id")
(defn -getRulesActions "Returns an ArrayList of Strings, where each of the items is an action identifier"
  [this]
  (try
    (java.util.ArrayList.
      (remove nil?
        (distinct
          (let [rules-map   (common/get-field this :rules)
                values-map  (common/get-field this :values)
                conds-map   (common/get-field this :conds)]
            (apply  concat
                    (for [x (conds-eval/eval-conditions
                              (conds-eval/get-current-conds-map conds-map values-map)
                              values-map)]
                      (if-not (get-in rules-map [x :fired])
                        (do
                          ; rule fired
                          (common/set-field this :rules
                            (common/update-value-in-map rules-map x :fired true))
                          ;; get action(s)
                          (get-in rules-map [x :actions]))
                        (do
                          (logs/log-warning "Rule [" x "] already fired")
                          nil))))))))
    (catch Exception e
      (do (logs/log-exception e) nil))))

;; FUNCTION: getFiredRules
(defn -getFiredRules "Returns an ArrayList of the fired rules"
  [this]
  (java.util.ArrayList.
    (remove nil?
      (let [rules-map (common/get-field this :rules)]
        (for [[k v] rules-map]
          (when (get-in rules-map [k :fired])
            {k v}))))))

@(state. )

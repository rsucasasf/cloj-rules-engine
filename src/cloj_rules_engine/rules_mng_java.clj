(ns cloj-rules-engine.rules-mng-java
  "Rules-engine library used by Java"
  (:use [clojure.math.numeric-tower])
  (:require [cloj-rules-engine.rules-funcs :as rules-funcs]
            [cloj-rules-engine.conds-eval :as conds-eval]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.common :as common]
            [clojure.data.json :as json])
  (:gen-class
    :name cloj_rules_engine.ClojRules
    :state state
    :init init
    :prefix "-"
    :implements [clojure.lang.IDeref]
    :main false
    :methods [[initialize [String] boolean]
              [initializeFromJson [String] boolean]
              [updateMapFacts [clojure.lang.PersistentArrayMap] void]
              [getRulesActions [] java.util.ArrayList]
              [getRulesActionsProbs [] java.util.ArrayList]
              [getFiredRules [] java.util.ArrayList]]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION: get-rule-action-fired
(defn- get-rule-action-fired ""
  [this rules-map k res-rule-actions]
  (common/set-field this :rules
    (common/update-value-in-map rules-map k :action-fired res-rule-actions)))

;; FUNCTION: set-rule-fired
(defn- set-rule-fired ""
  [this x rules-map]
  (common/set-field this :rules
    (common/update-value-in-map rules-map x :fired true)))

;; FUNCTION: get-rule-actions
(defn- get-rule-actions ""
  [x rules-map]
  (get-in rules-map [x :actions]))

;; FUNCTION:-init
;; Set defaults
(defn -init "store fields: values, conds & rules" []
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
  (rules-funcs/get-rules-actions (common/get-field this :rules)
                                 (common/get-field this :values)
                                 (common/get-field this :conds)
                                 #(set-rule-fired this % (common/get-field this :rules))
                                 #(get-rule-actions % (common/get-field this :rules))))

;; FUNCTION: getFiredRules
(defn -getFiredRules "Returns an ArrayList of the fired rules"
  [this]
  (rules-funcs/get-fired-rules (common/get-field this :rules)))

;; FUNCTION: get-rules-actions
;; Returns an ArrayList of Strings, where each of them is an action identifier (:actions [{"id" 0.5}] ===> "id")
;; 1. This function take all rules that are true for the values of ':map-values'
;; 2. For each rule's actions, the function calculates the chances /'get-action-chances') for each action (sorted as defined)
;;    ==> if true, then the action is added to the list
(defn -getRulesActionsProbs ""
  [this]
  (rules-funcs/get-rules-actions-probs  (common/get-field this :rules)
                                        (common/get-field this :values)
                                        (common/get-field this :conds)
                                        #(set-rule-fired this % (common/get-field this :rules))
                                        #(get-rule-action-fired this (common/get-field this :rules) %1 %2)))

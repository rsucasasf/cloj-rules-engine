(ns cloj-rules-engine.rules-mng
  "Rules-engine functions"
  (:use [clojure.math.numeric-tower])
  (:require [cloj-rules-engine.rules-funcs :as rules-funcs]
            [cloj-rules-engine.conds-eval :as conds-eval]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.common :as common]
            [clojure.data.json :as json]))

;; rules map
(def ^:private *rules-map (atom {}))

;; conds-map: created from *rules-map
(def ^:private *conds-map (atom {}))

;; values-map
(def ^:private *values-map (atom {}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION: set-rule-fired
(defn- set-rule-fired ""
  [x]
  (swap! *rules-map assoc-in [x :fired] true))

;; FUNCTION: get-rule-actions
(defn- get-rule-actions ""
  [x]
  (get-in @*rules-map [x :actions]))

;; FUNCTION: get-rule-action-fired
(defn- get-rule-action-fired ""
  [k res-rule-actions]
  (swap! *rules-map assoc-in [k :action-fired] res-rule-actions))

;; FUNCTION: initialize
;; 'rules-file': rules file
(defn initialize "Intializes rules and conditions map"
  [rules-file]
  (if-let [rules-map-content (common/read-content rules-file)]
    (do
      (reset! *rules-map rules-map-content)
      (reset! *conds-map (conds-eval/gen-conds-map @*rules-map))
      true)
    false))

;; FUNCTION: initialize-from-json
;; (json/read-str json-map-str :key-fn keyword)
(defn initialize-from-json "Intializes rules and conditions map from json string"
  [json-map-str]
  (try
    (do
      (reset! *rules-map (json/read-str json-map-str :key-fn keyword))
      (reset! *conds-map (conds-eval/gen-conds-map @*rules-map))
      true)
    (catch Exception e
      (do (logs/log-exception e) false))))

;; FUNCTION: update-map-facts
(defn update-map-facts "Updates values-map (facts) content"
  [map-values]
  (rules-funcs/update-map-facts map-values *rules-map *values-map))

;; FUNCTION: get-fired-rules
(defn get-fired-rules "Returns an ArrayList of the fired rules"
  []
  (rules-funcs/get-fired-rules @*rules-map))

;; FUNCTION: get-rules-actions
(defn get-rules-actions "Returns an ArrayList of Strings, where each of the items is an action identifier"
  []
  (rules-funcs/get-rules-actions @*rules-map @*values-map @*conds-map set-rule-fired get-rule-actions))

;; FUNCTION: get-rules-actions-probs
(defn get-rules-actions-probs "Returns an ArrayList of Strings, where each of the items is an action identifier"
  []
  (rules-funcs/get-rules-actions-probs @*rules-map @*values-map @*conds-map set-rule-fired get-rule-action-fired))

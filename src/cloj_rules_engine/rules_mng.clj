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

;  (let [m (common/parse-m map-values)]
;    (if-not (every? common/valid? m)
;      (do (logs/log-warning "Fact map's values are not valid") false)
;      (do
        ; reset / update facts
;        (reset! *values-map m)
        ; fired set to false
;        (doseq [[k v] @*rules-map]
;          (swap! *rules-map assoc-in [k :fired] false))
;        true))))

;; FUNCTION: get-rules-actions
(defn get-rules-actions "Returns an ArrayList of Strings, where each of the items is an action identifier"
  []
  (rules-funcs/get-rules-actions *rules-map *values-map *conds-map))
;  (try
;    (java.util.ArrayList.
;      (remove nil?
;        (distinct
;          (apply concat (for [x (conds-eval/eval-conditions
;                                  (conds-eval/get-current-conds-map @*conds-map @*values-map)
;                                  @*values-map)]
;                          (if-not (get-in @*rules-map [x :fired])
;                            (do
;                              (swap! *rules-map assoc-in [x :fired] true) ; rule fired
;                              (get-in @*rules-map [x :actions]))
;                            (do
;                              (logs/log-warning "Rule [" x "] already fired")
;                              nil)))))))
;    (catch Exception e
;      (do (logs/log-exception e) nil))))

;; FUNCTION: get-fired-rules
(defn get-fired-rules "Returns an ArrayList of the fired rules"
  []
  (rules-funcs/get-fired-rules *rules-map))
;  (java.util.ArrayList.
;    (remove nil?
;      (for [[k v] (deref *rules-map)]
;        (when (get-in @*rules-map [k :fired])
;          {k v})))))

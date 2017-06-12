(ns cloj-rules-engine.rules-funcs
  "Rules-engine functions"
  (:require [cloj-rules-engine.conds-eval :as conds-eval]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.common :as common]))

;; FUNCTION: update-map-facts
(defn update-map-facts "Updates values-map (facts) content"
  [map-values *rules-map *values-map]
  (let [m (common/parse-m map-values)]
    (if-not (every? common/valid? m)
      (do (logs/log-warning "Fact map's values are not valid") false)
      (do
        ; reset / update facts
        (reset! *values-map m)
        ; fired set to false
        (doseq [[k v] @*rules-map]
          (swap! *rules-map assoc-in [k :fired] false))
        true))))

;; FUNCTION: get-rules-actions
(defn get-rules-actions "Returns an ArrayList of Strings, where each of the items is an action identifier"
  [*rules-map *values-map *conds-map]
  (try
    (java.util.ArrayList.
      (remove nil?
        (distinct
          (apply
            concat
              (for [x (conds-eval/eval-conditions (conds-eval/get-current-conds-map @*conds-map @*values-map) @*values-map)]
                (if-not (get-in @*rules-map [x :fired])
                  (do
                    (swap! *rules-map assoc-in [x :fired] true) ; rule fired
                    (get-in @*rules-map [x :actions]))
                  (do
                    (logs/log-warning "Rule [" x "] already fired")
                    nil)))))))
    (catch Exception e
      (do (logs/log-exception e) nil))))

;; FUNCTION: get-fired-rules
(defn get-fired-rules "Returns an ArrayList of the fired rules"
  [*rules-map]
  (java.util.ArrayList.
    (remove nil?
      (for [[k v] (deref *rules-map)]
        (when (get-in @*rules-map [k :fired])
          {k v})))))

;; FUNCTION: get-rules-actions
;; Returns an ArrayList of Strings, where each of them is an action identifier (:actions [{"id" 0.5}] ===> "id")
;; 1. This function take all rules that are true for the values of ':map-values'
;; 2. For each rule's actions, the function calculates the chances /'get-action-chances') for each action (sorted as defined)
;;    ==> if true, then the action is added to the list
(defn get-rules-actions-probs "Returns an ArrayList of Strings, where each of the items is an action identifier"
  [*rules-map *values-map *conds-map]
  (try
    (java.util.ArrayList.
      (remove nil?
        (distinct
          (apply
            concat
              (for [k (conds-eval/eval-conditions (conds-eval/get-current-conds-map @*conds-map @*values-map) @*values-map)]
                (if-not (get-in @*rules-map [k :fired])
                  (let [rule-actions (get-in @*rules-map [k :actions])
                        res-rule-actions (common/get-action-chances rule-actions)]
                    (if-not (nil? res-rule-actions)
                      (do
                        (swap! *rules-map assoc-in [k :fired] true) ; rule fired
                        (swap! *rules-map assoc-in [k :action-fired] res-rule-actions) ; action fired
                        [res-rule-actions])
                      nil))
                  (do
                    (logs/log-warning "Rule [" k "] already fired")
                    nil)))))))
    (catch Exception e
      (do (logs/log-exception e) nil))))

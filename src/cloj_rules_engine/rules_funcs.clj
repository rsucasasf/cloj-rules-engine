(ns cloj-rules-engine.rules-funcs
  "Rules-engine functions"
  (:require [cloj-rules-engine.conds-eval :as conds-eval]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.common :as common]))

;; FUNCTION: check-facts-map
(defn check-facts-map ""
  [map-values]
  (let [m (common/parse-m map-values)]
    (if-not (every? common/valid? m)
      (do (logs/log-warning "Fact map's values are not valid") false)
      true)))

;; FUNCTION: update-map-facts
(defn update-map-facts "Updates values-map (facts) content"
  [map-values *rules-map *values-map]
  (if (check-facts-map map-values)
    (do
      ; reset / update facts
      (reset! *values-map m)
      ; fired set to false
      (doseq [[k v] @*rules-map]
        (swap! *rules-map assoc-in [k :fired] false))
      true)
    false))


;  (let [m (common/parse-m map-values)]
;    (if-not (every? common/valid? m)
;      (do (logs/log-warning "Fact map's values are not valid") false)
;      (do
;        ; reset / update facts
;        (reset! *values-map m)
;        ; fired set to false
;        (doseq [[k v] @*rules-map]
;          (swap! *rules-map assoc-in [k :fired] false))
;        true))))

;; FUNCTION: get-rules-actions
(defn get-rules-actions "Returns an ArrayList of Strings, where each of the items is an action identifier"
  [*rules-map *values-map *conds-map]
  (try
    (java.util.ArrayList.
      (remove nil?
        (distinct
          (apply concat (for [x (conds-eval/eval-conditions
                                  (conds-eval/get-current-conds-map @*conds-map @*values-map)
                                  @*values-map)]
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

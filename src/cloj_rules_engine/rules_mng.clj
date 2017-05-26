(ns cloj-rules-engine.rules-mng
  "Rules-engine functions"
  (:require [cloj-rules-engine.conds-eval :as conds-eval]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.common :as common]))

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
  (if-let [rules-map-content (common/read-content rules-file)] ; (if-let [value nil] value "Not found")
    (do
      (reset! *rules-map rules-map-content)
      (reset! *conds-map (conds-eval/gen-conds-map @*rules-map))
      true)
    false))

;; FUNCTION: update-map-facts
(defn update-map-facts "Updates values-map (facts) content"
  [m]
  (do
    ; reset / update facts
    (reset! *values-map m)
    ; fired set to false
    (doseq [[k v] @*rules-map]
      (swap! *rules-map assoc-in [k :fired] false))
    true))

;; FUNCTION: get-rules-actions
(defn get-rules-actions "Returns an ArrayList of Strings, where each of the items is an action identifier"
  []
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
  []
  (java.util.ArrayList.
    (remove nil?
      (for [[k v] (deref *rules-map)]
        (when (get-in @*rules-map [k :fired])
          {k v})))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;(initialize "rules.clj")

;(update-map-facts {"#A" "14"})

;(deref *rules-map)
;(deref *values-map)
;(deref *conds-map)



;(get-rules-actions)
;(get-fired-rules)

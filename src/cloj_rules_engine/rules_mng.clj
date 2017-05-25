(ns cloj-rules-engine.rules-mng
  "Rules-engine functions"
  (:require [cloj-rules-engine.conds-eval :as conds-eval]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.common :as common]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PRIVATE FUNCTIONS - VARIABLES - ATOMS - DEFS:

;; rules map
(def ^:private rules-map (atom {}))

;; values-map
(def ^:private values-map (atom {"A" "15"
                                 "B" "46"
                                 "C" "11"}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PUBLIC FUNCTIONS:

;; FUNCTION: initialize
;; 'rules-file': rules file
(defn initialize "Intializes rules-map"
  [rules-file]
  (reset! rules-map (common/read-content rules-file)))

;; FUNCTION: update-map-values
(defn update-map-values ""
  [a b c]
  (do
    (swap! values-map assoc "A" (str a))
    (swap! values-map assoc "B" (str b))
    (swap! values-map assoc "C" (str c))))

;; FUNCTION: get-rules-actions
(defn get-rules-actions ""
  []
  (try
    (java.util.ArrayList.
      (remove nil?
        (distinct
          (apply concat (for [x (conds-eval/eval-conditions (conds-eval/gen-conds-map @rules-map) @values-map)]
                          (get-in @rules-map [x :actions]))))))
    (catch Exception e
      (do (logs/log-exception e) nil))))

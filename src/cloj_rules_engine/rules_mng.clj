(ns cloj-rules-engine.rules-mng
  (:require [cloj-rules-engine.conds-eval :as ce])
  (:use [cloj-rules-engine.common]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PRIVATE FUNCTIONS - VARIABLES - ATOMS - DEFS:

;; rules map
(def ^:private rules-map (atom {}))

;; map - values
(def ^:private map-values (atom {"A" "15"
                                 "B" "46"
                                 "C" "11"}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PUBLIC FUNCTIONS:

;; FUNCTION: initialize
;; 'rules-file': rules file
(defn initialize "Intializes rules-map"
  [rules-file]
  (reset! rules-map (read-content rules-file)))

;; FUNCTION: update-map-values
(defn update-map-values ""
  [a b c]
  (do
    (swap! map-values assoc "A" (str a))
    (swap! map-values assoc "B" (str b))
    (swap! map-values assoc "C" (str c))))

;; FUNCTION: get-rules-actions
(defn get-rules-actions ""
  []
  (java.util.ArrayList.
    (remove nil?
      (distinct
        (apply concat (for [x (ce/eval-conditions (ce/gen-conds-map @rules-map) @map-values)]
                        (get-in (deref rules-map) [x :actions])))))))


(initialize "rules.clj")
(deref rules-map)
(deref map-values)
(get-rules-actions)

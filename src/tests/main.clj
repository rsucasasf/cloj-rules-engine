(ns tests.main
  (:require [cloj-rules-engine.common :as common]))


;; rules map
(def ^:private rules-map (atom {}))

;; values-map
(def ^:private values-map (atom {"A" "15"
                                 "B" "46"
                                 "C" "11"}))

(reset! rules-map (common/read-content "rules.clj"))
(deref rules-map)
(deref values-map)

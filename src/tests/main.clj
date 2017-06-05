(ns tests.main
  (:use [clojure.math.numeric-tower])
  (:require [cloj-rules-engine.rules-mng :as rules-mng]
            [cloj-rules-engine.logs :as logs]))


;; TESTS / MAIN
(if (rules-mng/initialize "rules.clj")
  (when (rules-mng/update-map-facts {"#A" "21", "#B" 43, "#C" 1000, "#LIST1" "[121 321 123 122 1233]"})
    (rules-mng/get-rules-actions))
  false)

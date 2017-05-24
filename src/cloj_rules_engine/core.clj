(ns cloj-rules-engine.core
  (:use [cloj-rules-engine.logs])
  (:require [cloj-rules-engine.common :as cm]
            [cloj-rules-engine.rules-mng :as rules-mng]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(cm/read-content "rules.clj")

(rules-mng/initialize "rules.clj")

(rules-mng/get-rules-actions)

(ns cloj-rules-engine.common-test
  (:require [clojure.test :refer :all]
            [cloj-rules-engine.common :as common]))

(deftest test-01
  (testing "Testing common: "
    (do
      (common/read-content "rules.clj")
      (common/add-new-attr-to-map {:id1 {:val 1} :id2 {:val 3}} :xy "wo")
      (common/update-value-in-map {:id1 {:val 1} :id2 {:val 3}} :id1 :fired true)
      (is true))))

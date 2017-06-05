(ns cloj-rules-engine.rules-mng-test
  (:use [clojure.math.numeric-tower])
  (:require [clojure.test :refer :all]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.rules-mng :as rules-mng]))

(deftest test-01
  (testing "Test (1=1): " (is (= 1 1))))

(deftest test-02
  (testing "Initialize rules: " (is (rules-mng/initialize "rules.clj"))))

(deftest test-03
  (testing "Initialize & check rules: "
    (is
      (if (rules-mng/initialize "rules.clj")
        (when (rules-mng/update-map-facts {"#A" "21", "#B" 43, "#C" 1000, "#LIST1" "[121 321 123 122 1233]"})
          (let [res (rules-mng/get-rules-actions)]
            (logs/log-info "get-rules-actions> " res)
            (logs/log-info ">>>>>>>>>>>>>>>>>> " (> (count res) 0))
            (if (> (count res) 0) true false)))
        false))))

;(run-tests 'cloj-rules-engine.rules-mng-test)
;(run-all-tests)

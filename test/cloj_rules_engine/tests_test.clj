(ns cloj-rules-engine.tests-test
  (:require [clojure.test :refer :all]))

(deftest test-01
  (testing "Test (1=1): " (is (= 1 1))))

(deftest test-02
  (testing "Test (2=2): " (is (= 2 2))))

;(run-tests 'cloj-rules-engine.rules-mng-test)
;(run-all-tests)

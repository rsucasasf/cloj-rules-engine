(ns cloj-rules-engine.rules-mng-test
  (:require [clojure.test :refer :all]
            [cloj-rules-engine.rules-mng :refer :all]))

(deftest test-01
  (testing "It's ok." (is (= 1 1))))

(deftest test-02
  (testing "Initialize rules" (is (initialize "rules.clj"))))

;(run-tests 'cloj-rules-engine.rules-mng-test)
;(run-all-tests)

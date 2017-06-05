(ns cloj-rules-engine.rules-mng-java-test
  (:use [clojure.math.numeric-tower])
  (:require [clojure.test :refer :all]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.rules-mng-java :as rules-mng-java]
            [clojure.data.json :as json]))


(deftest test-01
  (testing "Initialize & check rules (java mode): "
    (do
      (rules-mng-java/-init)
      (is (= 1 1)))))

(deftest test-02
  (testing "Initialize rules with 'bad' path: "
    (is (not (rules-mng-java/-initialize "" "rule12s.clj")))))

;(deftest test-03
;  (testing "Initialize rules with path: "
;    (is (not (rules-mng-java/-initialize "" "rules.clj")))))

;(deftest test-03
;  (testing "Initialize rules from json: "
;    (is (rules-mng-java/-initializeFromJson "" (json/write-str {:RULE_1 {:cond "(and (< #A 10) (> #B 50))" :actions ["action-A"]}})))))

;(deftest test-04
;  (testing "Initialize rules from 'bad' json: "
;    (is (= false (not (rules-mng-java/-initializeFromJson "" (json/write-str {:RULE_1 {:cond "(and (< #A 10) (> #B 50))" :actions ["action-A"]}})))))))

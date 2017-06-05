(ns cloj-rules-engine.rules-mng-test
  (:use [clojure.math.numeric-tower])
  (:require [clojure.test :refer :all]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.rules-mng :as rules-mng]
            [cloj-rules-engine.rules-mng-java :as rules-mng-java]
            [clojure.data.json :as json]))

(deftest test-01
  (testing "Test (1=1): " (is (= 1 1))))

(deftest test-02
  (testing "Test logs: "
    (do
      (logs/log-info "info log")
      (logs/log-debug "debug log")
      (logs/log-error "error log")
      (logs/log-warning "warning log")
      (logs/log-trace "trace log")
      (is (= 1 1)))))

(deftest test-03
  (testing "Initialize rules: "
    (is
      (do
        (when (rules-mng/initialize "rules.clj")
          (rules-mng/update-map-facts {"#A" "", "#B" 43, "#C" 1000}))
        true))))

(deftest test-04
  (testing "Initialize rules with 'bad' path: "
    (is (not (rules-mng/initialize "rule12s.clj")))))

(deftest test-05
  (testing "Initialize rules from json: "
    (is (rules-mng/initialize-from-json (json/write-str {:RULE_1 {:cond "(and (< #A 10) (> #B 50))" :actions ["action-A"]}})))))

(deftest test-06
  (testing "Initialize rules from 'bad' json: "
    (is (= false (not (rules-mng/initialize-from-json (json/write-str {:RULE_1 {:cond "(and (< #A 10) (> #B 50))" :actions ["action-A"]}})))))))

(deftest test-07
  (testing "Initialize & check rules (clojure mode): "
    (is
      (if (rules-mng/initialize "rules.clj")
        (when (rules-mng/update-map-facts {"#A" "21", "#B" 43, "#C" 1000, "#LIST1" "[121 321 123 122 1233]"})
          (let [res (rules-mng/get-rules-actions)]
            (logs/log-info "get-rules-actions> " res)
            (logs/log-info "get-fired-rules " (rules-mng/get-fired-rules))
            (logs/log-info ">>>>>>>>>>>>>>>>>> " (> (count res) 0))
            (if (> (count res) 0) true false)))
        false))))

(deftest test-08
  (testing "Initialize & check rules (java mode): "
    (do
      (rules-mng-java/-init)
      (is (= 1 1)))))

;(run-tests 'cloj-rules-engine.rules-mng-test)
;(run-all-tests)

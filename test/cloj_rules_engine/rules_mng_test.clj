(ns cloj-rules-engine.rules-mng-test
  (:use [clojure.math.numeric-tower])
  (:require [clojure.test :refer :all]
            [cloj-rules-engine.logs :as logs]
            [cloj-rules-engine.rules-mng :as rules-mng]
            [clojure.data.json :as json]))

(deftest test-01
  (testing "Initialize rules: "
    (is
      (do
        (when (rules-mng/initialize "rules.clj")
          (rules-mng/update-map-facts {"#A" "", "#B" 43, "#C" 1000}))
        true))))

(deftest test-02
  (testing "Initialize rules with 'bad' path: "
    (is (not (rules-mng/initialize "rule12s.clj")))))

(deftest test-03
  (testing "Initialize rules with 'bad' path: "
    (is (not (rules-mng/initialize "c://rule12s.clj")))))

(deftest test-04
  (testing "Initialize rules from json: "
    (is (rules-mng/initialize-from-json (json/write-str {:RULE_1 {:cond "(and (< #A 10) (> #B 50))" :actions ["action-A"]}})))))

(deftest test-05
  (testing "Initialize rules from 'bad' json: "
    (is (= false (not (rules-mng/initialize-from-json (json/write-str {:RULE_1 {:cond "(and (< #A 10) (> #B 50))" :actions ["action-A"]}})))))))

(deftest test-06
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

(deftest test-07
  (testing "Initialize & check rules (clojure mode): "
    (is
      (if (rules-mng/initialize "rules.clj")
        (when (rules-mng/update-map-facts {"#A" "21", "#B" 43, "#C" 1000, "#LIST1" "[121 321 123 122 1233]"})
          (do
            (rules-mng/get-rules-actions)
            (rules-mng/get-rules-actions))
            true)
        false))))

(deftest test-08
  (testing "Initialize & check rules (clojure mode, probs): "
    (is
      (if (rules-mng/initialize "rules-probs.clj")
        (if (rules-mng/update-map-facts {"#A" "123", "#B" 43, "#C" 1000})
          (do (rules-mng/get-rules-actions-probs) (rules-mng/get-rules-actions-probs) true)
          false)
        false))))

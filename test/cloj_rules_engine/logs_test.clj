(ns cloj-rules-engine.logs-test
  (:require [clojure.test :refer :all]
            [cloj-rules-engine.logs :as logs]))

(deftest test-01
  (testing "Test logs: "
    (do
      (logs/log-info "info log")
      (logs/log-debug "debug log")
      (logs/log-error "error log")
      (logs/log-warning "warning log")
      (logs/log-trace "trace log")
      (is (= 1 1)))))

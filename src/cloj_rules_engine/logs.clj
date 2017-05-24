(ns cloj-rules-engine.logs
  (:require [clojure.tools.logging :as logging]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; use to check not nil
(def ^:private not-nil? (complement nil?))

;; prefix
(def ^:private logs-prefix " #cloj-rules-engine# ")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PRIVATE FUNCTIONS:

;; FUNCTION: print-log
(defn- print-log
  "prints log content on screen console"
  [debug-level txt]
  {:pre [(not-nil? debug-level) (not-nil? txt)]}
  (cond
    (= debug-level "DEBUG") (logging/debug txt)
    (= debug-level "INFO") (logging/info txt)
    (= debug-level "ERROR") (logging/error txt)
    (= debug-level "WARNING") (logging/warn txt)
    :else (logging/trace txt)))

;; FUNCTION: pr-log
(defn- pr-log
  [l-type & txt]
  (print-log l-type (str logs-prefix (apply str "" txt))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PUBLIC FUNCTIONS:

;; FUNCTION: log-debug
(defn log-debug [& txt] {:pre [(not-nil? txt)]}
  (apply pr-log "DEBUG" txt))

;; FUNCTION: log-info
(defn log-info [& txt] {:pre [(not-nil? txt)]}
  (apply pr-log "INFO" txt))

;; FUNCTION: log-error
(defn log-error [& txt] {:pre [(not-nil? txt)]}
  (apply pr-log "ERROR" txt))

;; FUNCTION: log-warning
(defn log-warning [& txt] {:pre [(not-nil? txt)]}
  (apply pr-log "WARNING" txt))

;; FUNCTION: log-trace
(defn log-trace [& txt] {:pre [(not-nil? txt)]}
  (apply pr-log "TRACE" txt))

;; FUNCTION: log-exception
(defn log-exception "creates a map with Exception info"
  ([e]
   (log-error "> Caught exception: [" (.getMessage e) "], stackTrace: \n    " (clojure.string/join "\n    " (map str (.getStackTrace e)))))
  ([e m]
   (log-error "> [" m "] > Caught exception: [" (.getMessage e) "], stackTrace: \n    " (clojure.string/join "\n    " (map str (.getStackTrace e))))))

;; FUNCTION: get-error-stacktrace
(defn get-error-stacktrace "gets error stacktrace"
  [e] (clojure.string/join "\n    " (map str (.getStackTrace e))))
